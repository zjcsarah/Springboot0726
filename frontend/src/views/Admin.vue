<!--
  管理后台页面
  功能：三个标签页 - 图书管理（增删改查）、读者管理（增删改查）、借阅记录（增删改查）
  所有列表均支持分页
-->
<template>
  <div>
    <!-- 顶部导航栏（深色背景） -->
    <div class="header">
      <h3>图书馆 - 管理后台</h3>
      <div class="user-info">
        <span>管理员：{{ user.name }}</span>
        <el-button size="small" type="danger" plain @click="logout">退出</el-button>
      </div>
    </div>

    <div class="container">
      <!--
        el-tabs: 标签页容器
        v-model="activeTab": 双向绑定当前激活的标签页 name
        type="border-card": 带边框卡片样式
        @tab-change: 标签页切换事件，用于懒加载数据（切换到某个标签页才请求数据）
      -->
      <el-tabs v-model="activeTab" type="border-card" @tab-change="handleTabChange">

        <!-- ==================== 标签页1：图书管理 ==================== -->
        <el-tab-pane label="图书管理" name="books">
          <!-- 工具栏：搜索框 + 分类筛选 + 按钮 -->
          <div style="margin-bottom:15px;display:flex;gap:10px;flex-wrap:wrap;align-items:center">
            <!-- clearable: 输入框右侧显示清空按钮 -->
            <el-input v-model="searchKeyword" placeholder="搜索书名/作者" style="width:250px" clearable />
            <!-- el-select: 下拉选择器 -->
            <el-select v-model="searchCategory" placeholder="分类筛选" style="width:150px" clearable>
              <el-option v-for="c in categories" :key="c" :label="c" :value="c" />
            </el-select>
            <el-button type="primary" @click="searchBooks">搜索</el-button>
            <el-button type="success" @click="showAddBookDialog">新增图书</el-button>
          </div>

          <!--
            图书表格
            :data="books": 绑定表格数据源
            border: 显示纵向边框
            stripe: 隔行变色
            v-loading: 加载动画，值为 true 时表格显示遮罩层
          -->
          <el-table :data="books" border stripe v-loading="bookLoading">
            <!-- prop: 对应数据对象中的字段名；label: 列标题；width: 列宽度（px） -->
            <el-table-column prop="id" label="ID" width="60" />
            <el-table-column prop="title" label="书名" width="160" />
            <el-table-column prop="author" label="作者" width="120" />
            <el-table-column prop="category" label="分类" width="100" />
            <el-table-column prop="publisher" label="出版社" width="140" />
            <el-table-column prop="isbn" label="ISBN" width="130" />
            <!-- 自定义列：通过插槽 template 自定义单元格内容 -->
            <el-table-column label="可借/总数" width="100">
              <template #default="{ row }">{{ row.availableQuantity }} / {{ row.totalQuantity }}</template>
            </el-table-column>
            <!-- 操作列 -->
            <el-table-column label="操作" width="180">
              <template #default="{ row }">
                <el-button size="small" @click="showEditBookDialog(row)">编辑</el-button>
                <!-- el-popconfirm: 气泡确认框，点击后弹出确认提示 -->
                <el-popconfirm title="确定删除此书？" @confirm="deleteBook(row.id)">
                  <!-- #reference 插槽：触发气泡框的元素 -->
                  <template #reference><el-button size="small" type="danger">删除</el-button></template>
                </el-popconfirm>
              </template>
            </el-table-column>
          </el-table>

          <!-- 分页组件 -->
          <el-pagination v-if="bookTotal > 0" style="margin-top:15px;justify-content:center"
            v-model:current-page="bookPage" :page-size="bookPageSize" :total="bookTotal"
            layout="total, prev, pager, next" @current-change="loadBooks" />
        </el-tab-pane>

        <!-- ==================== 标签页2：读者管理 ==================== -->
        <el-tab-pane label="读者管理" name="readers">
          <div style="margin-bottom:15px;display:flex;gap:10px;align-items:center">
            <el-input v-model="readerKeyword" placeholder="搜索用户名" style="width:250px" clearable />
            <el-button type="primary" @click="searchReaders">搜索</el-button>
            <el-button type="success" @click="showAddReaderDialog">新增读者</el-button>
          </div>
          <el-table :data="readers" border stripe v-loading="readerLoading">
            <el-table-column prop="id" label="ID" width="60" />
            <el-table-column prop="name" label="用户名" width="130" />
            <el-table-column prop="phone" label="联系电话" width="150">
              <!-- || 运算符：phone 为空时显示 '-' -->
              <template #default="{ row }">{{ row.phone || '-' }}</template>
            </el-table-column>
            <el-table-column prop="role" label="角色" width="100">
              <template #default="{ row }">
                <!-- el-tag: 标签组件，:type 动态绑定颜色 -->
                <el-tag :type="row.role === 'admin' ? 'danger' : 'info'" size="small">{{ row.role }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column label="操作" width="180">
              <template #default="{ row }">
                <el-button size="small" @click="showEditReaderDialog(row)">编辑</el-button>
                <el-popconfirm title="确定删除此读者？" @confirm="deleteReader(row.id)">
                  <template #reference><el-button size="small" type="danger">删除</el-button></template>
                </el-popconfirm>
              </template>
            </el-table-column>
          </el-table>
          <el-pagination v-if="readerTotal > 0" style="margin-top:15px;justify-content:center"
            v-model:current-page="readerPage" :page-size="readerPageSize" :total="readerTotal"
            layout="total, prev, pager, next" @current-change="loadReaders" />
        </el-tab-pane>

        <!-- ==================== 标签页3：借阅记录 ==================== -->
        <el-tab-pane label="借阅记录" name="borrows">
          <div style="margin-bottom:15px">
            <el-button type="success" @click="showAddBorrowDialog">新增借阅</el-button>
          </div>
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
            <el-table-column label="操作" width="180">
              <template #default="{ row }">
                <el-button size="small" @click="showEditBorrowDialog(row)">编辑</el-button>
                <el-popconfirm title="确定删除此记录？" @confirm="deleteBorrow(row.id)">
                  <template #reference><el-button size="small" type="danger">删除</el-button></template>
                </el-popconfirm>
              </template>
            </el-table-column>
          </el-table>
          <el-pagination v-if="borrowTotal > 0" style="margin-top:15px;justify-content:center"
            v-model:current-page="borrowPage" :page-size="borrowPageSize" :total="borrowTotal"
            layout="total, prev, pager, next" @current-change="loadBorrowRecords" />
        </el-tab-pane>

      </el-tabs>
    </div>

    <!-- ==================== 图书新增/编辑对话框 ==================== -->
    <!--
      v-model="bookDialogVisible": 对话框的显示/隐藏由这个变量控制
      :title="isEdit ? '编辑图书' : '新增图书'": 动态标题，新增/编辑显示不同文字
    -->
    <el-dialog v-model="bookDialogVisible" :title="isEdit ? '编辑图书' : '新增图书'" width="550px">
      <!--
        :model: 表单绑定的数据对象，表单输入会自动更新该对象的属性
        :rules: 表单校验规则对象
        ref: 模板引用，JS 中通过 bookFormRef.value 访问该组件实例
        label-width: 标签宽度
      -->
      <el-form :model="bookForm" :rules="bookRules" ref="bookFormRef" label-width="90px">
        <!-- prop 关联 rules 中的同名规则 -->
        <el-form-item label="书名" prop="title"><el-input v-model="bookForm.title" /></el-form-item>
        <el-form-item label="作者" prop="author"><el-input v-model="bookForm.author" /></el-form-item>
        <el-form-item label="ISBN"><el-input v-model="bookForm.isbn" /></el-form-item>
        <el-form-item label="分类"><el-input v-model="bookForm.category" /></el-form-item>
        <el-form-item label="出版社"><el-input v-model="bookForm.publisher" /></el-form-item>
        <el-form-item label="出版日期">
          <!-- el-date-picker: 日期选择器，value-format 指定输出格式 -->
          <el-date-picker v-model="bookForm.publishDate" type="date" placeholder="选择日期" style="width:100%" value-format="YYYY-MM-DD" />
        </el-form-item>
        <el-form-item label="简介">
          <el-input v-model="bookForm.description" type="textarea" rows="3" />
        </el-form-item>
        <el-form-item label="总数量" prop="totalQuantity">
          <!-- el-input-number: 数字输入框，:min/:max 限制范围 -->
          <el-input-number v-model="bookForm.totalQuantity" :min="1" :max="999" />
        </el-form-item>
        <el-form-item label="可借数量" prop="availableQuantity">
          <el-input-number v-model="bookForm.availableQuantity" :min="0" :max="999" />
        </el-form-item>
      </el-form>
      <!-- #footer 插槽：对话框底部按钮区 -->
      <template #footer>
        <el-button @click="bookDialogVisible = false">取消</el-button>
        <!-- :loading 保存中显示加载动画，防止重复提交 -->
        <el-button type="primary" @click="saveBook" :loading="saveLoading">保存</el-button>
      </template>
    </el-dialog>

    <!-- ==================== 读者新增/编辑对话框 ==================== -->
    <el-dialog v-model="readerDialogVisible" :title="isReaderEdit ? '编辑读者' : '新增读者'" width="450px">
      <el-form :model="readerForm" :rules="readerRules" ref="readerFormRef" label-width="90px">
        <el-form-item label="用户名" prop="name"><el-input v-model="readerForm.name" /></el-form-item>
        <el-form-item label="密码" prop="password">
          <!-- show-password: 可切换密码的显示/隐藏 -->
          <el-input v-model="readerForm.password" type="password" show-password />
        </el-form-item>
        <el-form-item label="联系电话"><el-input v-model="readerForm.phone" /></el-form-item>
        <el-form-item label="角色">
          <el-select v-model="readerForm.role" style="width:100%">
            <el-option label="读者" value="user" />
            <el-option label="管理员" value="admin" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="readerDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="saveReader" :loading="readerSaveLoading">保存</el-button>
      </template>
    </el-dialog>

    <!-- ==================== 借阅记录新增/编辑对话框 ==================== -->
    <el-dialog v-model="borrowDialogVisible" :title="isBorrowEdit ? '编辑借阅' : '新增借阅'" width="500px">
      <el-form :model="borrowForm" :rules="borrowRules" ref="borrowFormRef" label-width="90px">
        <el-form-item label="借阅人" prop="userId">
          <!-- filterable: 下拉框支持输入搜索 -->
          <el-select v-model="borrowForm.userId" style="width:100%" filterable placeholder="请选择读者">
            <!-- 遍历 allReaders 生成选项，label 是显示文字，value 是选中后的值 -->
            <el-option v-for="r in allReaders" :key="r.id" :label="r.name + ' (' + (r.phone || '无电话') + ')'" :value="r.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="图书" prop="bookId">
          <el-select v-model="borrowForm.bookId" style="width:100%" filterable placeholder="请选择图书">
            <el-option v-for="b in allBooks" :key="b.id" :label="b.title + ' - ' + b.author" :value="b.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="借阅日期">
          <!-- type="datetime": 可以选择日期+时间 -->
          <el-date-picker v-model="borrowForm.borrowDate" type="datetime" placeholder="选择日期" style="width:100%" value-format="YYYY-MM-DD HH:mm:ss" />
        </el-form-item>
        <el-form-item label="应还日期">
          <el-date-picker v-model="borrowForm.dueDate" type="datetime" placeholder="选择日期" style="width:100%" value-format="YYYY-MM-DD HH:mm:ss" />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="borrowForm.status" style="width:100%">
            <el-option label="借阅中" value="BORROWED" />
            <el-option label="已归还" value="RETURNED" />
          </el-select>
        </el-form-item>
        <!-- v-if 条件渲染：只有状态为"已归还"时才显示归还日期字段 -->
        <el-form-item label="归还日期" v-if="borrowForm.status === 'RETURNED'">
          <el-date-picker v-model="borrowForm.returnDate" type="datetime" placeholder="选择日期" style="width:100%" value-format="YYYY-MM-DD HH:mm:ss" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="borrowDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="saveBorrow" :loading="borrowSaveLoading">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
// ========== 导入依赖 ==========
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import axios from 'axios'

const router = useRouter()

// ========== 权限校验 ==========
// 从 sessionStorage 读取登录时保存的用户信息
const user = JSON.parse(sessionStorage.getItem('user') || '{}')
// 非管理员跳回登录页
if (!user.id || user.role !== 'admin') { router.replace('/'); throw new Error('not admin') }

// ========== 通用状态 ==========
const activeTab = ref('books')  // 当前激活的标签页名称

// ========== 图书管理相关状态 ==========
const searchKeyword = ref('')       // 搜索关键词
const searchCategory = ref('')      // 分类筛选
const books = ref([])               // 图书列表数据
const categories = ref([])          // 分类列表（下拉选项用）
const bookLoading = ref(false)      // 表格加载状态
// 分页：每页10条
const bookPage = ref(1)
const bookPageSize = ref(10)
const bookTotal = ref(0)

// ========== 图书对话框状态 ==========
const bookDialogVisible = ref(false) // 对话框是否显示
const isEdit = ref(false)            // 是否为编辑模式（false=新增，true=编辑）
const saveLoading = ref(false)       // 保存按钮加载状态
const bookFormRef = ref(null)        // 表单组件引用

// 图书表单数据（reactive 对象，适合表单双向绑定）
const bookForm = reactive({
  id: null, title: '', author: '', isbn: '', category: '',
  publisher: '', publishDate: '', description: '', totalQuantity: 1, availableQuantity: 1
})

// 图书表单校验规则
const bookRules = {
  title: [{ required: true, message: '请输入书名', trigger: 'blur' }],
  author: [{ required: true, message: '请输入作者', trigger: 'blur' }],
}

// ========== 读者管理相关状态 ==========
const readerKeyword = ref('')       // 读者搜索关键词
const readers = ref([])             // 读者列表数据
const readerLoading = ref(false)    // 表格加载状态
const readerPage = ref(1)
const readerPageSize = ref(10)
const readerTotal = ref(0)

// 读者对话框状态
const readerDialogVisible = ref(false)
const isReaderEdit = ref(false)
const readerSaveLoading = ref(false)
const readerFormRef = ref(null)
const readerForm = reactive({ id: null, name: '', password: '', phone: '', role: 'user' })
const readerRules = {
  name: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }],
}

