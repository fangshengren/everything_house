<template>
  <div style="display: inline-flex; align-items: center;">
    <div style="display: inline-flex; align-items: center;">
      <img :src="user.avatar" alt="" style="width: 30px; height: 30px; border-radius: 50%; margin-right: 5px;" @click="person" class="avatar">
      <span class="nickname_display">{{"Hello!"+user.nickname}}</span>
    </div>
    <el-dropdown trigger="click" style="cursor: pointer">
      <i class="el-icon-arrow-down" style="margin-left: 5px"></i>
      <el-dropdown-menu slot="dropdown">
        <el-dropdown-item>
          <div @click="person">个人信息</div>
        </el-dropdown-item>
        <el-dropdown-item>
          <div class="el-dropdown-item-content" @click="logout">退出</div>
        </el-dropdown-item>
      </el-dropdown-menu>
    </el-dropdown>
  </div>
</template>
<script>
export default {
  name: "Header",
  props: {
    name: String,
  },
  computed: {
    user() {
      return localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {};
    },
    avatar() {
      return localStorage.getItem("avatar") || this.user.avatar;
    },
  },
  methods: {
    logout() {
      this.$router.push("/login");
      localStorage.removeItem("user");
      this.$message.success("退出成功");
    },
    person() {
      this.$router.push("/person");
    },
  },
  watch: {
    user: {
      handler(val) {
        this.$forceUpdate();
      },
      deep: true
    }
  },
};
</script>

<style>

</style>
