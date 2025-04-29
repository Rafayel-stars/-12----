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
				 id="username"
				  
				  placeholder="请输入用户名">
			</view>
			<!-- required后面再补充 -->
			<view class="input-box">
				<text class="iconfont icon-jiesuo"></text>
				<input
				type="password"
				id="password"
				
				placeholder="请输入密码"
				>
				<!-- required后面再补充 -->
			</view>
			<view class="remember-forget">
				<view class="remember-me">
					<checkbox />记住密码
				</view>
				<navigator class="forget-pwd" url="/">忘记密码</navigator>
			</view>
			<!-- 修改按钮部分 -->
			<button form-type="submit" @click="handleLogin">登录按钮</button>
			
			<view class="register-link">
				<text>还没有账号？</text>
				<navigator url="/">点击注册</navigator>
			</view>
		</view>
		</view>
	</view>
</template>

<script setup>
// 新增登录处理方法
const handleLogin = () => {
  // 这里可以添加登录验证逻辑
  uni.navigateTo({
    url: '/pages/lb_index/lb_index' ,// 确保路径与实际页面路径一致
	// url: '/pages/UserManage/UserManage',
	
	
	
  });
};
</script>

<style lang="scss" scoped>
.body{
	background: linear-gradient(to right,
	      rgb(152, 251, 152),
	      rgb(120, 200, 250),
	      rgba(44, 114, 251, 1));
	  display: flex;
	  justify-content: center;
	  align-items: center;
	  min-height: 100vh;
	  margin: 0;
	  overflow: hidden;
	  .Application {
	    position: relative;
	    width: 30%;
	    height: 800rpx;
	    padding-top: 0;
	    background-color: #fff;
	    border-radius: 20rpx;
	    display: flex;
	    justify-content: center;
	    align-items: center;
		
		h2 {
		  font-size: 60rpx;
		  color: #000;
		  text-align: center;
		}
		
		.input-box {
		  display: flex;
		  align-items: center;
		  width: 320rpx;
		  margin: 80rpx 0;
		  border-bottom: 2rpx solid #000;
		  .iconfont {
		    margin-right: 20rpx;
		  }
		  input {
		    flex: 1;
		  }
		}

		.remember-forget {
		  display: flex;
		  justify-content: space-between;
		  align-items: center;
		  width: 320rpx;
		  margin: 20rpx 0;
		  font-size: 28rpx;
		  
		  .remember-me {
		    display: flex;
		    align-items: center;
		    gap: 10rpx;
		    
		    checkbox {
		      transform: scale(0.7);
		      transform-origin: left center;
		      margin-right: -8rpx;
		    }
		  }
		  
		  .forget-pwd {
		    font-size: 28rpx;
		    color: #666;
		    line-height: 1.4;
		  }
		}

		button {
		  width: 320rpx;
		  margin-top: 30rpx;
		  height: 80rpx;
		  line-height: 80rpx;
		  font-size: 32rpx;
		  background: linear-gradient(to right,
		        rgb(152, 251, 152),
		        rgb(120, 200, 250),
		        rgba(44, 114, 251, 1));
		}

		.register-link {
		  display: flex;
		  justify-content: center;
		  align-items: center;
		  gap: 10rpx;
		  margin-top: 20rpx;
		  font-size: 28rpx;
		  
		  navigator {
		    color: #2b85e4;
		  }
		}
	  }
}
</style>