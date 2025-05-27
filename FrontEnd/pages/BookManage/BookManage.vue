<template>
	<view class="content">
		<header-vue></header-vue>
		<view class="left">
			<sider-nav-vue></sider-nav-vue>
		</view>
		<!-- 右侧内容区域 -->
		<view class="right-content">
			<!-- 页面标题与搜索栏 -->
			<view class="t-b">
				<view class="page-title">||图书管理功能</view>
				<view class="action-buttons">
					<button class="btn-new" @click="handleNewBook()">新增图书</button>
					<button class="btn-del" @click="handleBatchDelete">批量删除</button>
				</view>
			</view>
			
			<form class="search-form">
				<view class="form-row">
					<view class="form-group">
						<label for="book-name">图书名称:</label>
						<input type="text" id="book-name" v-model="searchName" placeholder="请输入图书名称" class="input-field"/>
					</view>
					
					<view class="form-group">
						<label for="isbn">ISBN编号:</label>
						<input type="text" id="isbn" v-model="searchISBN" placeholder="请输入ISBN编号" class="input-field"/>
					</view>
					
					<view class="form-group">
						<label for="author">作者:</label>
						<input type="text" id="author" v-model="searchAuthor" placeholder="请输入作者姓名" class="input-field"/>
					</view>
					
					<view class="form-group">
						<label for="category">所属分类:</label>
						<input type="text" id="category" v-model="searchCategory" placeholder="请输入分类名称" class="input-field"/>
					</view>
					
					<view class="form-group">
						<button form-type="submit" class="btn-search" @click.prevent="handleSearch">搜索</button>
					</view>
					
				</view>
			</form>
			
			<table id="bookTable" class="form_mat">
			          <thead>
			            <tr>
			              <!-- <th><input type="checkbox" id="selectAll" class="custom-checkbox"></th> -->
						 
						  <th>
						    <checkbox-group @change="handleSelectAll">
						      <checkbox :checked="isAllSelected" />
						    </checkbox-group>
						  </th>
						  <th>图书馆自编编号</th>
			              <th>ISBN号</th>
			              <th>图书名称</th>
			              <th>作者</th>
			              <th>所属分类</th>
			              <th>借阅状态</th>
						  <th>入库时间</th>
			              <!-- <th>出版日期</th>
			              <th>库存数量</th>
			              <th>最后更新时间</th> -->
			              <th>操作</th>
			            </tr>
			          </thead>
					  <tbody>
					   <tr v-for="book in bookInfo" :key="book.id">
					        <!-- <td><input type="checkbox" class="custom-checkbox"></td> -->
							<td>
								<checkbox-group @change="(e) => handleCheckboxChange(book.id,e)">
									<checkbox :value="book.id" :checked="selectedIds.includes(book.id)"/>
								</checkbox-group>
							</td>
							<td>{{book.libraryCode}}</td>
					        <td>{{book.isbn}}</td>
					        <td>{{book.title}}</td>
					        <td>{{book.author}}</td>
					        <td>{{book.category}}</td>
					        <td>{{book.status ? '可借' : '已借'}}</td>
							<td>{{formatDate(book.entryTime)}}</td>
					        <td>
					          <view class="operate-buttons" style="color: #1890ff; gap: 20rpx; display: flex;">
								<view class="btn-edit" @click="handleEdit(book.libraryCode)">编辑</view>
								<view class="btn-delete" @click="handleDelete(book.id)">删除</view>
					          </view>
					        </td>
					      </tr>
						  
						</tbody>
			</table>
		</view>
	</view>
</template>

