<template>
  <view class="form-wrapper">
    <!-- 表单标题 -->
    <text class="form-title">借阅登记记录表</text>
    
    <!-- 借阅表单 -->
    <form @submit="handleSubmit">
      <scroll-view class="form-scroll" scroll-y="true">
        <!-- 借阅基本信息 -->
        <view class="form-item">
          <text class="label">借阅单号：</text>
          <input
            class="input"
            v-model="formData.loanNo"
            placeholder="请输入借阅单号（例：LY2023001）"
            maxlength="15"
            :required="true"
          />
        </view>

        <view class="form-item">
          <text class="label">读者ID：</text>
          <input
            class="input"
            v-model="formData.readerId"
            placeholder="请输入读者证号"
            maxlength="10"
            :required="true"
          />
        </view>

        <view class="form-item">
          <text class="label">图书编号：</text>
          <input
            class="input"
            v-model="formData.bookId"
            placeholder="请输入图书编号"
            maxlength="10"
            :required="true"
          />
        </view>

        <!-- 时间选择 -->
        <view class="form-item">
          <text class="label">借阅日期：</text>
          <uni-datetime-picker
            class="date-picker"
            v-model="formData.loanDate"
            type="datetime"
            :clear-icon="false"
            :required="true"
          />
        </view>

        <view class="form-item">
          <text class="label">应还日期：</text>
          <uni-datetime-picker
            class="date-picker"
            v-model="formData.dueDate"
            type="date"
            :clear-icon="false"
            :required="true"
          />
        </view>

        <!-- 状态选择 -->
        <view class="form-item">
          <text class="label">借阅状态：</text>
          <picker
            class="picker"
            :range="statusOptions"
            @change="handleStatusChange"
            mode="selector"
          >
            <view class="picker-view">{{ formData.status || '请选择借阅状态' }}</view>
          </picker>
        </view>

        <view class="form-item" v-if="formData.status === '已归还'">
          <text class="label">归还日期：</text>
          <uni-datetime-picker
            class="date-picker"
            v-model="formData.returnDate"
            type="datetime"
            :clear-icon="false"
          />
        </view>

        <view class="form-item">
          <text class="label">操作员：</text>
          <input
            class="input"
            v-model="formData.operator"
            placeholder="请输入操作员姓名"
          />
        </view>

        <!-- 备注信息 -->
        <view class="form-item">
          <text class="label">备注说明：</text>
          <textarea
            class="textarea"
            v-model="formData.remark"
            placeholder="请输入特殊情况说明"
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
  loanNo: '',
  readerId: '',
  bookId: '',
  loanDate: '',
  dueDate: '',
  status: '',
  returnDate: '',
  operator: '',
  remark: ''
});

// 状态选项
const statusOptions = [
  '待借出', 
  '已借出', 
  '已归还',
  '逾期未还',
  '续借中'
];

// 状态选择处理
const handleStatusChange = (e) => {
  formData.value.status = statusOptions[e.detail.value];
};

// 表单提交验证
const validateForm = () => {
  if (!formData.value.loanNo) {
    uni.showToast({ title: '借阅单号不能为空', icon: 'none' });
    return false;
  }
  if (!/^LY\d{4}[A-Z0-9]{3}$/.test(formData.value.loanNo)) {
    uni.showToast({ title: '单号格式：LY+4位年份+3位数字/字母', icon: 'none' });
    return false;
  }
  if (formData.value.dueDate < formData.value.loanDate) {
    uni.showToast({ title: '应还日期不能早于借阅日期', icon: 'none' });
    return false;
  }
  if (formData.value.status === '已归还' && !formData.value.returnDate) {
    uni.showToast({ title: '归还状态必须填写归还日期', icon: 'none' });
    return false;
  }
  return true;
};

// 提交处理
const handleSubmit = () => {
  if (!validateForm()) return;
  
  // 模拟提交成功
  uni.showToast({ 
    title: '借阅记录添加成功',
    icon: 'success',
    success: () => {
      setTimeout(() => uni.navigateBack(), 1500);
    }
  });
};

// 重置表单
const handleReset = () => {
  formData.value = {
    loanNo: '',
    readerId: '',
    bookId: '',
    loanDate: '',
    dueDate: '',
    status: '',
    returnDate: '',
    operator: '',
    remark: ''
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