<template>
  <view class="content">
    <view class="left">
      <view class="search">
        <view class="s-box">
          <input type="text" placeholder="请输入关键字" />
          <button class="search-btn" form-type="submit">搜索</button>
        </view>
      </view>
      <view class="source-serve">
        <view class="source">
          <view class="s-head"><text style="color: #8B4513; margin-left: 60rpx;">资源</text></view>
          <view class="s-grid">
            <view
              class="s-body"
              v-for="item in icon.iconSource"
              :key="item.id"
			  @click="handleLink(item)"
            >
              <image :src="item.url" mode="aspectFill"></image>
              <text>{{item.message}}</text>
            </view>
          </view>
          <view class="links">
            <view 
              class="link-box"
              v-for="item in icon.linkTo"
              :key="item.id"
			  @click="handleLink(item)"
            >  
              <image src="/static/img-index/方块.png"></image>
              <navigator :url="item.url">{{item.title}}</navigator>
            </view>
          </view>
        </view>
        <view class="serve">
          <view class="r-head"><text style="color: #8B4513; margin-left: 60rpx;">服务</text> </view>
          <view class="r-grid">
            <view
              class="r-body"
              v-for="item in icon.iconServe"
              :key="item.id"
			  @click="handleLink(item)"
            >
              <image :src="item.url" mode="aspectFill"></image>
              <text>{{item.message}}</text>
            </view>
          </view>
        </view>
      </view>
    </view>
    <view class="right">
      <view class="notice-box">
        <!-- 导航栏 -->
        <view class="nav">
          <view
            v-for="item in navItems"
            :key="item"
            :class="['nav-item', currentNav === item ? 'active' : '']"
            @click="currentNav = item"
          >
            {{ item }}
          </view>
        </view>
        <!-- 内容展示 -->
        <view class="notice">
          <view
            v-for="item in getCurrentList()"
            :key="item.id"
            class="notice-item"
          >
            <view class="title">{{ item.message }}</view>
            <view class="date">{{ item.date }}</view>
          </view>
        </view>
        <text class="more">更多资源</text>
      </view>
      <!-- 轮播图部分 -->
      <view class="swiper-box">
        <swiper
          :current="currentIndex"
          @change="onSwiperChange"
          :autoplay="true"
		  circular
        >
          <swiper-item
            v-for="item in getImgs.Imgs"
            :key="item.id"
          >
            <image :src="item.url" mode="aspectFill"></image>
          </swiper-item>
        </swiper>

      </view>
    </view>
  </view>
</template>

<script setup>
import { reactive, ref } from 'vue';

const icon = reactive({
  iconSource: [
    { id: 1, url: '/static/img-index/数据库导航.png', message: "数据库导航",src:'/pages/Source/lb_source_navigator/lb_source_navigator' },
    { id: 2, url: '/static/img-index/特色资源.png', message: "特色资源",src:'/pages/Source/lb_unique_source/lb_unique_source' },
    { id: 3, url: '/static/img-index/书籍借阅.png', message: "教学参考书目",src:'/pages/Source/lb_reference_list/lb_reference_list' },
    { id: 4, url: '/static/img-index/徽章.png', message: "资源荐购",src:'/pages/Source/lb_source_recommendation/lb_source_recommendation' },
    { id: 5, url: '/static/img-index/云展厅.png', message: "云展厅",src:'/pages/Source/lb_cloud_exhibit/lb_cloud_exhibit' },
    { id: 6, url: '/static/img-index/更多.png', message: "更多" ,src:'/pages/Source/lb_more/lb_more'},
  ],
  iconServe: [
    { id: 1, url: '/static/img-index/新生空间.png', message: "新生空间",src:'/pages/Service/lb_newspace/lb_newspace'},
    { id: 2, url: '/static/img-index/书籍借阅.png', message: "书籍借阅",src:'/pages/Service/lb_borrow/lb_borrow' },
    { id: 3, url: '/static/img-index/空间预约.png', message: "空间预约",src:'/pages/Service/lb_space_reserve/lb_space_reserve' },
    { id: 4, url: '/static/img-index/图书捐赠.png', message: "图书捐赠",src:'/pages/Service/lb_book_donation/lb_book_donation' },
    { id: 5, url: '/static/img-index/文献搜索.png', message: "文献搜索",src:'/pages/Service/lb_literature_search/lb_literature_search' },
    { id: 6, url: '/static/img-index/投诉入口.png', message: "投诉入口",src:'/pages/Service/lb_complaint_entrance/lb_complaint_entrance' },
    { id: 7, url: '/static/img-index/云展厅.png', message: "自助服务",src:'/pages/Service/lb_self_service/lb_self_serviceNotice' },
    { id: 8, url: '/static/img-index/信息素养教育.png', message: "信息素养教育",src:'/pages/Service/lb_literacy_education/lb_literacy_education' },
    { id: 9, url: '/static/img-index/知识产权服务.png', message: "知识产权服务",src:'https://ipisc.scnu.edu.cn/#/index'},
  ],
  linkTo: [
    { id: 1, src: "https://www.cnki.net/", title: "中国知网" },
    { id: 2, src: "https://www.wanfangdata.com.cn/", title: "万方数据知识服务平台" },
    { id: 3, src: "https://www.duxiu.com/", title: "读秀知识库" },
    { id: 4, src: "http://www.chaoxing.com/", title: "超星电子图书" },
    { id: 5, src: "http://lib.360edm.com/ ", title: "环球英语多媒体数据库" },
    { id: 6, src: "https://www.sciencedirect.com/", title: "Elsevier" },
    { id: 7, src: "https://link.springer.com/", title: "Springer" },
    { id: 8, src: "https://www.webofscience.com/", title: "SCI" },
    { id: 9, src: "https://onlinelibrary.wiley.com/", title: "Wiley" },
  ],
});

