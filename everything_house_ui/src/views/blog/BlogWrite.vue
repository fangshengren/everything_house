<template>
  <div>
  <div class="blog-write-title-box">
    <input type="text" class="blog-write-title" v-model="form.blogTitle" placeholder="请输入标题..." />
    <el-button type="text" :disabled="isDisabled" @click="submitBlog">提交</el-button>
  </div>
    <p style="text-align: left;font-size: 0.7rem;height: 0.7rem;line-height: 0.7rem;margin-bottom: 0.3rem">可选择上传展示图片:</p>
    <el-upload
        class="blog-image-uploader"
        :action="baseURL()+'/file/upload'"
        :show-file-list="false"
        :on-success="handleAvatarSuccess"
    >
      <img v-if="form.img" :src="form.img" class="blog-avatar">
      <i v-else class="el-icon-plus blog-avatar-uploader-icon"></i>
    </el-upload>
  <mavon-editor style="min-height: 80vh" ref="md" v-model="form.blogContent" :ishljs="true" @imgAdd="imgAdd" @save="" />
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
        this.form.img = res;
        const respond = this.request.get("/file/getUrlId");
        if (respond.code == '200') {
          this.form.imgId = respond.data;
        }
        this.$message.success("头像上传成功");
      } else {
        this.$message.error("头像上传失败");
      }
    },
    submitBlog(){
      try{
        this.request.post("/blog/saveBlog",this.form).then(res=>{
          console.log(res)
          if(res.code==='200') {
            this.$message.success("发布成功");
            this.$router.push("/blog/detail/"+res.data);
          }else{
            this.$message.error("发布失败"+res.msg);
          }
        }
        )
      }catch (e){
        this.$message.error("发布失败"+e);
      }

    }
  },
  data() {
    return {
      htmlContent: '',
      tableData: [],
      form: {
        // 用于展示，存的是存图片id
        img:'',
        imgId:'1',
        blogTitle: '',
        blogContent: '',
        authorID:'',
      },
    }
  },
  watch: {
    'user.id': function(newVal, oldVal) {
      this.form.authorID = newVal;
    }
  },
  created() {
    this.form.authorID = this.user.id;
  },
  computed:{
    user() {
      return localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {};
    },
    isDisabled() {
      return !this.form.blogTitle || !this.form.blogContent || !this.form.authorID;
    }
  }

}
</script>