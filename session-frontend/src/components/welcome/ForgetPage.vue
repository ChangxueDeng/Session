<script setup>
import {reactive, ref} from "vue";
import {post} from "@/net";
import {ElMessage} from "element-plus";
import router from "@/router";


const form = reactive({
  email: '',
  email_code: '',
  password: '',
  password_repeat: ''
})
const validPass = (rule, value, callback) => {
  if (value === '') {
    callback(new Error('输入密码'))
  } else if (value !== form.password) {
    callback(new Error('两次输入的密码不一致，请重试'))
  } else callback()
}
const rules = {
  email: [{required: true, message: '请输入电子邮件地址', trigger: ['blur', 'change']},
    {type: 'email', message: '请输入合法邮件地址', trigger: ['blur', 'change']}],
  email_code: [{required: true, message: '输入验证码', trigger: ['blur', 'change']},],
  password: [{required: true, message: '请输入新密码', trigger: ['blur', 'change']},
    {min: 6, max: 16, message: '密码长度在6-16个字符之间', trigger: ['blur', 'change']}],
  password_repeat: [{validator: validPass, trigger: ['blur', 'change']}]
}

const isEmailValidate = ref(false)
const onValid = (prop, isValid) => {
  if (prop === 'email') {
    isEmailValidate.value = isValid;
  }
}


const steps = ref(0)

const coldTime = ref(0);

const validEmail = () => {
  coldTime.value = 60;
  post('api/auth/valid-reset-email', {
    email: form.email,
  }, (message) => {
    ElMessage.success(message)
    setInterval(() => coldTime.value--, 1000)
  },(message)=>{
    ElMessage.warning(message)
    coldTime.value = 0})
}
const startRef = ref()
const resetRef = ref()
const startReset = () => {
  startRef.value.validate((isValid) => {
    if (!isValid) {
      ElMessage.warning('请完整填写信息')
    } else {
      post('api/auth/start-reset', {
        email: form.email,
        email_code: form.email_code,
      }, (message) => {
        ElMessage.success(message)
        steps.value = 1
      })
    }
  })
}

const resetPassword = () => {
  resetRef.value.validate((isValid) => {
    if (!isValid) {
      ElMessage.warning('请完整填写信息')
    } else {
      post('api/auth/do-reset', {
        password: form.password,
      }, (message) => {
        ElMessage.success(message)
        router.replace('/')
      })
    }
  })
}


</script>

<template>

  <div>
    <div>
      <div style="margin-top: 30px">
        <el-steps :space="200" :active="steps" finish-status="success" align-center>
          <el-step title="验证电子邮件"/>
          <el-step title="重新设置密码"/>
        </el-steps>
      </div>
      <div style="margin: 20px; text-align: center">
        <div style="font-size: 25px; margin-top: 150px; font-weight: bold">重置密码</div>
        <div style="font-size: 14px; color: gray">
          {{ steps === 0 ? '输入对于电子邮件地址' : '填写您的新密码，务必牢记，防止丢失' }}
        </div>
      </div>
    </div>
    <transition name="el-zoom-in-center" mode="out-in" style="position: absolute">
      <div v-if="steps===0" style="width: 330px; margin: 10px;">
        <el-form :rules="rules" :model="form" @validate="onValid" ref="startRef">
          <el-form-item prop="email">
            <el-input type="text" placeholder="电子邮件地址" v-model="form.email"></el-input>
          </el-form-item>
          <el-row>
            <el-col :span="16">
              <el-form-item prop="email_code">
                <el-input type="text" placeholder="验证码" v-model="form.email_code"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item style="margin-left:8px">
                <el-button plain type="primary" :disabled="!isEmailValidate || coldTime > 0" @click="validEmail">
                  {{ coldTime > 0 ? '请稍后' + coldTime + '秒' : '获取验证码' }}
                </el-button>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <div style="text-align: center">
          <el-button style="width: 190px" plain type="success" @click="startReset">开始重置密码</el-button>
        </div>
      </div>
    </transition>
    <transition name="el-zoom-in-center" mode="out-in" style="position: absolute">
      <div v-if="steps===1" style="width: 330px; margin: 10px;">
        <el-form :model="form" :rules="rules" ref="resetRef">
          <el-form-item prop="password">
            <el-input type="text" placeholder="新密码" v-model="form.password"></el-input>
          </el-form-item>
          <el-form-item prop="password_repeat">
            <el-input type="text" placeholder="重复密码" v-model="form.password_repeat"></el-input>
          </el-form-item>
        </el-form>
        <div style="text-align: center">
          <el-button type="success" plain style="width: 180px" @click="resetPassword">确认重置</el-button>
        </div>
      </div>
    </transition>
  </div>
</template>

<style scoped>

</style>