// ========== 借阅管理相关状态 ==========
const borrowRecords = ref([])       // 借阅记录列表
const borrowLoading = ref(false)    // 表格加载状态
const borrowPage = ref(1)
const borrowPageSize = ref(10)
const borrowTotal = ref(0)

// 借阅对话框状态
const borrowDialogVisible = ref(false)
const isBorrowEdit = ref(false)
const borrowSaveLoading = ref(false)
const allReaders = ref([])          // 所有读者列表（用于下拉选择）
const allBooks = ref([])            // 所有图书列表（用于下拉选择）
const borrowFormRef = ref(null)
const borrowForm = reactive({
  id: null, userId: null, bookId: null,
  borrowDate: '', dueDate: '', returnDate: '', status: 'BORROWED'
})
const borrowRules = {
  // trigger: 'change' 在下拉值改变时校验
  userId: [{ required: true, message: '请选择读者', trigger: 'change' }],
  bookId: [{ required: true, message: '请选择图书', trigger: 'change' }],
}

// ================================================================
//                          图书管理 方法
// ================================================================

/** 加载图书列表（分页） */
async function loadBooks() {
  bookLoading.value = true  // 显示加载动画
  try {
    // 构造查询参数
    const params = { pageNum: bookPage.value, pageSize: bookPageSize.value }
    if (searchKeyword.value) params.keyword = searchKeyword.value
    if (searchCategory.value) params.category = searchCategory.value
    // GET 请求，params 自动拼接到 URL 后面
    const res = await axios.get('/api/books', { params })
    books.value = res.data.data.records || []
    bookTotal.value = res.data.data.total || 0
  } catch (e) { ElMessage.error('加载图书失败') }
  bookLoading.value = false  // 隐藏加载动画
}

