<template>
  <div class="blog-header-container">
      <div class="blog-header-left">
      <!-- 向左靠的logo部分 -->
        <img src="../../../assets/logo.png" alt="图片不存在" class="blog-header-image">
        <span class="blog-header-title">WYQ Blog</span>
      </div>
      <!-- 中间的按钮菜单部分 -->
      <div class="blog-header-menu">
        <el-menu
          :default-active="activeIndex"
          class="blog-header-el-menu"
          mode="horizontal"
          @select="handleSelect"
          background-color="#545c64"
          text-color="#fff"
          active-text-color="#ffd04b"
        >
          <el-menu-item index="1"><span>首页</span></el-menu-item>
          <el-submenu index="2">
            <template slot="title">文章</template>
            <el-menu-item index="2-1">文章列表</el-menu-item>
            <el-menu-item index="2-2">文章归档</el-menu-item>
            <el-menu-item index="2-3">文章分类</el-menu-item>
          </el-submenu>
          <el-submenu index="3">
            <template slot="title">留言</template>
            <el-menu-item index="3-1">留言列表</el-menu-item>
            <el-menu-item index="3-2">留言板</el-menu-item>
          </el-submenu>
          <el-submenu index="4">
            <template slot="title">关于</template>
            <el-menu-item index="4-1">关于我</el-menu-item>
            <el-menu-item index="4-2">关于本站</el-menu-item>
          </el-submenu>
        </el-menu>
      </div>
      <!-- 向右靠的用户头像和用户名部分 -->
      <div class="blog-header-right">
        <el-dropdown trigger="click" v-model="dropdownVisible">
        <div class="blog-header-right-hover" style="cursor: pointer">
          <img :src="user.avatar" alt="加载失败" class="blog-header-image blog-right-header-image" v-if="user.avatar!==undefined||null">
          <img src="../../../assets/default_user_avatar.jpg" alt="加载失败" class="blog-header-image blog-right-header-image" v-else="">
          <span class="blog-header-username" v-if="user.nickname===undefined">{{"Please Login"}}</span>
          <span class="blog-header-username" v-else="">{{displayedNickname}}</span>
          <div class="blog-header-right-arrow">
            <el-icon :class="arrowIcon" style="margin-left: 5px"></el-icon>
          </div>
        </div>
          <el-dropdown-menu slot="dropdown" class="blog-header-dropdown-menu">
            <el-dropdown-item>
              <div @click="person" class="blog-header-dropdown-menu-item">
                <span class="el-icon-user"></span>个人信息
              </div>
            </el-dropdown-item>
            <el-dropdown-item>
              <div class="blog-header-dropdown-menu-item" @click="login"  v-if="user.username===undefined||null">
                <span class="el-icon-right"></span>
                登入
              </div>
              <div class="blog-header-dropdown-menu-item" @click="logout" v-else>
                <span class="el-icon-left"></span>
                退出
              </div>
            </el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
      </div>
  </div>
</template>
<script>
  export default{
    name:"BlogHeader",
    data(){
      return{
        dropdownVisible: false,
      }
    },
    methods:{

    },
    computed:{
      arrowIcon() {
        return this.dropdownVisible ? "el-icon-arrow-up" : "el-icon-arrow-down";
      },
      user() {
        return localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {};
      },
      displayedNickname() {
        if (this.user.nickname.length > 6) {
          return "Hi!" + this.user.nickname.slice(0, 6) + '...';
        }
        return "Hi!" + this.user.nickname;
      }
    }
  }
</script>