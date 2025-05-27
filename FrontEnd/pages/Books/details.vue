<!-- 详情 -->

<template>
  <view class="container">
    <!-- 搜索条件展示 -->
    <view class="search-info">
      <text class="search-label">搜索模式:</text>
      <text class="search-value">{{ searchMode }}</text>
      <text class="search-label">关键词:</text>
      <text class="search-value">{{ keyword }}</text>
	  <button @click="handleBack" style="width: 200rpx;height: 100rpx;background-color:#8B4513;color: #fff;">返回</button>
    </view>

    <!-- 数据展示区域 -->
    <view class="book-container">
      <view v-for="item in bookList" :key="item.id" class="book-card">
        <view class="book-cover">
          <image 
            :src="item.coverUrl || 'https://picsum.photos/200/300'" 
            mode="aspectFill" 
            class="cover-image"
          ></image>
        </view>
        <view class="book-info">
          <text class="book-title">{{ item.title || '未知书名' }}</text>
          <text class="book-author">作者: {{ item.author || '未知作者' }}</text>
          <text class="book-isbn">ISBN: {{ item.isbn || '无ISBN' }}</text>
          <text class="book-category">分类: {{ item.category || '未知分类' }}</text>
          <text class="book-publisher">出版社: {{ item.publisher || '未知出版社' }}</text>
          <text class="book-price">价格: {{ item.price || '未知价格' }}</text>
		  <text class="book-status">借阅状态：{{item.status === 1 ? '可借':'已借出'}}</text>
        </view>
      </view>
    </view>

    <!-- 加载状态和错误提示 -->
    <view v-if="loading" class="loading-container">
      <view class="loading-spinner"></view>
      <text class="loading-text">加载中...</text>
    </view>
    <view v-if="errorMsg" class="error-container">
      <text class="error-icon">❗</text>
      <text class="error-text">{{ errorMsg }}</text>
    </view>
  </view>
</template>

<script setup>
import { ref, onMounted } from 'vue';

// 定义响应式变量用于保存参数
const searchMode = ref(''); // 搜索模式
const keyword = ref(''); // 搜索关键字

// 响应式数据
const bookList = ref([]);
const loading = ref(true);
const errorMsg = ref('');

// 获取本地存储的用户信息
const user = uni.getStorageSync('user');
const token = uni.getStorageSync('token');

// 使用 onMounted 钩子获取路由参数
onMounted(() => {
  // 使用 uni-app 的标准方法获取路由参数
  const pages = getCurrentPages();
  const currentPage = pages[pages.length - 1];
  const options = currentPage.options;
  
  // 赋值给响应式变量
  searchMode.value = options.mode || '';
  keyword.value = options.keyword || '';
  
  console.log('获取到的参数', searchMode.value, keyword.value); // 调试日志
  handleSearch();
});

// 封装搜索逻辑函数
const handleSearch = async () => {
  // 检查必要参数是否存在
  if (!searchMode.value || !keyword.value) {
    errorMsg.value = '缺少必要的搜索参数';
    loading.value = false;
    return;
  }

  // 构建请求地址
  let apiPath = '';
  switch (searchMode.value) {
    case 'title':
      apiPath = `http://localhost:9000/reader/book/searchbytitle/${keyword.value}`;
      break;
    case 'isbn':
      apiPath = `http://localhost:9000/reader/book/searchbyisbn/${keyword.value}`;
      break;
    case 'author':
      apiPath = `http://localhost:9000/reader/book/searchbyauthor/${keyword.value}`;
      break;
    case 'category':
      apiPath = `http://localhost:9000/reader/book/searchbycategory/${keyword.value}`;
      break;
    default:
      errorMsg.value = '不支持的搜索模式';
      loading.value = false;
      return;
  }
  console.log('detailapi', apiPath);

  // 发送请求
  try {
    const res = await uni.request({
      url: apiPath,
      method: 'GET',
      header: {
        'Authorization': `Bearer ${token}`
      }
    });
    console.log("res", res.data);
    
    if (res.statusCode === 200) {
      bookList.value = Array.isArray(res.data) ? res.data : [res.data];
    } else {
      uni.showToast({
        title: "数据获取失败",
        icon: "none"
      });
    }
  } catch (error) {
    uni.showToast({
      title: '请求失败，请检查网络',
      icon: 'none'
    });
  } finally {
    loading.value = false;
  }
};

const handleBack = () =>{
	uni.navigateBack()
}
</script>

<style scoped>
.container {
  padding: 20rpx;
  background-color: #f7f7f7;
  min-height: 100vh;
}

.search-info {
  display: flex;
  flex-wrap: wrap;
  padding: 15rpx;
  background-color: #ffffff;
  border-radius: 12rpx;
  box-shadow: 0 2rpx 10rpx rgba(0, 0, 0, 0.05);
  margin-bottom: 20rpx;
  height: 100rpx;
  align-items: center;
  /* justify-content: center; */
}

.search-label {
  color: #666666;
  font-size: 36rpx;
  margin-right: 10rpx;
}

.search-value {
  color: #333333;
  font-size: 36rpx;
  font-weight: 500;
  margin-right: 20rpx;
  margin-bottom: 10rpx;
}

.book-container {
  display: flex;
  flex-wrap: wrap;
  justify-content: space-between;
}

.book-card {
  width: 100%;
  background-color: #ffffff;
  border-radius: 16rpx;
  box-shadow: 0 4rpx 16rpx rgba(0, 0, 0, 0.08);
  margin-bottom: 24rpx;
  overflow: hidden;
  display: flex;
  transition: all 0.3s ease;
}

.book-card:hover {
  transform: translateY(-4rpx);
  box-shadow: 0 8rpx 24rpx rgba(0, 0, 0, 0.12);
}

.book-cover {
  width: 280rpx;
  height: 380rpx;
  background-color: #f0f0f0;
  display: flex;
  align-items: center;
  justify-content: center;
}

.cover-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.book-info {
  flex: 1;
  padding: 24rpx;
  display: flex;
  flex-direction: column;
  justify-content: center;
}

.book-title {
  font-size: 32rpx;
  font-weight: bold;
  color: #333333;
  margin-bottom: 12rpx;
  line-height: 1.4;
  display: -webkit-box;
  -webkit-line-clamp: 1;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.book-author,
.book-isbn,
.book-category,
.book-publisher,
.book-price,
.book-status{
  font-size: 26rpx;
  color: #666666;
  margin-bottom: 8rpx;
  line-height: 1.5;
}

.loading-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 60rpx 0;
}

.loading-spinner {
  width: 48rpx;
  height: 48rpx;
  border: 6rpx solid #f3f3f3;
  border-top: 6rpx solid #409eff;
  border-radius: 50%;
  animation: spin 1s linear infinite;
  margin-bottom: 16rpx;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

.loading-text {
  font-size: 28rpx;
  color: #999999;
}

.error-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 60rpx 0;
}

.error-icon {
  font-size: 60rpx;
  color: #ff4d4f;
  margin-bottom: 16rpx;
}

.error-text {
  font-size: 28rpx;
  color: #ff4d4f;
  text-align: center;
}

/* 适配不同屏幕尺寸 */
@media screen and (min-width: 768px) {
  .book-card {
    width: calc(50% - 12rpx);
  }
}

@media screen and (min-width: 1024px) {
  .book-card {
    width: calc(33.333% - 16rpx);
  }
}
</style>