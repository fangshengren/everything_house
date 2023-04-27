<template>
  <div class="login_container">
    <div class="login_log">
      <img src="../image/mylog.png" alt="logo" height="150px">
    </div>
    <div class="login_box">
      <el-tabs type="card" v-model="activeLoginMethod">
      <el-tab-pane label="账号密码登入" name="passwordLogin" @click="turnToPasswordLogin">
      <div style="margin:20px 0; text-align:center; font-size:24px"><b>登录</b></div>
      <el-form ref="LoginFormRef" :model="loginForm" :rules="LoginFormRules" >
        <!-- 用户名-->
        <el-form-item prop="username" class="login_input_form">
          <el-input size="medium" style="margin:10px 0px;width: 300px;margin-left:25px" v-model="loginForm.username" prefix-icon="el-icon-user"></el-input>
        </el-form-item>
        <!-- 密码-->
        <el-form-item prop="password" class="login_input_form">
          <el-input size="medium" style="margin:10px 0px;width: 300px;margin-left:25px" show-password v-model="loginForm.password" prefix-icon="el-icon-lock" type="password"></el-input>
        </el-form-item>
        <div style="margin:10px 0; text-align:center">
          <el-button type="primary" size="small" @click="login" >登录</el-button>
          <el-button type="warning" size="small" @click="resetLoginForm">重置</el-button>
          <br>
          <div style="display: flex; justify-content: space-between; padding: 0 20px;">
            <div style="text-align: left;">
              <span style="color: #409EFF; text-decoration: underline; cursor: pointer;" @click="forgetPassword">忘记密码</span>
            </div>
            <div style="text-align: right;">
              <span style="color: #409EFF; text-decoration: underline; cursor: pointer;" @click="register">注册用户</span>
            </div>
          </div>
        </div>
      </el-form>
      </el-tab-pane>
      <el-tab-pane label="邮箱登入" name="emailLogin" @click="turnToEmailLogin">
        <div style="margin:20px 0; text-align:center; font-size:24px"><b>邮箱登录</b></div>
        <el-form ref="EmailLoginFormRef" :model="emailLoginForm" :rules="EmailLoginFormRules" >
          <!-- 邮箱-->
          <el-form-item prop="email" class="login_input_form">
            <el-input size="medium" style="margin:10px 0px;width: 300px;margin-left:25px" v-model="emailLoginForm.email" prefix-icon="el-icon-message"></el-input>
          </el-form-item>
          <!-- 验证码-->
          <div style="display: flex;align-items: center;" >
          <el-form-item prop="code" class="login_input_form">
            <el-input size="medium" style="margin:10px 0px;width: 200px;margin-left:25px" v-model="emailLoginForm.code" prefix-icon="el-icon-mobile-phone" type="email"></el-input>
          </el-form-item>
            <el-button
                type="primary"
                size="small"
                @click="getCode"
                :disabled="isDisabled"
                style="margin-bottom: 22px; margin-left: 10px"
            >
              {{ buttonText }}
            </el-button>
          </div>
          <div style="margin:10px 0; text-align:center">
            <el-button type="primary" size="small" @click="emailLogin" >登录</el-button>
            <el-button type="warning" size="small" @click="resetEmailLoginForm">重置</el-button>
            <br>
          </div>
        </el-form>
      </el-tab-pane>
      </el-tabs>
    </div>
  </div>
</template>

<script>
import {setRoutes} from "@/router";
import dealSqlConfirm from '@/utils/dealSql';
import {resetRouter} from "@/router";
export default {
  name: "Login",
  mixins: [dealSqlConfirm],
  data() {
    return {
      isDisabled: false,
      buttonText: "获取验证码",
      countdown: 60,
      activeLoginMethod:"passwordLogin",
      loginForm: {
        username:'',
        password:'',
      },
      emailLoginForm: {
        email:'',
        code:'',
      },
      LoginFormRules:{
        username:[
          { required: true, message: '请输入用户名', trigger: 'blur' },
        ],
        password:[
          { required: true, message: '请输入密码', trigger: 'blur' },
        ]
      },
      EmailLoginFormRules:{
        email:[
          { required: true, message: '请输入邮箱', trigger: 'blur' },
        ],
        code:[
          { required: true, message: '请输入验证码', trigger: 'blur' },
          { min:4,max:4,message: '验证码长度为4位',trigger: 'change'}
        ]
      }
    }
  },
  methods:{
    turnToPasswordLogin(){
      this.activeLoginMethod="passwordLogin"
    },
    turnToEmailLogin(){
      this.activeLoginMethod="emailLogin"
    },
    login(){
      this.$refs['LoginFormRef'].validate(async (valid) => {
        if (valid) {
          this.request.post("/user/login",this.loginForm).then(res=>{
            if(res.code=='200'){
              localStorage.setItem("user",JSON.stringify(res.data));//存储用户信息到浏览器
              localStorage.setItem("menus",JSON.stringify(res.data.menus));//存储用户权限菜单信息到浏览器
              //动态设置当前用户的路由
              setRoutes();
              this.$router.push("/home");
              this.$message.success("登录成功");
            }else{
              this.$message.error(res.msg);
            }
          })
        }

      })
    },
    async emailLogin() {
      let loadingInstance;
      try {
        // 显示加载中
        loadingInstance = this.loginToggleLoading(true);

        this.$refs['EmailLoginFormRef'].validate(async (valid) => {
          if (valid) {
            const res = await this.request.post("/user/loginByEmail", this.emailLoginForm);

            if (res.code == '200') {
              localStorage.setItem("user", JSON.stringify(res.data)); //存储用户信息到浏览器
              localStorage.setItem("menus", JSON.stringify(res.data.menus)); //存储用户权限菜单信息到浏览器

              //动态设置当前用户的路由
              setRoutes();
              this.$router.push("/home");
              this.$message.success("登录成功");
              this.loginToggleLoading(false);
            } else {
              this.$message.error(res.msg);
            }
          }
        });
      } catch (error) {
        console.error("Error in emailLogin():", error);
        this.$message.error("登入异常");
        this.loginToggleLoading(false);
      } finally {
        // 关闭加载中
        loadingInstance && loadingInstance.close();
      }
    },

    getCode(){
      // 禁用按钮并启动倒计时
      this.isDisabled = true;
      this.buttonText = `请等待 ${this.countdown} 秒`;

      // 实现倒计时功能
      const countdownInterval = setInterval(() => {
        this.countdown--;
        this.buttonText = `请等待 ${this.countdown} 秒`;

        if (this.countdown === 0) {
          clearInterval(countdownInterval);
          this.isDisabled = false;
          this.buttonText = "获取验证码";
          this.countdown = 60;
        }
      }, 1000);
      if(!this.emailLoginForm.email){
        this.$message.error("请输入邮箱账号");
        return;
      }
      if (!(/^\w+((-?\w+)|(\.\w+))*@[A-Za-z0-9]+((\.|-)[A-Za-z0-9]+)*\.[A-Za-z0-9]+$/.test(this.emailLoginForm.email))) {
        this.$message.error("请输入正确的邮箱账号");
        return;
      }
      this.$message.success("验证码已发送,请耐心等待系统响应");
      this.request.post("/user/sendEmail/"+this.emailLoginForm.email).then(res=>{
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
    resetLoginForm(){
      this.$refs.LoginFormRef.resetFields()
    },
    resetEmailLoginForm(){
      this.$refs.EmailLoginFormRef.resetFields()
    },
    register(){
      this.$router.push("/register");
    },
    forgetPassword(){
      this.$router.push("/forgetPassword");
    }

  }
}
</script>
