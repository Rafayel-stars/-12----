<template>
  <view class="container">
    <lb_index_head-vue></lb_index_head-vue>
    <view class="content">
      <view class="left">
        <text>您当前的界面：首页》服务》新生空间》新生培训预约</text>
        
        <view class="nav">
          <view class="nav-item">新生培训预约</view>
          <view 
            class="back-btn" 
            @click="handleBack"
          >
            <text style="color: #fff;">‹ 返回</text>
          </view>
        </view>
		<view class="reserve-container">
			<text class="emsp">
				以院系或班级为单位，举办“新生入馆教育----如何利用图书馆”专题讲座。培训结束后，可以根据需求安排实地参观图书馆。\n
				
				培训时间：一般为入学军训后一个月之内（10月份），正常上班时间段均可预约。\n
				
				培训内容：包括图书馆简介、资源布局、规章制度、服务内容、图书借还、书目查询、图书分类与排架知识、常用电子资源/数据库介绍等利用图书馆的基本知识和技能。针对研究生新生的入馆教育更侧重于电子资源/数据库的介绍和获取。\n
				
				培训地点：可根据具体情况选择确定。\n
				
				（1）可以利用图书馆培训室。\n
				
				（2）可以利用院系教室进行培训。请提前告知我们现场设备、网络条件等情况。\n
				
				（3）可以由学生与教室管理中心联系大型阶梯教室或机房。请提前告知现场设备、网络条件等情况。\n
				
				石牌校区图书馆联系人：     张老师   联系电话：85214537-857\n
				
				大学城校区图书馆联系人： 廖老师   联系电话：39310107\n
				
				南海校区图书馆联系人：    闫老师   联系电话：0757-86687873\n
				
				汕尾校区图书馆联系人：    吕老师   联系电话：0660-3808026\n
				
				 
			</text>
			<view class="right">
			<lb_side_nav-vue
			      			:section="'服务'" 
			      			:current-path="currentPagePath"
			/>
			</view>
		</view>
        
      </view>
      <view class="right"></view>
    </view>
  </view>
</template>

<script setup>
import { ref } from 'vue';
import lb_index_headVue from '@/components/lb_index_head.vue';
import lb_side_navVue from '@/components/lb_side_nav.vue';
// 获取当前页面路径
const currentPagePath = ref('/pages/Service/lb_newspace/lb_newspace_reserve');


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