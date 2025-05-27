<template>
  <view class="form-wrapper">
    <!-- 表单标题 -->
    <text class="form-title">图书馆用户登记表</text>
    
    <form @submit="handleSubmit">
      <scroll-view class="form-scroll" scroll-y="true">
        <!-- 账号 -->
        <view class="form-item">
          <text class="label">账号：</text>
          <input
            class="input"
            v-model="formData.no"
            placeholder="请输入账号（4-20位字符）"
            maxlength="20"
            :required="true"
          />
        </view>

        <!-- 姓名 -->
        <view class="form-item">
          <text class="label">姓名：</text>
          <input
            class="input"
            v-model="formData.name"
            placeholder="请输入真实姓名"
            :required="true"
          />
        </view>

        <!-- 密码 -->
        <view class="form-item">
          <text class="label">密码：</text>
          <input
            class="input"
            type="password"
            v-model="formData.password"
            placeholder="请输入密码（至少6位）"
            :required="true"
          />
        </view>

        <!-- 年龄 -->
        <view class="form-item">
          <text class="label">年龄：</text>
          <input
            class="input"
            v-model="formData.age"
            placeholder="请输入年龄"
            type="number"
            min="0"
            max="150"
          />
        </view>

        <!-- 性别 -->
        <view class="form-item">
          <text class="label">性别：</text>
          <picker
            class="picker"
            :range="sexOptions"
            @change="handleSexChange"
            mode="selector"
          >
            <view class="picker-view">{{ sexDisplay }}</view>
          </picker>
        </view>

        <!-- 联系电话 -->
        <view class="form-item">
          <text class="label">电话：</text>
          <input
            class="input"
            v-model="formData.phone"
            placeholder="请输入联系电话"
            maxlength="20"
          />
        </view>

        <!-- 用户类型 -->
        <view class="form-item">
          <text class="label">用户类型：</text>
          <picker
            class="picker"
            :range="userTypeOptions"
            @change="handleTypeChange"
            mode="selector"
            :required="true"
          >
            <view class="picker-view">{{ formData.userType || '请选择用户类型' }}</view>
          </picker>
        </view>

        <!-- 账户状态 -->
        <view class="form-item">
          <text class="label">账户状态：</text>
          <picker
            class="picker"
            :range="statusOptions"
            @change="handleStatusChange"
            mode="selector"
          >
            <view class="picker-view">{{ statusDisplay }}</view>
          </picker>
        </view>

        <!-- 注册时间 -->
        <view class="form-item">
          <text class="label">注册时间：</text>
          <uni-datetime-picker
            class="date-picker"
            v-model="formData.registerTime"
            type="datetime"
            :clear-icon="false"
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
          :disabled="loading"
          :loading="loading"
        >{{ loading ? '提交中...' : '提交登记' }}</button>
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
  no: '',
  name: '',
  password: '',
  age: null,
  sex: null,
  phone: '',
  userType: 0,
  is_valid: 1,
  registerTime: '' // 新增注册时间字段
});

// 页面状态
const loading = ref(false);
const sexOptions = ['未知', '男', '女'];
const userTypeOptions = ['管理员', '普通用户'];
const statusOptions = ['无效', '有效'];

// 计算属性
const sexDisplay = computed(() => 
  formData.value.sex !== null ? sexOptions[formData.value.sex] : '请选择性别'
);

const statusDisplay = computed(() => 
  statusOptions[formData.value.is_valid] || '请选择状态'
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
const handleSexChange = (e) => {
  formData.value.sex = parseInt(e.detail.value);
};

const handleTypeChange = (e) => {
  formData.value.userType = e.detail.value === 0 ? 'admin' : 'user';
};

const handleStatusChange = (e) => {
  formData.value.is_valid = parseInt(e.detail.value);
};

// 时间格式转换函数（与参考代码保持一致）
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
  // 表单验证（新增注册时间验证）
  if (!formData.value.no.match(/^[\w-]{4,20}$/)) {
    uni.showToast({ title: '账号需4-20位字母数字', icon: 'none' });
    return;
  }
  if (!formData.value.name.trim()) {
    uni.showToast({ title: '姓名不能为空', icon: 'none' });
    return;
  }
  if (formData.value.password.length < 6) {
    uni.showToast({ title: '密码至少6位', icon: 'none' });
    return;
  }
  if (!formData.value.registerTime) { // 新增时间必填验证
    uni.showToast({ title: '注册时间不能为空', icon: 'none' });
    return;
  }

  // 获取token
  const token = uni.getStorageSync('token');
  if (!token) {
    uni.showToast({ title: '请先登录', icon: 'none' });
    return uni.reLaunch({ url: '/pages/lb_login/lb_login' });
  }

  // 准备数据（新增时间格式转换）
  const submitData = {
    ...formData.value,
    sex: formData.value.sex || 0,
    age: formData.value.age || null,
    registerTime: formatDate(formData.value.registerTime) // 转换时间格式
  };

  loading.value = true;

  // 发送请求（假设后端接口需要registerTime字段）
  uni.request({
    url: 'http://localhost:9000/admin/user/add',
    method: 'POST',
    header: {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    },
    data: submitData,
    success: (response) => {
      if (response.statusCode === 200) {
        uni.showToast({
          title: '用户添加成功',
          success: () => {
            setTimeout(() => uni.navigateBack(), 1500);
          }
        });
      } else if (response.statusCode === 401) {
        handleUnauthorized();
      } else {
        uni.showToast({ 
          title: response.data?.message || '添加用户失败',
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
    no: '',
    name: '',
    password: '',
    age: null,
    sex: null,
    phone: '',
    userType: 0,
    is_valid: 1,
    registerTime: '' // 重置时间字段
  };
};

const handleCancel = () => {
  uni.navigateBack();
};

// 处理未授权情况（保持不变）
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
  height: 2500rpx; // 增加表单容器高度
  max-width: 1800rpx;
  box-shadow: 0 4rpx 12rpx rgba(0, 0, 0, 0.1);

  .form-scroll {
    max-height: 60vh; // 增加滚动区域高度
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
    margin-top: 60rpx; // 增加按钮组与表单内容的间距
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