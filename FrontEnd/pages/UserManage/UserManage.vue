<template>
	<view class="content">
		<header-vue></header-vue>
		<view class="left">
			<sider-nav-vue></sider-nav-vue>
		</view>
		<!-- 右侧内容区域 -->
		<view class="right-content">
			<!-- 页面标题与搜索栏 -->
			<view class="t-b">
				<view class="page-title">||用户管理功能</view>
				<view class="action-buttons">
					<button class="btn-new" @click="handleNewUser()">新增用户</button>
					<button class="btn-del">批量删除</button>
				</view>
			</view>
			
			<form class="search-form">
				<view class="form-row">
					<view class="form-group">
						<label for="user-name">用户姓名:</label>
						<input type="text" id="user-name" placeholder="请输入用户姓名" class="input-field"/>
					</view>
					
					<view class="form-group">
						<label for="user-id">用户ID:</label>
						<input type="text" id="user-id" placeholder="请输入用户ID" class="input-field"/>
					</view>
					
					<view class="form-group">
						<label for="email">邮箱地址:</label>
						<input type="text" id="email" placeholder="请输入邮箱地址" class="input-field"/>
					</view>
					
					<view class="form-group">
						<label for="phone">手机号码:</label>
						<input type="text" id="phone" placeholder="请输入手机号码" class="input-field"/>
					</view>
					
					<view class="form-group">
						<button form-type="submit" class="btn-search">搜索</button>
					</view>
					
				</view>
			</form>
			
			<table id="userTable" class="form_mat">
			          <thead>
			            <tr>
						  <th><checkbox></checkbox></th>
			              <th>用户ID</th>
			              <th>用户名</th>
			              <th>真实姓名</th>
			              <th>邮箱</th>
			              <th>手机号</th>
			              <th>注册时间</th>
			              <th>用户状态</th>
			              <th>最后更新时间</th>
			              <th>操作</th>
			            </tr>
			          </thead>
					  <tbody>
					   <tr>
							<td><checkbox></checkbox></td>
					        <td>U001</td>
					        <td>user001</td>
					        <td>李四</td>
					        <td>lisi@example.com</td>
					        <td>13812345678</td>
					        <td>2023-05-15</td>
					        <td>启用</td>
					        <td>2024-04-20 15:45</td>
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
	
	const handleNewUser = () =>{
		uni.navigateTo({
			url:'/pages/UserManage/NewUserForm'
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
	
		#userTable{
			width: 100%;
			border-collapse: collapse;
		}
		#userTable th,
		#userTable td{
			border: 1rpx solid #ccc;
			padding: 8rpx;
			text-align: left;
			vertical-align: middle;
		}
		
		#userTable th{
			background-color: #f2f2f2
		}
	}
</style>
