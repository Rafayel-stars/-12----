<!-- lb_login.vue -->
<template>
  <view class="body">
    <view class="Application">
      <view id="loginForm">
        <h2>登录界面</h2>
        <view class="input-box">
          <text class="iconfont icon-wode"></text>
          <input
            type="text"
            v-model="formData.no"
            placeholder="请输入用户名"
          >
        </view>

        <view class="input-box">
          <text class="iconfont icon-jiesuo"></text>
          <input
            type="password"
            v-model="formData.password"
            placeholder="请输入密码"
            @keyup.enter="handleLogin"
          >
        </view>

        <view class="remember-forget">
          <view class="remember-me">
            <checkbox v-model="rememberMe" /> 记住密码
          </view>
          <navigator class="forget-pwd" url="/">忘记密码</navigator>
        </view>

        <button 
          :disabled="loading"
          :loading="loading"
          @click="handleLogin"
        >
          {{ loading ? '登录中...' : '立即登录' }}
        </button>

        <view class="register-link">
          <text>还没有账号？</text>
          <navigator url="/">点击注册</navigator>
        </view>
      </view>
    </view>
  </view>
</template>

<script setup>
import { ref, reactive } from 'vue';
import { useRouter } from 'vue-router';

// 响应式数据
const formData = reactive({
  no: '',
  password: ''
});

const rememberMe = ref(false);
const loading = ref(false);
const router = useRouter();

// 登录方法
const handleLogin = async () => {
  if (!validateForm()) return;
  
  loading.value = true;
  
  try {
    const params = new URLSearchParams();
    params.append('no', formData.no);
    params.append('password', formData.password);

    const response = await uni.request({
      url: 'http://localhost:9000/auth/login',
      method: 'POST',
      header: {
        'Content-Type': 'application/x-www-form-urlencoded'
      },
      data: params.toString()
    });

    handleResponse(response);
  } catch (error) {
    showToast('网络请求失败，请稍后重试');
  } finally {
    loading.value = false;
  }
};

// 表单验证
const validateForm = () => {
  if (!formData.no.trim()) {
    showToast('请输入用户名');
    return false;
  }
  if (!formData.password) {
    showToast('请输入密码');
    return false;
  }
  return true;
};

// 处理响应
const handleResponse = (response) => {
  const res = response.data;
  if(response.statusCode === 200){
	  uni.setStorageSync('token', res.token);
	  uni.setStorageSync('user',res.user)
	  const user1 = uni.getStorageSync('user');
	  const token1 = uni.getStorageSync('token')
	  console.log('本地存储user',user1)
	  console.log('本地存储token',token1)
  }
  if (response.statusCode === 200 && res.user.userType === 'admin') {
    showToast('登录成功', 'success');
   router.push('/pages/UserManage/UserManage');
  } 
  else if (response.statusCode === 200 && res.user.userType === 'user') {
	  showToast('登录成功', 'success');
	  
	   router.push('/pages/lb_index/lb_index');
  }
  else {
    showToast(res.message || '用户名或密码错误');
  }
};

// 通用提示方法
const showToast = (title, icon = 'none') => {
  uni.showToast({
    title,
    icon,
    duration: 2000
  });
};
</script>

<style lang="scss" scoped>
.body {
  background: linear-gradient(
    to right,
    rgb(152, 251, 152),
    rgb(120, 200, 250),
    rgba(44, 114, 251, 1)
  );
  min-height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;

  .Application {
    background: white;
    width: 90%;
    max-width: 400px;
    padding: 2rem;
    border-radius: 12px;
    box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);

    h2 {
      text-align: center;
      margin-bottom: 2rem;
      color: #2c72fb;
    }

    .input-box {
      display: flex;
      align-items: center;
      margin-bottom: 1.5rem;
      border-bottom: 2px solid #eee;
      padding: 0.5rem 0;

      .iconfont {
        margin-right: 0.8rem;
        color: #666;
      }

      input {
        flex: 1;
        padding: 0.5rem 0;
        font-size: 1rem;
        border: none;
        outline: none;

        &::placeholder {
          color: #999;
        }
      }
    }

    .remember-forget {
      display: flex;
      justify-content: space-between;
      margin: 1rem 0;
      font-size: 0.9rem;

      .remember-me {
        display: flex;
        align-items: center;
        gap: 0.5rem;
      }

      .forget-pwd {
        color: #2c72fb;
        text-decoration: none;
      }
    }

    button {
      width: 100%;
      padding: 1rem;
      background: linear-gradient(
        to right,
        rgb(152, 251, 152),
        rgb(120, 200, 250)
      );
      color: white;
      border: none;
      border-radius: 6px;
      font-size: 1rem;
      transition: opacity 0.3s;

      &[disabled] {
        opacity: 0.7;
        background: #ccc;
      }
    }

    .register-link {
      text-align: center;
      margin-top: 1.5rem;
      color: #666;

      navigator {
        color: #2c72fb;
        margin-left: 0.5rem;
      }
    }
  }
}
</style>