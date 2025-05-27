<template>
  <view class="form-wrapper">
    <!-- 表单标题 -->
    <text class="form-title">空间预约申请表</text>
    
    <!-- 预约表单 -->
    <form @submit="handleSubmit">
      <scroll-view class="form-scroll" scroll-y="true">
        <!-- 第一行 -->
        <view class="form-row">
          <view class="form-item">
            <text class="label">空间编号：</text>
            <input
              class="input"
              v-model="formData.spaceId"
              placeholder="例：R3F-202"
              maxlength="10"
              :required="true"
            />
          </view>

          <view class="form-item">
            <text class="label">容纳人数：</text>
            <input
              class="input"
              v-model="formData.capacity"
              placeholder="请输入人数"
              type="number"
              :required="true"
            />
          </view>
        </view>

        <!-- 第二行 -->
        <view class="form-row">
          <view class="form-item">
            <text class="label">空间类型：</text>
            <picker
              class="picker"
              :range="spaceTypeOptions"
              @change="handleTypeChange"
            >
              <view class="picker-view">{{ formData.spaceType || '请选择类型' }}</view>
            </picker>
          </view>

          <view class="form-item">
            <text class="label">所在楼层：</text>
            <input
              class="input"
              v-model="formData.floor"
              placeholder="例：3F"
              :required="true"
            />
          </view>
        </view>

        <!-- 时间选择组 -->
        <view class="form-item">
          <text class="label">开放时段：</text>
          <view class="time-group">
            <uni-datetime-picker
              class="time-picker"
              v-model="formData.openTime"
              type="time"
              placeholder="开始时间"
            />
            <text class="time-separator">至</text>
            <uni-datetime-picker
              class="time-picker"
              v-model="formData.closeTime"
              type="time"
              placeholder="结束时间"
            />
          </view>
        </view>

        <!-- 预约日期 -->
        <view class="form-item">
          <text class="label">预约日期：</text>
          <uni-datetime-picker
            class="date-picker"
            v-model="formData.reserveDate"
            type="date"
            :clear-icon="false"
            :required="true"
          />
        </view>

        <!-- 设备选择 -->
        <view class="form-item">
          <text class="label">设备需求：</text>
          <view class="equipment-group">
            <checkbox-group @change="handleEquipmentChange">
              <label 
                v-for="item in equipmentOptions" 
                :key="item"
                class="equipment-item"
              >
                <checkbox :value="item" class="blue-checkbox" />
                <text>{{ item }}</text>
              </label>
            </checkbox-group>
          </view>
        </view>

        <!-- 申请人信息 -->
        <view class="form-item">
          <text class="label">申请人：</text>
          <input
            class="input"
            v-model="formData.applicant"
            placeholder="请输入姓名"
            :required="true"
          />
        </view>

        <!-- 联系方式 -->
        <view class="form-item">
          <text class="label">联系电话：</text>
          <input
            class="input"
            v-model="formData.phone"
            placeholder="11位手机号"
            type="number"
            maxlength="11"
          />
        </view>

        <!-- 使用说明 -->
        <view class="form-item">
          <text class="label">使用说明：</text>
          <textarea
            class="textarea"
            v-model="formData.usageDesc"
            placeholder="请输入详细使用说明"
            :auto-height="true"
          />
        </view>
      </scroll-view>

      <!-- 操作按钮 -->
      <view class="button-group">
        <button class="submit-btn" form-type="submit">立即预约</button>
        <button class="reset-btn" @click="handleReset">重置表单</button>
        <button class="cancel-btn" @click="handleCancel">取消返回</button>
      </view>
    </form>
  </view>
</template>

<script setup>
import { ref } from 'vue';

const formData = ref({
  spaceId: '',
  capacity: '',
  spaceType: '',
  floor: '',
  openTime: '',
  closeTime: '',
  reserveDate: '',
  equipment: [],
  applicant: '',
  phone: '',
  usageDesc: ''
});

const spaceTypeOptions = ['研讨室', '会议室', '影音室', '自习区'];
const equipmentOptions = ['投影仪', '白板', '视频会议', '电脑', '打印机'];

const handleTypeChange = (e) => {
  formData.value.spaceType = spaceTypeOptions[e.detail.value];
};

const handleEquipmentChange = (e) => {
  formData.value.equipment = e.detail.value;
};
const handleCancel = () =>{
	uni.navigateBack()
}

// 保持原有验证逻辑和事件处理...
</script>

<style lang="scss" scoped>
.form-wrapper {
  padding: 40rpx;
  margin: 30rpx auto;
  width: 1600rpx;
  background: #fff;
  border-radius: 12rpx;
  box-shadow: 0 4rpx 12rpx rgba(0,0,0,0.1);

  .form-title {
    display: block;
    text-align: center;
    font-size: 72rpx;
    color: #1890ff;
    font-weight: bold;
    margin-bottom: 60rpx;
  }

  .form-row {
    display: flex;
    gap: 80rpx;
    margin-bottom: 40rpx;
  }

  .form-item {
    flex: 1;
    margin-bottom: 40rpx;

    .label {
      display: block;
      font-size: 40rpx;
      color: #333;
      margin-bottom: 20rpx;
      font-weight: bold;
    }

    .input,
    .picker-view,
    .textarea {
      width: 100%;
      padding: 28rpx;
      border: 3rpx solid #000;
      border-radius: 12rpx;
      font-size: 40rpx;
      background: #fff;
    }

    .time-group {
      display: flex;
      align-items: center;
      gap: 20rpx;
      
      .time-picker {
        flex: 1;
      }
      
      .time-separator {
        color: #666;
        font-size: 40rpx;
      }
    }

    .equipment-group {
      padding: 20rpx;
      border: 3rpx solid #000;
      border-radius: 12rpx;
      
      .equipment-item {
        display: inline-flex;
        align-items: center;
        margin-right: 40rpx;
        font-size: 40rpx;
        
        .blue-checkbox {
          transform: scale(0.9);
          margin-right: 10rpx;
          color: #1890ff;
        }
      }
    }
  }

  .button-group {
    margin-top: 60rpx;
    display: flex;
    gap: 30rpx;

    button {
      flex: 1;
      height: 100rpx;
      font-size: 40rpx;
      border-radius: 12rpx;
      
      &.submit-btn {
        background: #1890ff;
        color: #fff;
      }
      
      &.reset-btn {
        background: #f5222d;
        color: #fff;
      }
      
      &.cancel-btn {
        background: #faad14;
        color: #fff;
      }
    }
  }
}
</style>