const notice = reactive({
  note: [
    { id: 1, message: "图书馆2024-2025学年第二学期借书流程", date: "2026-03-17" },
    { id: 2, message: "关于图书馆流通业务暂停服务的通知", date: "2025-01-13" },
    { id: 3, message: "党员学习空间临时关闭通知", date: "2025-01-06" },
    { id: 4, message: "党委第一监督执纪专项检查组召开图书馆监督会议", date: "2025-01-03" },
  ],
  news: [
    { id: 1, message: "图书馆举办2024年学术讲座", date: "2024-12-20" },
    { id: 2, message: "图书馆新增电子资源库", date: "2024-12-15" },
  ],
  source: [
    { id: 1, message: "新增超星电子图书资源", date: "2024-12-10" },
    { id: 2, message: "万方数据知识服务平台更新", date: "2024-12-05" },
  ],
  activity: [
    { id: 1, message: "图书馆2024年读书月活动", date: "2024-11-30" },
    { id: 2, message: "图书馆培训活动报名开始", date: "2024-11-25" },
  ],
});

const navItems = ['最新公告', '新闻时讯', '资源动态', '培训/活动'];
const currentNav = ref('最新公告');

// 根据当前导航项获取对应的数据
const getCurrentList = () => {
  switch (currentNav.value) {
    case '最新公告':
      return notice.note;
    case '新闻时讯':
      return notice.news;
    case '资源动态':
      return notice.source;
    case '培训/活动':
      return notice.activity;
    default:
      return [];
  }
};

// 模拟轮播图数据
const getImgs = reactive({
  Imgs: [
    { id: 1, url: "/static/img-index/photo.png" },
    { id: 2, url: "/static/img-index/photo.png" },
    { id: 3, url: "/static/img-index/photo.png" },
    { id: 4, url: "/static/img-index/photo.png" },
    { id: 5, url: "/static/img-index/photo.png" }
  ]
});

//实现知网等网站跳转
const handleLink = (item) =>{
	if(!item.src){
		uni.showToast({
			title:'功能正在开发中',
			icon:'none'
		});
		return;
	}
	//判断是不是为外部链接
	if(item.src.startsWith('http')){
		uni.showModal({
			title:'提示',
			content:'即将打开外部链接，是否继续？',
			success(res){
				
				if(res.confirm){
					//使用浏览器打开
					window.open(item.src,'_blank');
				}
			}
		});
	}else{
		//内部页面跳转，不确定要不要保留
		uni.navigateTo({
			url:item.src,
			fail(err){
				console.error('跳转失败：',err);
				uni.showToast({
					title:'页面不存在',
					icon:'error'
				})
			}
		})
	}
}



// 当前图片索引
const currentIndex = ref(0);

// 监听 swiper 切换事件
const onSwiperChange = (event) => {
  currentIndex.value = event.detail.current;
};

// 切换到上一张图片
const prevImage = () => {
  if (currentIndex.value > 0) {
    currentIndex.value--;
  }
};

// 切换到下一张图片
const nextImage = () => {
  if (currentIndex.value < getImgs.Imgs.length - 1) {
    currentIndex.value++;
  }
};
</script>

