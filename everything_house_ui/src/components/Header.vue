<template>
  <div style="display: inline-flex; align-items: center;">
    <span class="nickname_display">{{"Hello!"+user.nickname}}</span>
    <el-dropdown trigger="click" style="cursor: pointer">
      <i class="el-icon-arrow-down" style="margin-left: 5px"></i>
      <el-dropdown-menu slot="dropdown">
        <el-dropdown-item>
          <div>个人信息</div>
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
  data() {
    return{
      user:localStorage.getItem("user")?JSON.parse(localStorage.getItem("user")):{}
    }
  },
  methods:{
    logout(){
      this.$router.push("/login");
      localStorage.removeItem("user");
      this.$message.success("退出成功");
    }
  },
}
</script>

<style>

</style>
