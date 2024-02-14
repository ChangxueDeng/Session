import { createRouter, createWebHashHistory } from 'vue-router'
import {useStore} from "@/stores";

const routes = [
  {
    path:'/',
    name:'welcome',
    //懒加载
    component:() => import('@/views/WelcomeView.vue'),
    children:[
      {
        path:'',
        name:'welcome-login',
        component:() => import('@/components/welcome/LoginPage.vue')
      },
      {
        path: '/register',
        name: 'welcome-register',
        component:() => import('@/components/welcome/RegisterPage.vue')
      },
      {
        path:'/forget',
        name: 'forget',
        component:()=>import('@/components/welcome/ForgetPage.vue')
      }
    ]
  },
  {
    path: '/index',
    name: 'index',
    component:() => import('@/views/IndexView.vue')
  }
]

const router = createRouter({
  history: createWebHashHistory(),
  routes,
})

router.beforeEach((to, from, next)=>{
  const store = useStore();
  if(store.auth.user !== null && to.name.startsWith('Welcome-')){
    next('/index')
  }else if(store.auth.user === null && to.fullPath.startsWith('/index')){
    next('/')
  }else if(to.matched.length === 0){
    next('/index')
  } else {
    next()
  }
})
export default router
