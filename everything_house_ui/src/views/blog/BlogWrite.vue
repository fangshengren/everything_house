<template>
  <div>
  <div class="blog-write-title-box">
    <input type="text" class="blog-write-title" v-model="form.title" placeholder="请输入标题..." />
    <el-button type="text" @click="openDialog">提交</el-button>
  </div>
  <mavon-editor style="min-height: 80vh" ref="md" v-model="form.content" :ishljs="true" @imgAdd="imgAdd"/>
  </div>
</template>
<script>
import BlogAside from './blogComponent/BlogAside.vue'
import BlogHeader from './blogComponent/BlogHeader.vue'
import BlogFooter from './blogComponent/BlogFooter.vue'
export default{
  name:"BlogWrite",
  data() {
    return {
      tableData: [],
      form: {},
    }
  },

}
</script>