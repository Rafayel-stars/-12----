<template>
  <view class="form-wrapper">
    <text class="form-title">空间预约申请表</text>
    <form @submit="handleSubmit">
      <scroll-view class="form-scroll" scroll-y="true">
        <!-- 预约人信息 -->
        <view class="form-item">
          <text class="label">预约人姓名：</text>
          <input
            class="input"
            v-model="formData.applicant"
            placeholder="请输入真实姓名"
            :required="true"
          />
        </view>

        <view class="form-item">
          <text class="label">联系电话：</text>
          <input
            class="input"
            v-model="formData.phone"
            placeholder="请输入手机号码"
            type="number"
            maxlength="11"
            :required="true"
          />
        </view>

        <!-- 预约信息 -->
        <view class="form-item">
          <text class="label">预约日期：</text>
          <uni-datetime-picker
            class="date-picker"
            v-model="formData.date"
            type="date"
            :clear-icon="false"
            :start="new Date().getTime()"
            :required="true"
          />
        </view>

        <view class="form-item">
          <text class="label">时段选择：</text>
          <picker
            class="picker"
            :range="timeSlots"
            @change="handleTimeChange"
            mode="selector"
          >
            <view class="picker-view">{{ formData.timeSlot || '请选择时段' }}</view>
          </picker>
        </view>

        <view class="form-item">
          <text class="label">空间类型：</text>
          <picker
            class="picker"
            :range="spaceTypes"
            @change="handleSpaceChange"
            mode="selector"
          >
            <view class="picker-view">{{ formData.spaceType || '请选择空间类型' }}</view>
          </picker>
        </view>

        <view class="form-item">
          <text class="label">使用人数：</text>
          <input
            class="input"
            v-model="formData.peopleNum"
            placeholder="请输入人数(1-10人)"
            type="number"
            :required="true"
          />
        </view>

        <!-- 附加信息 -->
        <view class="form-item">
          <text class="label">使用目的：</text>
          <textarea
            class="textarea"
            v-model="formData.purpose"
            placeholder="请输入使用目的"
            :auto-height="true"
            :required="true"
          />
        </view>

        <view class="form-item">
          <text class="label">特殊需求：</text>
          <textarea
            class="textarea"
            v-model="formData.requirements"
            placeholder="可填写设备需求或其他要求"
            :auto-height="true"
          />
        </view>
      </scroll-view>

      <!-- 操作按钮 -->
      <view class="button-group">
        <button class="submit-btn" form-type="submit" type="primary">提交申请</button>
        <button class="cancel-btn" type="default" @click="handleCancel">取消</button>
        <button class="back-btn" type="default" @click="handleBack">返回</button>
      </view>
    </form>
  </view>
</template>

<script setup>
import { ref } from 'vue';

const formData = ref({
  applicant: '',
  phone: '',
  date: '',
  timeSlot: '',
  spaceType: '',
  peopleNum: '',
  purpose: '',
  requirements: ''
});

const timeSlots = [
  '08:00-10:00',
  '10:00-12:00',
  '13:00-15:00',
  '15:00-17:00',
  '18:00-20:00'
];

const spaceTypes = [
  '研讨室A（4人）',
  '研讨室B（6人）',
  '多媒体室（10人）',
  '开放学习区',
  '影音室'
];

const handleTimeChange = (e) => {
  formData.value.timeSlot = timeSlots[e.detail.value];
};

const handleSpaceChange = (e) => {
  formData.value.spaceType = spaceTypes[e.detail.value];
};

const handleSubmit = () => {
  // 验证逻辑
  if (!validateForm()) return;

  uni.showLoading({ title: '提交中...' });
  // 实际提交逻辑
  setTimeout(() => {
    uni.hideLoading();
    uni.showToast({ title: '预约成功' });
    resetForm();
  }, 1500);
};

const validateForm = () => {
  if (!formData.value.applicant) {
    uni.showToast({ title: '请填写姓名', icon: 'none' });
    return false;
  }
  if (!/^1[3-9]\d{9}$/.test(formData.value.phone)) {
    uni.showToast({ title: '手机号格式错误', icon: 'none' });
    return false;
  }
  if (!formData.value.date) {
    uni.showToast({ title: '请选择日期', icon: 'none' });
    return false;
  }
  if (!formData.value.timeSlot) {
    uni.showToast({ title: '请选择时段', icon: 'none' });
    return false;
  }
  if (formData.value.peopleNum < 1 || formData.value.peopleNum > 10) {
    uni.showToast({ title: '人数需在1-10之间', icon: 'none' });
    return false;
  }
  return true;
};

const resetForm = () => {
  formData.value = {
    applicant: '',
    phone: '',
    date: '',
    timeSlot: '',
    spaceType: '',
    peopleNum: '',
    purpose: '',
    requirements: ''
  };
};

const handleCancel = () => {
  resetForm();
};

const handleBack = () => {
  uni.navigateTo({
  	url:'/pages/lb_index/lb_index'
  })
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
      transition: all 0.3s;

      &.submit-btn {
        background-color: #28a745 !important;
      }
      
      &.cancel-btn {
        background-color: #dc3545 !important;
        color: white !important;
      }
      
      // &.back-btn {
      //   background-color: #6c757d !important;
      //   color: white !important;
      // }

      &:active {
        opacity: 0.8;
      }
    }
  }
}
</style>