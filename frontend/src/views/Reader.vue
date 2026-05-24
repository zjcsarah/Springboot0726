<template>
  <div>
    <!--
      读者端页面
      功能：浏览图书、搜索、借阅、归还、查看借阅记录
    -->
    <!-- 顶部导航栏 -->
    <div class="header">
      <h3>图书馆 - 读者端</h3>
      <div class="user-info">
        <!-- {{ user.name }}: 双花括号是 Vue 的插值表达式，显示 JS 变量值 -->
        <span>欢迎，{{ user.name }}</span>
        <el-button size="small" @click="showMyBorrows">我的借阅</el-button>
        <el-button size="small" type="danger" plain @click="logout">退出</el-button>
      </div>
    </div>

    <div class="container">
      <!-- 搜索栏 -->
      <div class="search-bar">
        <!-- @clear: clearable 清空时触发 -->
        <el-input v-model="keyword" placeholder="搜索书名或作者..." style="width:300px" clearable @clear="search" />
        <el-select v-model="category" placeholder="选择分类" style="width:180px" clearable @clear="search">
          <!-- v-for 循环渲染下拉选项，:key 是 Vue 的列表渲染标识，提高渲染性能 -->
          <el-option v-for="c in categories" :key="c" :label="c" :value="c" />
        </el-select>
        <el-button type="primary" @click="search">搜索</el-button>
        <el-button @click="resetSearch">重置</el-button>
        <!-- style 中 margin-left:auto 将这个元素推到最右边 -->
        <span style="margin-left:auto;color:#909399">共 {{ total }} 本图书</span>
      </div>

      <!--
        分页组件
        v-model:current-page: 双向绑定当前页码
        :page-size: 每页条数
        :total: 总记录数
        layout: 显示的元素排列（总数、上一页、页码、下一页）
        @current-change: 页码变化事件，调用 loadBooks 重新加载
      -->
      <el-pagination v-if="total > 0" style="margin-bottom:20px;justify-content:center"
        v-model:current-page="currentPage" :page-size="pageSize" :total="total"
        layout="total, prev, pager, next" @current-change="loadBooks" />

      <!-- v-if 条件渲染：数据为空时显示占位图 -->
      <div v-if="books.length === 0" class="empty-state">
        <p style="font-size:48px">📖</p>
        <p style="margin-top:10px">暂无图书数据</p>
      </div>

      <!-- 图书卡片列表（CSS Grid 网格布局） -->
      <div class="book-list">
        <!-- v-for 遍历 books 数组，每个 book 渲染一张卡片 -->
        <div class="book-card" v-for="book in books" :key="book.id">
          <h4>{{ book.title }}</h4>
          <div class="info">
            <div>作者：{{ book.author }}</div>
            <div>分类：{{ book.category || '未分类' }}</div>
            <div>出版社：{{ book.publisher || '-' }}</div>
            <!--
              使用 JavaScript 表达式截取简介前 50 字
              substring(0, 50): 截取前50个字符
              ?. 可选链操作符：description 为 null/undefined 时不报错
            -->
            <div v-if="book.description">简介：{{ book.description?.substring(0, 50) }}{{ book.description?.length > 50 ? '...' : '' }}</div>
          </div>
          <div class="quantity">可借：{{ book.availableQuantity }} / 共 {{ book.totalQuantity }} 本</div>
          <div class="actions">
            <!-- :disabled 动态绑定禁用状态，可借数量 <= 0 时按钮不可点击 -->
            <el-button type="primary" size="small" @click="borrowBook(book)" :disabled="book.availableQuantity <= 0">
              <!-- 三元表达式：可借数 > 0 显示"借阅"，否则显示"已借完" -->
              {{ book.availableQuantity > 0 ? '借阅' : '已借完' }}
            </el-button>
            <el-button size="small" @click="showBookDetail(book)">详情</el-button>
          </div>
        </div>
      </div>
    </div>

    <!--
      我的借阅对话框
      el-dialog: Element Plus 对话框组件
      v-model="borrowDialogVisible": 控制显示/隐藏
    -->
    <el-dialog v-model="borrowDialogVisible" title="我的借阅记录" width="700px">
      <el-table :data="myBorrows" border stripe>
        <el-table-column prop="bookTitle" label="书名" width="180" />
        <el-table-column prop="borrowDate" label="借阅日期" width="170" />
        <el-table-column prop="dueDate" label="应还日期" width="170" />
        <el-table-column prop="status" label="状态" width="100">
          <!--
            #default="{ row }": Vue 3 插槽语法（v-slot 简写）
            { row } 解构出行数据，相当于 slotProps.row
          -->
          <template #default="{ row }">
            <!-- 根据状态显示不同颜色标签：BORROWED 橙色，其它绿色 -->
            <el-tag :type="row.status === 'BORROWED' ? 'warning' : 'success'" size="small">
              {{ row.status === 'BORROWED' ? '借阅中' : '已归还' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="100">
          <template #default="{ row }">
            <!-- v-if 条件渲染：只有借阅中的记录才显示归还按钮 -->
            <el-button v-if="row.status === 'BORROWED'" type="danger" size="small" @click="returnBook(row)">归还</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-dialog>

    <!-- 图书详情对话框 -->
    <el-dialog v-model="detailDialogVisible" :title="currentBook?.title" width="500px">
      <!-- v-if 确保 currentBook 有值时才渲染内容 -->
      <div v-if="currentBook" style="line-height:2.2">
        <p><b>书名：</b>{{ currentBook.title }}</p>
        <p><b>作者：</b>{{ currentBook.author }}</p>
        <p><b>ISBN：</b>{{ currentBook.isbn || '-' }}</p>
        <p><b>分类：</b>{{ currentBook.category || '-' }}</p>
        <p><b>出版社：</b>{{ currentBook.publisher || '-' }}</p>
        <p><b>出版日期：</b>{{ currentBook.publishDate || '-' }}</p>
        <p><b>简介：</b>{{ currentBook.description || '暂无简介' }}</p>
        <p><b>可借数量：</b>{{ currentBook.availableQuantity }} / {{ currentBook.totalQuantity }}</p>
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
// ========== 导入依赖 ==========
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import axios from 'axios'

const router = useRouter()

// ========== 获取当前登录用户 ==========
// sessionStorage.getItem 从浏览器存储中读取登录时保存的用户信息
const user = JSON.parse(sessionStorage.getItem('user') || '{}')
// 未登录则强制跳转到登录页
if (!user.id) { router.replace('/'); throw new Error('not login') }

// ========== 响应式数据 ==========
const books = ref([])           // 图书列表
const categories = ref([])      // 分类列表（用于下拉筛选）
const keyword = ref('')         // 搜索关键词
const category = ref('')        // 选中的分类
const total = ref(0)            // 图书总数
const currentPage = ref(1)      // 当前页码
const pageSize = ref(12)        // 每页显示 12 本书（配合网格布局）
const myBorrows = ref([])       // 我的借阅记录
const borrowDialogVisible = ref(false)   // 借阅记录对话框是否显示
const detailDialogVisible = ref(false)   // 图书详情对话框是否显示
const currentBook = ref(null)            // 当前查看详情的图书对象

// ========== 加载图书列表 ==========
async function loadBooks() {
  try {
    const params = { pageNum: currentPage.value, pageSize: pageSize.value }
    // 如果有搜索关键词，加入请求参数
    if (keyword.value) params.keyword = keyword.value
    // 如果有分类筛选，加入请求参数
    if (category.value) params.category = category.value
    // axios.get(url, { params }): GET 请求，params 自动拼接为查询字符串 ?pageNum=1&pageSize=12
    const res = await axios.get('/api/books', { params })
    books.value = res.data.data.records || []   // records 是当前页数据
    total.value = res.data.data.total || 0       // total 是总记录数
  } catch (e) { ElMessage.error('加载图书失败') }
}

// ========== 加载图书分类 ==========
async function loadCategories() {
  try {
    const res = await axios.get('/api/books/categories')
    categories.value = res.data.data || []
  } catch (e) {}
}

// ========== 搜索图书 ==========
// 搜索时将页码重置为第 1 页
function search() { currentPage.value = 1; loadBooks() }

// ========== 重置搜索条件 ==========
function resetSearch() {
  keyword.value = ''
  category.value = ''
  currentPage.value = 1
  loadBooks()
}

// ========== 借阅图书 ==========
async function borrowBook(book) {
  try {
    // ElMessageBox.confirm: 确认对话框，用户点击"确定"才继续执行
    await ElMessageBox.confirm(`确定要借阅《${book.title}》吗？借阅期限为30天。`, '确认借阅', { type: 'info' })
    // 请求借阅接口，URL 中 book.id 是路径参数，body 中传 userId
    const res = await axios.post('/api/borrow/' + book.id, { userId: user.id })
    if (res.data.code === 0) {
      ElMessage.success('借阅成功')
      loadBooks()  // 刷新图书列表（可借数量会变化）
    } else {
      ElMessage.error(res.data.msg)
    }
  } catch (e) {
    // 用户取消确认会抛异常，这里忽略即可
  }
}

// ========== 查看我的借阅记录 ==========
async function showMyBorrows() {
  try {
    // pageSize: 999 一次性查出所有借阅记录（一般用户借阅量不会超）
    const res = await axios.get('/api/borrows/my', { params: { userId: user.id, pageSize: 999 } })
    myBorrows.value = res.data.data.records || []
    borrowDialogVisible.value = true   // 打开对话框
  } catch (e) { ElMessage.error('加载借阅记录失败') }
}

// ========== 归还图书 ==========
async function returnBook(record) {
  try {
    await ElMessageBox.confirm('确定要归还此书吗？', '确认归还', { type: 'info' })
    const res = await axios.post('/api/return/' + record.id)
    if (res.data.code === 0) {
      ElMessage.success('归还成功')
      showMyBorrows()  // 刷新借阅记录列表
      loadBooks()      // 刷新图书列表（可借数量恢复）
    } else {
      ElMessage.error(res.data.msg)
    }
  } catch (e) {}
}

// ========== 查看图书详情 ==========
function showBookDetail(book) {
  currentBook.value = book
  detailDialogVisible.value = true
}

// ========== 退出登录 ==========
function logout() {
  sessionStorage.clear()   // 清空浏览器存储
  router.push('/')         // 跳转到登录页
}

// onMounted: Vue 生命周期钩子，组件挂载到页面后执行（类似 jQuery 的 $(document).ready）
onMounted(() => {
  loadBooks()
  loadCategories()
})
</script>

<style scoped>
/* 页面顶部导航栏（蓝色背景） */
.header { background: #409eff; color: #fff; padding: 15px 30px; display: flex; justify-content: space-between; align-items: center; }
.header h3 { font-size: 18px; }
.header .user-info { display: flex; align-items: center; gap: 15px; }

/* 内容区域 */
.container { max-width: 1200px; margin: 20px auto; padding: 0 20px; }

/* 搜索工具栏 */
.search-bar { background: #fff; padding: 20px; border-radius: 8px; margin-bottom: 20px; display: flex; gap: 15px; flex-wrap: wrap; align-items: center; }

/* 图书网格布局
   grid-template-columns: repeat(auto-fill, minmax(280px, 1fr))
   → 自动填充列，每列最小 280px，最大均分剩余空间，实现响应式卡片排列 */
.book-list { display: grid; grid-template-columns: repeat(auto-fill, minmax(280px, 1fr)); gap: 20px; margin-bottom: 30px; }

/* 图书卡片 */
.book-card { background: #fff; border-radius: 8px; padding: 20px; box-shadow: 0 2px 12px rgba(0,0,0,0.06); transition: transform 0.2s; }
/* :hover 鼠标悬停时卡片上浮效果 */
.book-card:hover { transform: translateY(-3px); box-shadow: 0 4px 20px rgba(0,0,0,0.1); }
.book-card h4 { color: #303133; margin-bottom: 8px; font-size: 16px; }
.book-card .info { color: #909399; font-size: 13px; line-height: 1.8; }
.book-card .quantity { margin-top: 10px; color: #409eff; font-weight: bold; }
.book-card .actions { margin-top: 12px; }

/* 空数据占位 */
.empty-state { text-align: center; padding: 60px; color: #909399; }
body { background: #f5f7fa; font-family: 'Helvetica Neue', Helvetica, 'PingFang SC', 'Microsoft YaHei', sans-serif; }
</style>