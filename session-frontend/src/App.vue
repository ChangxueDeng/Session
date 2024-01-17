<template>
<div style="width: 100vw; height: 100vh; overflow: hidden; display: flex">
  <div style="flex: 1">
    <el-image style="width: 100%; height: 100%" src="https://cdn.pixabay.com/photo/2014/07/28/20/39/sunset-404072_1280.jpg"></el-image>
  </div>
  <div style="width: 350px; background-color: white;text-align: center">
    <div style="margin: 20px">
      <div>
        <div style="font-size: 25px; margin-top: 200px; font-weight: bold">登录</div>
        <div style="font-size: 14px; color: gray">进入系统前请先输入用户名和密码进行登录</div>
      </div>
      <div style="margin-top: 50px">
        <el-input type="text" placeholder="用户名或邮箱登录" :prefix-icon="User"></el-input>
        <el-input type="password" placeholder="密码" :prefix-icon="Lock" style="margin-top: 10px"></el-input>
      </div>
      <div style="margin-top: 10px">
        <el-row>
          <el-col :span="12" style="text-align: left">
            <el-checkbox label="记住我" v-model="check1" size="large"></el-checkbox>
          </el-col>
          <el-col :span="12" style="text-align: right; margin-top: 5px">
            <el-link>忘记密码？</el-link>
          </el-col>
        </el-row>
      </div>
      <div>
        <el-button type="success" plain style="width: 270px;margin-top: 40px">登录</el-button>
      </div>
      <el-divider>
        <span style="color: gray">没有账号</span>
      </el-divider>
    </div>
    <div>
      <el-button type="primary" plain style="width: 270px">注册账号</el-button>
    </div>
  </div>
</div>
</template>


<script>
import {User, Lock} from '@element-plus/icons-vue'
export default{
  data(){
    return{
      User,
      Lock
    }
  }
}
</script>

<style>
body{
  margin: 0;
}
</style>