<script setup>
	import headerVue from '../../components/header.vue';
	import siderNavVue from '../../components/siderNav.vue';
	import {ref, onMounted, computed} from 'vue';
	const bookInfo = ref([]);
	const searchName = ref('');
	const searchISBN = ref('');
	const searchAuthor = ref('')
	const searchCategory = ref('');
	const selectedIds = ref([])//存储选中图书的ID
	
	
	// 时间格式化函数
	const formatDate = (dateStr) => {
	  if (!dateStr) return '-';
	  
	  try {
	    // 处理ISO格式的日期字符串
	    const date = new Date(dateStr);
	    
	    // 获取年、月、日
	    const year = date.getFullYear();
	    const month = String(date.getMonth() + 1).padStart(2, '0');
	    const day = String(date.getDate()).padStart(2, '0');
	    
	    // 获取时、分、秒
	    const hours = String(date.getHours()).padStart(2, '0');
	    const minutes = String(date.getMinutes()).padStart(2, '0');
	    const seconds = String(date.getSeconds()).padStart(2, '0');
	    
	    // 返回格式化后的字符串：YYYY-MM-DD HH:MM:SS
	    return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
	  } catch (error) {
	    console.error('日期格式化失败:', error);
	    return dateStr;
	  }
	};
	
	//全选状态计算属性
	const isAllSelected = computed(() =>{
		return bookInfo.value.length > 0 && selectedIds.value.length === bookInfo.value.length;
	});
	
	
	//展示图书信息
	const fetchBook = async(url) =>{
		//获取token
		const token = uni.getStorageSync('token');
		if(!token){
			uni.showToast({
				title:'请先登录',
				icon:'none'
			});
			return uni.reLaunch({
				url:'/pages/lb_login/lb_login'
			});
		}
		try{
			const res = await uni.request({
				url,
				method:'GET',
				header:{
					'Authorization': `Bearer ${token}`
				}
			});
			console.log("data",res);
			if(res.statusCode === 200){
				bookInfo.value = Array.isArray(res.data) ? res.data : [res.data];
				selectedIds.value = [];//清空选中状态
				console.log('数据加载成功，共' + bookInfo.value.length + '条记录');
				if(bookInfo.value.length > 0){
					console.log('第一条数据结构：',bookInfo.value[0]);
				}
			}else{
				uni.showToast({
					title:'图书获取失败',
					icon:'fail'
				});
				bookInfo.value = [];
			}
			
		}catch(error){
			uni.showToast({
				title:'请求失败，请检查网络',
				icon:'none'
			});
			bookInfo.value = [];
		}	
		
	};
	
	
	// 处理搜索
	const handleSearch = async () =>{
		if(searchName.value&&searchISBN.value&&searchAuthor.value&&searchCategory.value){
			uni.showToast({
				title:'请使用单一条件搜索',
				icon:'none'
			});
			return
		}
		
		
		let apiUrl = 'http://localhost:9000/admin/book/get';
		if(searchName.value){
			apiUrl = `http://localhost:9000/admin/book/searchbytitle/${encodeURIComponent(searchName.value)}`;
		}else if(searchISBN.value){
			apiUrl = `http://localhost:9000/admin/book/searchbyisbn/${encodeURIComponent(searchISBN.value)}`
		}else if(searchAuthor.value){
			apiUrl = `http://localhost:9000/admin/book/searchbyauthor/${encodeURIComponent(searchAuthor.value)}`
		}else if(searchCategory.value){
			apiUrl = `http://localhost:9000/admin/book/searchbycategory/${encodeURIComponent(searchCategory.value)}`
		}
		
		await fetchBook(apiUrl);
	}
	
	
	//处理复选框变化
	const handleCheckboxChange = (bookId,event) => {
		console.log('复选框事件触发，图书id',bookId);
		console.log('事件对象',event);
		
		const checkValues = event.detail.value;
		
		if(checkValues.includes(bookId)){
			if(!selectedIds.value.includes(bookId)){
				selectedIds.value = [...selectedIds.value,bookId];
			}
		}else{
			selectedIds.value = selectedIds.value.filter(id => id !== bookId);
		}
		
		console.log('更新后选中的ID列表',selectedIds.value)
	};
	
	// 全选/反选
	const handleSelectAll = (event) =>{
		console.log('全选按钮事件触发');
		const isChecked = event.detail.value.length > 0;
		if(isChecked){
			selectedIds.value = bookInfo.value.map(book => book.id);
		}else{
			selectedIds.value = [];
		}
		
		console.log('全选操作后选中的ID列表',selectedIds.value)
	}
	
	// 单个删除
	const handleDelete = async(bookId) =>{
		const token = uni.getStorageSync('token');
		if(!token){
			uni.showToast({title:'请先登录',icon:'none',});
			return uni.reLaunch({url:'/pages/lb_login/lb_login'});
			}
			
		try {
			const res = await uni.request({
				url:`http://localhost:9000/admin/delete/${bookId}`,
				method:'DELETE',
				header:{
					'Authorization': `Bearer ${token}`,
					'Content-Type': 'application/json' // 添加Content-Type
				}
			});
			
			if(res.statusCode === 200){
				uni.showToast({title:'删除成功'});
				await fetchBook('http://localhost:9000/admin/book/get')
			}else{
				uni.showToast({title:'删除失败',icon:'none'});
			}
		} catch (error) {
			uni.showToast({
				title:'请求失败，请检查网络',icon:'none'
			});
		}
		
	}
	
	
	// 批量删除
	const handleBatchDelete = async() =>{
		console.log('准备批量删除，选中的ID列表',selectedIds.value);
		console.log('选中的ID数量',selectedIds.value.length);
		if(selectedIds.value.length === 0){
			uni.showToast({title:'请选择要删除的图书',icon:'none'});
			return;
		}
		
		const token = uni.getStorageSync('token');
		if(!token){
			uni.showToast({title:'请先登录',icon:'none'});
			return uni.reLaunch({url:'/pages/lb_login/lb_login'});
		}
		
		try {
			const res = await uni.request({
				url:'http://localhost:9000/admin/book/delete/batch',
				method:'DELETE',
				header:{
					'Authorization': `Bearer ${token}`,
					'Content-Type': 'application/json' // 必须指定JSON格式
				},
				data:selectedIds.value//传递图书ID数组
			});
			
			if(res.statusCode === 200){
				uni.showToast({ title: '批量删除成功' });
				await fetchBook('http://localhost:9000/admin/book/get');
				selectedIds.value = [];
			}else{
				uni.showToast({title:'批量删除失败',icon:'none'});
				console.error('批量删除失败,响应：',res);
			}
			
		} catch (error) {
			uni.showToast({title:'请求失败，请检查网络',icon:'none'});
			console.error('批量删除请求错误',error)
		}
	}
	onMounted(() =>{
		fetchBook('http://localhost:9000/admin/book/get')
	})
	
	// 新增图书
	const handleNewBook = () =>{
		uni.navigateTo({
			url:'/pages/BookManage/NewBookForm'
		})
	}
	// 处理编辑跳转
	const handleEdit = (libCode) => {
		uni.navigateTo({
			 url: `/pages/BookManage/EditBookForm?libraryCode=${encodeURIComponent(libCode)}` // 传递no参数
		});
	};

