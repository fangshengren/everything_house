<template>
  <div>
  <div class="blog-write-title-box">
    <input type="text" class="blog-write-title" v-model="form.blogTitle" placeholder="请输入标题..." />
    <el-button type="text" @click="openDialog">提交</el-button>
  </div>
    <el-upload
        class="blog-image-uploader"
        :action="baseURL()+'/file/upload'"
        :show-file-list="false"
        :on-success="handleAvatarSuccess"
    >
      <img v-if="form.avatar" :src="form.avatar" class="person-avatar">
      <i v-else class="el-icon-plus avatar-uploader-icon"></i>
    </el-upload>
  <mavon-editor style="min-height: 80vh" ref="md" v-model="form.blogContent" :ishljs="true" @imgAdd="imgAdd"/>
  </div>
</template>
<script>
import BlogAside from './blogComponent/BlogAside.vue'
import BlogHeader from './blogComponent/BlogHeader.vue'
import BlogFooter from './blogComponent/BlogFooter.vue'
import {baseURL} from "@/utils/request";
export default{
  name:"BlogWrite",
  methods: {
    baseURL() {
      return baseURL
    },
    handleAvatarSuccess(res) {
      //console.log(this.form.avatarUrl);
      //console.log(res);
      // 检查返回的响应是否是一个URL
      if (typeof res === 'string' && res.startsWith('http')) {
        this.form.avatar = res;
        this.$message.success("头像上传成功");
      } else {
        this.$message.error("头像上传失败");
      }
    },
  },
  data() {
    return {
      tableData: [],
      form: {
        blogTitle: '',
        blogContent: '',
      },
    }
  },

}
</script>