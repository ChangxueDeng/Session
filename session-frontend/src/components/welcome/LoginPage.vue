<script setup>
  import {User,Lock} from '@element-plus/icons-vue'
  import {reactive} from "vue";
  import {ElMessage} from "element-plus";
  import {post} from "@/net"
  import router from "@/router";
  const form = reactive({
    username:'',
    password:'',
    remember:false
  })
  const login = () =>{
    if(!form.username || !form.password){
      ElMessage.warning('请填写用户名和密码')
    }else {
      post("/api/auth/login",{
        username: form.username,
        password: form.password,
        remember: form.remember
      }, (message) =>{
        ElMessage.success(message)
        router.push('/index')
      })
    }
  }
</script>

<template>
  <div style="text-align: center">
    <div style="margin: 20px">
      <div>
        <div style="font-size: 25px; margin-top: 200px; font-weight: bold">登录</div>
        <div style="font-size: 14px; color: gray">进入系统前请先输入用户名和密码进行登录</div>
      </div>
      <div style="margin-top: 50px">
        <el-input v-model="form.username" type="text" placeholder="用户名或邮箱登录" :prefix-icon="User"></el-input>
        <el-input v-model="form.password" type="password" placeholder="密码" :prefix-icon="Lock" style="margin-top: 10px"></el-input>
      </div>
      <div style="margin-top: 10px">
        <el-row>
          <el-col :span="12" style="text-align: left">
            <el-checkbox label="记住我" v-model="form.remember" size="large"></el-checkbox>
          </el-col>
          <el-col :span="12" style="text-align: right; margin-top: 5px">
            <el-link>忘记密码？</el-link>
          </el-col>
        </el-row>
      </div>
      <div>
        <el-button @click="login()" type="success" plain style="width: 270px;margin-top: 40px">登录</el-button>
      </div>
      <el-divider>
        <span style="color: gray">没有账号</span>
      </el-divider>
    </div>
    <div>
      <el-button type="primary" plain style="width: 270px">注册账号</el-button>
    </div>
  </div>
</template>

<style scoped>

</style>