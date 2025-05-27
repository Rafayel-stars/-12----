<!-- 借阅记录 -->
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
          <button class="btn-del" @click="handleBatchDelete">批量删除</button> <!-- 修改按钮名称和事件 -->
        </view>
      </view>

      <form class="search-form">
        <view class="form-row">
          <view class="form-group">
            <label for="user-name">用户姓名:</label>
            <input type="text" id="user-name" v-model="searchName" placeholder="请输入用户姓名" class="input-field"/>
          </view>
          <view class="form-group">
            <button class="btn-search" @click.prevent="handleSearch">搜索</button>
          </view>
        </view>
      </form>

      <table id="borrowTable" class="form_mat">
        <thead>
          <tr>
            <th>
              <checkbox-group @change="handleSelectAll"> <!-- 全选 checkbox 组 -->
                <checkbox :checked="isAllSelected" />
              </checkbox-group>
            </th>
            <th>图书ID</th>
            <th>借阅人账号</th>
            <th>借阅时间</th>
            <th>归还时间</th>
            <th>借阅状态</th>
            <th>操作人编号</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="record in recordInfo" :key="record.id">
            <td>
              <checkbox-group @change="(e) => handleCheckboxChange(record.id, e)"> <!-- 单行 checkbox 组 -->
                <checkbox :value="record.id" :checked="selectedIds.includes(record.id)" />
              </checkbox-group>
            </td>
            <td>{{ record.bookId }}</td>
            <td>{{ record.borrowerName }}</td>
            <td>{{ formatDate(record.borrowTime) }}</td>
            <td>{{ record.returnTime === null ? '未归还' : formatDate(record.returnTime) }}</td>
            <td>{{ record.status ? '未归还' : '已归还' }}</td>
            <td>{{ record.adminId }}</td>
            <td>
              <view class="operate-buttons" style="color: #1890ff; gap: 20rpx; display: flex;">
                <view class="btn-edit" @click="handleEdit(record.id)">编辑</view>
                <view class="btn-return" @click="handleDelete(record.id)">归还</view> <!-- 单个删除改为归还，需根据实际接口调整 -->
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

const recordInfo = ref([]);
const searchName = ref('');
const selectedIds = ref([]); // 存储选中借阅记录的ID

// 全选状态计算属性
const isAllSelected = computed(() => {
  return recordInfo.value.length > 0 && selectedIds.value.length === recordInfo.value.length;
});

// 时间格式化函数（保持不变）
const formatDate = (dateStr) => {
  if (!dateStr) return '';
  const date = new Date(dateStr);
  const year = date.getFullYear();
  const month = String(date.getMonth() + 1).padStart(2, '0');
  const day = String(date.getDate()).padStart(2, '0');
  const hours = String(date.getHours()).padStart(2, '0');
  const minutes = String(date.getMinutes()).padStart(2, '0');
  const seconds = String(date.getSeconds()).padStart(2, '0');
  return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
};

// 统一请求方法（保持不变）
const fetchRecord = async (url) => {
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
      recordInfo.value = Array.isArray(res.data) ? res.data : [res.data];
      selectedIds.value = []; // 清空选中状态
    } else {
      uni.showToast({
        title: '借阅记录数据获取失败',
        icon: 'none'
      });
      recordInfo.value = [];
    }
  } catch (error) {
    uni.showToast({
      title: '请求失败，请检查网络',
      icon: 'none'
    });
    recordInfo.value = [];
  }
};

// 搜索处理（保持不变）
const handleSearch = async () => {
  let apiUrl = 'http://localhost:9000/admin/borrow/search';
  if (searchName.value) {
    apiUrl = `http://localhost:9000/admin/borrow/search/${encodeURIComponent(searchName.value)}`;
  }
  await fetchRecord(apiUrl);
};

// 新增借阅（保持不变）
const handleNewBorrow = () => {
  uni.navigateTo({
    url: '/pages/BorrowManage/NewBorrowForm'
  });
};

// 处理编辑跳转（保持不变，需根据实际编辑接口调整）
const handleEdit = (recordId) => {
  uni.navigateTo({
    url: `/pages/BorrowManage/EditBorrowForm?id=${encodeURIComponent(recordId)}`
  });
};

// 单个删除 - 使用DELETE请求
const handleDelete = async (Id) => {
  const token = uni.getStorageSync('token');
  if (!token) {
    uni.showToast({ title: '请先登录', icon: 'none' });
    return uni.reLaunch({ url: '/pages/lb_login/lb_login' });
  }
  
  try {
    const res = await uni.request({
      url: `http://localhost:9000/admin/borrow/delete/${Id}`,
      method: 'DELETE',
      header: { 
        'Authorization': `Bearer ${token}`,
        'Content-Type': 'application/json' // 添加Content-Type
      }
    });
    
    if (res.statusCode === 200) {
      uni.showToast({ title: '删除成功' });
      await fetchData('http://localhost:9000/admin/user/get');
    } else {
      uni.showToast({ title: '删除失败', icon: 'none' });
    }
  } catch (error) {
    uni.showToast({ title: '请求失败，请检查网络', icon: 'none' });
  }
};



// 处理复选框变化（新增）
const handleCheckboxChange = (recordId, event) => {
  const checkedValues = event.detail.value;
  if (checkedValues.includes(recordId)) {
    if (!selectedIds.value.includes(recordId)) {
      selectedIds.value = [...selectedIds.value, recordId];
    }
  } else {
    selectedIds.value = selectedIds.value.filter(id => id !== recordId);
  }
  console.log('更新后选中的借阅记录ID列表:', selectedIds.value);
};

// 全选/反选处理（新增）
const handleSelectAll = (event) => {
  const isChecked = event.detail.value.length > 0;
  if (isChecked) {
    selectedIds.value = recordInfo.value.map(record => record.id);
  } else {
    selectedIds.value = [];
  }
  console.log('全选操作后选中的借阅记录ID列表:', selectedIds.value);
};

// 批量删除处理（核心修改部分）
const handleBatchDelete = async () => {
  if (selectedIds.value.length === 0) {
    uni.showToast({ title: '请选择要删除的借阅记录', icon: 'none' });
    return;
  }

  const token = uni.getStorageSync('token');
  if (!token) {
    uni.showToast({ title: '请先登录', icon: 'none' });
    return uni.reLaunch({ url: '/pages/lb_login/lb_login' });
  }

  try {
    const res = await uni.request({
      url: 'http://localhost:9000/admin/borrow/delete/batch',
      method: 'DELETE', // 符合接口要求的DELETE方法
      header: {
        'Authorization': `Bearer ${token}`,
        'Content-Type': 'application/json' // 必须指定JSON格式
      },
      data: selectedIds.value , // 按接口要求传递{ids: [id列表]}
    });

    if (res.statusCode === 200) {
      uni.showToast({ title: '批量删除成功' });
      await fetchRecord('http://localhost:9000/admin/borrow/search'); // 刷新数据
      selectedIds.value = []; // 清空选中状态
    } else {
      uni.showToast({ title: '批量删除失败', icon: 'none' });
      console.error('批量删除失败，响应:', res);
    }
  } catch (error) {
    uni.showToast({ title: '请求失败，请检查网络', icon: 'none' });
    console.error('批量删除请求错误:', error);
  }
};

// 初始化加载数据（保持不变）
onMounted(() => {
  fetchRecord('http://localhost:9000/admin/borrow/search');
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