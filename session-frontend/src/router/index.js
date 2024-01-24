import { createRouter, createWebHashHistory } from 'vue-router'

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

export default router
