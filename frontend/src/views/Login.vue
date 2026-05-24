<template>
  <div class="login-page">
    <div class="login-box">
      <h2>📚 图书馆管理系统</h2>
      <el-tabs v-model="activeTab" stretch>
        <el-tab-pane label="登录" name="login">
          <el-form :model="loginForm" :rules="loginRules" ref="loginFormRef" label-position="top">
            <el-form-item label="用户名" prop="name">
              <el-input v-model="loginForm.name" placeholder="请输入用户名" />
            </el-form-item>
            <el-form-item label="密码" prop="password">
              <el-input v-model="loginForm.password" type="password" placeholder="请输入密码" show-password />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="doLogin" :loading="loading" style="width:100%">登 录</el-button>
            </el-form-item>
          </el-form>
        </el-tab-pane>
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
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import axios from 'axios'

const router = useRouter()
const activeTab = ref('login')
const loading = ref(false)
const regLoading = ref(false)
const loginFormRef = ref(null)
const registerFormRef = ref(null)

const loginForm = reactive({ name: '', password: '' })
const registerForm = reactive({ name: '', password: '', phone: '' })

const loginRules = {
  name: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
}
const registerRules = {
  name: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
}

async function doLogin() {
  const valid = await loginFormRef.value.validate().catch(() => false)
  if (!valid) return
  loading.value = true
  try {
    const res = await axios.post('/api/readers/login', loginForm)
    if (res.data.code === 0) {
      const user = res.data.data
      sessionStorage.setItem('user', JSON.stringify(user))
      ElMessage.success('登录成功')
      router.push(user.role === 'admin' ? '/admin' : '/reader')
    } else {
      ElMessage.error(res.data.msg || '登录失败')
    }
  } catch (e) {
    ElMessage.error('网络错误')
  }
  loading.value = false
}

async function doRegister() {
  const valid = await registerFormRef.value.validate().catch(() => false)
  if (!valid) return
  regLoading.value = true
  try {
    const res = await axios.post('/api/readers/register', registerForm)
    if (res.data.code === 0) {
      ElMessage.success('注册成功，请登录')
      activeTab.value = 'login'
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
.login-page {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  min-height: 100vh; display: flex; align-items: center; justify-content: center;
}
.login-box {
  background: #fff; border-radius: 12px; padding: 40px; width: 420px;
  box-shadow: 0 20px 60px rgba(0,0,0,0.3);
}
.login-box h2 { text-align: center; margin-bottom: 30px; color: #303133; font-size: 24px; }
</style>
