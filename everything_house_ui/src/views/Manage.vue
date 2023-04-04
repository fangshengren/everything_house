<template>
  <div id="home">
    <el-container>
      <el-aside width="200px" style="background-color: rgb(238, 241, 246)">
        <Aside></Aside>
      </el-aside>
      <el-container>
      <el-header style="text-align: right; font-size: 12px; border-bottom: 1px solid red; line-height:60px">
        <Header name="王小虎测试"/>
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

