<script setup>
import {User,Lock,Message,EditPen} from "@element-plus/icons-vue";
import router from "@/router";
import {reactive, ref} from "vue";
import {ElMessage} from "element-plus";
import {post} from "@/net";

const form = reactive({
  username:'',
  password:'',
  password_repeat:'',
  email:'',
  email_code:'',
})
const isEmailValid = ref(false)
const onValid = (prop, isValid) =>{
  if(prop === 'email'){
    isEmailValid.value = isValid
  }
}
const validateUsername=(rule,value,callback)=>{
  if (value ==='')
    callback(new Error('请输入用户名'))
  else if(!/[a-zA-Z0-9\u4e00-\u9fa5]+$/.test(value)){
    callback(new Error('用户名不能包含特殊字符，只能是中文/英文'))}
  else
    callback()
  }
// const validateEmail = (rule, value, callback)=>{
//   if(value === '')
//     callback(new Error('请输入邮箱'))
//   else if(!/\w[-\w.+]*@([A-Za-z0-9][-A-Za-z0-9]+\.)+[A-Za-z]{2,14}/.test(value))
//     callback(new Error('邮箱格式不合法'))
//   else callback()
// }
const validatePassword = (rule, value, callback) => {
  if(value === '')
    callback(new Error('请输入密码'))
  else if(value !== form.password)
    callback(new Error('两次密码输入不一致'))
  else callback()
}
  const rules = {
    username: [
        {validator:validateUsername,trigger:['blur','change']},
      {min:3,max:5,message:'用户长度在3到5个字符之间',trigger: ['blur', 'change']},

    ],
    password: [
      {required: true, message: '请输入密码', trigger: ['blur', 'change']},
      {min:6,max:16,message:'密码长度在6到16个字符之间', trigger:['blur','change']}
    ],
    password_repeat:[
      {validator:validatePassword, trigger:['blur','change']}
    ],
    email:[
      {required: true, message: '请输入邮件', trigger: ['blur', 'change']},
      {type:'email', message: '请输入合法的邮件地址',trigger:['blur', 'change']}
    ],
    email_code:[
      {required:true, message:'请输入验证码', trigger:'blur'}
    ]
  }
const formRef = ref()
const register = () => {
  formRef.value.validate((isValid) =>{
    if(!isValid) {
      ElMessage.warning('请完整填写信息')
    }
  })
}

const validateEmail = () =>{
  post('api/auth/valid-email', {
    email: form.email
  },(message) => {
    ElMessage.success(message)
  })
}
// const register = () =>{
//   formRef.value.validate((isValid) =>{
//     if(isValid){
//       ElMessage.warning('yes')
//     }else {
//       ElMessage.warning('请完整填写信息')
//     }
//   })
// }
</script>

<template>
  <div>
    <div>
      <div style="margin: 20px; text-align: center">
        <div style="font-size: 25px; margin-top: 200px; font-weight: bold">注册</div>
        <div style="font-size: 14px; color: gray">欢迎进行用户注册，请填写下列内容</div>
      </div>
      <div style=" margin:10px">
        <el-form :rules="rules" :model="form" @validate="onValid" ref="formRef">
          <el-form-item prop="username">
            <el-input v-model="form.username" type="text" placeholder="用户名" style="margin-top: 10px" :prefix-icon="User"></el-input>
          </el-form-item>
          <el-form-item prop="password">
            <el-input v-model="form.password" type="password" placeholder="密码" style="margin-top: 10px; " :prefix-icon="Lock"></el-input>
          </el-form-item>
          <el-form-item prop="password_repeat">
            <el-input v-model="form.password_repeat" type="password" placeholder="重复密码" style="margin-top: 10px" :prefix-icon="Lock"></el-input>
          </el-form-item>
          <el-form-item prop="email">
            <el-input v-model="form.email" type="text" placeholder="电子邮件地址" :prefix-icon="Message"></el-input>
          </el-form-item>
          <el-row :gutter="10" style="margin-top: 10px">
            <el-col :span="16">
              <el-form-item prop="email_code">
                <el-input v-model="form.email_code" type="text" :prefix-icon="EditPen" placeholder="验证码"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="7">
              <el-button :disabled="!isEmailValid" type="success" @click="validateEmail()">获取验证码</el-button>
            </el-col>
          </el-row>
        </el-form>
      </div>
    </div>
    <div style="margin: 10px; ">
      <div style="margin-top: 50px; text-align: center" >
        <el-button type="primary" plain style="width: 270px;" @click="register()">立即注册</el-button>
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