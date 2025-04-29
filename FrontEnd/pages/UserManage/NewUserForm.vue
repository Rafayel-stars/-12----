<template>
  <view class="form-wrapper">
    <!-- 表单标题 -->
    <text class="form-title">图书馆用户登记表</text>
    
    <!-- 用户表单 -->
    <form @submit="handleSubmit">
      <scroll-view class="form-scroll" scroll-y="true">
        <!-- 用户基本信息 -->
        <view class="form-item">
          <text class="label">用户编号：</text>
          <input
            class="input"
            v-model="formData.userId"
            placeholder="请输入唯一用户编号"
            maxlength="10"
            :required="true"
          />
        </view>

        <view class="form-item">
          <text class="label">用户姓名：</text>
          <input
            class="input"
            v-model="formData.userName"
            placeholder="请输入真实姓名"
            :required="true"
          />
        </view>

        <!-- 用户角色 -->
        <view class="form-item">
          <text class="label">用户角色：</text>
          <picker
            class="picker"
            :range="roleOptions"
            @change="handleRoleChange"
            mode="selector"
          >
            <view class="picker-view">{{ formData.role || '请选择用户角色' }}</view>
          </picker>
        </view>

        <!-- 部门信息 -->
        <view class="form-item">
          <text class="label">所属部门：</text>
          <input
            class="input"
            v-model="formData.department"
            placeholder="请输入完整部门名称"
          />
        </view>

        <!-- 联系信息 -->
        <view class="form-item">
          <text class="label">联系电话：</text>
          <input
            class="input"
            v-model="formData.phone"
            placeholder="请输入11位手机号码"
            type="number"
            maxlength="11"
            :required="true"
          />
        </view>

        <view class="form-item">
          <text class="label">电子邮箱：</text>
          <input
            class="input"
            v-model="formData.email"
            placeholder="请输入有效邮箱"
            type="email"
          />
        </view>

        <!-- 时间选择 -->
        <view class="form-item">
          <text class="label">注册时间：</text>
          <uni-datetime-picker
            class="date-picker"
            v-model="formData.registerTime"
            type="datetime"
            :clear-icon="false"
            :required="true"
          />
        </view>

        <!-- 状态选择 -->
        <view class="form-item">
          <text class="label">账户状态：</text>
          <picker
            class="picker"
            :range="statusOptions"
            @change="handleStatusChange"
            mode="selector"
          >
            <view class="picker-view">{{ formData.status || '请选择账户状态' }}</view>
          </picker>
        </view>

        <!-- 备注信息 -->
        <view class="form-item">
          <text class="label">备注说明：</text>
          <textarea
            class="textarea"
            v-model="formData.remarks"
            placeholder="可填写特殊说明或备注"
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
  userId: '',
  userName: '',
  role: '',
  department: '',
  phone: '',
  email: '',
  registerTime: '',
  status: '',
  remarks: ''
});

// 选项数据
const roleOptions = ['普通用户', '图书管理员', '系统管理员', '访客'];
const statusOptions = ['正常', '冻结', '注销', '待激活'];

// 选择器事件处理
const handleRoleChange = (e) => {
  formData.value.role = roleOptions[e.detail.value];
};

const handleStatusChange = (e) => {
  formData.value.status = statusOptions[e.detail.value];
};

// 表单提交
const handleSubmit = () => {
  // 验证逻辑
  if (!formData.value.userId) {
    uni.showToast({ title: '用户编号不能为空', icon: 'none' });
    return;
  }
  if (!/^\d{6,10}$/.test(formData.value.userId)) {
    uni.showToast({ title: '用户编号需为6-10位数字', icon: 'none' });
    return;
  }
  if (!formData.value.userName) {
    uni.showToast({ title: '用户姓名不能为空', icon: 'none' });
    return;
  }
  // 其他验证逻辑...
  
  // 提交成功后跳转
  uni.showToast({ 
    title: '用户添加成功',
    success: () => {
      setTimeout(() => uni.navigateBack(), 1500);
    }
  });
};

// 重置表单
const handleReset = () => {
  formData.value = {
    userId: '',
    userName: '',
    role: '',
    department: '',
    phone: '',
    email: '',
    registerTime: '',
    status: '',
    remarks: ''
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