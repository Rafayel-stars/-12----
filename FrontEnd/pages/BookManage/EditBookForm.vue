<template>
  <view class="form-wrapper">
    <text class="form-title">编辑图书信息</text>
    <form @submit="handleSubmit">
      <scroll-view class="form-scroll" scroll-y="true">
        <!-- 图书编号：禁用，主键不可修改 -->
        <view class="form-item">
          <text class="label">ID：</text>
          <input
            class="input"
            v-model="formData.id"
            placeholder="图书编号"
            disabled
          />
        </view>

        <!-- 图书馆自编编号：禁用，唯一标识不可修改 -->
        <view class="form-item">
          <text class="label">图书馆编号：</text>
          <input
            class="input"
            v-model="formData.libraryCode"
            placeholder="图书馆自编编号"
            maxlength="20"
            disabled
          />
        </view>

        <!-- ISBN号 -->
        <view class="form-item">
          <text class="label">ISBN号：</text>
          <input
            class="input"
            v-model="formData.isbn"
            placeholder="请输入ISBN号"
            :required="true"
            maxlength="20"
          />
        </view>

        <!-- 书名 -->
        <view class="form-item">
          <text class="label">书名：</text>
          <input
            class="input"
            v-model="formData.title"
            placeholder="请输入书名"
            :required="true"
            maxlength="100"
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
            maxlength="50"
          />
        </view>

        <!-- 分类 -->
        <view class="form-item">
          <text class="label">分类：</text>
          <input
            class="input"
            v-model="formData.category"
            placeholder="请输入分类"
            maxlength="30"
          />
        </view>

        <!-- 借阅状态 -->
        <view class="form-item">
          <text class="label">借阅状态：</text>
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

        <!-- 入库时间 -->
        <view class="form-item">
          <text class="label">入库时间：</text>
          <uni-datetime-picker
            class="date-picker"
            v-model="formData.entryTime"
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
import { ref, onMounted } from 'vue';

const loading = ref(false);
const libraryCode = ref('');

// 表单数据
const formData = ref({
  id: '',
  libraryCode: '',
  isbn: '',
  title: '',
  author: '',
  category: '',
  status: 1, // 默认可借
  entryTime: ''
});

// 状态选项
const statusOptions = ref([
  { label: '可借', value: 1 },
  { label: '已借', value: 0 }
]);
const statusIndex = ref(0); // 默认选中第一个

// 从URL获取libraryCode
onMounted(() => {
  const pages = getCurrentPages();
  const currentPage = pages[pages.length - 1];
  const options = currentPage.options;
  libraryCode.value = options.libraryCode;
  
  if (libraryCode.value) {
    fetchBookData(libraryCode.value);
  }
});

// 获取图书数据
const fetchBookData = async (libCode) => {
  loading.value = true;
  const token = uni.getStorageSync('token');
  
  try {
    const res = await uni.request({
      url: `http://localhost:9000/admin/book/get/${libCode}`,
      method: 'GET',
      header: {
        'Authorization': `Bearer ${token}`
      }
    });
    
    if (res.statusCode === 200 && res.data) {
      formData.value = res.data;
      // 设置状态选择器默认值
      statusIndex.value = statusOptions.value.findIndex(item => item.value === formData.value.status);
    } else {
      uni.showToast({ title: '获取图书信息失败', icon: 'none' });
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
      url: 'http://localhost:9000/admin/book/update',
      method: 'PUT',
      header: {
        'Authorization': `Bearer ${token}`,
        'Content-Type': 'application/json'
      },
      data: formData.value // 直接传递包含id的完整表单数据
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