<template>
	<view class="form-wrapper">
		<!-- 表单标题 -->
		<text class="form-title">图书捐赠登记表</text>
		<!-- 捐赠表单 -->
		<form @submit="handleSubmit">
			<scroll-view class="form-scroll" scroll-y="true">
				<!-- 捐赠人信息 -->
				<view class="form-item">
					<text class="label">捐赠人姓名：</text>
					<input
						class="input"
						v-model="formData.donorName"
						placeholder="请输入真实姓名"
						:required="true"
					/>
				</view>

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

				<!-- 捐赠信息 -->
				<view class="form-item">
					<text class="label">捐赠日期：</text>
					<uni-datetime-picker
						class="date-picker"
						v-model="formData.donateDate"
						type="date"
						:clear-icon="false"
						:required="true"
					/>
				</view>

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
					/>
				</view>

				<view class="form-item">
					<text class="label">作者：</text>
					<input
						class="input"
						v-model="formData.author"
						placeholder="请输入作者全名"
					/>
				</view>

				<view class="form-item">
					<text class="label">出版社：</text>
					<input
						class="input"
						v-model="formData.publisher"
						placeholder="请输入出版社名称"
					/>
				</view>

				<view class="form-item">
					<text class="label">捐赠数量：</text>
					<input
						class="input"
						v-model="formData.quantity"
						placeholder="请输入捐赠数量"
						type="number"
						:required="true"
					/>
				</view>

				<!-- 图书分类 -->
				<view class="form-item">
					<text class="label">书籍分类：</text>
					<picker
						class="picker"
						:range="categoryOptions"
						@change="handleCategoryChange"
						mode="selector"
					>
						<view class="picker-view">{{formData.category || '请选择分类'}}</view>
					</picker>
				</view>

				<!-- 匿名捐赠 -->
				<view class="form-item">
					<text class="label">匿名捐赠：</text>
					<switch
						class="switch"
						color="#4cd964"
						@change="handleAnonymousChange"
					/>
				</view>

				<!-- 备注信息 -->
				<view class="form-item">
					<text class="label">捐赠备注：</text>
					<textarea
						class="textarea"
						v-model="formData.remarks"
						placeholder="可填写捐赠寄语或特殊说明"
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
				>提交捐赠</button>
				<button
					class="cancel-btn"
					type="default"
					@click="handleCancel"
				>重置</button>
			</view>
		</form>
	</view>
</template>

<script setup>
import { ref } from 'vue';

const formData = ref({
	donorName: '',
	phone: '',
	donateDate: '',
	bookName: '',
	isbn: '',
	author: '',
	publisher: '',
	quantity: 1,
	category: '',
	anonymous: false,
	remarks: ''
});

const categoryOptions = ['文学小说', '科学技术', '教育教材', '儿童读物', '人文社科', '其他'];

const handleCategoryChange = (e) => {
	formData.value.category = categoryOptions[e.detail.value];
};

const handleAnonymousChange = (e) => {
	formData.value.anonymous = e.detail.value;
};

const handleSubmit = () => {
	// 表单验证逻辑
	if (!formData.value.donorName) {
		uni.showToast({ title: '请填写捐赠人姓名', icon: 'none' });
		return;
	}
	if (!/^1[3-9]\d{9}$/.test(formData.value.phone)) {
		uni.showToast({ title: '请输入有效的手机号', icon: 'none' });
		return;
	}
	// 其他验证逻辑...
	console.log('提交捐赠数据：', formData.value);
	uni.showToast({ title: '捐赠信息已提交' });
};

const handleCancel = () => {
	formData.value = {
		donorName: '',
		phone: '',
		donateDate: '',
		bookName: '',
		isbn: '',
		author: '',
		publisher: '',
		quantity: 1,
		category: '',
		anonymous: false,
		remarks: ''
	};
};
</script>

<style lang="scss" scoped>
.form-wrapper {
	/* 保持原有样式不变 */
	.switch {
		transform: scale(0.8);
		margin-left: 20rpx;
	}
	.button-group {
		.submit-btn {
			background-color: #4cd964 !important; /* 绿色按钮 */
		}
		.cancel-btn {
			background-color: #f0ad4e !important; /* 橙色按钮 */
			color: white !important;
		}
	}
}
</style>