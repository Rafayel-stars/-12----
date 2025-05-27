<template>
  <view class="container">
    <lb_index_head-vue></lb_index_head-vue>
    <view class="main-content">
      <view class="left"></view>
      <view class="nav">
        <view 
          class="nav-item"
          v-for="item in navItems"
          :key="item"
          :class="{ active: currentNav === item }"
          @click="currentNav = item"
        >
          {{item}}
        </view>
		<button @click="handleBack()">‹ 返回</button>
      </view>
      <view class="content-text">
        <!-- 根据当前导航显示对应文本 -->
        <view v-for="(paragraph, index) in Text[currentNav]" :key="index" class="text-paragraph">
          {{ paragraph }}
        </view>
      </view>
    </view>
  </view>
</template>

<script setup>
import lb_index_headVue from '@/components/lb_index_head.vue';
import { ref } from 'vue';

const navItems = ['图书捐赠通告','名人书架','图书捐赠办法','勷勤文库征集启事'];
const currentNav = ref('图书捐赠通告');
const handleBack = () =>{
	uni.navigateBack()
}
const Text = ref({
  '图书捐赠通告': [
    '华南师范大学图书馆热情欢迎并诚恳征求本校师生、校友、社会团体和各界人士友情捐赠图书及其它类型的文献资料，以泽被后学。捐赠图书登记后，将在图书馆主页的捐赠名录上予以公布。对于捐赠文献，图书馆将秉承"物尽其用"的原则，依据《华南师范大学图书馆图书捐赠办法》确定是否收藏。被收藏的捐赠文献将归华南师范大学图书馆所有，对于不符合《捐赠办法》的文献，图书馆将以适当的方式进行处置。',
    ' 敬请捐赠者在扉页题签或盖章留念，并另附纸留下联系人、联系地址及邮政编码，本馆收到赠品后将根据捐赠者意愿寄发《收藏证书》。对于数量巨大或特别珍贵的书刊捐赠，本馆将根据需要组织专门的捐赠仪式及宣传活动。',
	`\n捐赠方式：\n 1.到馆捐赠：\n石牌校区： 图书馆一楼资源建设部\n电话：  020-85214538-846 张老师\n大学城校区：图书馆一楼办公室\n 电话：020-39310108 张老师\n南海校区：图书馆一楼办公室\n电话：  0757-86687873 闫老师\n2.邮寄捐赠：\n邮寄地址：广东省广州市天河区中山大道西55号华南师范大学图书馆\n联系人：资源建设部图书捐赠 张老师\n 联系电话：020-85214538-846\n邮政编码：510631\n温情提示：为保证赠书顺利接收，请选择中国邮政普通包裹或顺丰快递。\n`
 ],
 
  '名人书架': [
    '图书馆为个人批量捐赠者设立了个人捐赠书架，陈列于石牌校区图书馆四楼的教育文献中心，以宣传他们的可敬精神。欢迎读者利用！',
    '捐赠者：李育中、罗倬汉、黄灼耀、马肖云、罗莎、李逖、罗宏休、石景宜、柯汉琳。',
	'                                                                                                                                                                                                          华南师范大学图书馆'
  ],
  '图书捐赠办法': [
    '一、凡符合以下条件的图书将被纳入馆藏：\n1．内容适应本校教学科研范围；\n2．正式出版物；\n3．适应大学及以上程度；\n4．本馆缺藏，或本馆需增加复本之图书。\n',
    '二、下列图书不予入藏：\n1．非正式出版物（具有学术价值的手稿等除外）。\n2．违反中华人民共和国著作权法之规定的图书。\n3．破损图书。\n4．內有划线、注记、眉批的图书。(若为海內外孤本或为名人批注，则不在此限)\n5．內容陈旧的图书。\n6．基本上不具学术参考价值的图书。\n7．其它不符合本馆馆藏发展政策的图书。\n',
	'三、不入藏处理\n1.捐赠图书我馆一经接收，即拥有其所有权和处理权\n2.不入藏图书将视情况另行处理：\n（1）转赠 ：具参考价值，但图书馆已有收藏，或不适合图书馆收藏的图书，转赠其他单位或转至本馆“漂流角”供读者取阅。\n（2）淘汰：盗版、残旧及无学术价值图书，直接淘汰。\n3.本馆无义务将捐赠文献寄还捐赠者。\n'
  ],
  '勷勤文库征集启事': [
    '“勷勤文库”是本馆长期进行的特藏建设，主要收藏本校师生及校友的作品（包括著、译、编[教材、丛书主编]等各类型正式出版物和非正式出版物；以及日记、笔记、自传、信件、手稿）等，还广泛收藏校内各单位各团体主办的内部发行或公开出版的用于指导工作或交流信息的杂志、报纸等刊物，以及机关公文性或学术性的连续发行的简报类信息资料和学生班级的班刊等等。 文库藏品逐年增长。其来源以教工和校友捐赠为主，本馆购藏为辅。 ',
    '  “勷勤文库”的藏品因其特有意义，本馆将永久特藏， 并将根据捐赠者意愿在图书馆主页上公布捐赠者芳名与大作，以示感谢和留念。文献加工入藏后将提供服务给校内外学术交流及华师教工与学子们学习参考。',
	'为了充实我校“勷勤文库”的内涵，丰富文库的内容，我馆长年开展文库征集活动。其宗旨是为了传承我校优良传统，延续前辈和学长的先进思想、优良品德及严谨的治学态度，促进我校教育事业的蓬勃发展。竭诚希望华师人踊跃向“勷勤文库”提供各类书籍和藏品。欢迎免费捐赠；或提供作品名称、出版书号等信息，图书馆会筹集专门经费多方收集，主动购藏。',
	'捐赠人可在赠书扉页题签或盖章留念，本馆收到赠品后将向捐赠者回发《收藏证书》。对于数量巨大或特别珍贵的书刊捐赠，本馆将根据需要组织专门的捐赠仪式及宣传活动。',
	'捐赠办法：\n1.到馆捐赠：石牌校区：图书馆一楼资源建设部\n 电话：020-85214538-846 张老师\n大学城校区：图书馆一楼办公室\n电话：020-39310109 黄老师 \n 2.邮寄捐赠：\n广州市中山大道西55号华南师范大学图书馆资源建设部（请标注“赠书”字样）\n联系人：张老师\n联系电话：020-85214538-846\n邮政编码：510631\n'
  ]
});
</script>

