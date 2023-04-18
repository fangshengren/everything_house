// An highlighted block
import Vue from 'vue'
import ElementUI from 'element-ui';// 添加
import 'element-ui/lib/theme-chalk/index.css'; // 添加
import App from './App.vue'
import router from './router'
import './style/gloable.css'
import request from '@/utils/request'//添加
import dealSqlConfirm from "@/utils/dealSql";

Vue.config.productionTip = false
Vue.use(ElementUI);// 添加
Vue.prototype.request=request // 添加

new Vue({
  router,
  render: h => h(App)
}).$mount('#app')
