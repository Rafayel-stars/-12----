<!-- lb_newspace_nav -->
<template>
  <view class="container">
    <lb_index_head-vue></lb_index_head-vue>
    <view class="content">
      <view class="left">
        <!-- 路径提示 -->
        <text>您当前的界面：首页》服务》新生空间》馆舍导航</text>

        <!-- 导航栏 -->
        <view class="nav">
          <view class="nav-item">馆舍导航</view>
          <view 
            class="back-btn" 
            @click="handleBack"
          >
            <text style="color: #fff;">‹ 返回</text>
          </view>
        </view>

        <!-- 主体内容 -->
        <view class="map-container">
          <!-- 校区选择器 -->
          <picker 
            mode="selector" 
            :range="campuses" 
            range-key="name"
            @change="changeCampus"
            class="campus-selector"
          >
            <view class="selector-box">
              <text class="current-campus">{{ currentCampus.name }}</text>
              <uni-icons type="arrowdown" color="#8B4513" size="20"></uni-icons>
            </view>
          </picker>

          <!-- 地图容器 -->
          <map 
            id="campusMap"
            :latitude="currentCampus.latitude"
            :longitude="currentCampus.longitude"
            :markers="markers"
            :scale="16"
            show-location
            class="campus-map"
            @markertap="handleMarkerTap"
          >
            <cover-view class="map-tips">点击标记查看导航</cover-view>
          </map>

          <!-- 校区信息 -->
          <view class="info-card">
            <view class="card-header">
              
              <text class="campus-name">{{ currentCampus.name }}</text>
			  
            </view>
            <view class="card-content">
              <text class="info-item">地址：{{ currentCampus.address }}</text>
              <text class="info-item">开放时间：{{ currentCampus.openTime }}</text>
              <text class="info-item">联系电话：{{ currentCampus.tel }}</text>
            </view>
          </view>
        </view>
      </view>
      <view class="right">
      		  <lb_side_nav-vue
      			:section="'服务'" 
      			:current-path="currentPagePath"
      		  />
      </view>
    </view>
  </view>
</template>

<script setup>
import { ref, computed } from 'vue';
import lb_index_headVue from '@/components/lb_index_head.vue';
import lb_side_navVue from '@/components/lb_side_nav.vue';
// 获取当前页面路径
const currentPagePath = ref('/pages/Service/lb_newspace/lb_newspace_nav');
// 校区数据（请确认实际坐标）
const campuses = ref([
  {
    name: '石牌校区图书馆',
    latitude: 23.139220,
    longitude: 113.347954,
    address: '广州市天河区中山大道西55号',
    openTime: '08:00-22:00',
    tel: '020-85214563'
  },
  {
    name: '大学城校区图书馆',
    latitude: 23.051143,
    longitude: 113.380521,
    address: '广州市番禺区大学城外环西路378号',
    openTime: '07:30-22:30',
    tel: '020-39310000'
  },
  {
    name: '南海校区图书馆',
    latitude: 23.146194,
    longitude: 113.026773,
    address: '佛山市南海区狮山镇南海软件科技园',
    openTime: '08:30-21:30',
    tel: '0757-86687868'
  },
  {
    name: '汕尾校区图书馆',
    latitude: 22.731635,
    longitude: 115.372639,
    address: '汕尾市城区马宫街道',
    openTime: '09:00-21:00',
    tel: '0660-3800000'
  }
]);

const selectedIndex = ref(0);

// 计算属性获取当前校区
const currentCampus = computed(() => campuses.value[selectedIndex.value]);

// 地图标记配置
const markers = computed(() => [
  {
    id: 0,
    latitude: currentCampus.value.latitude,
    longitude: currentCampus.value.longitude,
    iconPath: '/static/img-index/map-marker.png',
    width: 40,
    height: 40,
    callout: {
      content: currentCampus.value.name,
      color: '#ffffff',
      bgColor: '#8B4513',
      padding: 10,
      borderRadius: 8
    }
  }
]);

