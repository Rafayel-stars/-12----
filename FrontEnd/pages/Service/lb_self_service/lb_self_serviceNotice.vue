<template>
<view class="container">
  <!-- 公告弹窗（始终在最顶层） -->
  <uni-popup 
    ref="popup" 
    type="center" 
    :mask-closable="false"           
  >
    <view class="popup-wrapper">
      <view class="popup-content">
        <text class="title">图书馆自助借还公告</text>
		<text class="content" style="color: black;">尊敬的读者：\n
为提升借阅效率，本馆已全面启用自助借还服务机，操作指南如下：\n

借书流程\n

点击「借书」→ 放置读者证/扫码登录 → 将图书平放识别区 → 核对屏幕信息 → 完成\n

还书流程\n

点击「还书」→ 无需登录 → 单本放入还书口（书脊朝下）→ 确认归还成功\n

注意事项\n
✅ 单次最多借阅5本，借期30天\n
✅ 请勿强行抽取未识别书籍，故障请联系服务台\n
✅ 归还后图书将自动消磁，请勿重复操作\n
⏰ 服务时间：7:00-22:00（系统维护时段暂停）\n</text>
        <text class="content">请仔细阅读并同意我们的服务条款...</text>
        <button 
          :disabled="agreeDisabled"
          @click="handleAgree"
          class="agree-btn">
          {{ agreeText }}
        </button>
		<button @click="handleBack()">返回</button>
      </view>
    </view>
  </uni-popup>
</view>
</template>

<script setup>
import { ref, computed, onMounted, getCurrentInstance } from 'vue';
// import lb_index_headVue from '../../../components/lb_index_head.vue';

const agree = ref(false);
const agreeDisabled = ref(true);
const countdown = ref(5);

const agreeText = computed(() => 
  agreeDisabled.value ? `请仔细阅读（${countdown.value}s）` : '同意并继续'
);

onMounted(() => {
  checkAgreement();
  startCountdown();
});

const checkAgreement = async () => {
  try {
    const { data } = await uni.getStorage({ key: 'agreed' });
    agree.value = !!data;
  } catch (e) {
    console.log('首次访问，无存储记录');
  }
};

const startCountdown = () => {
  const timer = setInterval(() => {
    if (countdown.value <= 1) {
      clearInterval(timer);
      agreeDisabled.value = false;
      return;
    }
    countdown.value--;
  }, 1000);
};

const handleAgree = () => {
  uni.navigateTo({
  	url:'/pages/Service/lb_self_service/lb_self_service'
	
  })
};
const handleBack = () =>{
	uni.navigateBack()
}
</script>

<style lang="scss" scoped>
/* 弹窗容器定位 */
.popup-wrapper {
  position: fixed;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  z-index: 10000;
}

/* 弹窗内容样式 */
.popup-content {
  background: #fff;
  border-radius: 24rpx;
  width: 1660rpx;
  height: 1660rpx;
  padding: 40rpx;
  box-shadow: 0 10rpx 30rpx rgba(0,0,0,0.1);
  text-align: center;
}

.title {
  font-size: 72rpx;
  font-weight: 600;
  color: #333;
  margin-bottom: 24rpx;
  display: block;
}

.content {
  font-size: 50rpx;
  color: #666;
  line-height: 1.6;
  margin-bottom: 48rpx;
  display: block;
}

.agree-btn {
  background: #007AFF;
  color: #fff;
  font-size: 40rpx;
  height: 88rpx;
  line-height: 88rpx;
  border-radius: 12rpx;
  transition: all 0.3s;

  &[disabled] {
    background: #e5e5e5 !important;
    color: #999 !important;
  }
}


</style>
