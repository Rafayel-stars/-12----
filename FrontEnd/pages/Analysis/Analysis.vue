<template>
  <view class="content">
    <header-vue></header-vue>
    <view class="left">
      <sider-nav-vue></sider-nav-vue>
    </view>

    <!-- 右侧统计内容区域 -->
    <view class="right-content stats-content">
      <!-- 统计标题 -->
      <view class="t-b">
        <view class="page-title">||数据统计中心</view>
      </view>

      <!-- 统计图表容器 -->
      <view class="chart-container">
        <!-- 性别占比饼图 -->
        <view class="chart-card">
          <view class="card-header">
            <text class="chart-title">读者性别占比</text>
          </view>
          <qiun-data-charts 
            type="pie" 
            :chartData="genderChartData"
            :opts="chartOptions"
            canvasId="genderChart"
            :key="genderChartKey" 
          />
        </view>

        <!-- 书籍分类柱状图 -->
        <view class="chart-card">
          <view class="card-header">
            <text class="chart-title">书籍借阅分类统计</text>
          </view>
          <qiun-data-charts
            type="column"
            :chartData="categoryChartData"
            :opts="chartOptions"
            canvasId="categoryChart"
            :key="categoryChartKey" 
          />
        </view>
      </view>

      <view class="action-footer">
        <button class="btn-back" @click="handleBack">返回管理</button>
      </view>
    </view>
  </view>
</template>

<script setup>
import { ref, onMounted, computed, watch, nextTick } from 'vue'
import headerVue from '@/components/header.vue'
import siderNavVue from '@/components/siderNav.vue'

// 图表全局配置 - 优化了legend和padding
const chartOptions = ref({
  padding: [20, 20, 30, 20], // 调整内边距
  enableScroll: false,
  legend: {
    show: true,
    position: 'bottom',
    padding: 15,
    lineHeight: 30, // 减小图例行高
    margin: 10
  },
  extra: {
    pie: {
      activeOpacity: 0.5,
      activeRadius: 8, // 减小选中效果
      labelWidth: 12 // 减小标签宽度
    },
    column: {
      type: 'group',
      width: 25, // 减小柱子宽度
      activeBgColor: '#000000',
      activeBgOpacity: 0.08
    }
  }
})

// 性别数据
const genderChartData = ref({
  series: [{
    data: [
      { name: '男', value: 0 },
      { name: '女', value: 0 },
      { name: '未知', value: 0 }
    ],
    color: ['#1890ff', '#ff4d4f', '#999']
  }]
})

// 书籍分类数据
const categoryChartData = ref({
  categories: [],
  series: [{
    name: '借阅量',
    data: [],
    color: '#1890ff'
  }]
})

// 为图表生成动态 key（基于数据内容）
const genderChartKey = computed(() => {
  return genderChartData.value.series[0].data.map(item => `${item.name}-${item.value}`).join('-')
})

const categoryChartKey = computed(() => {
  return categoryChartData.value.series[0].data.join('-')
})

// 监听数据变化，确保 DOM 更新后再渲染
watch([genderChartData, categoryChartData], () => {
  nextTick(() => {
    console.log('数据更新，DOM 已渲染完成')
    // 可添加额外的图表初始化逻辑（如果组件需要）
  })
}, { deep: true })

// 返回按钮处理
const handleBack = () => {
  uni.navigateTo({
    url: '/pages/UserManage/UserManage'
  })
}

