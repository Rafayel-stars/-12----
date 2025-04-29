<template>
	<view class="form-wrapper">
		<!-- 表单标题 -->
		<text class="form-title">图书借阅登记表</text>
		<!-- 借阅表单 -->
		<form @submit="handleSubmit">
			<scroll-view class="form-scroll" scroll-y="true">
				<!-- 书籍信息 -->
				<view class="form-item">
					<text class="label">书籍名称：</text>
					<input
						class="input"
						v-model="formData.bookName"
						placeholder="请输入书籍全称"
						:required="true"
					/>
				</view>

				<view class="form-item">
					<text class="label">ISBN编号：</text>
					<input
						class="input"
						v-model="formData.isbn"
						placeholder="请输入13位ISBN编号"
						maxlength="13"
						:required="true"
					/>
				</view>

				<!-- 借阅人信息 -->
				<view class="form-item">
					<text class="label">借阅人姓名：</text>
					<input
						class="input"
						v-model="formData.borrower"
						placeholder="请输入真实姓名"
						:required="true"
					/>
				</view>

				<view class="form-item">
					<text class="label">借阅日期：</text>
					<uni-datetime-picker
						class="date-picker"
						v-model="formData.borrowDate"
						type="date"
						:clear-icon="false"
						:required="true"
					/>
				</view>

				<view class="form-item">
					<text class="label">预计归还日期：</text>
					<uni-datetime-picker
						class="date-picker"
						v-model="formData.dueDate"
						type="date"
						:clear-icon="false"
						:required="true"
					/>
				</view>

				<!-- 联系方式 -->
				<view class="form-item">
					<text class="label">联系电话：</text>
					<input
						class="input"
						v-model="formData.phone"
						placeholder="请输入手机号码"
						type="number"
						maxlength="11"
						:required="true"
					/>
				</view>

				<!-- 借阅状态 -->
				<view class="form-item">
					<text class="label">借阅状态：</text>
					<picker
						class="picker"
						:range="statusOptions"
						@change="handleStatusChange"
						mode="selector"
					>
						<view class="picker-view">{{formData.status || '请选择状态'}}</view>
					</picker>
				</view>

				<!-- 备注信息 -->
				<view class="form-item">
					<text class="label">备注说明：</text>
					<textarea
						class="textarea"
						v-model="formData.remarks"
						placeholder="可填写特殊需求或者备注"
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
				>提交申请</button>
				<button
					class="cancel-btn"
					type="default"
					@click="handleCancel"
				>取消</button>
				<button
				  class="back-btn"
				  type="default"
				  @click="handleBack"
				  >
				  
				  返回
					
				</button>
			</view>
		</form>
	</view>
</template>

<script setup>
import { ref } from 'vue';
// 表单数据
const formData = ref({
	bookName: '',
	isbn: '',
	borrower: '',
	borrowDate: '',
	dueDate: '',
	phone: '',
	status: '',
	remarks: ''
});

// 状态选项
const statusOptions = ['在借中', '已归还', '已续借', '逾期未还'];

// 状态选择处理
const handleStatusChange = (e) => {
	formData.value.status = statusOptions[e.detail.value];
};

// 表单提交
const handleSubmit = () => {
	// 验证逻辑保持不变...
};

// 取消/重置表单
const handleCancel = () => {
	uni.navigateBack();
};

//返回页面逻辑
const handleBack = () =>{
	uni.navigateBack();
}
</script>

<style lang="scss" scoped>
.form-wrapper {
	padding: 30rpx;
	background-color: #fff;
	border-radius: 12rpx;
	margin: 30rpx auto; // 改为自动居中
	width: 1600rpx; // 增加宽度限制
	height: 2300rpx;
	// height: 1500rpx;
	max-width: 1800rpx; // 最大宽度限制
	box-shadow: 0 4rpx 12rpx rgba(0, 0, 0, 0.1);

	.form-scroll {
		// max-height: 70vh; // 限制滚动区域高度
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
			font-size: 50rpx; // 调大字号
			color: #333; // 加深颜色
			margin-bottom: 15rpx;
			font-weight: bold;
		}

		.input, .textarea {
			padding: 25rpx; // 增加内边距
			border: 2rpx solid #ddd;
			border-radius: 12rpx; // 加大圆角
			font-size: 40rpx;
			background-color: #fff; // 改为纯白背景
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
			font-size: 40rpx; // 加大按钮文字
			border-radius: 12rpx;
			
			&.submit-btn {
				background-color: #007bff !important;
			}
			
			&.cancel-btn {
				background-color: #dc3545 !important;
				color: white !important;
			}
		}
	}
}
</style>