<template>
  <view class="form-wrapper">
    <!-- 表单标题 -->
    <text class="form-title">图书信息登记表</text>
    
    <!-- 图书表单 -->
    <form @submit="handleSubmit">
      <scroll-view class="form-scroll" scroll-y="true">
        <!-- 图书基本信息 -->
		<view class="form-item">
		  <text class="label">图书自编编号：</text>
		  <input
		    class="input"
		    v-model="formData.libraryCode"
		    placeholder="请输入完整的图书自编编号"
		    :required="true"
		  />
		</view>
		
        <view class="form-item">
          <text class="label">ISBN编号：</text>
          <input
            class="input"
            v-model="formData.isbn"
            placeholder="请输入完整的ISBN编号"
            :required="true"
          />
        </view>

		<view class="form-item">
		  <text class="label">图书名称：</text>
		  <input
			class="input"
			v-model="formData.title"
			placeholder="请输入完整的图书名称"
			:required="true"
		  />
		</view>
	

        <view class="form-item">
          <text class="label">作者：</text>
          <input
            class="input"
            v-model="formData.author"
            placeholder="请输入作者全名"
            :required="true"
          />
        </view>

        <!-- 分类选择 -->
        <view class="form-item">
          <text class="label">所属分类：</text>
          <input 
		  class="input"
		  v-model="formData.category"
		  placeholder="请输入图书分类"
		  :required="true"
		  />
        </view>
		
		<!-- 借阅状态选择 -->
		<view class="form-item">
		  <text class="label">借阅状态：</text>
		 <picker
		 class="picker"
		 :range="statusOp"
		 @change="handleStatusChange"
		 mode="selector"
		 >
		 <view class="picker-view">{{statusDisplay}}</view>
		 </picker>
		</view>
		
		<!-- 入库时间 -->
		<view class="form-item">
		  <text class="label">入库时间：</text>
		  <uni-datetime-picker
		    class="date-picker"
		    v-model="formData.entryTime"
		    type="datetime"
		    :clear-icon="false"
		    :required="true"
		  />
		</view>

    
      </scroll-view>

      <!-- 操作按钮 -->
      <view class="button-group">
        <button
          class="submit-btn"
          form-type="submit"
          type="primary"
        >提交登记</button>
        <button
          class="reset-btn"
          type="default"
          @click="handleReset"
        >重置</button>
        <button
          class="cancel-btn"
          type="default"
          @click="handleCancel"
        >取消</button>
      </view>
    </form>
  </view>
</template>

<script setup>
import { ref ,computed,onMounted} from 'vue';

// 表单数据
const formData = ref({
 libraryCode:'',
 isbn:'',
 title:'',
 author:'',
 category:'',
 status: 1 ,// 默认可借状态
 entryTime:'',
});

const statusOp = ['已借','可借']
const loading = ref(false); // 新增加载状态
const statusDisplay = computed(() =>
 statusOp[formData.value.status] || '请选择借阅状态'
)

const handleStatusChange = (e) =>{
 formData.value.status = parseInt(e.detail.value)
}

// 表单提交验证（保持不变）
const validateForm = () => {
 const isbnDigits = formData.value.isbn.replace(/\D/g, '');
   
 if (!isbnDigits || isbnDigits.length !== 13) {
   uni.showToast({ title: 'ISBN必须为13位数字', icon: 'none' });
   return false;
 }
 
 if(!formData.value.title){
   uni.showToast({ title:'图书名称不能为空', icon:'none' });
   return false;
 }
 
 if(!formData.value.author){
   uni.showToast({ title:'图书作者不能为空', icon:'none' });
   return false;
 }
 
 if(!formData.value.category){
   uni.showToast({ title:'图书分类不能为空', icon:'none' });
   return false;
 }
 
 return true;
};

// 生命周期钩子（保持不变）
onMounted(() => {
  const user = uni.getStorageSync('user');
  if (user.userType !== 'admin') {
    uni.showToast({ title: '无权限访问', icon: 'none' });
    setTimeout(() => uni.navigateBack(), 1500);
  }
});

