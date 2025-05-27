<template>
  <view class="form-wrapper">
    <text class="form-title">编辑公告信息</text>
    <form @submit="handleSubmit">
      <scroll-view class="form-scroll" scroll-y="true">
        <!-- ID：禁用，因为ID不可修改 -->
        <view class="form-item">
          <text class="label">ID：</text>
          <input
            class="input"
            v-model="formData.id"
            placeholder="公告ID"
            disabled
          />
        </view>
        
        <!-- 标题 -->
        <view class="form-item">
          <text class="label">标题：</text>
          <input
            class="input"
            v-model="formData.title"
            placeholder="如有需要,请重新输入公告标题"
            
          />
        </view>

        <!-- 内容 -->
        <view class="form-item">
          <text class="label">内容：</text>
          <textarea
            class="textarea"
            v-model="formData.content"
            placeholder="如有需要,请重新输入公告内容"
            
            rows="5"
          />
        </view>

        <!-- 图片URL -->
        <view class="form-item">
          <text class="label">图片URL：</text>
          <input
            class="input"
            v-model="formData.picture"
            placeholder="如有需要,请重新输入图片在阿里云OSS存储的完整URL"
          />
        </view>

        <!-- 作者 -->
        <view class="form-item">
          <text class="label">作者：</text>
          <input
            class="input"
            v-model="formData.author"
            placeholder="如有需要,请重新输入作者"
            
          />
        </view>

        <!-- 发布时间 -->
        <view class="form-item">
          <text class="label">发布时间：</text>
          <uni-datetime-picker
            class="date-picker"
            v-model="formData.publishtime"
            type="datetime"
            :clear-icon="false"
          />
        </view>

        <!-- 公告状态 -->
        <view class="form-item">
          <text class="label">公告状态：</text>
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
const announcementId = ref('');

// 表单数据
const formData = ref({
  id: '',
  title: '',
  content: '',
  picture: '',
  author: '',
  publishtime: '',
  status: 1
});

// 状态选项
const statusOptions = ref([
  { label: '最新公告', value: 1 },
  { label: '新闻时讯', value: 2 },
  { label: '资源动态', value: 3 },
  { label: '培训/活动', value: 4 }
]);
const statusIndex = ref(0);

// 从URL获取公告ID
onMounted(() => {
  const pages = getCurrentPages();
  const currentPage = pages[pages.length - 1];
  const options = currentPage.options;
  announcementId.value = options.id;
  
  // 直接设置ID，不请求数据
  formData.value.id = announcementId.value;
});

// 状态选择变化
const handleStatusChange = (e) => {
  const index = e.detail.value;
  statusIndex.value = index;
  formData.value.status = statusOptions.value[index].value;
};

// 提交表单
// 提交表单
const handleSubmit = async () => {
  loading.value = true;
  const token = uni.getStorageSync('token');
  
  try {
    // 复制表单数据
    const submitData = {...formData.value};
    
    // 转换日期格式为ISO 8601
    if (submitData.publishtime) {
      // 将 "2025-05-08 13:48:24" 转换为 "2025-05-08T13:48:24"
      submitData.publishtime = submitData.publishtime.replace(' ', 'T') + '.000+08:00';
    }
    
    const res = await uni.request({
      url: `http://localhost:9000/admin/announcement/${announcementId.value}`,
      method: 'PUT',
      header: {
        'Authorization': `Bearer ${token}`,
        'Content-Type': 'application/json'
      },
      data: submitData
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

// 时间格式化函数
const formatDate = (dateStr) => {
  if (!dateStr) return '-';
  
  try {
    const date = new Date(dateStr);
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0');
    const hours = String(date.getHours()).padStart(2, '0');
    const minutes = String(date.getMinutes()).padStart(2, '0');
    const seconds = String(date.getSeconds()).padStart(2, '0');
    
    return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
  } catch (error) {
    console.error('日期格式化失败:', error);
    return dateStr;
  }
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

.textarea {
  flex: 1;
  padding: 20rpx;
  border: 2rpx solid #ddd;
  border-radius: 8rpx;
  font-size: 32rpx;
  min-height: 200rpx;
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