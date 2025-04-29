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
				<view class="page-title">||图书管理功能</view>
				<view class="action-buttons">
					<button class="btn-new" @click="handleNewBook()">新增图书</button>
					<button class="btn-del">批量删除</button>
				</view>
			</view>
			
			<form class="search-form">
				<view class="form-row">
					<view class="form-group">
						<label for="book-name">图书名称:</label>
						<input type="text" id="book-name" placeholder="请输入图书名称" class="input-field"/>
					</view>
					
					<view class="form-group">
						<label for="isbn">ISBN编号:</label>
						<input type="text" id="isbn" placeholder="请输入ISBN编号" class="input-field"/>
					</view>
					
					<view class="form-group">
						<label for="category">所属分类:</label>
						<input type="text" id="category" placeholder="请输入分类名称" class="input-field"/>
					</view>
					
					<view class="form-group">
						<button form-type="submit" class="btn-search">搜索</button>
					</view>
					
				</view>
			</form>
			
			<table id="bookTable" class="form_mat">
			          <thead>
			            <tr>
			              <!-- <th><input type="checkbox" id="selectAll" class="custom-checkbox"></th> -->
						 
						  <th> <checkbox></checkbox></th>
			              <th>图书编号</th>
			              <th>图书名称</th>
			              <th>作者</th>
			              <th>所属分类</th>
			              <th>ISBN编号</th>
			              <th>出版日期</th>
			              <th>库存数量</th>
			              <th>最后更新时间</th>
			              <th>操作</th>
			            </tr>
			          </thead>
					  <tbody>
					   <tr>
					        <!-- <td><input type="checkbox" class="custom-checkbox"></td> -->
							<td><checkbox></checkbox></td>
					        <td>B001</td>
					        <td>JavaScript高级编程</td>
					        <td>张三</td>
					        <td>计算机</td>
					        <td>978-7-121-12345-6</td>
					        <td>2023-01-01</td>
					        <td>50</td>
					        <td>2024-03-01 14:30</td>
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
	const handleNewBook = () =>{
		uni.navigateTo({
			url:'/pages/BookManage/NewBookForm'
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
		// background-color: pink;
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
					// flex-direction: column;
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
	
		#bookTable{
			width: 100%;
			border-collapse: collapse;
		}
		#bookTable th,
		#bookTable td{
			border: 1rpx solid #ccc;
			padding: 8rpx;
			text-align: left;
			vertical-align: middle;
		}
		
		#bookTable th{
			background-color: #f2f2f2
		}
	}
</style>