// 切换校区
const changeCampus = (e) => {
  selectedIndex.value = e.detail.value;
};

// 处理标记点击
const handleMarkerTap = () => {
  uni.openLocation({
    latitude: currentCampus.value.latitude,
    longitude: currentCampus.value.longitude,
    name: currentCampus.value.name,
    address: currentCampus.value.address,
    fail: (err) => {
      uni.showToast({
        title: '请安装地图应用',
        icon: 'none'
      });
    }
  });
};

// 返回处理
const handleBack = () => {
  uni.reLaunch({
  	url:'/pages/Service/lb_newspace/lb_newspace'
  })
};
</script>

<style lang="scss" scoped>
.container {
  background-image: url('/static/img-index/background.png');
  background-size: cover;
  background-position: center;
  width: 100vw;
  height: 100vh;
  position: relative;
  
  .content {
	display: flex;
    position: absolute;
    background-color: rgba(255, 255, 255, 0.8);
    top: 400rpx;
    left: 500rpx;
    width: 4000rpx;
    height: 2300rpx;

    .left {
      width: 70%;
      padding: 20rpx 40rpx;

      text {
        display: block;
        margin: 30rpx 0;
        padding: 30rpx;
        font-size: 60rpx;
        color: #8B4513;
      }

      .nav {
        width: 100%;
        height: 150rpx;
        margin: 60rpx 0;
        border-bottom: 10rpx solid #8B4513;
        display: flex;
        justify-content: space-between;
        align-items: center;

        .nav-item {
          padding: 0 50rpx;
          height: 100%;
          background-color: #8B4513;
          color: #fff;
          font-size: 60rpx;
          display: flex;
          align-items: center;
        }

        .back-btn {
          padding: 0 50rpx;
          height: 100%;
          background-color: #8B4513;
          display: flex;
          align-items: center;
          font-size: 60rpx;
        }
      }

      .map-container {
        width: 100%;
        height: 1800rpx;

        .campus-selector {
          margin: 50rpx 0;
          .selector-box {
			width: 100%;
			height: 100rpx;
            padding: 30rpx;
            background: #f5f5f5;
            border-radius: 20rpx;
            display: flex;
            align-items: center;
            justify-content: space-between;
            box-sizing: border-box;
            .current-campus {
              font-size: 50rpx;
			  
              color: #8B4513;
            }
          }
        }

        .campus-map {
          width: 100%;
          height: 1200rpx;
          border-radius: 30rpx;
          box-shadow: 0 10rpx 30rpx rgba(0,0,0,0.1);

          .map-tips {
            position: absolute;
            bottom: 50rpx;
            left: 50%;
            transform: translateX(-50%);
            background: rgba(139, 69, 19, 0.9);
            color: #fff;
            padding: 20rpx 50rpx;
            border-radius: 50rpx;
            font-size: 40rpx;
          }
        }

        .info-card {
          width: 100%;
          height: 400rpx; // 固定高度
          margin-top: 20rpx;
         
          background: #fff;
          border-radius: 30rpx;
          box-shadow: 0 10rpx 30rpx rgba(0,0,0,0.05);
          display: flex; // 启用flex布局
          flex-direction: column; // 垂直排列
        
          .card-header {
            
            display: flex;
            align-items: center;
            
            border-bottom: 4rpx solid #eee; // 增加分割线
        
            .campus-name {
              font-size: 44rpx; // 调整标题字号
              color: #8B4513;
              margin-left: 20rpx;
            }
          }
        
          .card-content {
            flex: 1; // 占据剩余空间
            overflow: auto; // 内容超出时显示滚动条
            
        
            .info-item {
              display: block;
              font-size: 40rpx; // 设置具体字号
			  
              color: #666;
              
              padding-left:0 0 0 40rpx;
            }
          }
        }
      }
    }
	.right{
		margin-top: 480rpx;
		margin-left: 100rpx;
		width: 500rpx;
	}
  }
}
</style>