<template>
	<view class="">
		<button 
		class="back-btn"
		@click="handleClick">
			返回
		</button>
	</view>
</template>

<script setup>
	const handleClick = () =>{
		uni.navigateTo({
			url:'@/lb_contact_robot/pages/index/index'
		})
	}
</script>

<style lang="scss" scoped>
.back-btn{
	width: 300rpx;
	height: 300rpx;
	background-color: pink;
}
</style>
