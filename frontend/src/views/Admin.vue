<template>
  <div>
    <div class="header">
      <h3>📚 图书馆 - 管理后台</h3>
      <div class="user-info">
        <span>管理员：{{ user.name }}</span>
        <el-button size="small" type="danger" plain @click="logout">退出</el-button>
      </div>
    </div>
    <div class="container">
      <el-tabs v-model="activeTab" type="border-card" @tab-change="handleTabChange">
        <el-tab-pane label="图书管理" name="books">
          <div style="margin-bottom:15px;display:flex;gap:10px;flex-wrap:wrap;align-items:center">
            <el-input v-model="searchKeyword" placeholder="搜索书名/作者" style="width:250px" clearable />
            <el-select v-model="searchCategory" placeholder="分类筛选" style="width:150px" clearable>
              <el-option v-for="c in categories" :key="c" :label="c" :value="c" />
            </el-select>
            <el-button type="primary" @click="searchBooks">搜索</el-button>
            <el-button type="success" @click="showAddBookDialog">新增图书</el-button>
          </div>
          <el-table :data="books" border stripe v-loading="bookLoading">
            <el-table-column prop="id" label="ID" width="60" />
            <el-table-column prop="title" label="书名" width="160" />
            <el-table-column prop="author" label="作者" width="120" />
            <el-table-column prop="category" label="分类" width="100" />
            <el-table-column prop="publisher" label="出版社" width="140" />
            <el-table-column prop="isbn" label="ISBN" width="130" />
            <el-table-column label="可借/总数" width="100">
              <template #default="{ row }">{{ row.availableQuantity }} / {{ row.totalQuantity }}</template>
            </el-table-column>
            <el-table-column label="操作" width="180">
              <template #default="{ row }">
                <el-button size="small" @click="showEditBookDialog(row)">编辑</el-button>
                <el-popconfirm title="确定删除此书？" @confirm="deleteBook(row.id)">
                  <template #reference><el-button size="small" type="danger">删除</el-button></template>
                </el-popconfirm>
              </template>
            </el-table-column>
          </el-table>
          <el-pagination v-if="bookTotal > 0" style="margin-top:15px;justify-content:center"
            v-model:current-page="bookPage" :page-size="bookPageSize" :total="bookTotal"
            layout="total, prev, pager, next" @current-change="loadBooks" />
        </el-tab-pane>

        <el-tab-pane label="读者管理" name="readers">
          <el-table :data="readers" border stripe v-loading="readerLoading">
            <el-table-column prop="id" label="ID" width="60" />
            <el-table-column prop="name" label="用户名" width="130" />
            <el-table-column prop="phone" label="联系电话" width="150">
              <template #default="{ row }">{{ row.phone || '-' }}</template>
            </el-table-column>
            <el-table-column prop="role" label="角色" width="100">
              <template #default="{ row }">
                <el-tag :type="row.role === 'admin' ? 'danger' : 'info'" size="small">{{ row.role }}</el-tag>
              </template>
            </el-table-column>
          </el-table>
          <el-pagination v-if="readerTotal > 0" style="margin-top:15px;justify-content:center"
            v-model:current-page="readerPage" :page-size="readerPageSize" :total="readerTotal"
            layout="total, prev, pager, next" @current-change="loadReaders" />
        </el-tab-pane>

        <el-tab-pane label="借阅记录" name="borrows">
          <el-table :data="borrowRecords" border stripe v-loading="borrowLoading">
            <el-table-column prop="id" label="ID" width="60" />
            <el-table-column prop="userName" label="借阅人" width="120" />
            <el-table-column prop="bookTitle" label="书名" width="180" />
            <el-table-column prop="borrowDate" label="借阅日期" width="170" />
            <el-table-column prop="dueDate" label="应还日期" width="170" />
            <el-table-column prop="returnDate" label="归还日期" width="170">
              <template #default="{ row }">{{ row.returnDate || '-' }}</template>
            </el-table-column>
            <el-table-column prop="status" label="状态" width="100">
              <template #default="{ row }">
                <el-tag :type="row.status === 'BORROWED' ? 'warning' : 'success'" size="small">
                  {{ row.status === 'BORROWED' ? '借阅中' : '已归还' }}
                </el-tag>
              </template>
            </el-table-column>
          </el-table>
          <el-pagination v-if="borrowTotal > 0" style="margin-top:15px;justify-content:center"
            v-model:current-page="borrowPage" :page-size="borrowPageSize" :total="borrowTotal"
            layout="total, prev, pager, next" @current-change="loadBorrowRecords" />
        </el-tab-pane>
      </el-tabs>
    </div>

    <el-dialog v-model="bookDialogVisible" :title="isEdit ? '编辑图书' : '新增图书'" width="550px">
      <el-form :model="bookForm" :rules="bookRules" ref="bookFormRef" label-width="90px">
        <el-form-item label="书名" prop="title"><el-input v-model="bookForm.title" /></el-form-item>
        <el-form-item label="作者" prop="author"><el-input v-model="bookForm.author" /></el-form-item>
        <el-form-item label="ISBN"><el-input v-model="bookForm.isbn" /></el-form-item>
        <el-form-item label="分类"><el-input v-model="bookForm.category" /></el-form-item>
        <el-form-item label="出版社"><el-input v-model="bookForm.publisher" /></el-form-item>
        <el-form-item label="出版日期">
          <el-date-picker v-model="bookForm.publishDate" type="date" placeholder="选择日期" style="width:100%" value-format="YYYY-MM-DD" />
        </el-form-item>
        <el-form-item label="简介"><el-input v-model="bookForm.description" type="textarea" rows="3" /></el-form-item>
        <el-form-item label="总数量" prop="totalQuantity">
          <el-input-number v-model="bookForm.totalQuantity" :min="1" :max="999" />
        </el-form-item>
        <el-form-item label="可借数量" prop="availableQuantity">
          <el-input-number v-model="bookForm.availableQuantity" :min="0" :max="999" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="bookDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="saveBook" :loading="saveLoading">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import axios from 'axios'

