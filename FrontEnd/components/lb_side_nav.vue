<!-- components/SideNav.vue -->
<template>
  <view class="side-nav">
    <!-- 当前板块标题 -->
    <view class="nav-title" style="background-color:#8B4513;color: white;font-weight: 700;font-size: 60rpx;">{{ currentSection }}</view>
    
    <!-- 子菜单列表 -->
    <view 
      v-for="(item, index) in currentMenu" 
      :key="index"
      class="nav-item"
      :class="{ active: isActive(item) }"
      @click="navigateTo(item.path)"
    >
      {{ item.name }}
    </view>
  </view>
</template>

<script setup>
import { computed } from 'vue';

const props = defineProps({
  // 当前所在板块（如'服务'/'资源'）
  section: {
    type: String,
    required: true
  },
  // 当前页面路径（用于高亮判断）
  currentPath: {
    type: String,
    required: true
  }
});

// 导航菜单数据结构
const navData = {
  服务: [
    { name: '新生空间', 
	  path: '/pages/Service/lb_newspace',  // 修改为父级路径
      isPrefix: true   ,//新增匹配方式标识  
	  },
    { name: '书刊借阅', path: '/pages/Service/lb_borrow/lb_borrow' },
    { name: '空间预约', path: '/pages/Service/lb_space_reserve/lb_space_reserve' },
    { name: '图书捐赠', path: '/pages/Service/lb_book_donation/lb_book_donation' },
	{ name: '文献搜索', path: '/pages/Service/lb_literature_search/lb_literature_search' },
	{ name: '投诉入口', path: '/pages/Service/lb_complaint_entrance/lb_complaint_entrance' },
	{ name: '自助服务', path: '/pages/Service/lb_self_service/lb_self_service' },
	{ name: '信息素养教育', path: '/pages/Service/lb_literacy_education/lb_literacy_education' },
	{ name: '知识产权服务', path: '/pages/Service/lb_property_service/lb_property_service' },
  ],
  资源: [
    { name: '数据库导航', path: '/pages/Source/lb_sql_navigator/lb_sql_navigator' },
    { name: '特色资源', path: '/pages/Source/lb_unique_source/lb_unique_source' },
    { name: '教学参考书目', path: '/pages/Source/lb_reference_list/lb_reference_list' },
	{ name: '资源荐购', path: '/pages/Source/lb_source_recommendation/lb_source_recommendation' },
	{ name: '云展厅', path: '/pages/Source/lb_cloud_exhibit/lb_cloud_exhibit' },
	{ name: '更多', path: '/pages/Source/lb_more/lb_more' },
  ]
};

// 计算当前板块对应的菜单
const currentMenu = computed(() => navData[props.section] || []);
// 新增计算属性
const currentSection = computed(() => props.section); 
// 判断是否当前选中项
const isActive = (item) => {
  return item.isPrefix 
    ? props.currentPath.startsWith(item.path)  //<!-- 前缀匹配 -->
    : props.currentPath === item.path;        //<!-- 精确匹配 -->
};


// 路由跳转
const navigateTo = (path) => {
  uni.navigateTo({ url: path });
};
</script>

<style lang="scss" scoped>
.side-nav {
  width: 500rpx;
  // padding: 30rpx;
  background-color: #f5f5f5;
  box-shadow: #666;

  .nav-title {
    font-size: 36rpx;
    color: #8B4513;
    padding: 20rpx;
    margin-bottom: 30rpx;
    border-bottom: 2rpx solid #ddd;
  }

  .nav-item {
    padding: 25rpx 30rpx;
    margin: 15rpx 0;
    font-size: 40rpx;
    color: #666;
    border-radius: 8rpx;
    transition: all 0.3s;
	border-bottom: 2rpx solid #ddd;

    &.active {
        // background-color: #ddd; // 浅色背景
        color: #8B4513; // 目标字体颜色
        font-weight: bold;
      }

    &:hover {
      background-color: #eee;
      cursor: pointer;
    }
  }
}
</style>