<template>
    <el-container class="el-container-operation">
      <el-aside width="200px" style="background-color: rgb(238, 241, 246)" class="el-aside-operation">
        <Aside></Aside>
      </el-aside>
      <el-container style="width: calc(100% - 200px);" class="el-container-operation">
      <el-header class="el-header-operation">
        <Header name=""/>
      </el-header>
      <el-main class="el-main-operation">
        <router-view/>
      </el-main>
      </el-container>
    </el-container>
</template>
<script>
import Aside from '@/components/Aside.vue'
import Header from '@/components/Header.vue'
export default {
  name:"Manage",
  components:{
    Aside,
    Header
  },
}
</script>