/** 加载所有图书分类（用于分类下拉筛选） */
async function loadCategories() {
  try { const res = await axios.get('/api/books/categories'); categories.value = res.data.data || [] } catch (e) {}
}

/** 搜索：重置到第1页再加载 */
function searchBooks() { bookPage.value = 1; loadBooks() }

/** 打开新增图书对话框 */
function showAddBookDialog() {
  isEdit.value = false  // 标记为新增模式
  // Object.assign: 将一个对象的属性复制到另一个对象，用于重置表单
  Object.assign(bookForm, { id: null, title: '', author: '', isbn: '', category: '', publisher: '', publishDate: '', description: '', totalQuantity: 1, availableQuantity: 1 })
  bookDialogVisible.value = true  // 打开对话框
}

/** 打开编辑图书对话框 */
function showEditBookDialog(book) {
  isEdit.value = true  // 标记为编辑模式
  // 将选中图书的数据填充到表单中，|| '' 防止 null 值
  Object.assign(bookForm, { id: book.id, title: book.title, author: book.author, isbn: book.isbn || '', category: book.category || '', publisher: book.publisher || '', publishDate: book.publishDate || '', description: book.description || '', totalQuantity: book.totalQuantity, availableQuantity: book.availableQuantity })
  bookDialogVisible.value = true
}

