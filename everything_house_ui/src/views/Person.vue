<template>
  <div id="website">
    <el-card style="width: 500px;">
      <el-form label-width="80px" size="small">
        <el-upload
            class="avatar-uploader"
            :action="baseURL()+'/file/upload'"
            :show-file-list="false"
            :on-success="handleAvatarSuccess"
        >
          <img v-if="form.avatar" :src="form.avatar" class="person-avatar">
          <i v-else class="el-icon-plus avatar-uploader-icon"></i>
        </el-upload>

        <el-form-item label="用户名">
          <el-input v-model="form.username" disabled autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="昵称">
          <el-input v-model="form.nickname" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="邮箱">
          <el-input v-model="form.email" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="电话">
          <el-input v-model="form.phone" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="地址">
          <el-input type="textarea" v-model="form.address" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="update()">确 定</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script>
import {baseURL} from "@/utils/request";

export default {
  name: "Person",
  data() {
    return {
      form: {
        id: '',
        avatar: '',
        username: '',
        nickname: '',
        email: '',
        phone: '',
        address: ''
      },
      user: localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {}
    };
  },

  created() {
    this.getUser().then(res => {
      //console.log(res);
      if (res && res.id) {
        this.form.id = res.id || '';
        this.form.avatar = res.avatar || '';
        this.form.username = res.username || '';
        this.form.nickname = res.nickname || '';
        this.form.email = res.email || '';
        this.form.phone = res.phone || '';
        this.form.address = res.address || '';
      }
    });
  },

  methods: {
    baseURL() {
      return baseURL
    },
    handleAvatarSuccess(res) {
      //console.log(this.form.avatarUrl);
      //console.log(res);
      // 检查返回的响应是否是一个URL
      if (typeof res === 'string' && res.startsWith('http')) {
        this.form.avatar = res;
        this.$message.success("头像上传成功");
      } else {
        this.$message.error("头像上传失败");
      }
    },
    async getUser() {
      //console.log(this.user);
      //console.log(this.user.id);
      try {
        const res = await this.request.get("/user/" + this.user.id);
        console.log('Response data:', res); // 打印返回的数据
        if (res && res.id) {
          return res;
        } else {
          // 可以添加错误处理逻辑，例如显示错误消息
          const errorMsg = res.msg || '未知错误'; // 使用默认错误消息
          console.error('Error fetching user data:', errorMsg);
          return {};
        }
      } catch (error) {
        console.error('Error in getUser():', error);
        return {};
      }
    },

    update() {
      this.request.put("/user", this.form).then((res) => {
        if (res > 0) {
          this.$message.success("更新成功");
          this.dialogFormVisible = false;

          // 获取本地存储中的 user 对象
          let user = JSON.parse(localStorage.getItem("user"));

          // 仅更新需要更新的属性
          user.avatar = this.form.avatar;
          user.username = this.form.username;
          user.nickname = this.form.nickname;
          user.email = this.form.email;
          user.phone = this.form.phone;
          user.address = this.form.address;

          // 将更新后的 user 对象存回本地存储
          localStorage.setItem("user", JSON.stringify(user));

          // 刷新页面
          window.location.reload();
        } else {
          this.$message.error("更新失败");
        }
      });
    },


  }
}
</script>

<style>
.avatar-uploader {
  text-align: center;
  padding-bottom: 10px;
}
.avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}
.avatar-uploader .el-upload:hover {
  border-color: #409EFF;
}
.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 138px;
  height: 138px;
  line-height: 138px;
  text-align: center;
}
.person-avatar {
  width: 138px;
  height: 138px;
  display: block;
}
</style>