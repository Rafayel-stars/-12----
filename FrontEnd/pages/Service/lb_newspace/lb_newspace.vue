<template>
	<view class="container">
		<lb_index_head-vue></lb_index_head-vue>
		<view class="content">
			<view class="left">
				<text>您当前的界面：首页》服务》新生空间</text>
				<view class="nav">
					<view class="nav-item">新生空间</view>
					<view 
					  class="back-btn" 
					  @click="handleBack"
					  
					>
						<text style="color: #fff;">‹ 返回</text>
					</view>
				</view>
				
				<view class="entry-container">
					<view 
					  class="newspace-item"
					  v-for="item in newspaceItems"
					  :key="item.id"
					  @click="navigateTo(item)"
					>
						<image :src="item.icon" mode="aspectFit"></image>
						<text class="title">{{ item.title }}</text>
					</view>
				</view>
			</view>
			<view class="right">
				
			</view>
		</view>
	</view>
</template>

<script setup>
import { ref, reactive } from 'vue'
import lb_index_headVue from '@/components/lb_index_head.vue'

// 显示返回按钮的状态
const showBack = ref(false)

// 导航数据
const newspaceItems = reactive([
  {
    id: 1,
    title: '馆长寄语',
    icon: '/static/img-index/tells.png',
    path: '/pages/Service/lb_newspace/lb_newspace_tells',
    type: 'navigate'
  },
  {
    id: 2,
    title: '读者手册',
    icon: '/static/img-index/manual.png',
    path: '/pages/Service/lb_newspace/lb_newspace_manual',
    type: 'redirect'
  },
  {
	id:3,
	title:'新生培训预约',
	icon:'/static/img-index/reserve.png',
	path:'/pages/Service/lb_newspace/lb_newspace_reserve',
	type:'redirect'
  },
  {
	id:4,
	title:'馆舍导航',
	icon:'/static/img-index/nav.png',
	path:'/pages/Service/lb_newspace/lb_newspace_nav',
	type:'redirect'
  },
  {
	id:5,
	title:'微信公众号',
	icon:'/static/img-index/qrcode.jpg',
	path:'/pages/Service/lb_newspace/lb_newspace_qrcode',
	type:'redirect'
  }
])

// 导航处理方法
const navigateTo = (item) => {
  showBack.value = true
  switch(item.type) {
    case 'navigate':
      uni.navigateTo({ url: item.path })
      break
    case 'redirect':
      uni.redirectTo({ url: item.path })
      break
    default:
      uni.navigateTo({ url: item.path })
  }
}

// 返回处理
const handleBack = () => {
  uni.reLaunch({
  	url:'/pages/lb_index/lb_index'
  })
  showBack.value = false
}
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
		background-color: #fff;
		top: 400rpx;
		left: 500rpx;
		width: 4000rpx;
		height: 2300rpx;

		.left {
			width: 70%;
			padding-top: 20rpx;

			text {
				display: block;
				margin-top: 30rpx;
				padding: 30rpx;
				font-size: 60rpx;
				color: #8B4513;
			}

			.nav {
				width: 100%;
				height: 150rpx;
				background-color: #fff;
				margin-top: 60rpx;
				border-bottom: 10rpx solid #8B4513;
				display: flex;
				justify-content: space-between;
				align-items: center;
				

				.nav-item {
					width: auto;
					height: 100%;
					background-color: #8B4513;
					color: #fff;
					font-size: 60rpx;
					display: flex;
					align-items: center;
					justify-content: center;
					padding: 0 50rpx;
				}

				.back-btn {
					width: auto;
					height: 100%;
					background-color: #8B4513;
					color: #fff;
					font-size: 60rpx;
					display: flex;
					align-items: center;
					justify-content: center;
					padding: 0 50rpx;

					
				}
			}
			
			.entry-container {
							display: flex;
							flex-wrap: wrap;
							gap: 80rpx;
							padding: 50rpx;
							
							.newspace-item {
								width: 400rpx;
								height: 500rpx;
								display: flex;
								flex-direction: column;
								align-items: center;
								background: #fff;
								border-radius: 20rpx;
								box-shadow: 0 4rpx 12rpx rgba(0,0,0,0.1);
								transition: all 0.3s;
								
								&:active {
									transform: scale(0.95);
									opacity: 0.9;
								}
								
								image {
									width: 300rpx;
									height: 300rpx;
									margin: 40rpx 0;
								}
								
								.title {
									font-size: 40rpx;
									color: #8B4513;
									margin-bottom: 30rpx;
								}
							}
						}
			
		}
		
	}
}
</style>