<template>
	<view class="Nav">
		<view
		class="Nav-item"
		v-for="item in NavData"
		:key="item.id"
		:class="{'active': currentPage === item.name}"
		@click="handlePageClick(item)"
		>
		<image :src="item.icon"></image>
		<view>{{item.name}}</view>
			
		</view>
		
	</view>
	
</template>

<script setup>
	import {ref, onMounted} from 'vue';
	const currentPage = ref('用户管理')
	
	const NavData = ref([
		{id:1,name:'用户管理',path:'/pages/UserManage/UserManage',icon:'/static/img-index/user.png'},
		{id:2,name:'图书管理',path:'/pages/BookManage/BookManage',icon:'/static/img-index/book.png'},
		{id:3,name:'借阅管理',path:'/pages/BorrowManage/BorrowManage',icon:'/static/img-index/borrow.png'},
		{id:4,name:'预约管理',path:'/pages/Reservation/Reservation',icon:'/static/img-index/reservation.png'},
		{id:5,name:'统计分析',path:'/pages/Analysis/Analysis',icon:'/static/img-index/analyse.png'}
		
	])
	
	const handlePageClick = (item)=>{
		currentPage.value = item.name;
		uni.redirectTo({
			url:item.path
		})
	}
// 初始化时根据当前页面设置激活状态
	onMounted(() => {
	  const pages = getCurrentPages();
	  if (pages.length) {
		const currentPath = '/'+pages[pages.length - 1].route;
		const currentNav = NavData.value.find(item => item.path === currentPath);
		if (currentNav) currentPage.value = currentNav.name;
	  }
	});
</script>

<style lang="scss" scoped>
.Nav{
	width: 800rpx;
	height:100vh;
	background-color: rgb(48, 65, 86);
	
	.Nav-item{
		display: flex;
		width:100%;
		height:200rpx;
		font-size: 100rpx;
		color: #fff;
		align-items: center;
		image{
			margin:0rpx 10rpx;
			width: 120rpx;
			height: 120rpx;
		}
		&.active {
		      background: #e9f5ff;
		      color: #1890ff;
		      font-weight: 500;
			   image {
			        filter: invert(56%) sepia(94%) saturate(1865%) hue-rotate(190deg) brightness(98%) contrast(105%);
			      }
	}
}
}
</style>