<template>
  <div>
    <div class="blog-detail-title">
      标题:{{ article.blogTitle }}
    </div>
    <mavon-editor v-if="article"
                  :subfield="false"
                  :defaultOpen="'preview'"
                  :editable="false"
                  :toolbarsFlag="false"
                  v-model="this.article.blogContent"
                  class="blog-detail-content">
    </mavon-editor>
  </div>
</template>

<script>
import {baseURL} from "@/utils/request";
import MarkdownIt from "markdown-it";


export default {

  data() {
    return {
      article: null,
      htmlContent: ''
    }
  },
  created() {
    const blogId = this.$route.params.blogId;

    // Replace with your actual API call
    fetch(baseURL + `/blog/detail/${blogId}`, {
      method: 'get'
    }).then(response => {
      return response.json();
    }).then(data => {
      this.article = data.data; // 获取data属性
      const md = new MarkdownIt({
        html: true,
        breaks: true,
        langPrefix: 'lang-',
        linkify: false,
        typographer: true,
        quotes: '“”‘’'
      });

      this.htmlContent = md.render(this.article.blogContent);
    });
  }
}
</script>
