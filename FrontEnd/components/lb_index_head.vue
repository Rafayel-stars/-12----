<!-- lb_index_header.vue -->
<template>
	<view class="body">
		<!-- 导航条 -->
		<view class="header">
			<!-- Logo -->
			<view class="logo">
				<image src="/static/img-index/logo-lb.png" mode="aspectFit"></image>
			</view>
			
			<h2>图书管理系统</h2>
			<view 
			class="exit"
			@click="uni.reLaunch({ url: '/pages/lb_login/lb_login' })"
			>
				<image 
				src="/static/img-index/exit.png"
				 mode="aspectFit"
				 @click="handleExit()"
				 ></image>
			</view>
		</view>
	</view>
</template>
<script setup>
	const handleExit = () => {
		uni.navigateTo({
			url:'/pages/lb_login/lb_login'
		})
	}
</script>
<style lang="scss">
		.header {
			/* 关键修改：固定定位在顶部 */
			position: absolute;
			top: 0;
			left: 0;
			width: 100%;
			height: 250rpx;  /* 更合理的导航条高度 */
			background: rgba(255,255,255,0.5);  /* 半透明背景 */
			display: flex;
			align-items: center;
			padding: 0 20rpx;
			z-index: 2;  /* 确保在背景图上层 */
			
			.logo {
				/* 修改为相对定位 */
				position: relative;
				width: 300rpx;  /* 合理logo宽度 */
				height: 250rpx;   /* 根据实际图片比例设置 */
				
				image {
					width: 100%;
					height: 100%;
					object-fit: contain;
				}
			}
			h2{
				font-size:100rpx ;
				
			}
			.exit{
				position: relative;
				width: 300rpx;  /* 合理logo宽度 */
				height: 250rpx;   /* 根据实际图片比例设置 */
				margin-left: auto;
				image {
					width: 100%;
					height: 100%;
					width: 150rpx;  /* 合理logo宽度 */
					height: 250rpx; 
					object-fit: contain;
					color: black;
				}
				
			}
		}
	
</style>