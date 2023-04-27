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
          <el-row>
            <el-col :span="18">
              <el-input v-model="form.email" disabled autocomplete="off"></el-input>
            </el-col>
            <el-col :span="5" style="margin-left: 15px">
              <el-button type="primary" @click="bindEmail()">更改邮箱</el-button>
            </el-col>
          </el-row>
        </el-form-item>
        <el-form-item label="电话">
          <el-input v-model="form.phone" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="地址">
          <el-input type="textarea" v-model="form.address" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="update()">确 定 更 改</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 弹窗  -->
    <el-dialog title="绑定邮箱" :visible.sync="dialogFormVisible" width="30%">
      <el-form ref="bindEmailForm" label-width="80px" :model="emailForm" :rules="EmailFormRules">
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="emailForm.email" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="验证码" prop="code">
          <el-row>
            <el-col :span="14">
              <el-input v-model="emailForm.code" autocomplete="off"></el-input>
            </el-col>
            <el-col :span="8" style="margin-left: 15px">
              <el-button
                  type="primary"
                  size="medium"
                  @click="sendEmailCode()"
                  :disabled="isDisabled"
              >{{ buttonText }}</el-button>
            </el-col>
          </el-row>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" size="medium" @click="bindEmailSubmit()">确 定 绑 定</el-button>
        </el-form-item>
      </el-form>
    </el-dialog>
    <el-dialog title="解绑邮箱" :visible.sync="dialogFormVisible2" width="30%">
      <el-form ref="bindEmailForm" label-width="80px" :model="emailForm" :rules="EmailFormRules">
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="emailForm.email" disabled autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="验证码" prop="code">
          <el-row>
            <el-col :span="14">
              <el-input v-model="emailForm.code" autocomplete="off"></el-input>
            </el-col>
            <el-col :span="8" style="margin-left: 15px">
              <el-button
                  type="primary"
                  size="medium"
                  @click="sendUnbindEmailCode()"
                  :disabled="isDisabled"
              >{{ buttonText }}</el-button>
            </el-col>
          </el-row>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" size="medium" @click="unbundleEmailSubmit()">确 定 解 绑</el-button>
        </el-form-item>
      </el-form>
    </el-dialog>
  </div>
</template>

<script>
import {baseURL} from "@/utils/request";

