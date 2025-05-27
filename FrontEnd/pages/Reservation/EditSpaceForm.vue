<template>
  <view class="form-wrapper">
    <text class="form-title">编辑空间申请信息</text>
    <form @submit="handleSubmit">
      <scroll-view class="form-scroll" scroll-y="true">
        <!-- 申请ID：禁用，因为ID不可修改 -->
        <view class="form-item">
          <text class="label">申请记录ID：</text>
          <input
            class="input"
            v-model="formData.applicationId"
            placeholder="申请记录ID"
            disabled
          />
        </view>
        
        <!-- 空间ID -->
        <view class="form-item">
          <text class="label">空间ID：</text>
          <input
            class="input"
            v-model="formData.spaceId"
            placeholder="空间ID"
            disabled
          />
        </view>

        <!-- 申请人姓名 -->
        <view class="form-item">
          <text class="label">申请人姓名：</text>
          <input
            class="input"
            v-model="formData.applicantName"
            placeholder="申请人姓名"
            disabled
          />
        </view>

        <!-- 预约时间 -->
        <view class="form-item">
          <text class="label">预约时间：</text>
          <uni-datetime-picker
            class="date-picker"
            v-model="formData.appointmentTime"
            type="datetime"
            :clear-icon="false"
          />
        </view>

        <!-- 使用时长 -->
        <view class="form-item">
          <text class="label">使用时长：</text>
          <input
            class="input"
            v-model="formData.duration"
            placeholder="使用时长（小时）"
            type="number"
            min="1"
          />
        </view>

        <!-- 申请状态 -->
        <view class="form-item">
          <text class="label">申请状态：</text>
          <picker
            class="picker"
            :range="statusOptions"
            range-key="label"
            @change="handleStatusChange"
            :value="statusIndex"
            mode="selector"
          >
            <view class="picker-view">{{ statusOptions[statusIndex].label }}</view>
          </picker>
        </view>
      </scroll-view>

      <!-- 操作按钮 -->
      <view class="button-group">
        <button
          class="submit-btn"
          form-type="submit"
          type="primary"
          :disabled="loading"
          :loading="loading"
        >{{ loading ? '修改中...' : '提交修改' }}</button>
        <button
          class="cancel-btn"
          type="default"
          @click="handleCancel"
          :disabled="loading"
        >取消</button>
      </view>
    </form>
  </view>
</template>

<script setup>
import { ref, onMounted } from 'vue';

const loading = ref(false);
const applicationId = ref('');

// 表单数据
const formData = ref({
  applicationId: '',
  spaceId: '',
  applicantName: '',
  appointmentTime: '',
  duration: '',
  status: 0
});

// 状态选项
const statusOptions = ref([
  { label: '待审核', value: 0 },
  { label: '已通过', value: 1 },
  { label: '未通过', value: 2 }
]);
const statusIndex = ref(0);

// 从URL获取申请ID
onMounted(() => {
  const pages = getCurrentPages();
  const currentPage = pages[pages.length - 1];
  const options = currentPage.options;
  console.log('options',options)
  applicationId.value = options.applicationId; // 从URL参数获取正确的字段
  console.log("applicantName",applicationId)
  if (applicationId.value) {
    fetchApplicationData(applicationId.value);
  }
});

// 获取申请数据
const fetchApplicationData = async (id) => {
  loading.value = true;
  const token = uni.getStorageSync('token');
  
  try {
    const res = await uni.request({
      url: `http://localhost:9000/admin/space/application/detail/${id}`,
      method: 'GET',
      header: {
        'Authorization': `Bearer ${token}`
      }
    });
    console.log('formdata',res.data)
    if (res.statusCode === 200 && res.data) {
      formData.value = res.data;
      
      // 设置状态选择器默认值
      statusIndex.value = statusOptions.value.findIndex(item => item.value === formData.value.status);
    } else {
      uni.showToast({ title: '获取申请信息失败', icon: 'none' });
      setTimeout(() => {
        uni.navigateBack();
      }, 1500);
    }
  } catch (error) {
    uni.showToast({ title: '请求失败，请检查网络', icon: 'none' });
  } finally {
    loading.value = false;
  }
};

// 状态选择变化
const handleStatusChange = (e) => {
  const index = e.detail.value;
  statusIndex.value = index;
  formData.value.status = statusOptions.value[index].value;
};

// 提交表单
const handleSubmit = async () => {
  loading.value = true;
  const token = uni.getStorageSync('token');
  
  try {
    const res = await uni.request({
      url: `http://localhost:9000/admin/space/application/status/${formData.value.applicationId}/${formData.value.status}`,
      method: 'PUT',
      header: {
        'Authorization': `Bearer ${token}`
      },
      data: formData.value
    });
    
    if (res.statusCode === 200) {
      uni.showToast({ title: '修改成功' });
      setTimeout(() => {
        uni.navigateBack();
      }, 1500);
    } else {
      uni.showToast({ title: '修改失败', icon: 'none' });
    }
  } catch (error) {
    uni.showToast({ title: '请求失败，请检查网络', icon: 'none' });
  } finally {
    loading.value = false;
  }
};

// 取消编辑
const handleCancel = () => {
  uni.navigateBack();
};
</script>

<style lang="scss" scoped>
.form-wrapper {
  padding: 40rpx;
  background-color: #fff;
  min-height: 100vh;
}

.form-title {
  display: block;
  font-size: 48rpx;
  font-weight: bold;
  color: #333;
  margin-bottom: 40rpx;
  text-align: center;
}

.form-scroll {
  max-height: 80vh;
}

.form-item {
  margin-bottom: 30rpx;
  display: flex;
  align-items: center;
}

.label {
  width: 200rpx;
  font-size: 32rpx;
  color: #666;
}

.input {
  flex: 1;
  padding: 20rpx;
  border: 2rpx solid #ddd;
  border-radius: 8rpx;
  font-size: 32rpx;
}

.picker {
  flex: 1;
}

.picker-view {
  padding: 20rpx;
  border: 2rpx solid #ddd;
  border-radius: 8rpx;
  font-size: 32rpx;
}

.button-group {
  margin-top: 60rpx;
  display: flex;
  justify-content: space-around;
}

.submit-btn {
  width: 300rpx;
  background-color: #1890ff;
  color: #fff;
}

.cancel-btn {
  width: 300rpx;
  background-color: #fff;
  border: 2rpx solid #ddd;
  color: #666;
}
</style>