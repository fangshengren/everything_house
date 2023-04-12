<template>
  <div id="home">
    <el-container>
      <el-aside width="200px" style="background-color: rgb(238, 241, 246)">
        <Aside></Aside>
      </el-aside>
      <el-container>
      <el-header>
        <Header name=""/>
      </el-header>
      <el-main>
        <router-view/>
      </el-main>
      </el-container>
    </el-container>
  </div>
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