export default {
  name: "Person",
  data() {
    return {
      isDisabled: false,
      buttonText: "获取验证码",
      countdown: 60,
      dialogFormVisible: false,
      dialogFormVisible2: false,
      form: {
        id: '',
        avatar: '',
        username: '',
        nickname: '',
        email: '',
        phone: '',
        address: ''
      },
      emailForm:{
        email: '',
        code: '',
        username: '',
      },
      EmailFormRules:{
        email:[
          { required: true, message: '请输入邮箱', trigger: 'blur' },
        ],
        code:[
          { required: true, message: '请输入验证码', trigger: 'blur' },
          { min:4,max:4,message: '验证码长度为4位',trigger: 'change'}
        ]
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
    bindEmail() {
      if (this.form.email === "") {
        this.emailForm.username=this.form.username;
        this.dialogFormVisible = true;
      } else {
        this.emailForm.email = this.form.email;
        this.dialogFormVisible2 = true;
        this.$message.warning("请先解绑当前邮箱，然后再绑定新邮箱");
      }
    },

    sendEmailCode(){
      // 禁用按钮并启动倒计时
      this.isDisabled = true;
      this.buttonText = `请等待 ${this.countdown} 秒`;
      const timer = setInterval(() => {
        if (this.countdown <= 0) {
          clearInterval(timer); // 停止计时器
          this.buttonText = '获取验证码';
          this.isDisabled = false;
          this.countdown = 60;
        } else {
          this.countdown--;
          this.buttonText = `请等待 ${this.countdown} 秒`;
        }
      }, 1000);
      if (!this.emailForm.email) {
        this.$message.error("请输入邮箱");
        return;
      }
      if (!(/^\w+((-?\w+)|(\.\w+))*@[A-Za-z0-9]+((\.|-)[A-Za-z0-9]+)*\.[A-Za-z0-9]+$/.test(this.emailForm.email))) {
        this.$message.error("请输入正确的邮箱账号");
        return;
      }
      this.$message.success("验证码已发送,请耐心等待系统响应");
      this.request.post("/user/sendEmail/"+this.emailForm.email).then(res=>{
        if(res.code==='200'){
          this.$message.success('验证码发送成功！');
        }else{
          this.$message.error(res.msg);
        }
      }).catch((error)=>{
        console.log(error)
        this.$message.error('请求失败，请稍后再试！');
      });
    },
    sendUnbindEmailCode(){
      // 禁用按钮并启动倒计时
      this.isDisabled = true;
      this.buttonText = `请等待 ${this.countdown} 秒`;
      const timer = setInterval(() => {
        if (this.countdown <= 0) {
          clearInterval(timer); // 停止计时器
          this.buttonText = '获取验证码';
          this.isDisabled = false;
          this.countdown = 60;
        } else {
          this.countdown--;
          this.buttonText = `请等待 ${this.countdown} 秒`;
        }
      }, 1000);
      if (!this.emailForm.email) {
        this.$message.error("请输入邮箱");
        return;
      }
      if (!(/^\w+((-?\w+)|(\.\w+))*@[A-Za-z0-9]+((\.|-)[A-Za-z0-9]+)*\.[A-Za-z0-9]+$/.test(this.emailForm.email))) {
        this.$message.error("请输入正确的邮箱账号");
        return;
      }
      this.$message.success("验证码已发送,请耐心等待系统响应");
      this.request.post("/user/sendUnbindEmail/"+this.emailForm.email).then(res=>{
        console.log(res)
        if(res.code==='200'){
          this.$message.success('验证码发送成功！');
        }else{
          this.$message.error(res.msg);
        }
      }).catch((error)=>{
        console.log(error)
        this.$message.error('请求失败，请稍后再试！');
      });
    },
    bindEmailSubmit(){
      if (!this.emailForm.code) {
        this.$message.error("请输入验证码");
        return;
      }
      this.request.post("/user/bindEmail",this.emailForm).then(res=>{
        console.log(res)
        if(res.code==='200'){
          this.$message.success('绑定成功！');
          this.dialogFormVisible = false;
          this.emailForm.email = '';
          this.emailForm.code = '';
          // 获取本地存储中的 user 对象
          let user = JSON.parse(localStorage.getItem("user"));

          // 仅更新需要更新的属性
          user.email = this.emailForm.email;

          // 将更新后的 user 对象存回本地存储
          localStorage.setItem("user", JSON.stringify(user));

          // 刷新页面
          window.location.reload();
        }else{
          this.$message.error(res.msg);
        }
      }).catch((error)=>{
        console.log(error)
        this.$message.error('请求失败，请稍后再试！');
      });
    },

    unbundleEmailSubmit() {
      if (!this.emailForm.code) {
        this.$message.error("请输入验证码");
        return;
      }
      this.request.post("/user/unbindEmail",this.emailForm).then(res=>{
        console.log(res)
        if(res.code==='200'){
          this.$message.success('解绑成功！');
          this.dialogFormVisible = false;
          this.emailForm.email = '';
          this.emailForm.code = '';
          // 获取本地存储中的 user 对象
          let user = JSON.parse(localStorage.getItem("user"));

          // 仅更新需要更新的属性
          user.email = this.emailForm.email;

          // 将更新后的 user 对象存回本地存储
          localStorage.setItem("user", JSON.stringify(user));

          // 刷新页面
          window.location.reload();
        }else{
          this.$message.error(res.msg);
        }
      }).catch((error)=>{
        console.log(error)
        this.$message.error('请求失败，请稍后再试！');
      });
    },
    },
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