const router = useRouter()
const user = JSON.parse(sessionStorage.getItem('user') || '{}')
if (!user.id || user.role !== 'admin') { router.replace('/'); throw new Error('not admin') }

const activeTab = ref('books')
const searchKeyword = ref('')
const searchCategory = ref('')
const books = ref([])
const categories = ref([])
const readers = ref([])
const borrowRecords = ref([])
const bookLoading = ref(false)
const readerLoading = ref(false)
const borrowLoading = ref(false)
const bookPage = ref(1); const bookPageSize = ref(10); const bookTotal = ref(0)
const readerPage = ref(1); const readerPageSize = ref(10); const readerTotal = ref(0)
const borrowPage = ref(1); const borrowPageSize = ref(10); const borrowTotal = ref(0)

const bookDialogVisible = ref(false)
const isEdit = ref(false)
const saveLoading = ref(false)
const bookFormRef = ref(null)
const bookForm = reactive({ id: null, title: '', author: '', isbn: '', category: '', publisher: '', publishDate: '', description: '', totalQuantity: 1, availableQuantity: 1 })
const bookRules = {
  title: [{ required: true, message: '请输入书名', trigger: 'blur' }],
  author: [{ required: true, message: '请输入作者', trigger: 'blur' }],
}

async function loadBooks() {
  bookLoading.value = true
  try {
    const params = { pageNum: bookPage.value, pageSize: bookPageSize.value }
    if (searchKeyword.value) params.keyword = searchKeyword.value
    if (searchCategory.value) params.category = searchCategory.value
    const res = await axios.get('/api/books', { params })
    books.value = res.data.data.records || []
    bookTotal.value = res.data.data.total || 0
  } catch (e) { ElMessage.error('加载图书失败') }
  bookLoading.value = false
}

async function loadCategories() {
  try { const res = await axios.get('/api/books/categories'); categories.value = res.data.data || [] } catch (e) {}
}

async function loadReaders() {
  readerLoading.value = true
  try {
    const res = await axios.get('/api/readers', { params: { pageNum: readerPage.value, pageSize: readerPageSize.value } })
    readers.value = res.data.data.records || []
    readerTotal.value = res.data.data.total || 0
  } catch (e) { ElMessage.error('加载读者失败') }
  readerLoading.value = false
}

async function loadBorrowRecords() {
  borrowLoading.value = true
  try {
    const res = await axios.get('/api/borrows/all', { params: { pageNum: borrowPage.value, pageSize: borrowPageSize.value } })
    borrowRecords.value = res.data.data.records || []
    borrowTotal.value = res.data.data.total || 0
  } catch (e) { ElMessage.error('加载借阅记录失败') }
  borrowLoading.value = false
}

function searchBooks() { bookPage.value = 1; loadBooks() }

function showAddBookDialog() {
  isEdit.value = false
  Object.assign(bookForm, { id: null, title: '', author: '', isbn: '', category: '', publisher: '', publishDate: '', description: '', totalQuantity: 1, availableQuantity: 1 })
  bookDialogVisible.value = true
}

function showEditBookDialog(book) {
  isEdit.value = true
  Object.assign(bookForm, { id: book.id, title: book.title, author: book.author, isbn: book.isbn || '', category: book.category || '', publisher: book.publisher || '', publishDate: book.publishDate || '', description: book.description || '', totalQuantity: book.totalQuantity, availableQuantity: book.availableQuantity })
  bookDialogVisible.value = true
}

async function saveBook() {
  const valid = await bookFormRef.value.validate().catch(() => false)
  if (!valid) return
  saveLoading.value = true
  try {
    const res = isEdit.value
      ? await axios.put('/api/books/' + bookForm.id, bookForm)
      : await axios.post('/api/books', bookForm)
    if (res.data.code === 0) {
      ElMessage.success(res.data.msg)
      bookDialogVisible.value = false
      loadBooks(); loadCategories()
    } else {
      ElMessage.error(res.data.msg)
    }
  } catch (e) { ElMessage.error('操作失败') }
  saveLoading.value = false
}

async function deleteBook(id) {
  try {
    const res = await axios.delete('/api/books/' + id)
    if (res.data.code === 0) { ElMessage.success('删除成功'); loadBooks(); loadCategories() }
    else ElMessage.error(res.data.msg)
  } catch (e) { ElMessage.error('删除失败') }
}

function handleTabChange(name) {
  if (name === 'readers') loadReaders()
  else if (name === 'borrows') loadBorrowRecords()
}

function logout() { sessionStorage.clear(); router.push('/') }

onMounted(() => { loadBooks(); loadCategories() })
</script>

<style scoped>
.header { background: #303133; color: #fff; padding: 15px 30px; display: flex; justify-content: space-between; align-items: center; }
.header h3 { font-size: 18px; }
.header .user-info { display: flex; align-items: center; gap: 15px; }
.container { max-width: 1300px; margin: 20px auto; padding: 0 20px; }
body { background: #f5f7fa; font-family: 'Helvetica Neue', Helvetica, 'PingFang SC', 'Microsoft YaHei', sans-serif; }
</style>
