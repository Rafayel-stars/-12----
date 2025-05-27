<template>
  <view class="container">
    <lb_index_head-vue></lb_index_head-vue>
    <view class="content">
      <view class="left">
        <text>您当前的界面：首页》服务》新生空间》馆长寄语</text>
        
        <view class="nav">
          <view class="nav-item">馆长寄语</view>
          <view 
            class="back-btn" 
            @click="handleBack"
          >
            <text style="color: #fff;">‹ 返回</text>
          </view>
        </view>
		<view class="tells-container">
			<text>
				亲爱的同学：  
			</text>
			<text>	
				    当你迈进华南师范大学校门时，图书馆将会陪伴你一起成长。
				
				    快捷的网络、丰富的数字资源和全新的高等教育模式颠覆了人们印象中的大学图书馆。但图书馆是大学的心脏、精神殿堂却永远不会改变；图书馆仍是人们心目中的知识中心、学习中心，并正在向思想和学术交流中心及文化中心拓展。
				
				    当你需要学习和研究时，图书馆有丰富的信息资源和舒适的学习空间；当你需要与老师及同学交流和研讨时，图书馆有设施先进的研讨空间；当你需要欣赏文化遗产和艺术作品时，图书馆有宽敞的展览空间；当你利用信息资源遇到困难时，图书馆有专业的咨询馆员；当你需要提高自己的高雅情趣时，图书馆有优雅的文化空间；当你在学习、研究、生活、就业遇到苦恼时，图书馆的真人图书会为你排忧解难；当你需要图书馆提供与立德、学习、研究、交流的其他需求时，我们会尽可能满足你的心愿。
				
				    由于种种原因，图书馆的一些工作尚不完善，希望同学们多对图书馆工作提出宝贵意见，以利于图书馆科学发展。
				
				    
			</text>
			<text>
				我们的愿望是：当你离开母校后，图书馆是你永远的思念！
			</text>
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
const currentPagePath = ref('/pages/Service/lb_newspace/lb_newspace_tells');


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
		
	  
    }
    .right{
    	position: fixed;
    	right: 900rpx;
    	top: 980rpx;
    }
  }
}
</style>