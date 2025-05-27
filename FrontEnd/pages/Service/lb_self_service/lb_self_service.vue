<template>
  <view class="container">
    <!-- 表单选择区域 -->
    <view class="form-select">
      <label>选择要生成二维码的借阅记录：</label>
      <picker 
        :range="formDataRef" 
        range-key="displayName" 
        @change="(e) => onFormChange(e.detail.value)"
      >
        <view class="picker">
          当前选择：{{ formDataRef[currentFormIndex]?.displayName || '暂无数据' }}
        </view>
      </picker>
    </view>

    <!-- 二维码展示区域 -->
    <view class="qrcode-wrap" v-if="formDataRef.length > 0">
      <view class="qrimg">
        <tki-qrcode
          ref="qrcode"
          :cid="qrform.cid"
          :val="qrform.val"
          :size="qrform.size"
          :unit="qrform.unit"
          :background="qrform.background"
          :foreground="qrform.foreground"
          :pdground="qrform.pdground"
          :icon="qrform.icon"
          :iconSize="qrform.iconsize"
          :lv="qrform.lv"
          :onval="qrform.onval"
          :loadMake="qrform.loadMake"
          :usingComponents="qrform.usingComponents"
          :showLoading="qrform.showLoading"
          :loadingText="qrform.loadingText"
          @result="qrR"
        />
      </view>
      
      <!-- 借阅信息展示 -->
      <view class="info-wrap">
        <view class="info-item">图书ID：{{ currentRecord.bookId }}</view>
        <view class="info-item">借阅时间：{{ formatDate(currentRecord.borrowTime) }}</view>
        <view class="info-item">借阅状态：{{ formatStatus(currentRecord.status) }}</view>
        <view class="info-item">管理员ID：{{ currentRecord.adminId }}</view>
      </view>
    </view>
    <view v-else class="empty-tip">暂无借阅记录</view>

    <button @click="handleBack()">返回</button>
  </view>
</template>

<script setup lang="ts">
import tkiQrcode from '../../../node_modules/tki-qrcode/components/tki-qrcode/tki-qrcode.vue'
import { ref, reactive, onMounted, nextTick } from 'vue'

// 声明qrcode ref
const qrcode = ref<InstanceType<typeof tkiQrcode> | null>(null)
const currentFormIndex = ref(0)
const currentRecord = ref<any>({})

// 用户信息
const user = uni.getStorageSync('user')
const token = uni.getStorageSync('token')

// 从接口获取的表单数据
const formDataRef = ref<Array<any>>([])

// 二维码配置
const qrform = reactive({
  cid: 'tki-qrcode-canvas',
  val: '',
  size: 400,
  unit: 'upx',
  background: '#ffffff',
  foreground: '#000000',
  pdground: '#000000',
  icon: '',
  iconsize: 60,
  lv: 3,
  onval: true,
  loadMake: false,
  usingComponents: false,
  showLoading: true,
  loadingText: '生成中...'
})

// 日期格式化函数
const formatDate = (isoString: string) => {
  if (!isoString) return '未知日期'
  try {
    const date = new Date(isoString)
    return `${date.getFullYear()}-${(date.getMonth()+1).toString().padStart(2,'0')}-${date.getDate().toString().padStart(2,'0')}`
  } catch {
    return '日期格式错误'
  }
}

// 状态格式化函数
const formatStatus = (status: number) => {
  const statusMap: { [key: number]: string } = {
    1: '借阅中',
    2: '已归还',
    3: '逾期'
  }
  return statusMap[status] || '未知状态'
}

// 获取借阅记录
const fetchFormData = async () => {
  try {
    uni.showLoading({ title: '加载中...' })
    
    const res = await uni.request({
      url: 'http://localhost:9000/reader/borrow/records',
      method: 'GET',
      header: {
        'Authorization': `Bearer ${token}`
      },
      data: {
        borrower_name: user.no
      }
    })

    if (res.statusCode === 200) {
      formDataRef.value = (res.data as any[]).map(item => ({
        ...item,
        displayName: `${item.bookId}（${formatDate(item.borrowTime)}）`
      }))
      
      if (formDataRef.value.length > 0) {
        currentRecord.value = formDataRef.value[0]
        onFormChange(0)
      }
    }
  } catch (error) {
    console.error('获取借阅记录失败:', error)
    uni.showToast({
      title: '数据加载失败',
      icon: 'none'
    })
  } finally {
    uni.hideLoading()
  }
}

// 处理表单选择变化
const onFormChange = async (index: number) => {
  try {
    currentFormIndex.value = index
    const selectedForm = formDataRef.value[index]
    currentRecord.value = selectedForm

    // 构造二维码内容
    const params = new URLSearchParams({
      recordId: selectedForm.id,
      bookId: selectedForm.bookId,
      borrowTime: selectedForm.borrowTime,
      adminId: selectedForm.adminId,
      borrowerName: selectedForm.borrowerName,
      status: selectedForm.status.toString()
    }).toString()

    const basePath = process.env.NODE_ENV === 'h5' 
      ? window.location.origin 
      : '/pages/Service/lb_self_service/lb_self_serviceNotice'

    qrform.val = `${basePath}?${params}`

    await nextTick()
    if (qrcode.value) {
      qrcode.value._makeCode()
    }
  } catch (error) {
    console.error('生成二维码失败:', error)
    uni.showToast({
      title: '生成失败，请重试',
      icon: 'none'
    })
  }
}

// 监听二维码生成结果
const qrR = (res: string) => {
  if (res.startsWith('data:image')) {
    uni.showToast({
      title: '生成成功',
      icon: 'success'
    })
  }
}

const handleBack = () => {
  uni.navigateBack()
}

// 组件挂载时加载数据
onMounted(() => {
  if (user && token) {
    fetchFormData()
  } else {
    uni.showToast({
      title: '请先登录',
      icon: 'none'
    })
    setTimeout(() => {
      uni.navigateTo({ url: '/pages/login/login' })
    }, 1500)
  }
})
</script>

<style scoped lang="scss">
.container {
  padding: 20rpx;

  .form-select {
    margin-bottom: 30rpx;
    padding: 20rpx;
    background: #f5f5f5;
    border-radius: 12rpx;

    .picker {
      padding: 15rpx;
      background: white;
      border-radius: 8rpx;
      margin-top: 10rpx;
      color: #666;
    }
  }

  .qrcode-wrap {
    display: flex;
    flex-direction: column;
    align-items: center;
    padding: 30rpx;
    background: #fff;
    border-radius: 16rpx;
    box-shadow: 0 4rpx 12rpx rgba(0,0,0,0.1);
    margin-bottom: 30rpx;

    .info-wrap {
      margin-top: 30rpx;
      width: 100%;
      padding: 20rpx;
      background: #f8f8f8;
      border-radius: 12rpx;

      .info-item {
        margin: 15rpx 0;
        color: #666;
        font-size: 28rpx;
      }
    }
  }

  .empty-tip {
    text-align: center;
    color: #888;
    padding: 40rpx;
  }

  button {
    margin-top: 30rpx;
    background-color: #007AFF;
    color: white;
    border-radius: 8rpx;
    padding: 20rpx 40rpx;
  }
}
</style>