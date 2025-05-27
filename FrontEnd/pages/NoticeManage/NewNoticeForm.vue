<template>
  <view class="form-wrapper">
    <!-- 表单标题 -->
    <text class="form-title">新增公告</text>
    
    <form @submit="handleSubmit">
      <scroll-view class="form-scroll" scroll-y="true">
        <!-- 标题 -->
        <view class="form-item">
          <text class="label">标题：</text>
          <input
            class="input"
            v-model="formData.title"
            placeholder="请输入公告标题"
            :required="true"
          />
        </view>

        <!-- 内容 -->
        <view class="form-item">
          <text class="label">内容：</text>
          <textarea
            class="textarea"
            v-model="formData.content"
            placeholder="请输入公告内容"
            :required="true"
          />
        </view>

        <!-- 图片 -->
        <view class="form-item">
          <text class="label">图片URL：</text>
          <input
            class="input"
            v-model="formData.picture"
            placeholder="请输入图片在阿里云OSS存储的完整URL"
          />
        </view>

        <!-- 作者 -->
        <view class="form-item">
          <text class="label">作者：</text>
          <input
            class="input"
            v-model="formData.author"
            placeholder="请输入作者"
            :required="true"
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
            :required="true"
          />
        </view>

        <!-- 公告类型 -->
        <view class="form-item">
          <text class="label">公告类型：</text>
          <picker
            class="picker"
            :range="statusOptions"
            @change="handleStatusChange"
            mode="selector"
            :required="true"
          >
            <view class="picker-view">{{ statusDisplay }}</view>
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
        >{{ loading ? '提交中...' : '提交公告' }}</button>
        <button
          class="reset-btn"
          type="default"
          @click="handleReset"
          :disabled="loading"
        >重置</button>
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
import { ref, computed, onMounted } from 'vue';

// 表单数据
const formData = ref({
  title: '',
  content: '',
  picture: '',
  author: '',
  publishtime: '',
  status: 1
});

// 页面状态
const loading = ref(false);
const statusOptions = ['最新公告', '新闻时讯', '资源动态', '培训/活动'];

// 计算属性
const statusDisplay = computed(() => 
  formData.value.status !== null ? statusOptions[formData.value.status - 1] : '请选择公告类型'
);

// 生命周期钩子
onMounted(() => {
  // 权限验证
  const user = uni.getStorageSync('user');
  if (user.userType !== 'admin') {
    uni.showToast({ title: '无权限访问', icon: 'none' });
    setTimeout(() => uni.navigateBack(), 1500);
  }
});

// 事件处理
const handleStatusChange = (e) => {
  // 注意：picker的value是从0开始的，而我们的status是从1开始的
  formData.value.status = parseInt(e.detail.value) + 1;
};

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

const handleSubmit = () => {
  // 表单验证
  if (!formData.value.title.trim()) {
    uni.showToast({ title: '标题不能为空', icon: 'none' });
    return;
  }
  if (!formData.value.content.trim()) {
    uni.showToast({ title: '内容不能为空', icon: 'none' });
    return;
  }
  if (!formData.value.author.trim()) {
    uni.showToast({ title: '作者不能为空', icon: 'none' });
    return;
  }
  if (!formData.value.publishtime) {
    uni.showToast({ title: '发布时间不能为空', icon: 'none' });
    return;
  }
  if (!formData.value.status) {
    uni.showToast({ title: '请选择公告类型', icon: 'none' });
    return;
  }

  // 获取token
  const token = uni.getStorageSync('token');
  if (!token) {
    uni.showToast({ title: '请先登录', icon: 'none' });
    return uni.reLaunch({ url: '/pages/lb_login/lb_login' });
  }

  // 准备数据
  const submitData = {
    ...formData.value,
    publishtime: formatDate(formData.value.publishtime)
  };

  loading.value = true;

  // 发送请求
  uni.request({
    url: 'http://localhost:9000/admin/announcement/add',
    method: 'POST',
    header: {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    },
    data: submitData,
    success: (response) => {
      if (response.statusCode === 200) {
        uni.showToast({
          title: '公告添加成功',
          success: () => {
            setTimeout(() => uni.navigateBack(), 1500);
          }
        });
      } else if (response.statusCode === 400) {
        uni.showToast({ 
          title: response.data?.message || '参数错误',
          icon: 'none'
        });
      } else if (response.statusCode === 401) {
        handleUnauthorized();
      } else {
        uni.showToast({ 
          title: response.data?.message || '添加公告失败',
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

const handleReset = () => {
  formData.value = {
    title: '',
    content: '',
    picture: '',
    author: '',
    publishtime: '',
    status: 1
  };
};

const handleCancel = () => {
  uni.navigateBack();
};

// 处理未授权情况
const handleUnauthorized = () => {
  uni.showToast({ title: '登录已过期，请重新登录', icon: 'none' });
  uni.removeStorageSync('token');
  uni.removeStorageSync('userType');
  setTimeout(() => {
    uni.reLaunch({ url: '/pages/login' });
  }, 1500);
};
</script>

<style lang="scss" scoped>
.form-wrapper {
  padding: 30rpx;
  background-color: #fff;
  border-radius: 12rpx;
  margin: 30rpx auto;
  width: 1600rpx;
  height: 2500rpx;
  max-width: 1800rpx;
  box-shadow: 0 4rpx 12rpx rgba(0, 0, 0, 0.1);

  .form-scroll {
    max-height: 60vh;
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
        padding: 25rpx !important;
      }
    }
  }

  .button-group {
    margin-top: 60rpx;
    display: flex;
    gap: 20rpx;

    button {
      flex: 1;
      padding: 25rpx 0;
      font-size: 40rpx;
      border-radius: 12rpx;
      
      &[disabled] {
        opacity: 0.6;
      }
      
      &.submit-btn {
        background-color: #28a745 !important;
      }
      
      &.reset-btn {
        background-color: #ffc107 !important;
        color: #333 !important;
      }
      
      &.cancel-btn {
        background-color: #dc3545 !important;
        color: white !important;
      }
    }
  }
}
</style>
    