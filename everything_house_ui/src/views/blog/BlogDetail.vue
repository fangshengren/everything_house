<template>
  <div>
    <div class="getSuccess">
    <h1>{{ article.blogTitle }}</h1>
    <p>{{ article.blogContent }}</p>
    </div>
    <div class="getFailure">

    </div>
  </div>
</template>

<script>
import {baseURL} from "@/utils/request";

export default {
  data() {
    return {
      article: null
    }
  },
  created() {
    const blogId = this.$route.params.blogId;
    // Replace with your actual API call
    fetch(baseURL+`/blog/detail/${blogId}`,{
      method: 'get'
    }).then(response =>{
      return response.json();
    }
    ).then(data => {
      this.article = data.data; // 获取data属性
      //console.log(data);
    });
  }
}
</script>