/** 保存图书（新增/编辑） */
async function saveBook() {
  // 表单校验：validate() 返回 Promise，校验失败用 .catch(() => false) 兜底
  const valid = await bookFormRef.value.validate().catch(() => false)
  if (!valid) return  // 校验不通过，直接返回

  saveLoading.value = true
  try {
    // 根据 isEdit 决定调用新增还是编辑接口
    // isEdit 为 true → PUT 修改；为 false → POST 新增
    const res = isEdit.value
      ? await axios.put('/api/books/' + bookForm.id, bookForm)
      : await axios.post('/api/books', bookForm)
    // 后端统一返回 { code: 0 成功 }
    if (res.data.code === 0) {
      ElMessage.success(res.data.msg)
      bookDialogVisible.value = false  // 关闭对话框
      loadBooks()    // 刷新图书列表
      loadCategories()  // 刷新分类（可能新增了分类）
    } else {
      ElMessage.error(res.data.msg)
    }
  } catch (e) { ElMessage.error('操作失败') }
  saveLoading.value = false
}

/** 删除图书 */
async function deleteBook(id) {
  try {
    const res = await axios.delete('/api/books/' + id)
    if (res.data.code === 0) { ElMessage.success('删除成功'); loadBooks(); loadCategories() }
    else ElMessage.error(res.data.msg)
  } catch (e) { ElMessage.error('删除失败') }
}

