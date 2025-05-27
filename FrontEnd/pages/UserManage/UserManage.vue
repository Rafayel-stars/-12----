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
          <button class="btn-del" @click="handleBatchDelete">批量删除</button>
        </view>
      </view>

      <form class="search-form">
        <view class="form-row">
          <view class="form-group">
            <label for="user-name">用户姓名:</label>
            <input type="text" id="user-name" v-model="searchName" placeholder="请输入用户姓名" class="input-field"/>
          </view>

          <view class="form-group">
            <label for="phone">手机号码:</label>
            <input type="text" id="phone" v-model="searchPhone" placeholder="请输入手机号码" class="input-field"/>
          </view>

          <view class="form-group">
            <button class="btn-search" @click.prevent="handleSearch">搜索</button>
          </view>
        </view>
      </form>

      <table id="userTable" class="form_mat">
        <thead>
          <tr>
            <th>
              <checkbox-group @change="handleSelectAll">
                <checkbox :checked="isAllSelected" />
              </checkbox-group>
            </th>
            <th>用户账号</th>
            <th>姓名</th>
            <th>年龄</th>
            <th>性别</th>
            <th>电话</th>
            <th>用户类型</th>
            <th>状态</th>
            <th>注册时间</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="user in userInfo" :key="user.id">
            <td>
              <checkbox-group @change="(e) => handleCheckboxChange(user.id, e)">
                <checkbox :value="user.id" :checked="selectedIds.includes(user.id)" />
              </checkbox-group>
            </td>
            <td>{{ user.no }}</td>
            <td>{{ user.name }}</td>
            <td>{{ user.age }}</td>
            <td>{{ 
              user.sex === 0? '未知' : 
              user.sex === 1? '男':'女'
            }}</td>
            <td>{{ user.phone }}</td>
            <td>{{ user.userType }}</td>
            <td>{{ user.is_valid? '有效' : '无效' }}</td>
            <td>{{ formatDate(user.registerTime) }}</td>
            <td>
                  <view class="operate-buttons" style="color: #1890ff; gap: 20rpx; display: flex;">
                    <view class="btn-edit" @click="handleEdit(user.no)">编辑</view> <!-- 传递no参数 -->
                    <view class="btn-delete" @click="handleDelete(user.id)">删除</view>
                  </view>
            </td>
          </tr>
          <tr v-if="userInfo.length === 0">
            <td colspan="9" style="text-align: center; color: #999; padding: 20rpx 0;">暂无数据</td>
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

const userInfo = ref([]);
const searchName = ref('');
const searchPhone = ref('');
const selectedIds = ref([]); // 存储选中用户的ID

// 全选状态计算属性
const isAllSelected = computed(() => {
  return userInfo.value.length > 0 && selectedIds.value.length === userInfo.value.length;
});

// 新增用户
const handleNewUser = () => {
  uni.navigateTo({
    url: '/pages/UserManage/NewUserForm'
  });
};
//处理编辑跳转
const handleEdit = (userNo) => {
  uni.navigateTo({
    url: `/pages/UserManage/EditUserForm?no=${encodeURIComponent(userNo)}` // 传递no参数
  });
};

// 统一请求方法
const fetchData = async (url) => {
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
      userInfo.value = Array.isArray(res.data) ? res.data : [res.data];
      selectedIds.value = []; // 清空选中状态
      // console.log('数据加载成功，共', userInfo.value.length, '条记录');
      if (userInfo.value.length > 0) {
        // console.log('第一条数据结构:', userInfo.value[0]);
      }
    } else {
      uni.showToast({
        title: '数据获取失败',
        icon: 'none'
      });
      userInfo.value = [];
    }
  } catch (error) {
    uni.showToast({
      title: '请求失败，请检查网络',
      icon: 'none'
    });
    userInfo.value = [];
  }
};

// 搜索处理
const handleSearch = async () => {
  if (searchName.value && searchPhone.value) {
    uni.showToast({
      title: '请使用单一条件搜索',
      icon: 'none'
    });
    return;
  }
  
  let apiUrl = 'http://localhost:9000/admin/user/get';
  
  if (searchName.value) {
    apiUrl = `http://localhost:9000/admin/user/getByName/${encodeURIComponent(searchName.value)}`;
  } else if (searchPhone.value) {
    apiUrl = `http://localhost:9000/admin/user/getByPhone/${encodeURIComponent(searchPhone.value)}`;
  }
  
  await fetchData(apiUrl);
};

