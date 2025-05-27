<template>
  <view class="form-wrapper">
    <!-- 表单标题 -->
    <text class="form-title">借阅登记记录表</text>
    
    <!-- 借阅表单 -->
    <form @submit="handleSubmit">
      <scroll-view class="form-scroll" scroll-y="true">
        <!-- 借阅基本信息 -->

        <view class="form-item">
          <text class="label">图书ID：</text>
          <input
            class="input"
            v-model="formData.bookId"
            placeholder="请输入图书ID"
            :required="true"
          />
        </view>

        <view class="form-item">
          <text class="label">借阅人姓名：</text>
          <input
            class="input"
            v-model="formData.borrowerName"
            placeholder="请输入借阅人姓名"
            :required="true"
          />
        </view>

        <!-- 时间选择 -->
        <view class="form-item">
          <text class="label">借阅日期：</text>
          <uni-datetime-picker
            class="date-picker"
            v-model="formData.borrowTime"
            type="datetime"
            :clear-icon="false"
            :required="true"
          />
        </view>

        <view class="form-item">
          <text class="label">归还日期：</text>
          <uni-datetime-picker
            class="date-picker"
            v-model="formData.returnTime"
            type="datetime"
            :clear-icon="false"
            :required="true"
          />
        </view>

        <!-- 状态选择 -->
        <view class="form-item">
          <text class="label">借阅状态：</text>
          <picker
            class="picker"
            :range="statusOp"
            @change="handleStatusChange"
            mode="selector"
          >
            <view class="picker-view">{{ statusDisplay }}</view>
          </picker>
        </view>


        <view class="form-item">
          <text class="label">操作员ID：</text>
          <input
            class="input"
            v-model="formData.adminId"
            placeholder="请输入操作员ID"
          />
        </view>

      </scroll-view>

      <!-- 操作按钮 -->
      <view class="button-group">
        <button
          class="submit-btn"
          form-type="submit"
          type="primary"
        >提交登记</button>
        <button
          class="reset-btn"
          type="default"
          @click="handleReset"
        >重置</button>
        <button
          class="cancel-btn"
          type="default"
          @click="handleCancel"
        >取消</button>
      </view>
    </form>
  </view>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue';

// 表单数据
const formData = ref({
  bookId: 0,
  borrowerName: '',
  borrowTime: '',
  returnTime: '',
  status: 1,
  adminId: 0
});

const statusOp = ['已借', '可借']
const loading = ref(true)
const statusDisplay = computed(() =>
  statusOp[formData.value.status] || '请选择借阅状态'
)

// 状态选择处理（修正索引获取方式）
const handleStatusChange = (e) => {
  formData.value.status = parseInt(e.detail.value); // 获取选中项的索引
};

// 表单提交验证
const validateForm = () => {
  if (!formData.value.borrowerName) {
    uni.showToast({ title: '借阅人姓名不能为空', icon: 'none' });
    return false;
  }
  if (!formData.value.borrowTime) {
    uni.showToast({ title: '借阅时间不能为空', icon: 'none' });
    return false;
  }
  if (formData.value.returnTime < formData.value.borrowTime) {
    uni.showToast({ title: '应还日期不能早于借阅日期', icon: 'none' });
    return false;
  }
  if (formData.value.status === '已归还' && !formData.value.returnTime) {
    uni.showToast({ title: '归还状态必须填写归还日期', icon: 'none' });
    return false;
  }
  return true;
};

// 生命周期钩子（保持不变）
onMounted(() => {
  const user = uni.getStorageSync('user');
  if (user.userType!== 'admin') {
    uni.showToast({ title: '无权限访问', icon: 'none' });
    setTimeout(() => uni.navigateBack(), 1500);
  }
});

// 时间格式转换函数
const formatDate = (dateStr) => {
  const date = new Date(dateStr);
  const year = date.getFullYear();
  const month = String(date.getMonth() + 1).padStart(2, '0');
  const day = String(date.getDate()).padStart(2, '0');
  const hours = String(date.getHours()).padStart(2, '0');
  const minutes = String(date.getMinutes()).padStart(2, '0');
  const seconds = String(date.getSeconds()).padStart(2, '0');
  return `${year}-${month}-${day}T${hours}:${minutes}:${seconds}.000+00:00`;
};