// 提交处理（关键修改部分）
const handleSubmit = () => {
  if (!validateForm()) return;
  
  // 获取token
  const token = uni.getStorageSync('token');
  if (!token) {
    uni.showToast({ title: '请先登录', icon: 'none' });
    return uni.reLaunch({ url: '/pages/lb_login/lb_login' });
  }

  loading.value = true;

  // 准备提交数据（根据接口需求调整字段）
  const submitData = {
	libraryCode:formData.value.libraryCode,
    isbn: formData.value.isbn, // 清理ISBN格式
    title: formData.value.title,    // 根据接口字段命名调整
    author: formData.value.author,
    category: formData.value.category,
    status: formData.value.status === 0 ? 0 : 1 ,// 根据接口需求转换状态值
	entryTime:formatDate(formData.value.entryTime)
  };

  // 发送请求
  uni.request({
    url: 'http://localhost:9000/admin/book/add',
    method: 'POST',
    header: {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    },
    data: submitData,
    success: (response) => {
      if (response.statusCode === 200) {
        uni.showToast({
          title: '图书添加成功',
          success: () => {
            setTimeout(() => uni.navigateBack(), 1500);
          }
        });
      } else if (response.statusCode === 401) {
        handleUnauthorized();
      } else {
        uni.showToast({ 
          title: response.data?.message || '添加图书失败',
          icon: 'none'
        });
      }
    },
    fail: (error) => {
      uni.showToast({ 
        title: '网络请求失败，请检查连接',
        icon: 'none'
      });
    },
    complete: () => {
      loading.value = false;
    }
  });
};

// 新增未授权处理（与用户表单一致）
const handleUnauthorized = () => {
  uni.showToast({ title: '登录已过期，请重新登录', icon: 'none' });
  uni.removeStorageSync('token');
  setTimeout(() => {
    uni.reLaunch({ url: '/pages/login' });
  }, 1500);
};

// 重置表单（保持不变）
const handleReset = () => {
  formData.value = {
	libraryCode:'',
    isbn:'',
    title:'',
    author:'',
    category:'',
    status:1,
	entryTime:''
  };
};

// 时间格式转换函数（与参考代码保持一致）
const formatDate = (dateStr) => {
  const date = new Date(dateStr);
  const year = date.getFullYear();
  const month = String(date.getMonth() + 1).padStart(2, '0');
  const day = String(date.getDate()).padStart(2, '0');
  const hours = String(date.getHours()).padStart(2, '0');
  const minutes = String(date.getMinutes()).padStart(2, '0');
  const seconds = String(date.getSeconds()).padStart(2, '0');
  return `${year}-${month}-${day}T${hours}:${minutes}:${seconds}.000+00:00`;
};

// 取消操作（保持不变）
const handleCancel = () => {
  uni.navigateBack();
};
</script>

<style lang="scss" scoped>
.form-wrapper {
  padding: 30rpx;
  background-color: #fff;
  border-radius: 12rpx;
  margin: 30rpx auto;
  width: 1600rpx;
  height: 2300rpx;
  max-width: 1800rpx;
  box-shadow: 0 4rpx 12rpx rgba(0, 0, 0, 0.1);

  .form-scroll {
    max-height: 50vh;
    max-width: 80vw;
  }

  .form-title {
    display: block;
    text-align: center;
    font-size: 72rpx;
    color: #333;
    font-weight: bold;
    margin-bottom: 40rpx;
  }

  .form-item {
    margin-bottom: 30rpx;

    .label {
      font-size: 50rpx;
      color: #333;
      margin-bottom: 15rpx;
      font-weight: bold;
    }

    .input, .textarea {
      padding: 25rpx;
      border: 2rpx solid #ddd;
      border-radius: 12rpx;
      font-size: 40rpx;
      background-color: #fff;
    }

    .textarea {
      min-height: 200rpx;
      width: 1500rpx;
    }

    .picker-view {
      padding: 25rpx;
      background-color: #fff;
      border: 2rpx solid #ddd;
      border-radius: 12rpx;
      color: #666;
      font-size: 40rpx;
    }

    .date-picker {
      ::v-deep .uni-date__x-input {
        padding: 25rpx !important;
      }
    }
  }

  .button-group {
    margin-top: 40rpx;
    display: flex;
    gap: 20rpx;

    button {
      flex: 1;
      padding: 25rpx 0;
      font-size: 40rpx;
      border-radius: 12rpx;
      
      &.submit-btn {
        background-color: #28a745 !important;
      }
      
      &.reset-btn {
        background-color: #ffc107 !important;
        color: #333 !important;
      }
      
      &.cancel-btn {
        background-color: #dc3545 !important;
        color: white !important;
      }
    }
  }
}
</style>