// An highlighted block
import Vue from 'vue'
import ElementUI, { InfiniteScroll } from 'element-ui';// 添加
import 'element-ui/lib/theme-chalk/index.css'; // 添加
import App from './App.vue'
import router from './router'
import './style/gloable.css'
import './style/blog.css'
import request from '@/utils/request'//添加
import dealSqlConfirm from "@/utils/dealSql";
import dropdown from "@/utils/dropdown";
//全局注册
import mavonEditor from 'mavon-editor'
import 'mavon-editor/dist/css/index.css'	//解决编辑器的功能显示问题

Vue.config.productionTip = false
Vue.use(mavonEditor)
Vue.use(ElementUI);// 添加
Vue.use(InfiniteScroll);// 添加
Vue.prototype.request=request // 添加

new Vue({
  router,
  render: h => h(App)
}).$mount('#app')

Vue.directive("hover-dropdown", dropdown);
