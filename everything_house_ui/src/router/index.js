import Vue from 'vue'
import VueRouter from 'vue-router'
import Manage from '../views/Manage.vue'
import User from '../views/User.vue'
import Login from '../views/Login.vue'
import Home from "@/views/Home.vue";
import Menu from "../views/Menu.vue"

Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    name: 'Manage',
    redirect: '/login',
    component: Manage,
    children:[
      {
        path: 'user',
        name: 'User',
        component: () => import(/* webpackChunkName: "about" */ '../views/User.vue')
      },
      {
        path: 'home',
        name: 'Home',
        // route level code-splitting
        // this generates a separate chunk (about.[hash].js) for this route
        // which is lazy-loaded when the route is visited.
        component: Home,
      },
      {
        path: 'menu',
        name: 'Menu',
        // route level code-splitting
        // this generates a separate chunk (about.[hash].js) for this route
        // which is lazy-loaded when the route is visited.
        component: Menu,
      },
    ]
  },
  {
    path: '/login',
    name: 'Login',
    component: Login
  }
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

export default router
