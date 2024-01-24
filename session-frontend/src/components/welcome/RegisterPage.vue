<script setup>
import {User,Lock,Message,EditPen} from "@element-plus/icons-vue";
import router from "@/router";
import {reactive} from "vue";
const email_data = /\w[-\w.+]*@([A-Za-z0-9][-A-Za-z0-9]+\.)+[A-Za-z]{2,14}/
function check_email(){
  if(!email_data.test(form.email))
    alert("邮箱不符合格式请重新输入")
}
const form = reactive({
  username:'',
  password:'',
  password_repeat:'',
  email:'',
  email_code:'',
})
</script>

<template>
  <div>
    <div>
      <div style="margin: 20px; text-align: center">
        <div style="font-size: 25px; margin-top: 200px; font-weight: bold">注册</div>
        <div style="font-size: 14px; color: gray">欢迎进行用户注册，请填写下列内容</div>
      </div>
      <div style=" margin:10px">
        <el-input v-model="form.username" type="text" placeholder="用户名" style="margin-top: 10px" :prefix-icon="User"></el-input>
        <el-input v-model="form.password" type="password" placeholder="密码" style="margin-top: 10px; " :prefix-icon="Lock"></el-input>
        <el-input v-model="form.password_repeat" type="password" placeholder="重复密码" style="margin-top: 10px"></el-input>
      </div>
    </div>
    <div style="margin: 10px; ">
      <el-input v-model="form.email" type="text" placeholder="电子邮件地址" :prefix-icon="Message" @blur="check_email()"></el-input>
      <div style="width: 100%;">
        <el-row :gutter="10" style="margin-top: 10px">
          <el-col :span="16">
            <el-input v-model="form.email_code" type="text" :prefix-icon="EditPen" placeholder="验证码"></el-input>
          </el-col>
          <el-col :span="7">
            <el-button type="success">获取验证码</el-button>
          </el-col>
        </el-row>
      </div>
      <div style="margin-top: 50px; text-align: center" >
        <el-button type="primary" plain style="width: 270px;">立即注册</el-button>
      </div>
      <div style="margin-top: 20px;text-align: center; font-size: 15px">
        <span style="color: gray">已有账号?</span>
        <el-link @click="router.push('/')" style="translate: 0 -2px; font-size: 15px" type="success">回到登录界面</el-link>
      </div>
    </div>
  </div>


</template>

<style scoped>

</style>