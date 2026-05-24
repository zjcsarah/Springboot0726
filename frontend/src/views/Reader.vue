<template>
  <div>
    <div class="header">
      <h3>📚 图书馆 - 读者端</h3>
      <div class="user-info">
        <span>欢迎，{{ user.name }}</span>
        <el-button size="small" @click="showMyBorrows">我的借阅</el-button>
        <el-button size="small" type="danger" plain @click="logout">退出</el-button>
      </div>
    </div>
    <div class="container">
      <div class="search-bar">
        <el-input v-model="keyword" placeholder="搜索书名或作者..." style="width:300px" clearable @clear="search" />
        <el-select v-model="category" placeholder="选择分类" style="width:180px" clearable @clear="search">
          <el-option v-for="c in categories" :key="c" :label="c" :value="c" />
        </el-select>
        <el-button type="primary" @click="search">搜索</el-button>
        <el-button @click="resetSearch">重置</el-button>
        <span style="margin-left:auto;color:#909399">共 {{ total }} 本图书</span>
      </div>

      <el-pagination v-if="total > 0" style="margin-bottom:20px;justify-content:center"
        v-model:current-page="currentPage" :page-size="pageSize" :total="total"
        layout="total, prev, pager, next" @current-change="loadBooks" />

      <div v-if="books.length === 0" class="empty-state">
        <p style="font-size:48px">📖</p>
        <p style="margin-top:10px">暂无图书数据</p>
      </div>

      <div class="book-list">
        <div class="book-card" v-for="book in books" :key="book.id">
          <h4>{{ book.title }}</h4>
          <div class="info">
            <div>作者：{{ book.author }}</div>
            <div>分类：{{ book.category || '未分类' }}</div>
            <div>出版社：{{ book.publisher || '-' }}</div>
            <div v-if="book.description">简介：{{ book.description?.substring(0, 50) }}{{ book.description?.length > 50 ? '...' : '' }}</div>
          </div>
          <div class="quantity">可借：{{ book.availableQuantity }} / 共 {{ book.totalQuantity }} 本</div>
          <div class="actions">
            <el-button type="primary" size="small" @click="borrowBook(book)" :disabled="book.availableQuantity <= 0">
              {{ book.availableQuantity > 0 ? '借阅' : '已借完' }}
            </el-button>
            <el-button size="small" @click="showBookDetail(book)">详情</el-button>
          </div>
        </div>
      </div>
    </div>

    <el-dialog v-model="borrowDialogVisible" title="我的借阅记录" width="700px">
      <el-table :data="myBorrows" border stripe>
        <el-table-column prop="bookTitle" label="书名" width="180" />
        <el-table-column prop="borrowDate" label="借阅日期" width="170" />
        <el-table-column prop="dueDate" label="应还日期" width="170" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.status === 'BORROWED' ? 'warning' : 'success'" size="small">
              {{ row.status === 'BORROWED' ? '借阅中' : '已归还' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="100">
          <template #default="{ row }">
            <el-button v-if="row.status === 'BORROWED'" type="danger" size="small" @click="returnBook(row)">归还</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-dialog>

    <el-dialog v-model="detailDialogVisible" :title="currentBook?.title" width="500px">
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
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import axios from 'axios'

const router = useRouter()
const user = JSON.parse(sessionStorage.getItem('user') || '{}')
if (!user.id) { router.replace('/'); throw new Error('not login') }

const books = ref([])
const categories = ref([])
const keyword = ref('')
const category = ref('')
const total = ref(0)
const currentPage = ref(1)
const pageSize = ref(12)
const myBorrows = ref([])
const borrowDialogVisible = ref(false)
const detailDialogVisible = ref(false)
const currentBook = ref(null)

async function loadBooks() {
  try {
    const params = { pageNum: currentPage.value, pageSize: pageSize.value }
    if (keyword.value) params.keyword = keyword.value
    if (category.value) params.category = category.value
    const res = await axios.get('/api/books', { params })
    books.value = res.data.data.records || []
    total.value = res.data.data.total || 0
  } catch (e) { ElMessage.error('加载图书失败') }
}

async function loadCategories() {
  try {
    const res = await axios.get('/api/books/categories')
    categories.value = res.data.data || []
  } catch (e) {}
}

function search() { currentPage.value = 1; loadBooks() }
function resetSearch() { keyword.value = ''; category.value = ''; currentPage.value = 1; loadBooks() }

async function borrowBook(book) {
  try {
    await ElMessageBox.confirm(`确定要借阅《${book.title}》吗？借阅期限为30天。`, '确认借阅', { type: 'info' })
    const res = await axios.post('/api/borrow/' + book.id, { userId: user.id })
    if (res.data.code === 0) { ElMessage.success('借阅成功'); loadBooks() }
    else ElMessage.error(res.data.msg)
  } catch (e) {}
}

async function showMyBorrows() {
  try {
    const res = await axios.get('/api/borrows/my', { params: { userId: user.id, pageSize: 999 } })
    myBorrows.value = res.data.data.records || []
    borrowDialogVisible.value = true
  } catch (e) { ElMessage.error('加载借阅记录失败') }
}

async function returnBook(record) {
  try {
    await ElMessageBox.confirm('确定要归还此书吗？', '确认归还', { type: 'info' })
    const res = await axios.post('/api/return/' + record.id)
    if (res.data.code === 0) { ElMessage.success('归还成功'); showMyBorrows(); loadBooks() }
    else ElMessage.error(res.data.msg)
  } catch (e) {}
}

function showBookDetail(book) { currentBook.value = book; detailDialogVisible.value = true }

function logout() { sessionStorage.clear(); router.push('/') }

onMounted(() => { loadBooks(); loadCategories() })
</script>

<style scoped>
.header { background: #409eff; color: #fff; padding: 15px 30px; display: flex; justify-content: space-between; align-items: center; }
.header h3 { font-size: 18px; }
.header .user-info { display: flex; align-items: center; gap: 15px; }
.container { max-width: 1200px; margin: 20px auto; padding: 0 20px; }
.search-bar { background: #fff; padding: 20px; border-radius: 8px; margin-bottom: 20px; display: flex; gap: 15px; flex-wrap: wrap; align-items: center; }
.book-list { display: grid; grid-template-columns: repeat(auto-fill, minmax(280px, 1fr)); gap: 20px; margin-bottom: 30px; }
.book-card { background: #fff; border-radius: 8px; padding: 20px; box-shadow: 0 2px 12px rgba(0,0,0,0.06); transition: transform 0.2s; }
.book-card:hover { transform: translateY(-3px); box-shadow: 0 4px 20px rgba(0,0,0,0.1); }
.book-card h4 { color: #303133; margin-bottom: 8px; font-size: 16px; }
.book-card .info { color: #909399; font-size: 13px; line-height: 1.8; }
.book-card .quantity { margin-top: 10px; color: #409eff; font-weight: bold; }
.book-card .actions { margin-top: 12px; }
.empty-state { text-align: center; padding: 60px; color: #909399; }
body { background: #f5f7fa; font-family: 'Helvetica Neue', Helvetica, 'PingFang SC', 'Microsoft YaHei', sans-serif; }
</style>