// ================================================================
//                          读者管理 方法
// ================================================================

/** 加载读者列表（分页，支持关键词搜索） */
async function loadReaders() {
  readerLoading.value = true
  try {
    const params = { pageNum: readerPage.value, pageSize: readerPageSize.value }
    if (readerKeyword.value) params.keyword = readerKeyword.value
    const res = await axios.get('/api/readers', { params })
    readers.value = res.data.data.records || []
    readerTotal.value = res.data.data.total || 0
  } catch (e) { ElMessage.error('加载读者失败') }
  readerLoading.value = false
}

function searchReaders() { readerPage.value = 1; loadReaders() }

function showAddReaderDialog() {
  isReaderEdit.value = false
  Object.assign(readerForm, { id: null, name: '', password: '', phone: '', role: 'user' })
  readerDialogVisible.value = true
}

function showEditReaderDialog(row) {
  isReaderEdit.value = true
  // 编辑时密码字段留空（不显示原密码）
  Object.assign(readerForm, { id: row.id, name: row.name, password: '', phone: row.phone || '', role: row.role })
  readerDialogVisible.value = true
}

async function saveReader() {
  const valid = await readerFormRef.value.validate().catch(() => false)
  if (!valid) return
  readerSaveLoading.value = true
  try {
    const data = { name: readerForm.name, password: readerForm.password, phone: readerForm.phone, role: readerForm.role }
    const res = isReaderEdit.value
      ? await axios.put('/api/readers/' + readerForm.id, data)
      : await axios.post('/api/readers/register', data)
    if (res.data.code === 0) {
      ElMessage.success(res.data.msg)
      readerDialogVisible.value = false
      loadReaders()
    } else {
      ElMessage.error(res.data.msg)
    }
  } catch (e) { ElMessage.error('操作失败') }
  readerSaveLoading.value = false
}

async function deleteReader(id) {
  try {
    const res = await axios.delete('/api/readers/' + id)
    if (res.data.code === 0) { ElMessage.success('删除成功'); loadReaders() }
    else ElMessage.error(res.data.msg)
  } catch (e) { ElMessage.error('删除失败') }
}

// ================================================================
//                        借阅记录管理 方法
// ================================================================

