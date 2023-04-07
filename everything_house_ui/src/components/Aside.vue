<template>
  <el-menu :default-openeds="opens" style="min-height:100%; overflow-x:hidden"
           background-color=rgb(48,65,86)
           text-color=#ccc
           active-text-color=red
           router=""
  >
    <div style="height:60px; line-height:60px; text-align:center">
      <img src="../assets/logo.png" style="width:20px;position:relative;top:5px;margin-right:5px"/>
      <b style="color:white">后台管理系统</b>
    </div>
    <div v-for="item in menus" :key="item.id">
      <div v-if="item.path">
        <el-menu-item :index="item.path">
          <i :class="item.icon"></i>
          <span slot="title">{{item.name}}</span>
        </el-menu-item>
      </div>
      <div v-else>
        <el-submenu :index="item.id+'' ">
          <template slot="title">
            <i :class="item.icon"></i>
            <span slot="title">{{item.name}}</span>
          </template>
          <div v-for="subItem in item.children" :key="subItem.id">
            <el-menu-item :index="subItem.path">
              <i :class="subItem.icon"></i>
              <span slot="title">{{subItem.name}}</span>
            </el-menu-item>
          </div>
        </el-submenu>
      </div>
    </div>
  </el-menu>
</template>

<script>
export default {
  name: "Aside",
  props:{

  },
  data(){
    return{
      menus:localStorage.getItem("menus")?JSON.parse(localStorage.getItem("menus")):[],
      opens:localStorage.getItem("menus")?JSON.parse(localStorage.getItem("menus")).map(v=>v.id+''):[],
    }
  },
  methods:{
    showmst(){
      console.log(this.opens);
    }
  }
}
</script>

<style scoped>
</style>
