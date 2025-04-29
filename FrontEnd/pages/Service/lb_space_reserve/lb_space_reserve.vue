<template>
	<view class="container">
		<lb_index_head-vue></lb_index_head-vue>
		<view class="content">
			<lb_reserve-vue></lb_reserve-vue>
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
	import lb_index_headVue from '../../../components/lb_index_head.vue';
	import lb_reserveVue from '../../../components/lb_reserve.vue';
	import lb_side_navVue from '../../../components/lb_side_nav.vue';
	// 获取当前页面路径
	const currentPagePath = ref('/pages/Service/lb_borrow/lb_borrow');
</script>

<style lang="scss" scoped>
	.container{
		background-image: url('/static/img-index/head.png');
		background-size:cover;
		background-position: center;
		width: 100vw;
		height: 100vh;
		position: relative;
		.content{
			position: absolute;
			background: rgba(255, 255, 255, 0.8);
			top: 400rpx;
			left: 500rpx;
			width: 4000rpx;
			height: 4000rpx;
			display: flex;
		}
	}
</style>
