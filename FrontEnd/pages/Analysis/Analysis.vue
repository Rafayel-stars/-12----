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
import { ref } from 'vue'
import headerVue from '@/components/header.vue'
import siderNavVue from '@/components/siderNav.vue'

// 图表全局配置
const chartOptions = ref({
  padding: [30, 30, 30, 30],
  enableScroll: false,
  legend: {
    show: true,
    position: 'bottom',
    padding: 20,
    lineHeight: 40,
    margin: 10
  },
  extra: {
    pie: {
      activeOpacity: 0.5,
      activeRadius: 10,
      labelWidth: 15
    },
    column: {
      type: 'group',
      width: 30,
      activeBgColor: '#000000',
      activeBgOpacity: 0.08
    }
  }
})

// 性别数据
const genderChartData = ref({
  series: [{
    data: [
      { name: '男', value: 65 },
      { name: '女', value: 35 }
    ],
    color: ['#1890ff', '#ff4d4f']
  }]
})

// 书籍分类数据
const categoryChartData = ref({
  categories: ['计算机', '文学', '历史', '经济', '科幻'],
  series: [{
    name: '借阅量',
    data: [120, 80, 45, 60, 30],
    color: '#1890ff'
  }]
})

// 返回按钮处理
const handleBack = () => {
  uni.navigateBack()
}
</script>

<style lang="scss" scoped>
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
    grid-template-columns: repeat(auto-fit, minmax(650rpx, 1fr));
    gap: 40rpx;
    margin-top: 40rpx;
  }

  .chart-card {
    background: #ffffff;
    border-radius: 24rpx;
    box-shadow: 0 6rpx 18rpx rgba(0,0,0,0.06);
    padding: 30rpx;
    transition: transform 0.2s;
    
    &:active {
      transform: scale(0.98);
    }
    
    .card-header {
      border-bottom: 2rpx solid #eee;
      padding-bottom: 30rpx;
      margin-bottom: 30rpx;
    }

    .chart-title {
      font-size: 48rpx;
      color: #333;
      font-weight: 600;
      position: relative;
      padding-left: 30rpx;
      
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
      height: 700rpx !important;
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