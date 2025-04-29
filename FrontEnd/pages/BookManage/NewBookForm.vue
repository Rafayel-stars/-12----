<template>
  <view class="form-wrapper">
    <!-- 表单标题 -->
    <text class="form-title">图书信息登记表</text>
    
    <!-- 图书表单 -->
    <form @submit="handleSubmit">
      <scroll-view class="form-scroll" scroll-y="true">
        <!-- 图书基本信息 -->
        <view class="form-item">
          <text class="label">图书编号：</text>
          <input
            class="input"
            v-model="formData.bookId"
            placeholder="请输入图书唯一编号"
            maxlength="10"
            :required="true"
          />
        </view>

        <view class="form-item">
          <text class="label">图书名称：</text>
          <input
            class="input"
            v-model="formData.bookName"
            placeholder="请输入完整书名"
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
          <picker
            class="picker"
            :range="categoryOptions"
            @change="handleCategoryChange"
            mode="selector"
          >
            <view class="picker-view">{{ formData.category || '请选择图书分类' }}</view>
          </picker>
        </view>

        <view class="form-item">
          <text class="label">ISBN编号：</text>
          <input
            class="input"
            v-model="formData.isbn"
            placeholder="请输入13位ISBN编号"
            type="number"
            maxlength="13"
            :required="true"
          />
        </view>

        <!-- 出版社信息 -->
        <view class="form-item">
          <text class="label">出版社：</text>
          <input
            class="input"
            v-model="formData.publisher"
            placeholder="请输入出版社全称"
          />
        </view>

        <!-- 时间选择 -->
        <view class="form-item">
          <text class="label">出版日期：</text>
          <uni-datetime-picker
            class="date-picker"
            v-model="formData.publishDate"
            type="date"
            :clear-icon="false"
            :required="true"
          />
        </view>

        <view class="form-item">
          <text class="label">库存数量：</text>
          <input
            class="input"
            v-model="formData.stock"
            placeholder="请输入库存数量"
            type="number"
            :required="true"
          />
        </view>

        <view class="form-item">
          <text class="label">定价（元）：</text>
          <input
            class="input"
            v-model="formData.price"
            placeholder="请输入图书定价"
            type="digit"
          />
        </view>

        <!-- 最后更新时间 -->
        <view class="form-item">
          <text class="label">最后更新：</text>
          <uni-datetime-picker
            class="date-picker"
            v-model="formData.updateTime"
            type="datetime"
            :clear-icon="false"
            :required="true"
          />
        </view>

        <!-- 图书简介 -->
        <view class="form-item">
          <text class="label">图书简介：</text>
          <textarea
            class="textarea"
            v-model="formData.description"
            placeholder="请输入图书内容简介"
            :auto-height="true"
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
import { ref } from 'vue';

// 表单数据
const formData = ref({
  bookId: '',
  bookName: '',
  author: '',
  category: '',
  isbn: '',
  publisher: '',
  publishDate: '',
  stock: '',
  price: '',
  updateTime: '',
  description: ''
});

// 分类选项
const categoryOptions = [
  '文学小说', 
  '科学技术', 
  '经济管理', 
  '历史地理', 
  '艺术设计', 
  '教育学习',
  '儿童读物'
];

// 分类选择处理
const handleCategoryChange = (e) => {
  formData.value.category = categoryOptions[e.detail.value];
};

// 表单提交验证
const validateForm = () => {
  if (!formData.value.bookId) {
    uni.showToast({ title: '图书编号不能为空', icon: 'none' });
    return false;
  }
  if (!/^[A-Za-z0-9]{6,10}$/.test(formData.value.bookId)) {
    uni.showToast({ title: '编号需为6-10位字母数字组合', icon: 'none' });
    return false;
  }
  if (!formData.value.isbn || formData.value.isbn.length !== 13) {
    uni.showToast({ title: 'ISBN必须为13位数字', icon: 'none' });
    return false;
  }
  if (formData.value.stock < 0) {
    uni.showToast({ title: '库存数量不能为负数', icon: 'none' });
    return false;
  }
  return true;
};

// 提交处理
const handleSubmit = () => {
  if (!validateForm()) return;
  
  // 模拟提交成功
  uni.showToast({ 
    title: '图书添加成功',
    icon: 'success',
    success: () => {
      setTimeout(() => uni.navigateBack(), 1500);
    }
  });
};

// 重置表单
const handleReset = () => {
  formData.value = {
    bookId: '',
    bookName: '',
    author: '',
    category: '',
    isbn: '',
    publisher: '',
    publishDate: '',
    stock: '',
    price: '',
    updateTime: '',
    description: ''
  };
};

// 取消操作
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