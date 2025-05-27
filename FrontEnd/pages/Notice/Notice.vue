<template>
  <view class="container">
    <lb_index_head-vue></lb_index_head-vue>
    <view class="content">
      <view class="left">
        <view class="nav">
          <view class="nav-item">详情</view>
          <view 
            class="back-btn" 
            @click="handleBack"
          >
            <text style="color: #fff;">‹ 返回</text>
          </view>
        </view>
        <scroll-view class="form-scroll" scroll-y="true">
          <view v-if="announcement" class="education-container">
			<view class="title"><text>{{announcement.title}}</text></view>
			
            <text>{{ announcement.content }}</text>  <!-- 渲染内容 -->
			<view class="a-t">
				<text>{{announcement.author}}</text>
				<text>{{formatDate(announcement.publishtime)}}</text>
			</view>
			
			
          </view>
          <view v-else class="loading">加载中...</view>
        </scroll-view>
      </view>
    </view>
  </view>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue';
import lb_index_headVue from '@/components/lb_index_head.vue';



const announcement = ref(null);  // 存储公告详情
const token = uni.getStorageSync('token');  // 获取token
const announcementId = ref('')
onMounted(() => {
	// 获取路由参数
	const pages = getCurrentPages();
	const currentPage = pages[pages.length - 1];
	const options = currentPage.options;
	console.log('options',options)
	announcementId.value = options.id
	console.log("announcementId.value",announcementId.value)
  if (!announcementId) {
    uni.showToast({ title: '参数错误', icon: 'none' });
    return handleBack();
  }

  loadAnnouncementDetail(announcementId.value);
});

// 加载公告详情
const loadAnnouncementDetail = async (id) => {
  try {
    const res = await uni.request({
      url: `http://localhost:9000/reader/announcement/detail/${id}`,
      method: 'GET',
      header: {
        'Authorization': `Bearer ${token}`,
        // 'Content-Type': 'application/json'
      },
    });
	console.log("res.data",res.data)
    if (res.statusCode === 200) {
      announcement.value = res.data;
    } else {
      uni.showToast({
        title: '公告详情获取失败',
        icon: 'none'
      });
    }
  } catch (error) {
    console.error('请求出错:', error);
    uni.showToast({
      title: '请求失败，请检查网络',
      icon: 'none'
    });
  }
};

const handleBack = () => {
  uni.navigateBack();  // 返回上一页，比reLaunch更友好
};

// 日期格式化函数
const formatDate = (dateString) => {
  const date = new Date(dateString);
  const year = date.getFullYear();
  const month = String(date.getMonth() + 1).padStart(2, '0');
  const day = String(date.getDate()).padStart(2, '0');
  return `${year}-${month}-${day}`;
};
</script>

<style lang="scss" scoped>
.container {
  background-image: url('/static/img-index/background.png');
  background-size: cover;
  background-position: center;
  width: 100vw;
  height: 100vh;
  position: relative;
  
  .content {
    display: flex;
    position: absolute;
    background-color: rgba(255, 255, 255, 0.8);
    top: 400rpx;
    left: 500rpx;
    width: 4000rpx;
    height: 2300rpx;

    .left {
      width: 70%;
      padding: 20rpx 40rpx;

      text {
        display: block;
        margin: 30rpx 0;
        padding: 30rpx;
        font-size: 60rpx;
        color: #8B4513;
        white-space: pre-wrap;  /* 保留换行 */
      }

      .nav {
        width: 100%;
        height: 150rpx;
        margin: 60rpx 0;
        border-bottom: 10rpx solid #8B4513;
        display: flex;
        justify-content: space-between;
        align-items: center;

        .nav-item, .back-btn {
          padding: 0 50rpx;
          height: 100%;
          background-color: #8B4513;
          color: #fff;
          font-size: 60rpx;
          display: flex;
          align-items: center;
        }
      }

      .form-scroll {
        height: 1500rpx; 
        overflow-y: auto; 
      }

      .education-container {
        padding: 30rpx;
        font-size: 50rpx;
        line-height: 1.6;
		border: 5rpx solid #8B4513;
		.title{
			height: 200rpx;
			width: 100%;
			font-size: 36rpx;
			border-bottom: 3rpx solid #8B4513;
			font-weight: 700;
		}
		.a-t{
			text-align: right;
		}
	  }

      .loading {
        padding: 30rpx;
        font-size: 50rpx;
        color: #666;
      }
    }
  }
}
</style>