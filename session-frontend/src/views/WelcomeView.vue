<template>
  <div style="width: 100vw; height: 100vh; overflow: hidden; display: flex">
    <div style="flex: 1">
      <el-image style="width: 100%; height: 100%" src="https://cdn.pixabay.com/photo/2014/07/28/20/39/sunset-404072_1280.jpg"></el-image>
    </div>
    <div class="welcome-title">
      <div style="font-weight: bold; font-size: 20px">进行测试</div>
      <div style="margin-top: 10px">测试二</div>
    </div>
    <div style="width: 350px; background-color: white;">
      <router-view v-slot="{ Component}">
        <transition name="el-zoom-in-center" mode="out-in" >
              <component :is="Component" />
        </transition>
      </router-view>
    </div>
  </div>
</template>


<script setup>

</script>

<style>
body{
  margin: 0;
}
.welcome-title{
  position: absolute;
  bottom: 30px;
  left: 30px;
  color: beige;
  text-shadow: 0 0 10px black;
}
</style>
