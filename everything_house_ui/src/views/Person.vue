<template>
  <div id="home">
    <el-container>
      <el-main>
        <div>个人主页</div>
        <el-form :model="userInfo" label-width="120px">
          <el-form-item label="用户名" prop="username">
            <el-input v-model="userInfo.username" readonly></el-input>
          </el-form-item>
          <el-form-item label="邮箱" prop="email">
            <el-input v-model="userInfo.email"></el-input>
          </el-form-item>
          <el-form-item label="手机号" prop="phone">
            <el-input v-model="userInfo.phoneNumber"></el-input>
          </el-form-item>
          <el-form-item label="头像">
            <el-upload
                class="avatar-uploader"
                action="your-upload-api-url"
                :show-file-list="false"
                :on-success="handleAvatarSuccess"
                :before-upload="beforeAvatarUpload"
            >
              <img v-if="imageUrl" :src="imageUrl" class="avatar" />
              <i v-else class="el-icon-plus avatar-uploader-icon"></i>
            </el-upload>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="updateUserInfo">更新信息</el-button>
          </el-form-item>
        </el-form>
      </el-main>
    </el-container>
  </div>
</template>

<script>
import Aside from "@/components/Aside.vue";
import Header from "@/components/Header.vue";

export default {
  name: "Person",
  components: {
    Aside,
    Header,
  },
  data() {
    return {
      userInfo: {
        username: "",
        email: "",
        phoneNumber: "",
      },
      imageUrl: "",
    };
  },
  created() {
    this.loadUserInfo();
  },
  methods: {
    loadUserInfo() {
      // 请求后端 API 获取用户信息
      // 假设已经从后端获取到用户信息，存储在 userInfo 中
      this.userInfo.username = "user123";
      this.userInfo.email = "user123@example.com";
      this.userInfo.phoneNumber = "1234567890";
    },
    updateUserInfo() {
      // 更新用户信息，调用后端 API
      console.log("更新用户信息:", this.userInfo);
    },
    handleAvatarSuccess(res, file) {
      this.imageUrl = URL.createObjectURL(file.raw);
    },
    beforeAvatarUpload(file) {
      const isImage = file.type.includes("image/");
      if (!isImage) {
        this.$message.error("上传头像图片格式不正确!");
      }
      return isImage;
    },
  },
};
</script>

<style scoped>
.avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  width: 150px;
  height: 150px;
}
.avatar-uploader .el-upload:hover {
  border-color: #409eff;
}
.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 150px;
  height: 150px;
  line-height: 150px;
  text-align: center;
}
.avatar {
  width: 150px;
  height: 150px;
  display: block;
}
</style>
