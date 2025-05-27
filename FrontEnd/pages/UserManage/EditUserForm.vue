<template>
  <view class="form-wrapper">
    <text class="form-title">编辑用户信息</text>
    <form @submit="handleSubmit">
      <scroll-view class="form-scroll" scroll-y="true">
        <!-- 账号：禁用，因为账号不可修改 -->
        <view class="form-item">
          <text class="label">ID：</text>
          <input
            class="input"
            v-model="formData.id"
            placeholder="用户id"
            disabled
          />
        </view>
        
        <view class="form-item">
          <text class="label">账号：</text>
          <input
            class="input"
            v-model="formData.no"
            placeholder="用户账号"
            maxlength="20"
            disabled
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

        <!-- 密码：可选，允许留空表示不修改 -->
        <view class="form-item">
          <text class="label">密码：</text>
          <input
            class="input"
            type="password"
            v-model="formData.password"
            placeholder="留空表示不修改密码"
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
            range-key="label"
            @change="handleSexChange"
            :value="sexIndex"
            mode="selector"
          >
            <view class="picker-view">{{ sexOptions[sexIndex].label }}</view>
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
            range-key="label"
            @change="handleTypeChange"
            :value="userTypeIndex"
            mode="selector"
          >
            <view class="picker-view">{{ userTypeOptions[userTypeIndex].label }}</view>
          </picker>
        </view>

        <!-- 账户状态 -->
        <view class="form-item">
          <text class="label">账户状态：</text>
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

        
        <view class="form-item">
            <text class="label">注册时间：</text>
            <uni-datetime-picker
              class="date-picker"
              v-model="formData.registerTime"
              type="datetime"
              :clear-icon="false"
              :disabled="true"
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
import { ref, onMounted, computed } from 'vue';

const loading = ref(false);
const userNo = ref('');

// 表单数据
const formData = ref({
  id: '',
  no: '',
  name: '',
  password: '',
  age: '',
  sex: 0,
  phone: '',
  userType: '',
  is_valid: true,
  registerTime: ''
});

// 选择器选项
const sexOptions = ref([
  { label: '未知', value: 0 },
  { label: '男', value: 1 },
  { label: '女', value: 2 }
]);
const sexIndex = ref(0);

const userTypeOptions = ref([
  { label: '普通用户', value: 'user' },
  { label: '管理员', value: 'admin' }
]);
const userTypeIndex = ref(0);

const statusOptions = ref([
  { label: '有效', value: 1 },
  { label: '无效', value: 0 }
]);
const statusIndex = ref(0);

// 从URL获取用户账号
onMounted(() => {
  const pages = getCurrentPages();
  const currentPage = pages[pages.length - 1];
  const options = currentPage.options;
  userNo.value = options.no;
  console.log('pages',pages)
  console.log('currentPage',currentPage)
  console.log('options',options)
  console.log('userNo',userNo)
  if (userNo.value) {
    fetchUserData(userNo.value);
  }
});

// 获取用户数据
const fetchUserData = async (no) => {
  loading.value = true;
  const token = uni.getStorageSync('token');
  
  try {
    const res = await uni.request({
      url: `http://localhost:9000/admin/user/get/${no}`,
      method: 'GET',
      header: {
        'Authorization': `Bearer ${token}`
      }
    });
    console.log("res",res)
    if (res.statusCode === 200 && res.data) {
      formData.value = res.data;
    console.log("formData",formData)
      // 设置选择器默认值
      sexIndex.value = sexOptions.value.findIndex(item => item.value === formData.value.sex);
      userTypeIndex.value = userTypeOptions.value.findIndex(item => item.value === formData.value.userType);
      statusIndex.value = statusOptions.value.findIndex(item => item.value === formData.value.is_valid);
      
    } else {
      uni.showToast({ title: '获取用户信息失败', icon: 'none' });
      setTimeout(() => {
        uni.navigateBack();
      }, 1500);
    }
  } catch (error) {
    uni.showToast({ title: '请求失败，请检查网络', icon: 'none' });
    // setTimeout(() => {
    //   uni.navigateBack();
    // }, 1500);
  } finally {
    loading.value = false;
  }
};

// 性别选择变化
const handleSexChange = (e) => {
  const index = e.detail.value;
  sexIndex.value = index;
  formData.value.sex = sexOptions.value[index].value;
};

// 用户类型选择变化
const handleTypeChange = (e) => {
  const index = e.detail.value;
  userTypeIndex.value = index;
  formData.value.userType = userTypeOptions.value[index].value;
};

// 状态选择变化
const handleStatusChange = (e) => {
  const index = e.detail.value;
  statusIndex.value = index;
  formData.value.is_valid = statusOptions.value[index].value;
};

// 提交表单
const handleSubmit = async () => {
  loading.value = true;
  const token = uni.getStorageSync('token');
  
  try {
    // 如果密码为空，则不修改密码
    const submitData = { ...formData.value };
    if (!submitData.password) {
      delete submitData.password;
    }
    
    const res = await uni.request({
      url: 'http://localhost:9000/admin/user/update',
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