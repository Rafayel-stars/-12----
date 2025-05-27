<template>
  <view class="container">
    <lb_index_head-vue></lb_index_head-vue>
    <view class="content">
      <view class="left">
        <text>您当前的界面：首页》服务》新生空间》微信公众号</text>
        
        <view class="nav">
          <view class="nav-item">微信公众号</view>
          <view 
            class="back-btn" 
            @click="handleBack"
          >
            <text style="color: #fff;">‹ 返回</text>
          </view>
        </view>
		<view class="qrcode-container">
			<image src="/static/img-index/qrcode.jpg" mode="aspectFit"></image>
		</view>
        
      </view>
      <view class="right">
		  <lb_side_nav-vue
		   :section="'服务'" 
		    :current-path="currentPagePath"
		  />
	  </view>
    </view>
  </view>
</template>

<script setup>
import { ref } from 'vue';
import lb_index_headVue from '@/components/lb_index_head.vue';
import lb_side_navVue from '@/components/lb_side_nav.vue';
// 获取当前页面路径
const currentPagePath = ref('/pages/Service/lb_newspace/lb_newspace_qrcode');

const handleBack = () => {
  uni.reLaunch({
  	url:'/pages/Service/lb_newspace/lb_newspace'
  })
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
    position: absolute;
    // background-color: #fff;
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
      }

      .nav {
        width: 100%;
        height: 150rpx;
        // background-color: #fff;
        margin: 60rpx 0;
        border-bottom: 10rpx solid #8B4513;
        display: flex;
        justify-content: space-between;
        align-items: center;

        .nav-item {
          padding: 0 50rpx;
          height: 100%;
          background-color: #8B4513;
          color: #fff;
          font-size: 60rpx;
          display: flex;
          align-items: center;
        }

        .back-btn {
          padding: 0 50rpx;
          height: 100%;
          background-color: #8B4513;
          display: flex;
          align-items: center;
          font-size: 60rpx;
        }
      }
		
	  .qrcode-container{
		  width: 100%;
		  height: 1500rpx;
		  display: flex;
		  align-items: center;
		  justify-content: center;
		  image{
			  width: 100%;
			  height: 100%;
		  }
	  }
      
    }
	.right{
		position: fixed;
		right: 900rpx;
		top: 980rpx;
	}
  }
  
}
</style>