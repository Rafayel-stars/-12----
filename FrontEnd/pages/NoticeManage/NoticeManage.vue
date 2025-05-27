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
        <view class="page-title">||公告管理功能</view>
        <view class="action-buttons">
          <button class="btn-new" @click="handleNewAnnouncement">新增公告</button>
          <!-- <button class="btn-del" @click="handleBatchDelete">批量删除</button> -->
        </view>
      </view>

      <!-- 搜索表单 -->
      <form class="search-form">
        <view class="form-row">
          
          <view class="form-group">
            <label for="status">发布类型:</label>
            <input 
              type="text" 
              id="status" 
              v-model="searchStatus" 
              placeholder="请输入发布类型" 
              class="input-field"
            />
          </view>

          

          <view class="form-group">
            <button class="btn-search" @click.prevent="handleSearch">搜索</button>
          </view>
        </view>
      </form>

      <!-- 数据表格 -->
      <table id="announcementTable" class="form_mat">
        <thead>
          <tr>
            <th>
              <checkbox-group @change="handleSelectAll">
                <checkbox :checked="isAllSelected" />
              </checkbox-group>
            </th>
            <th>公告ID</th>
            <th>标题</th>
            <th>内容预览</th>
            <th>图片</th>
            <th>发布人</th>
            <th>发布时间</th>
            <th>类型</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="announcement in announcementList" :key="announcement.id">
            <td>
              <checkbox-group @change="(e) => handleCheckboxChange(announcement.id, e)">
                <checkbox :value="announcement.id" :checked="selectedIds.includes(announcement.id)" />
              </checkbox-group>
            </td>
            <td>{{ announcement.id }}</td>
            <td>{{ announcement.title }}</td>
            <td>{{ announcement.content.length > 20 ? announcement.content.substring(0, 20) + '...' : announcement.content }}</td>
            <td>
              <image 
                v-if="announcement.picture" 
                :src="announcement.picture" 
                mode="aspectFill" 
                class="preview-image"
              />
              <view v-else>无图片</view>
            </td>
            <td>{{ announcement.author }}</td>
            <td>{{ formatDate(announcement.publishtime) }}</td>
            <td>{{ getStatusLabel(announcement.status) }}</td>
            <td>
              <view class="operate-buttons" style="color: #1890ff; gap: 20rpx; display: flex;">
                <view class="btn-edit" @click="handleEdit(announcement.id)">编辑</view>
                <view class="btn-delete" @click="handleDelete(announcement.id)">删除</view>
              </view>
            </td>
          </tr>
          <tr v-if="announcementList.length === 0">
            <td colspan="9" style="text-align: center; color: #999; padding: 20rpx 0;">暂无公告数据</td>
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
const searchStatus = ref('')
// 公告类型映射
const statusOptions = ref([
  { value: 1, label: '最新公告' },
  { value: 2, label: '新闻时讯' },
  { value: 3, label: '资源动态' },
  { value: 4, label: '培训/活动' }
]);

// 数据与状态管理
const announcementList = ref([]);

const selectedIds = ref([]); // 存储选中的公告ID

// 全选状态计算属性
const isAllSelected = computed(() => {
  return announcementList.value.length > 0 && selectedIds.value.length === announcementList.value.length;
});

// 获取状态文本
const getStatusLabel = (status) => {
  const item = statusOptions.value.find(item => item.value === status);
  return item ? item.label : '未知类型';
};

// 新增公告
const handleNewAnnouncement = () => {
  uni.navigateTo({
    url: '/pages/NoticeManage/NewNoticeForm'
  });
};

// 编辑公告
const handleEdit = (id) => {
  uni.navigateTo({
    url: `/pages/NoticeManage/EditNoticeForm?id=${encodeURIComponent(id)}`
  });
};

// 数据获取
const fetchData = async (url) => {
  const token = uni.getStorageSync('token')
  if(!token){
	  uni.showToast({
	  	title:'请先登录',
		icon:'none'
	  })
	  return uni.reLaunch({
	  	url:'/pages/lb_login/lb_login'
	  })
  }
  
  try {
  	const res = await uni.request({
  		url,
		method:'GET',
		header:{
			'Authorization': `Bearer ${token}`
		}
  	})
	console.log('data',res.data)
	if(res.statusCode === 200){
		announcementList.value = Array.isArray(res.data) ? res.data : [res.data],
		selectedIds.value = []
	}else{
		uni.showToast({
			title:'数据获取失败',
			icon:'none'
		})
		announcementList.value = []
	}
  } catch (error) {
  	uni.showToast({
  		title:'请求失败，请检查网络',
		icon:'none'
  	})
	announcementList.value = []
  }
};