<style lang="scss" scoped>
.content {
  position: absolute;
  background: rgba(255, 255, 255, 0.8);
  top: 400rpx;
  left: 500rpx;
  width: 4000rpx;
  height: 2300rpx;
  display: flex;

  .left {
    .search {
      opacity: 0.7;
      background-image: url('/static/img-index/1.png');
      background-size: cover;
      background-position: center;
      width: 2500rpx;
      height: 350rpx;
      display: flex;
      justify-content: center;
      align-items: center;

      .s-box {
        display: flex;
        align-items: center;
        justify-content: center;
        padding: 20rpx;
        width: 1500rpx;

        input {
          flex: 1;
          width: 1500rpx;
          height: 80rpx;
          margin-right: 20rpx;
          padding: 0 20rpx;
          border: 2rpx solid #000;
          border-radius: 8rpx;
        }

        .search-btn {
          width: 160rpx;
          height: 80rpx;
          line-height: 80rpx;
          padding: 0;
          font-size: 28rpx;
          background-color: #007AFF;
          color: white;
          border-radius: 8rpx;
        }
      }
    }

    .source-serve {
      display: flex;

      .source {
        width: 1200rpx;
        height: 1800rpx;
        margin: 80rpx auto;
        background-color: #fff;
        border: 1rpx solid #ccc;

        .s-head {
          border-top: 15rpx solid #8B4513;
          font-size: 50rpx;
          font-weight: bold;
          margin-bottom: 20rpx;
          background-color: #f3f3f3;
        }

        .s-grid {
          display: grid;
          grid-template-columns: repeat(3, 1fr);

          .s-body {
            display: flex;
            justify-content: center;
            align-items: center;
            flex-direction: column;
            background-color: #fff;
            border: 1rpx solid #ccc;
            width: 100%;
            height: 300rpx;

            image {
              width: 80rpx;
              height: 80rpx;
              display: block;
            }

            text {
              margin-top: 50rpx;
              font-size: 40rpx;
              font-weight: 700;
            }
          }
        }

        .links {
          margin: 100rpx 20rpx;

          .link-box {
            image {
              width: 80rpx;
              height: 80rpx;
            }

            display: flex;
            align-items: center;
            width: 100%;
            height: 100rpx;
            font-size: 60rpx;
            font-weight: 700;
          }
        }
      }

      .serve {
        width: 1200rpx;
        height: 1800rpx;
        margin: 80rpx auto;
        background-color: #fff;
        border: 1rpx solid #ccc;

        .r-head {
          border-top: 15rpx solid #8B4513;
          font-size: 50rpx;
          font-weight: bold;
          margin-bottom: 20rpx;
          background-color: #f3f3f3;
        }

        .r-grid {
          display: grid;
          grid-template-columns: repeat(3, 1fr);

          .r-body {
            display: flex;
            justify-content: center;
            align-items: center;
            flex-direction: column;
            background-color: #fff;
            border: 1rpx solid #ccc;
            width: 100%;
            height: 300rpx;

            image {
              width: 80rpx;
              height: 80rpx;
              display: block;
            }

            text {
              margin-top: 50rpx;
              font-size: 40rpx;
              font-weight: 700;
            }
          }
        }
      }
    }
  }

  .right {
    width: 100%;
    background-color: #fff;
    height: 2225rpx;

    .nav {
      display: flex;
      justify-content: space-between;
      padding: 0rpx 20rpx;
      background-color: #f7f7f9;

      .nav-item {
        font-size: 50rpx;
        font-weight: 700;
        padding: 20rpx;
        color: #666;
        cursor: pointer;

        &.active {
          color: #8B4513;
          font-weight: bold;
          border-bottom: 8rpx solid #8B4513;
        }
      }
    }

    .notice {
      .notice-item {
        padding: 30rpx;
        background-color: #fff;
        border-radius: 8rpx;
        border-bottom: 5rpx solid #f1f7f9;

        .title {
          font-size: 40rpx;
          font-weight: bold;
          color: #333;
        }

        .date {
          font-size: 40rpx;
          color: #999;
          margin-top: 10rpx;
        }
      }
    }

    .more {
      height: 100rpx;
      font-size: 50rpx;
      font-weight: 700;
      color: #8B4513;
      display: flex;
      justify-content: center;
      align-items: center;
      border-bottom: 5rpx solid #f1f7f9;
      background-color: #f9f9f9;
    }

    .swiper-box {
      width: 100%;
      height: 1200rpx;
      margin-top: 20rpx;
      background-color: #fff;
      border-radius: 16rpx;
      overflow: hidden;

      swiper {
        width: 100%;
        height: 100%;
      }

      image {
        width: 100%;
        height: 100%;
      }

      .t-n {
        display: flex;
        justify-content: space-between;
        align-items: center;
        padding: 20rpx;
        background-color: #f9f9f9;

        .img-title {
          font-size: 28rpx;
          color: #333;
        }

        
      }
    }
  }
}
</style>