</script>

<style lang="scss" scoped>
	.content {
		display: flex;
		flex-direction: column;
		align-items: center;
		justify-content: center;
		
		
	}

	.left{
		position: fixed;
		top:380rpx;
		left: 0rpx;
		
	}
	
	.right-content{
		margin-left: 800rpx;
		margin-top: 300rpx;
		padding: 20rpx;
		width: calc(100vw - 900rpx);
		height: calc(100vh - 250rpx);
		// background-color: pink;
		.t-b{
			display: flex;
			justify-content: space-between;
			.page-title{
				font-size: 80rpx;
				color:  #1890ff;
				
			}
			.action-buttons{
				display: flex;
				gap: 20rpx;
				.btn-new{
					width: 250rpx;
					height: 100rpx;
					color:  #fff;
					background-color: #1890ff;
				}
				
				.btn-del{
					width: 250rpx;
					height: 100rpx;
					color:  #fff;
					background-color: #FF4D4F;
				}
			}
			
		}
		
		
		.search-form{
			display: flex;
			flex-wrap: wrap;
			gap: 20rpx;
			margin-top: 80rpx;
			margin-bottom: 20rpx;
			
			.form-row{
				display: flex;
				gap: 100rpx;
				margin-bottom: 20rpx;
				
				.form-group{
					display: flex;
					// flex-direction: column;
					gap: 20rpx;
					
					label{
						font-size: 40rpx;
						display: flex;
						align-items: center;
						color: #666;
					}
					
					.input-field{
						padding: 18rpx 24rpx;
						border: 3rpx solid #000;
						border-radius: 10rpx;
						width: 700rpx;
						
					}
					
					.btn-search{
						width: 250rpx;
						height: 100rpx;
						color:  #fff;
						background-color: #1890ff;
					}
					
				}
				
			}
		}
	
		#bookTable{
			width: 100%;
			border-collapse: collapse;
		}
		#bookTable th,
		#bookTable td{
			border: 1rpx solid #ccc;
			padding: 8rpx;
			text-align: left;
			vertical-align: middle;
		}
		
		#bookTable th{
			background-color: #f2f2f2
		}
	}
</style>
