<template>
  <div class="login-page">
    <div class="login-box">
      <h2>图书馆管理系统</h2>

      <!--
        el-tabs: Element Plus 标签页组件
        v-model="activeTab": 双向绑定，activeTab 变化时自动切换标签页
        stretch: 标签页宽度拉伸均分
      -->
      <el-tabs v-model="activeTab" stretch>

        <!-- ========== 登录标签页 ========== -->
        <el-tab-pane label="登录" name="login">
          <!--
            el-form: 表单组件
            :model="loginForm": 绑定表单数据对象，冒号表示动态绑定 JS 变量
            :rules="loginRules": 绑定校验规则
            ref="loginFormRef": 给组件起引用名，JS 中通过 loginFormRef 访问
            label-position="top": 标签显示在输入框上方
          -->
          <el-form :model="loginForm" :rules="loginRules" ref="loginFormRef" label-position="top">
            <!-- prop="name" 关联 rules 中的校验规则 -->
            <el-form-item label="用户名" prop="name">
              <el-input v-model="loginForm.name" placeholder="请输入用户名" />
            </el-form-item>
            <el-form-item label="密码" prop="password">
              <!-- type="password" 密码输入框，show-password 可切换明文/密文 -->
              <el-input v-model="loginForm.password" type="password" placeholder="请输入密码" show-password />
            </el-form-item>
            <el-form-item>
              <!-- @click: Vue 事件绑定，点击时调用 doLogin 函数 -->
              <!-- :loading="loading": 按钮加载状态，请求中显示转圈 -->
              <el-button type="primary" @click="doLogin" :loading="loading" style="width:100%">登 录</el-button>
            </el-form-item>
          </el-form>
        </el-tab-pane>

        <!-- ========== 注册标签页 ========== -->
        <el-tab-pane label="注册" name="register">
          <el-form :model="registerForm" :rules="registerRules" ref="registerFormRef" label-position="top">
            <el-form-item label="用户名" prop="name">
              <el-input v-model="registerForm.name" placeholder="请输入用户名" />
            </el-form-item>
            <el-form-item label="密码" prop="password">
              <el-input v-model="registerForm.password" type="password" placeholder="请输入密码" show-password />
            </el-form-item>
            <el-form-item label="联系电话" prop="phone">
              <el-input v-model="registerForm.phone" placeholder="请输入联系电话" />
            </el-form-item>
            <el-form-item>
              <el-button type="success" @click="doRegister" :loading="regLoading" style="width:100%">注 册</el-button>
            </el-form-item>
          </el-form>
        </el-tab-pane>

      </el-tabs>
    </div>
  </div>
</template>

<script setup>
/*
 * <script setup>: Vue 3 组合式 API 语法糖
 * 无需手动 return，顶层变量自动暴露给模板
 */
// 导入 Vue 组合式 API 工具函数
import { ref, reactive } from 'vue'
// useRouter: Vue Router 提供的路由跳转钩子
import { useRouter } from 'vue-router'
// ElMessage: Element Plus 消息提示（成功/错误弹窗）
import { ElMessage } from 'element-plus'
// axios: HTTP 请求库，用于调用后端 API
import axios from 'axios'

const router = useRouter()

// ========== 响应式数据定义 ==========

// ref(): 将基本类型包装为响应式变量，值变化时自动更新模板
//   修改时需要用 .value，模板中自动解包无需 .value
const activeTab = ref('login')        // 当前选中的标签页
const loading = ref(false)            // 登录按钮加载中
const regLoading = ref(false)         // 注册按钮加载中
const loginFormRef = ref(null)        // 登录表单组件引用（用于调用 validate）
const registerFormRef = ref(null)     // 注册表单组件引用

// reactive(): 将对象包装为响应式对象，修改属性时自动更新模板
//   与 ref 不同，访问属性时不需要 .value
const loginForm = reactive({
  name: '',       // 登录用户名
  password: ''    // 登录密码
})
const registerForm = reactive({
  name: '',       // 注册用户名
  password: '',   // 注册密码
  phone: ''       // 注册联系电话
})

// 表单校验规则：required 必填，message 不通过时的提示，trigger 触发时机（blur 失去焦点时）
const loginRules = {
  name: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
}
const registerRules = {
  name: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
}

// ========== 登录逻辑 ==========
async function doLogin() {
  // 调用 Element Plus 表单的 validate() 方法校验表单，失败返回 false
  const valid = await loginFormRef.value.validate().catch(() => false)
  if (!valid) return

  loading.value = true   // 显示按钮加载动画
  try {
    // axios.post(url, data): 发送 POST 请求，参数自动转 JSON
    const res = await axios.post('/api/readers/login', loginForm)
    // 后端统一返回 { code: 0 成功, code: -1 失败, msg: 提示信息, data: 用户数据 }
    if (res.data.code === 200) {
      const user = res.data.data
      // sessionStorage: 浏览器标签页级存储，关闭标签页后清除
      // 存储用户信息供其他页面读取
      sessionStorage.setItem('user', JSON.stringify(user))
      ElMessage.success('登录成功')
      // router.push(path): 编程式导航，跳转到指定路由
      // 根据角色跳转到管理员或读者页面
      router.push(user.role === 'admin' ? '/admin' : '/reader')
    } else {
      ElMessage.error(res.data.msg || '登录失败')
    }
  } catch (e) {
    ElMessage.error('网络错误')
  }
  loading.value = false  // 无论成功失败都关闭加载动画
}

// ========== 注册逻辑 ==========
async function doRegister() {
  const valid = await registerFormRef.value.validate().catch(() => false)
  if (!valid) return

  regLoading.value = true
  try {
    const res = await axios.post('/api/readers/register', registerForm)
    if (res.data.code === 200) {
      ElMessage.success('注册成功，请登录')
      activeTab.value = 'login'  // 切换到登录标签页
      // 清空注册表单
      registerForm.name = ''
      registerForm.password = ''
      registerForm.phone = ''
    } else {
      ElMessage.error(res.data.msg || '注册失败')
    }
  } catch (e) {
    ElMessage.error('网络错误')
  }
  regLoading.value = false
}
</script>

<style scoped>
/*
 * scoped: 样式只作用于当前组件，不会影响其他组件
 */
/* 登录页全屏居中背景 */
.login-page {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
}
/* 登录卡片 */
.login-box {
  background: #fff;
  border-radius: 12px;
  padding: 40px;
  width: 420px;
  box-shadow: 0 20px 60px rgba(0,0,0,0.3);
}
.login-box h2 {
  text-align: center;
  margin-bottom: 30px;
  color: #303133;
  font-size: 24px;
}
</style>