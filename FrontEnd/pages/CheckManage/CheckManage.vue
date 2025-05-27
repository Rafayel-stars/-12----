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
        <view class="page-title">||借阅审核管理</view>
        <view class="action-buttons">
          <button class="btn-new" @click="handleNewApplication">新增借阅申请</button>
          <button class="btn-del" @click="handleBatchAudit">批量处理审核</button>
        </view>
      </view>

      <!-- 搜索表单 -->
      <form class="search-form">
        <view class="form-row">
          <view class="form-group">
            <label for="borrower-no">借阅者编号:</label>
            <input 
              type="text" 
              id="borrower-no" 
              v-model="searchBorrowerNo" 
              placeholder="请输入借阅者编号" 
              class="input-field"
            />
          </view>

          <view class="form-group">
            <label for="library-code">图书馆编号:</label>
            <input 
              type="text" 
              id="library-code" 
              v-model="searchLibraryCode" 
              placeholder="请输入图书馆编号" 
              class="input-field"
            />
          </view>

          <view class="form-group">
            <label for="status">审核状态:</label>
           <!-- <picker 
              v-model="searchStatus" 
              range="statusOptions" 
              range-key="label"
              class="input-field"
            > -->
              <input 
                type="text" 
                :value="searchStatus ? searchStatus.label : '请选择状态'" 
                placeholder="请选择状态" 
                class="input-field"
              />
            <!-- </picker> -->
          </view>

          <view class="form-group">
            <button class="btn-search" @click.prevent="handleSearch">搜索</button>
          </view>
        </view>
      </form>

      <!-- 数据表格 -->
      <table id="auditTable" class="form_mat">
        <thead>
          <tr>
            <th>
              <checkbox-group @change="handleSelectAll">
                <checkbox :checked="isAllSelected" />
              </checkbox-group>
            </th>
            <th>申请ID</th>
            <th>图书馆编号</th>
            <th>借阅者编号</th>
            <th>借书日期</th>
            <th>预计归还时间</th>
            <th>审核状态</th>
            <th>审核人编号</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="application in applicationList" :key="application.id">
            <td>
              <checkbox-group @change="(e) => handleCheckboxChange(application.id, e)">
                <checkbox :value="application.id" :checked="selectedIds.includes(application.id)" />
              </checkbox-group>
            </td>
            <td>{{ application.id }}</td>
            <td>{{ application.library_code }}</td>
            <td>{{ application.borrower_no }}</td>
            <td>{{ formatDate(application.borrow_date) }}</td>
            <td>{{ formatDate(application.expected_return_date) }}</td>
            <td>
              {{ 
                application.status === 0 ? '待审核' : 
                application.status === 1 ? '审核通过' : 
                application.status === 2 ? '审核拒绝' : '未知状态'
              }}
            </td>
            <td>{{ application.reviewer_no }}</td>
            <td>
              <view class="operate-buttons" style="color: #1890ff; gap: 20rpx; display: flex;">
                <view class="btn-edit" @click="handleAudit(application.id)">处理审核</view>
              </view>
            </td>
          </tr>
          <tr v-if="applicationList.length === 0">
            <td colspan="9" style="text-align: center; color: #999; padding: 20rpx 0;">暂无待审核的借阅申请</td>
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

// 模拟数据
const statusOptions = ref([
  { value: 0, label: '待审核' },
  { value: 1, label: '审核通过' },
  { value: 2, label: '审核拒绝' }
]);

// 表单数据
const applicationList = ref([]);
const searchBorrowerNo = ref('');
const searchLibraryCode = ref('');
const searchStatus = ref(null);
const selectedIds = ref([]); // 存储选中的申请ID

// 全选状态计算属性
const isAllSelected = computed(() => {
  return applicationList.value.length > 0 && selectedIds.value.length === applicationList.value.length;
});

// 新增借阅申请
const handleNewApplication = () => {
  uni.navigateTo({
    url: '/pages/BorrowAudit/NewBorrowForm'
  });
};

// 处理审核跳转
const handleAudit = (applicationId) => {
  uni.navigateTo({
    url: `/pages/BorrowAudit/AuditForm?id=${encodeURIComponent(applicationId)}`
  });
};

// 统一数据获取方法
const fetchData = async () => {
  // 模拟API请求，实际需替换为真实接口
  const mockData = [
    {
      id: 1,
      library_code: 'LIB001',
      borrower_no: 'U001',
      borrow_date: '2025-05-10 09:30:00',
      expected_return_date: '2025-05-20 09:30:00',
      status: 0,
      reviewer_no: 'ADMIN001'
    },
    {
      id: 2,
      library_code: 'LIB002',
      borrower_no: 'U002',
      borrow_date: '2025-05-11 14:00:00',
      expected_return_date: '2025-05-25 14:00:00',
      status: 1,
      reviewer_no: 'ADMIN002'
    }
  ];
  
  applicationList.value = mockData;
  selectedIds.value = []; // 清空选中状态
};

// 搜索处理
const handleSearch = async () => {
  // 这里可以添加实际的搜索逻辑和API请求
  // 目前直接使用模拟数据
  await fetchData();
};

// 复选框处理逻辑（与用户管理组件一致）
const handleCheckboxChange = (applicationId, event) => {
  const checkedValues = event.detail.value;
  checkedValues.includes(applicationId) 
    ? !selectedIds.value.includes(applicationId) && (selectedIds.value = [...selectedIds.value, applicationId]) 
    : (selectedIds.value = selectedIds.value.filter(id => id !== applicationId));
};

const handleSelectAll = (event) => {
  const isChecked = event.detail.value.length > 0;
  isChecked 
    ? (selectedIds.value = applicationList.value.map(item => item.id)) 
    : (selectedIds.value = []);
};

// 批量审核处理（模拟逻辑）
const handleBatchAudit = () => {
  if (selectedIds.value.length === 0) {
    uni.showToast({ title: '请选择需要处理的申请', icon: 'none' });
    return;
  }
  uni.showToast({ title: `已选中${selectedIds.value.length}条申请进行处理`, icon: 'none' });
};

// 日期格式化函数（与用户管理组件一致）
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
  fetchData();
});
</script>

<style lang="scss" scoped>
/* 完全复用用户管理界面的样式，无需修改 */
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

  #auditTable {
    width: 100%;
    border-collapse: collapse;
  }
  #auditTable th,
  #auditTable td {
    border: 1rpx solid #ccc;
    padding: 8rpx;
    text-align: left;
    vertical-align: middle;
  }
  
  #auditTable th {
    background-color: #f2f2f2
  }
}
</style>