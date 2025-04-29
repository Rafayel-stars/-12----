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
				<view class="page-title">||借阅记录管理</view>
				<view class="action-buttons">
					<button class="btn-new" @click="handleNewBorrow()">新增借阅</button>
					<button class="btn-del">批量归还</button>
				</view>
			</view>
			
			<form class="search-form">
				<view class="form-row">
					<view class="form-group">
						<label for="borrow-no">借阅编号:</label>
						<input type="text" id="borrow-no" placeholder="请输入借阅编号" class="input-field"/>
					</view>
					
					<view class="form-group">
						<label for="user-name">用户姓名:</label>
						<input type="text" id="user-name" placeholder="请输入用户姓名" class="input-field"/>
					</view>
					
					<view class="form-group">
						<label for="book-name">图书名称:</label>
						<input type="text" id="book-name" placeholder="请输入图书名称" class="input-field"/>
					</view>
					
					<view class="form-group">
						<label for="borrow-time">借阅时间:</label>
						<input type="text" id="borrow-time" placeholder="请输入借阅时间" class="input-field"/>
					</view>
					
					<view class="form-group">
						<button form-type="submit" class="btn-search">搜索</button>
					</view>
					
				</view>
			</form>
			
			<table id="borrowTable" class="form_mat">
				<thead>
					<tr>
						<th><checkbox></checkbox></th>
						<th>借阅编号</th>
						<th>用户姓名</th>
						<th>图书名称</th>
						<th>借阅时间</th>
						<th>应还时间</th>
						<th>归还时间</th>
						<th>归还状态</th>
						<th>逾期天数</th>
						<th>操作</th>
					</tr>
				</thead>
				<tbody>
					<tr>
						<td><checkbox></checkbox></td>
						<td>BOR20240423001</td>
						<td>李四</td>
						<td>JavaScript高级编程</td>
						<td>2024-04-15</td>
						<td>2024-05-15</td>
						<td>2025-03-01 14:30</td>
						<td>未归还</td>
						<td class="status-overdue">逾期10天</td>
						<td>
							<view class="operate-buttons" style="color: #1890ff; gap: 20rpx; display: flex;">
								<view class="btn-edit">编辑</view>
								<view class="btn-return">归还</view>
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
	const handleNewBorrow = () =>{
		uni.navigateTo({
			url:'/pages/BorrowManage/NewBorrowForm'
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
			align-items: center;
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
			margin-bottom: 40rpx;
			
			.form-row{
				display: flex;
				gap: 100rpx;
				margin-bottom: 20rpx;
				
				.form-group{
					display: flex;
					gap: 20rpx;
					align-items: center;
					
					label{
						font-size: 40rpx;
						color: #666;
					}
					
					.input-field{
						padding: 18rpx 24rpx;
						border: 3rpx solid #000;
						border-radius: 10rpx;
						width: 600rpx;
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
	
		#borrowTable{
			width: 100%;
			border-collapse: collapse;
		}
		
		#borrowTable th,
		#borrowTable td{
			border: 1rpx solid #ccc;
			padding: 8rpx;
			text-align: left;
			vertical-align: middle;
		}
		
		#borrowTable th{
			background-color: #f2f2f2;
			font-weight: 500;
		}
		
		.status-overdue{
			color: #FF4D4F;
			font-weight: 600;
		}
		
		.operate-buttons{
			display: flex;
			gap: 20rpx;
			font-size: 40rpx;
			cursor: pointer;
			
			.btn-edit{
				padding: 8rpx 16rpx;
				border: 2rpx solid #1890ff;
				border-radius: 6rpx;
			}
			
			.btn-return{
				padding: 8rpx 16rpx;
				border: 2rpx solid #52c41a;
				color: #52c41a;
				border-radius: 6rpx;
			}
		}
	}
</style>