// 提交处理
const handleSubmit = () => {
  if (!validateForm()) return;

  // 获取token
  const token = uni.getStorageSync('token');
  if (!token) {
    uni.showToast({ title: '请先登录', icon: 'none' });
    return uni.reLaunch({ url: '/pages/lb_login/lb_login' });
  }

  // 准备提交数据（根据接口需求调整字段）
  const submitData = {
    bookId: formData.value.bookId,
    borrowerName: formData.value.borrowerName,
    borrowTime: formatDate(formData.value.borrowTime), // 转换借阅时间格式
    returnTime: formData.value.returnTime? formatDate(formData.value.returnTime) : null, // 转换归还时间格式
    status: formData.value.status === 0? 0 : 1,
    adminId: formData.value.adminId
  };
  console.log('准备提交的数据', submitData)
  loading.value = true;

  // 发送请求
  uni.request({
    url: 'http://localhost:9000/admin/borrow/add',
    method: 'POST',
    header: {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    },
    data: submitData,
    success: (response) => {
      if (response.statusCode === 200) {
        uni.showToast({
          title: '借阅记录添加成功',
          success: () => {
            setTimeout(() => uni.navigateBack(), 1500);
          }
        });
      } else if (response.statusCode === 401) {
        handleUnauthorized();
      } else if (response.statusCode === 403) {
        uni.showToast({
          title: '图书ID或操作人ID不存在或输入格式有误',
          icon: 'fail'
        });
      } else {
        uni.showToast({
          title: response.data?.message || '添加借阅记录失败',
          icon: 'none'
        });
      }
    },
    fail: (error) => {
      uni.showToast({
        title: '网络请求失败，请检查连接',
        icon: 'none'
      });
    },
    complete: () => {
      loading.value = false;
    }
  });
};

// 重置表单
const handleReset = () => {
  formData.value = {
    bookId: 0,
    borrowerName: '',
    borrowTime: '',
    returnTime: '',
    status: 1,
    adminId: 0
  };
};

// 取消操作
const handleCancel = () => {
  uni.navigateBack();
};
</script>

<style lang="scss" scoped>
.form-wrapper {
  padding: 30rpx;
  background-color: #fff;
  border-radius: 12rpx;
  margin: 30rpx auto;
  width: 1600rpx;
  height: 2300rpx;
  max-width: 1800rpx;
  box-shadow: 0 4rpx 12rpx rgba(0, 0, 0, 0.1);

  .form-scroll {
    max-height: 50vh;
    max-width: 80vw;
  }

  .form-title {
    display: block;
    text-align: center;
    font-size: 72rpx;
    color: #333;
    font-weight: bold;
    margin-bottom: 40rpx;
  }

  .form-item {
    margin-bottom: 30rpx;

    .label {
      font-size: 50rpx;
      color: #333;
      margin-bottom: 15rpx;
      font-weight: bold;
    }

    .input, .textarea {
      padding: 25rpx;
      border: 2rpx solid #ddd;
      border-radius: 12rpx;
      font-size: 40rpx;
      background-color: #fff;
    }

    .textarea {
      min-height: 200rpx;
      width: 1500rpx;
    }

    .picker-view {
      padding: 25rpx;
      background-color: #fff;
      border: 2rpx solid #ddd;
      border-radius: 12rpx;
      color: #666;
      font-size: 40rpx;
    }

    .date-picker {
      ::v-deep .uni-date__x-input {
        padding: 25rpx!important;
      }
    }
  }

  .button-group {
    margin-top: 40rpx;
    display: flex;
    gap: 20rpx;

    button {
      flex: 1;
      padding: 25rpx 0;
      font-size: 40rpx;
      border-radius: 12rpx;
      
      &.submit-btn {
        background-color: #28a745!important;
      }
      
      &.reset-btn {
        background-color: #ffc107!important;
        color: #333!important;
      }
      
      &.cancel-btn {
        background-color: #dc3545!important;
        color: white!important;
      }
    }
  }
}
</style>