<template>
  <div class="blog-home-page">
    <el-carousel trigger="click" class="blog-home-page-carousel">
      <el-carousel-item v-for="item in blogHomePicture.path" :key="item">
        <img :src="item" alt="" class="blog-home-page-carousel-img">
      </el-carousel-item>
    </el-carousel>
  </div>
</template>
<script>
  export default{
      data(){
        return{
          blogHomePicture:{
            path:[

            ]
          }
        }
      },
      name:"BlogHomePage",
      created(){
        this.getBlogHomePicture()
      },
      methods:{
        getBlogHomePicture(){
          this.request.get('/blog/getBlogHomePicture').then(res=>{
            console.log(res)
            this.blogHomePicture.path=res.data
          })
        }
      }
  }
</script>