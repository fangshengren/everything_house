import Vue from 'vue'
import VueRouter from 'vue-router'
import Login from '../views/Login.vue'
import Home from '../views/Home.vue'
import Register from '../views/Register.vue'
import RealHome from "@/views/RealHome.vue";
import ForgetPassword from "@/views/ForgetPassword.vue";
import BlogManage from "@/views/blog/BlogManage.vue";

Vue.use(VueRouter)

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: Login
  },
  {
    path: '/realHome',
    name: 'RealHome',
    component: RealHome
  },
  {
    path: '/register',
    name: 'Register',
    component: Register
  },
  {
    path: '/forgetPassword',
    name: 'ForgetPassword',
    component: ForgetPassword,
  },
  {
    path: '/blog',
    name: 'BlogManage',
    component: BlogManage,
    redirect: '/blog/homePage',
    children: [
      {
        path:'homePage',
        name:'BlogHomePage',
        component:()=>import('../views/blog/BlogHomePage.vue'),
      },{
        path:'write',
        name:'BlogWrite',
        component:()=>import('../views/blog/BlogWrite.vue'),
      }
    ]
  }
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

// 提供一个重置路由的方法
export const resetRouter=()=>{
  router.matcher=new VueRouter({
    mode: 'history',
    base: process.env.BASE_URL,
    routes
  })
}
//刷新页面会重置路由
export const setRoutes=()=>{
  const storeMenus=localStorage.getItem("menus");
  if(storeMenus){
    //拼装动态路由
    const manageRoute={ path: '/', name:'Manage', component: () => import('../views/Manage.vue'),redirect: '/login', children: []}
    const menus=JSON.parse(storeMenus)
    menus.forEach(item=>{
      if(item.path){//当且仅当path不为空的时候才去设置路由
        let itemMenu={ path: item.path.replace("/",""),name: item.name,component: () => import('../views/'+item.pagePath+'.vue')}
        manageRoute.children.push(itemMenu)
      }else if(item.children.length){
        item.children.forEach(item=>{
          if(item.path){
            let itemMenu={ path: item.path.replace("/",""),name: item.name,component: () => import('../views/'+item.pagePath+'.vue')}
            manageRoute.children.push(itemMenu)
          }
        })
      }
    })
    //获取当前的路由对象名称数组
    const currentRouteNames=router.getRoutes().map(v=>v.name)
    if(!currentRouteNames.includes('Manage')){
      //动态加载到想在的路由对象
      router.addRoute(manageRoute)
    }
  }
}
setRoutes()
export default router

