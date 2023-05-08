<template>
  <el-container class="el-container-blog">
    <el-header class="el-header-blog">
      <BlogHeader></BlogHeader>
    </el-header>
    <el-main class="el-main-blog"></el-main>
    <el-footer class="el-footer-blog">
      <BlogFooter></BlogFooter>
    </el-footer>
  </el-container>
</template>
<script>
import BlogAside from './blogComponent/BlogAside.vue'
import BlogHeader from './blogComponent/BlogHeader.vue'
import BlogFooter from './blogComponent/BlogFooter.vue'
  export default{
    name:"BlogHome",
    components: {BlogHeader,BlogFooter},
  }
</script>