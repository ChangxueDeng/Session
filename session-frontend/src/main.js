import { createApp } from 'vue'
//import {createPinia} from "pinia";
import App from './App.vue'
import router from './router'
import 'element-plus/dist/index.css'
import axios from "axios";
axios.defaults.baseURL = 'http://localhost:8081'
createApp(App).use(router).mount('#app')