// 搜索处理
const searchIndex = ref('')
const handleSearch = async () => {
  // 从状态选项中查找匹配的 value
  const selectedStatus = statusOptions.value.find(
    option => option.label === searchStatus.value.trim() // 去除输入前后空格
  );
  let apiUrl = 'http://localhost:9000/admin/announcement/status';

  if (selectedStatus) { // 匹配到有效状态
    searchIndex.value = selectedStatus.value; // 修正此处，使用 .value
    apiUrl = `http://localhost:9000/admin/announcement/status/${encodeURIComponent(selectedStatus.value)}`;
  } else if (searchStatus.value.trim()) { // 输入无效值时提示（可选）
    uni.showToast({ title: '请输入有效的发布类型（如：最新公告、新闻时讯等）', icon: 'none' });
    return;
  }

  await fetchData(apiUrl);
};

// 复选框处理逻辑
const handleCheckboxChange = (id, event) => {
  const checkedValues = event.detail.value;
  checkedValues.includes(id) 
    ? !selectedIds.value.includes(id) && (selectedIds.value = [...selectedIds.value, id]) 
    : (selectedIds.value = selectedIds.value.filter(itemId => itemId !== id));
};

// 全选/反选
const handleSelectAll = (event) => {
  const isChecked = event.detail.value.length > 0;
  isChecked 
    ? (selectedIds.value = announcementList.value.map(item => item.id)) 
    : (selectedIds.value = []);
};

// 删除公告（模拟）
const handleDelete = async(id) => {
  const token = uni.getStorageSync('token')
  if(!token){
	  uni.showToast({
	  	title:'请先登录',
		icon:'none'
	  })
	return uni.reLaunch({
		url:'/pages/lb_login/lb_login'
	})  
  }
  
  try {
  	const res = await uni.request({
  		url:`http://localhost:9000/admin/announcement/delete/${id}`,
		method:'DELETE',
		header:{
			'Authorization': `Bearer ${token}`,
		}
  	})
	console.log('res.data',res.data)
	if(res.statusCode === 200){
		uni.showToast({
			title:'删除成功'
		})
		await fetchData('http://localhost:9000/admin/announcement/status')
	}else{
		uni.showToast({
			title:'删除失败',
			icon:'none'
		})
	}
	
  } catch (error) {
  	uni.showToast({
  		title:'请求失败，请检查网络',
		icon:'none'
  	})
  }
  
  
  
};

// 批量删除（模拟）
const handleBatchDelete = () => {
  if (selectedIds.value.length === 0) {
    uni.showToast({ title: '请选择要删除的公告', icon: 'none' });
    return;
  }
  
  uni.showModal({
    title: '批量删除',
    content: `确定要删除选中的${selectedIds.value.length}条公告吗？`,
    success: (res) => {
      if (res.confirm) {
        // 模拟批量删除
        announcementList.value = announcementList.value.filter(item => !selectedIds.value.includes(item.id));
        selectedIds.value = [];
        uni.showToast({ title: '批量删除成功' });
      }
    }
  });
};

// 日期格式化函数
const formatDate = (dateStr) => {
  if (!dateStr) return '-';
  try {
    const date = new Date(dateStr);
    return `${date.getFullYear()}-${(date.getMonth() + 1).toString().padStart(2, '0')}-${date.getDate().toString().padStart(2, '0')} ${date.getHours().toString().padStart(2, '0')}:${date.getMinutes().toString().padStart(2, '0')}`;
  } catch (error) {
    return dateStr;
  }
};

// 初始化加载数据
onMounted(() => {
  fetchData('http://localhost:9000/admin/announcement/status');
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

  #announcementTable {
    width: 100%;
    border-collapse: collapse;
  }
  #announcementTable th,
  #announcementTable td {
    border: 1rpx solid #ccc;
    padding: 8rpx;
    text-align: left;
    vertical-align: middle;
  }
  
  #announcementTable th {
    background-color: #f2f2f2
  }
  
  .preview-image {
    width: 100px;
    height: 50px;
    object-fit: cover;
    border-radius: 4px;
  }
}
</style>