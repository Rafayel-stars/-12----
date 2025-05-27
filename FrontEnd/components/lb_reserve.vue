<template>
  <view class="form-wrapper">
    <text class="form-title">空间预约申请表</text>
    <form @submit="handleSubmit">
      <scroll-view class="form-scroll" scroll-y="true">
        <!-- 预约人信息 -->
        <view class="form-item">
          <text class="label">申请空间ID：</text>
          <input
            class="input"
            v-model="formData.spaceId"
            placeholder="请输入申请的空间ID"
            
            :required="true"
          />
        </view>

        <view class="form-item">
          <text class="label">申请人姓名：</text>
          <input
            class="input"
            v-model="formData.applicantName"
            placeholder="请输入申请人姓名" 
            :required="true"
          />
        </view>

        <!-- 预约信息 -->
        <view class="form-item">
          <text class="label">预约日期：</text>
          <uni-datetime-picker
            class="date-picker"
            v-model="formData.appointmentTime"
            type="datetime"
            format="YYYY-MM-DDTHH:mm:ss"
            :clear-icon="false"
            :required="true"
          />
        </view>

        <view class="form-item">
          <text class="label">使用时长（小时）：</text>
          <input
            class="input"
            v-model="formData.duration"
            type="number"
            :required="true"
          />
        </view>

        <view class="form-item">
          <text class="label">申请状态：</text>
          <picker
            class="picker"
            :range="statusOptions"
            @change="handleStatusChange"
            mode="selector"
          >
            <view class="picker-view">{{ statusDisplay }}</view>
          </picker>
        </view>
        
      </scroll-view>

      <!-- 操作按钮 -->
      <view class="button-group">
        <button class="submit-btn" form-type="submit" type="primary">提交申请</button>
        <button class="reset-btn" type="default" @click="handleReset">重置</button>
        <button class="cancel-btn" type="default" @click="handleBack">返回</button>
      </view>
    </form>
  </view>
</template>

<script setup>
import { ref, computed } from 'vue';

const formData = ref({
  spaceId: '',
  applicantName: '',
  appointmentTime: new Date().toISOString(),
  duration: 2,
  status: 0
});

const statusOptions = ['待审核', '已通过', '未通过'];
const loading = ref(false);
const statusDisplay = computed(() => 
  statusOptions[formData.value.status] || '请选择申请状态'
);

// 状态选择处理
const handleStatusChange = (e) => {
  formData.value.status = parseInt(e.detail.value);
};

// 时间格式转换
const formatDate = (dateStr) => {
  return new Date(dateStr).toISOString().replace(/\.\d{3}Z$/, '.000+00:00');
};

// 表单验证
const validateForm = () => {
  if (!formData.value.spaceId) {
    uni.showToast({ title: '空间ID不能为空', icon: 'none' });
    return false;
  }
  if (!formData.value.applicantName) {
    uni.showToast({ title: '申请人姓名不能为空', icon: 'none' });
    return false;
  }
  if (!formData.value.appointmentTime) {
    uni.showToast({ title: '预约时间不能为空', icon: 'none' });
    return false;
  }
  if (formData.value.duration <= 0) {
    uni.showToast({ title: '使用时长必须大于0', icon: 'none' });
    return false;
  }
  return true;
};

const handleUnauthorized = () => {
  uni.showToast({ title: '登录已过期，请重新登录', icon: 'none' });
  uni.removeStorageSync('token');
  uni.removeStorageSync('user');
  uni.reLaunch({ url: '/pages/lb_login/lb_login' });
};

// 提交处理
const handleSubmit = () => {
  if (!validateForm()) return;

  const token = uni.getStorageSync('token');
  if (!token) {
    uni.showToast({ title: '请先登录', icon: 'none' });
    return uni.reLaunch({ url: '/pages/lb_login/lb_login' });
  }

  const submitData = {
    spaceId: formData.value.spaceId,
    applicantName: formData.value.applicantName,
    appointmentTime: formatDate(formData.value.appointmentTime),
    duration: formData.value.duration,
    status: formData.value.status
  };

  loading.value = true;
  uni.showLoading({ title: '提交中...' });

  uni.request({
    url: 'http://localhost:9000/reader/space/application/add',
    method: 'POST',
    header: {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    },
    data: submitData,
    success: (res) => {
      if (res.statusCode === 200) {
        uni.showToast({
          title: '预约申请提交成功',
          success: () => {
            setTimeout(() => uni.navigateBack(), 1500);
          }
        });
        handleReset();
      } else if (res.statusCode === 401) {
        handleUnauthorized();
      } else {
        uni.showToast({
          title: res.data?.message || '提交失败',
          icon: 'none'
        });
      }
    },
    fail: () => {
      uni.showToast({ title: '网络请求失败', icon: 'none' });
    },
    complete: () => {
      loading.value = false;
      uni.hideLoading();
    }
  });
};

// 重置表单
const handleReset = () => {
  formData.value = {
    spaceId: '',
    applicantName: '',
    appointmentTime: new Date().toISOString(),
    duration: 2,
    status: 0
  };
};

// 返回操作
const handleBack = () => {
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