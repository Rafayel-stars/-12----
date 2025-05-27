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
					 <!-- <button class="btn-new" @click="handleNewSpace()">新增空间</button> -->
					<button class="btn-del" @click="handleBatchDelete">批量删除</button>
				</view>
			</view>
			
			<!-- 搜索表单 -->
			<form class="search-form">
				<view class="form-row">
					
					<view class="form-group">
						<label for="applicant-name">申请人账号:</label>
						<input type="text" id="applicant-name" v-model="searchNo" placeholder="请输入申请人账号" class="input-field"/>
					</view>
					
					<view class="form-group">
						<button form-type="submit" class="btn-search" @click.prevent="handleSearch">搜索</button>
					</view>
				</view>
			</form>

			<!-- 预约表格 -->
			<table id="spaceTable" class="form_mat">
				<thead>
					<tr>
						<th>
						  <checkbox-group @change="handleSelectAll">
						    <checkbox :checked="isAllSelected" />
						  </checkbox-group>
						</th>
						<th>申请记录ID</th>
						<th>空间ID</th>
						<th>申请人账号</th>
						<th>预约时间</th>
						<th>使用时长</th>
						<th>预约状态</th>
						<th>操作</th>
					</tr>
				</thead>
				<tbody>
					<tr v-for="reserve in reserveInfo" :key="reserve.applicationId">
						<td>
						  <checkbox-group @change="(e) => handleCheckboxChange(reserve.applicationId, e)">
						    <checkbox :value="reserve.applicationId" :checked="selectedIds.includes(reserve.applicationId)" />
						  </checkbox-group>
						</td>
						<td>{{reserve.applicationId}}</td>
						<td>{{reserve.spaceId}}</td>
						<td>{{reserve.applicantName}}</td>
						<td>{{formatDate(reserve.appointmentTime)}}</td>
						<td>{{reserve.duration}}</td>
						<td>{{
							reserve.status === 0 ? '待审核':'已通过'
						}}	
						</td>
						<td>
							<view class="operate-buttons" style="color: #1890ff; gap: 20rpx; display: flex;">
								<view class="btn-edit" @click="handleEdit(reserve.applicationId)">编辑</view>
								<view class="btn-delete" @click="handleDelete(reserve.applicationId)">删除</view>
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
	import { ref, onMounted, computed } from 'vue';
	const reserveInfo = ref([]);
	const searchNo = ref('')
	const selectedIds = ref([])
	
	// 全选状态计算属性
	const isAllSelected = computed(() => {
	  return reserveInfo.value.length > 0 && selectedIds.value.length === reserveInfo.value.length;
	});
	
	// 统一请求方法
	const fetchReserve = async (url) => {
	  const token = uni.getStorageSync('token');
	  if (!token) {
	    uni.showToast({ title: '请先登录', icon: 'none' });
	    return uni.reLaunch({ url: '/pages/lb_login/lb_login' });
	  }
	  
	  try {
	    const res = await uni.request({
	      url,
	      method: 'GET',
	      header: {
	        'Authorization': `Bearer ${token}`
	      }
	    });
	    console.log("data", res);
	    
	    if (res.statusCode === 200) {
	      reserveInfo.value = Array.isArray(res.data) ? res.data : [res.data];
	      selectedIds.value = []; // 清空选中状态
	      // console.log('数据加载成功，共', userInfo.value.length, '条记录');
	      if (reserveInfo.value.length > 0) {
	        // console.log('第一条数据结构:', userInfo.value[0]);
	      }
	    } else {
	      uni.showToast({
	        title: '数据获取失败',
	        icon: 'none'
	      });
	      reserveInfo.value = [];
	    }
	  } catch (error) {
	    uni.showToast({
	      title: '请求失败，请检查网络',
	      icon: 'none'
	    });
	    reserveInfo.value = [];
	  }
	};
	
	// 搜索处理
	const handleSearch = async () => {
	  
	  let apiUrl = 'http://localhost:9000/admin/space/application/status';
	  
	  if (searchNo.value) {
	    apiUrl = `http://localhost:9000/admin/space/application/status/${encodeURIComponent(searchNo.value)}`;
	  } 
	  
	  await fetchReserve(apiUrl);
	};
	
	// 处理复选框变化
	const handleCheckboxChange = (appId, event) => {
	  console.log('复选框组事件触发，空间预约申请ID:', appId);
	  console.log('事件对象:', event);
	  
	  const checkedValues = event.detail.value;
	  
	  if (checkedValues.includes(appId)) {
	    if (!selectedIds.value.includes(appId)) {
	      selectedIds.value = [...selectedIds.value, appId];
	    }
	  } else {
	    selectedIds.value = selectedIds.value.filter(id => id !== appId);
	  }
	  
	  console.log('更新后选中的ID列表:', selectedIds.value);
	};
	
	// 全选/反选
	const handleSelectAll = (event) => {
	  console.log('全选按钮事件触发');
	  
	  const isChecked = event.detail.value.length > 0;
	  
	  if (isChecked) {
	    selectedIds.value = reserveInfo.value.map(reserve => reserve.applicationId);
	  } else {
	    selectedIds.value = [];
	  }
	  
	  console.log('全选操作后选中的ID列表:', selectedIds.value);
	};
	
	// 单个删除 - 使用DELETE请求
	const handleDelete = async (appId) => {
	  const token = uni.getStorageSync('token');
	  if (!token) {
	    uni.showToast({ title: '请先登录', icon: 'none' });
	    return uni.reLaunch({ url: '/pages/lb_login/lb_login' });
	  }
	  
	  try {
	    const res = await uni.request({
	      url: `http://localhost:9000/admin/space/application/delete/${appId}`,
	      method: 'DELETE',
	      header: { 
	        'Authorization': `Bearer ${token}`,
	        'Content-Type': 'application/json' // 添加Content-Type
	      }
	    });
	    
	    if (res.statusCode === 200) {
	      uni.showToast({ title: '删除成功' });
	      await fetchReserve('http://localhost:9000/admin/space/application/status');
	    } else {
	      uni.showToast({ title: '删除失败', icon: 'none' });
	    }
	  } catch (error) {
	    uni.showToast({ title: '请求失败，请检查网络', icon: 'none' });
	  }
	};
	
	// 批量删除 - 使用DELETE请求
	const handleBatchDelete = async () => {
	  console.log('准备批量删除，选中的ID列表:', selectedIds.value);
	  console.log('选中的ID数量:', selectedIds.value.length);
	  
	  if (selectedIds.value.length === 0) {
	    uni.showToast({ title: '请选择要删除的空间预约记录', icon: 'none' });
	    return;
	  }
	  
	  const token = uni.getStorageSync('token');
	  if (!token) {
	    uni.showToast({ title: '请先登录', icon: 'none' });
	    return uni.reLaunch({ url: '/pages/lb_login/lb_login' });
	  }
	  
	  try {
	    const res = await uni.request({
	      url: 'http://localhost:9000/admin/space/application/delete/batch',
	      method: 'DELETE', // 修改为DELETE请求
	      header: {
	        'Authorization': `Bearer ${token}`,
	        'Content-Type': 'application/json' // 必须指定JSON格式
	      },
	      data: selectedIds.value, // 直接传递ID数组
	    });
	    
	    if (res.statusCode === 200) {
	      uni.showToast({ title: '批量删除成功' });
	      await fetchReserve('http://localhost:9000/admin/space/application/status');
	      selectedIds.value = [];
	    } else {
	      uni.showToast({ title: '批量删除失败', icon: 'none' });
	      console.error('批量删除失败，响应:', res);
	    }
	  } catch (error) {
	    uni.showToast({ title: '请求失败，请检查网络', icon: 'none' });
	    console.error('批量删除请求错误:', error);
	  }
	};
	
	// 时间格式化函数
	const formatDate = (dateStr) => {
	  if (!dateStr) return '-';
	  
	  try {
	    // 处理ISO格式的日期字符串
	    const date = new Date(dateStr);
	    
	    // 获取年、月、日
	    const year = date.getFullYear();
	    const month = String(date.getMonth() + 1).padStart(2, '0');
	    const day = String(date.getDate()).padStart(2, '0');
	    
	    // 获取时、分、秒
	    const hours = String(date.getHours()).padStart(2, '0');
	    const minutes = String(date.getMinutes()).padStart(2, '0');
	    const seconds = String(date.getSeconds()).padStart(2, '0');
	    
	    // 返回格式化后的字符串：YYYY-MM-DD HH:MM:SS
	    return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
	  } catch (error) {
	    console.error('日期格式化失败:', error);
	    return dateStr;
	  }
	};
	// 新增记录
	const handleNewSpace = () => {
		uni.navigateTo({
			url: '/pages/Reservation/NewSpaceForm'
		})
	}

	//处理编辑跳转
	const handleEdit = (appId) => {
	  uni.navigateTo({
	    url: `/pages/Reservation/EditSpaceForm?applicationId=${encodeURIComponent(appId)}`
		
	  });
	  // console.log('applicantName',appName)
	};
// 初始化加载数据
onMounted(() => {
  fetchReserve('http://localhost:9000/admin/space/application/status');
});
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