/** 加载所有借阅记录（分页） */
async function loadBorrowRecords() {
  borrowLoading.value = true
  try {
    const res = await axios.get('/api/borrows/all', { params: { pageNum: borrowPage.value, pageSize: borrowPageSize.value } })
    borrowRecords.value = res.data.data.records || []
    borrowTotal.value = res.data.data.total || 0
  } catch (e) { ElMessage.error('加载借阅记录失败') }
  borrowLoading.value = false
}

/** 加载全部读者列表（用于借阅对话框中的读者下拉选择，pageSize 999 一次查出全部） */
async function loadAllReadersForSelect() {
  try {
    const res = await axios.get('/api/readers', { params: { pageSize: 999 } })
    allReaders.value = res.data.data.records || []
  } catch (e) {}
}

/** 加载全部图书列表（用于借阅对话框中的图书下拉选择） */
async function loadAllBooksForSelect() {
  try {
    const res = await axios.get('/api/books', { params: { pageSize: 999 } })
    allBooks.value = res.data.data.records || []
  } catch (e) {}
}

function showAddBorrowDialog() {
  isBorrowEdit.value = false
  Object.assign(borrowForm, { id: null, userId: null, bookId: null, borrowDate: '', dueDate: '', returnDate: '', status: 'BORROWED' })
  loadAllReadersForSelect()  // 预加载读者下拉数据
  loadAllBooksForSelect()    // 预加载图书下拉数据
  borrowDialogVisible.value = true
}

function showEditBorrowDialog(row) {
  isBorrowEdit.value = true
  Object.assign(borrowForm, {
    id: row.id, userId: row.userId, bookId: row.bookId,
    borrowDate: row.borrowDate, dueDate: row.dueDate,
    returnDate: row.returnDate || '', status: row.status
  })
  loadAllReadersForSelect()
  loadAllBooksForSelect()
  borrowDialogVisible.value = true
}

async function saveBorrow() {
  const valid = await borrowFormRef.value.validate().catch(() => false)
  if (!valid) return
  borrowSaveLoading.value = true
  try {
    const data = {
      userId: borrowForm.userId, bookId: borrowForm.bookId,
      borrowDate: borrowForm.borrowDate, dueDate: borrowForm.dueDate,
      returnDate: borrowForm.returnDate || null, status: borrowForm.status
    }
    const res = isBorrowEdit.value
      ? await axios.put('/api/borrows/' + borrowForm.id, data)
      : await axios.post('/api/borrows', data)
    if (res.data.code === 0) {
      ElMessage.success(res.data.msg)
      borrowDialogVisible.value = false
      loadBorrowRecords()
    } else {
      ElMessage.error(res.data.msg)
    }
  } catch (e) { ElMessage.error('操作失败') }
  borrowSaveLoading.value = false
}

async function deleteBorrow(id) {
  try {
    const res = await axios.delete('/api/borrows/' + id)
    if (res.data.code === 0) { ElMessage.success('删除成功'); loadBorrowRecords() }
    else ElMessage.error(res.data.msg)
  } catch (e) { ElMessage.error('删除失败') }
}

// ================================================================
//                          通用方法
// ================================================================

/** 标签页切换时懒加载数据（点哪个标签页才加载哪个的数据） */
function handleTabChange(name) {
  if (name === 'readers') loadReaders()
  else if (name === 'borrows') loadBorrowRecords()
}

/** 退出登录 */
function logout() {
  sessionStorage.clear()   // 清空浏览器中存储的用户信息
  router.push('/')         // 跳转到登录页
}

// 页面加载时自动执行（仅加载图书相关，读者和借阅标签页点击时才懒加载）
onMounted(() => {
  loadBooks()
  loadCategories()
})
</script>

<style scoped>
/* 顶部导航栏（深色） */
.header { background: #303133; color: #fff; padding: 15px 30px; display: flex; justify-content: space-between; align-items: center; }
.header h3 { font-size: 18px; }
/* 用户信息区：水平排列，间距 15px */
.header .user-info { display: flex; align-items: center; gap: 15px; }
/* 内容区域：最大宽度 1300px，居中 */
.container { max-width: 1300px; margin: 20px auto; padding: 0 20px; }
body { background: #f5f7fa; font-family: 'Helvetica Neue', Helvetica, 'PingFang SC', 'Microsoft YaHei', sans-serif; }
</style>