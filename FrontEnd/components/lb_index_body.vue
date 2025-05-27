<template>
  <view class="content">
    <view class="left">
      <view class="search">
        <view class="s-box">
          <!-- 搜索输入框 -->
          <input type="text" placeholder="请输入关键字" v-model="searchKey" />
          <!-- 搜索模式选择（Radio组） -->
          <radio-group 
            class="search-mode" 
            v-model="searchMode"
            @change="handleRadioChange">
            <radio value="title">标题</radio>
            <radio value="isbn">ISBN</radio>
            <radio value="author">作者</radio>
            <radio value="category">分类</radio>
          </radio-group>
          <!-- 搜索按钮 -->
          <button class="search-btn" @click="handleSearch">搜索</button>
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
            @click="changeNav(item)"
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
			@click="handleNoticeDetail(item.id)"
          >
            <view class="title">{{ item.title.length > 20 ? item.title.slice(0,20)+'...':item.title }}</view>
            <view class="date">{{ formatDate(item.publishtime) }}</view>
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
             v-for="item in getCurrentList()"
            :key="item.id"
            @click="handleNoticeDetail(item.id)"
          >
            <image :src="item.picture" mode="aspectFill"></image>
          </swiper-item>
        </swiper>

      </view>
    </view>
  </view>
</template>

<script setup>
import { reactive, ref, onMounted, watch } from 'vue';

// 状态码与notice属性映射
const statusMap = {
  1: 'note',
  2: 'news',
  3: 'source',
  4: 'activity'
};

// 新增搜索相关变量
const searchKey = ref(''); // 搜索关键字
const searchMode = ref('title'); // 默认搜索模式（标题）

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
  note: [],
  news: [],
  source: [],
  activity: []
});

const navItems = ['最新公告', '新闻时讯', '资源动态', '培训/活动'];
const currentNav = ref('最新公告');
const currentNavIndex = ref(1); // 默认是最新公告(1)

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

// 根据导航项获取状态码
const getStatusByNav = (navItem) => {
  return navItems.indexOf(navItem) + 1;
};

// 加载公告数据
const loadNotice = async (status) => {
  const token = uni.getStorageSync('token');
  if (!token) {
    uni.showToast({ title: '请先登录', icon: 'none' });
    return uni.reLaunch({ url: '/pages/lb_login/lb_login' });
  }

  const property = statusMap[status];
  if (!property) {
    console.error('无效的状态码', status);
    return;
  }

  try {
    const res = await uni.request({
      url: `http://localhost:9000/reader/announcement/list/status/${status}`,
      method: 'GET',
      header: {
        'Authorization': `Bearer ${token}`,
        'Content-Type': 'application/json'
      },
    });

    if (res.statusCode === 200) {
      notice[property] = Array.isArray(res.data) ? res.data : [res.data];
      console.log(`加载${navItems[status-1]}成功`, notice[property]);
    } else {
      uni.showToast({
        title: `${navItems[status-1]}数据获取失败`,
        icon: 'none'
      });
    }
  } catch (error) {
    console.error('请求出错:', error);
    uni.showToast({
      title: '请求失败，请检查网络',
      icon: 'none'
    });
  }
};

// 切换导航项
const changeNav = (navItem) => {
  currentNav.value = navItem;
  const status = getStatusByNav(navItem);
  currentNavIndex.value = status;
  loadNotice(status);
};

onMounted(() => {
  const initialStatus = getStatusByNav(currentNav.value);
  currentNavIndex.value = initialStatus;
  loadNotice(initialStatus);
});

// 监听导航变化，动态加载数据
watch(currentNav, (newNav) => {
  const status = getStatusByNav(newNav);
  currentNavIndex.value = status;
  loadNotice(status);
});

// // 模拟轮播图数据
// const getImgs = reactive({
//   Imgs: [
//     { id: 1, url: "/static/img-index/photo.png" },
//     { id: 2, url: "/static/img-index/photo.png" },
//     { id: 3, url: "/static/img-index/photo.png" },
//     { id: 4, url: "/static/img-index/photo.png" },
//     { id: 5, url: "/static/img-index/photo.png" }
//   ]
// });

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
    //内部页面跳转
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

// 有关搜索
// 修改：添加 radio-group 的 change 事件处理
const handleRadioChange = (e) => {
  searchMode.value = e.detail.value; // 确保值更新
  console.log('当前搜索模式:', searchMode.value); // 调试用
};

// 新增搜索处理函数
const handleSearch = () => {
  if (!searchKey.value.trim()) {
    uni.showToast({ title: '请输入搜索关键字', icon: 'none' });
    return;
  }

  const keyword = searchKey.value.trim();
  
  console.log('当前搜索模式', searchMode.value); // 新增日志
  
  let apiPath = '';
  switch (searchMode.value) {
    case 'title':
      apiPath = `http://localhost:9000/reader/book/searchbytitle/${keyword}`;
      break;
    case 'isbn':
      apiPath = `http://localhost:9000/reader/book/searchbyisbn/${keyword}`;
      break;
    case 'author':
      apiPath = `http://localhost:9000/reader/book/searchbyauthor/${keyword}`;
      break;
    case 'category':
      apiPath = `http://localhost:9000/reader/book/searchbycategory/${keyword}`;
      break;
    default:
      uni.showToast({ title: '未知搜索模式', icon: 'none' });
      return;
  }
  console.log('apiPath',apiPath);
  
  // 跳转到查询图书详情页并传递参数（搜索模式和关键字）
  uni.navigateTo({
    url: `/pages/Books/details?mode=${searchMode.value}&keyword=${keyword}`
  });
};

// 新增公告详情跳转方法
const handleNoticeDetail = (id) => {
  uni.navigateTo({
    url: `/pages/Notice/Notice?id=${encodeURIComponent(id)}`  // 传递id参数
  });
};

// 日期格式化函数
const formatDate = (dateString) => {
  const date = new Date(dateString);
  const year = date.getFullYear();
  const month = String(date.getMonth() + 1).padStart(2, '0');
  const day = String(date.getDate()).padStart(2, '0');
  return `${year}-${month}-${day}`;
};
</script>

<style lang="scss" scoped>
  /* 新增搜索模式选择样式（不破坏原有布局） */
  .search-mode {
    display: flex;
    align-items: center;
    margin: 0 20rpx;
    font-size: 32rpx;
    color: #666;

    radio {
      margin-right: 20rpx;
    }
  }

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