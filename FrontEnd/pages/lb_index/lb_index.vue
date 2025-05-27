<!-- lb_index.vue -->
<template>
	<view class="container" >
		<!-- 背景图 -->
		<lb_index_head-vue></lb_index_head-vue>
		<lb_index_body-vue></lb_index_body-vue>
		
	</view>
</template>

<script setup>
	import lb_index_headVue from '../../components/lb_index_head.vue';
	import lb_index_bodyVue from '../../components/lb_index_body.vue';
</script>

<style lang="scss" scoped>
	.container{
		background-image: url('/static/img-index/head.png');
		background-size:cover;
		background-position: center;
		width: 100vw;
		height: 100vh;
		position: relative;
	}

</style>