<template>
  <view class="container">
    <!-- 表单选择区域 -->
    <view class="form-select">
      <label>选择要生成二维码的表单：</label>
      <picker 
        :range="formDataRef" 
        range-key="name" 
        @change="(e) => onFormChange(e.detail.value)"
      >
        <view class="picker">
          当前选择：{{ formDataRef[currentFormIndex].name }}
        </view>
      </picker>
    </view>

    <!-- 二维码展示区域 -->
    <view class="qrcode-wrap">
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
    </view>
	
	<button @click="handleBack()">返回</button>
  </view>
</template>

<script setup lang="ts">
import tkiQrcode from '../../../node_modules/tki-qrcode/components/tki-qrcode/tki-qrcode.vue'
import { ref, reactive, nextTick } from 'vue'

// 声明qrcode ref
const qrcode = ref<InstanceType<typeof tkiQrcode> | null>(null)

// 模拟从后端获取的表单数据
const formDataRef = ref([
  { 
    id: 1,
    name: '图书借阅表',
    bookName: 'JavaScript高级程序设计',
    borrower: '张三',
    borrowDate: '2023-10-01',
    dueDate: '2023-11-01'
  },
  {
    id: 2,
    name: '图书归还表',
    bookName: 'Vue.js设计与实现',
    borrower: '李四',
    returnDate: '2023-10-15',
    fine: 0
  }
])

const currentFormIndex = ref(0)

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

// 处理表单选择变化
const onFormChange = async (index: number) => {
  try {
    currentFormIndex.value = index
    const selectedForm = formDataRef.value[index]

    // 手动拼接 query 字符串，保持中文不编码
    const params = Object.entries({
      formType: selectedForm.name,
      ...selectedForm
    }).map(([key, value]) => `${key}=${value}`).join('&')

    // 根据运行环境设置基础路径
    const basePath = process.env.NODE_ENV === 'h5' 
      ? window.location.origin 
      : '/pages/Service/lb_self_service/lb_self_serviceNotice'

    qrform.val = `${basePath}?${params}`

    // 确保组件加载后生成二维码
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
  console.log('二维码生成结果：', res)
  if (res.startsWith('data:image')) {
    uni.showToast({
      title: '生成成功',
      icon: 'success'
    })
  }
}

const handleBack = () =>{
	uni.navigateBack()
}
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
    justify-content: center;
    padding: 30rpx;
    background: #fff;
    border-radius: 16rpx;
    box-shadow: 0 4rpx 12rpx rgba(0,0,0,0.1);
  }
}
</style>
