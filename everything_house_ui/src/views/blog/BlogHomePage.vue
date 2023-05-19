<template>
  <div class="blog-home-page">
    <el-carousel trigger="click" class="blog-home-page-carousel">
      <el-carousel-item v-for="item in blogHomePicture.path" :key="item">
        <img :src="item" alt="" class="blog-home-page-carousel-img">
      </el-carousel-item>
    </el-carousel>
    <div class="blog-home-page-main">
      <div class="blog-home-page-main-first-part">
      <el-row>
        <el-col :xs="24" :sm="17" :md="17" :lg="17" :xl="17">
        <el-card class="blog-home-page-first-part-left-card">
          <div slot="header" class="clearfix">
            <span style="font-weight: 700">最新文章</span>
            <el-button style="float: right; padding: 3px 0;font-size: 0.7rem;color:dodgerblue;font-weight: 300" type="text" @click="pushToMoreBlogs">更多文章</el-button>
          </div>
          <div class="infinite-list-wrapper" style="overflow:auto">
            <ul class="list">
              <li v-for="blog in blogs" class="list-item">
                <div class="blog-item">
                  <div class="blog-cover">
                    <img :src="blog.coverImageUrl" alt="" />
                  </div>
                  <div class="blog-content">
                    <h2 class="blog-title">{{ blog.blogTitle }}</h2>
                    <p class="blog-excerpt">{{ truncateText(blog.blogContent,300) }}</p>
                    <div class="blog-meta">
                      <span class="blog-author">{{ blog.author }}</span>
                      <span class="blog-date">{{ blog.createTime }}</span>
                    </div>
                  </div>
                </div>
              </li>
            </ul>
            <p v-if="loading" style="font-size: 0.7rem;color:dodgerblue;font-weight: 300">加载中<img src="" alt="" class="el-icon-loading"></p>
            <el-button type="text" @click="loadMoreBlogs" v-if="!noMore&&!loading" style="font-size: 0.7rem;color:dodgerblue;font-weight: 300">加载更多</el-button>
            <el-button type="text" @click="toMoreBlogs" v-if="noMore" style="font-size: 0.7rem;color:dodgerblue;font-weight: 300">查看更多</el-button>
          </div>
        </el-card>
        </el-col>
        <el-col :xs="0" :sm="6" :md="6" :lg="6" :xl="6" style="" class="blog-home-util">
        <el-card class="blog-home-page-first-part-right-card">
          <div slot="header" class="clearfix">
            <span style="font-weight: 700">推荐工具</span>
            <el-button type="text" style="font-size: 0.7rem;color:dodgerblue;font-weight: 300;float: right; padding: 3px 0" @click="pushToMoreTools">更多工具</el-button>
          </div>
          <div class="infinite-list-wrapper">
            <ul class="list tool-list">
              <li v-for="tool in tools" class="list-item">
                <div class="tool-item">
                  <img :src="tool.imgHref" alt="" class="tool-img">
                  <a :href="tool.toolLink" class="tool-link">{{tool.toolName}}</a>
                </div>
              </li>
            </ul>
          </div>
        </el-card>
        </el-col>
      </el-row>
      </div>
    </div>
  </div>
</template>
<script>
export default {
  data() {
    return {
      page: 1,
      loading: false,
      blogHomePicture: {
        path: []
      },
      blogs: [

      ],
      tools:[
        {
          imgHref: "https://openai.com/favicon.ico",
          toolName: "chatGpt",
          toolLink: "https://chat.openai.com/",
        },
        {
          imgHref: "https://outlook.office.com/owa/favicon.ico",
          toolName: "outLook",
          toolLink: "https://outlook.office.com/mail/",
        },
        {
          imgHref: "https://static.deepl.com/img/logo/DeepL_Logo_darkBlue_v2.svg",
          toolName: "deepL",
          toolLink: "https://www.deepl.com/translator",
        },
        {
          imgHref: "https://www.csdn.net/favicon.ico",
          toolName: "CSDN",
          toolLink: "https://www.csdn.net/",
        },
      ],
    };
  },
  name: "BlogHomePage",
  created() {
    this.getBlogHomePicture();
    this.loadMoreBlogs();
  },
  computed: {
    noMore() {
      return this.count >= 50;
    }
  },
  methods: {
    pushToMoreTools(){
      this.$router.push("/blog/moreTools");
    },
    pushToMoreBlogs(){
      this.$router.push("/blog/moreBlogs");
    },
    truncateText(text, length) {
      let charCount = 0;
      for (let i = 0; i < text.length; i++) {
        const strCode = text.charCodeAt(i);
        if (strCode > 255) {
          charCount += 1;  // 对于汉字，计数器加1
        } else {
          charCount += 0.5;  // 对于英文字符，计数器加0.5
        }
        if (charCount > length) {
          return text.substring(0, i) + '...';
        }
      }
      return text;
    },
    getBlogHomePicture() {
      this.request.get("/blog/getBlogHomePicture").then(res => {
        console.log(res);
        this.blogHomePicture.path = res.data;
      });
    },
    loadMoreBlogs() {
      this.loading = true;
      this.request.get("/blog/getBlogs", {
        params: {
          limit: 5,
          page: this.page,
        },
      }).then(res => {
        console.log(res);
        this.blogs.push(...res.data.records);
        this.page += 1;
        this.loading = false;
      });
    },
    toMoreBlogs(){
      this.$router.push("/blog/moreBlogs");
    }
  }
};
</script>
