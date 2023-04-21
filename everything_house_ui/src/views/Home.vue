<template>
  <div class="home-page">
    <div class="project-overview">
      <h2 class="home-h2">项目概要</h2>
      <p>
        这是我的第一个项目......待续
      </p>
<!--      <p>-->
<!--        思路参考: <a href="https://blog.csdn.net/wdyan297?type=blog">csdn的一个案例</a>-->
<!--      </p>-->
    </div>

    <div class="tech-stack">
      <h2 class="home-h2">技术栈</h2>
      <el-row :gutter="20">
        <el-col :span="8">
          <h3 class="home-h3">前端</h3>
          <el-card v-for="(tech, index) in frontendStacks" :key="index">
            {{ tech }}
          </el-card>
        </el-col>
        <el-col :span="8">
          <h3 class="home-h3">后端</h3>
          <el-card v-for="(tech, index) in backendStacks" :key="index">
            {{ tech }}
          </el-card>
        </el-col>
        <el-col :span="8">
          <h3 class="home-h3">其他技术</h3>
          <el-card v-for="(tech, index) in OtherStacks" :key="index">
            {{ tech }}
          </el-card>
        </el-col>
      </el-row>
    </div>

<!--    <div class="home-insert-content">-->
<!--      <h2 class="home-h2">收款码</h2>-->
<!--      <img src="../image/微信支付.png" alt="收款码" class="home-insert-image" />-->
<!--    </div>-->
  </div>
</template>

<script>
export default {
  name: "Home",
  data() {
    return {
      frontendStacks2: ["Vue.js", "ElementUI","HTML","CSS","JavaScript"],
      frontendStacks: ["Vue.js", "ElementUI"],
      backendStacks: ["SpringBoot", "Mybatis-plus", "Selenium-java爬虫"],
      OtherStacks: ["Nginx", "Docker", "Git"],
    };
  },
}
</script>

