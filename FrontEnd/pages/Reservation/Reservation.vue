<template>
	<view class="content">
		<header-vue></header-vue>
		<view class="left">
			<sider-nav-vue></sider-nav-vue>
		</view>
		<!-- 右侧内容区域 -->
		<view class="right-content">
			<!-- 页面标题与操作按钮 -->
			<view class="t-b">
				<view class="page-title">||空间预约管理</view>
				<view class="action-buttons">
					<button class="btn-new" @click="handleNewSpace()">新增空间</button>
					<button class="btn-del">批量删除</button>
				</view>
			</view>
			
			<!-- 搜索表单 -->
			<form class="search-form">
				<view class="form-row">
					<view class="form-group">
						<label for="space-name">空间名称:</label>
						<input type="text" id="space-name" placeholder="请输入空间名称" class="input-field"/>
					</view>
					
					<view class="form-group">
						<label for="space-type">空间类型:</label>
						<input type="text" id="space-type" placeholder="请输入空间类型" class="input-field"/>
					</view>
					
					<view class="form-group">
						<label for="status">当前状态:</label>
						<input type="text" id="status" placeholder="请输入状态" class="input-field"/>
					</view>
					
					<view class="form-group">
						<button form-type="submit" class="btn-search">搜索</button>
					</view>
				</view>
			</form>

			<!-- 预约表格 -->
			<table id="spaceTable" class="form_mat">
				<thead>
					<tr>
						<th><checkbox></checkbox></th>
						<th>空间编号</th>
						<th>空间名称</th>
						<th>所在位置</th>
						<th>容纳人数</th>
						<th>开放时间</th>
						<th>预约状态</th>
						<th>最后更新时间</th>
						<th>操作</th>
					</tr>
				</thead>
				<tbody>
					<tr>
						<td><checkbox></checkbox></td>
						<td>R202</td>
						<td>三楼研讨室A</td>
						<td>3F东区</td>
						<td>8人</td>
						<td>08:00-22:00</td>
						<td>
							<text class="status-tag available">可预约</text>
						</td>
						<td>2024-03-01 09:00</td>
						<td>
							<view class="operate-buttons" style="color: #1890ff; gap: 20rpx; display: flex;">
								<view class="btn-edit">编辑</view>
								<view class="btn-delete">删除</view>
							</view>
						</td>
					</tr>
				</tbody>
			</table>
		</view>
	</view>
</template>

<script setup>
	import headerVue from '../../components/header.vue';
	import siderNavVue from '../../components/siderNav.vue';
	
	const handleNewSpace = () => {
		uni.navigateTo({
			url: '/pages/Reservation/NewSpaceForm'
		})
	}
</script>

<style lang="scss" scoped>
	.content {
		display: flex;
		flex-direction: column;
		align-items: center;
		justify-content: center;
	}

	.left{
		position: fixed;
		top:380rpx;
		left: 0rpx;
	}
	
	.right-content{
		margin-left: 800rpx;
		margin-top: 300rpx;
		padding: 20rpx;
		width: calc(100vw - 900rpx);
		height: calc(100vh - 250rpx);
		
		.t-b{
			display: flex;
			justify-content: space-between;
			.page-title{
				font-size: 80rpx;
				color:  #1890ff;
			}
			.action-buttons{
				display: flex;
				gap: 20rpx;
				.btn-new{
					width: 250rpx;
					height: 100rpx;
					color:  #fff;
					background-color: #1890ff;
				}
				.btn-del{
					width: 250rpx;
					height: 100rpx;
					color:  #fff;
					background-color: #FF4D4F;
				}
			}
		}
		
		.search-form{
			display: flex;
			flex-wrap: wrap;
			gap: 20rpx;
			margin-top: 80rpx;
			margin-bottom: 20rpx;
			
			.form-row{
				display: flex;
				gap: 100rpx;
				margin-bottom: 20rpx;
				
				.form-group{
					display: flex;
					gap: 20rpx;
					
					label{
						font-size: 40rpx;
						display: flex;
						align-items: center;
						color: #666;
					}
					
					.input-field{
						padding: 18rpx 24rpx;
						border: 3rpx solid #000;
						border-radius: 10rpx;
						width: 700rpx;
					}
					
					.btn-search{
						width: 250rpx;
						height: 100rpx;
						color:  #fff;
						background-color: #1890ff;
					}
				}
			}
		}
	
		.form_mat{
			width: 100%;
			border-collapse: collapse;
			
			th,td{
				border: 1rpx solid #ccc;
				padding: 8rpx;
				text-align: left;
				vertical-align: middle;
			}
			
			th{
				background-color: #f2f2f2
			}
			
			.status-tag{
				display: inline-block;
				padding: 4rpx 12rpx;
				border-radius: 4rpx;
				font-size: 36rpx;
				
				&.available{
					color: #52c41a;
					background: #f6ffed;
				}
				&.occupied{
					color: #ff4d4f;
					background: #fff1f0;
				}
			}
		}
	}
</style>