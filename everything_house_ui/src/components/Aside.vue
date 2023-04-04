<template>
  <el-menu :default-openeds="[]"
           background-color=rgb(48,65,86)
           text-color=#ccc
           active-text-color=#ff0000
           router="">
    <div style="height:60px; line-height:60px; text-align:center">
      <img src="../assets/logo.png" style="width:20px;position:relative;top:5px;margin-right:5px"/>
      <b style="color:white">WYQ Project</b>
    </div>
    <el-menu-item index="/">
      <template slot="title">
        <i class="el-icon-house"></i>
        <span slot="title">主页</span></template>
    </el-menu-item>
    <el-submenu index="/manage">
      <template slot="title"><i class="el-icon-menu"></i>用户管理</template>
      <el-menu-item-group>
        <el-menu-item index="about">用户列表</el-menu-item>
        <el-menu-item index="about2">权限列表</el-menu-item>
        <el-menu-item index="about3">选项3</el-menu-item>
      </el-menu-item-group>
    </el-submenu>
    <el-submenu index="/user">
      <template slot="title"><i class="el-icon-setting"></i>导航三</template>
      <el-menu-item-group>
        <template slot="title">分组一</template>
        <el-menu-item index="3-1">选项1</el-menu-item>
        <el-menu-item index="3-2">选项2</el-menu-item>
      </el-menu-item-group>
      <el-menu-item-group title="分组2">
        <el-menu-item index="3-3">选项3</el-menu-item>
      </el-menu-item-group>
      <el-submenu index="3-4">
        <template slot="title">选项4</template>
        <el-menu-item index="3-4-1">选项4-1</el-menu-item>
      </el-submenu>
    </el-submenu>
  </el-menu>
</template>

<script>
export default {
  //输出组件
  name: "Aside"
}
</script>

<style scoped>
</style>
