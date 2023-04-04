import Vue from 'vue'
import VueRouter from 'vue-router'
import home from "@/views/Home.vue";
import User from "@/views/User.vue";
import Home from "@/views/Home.vue";
import Manage from "@/views/Manage.vue";

Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    name: 'home',
    component: Manage,
    children:[
      {
        path: 'user',
        name: 'User',
        component: User,
      },
      {
        path: 'about',
        name: 'about',
        // route level code-splitting
        // this generates a separate chunk (about.[hash].js) for this route
        // which is lazy-loaded when the route is visited.
        component: () => import(/* webpackChunkName: "about" */ '../views/User.vue')
      }
    ]
  }
]


const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

export default router