const fetchData = async () => {
  try {
    // 获取token
    const token = uni.getStorageSync('token');
    if (!token) {
      console.error('未找到token');
      return;
    }
    
    // 获取用户数据
    const userResponse = await uni.request({
      url: 'http://localhost:9000/admin/user/get',
      method: 'GET',
      header: {
        'Authorization': `Bearer ${token}`
      }
    })
    
    // 检查响应结构
    if (userResponse.statusCode !== 200 || !userResponse.data) {
      console.error('用户数据请求失败', userResponse);
      return;
    }
    
    const users = userResponse.data;
    console.log('用户数据:', users);
    
    // 统计性别
    let maleCount = 0;
    let femaleCount = 0;
    let unknownCount = 0;
    
    users.forEach(user => {
      if (user.sex === 1) {
        maleCount++;
      } else if (user.sex === 2) {
        femaleCount++;
      } else {
        unknownCount++;
      }
    })
    
    // 使用Vue的响应式更新机制
    genderChartData.value = {
      series: [{
        data: [
          { name: '男', value: maleCount },
          { name: '女', value: femaleCount },
          { name: '未知', value: unknownCount }
        ],
        color: ['#1890ff', '#ff4d4f', '#999']
      }]
    }

    // 获取图书数据
    const bookResponse = await uni.request({
      url: 'http://localhost:9000/admin/book/get',
      method: 'GET',
      header: {
        'Authorization': `Bearer ${token}`
      }
    })
    
    // 检查响应结构
    if (bookResponse.statusCode !== 200 || !bookResponse.data) {
      console.error('图书数据请求失败', bookResponse);
      return;
    }
    
    const books = bookResponse.data;
    console.log('图书数据:', books);
    
    // 统计书籍分类
    const categoryCount = {};
    books.forEach(book => {
      if (book.category !== undefined && book.category !== null) {
        if (categoryCount[book.category]) {
          categoryCount[book.category]++;
        } else {
          categoryCount[book.category] = 1;
        }
      }
    })
    
    const categories = Object.keys(categoryCount);
    const categoryData = Object.values(categoryCount);
    
    // 使用Vue的响应式更新机制
    categoryChartData.value = {
      categories: categories,
      series: [{
        name: '借阅量',
        data: categoryData,
        color: '#1890ff'
      }]
    }
    
    console.log('更新后的性别数据:', genderChartData.value);
    console.log('更新后的分类数据:', categoryChartData.value);
    
  } catch (error) {
    console.error('数据请求出错:', error);
  }
}

onMounted(() => {
  fetchData();
})
</script>

<style lang="scss" scoped>
/* 优化后的样式 */
.content {
  display: flex;
  min-height: 100vh;
  background: #f5f7fa;
  position: relative;
}

.left {
  position: fixed;
  top: 380rpx;
  left: 0;
  z-index: 1000;
}

.right-content.stats-content {
  flex: 1;
  margin-left: 800rpx;
  margin-top: 300rpx;
  padding: 40rpx 60rpx;
  min-height: calc(100vh - 380rpx);
  
  .chart-container {
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(600rpx, 1fr)); /* 优化最小宽度 */
    gap: 60rpx; /* 增加间距 */
    margin-top: 40rpx;
  }

  .chart-card {
    background: #ffffff;
    border-radius: 24rpx;
    box-shadow: 0 6rpx 18rpx rgba(0,0,0,0.06);
    padding: 24rpx; /* 减小内边距 */
    transition: transform 0.2s;
    
    &:active {
      transform: scale(0.98);
    }
    
    .card-header {
      border-bottom: 2rpx solid #eee;
      padding-bottom: 24rpx; /* 减小底部间距 */
      margin-bottom: 24rpx; /* 减小底部间距 */
    }

    .chart-title {
      font-size: 48rpx;
      color: #333;
      font-weight: 600;
      position: relative;
      padding-left: 30rpx;
      margin-bottom: 24rpx; /* 增加底部间距 */
      
      &::before {
        content: '';
        position: absolute;
        left: 0;
        top: 50%;
        transform: translateY(-50%);
        width: 8rpx;
        height: 60%;
        background: #1890ff;
        border-radius: 4rpx;
      }
    }

    .qiun-data-charts {
      width: 100% !important;
      height: 600rpx !important; /* 优化图表高度 */
    }
  }

  .action-footer {
    margin-top: 60rpx;
    text-align: right;
    
    .btn-back {
      width: 280rpx;
      height: 90rpx;
      line-height: 90rpx;
      background: #1890ff;
      color: white;
      border-radius: 12rpx;
      font-size: 36rpx;
      transition: all 0.2s;
      
      &:active {
        opacity: 0.8;
        transform: scale(0.95);
      }
    }
  }
}

.page-title {
  font-size: 80rpx !important;
  color: #1890ff !important;
  letter-spacing: 4rpx;
  padding-left: 30rpx;
  margin-bottom: 40rpx;
  position: relative;
  
  &::after {
    content: '';
    position: absolute;
    bottom: -10rpx;
    left: 30rpx;
    width: 120rpx;
    height: 6rpx;
    background: #1890ff;
  }
}
</style>