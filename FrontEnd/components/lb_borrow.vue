<template>
  <view class="form-wrapper">
    <!-- 表单标题 -->
    <text class="form-title">借阅登记记录表</text>
    
    <!-- 借阅表单 -->
    <form @submit="handleSubmit">
      <scroll-view class="form-scroll" scroll-y="true">
        <!-- 借阅基本信息 -->

        <view class="form-item">
          <text class="label">图书馆编码：</text>
          <input
            class="input"
            v-model="formData.libraryCode"
            placeholder="请输入图书馆编码"
            :required="true"
          />
        </view>

        <view class="form-item">
          <text class="label">借阅人编号：</text>
          <input
            class="input"
            v-model="formData.borrowerNo"
            placeholder="请输入借阅人编号"
            :required="true"
          />
        </view>

        <!-- 时间选择 -->
        <view class="form-item">
          <text class="label">借阅日期：</text>
          <uni-datetime-picker
            class="date-picker"
            v-model="formData.borrowDate"
            type="datetime"
            :clear-icon="false"
            :required="true"
          />
        </view>

        <view class="form-item">
          <text class="label">归还日期：</text>
          <uni-datetime-picker
            class="date-picker"
            v-model="formData.expectedReturnDate"
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
            :range="statusOptions"
            @change="handleStatusChange"
            mode="selector"
          >
            <view class="picker-view">{{ statusDisplay }}</view>
          </picker>
        </view>

        <view class="form-item">
          <text class="label">审核人编号：</text>
          <input
            class="input"
            v-model="formData.reviewerNo"
            placeholder="请输入审核人编号"
            :required="true"
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

// 表单数据 - 使用驼峰命名法
const formData = ref({
  libraryCode: '',
  borrowerNo: '',
  borrowDate: '',
  expectedReturnDate: '',
  status: 0,
  reviewerNo: ''
});

// 状态选项 - 与数据库tinyint值对应
const statusOptions = ['待审核', '已借出', '已归还', '已拒绝'];
const loading = ref(true);

// 计算属性 - 显示状态文本
const statusDisplay = computed(() =>
  statusOptions[formData.value.status] || '请选择状态'
);

// 状态选择处理
const handleStatusChange = (e) => {
  formData.value.status = parseInt(e.detail.value);
};

// 表单验证
const validateForm = () => {
  if (!formData.value.libraryCode) {
    uni.showToast({ title: '图书馆编码不能为空', icon: 'none' });
    return false;
  }
  if (!formData.value.borrowerNo) {
    uni.showToast({ title: '借阅人编号不能为空', icon: 'none' });
    return false;
  }
  if (!formData.value.borrowDate) {
    uni.showToast({ title: '借阅日期不能为空', icon: 'none' });
    return false;
  }
  if (!formData.value.expectedReturnDate) {
    uni.showToast({ title: '预计归还日期不能为空', icon: 'none' });
    return false;
  }
  if (new Date(formData.value.expectedReturnDate) <= new Date(formData.value.borrowDate)) {
    uni.showToast({ title: '预计归还日期必须晚于借阅日期', icon: 'none' });
    return false;
  }
  if (!formData.value.reviewerNo) {
    uni.showToast({ title: '审核人编号不能为空', icon: 'none' });
    return false;
  }
  return true;
};

// 生命周期钩子
onMounted(() => {
  const user = uni.getStorageSync('user');
  if (user.userType!== 'user') {
    uni.showToast({ title: '无权限访问', icon: 'none' });
    setTimeout(() => uni.navigateBack(), 1500);
  }
});

// 日期格式化函数
const formatDate = (dateStr) => {
  if (!dateStr) return null;
  const date = new Date(dateStr);
  return date.toISOString();
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

  // 准备提交数据
  const submitData = {
    libraryCode: formData.value.libraryCode,
    borrowerNo: formData.value.borrowerNo,
    borrowDate: formatDate(formData.value.borrowDate),
    expectedReturnDate: formatDate(formData.value.expectedReturnDate),
    status: formData.value.status,
    reviewerNo: formData.value.reviewerNo
  };

  console.log('准备提交的数据', submitData);
  loading.value = true;

  // 发送请求
  uni.request({
    url: 'http://localhost:9000/reader/borrowapplication/submit',
    method: 'POST',
    header: {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    },
    data: submitData,
    success: (response) => {
      if (response.statusCode === 200) {
        uni.showToast({
          title: '借阅申请提交成功',
          icon: 'success',
          success: () => {
            setTimeout(() => uni.navigateBack(), 1500);
          }
        });
      } else if (response.statusCode === 401) {
        uni.showToast({ title: '登录状态已过期，请重新登录', icon: 'none' });
        setTimeout(() => uni.reLaunch({ url: '/pages/lb_login/lb_login' }), 1500);
      } else {
        uni.showToast({
          title: response.data?.message || '提交借阅申请失败',
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
    libraryCode: '',
    borrowerNo: '',
    borrowDate: '',
    expectedReturnDate: '',
    status: 0,
    reviewerNo: ''
  };
};

// 取消操作
const handleCancel = () => {
  uni.navigateBack();
};
</script>

<style lang="scss" scoped>
// 样式保持不变
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