// 处理复选框变化
const handleCheckboxChange = (userId, event) => {
  console.log('复选框组事件触发，用户ID:', userId);
  console.log('事件对象:', event);
  
  const checkedValues = event.detail.value;
  
  if (checkedValues.includes(userId)) {
    if (!selectedIds.value.includes(userId)) {
      selectedIds.value = [...selectedIds.value, userId];
    }
  } else {
    selectedIds.value = selectedIds.value.filter(id => id !== userId);
  }
  
  console.log('更新后选中的ID列表:', selectedIds.value);
};

// 全选/反选
const handleSelectAll = (event) => {
  console.log('全选按钮事件触发');
  
  const isChecked = event.detail.value.length > 0;
  
  if (isChecked) {
    selectedIds.value = userInfo.value.map(user => user.id);
  } else {
    selectedIds.value = [];
  }
  
  console.log('全选操作后选中的ID列表:', selectedIds.value);
};

// 单个删除 - 使用DELETE请求
const handleDelete = async (userId) => {
  const token = uni.getStorageSync('token');
  if (!token) {
    uni.showToast({ title: '请先登录', icon: 'none' });
    return uni.reLaunch({ url: '/pages/lb_login/lb_login' });
  }
  
  try {
    const res = await uni.request({
      url: `http://localhost:9000/admin/user/delete/${userId}`,
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

// 批量删除 - 使用DELETE请求
const handleBatchDelete = async () => {
  console.log('准备批量删除，选中的ID列表:', selectedIds.value);
  console.log('选中的ID数量:', selectedIds.value.length);
  
  if (selectedIds.value.length === 0) {
    uni.showToast({ title: '请选择要删除的用户', icon: 'none' });
    return;
  }
  
  const token = uni.getStorageSync('token');
  if (!token) {
    uni.showToast({ title: '请先登录', icon: 'none' });
    return uni.reLaunch({ url: '/pages/lb_login/lb_login' });
  }
  
  try {
    const res = await uni.request({
      url: 'http://localhost:9000/admin/user/delete/batch',
      method: 'DELETE', // 修改为DELETE请求
      header: {
        'Authorization': `Bearer ${token}`,
        'Content-Type': 'application/json' // 必须指定JSON格式
      },
      data: selectedIds.value, // 直接传递ID数组
    });
    
    if (res.statusCode === 200) {
      uni.showToast({ title: '批量删除成功' });
      await fetchData('http://localhost:9000/admin/user/get');
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

// 初始化加载数据
onMounted(() => {
  fetchData('http://localhost:9000/admin/user/get');
});
</script>

<style lang="scss" scoped>
/* 保持原有样式完全不变 */
.content {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
}

.left {
  position: fixed;
  top: 380rpx;
  left: 0rpx;
}

.right-content {
  margin-left: 800rpx;
  margin-top: 300rpx;
  padding: 20rpx;
  width: calc(100vw - 900rpx);
  height: calc(100vh - 250rpx);
  .t-b {
    display: flex;
    justify-content: space-between;
    .page-title {
      font-size: 80rpx;
      color: #1890ff;
    }
    .action-buttons {
      display: flex;
      gap: 20rpx;
      .btn-new {
        width: 250rpx;
        height: 100rpx;
        color: #fff;
        background-color: #1890ff;
      }
      
      .btn-del {
        width: 250rpx;
        height: 100rpx;
        color: #fff;
        background-color: #FF4D4F;
      }
    }
  }
  
  .search-form {
    display: flex;
    flex-wrap: wrap;
    gap: 20rpx;
    margin-top: 80rpx;
    margin-bottom: 20rpx;
    
    .form-row {
      display: flex;
      gap: 100rpx;
      margin-bottom: 20rpx;
      
      .form-group {
        display: flex;
        gap: 20rpx;
        
        label {
          font-size: 40rpx;
          display: flex;
          align-items: center;
          color: #666;
        }
        
        .input-field {
          padding: 18rpx 24rpx;
          border: 3rpx solid #000;
          border-radius: 10rpx;
          width: 600rpx;
        }
        
        .btn-search {
          width: 250rpx;
          height: 100rpx;
          color: #fff;
          background-color: #1890ff;
        }
      }
    }
  }

  #userTable {
    width: 100%;
    border-collapse: collapse;
  }
  #userTable th,
  #userTable td {
    border: 1rpx solid #ccc;
    padding: 8rpx;
    text-align: left;
    vertical-align: middle;
  }
  
  #userTable th {
    background-color: #f2f2f2
  }
}
</style>