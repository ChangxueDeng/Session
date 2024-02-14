<script setup>
import {get} from "@/net"
import {ElMessage} from "element-plus";
import router from "@/router";
import {useStore} from "@/stores";
const logout = () =>{
  get('/api/auth/logout', (message) => {
    ElMessage.success(message)
    const store = useStore()
    store.auth.user = null;
    router.push('/')
  })
}
</script>

<template>
  <div>欢迎进入</div>
  <div>
    <el-button @click="logout()" type="primary" round>退出登录</el-button>
  </div>
</template>

<style scoped>

</style>