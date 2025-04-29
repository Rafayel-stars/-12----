<template>
  <view class="container">
    <lb_index_head-vue></lb_index_head-vue>
    <view class="content">
      <view class="left">
        <text>您当前的界面：首页》服务》信息素养教育 </text>
        
        <view class="nav">
          <view class="nav-item">信息素养教育</view>
          <view 
            class="back-btn" 
            @click="handleBack"
          >
            <text style="color: #fff;">‹ 返回</text>
          </view>
        </view>
        <!-- 关键修改：给scroll-view设置固定高度，并确保内容高度超过容器高度 -->
        <scroll-view class="form-scroll" scroll-y="true">
          <view class="education-container">
            <text>
              1、 课程名称\n
              
              "信息检索与利用“，目前图书馆该课程在石牌和大学城两个校区开设，面向本科学生。\n
              
              
              
              2、课程性质、对象和选课方式\n
              
              “信息检索与利用”课程正式列入学校教学计划，为全校公共选修课，面向本科生开设，每学期34学时，2个学分，学生每学期末通过学校教务管理系统选课。石牌校区限报40人，大学城校区限报80人。\n
              
              3、 课程内容\n
              
              主要包括文献信息检索基础知识、基本原理、方法与技能，不同类型数据库的检索以及因特网信息资源检索、学术规范及论文写作。\n
              
              4、 课程目标\n
              
              本课程是根据教育部若干文件精神开设的一门信息教育基础课程，其目的在于增强学生的信息意识，掌握查找和利用各种文献信息的技能，提高学生利用信息解决实际问题的能力。课程目标包括：了解文献的基本知识；掌握信息检索的基本原理和方法以及基本的信息检索技术；熟练掌握本领域常用的检索工具以及网络信息资源的检索方法；学会分析、整理和正确使用信息的方法；了解如何合理合法地利用信息。\n
              
              5、 授课方式\n
              
              本课程实用性强，注重学生的动手操作能力，上课地点为学校机房，采用课堂讲授和上机实习相结合的方式，教学过程中会利用我校砺儒云平台发布课程文档、作业、调查和测验。\n
              
              6、 授课教师:\n
              课程:\n
              
              信息检索与利用\n
              
              授课教师:\n
              
              马晶晶\n
              
              电话:\n
              
              85214537-856\n
            </text>
          </view>
        </scroll-view>
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
import { ref } from 'vue';
import lb_index_headVue from '@/components/lb_index_head.vue';
import lb_side_navVue from '../../../components/lb_side_nav.vue';

const currentPagePath = ref('/pages/Service/lb_literacy_education/lb_literacy_education');
const showContent = ref(true);

const handleBack = () => {
  uni.reLaunch({
    url: '/pages/lb_index/lb_index'
  });
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

        .nav-item, .back-btn {
          padding: 0 50rpx;
          height: 100%;
          background-color: #8B4513;
          color: #fff;
          font-size: 60rpx;
          display: flex;
          align-items: center;
        }
      }

      /* 关键修改：给scroll-view添加固定高度 */
      .form-scroll {
        height: 1500rpx; /* 根据实际需求设置合适高度，确保内容超过时触发滚动 */
        overflow-y: auto; /* 显示滚动条 */
      }

      .education-container {
        /* 确保内容高度超过scroll-view高度 */
        min-height: 2000rpx; /* 示例高度，可根据实际内容调整 */
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