<style lang="scss" scoped>
.container {
  background-image: url('/static/img-index/head.png');
    background-size: cover;
    background-position: center;
    width: 100vw;
    height: 100vh;
    position: relative;
  

  .main-content {
    position: absolute;
    background: rgba(255, 255, 255, 0.8);
    top: 400rpx;
    left: 500rpx;
    width: 4000rpx;
    height: 2300rpx;
    padding: 40rpx;  // 添加内边距
    
    .nav {
      display: flex;
      margin-bottom: 40rpx;  // 添加底部间距
	  // justify-content: end;
      .nav-item {
        padding: 30rpx;
        font-size: 45rpx;
        border-bottom: 2rpx solid #eee;
        cursor: pointer;
        
        &.active {
          color: #8B4513;
          font-weight: bold;
          border-bottom: 8rpx solid #8B4513;
        }
      }
	  
	  button{
		  width: auto;
		  height: 100%;
		  background-color: #8B4513;
		  color: #fff;
		  font-size: 60rpx;
		  display: flex;
		  align-items: center;
		  justify-content: center;
		  padding: 0 50rpx;
	  }
    }

    .content-text {
      .text-paragraph {
        font-size: 60rpx;
        line-height: 1.6;
        margin-bottom: 30rpx;
        color: #666;
		 text-indent: 2em; /* 标准中文段落缩进 */
		 white-space: pre-wrap; // 保留换行符和空格

      }
    }
  }
}
</style>