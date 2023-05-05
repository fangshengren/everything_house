<template>
<!--  写忘记密码的页面 -->
  <div class="forget-password">
    <div class="forget-password-background"></div>
    <div class="forget-password-container">
      <div class="forget-password-title">
        <span>忘记密码/修改密码</span>
      </div>
      <div class="forget-password-content">
        <el-form ref="ForgetPasswordFormRef" :model="ForgetPasswordForm" :rules="ForgetPasswordFormRules" label-width="80px">
          <el-form-item label="邮箱" prop="email">
            <el-input v-model="ForgetPasswordForm.email" placeholder="请输入邮箱"></el-input>
          </el-form-item>
          <div style="display: flex;align-items: center;" >
          <el-form-item label="验证码" prop="code">
            <el-input type="code" v-model="ForgetPasswordForm.code" placeholder="请输入验证码" style="width: 200px" autocomplete="off"></el-input>
          </el-form-item>
          <el-button
              type="primary"
                size="medium"
              @click="getCode"
              :disabled="isDisabled"
              style="margin-bottom: 22px; margin-left: 10px"
          >
            {{ buttonText }}
          </el-button>
          </div>
          <el-form-item label="新的密码" prop="password">
            <el-input type="password" v-model="ForgetPasswordForm.password" placeholder="请输入新的密码" @input="checkPassword" autocomplete="off" show-password prefix-icon="el-icon-lock"></el-input>
            <el-tooltip
                v-if="showStrengthTooltip"
                class="item"
                effect="dark"
                :content="passwordStrengthText"
                placement="right"
                :manual="true"
                :value="showStrengthTooltip"
            >
              <div class="password-strength">
                <div
                    class="strength-bar"
                    :class="{ 'strength-weak': passwordStrength === 'weak' }"
                ></div>
                <div
                    class="strength-bar"
                    :class="{ 'strength-medium': passwordStrength === 'medium' }"
                ></div>
                <div
                    class="strength-bar"
                    :class="{ 'strength-strong': passwordStrength === 'strong' }"
                ></div>
              </div>
            </el-tooltip>
          </el-form-item>
          <el-form-item label="确认密码" prop="confirmPassword">
            <el-input v-model="ForgetPasswordForm.confirmPassword" placeholder="请再次输入密码" autocomplete="off" show-password prefix-icon="el-icon-lock"></el-input>
          </el-form-item>
          <el-button type="primary" @click="verificationSuccess" size="medium" style="margin-left: 330px">确认</el-button>
        </el-form>
      </div>
    </div>
  </div>

</template>

<script>
export default {
  name: "ForgetPassword",
  data() {
    return {
      isDisabled: false,
      buttonText: "获取验证码",
      countdown: 60,
      hasAndCorrectConfirmPassword:false,
      passwordStrength: '',
      showStrengthTooltip: false,
      passwordStrengthText: '',
      passwordStrengthExtraText: '',
      passwordHasBlank:null,
      passwordHasChinese:null,
      correctPasswordLength:false,
      ForgetPasswordForm: {
        email: "",
        code: "",
        password: "",
        confirmPassword: "",
      },
      ForgetPasswordFormRules: {
        email: [
          { required: true, message: "请输入邮箱", trigger: "blur" },
          {
            pattern: /^([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/,
            message: "邮箱格式不正确",
            trigger: "blur",
          },
        ],
        code: [
          { required: true, message: "请输入验证码", trigger: "blur" },
          {
            pattern: /^[a-zA-Z0-9]{4}$/,
            message: "验证码格式不正确",
            trigger: "blur",
          },
        ],
        password: [
          { required: true, message: "请输入新的密码", trigger: "blur" },
          { min: 8, max: 20, message: '密码长度应在8到20个字符之间,且不包含空格和中文', trigger: 'blur' }
        ],
        confirmPassword: [
          { required: true, message: "请再次输入密码", trigger: "blur" },
          {
            validator: (rule, value, callback) => {
              if (value !== this.ForgetPasswordForm.password) {
                callback(new Error('两次输入的密码不一致'));
              } else {
                callback();
                this.hasAndCorrectConfirmPassword=true;
              }
            },
            trigger: 'blur'
          }
        ],

      },
    };
  },
  methods: {
    verificationSuccess() {
      if(!this.hasAndCorrectConfirmPassword){
        this.$message.error("您还没有确认密码或者两次密码输入不一致");
      }else if(!this.correctPasswordLength){
        this.$message.error("密码长度不符合要求，请更改您的密码");
      }
      else if(this.passwordHasBlank){
        this.$message.error("密码中含有空格，请更改您的密码");
      }
      else if(this.passwordHasChinese){
        this.$message.error("密码中含有中文，请更改您的密码");
      }
      else {
        if (!this.ForgetPasswordForm.code) {
          this.$message.error("请输入验证码");
          return;
        }
        try{
          this.$refs.ForgetPasswordFormRef.validate(async (valid) => {
            if (!valid) return;
            const res = await this.request.post("/user/checkChangePassword", this.ForgetPasswordForm);
            console.log(res);
            if (res.code === '200') {
              await this.$router.push("/login");
              this.$message.success("修改密码成功");
            }else{
              this.$message.error(res.msg);
            }
          });
        }catch (e) {
          this.$message.error("服务器异常，请稍后重试");
          console.error('Error:',e);
        }
      }
    },
    getCode() {
      if (!this.ForgetPasswordForm.email) {
        this.$message.error("请输入邮箱");
        return;
      }
      if (!(/^\w+((-?\w+)|(\.\w+))*@[A-Za-z0-9]+((\.|-)[A-Za-z0-9]+)*\.[A-Za-z0-9]+$/.test(this.ForgetPasswordForm.email))) {
        this.$message.error("请输入正确的邮箱账号");
        return;
      }
      // 禁用按钮并启动倒计时
      this.isDisabled = true;
      this.buttonText = `请等待 ${this.countdown} 秒`;
      const timer = setInterval(() => {
        if (this.countdown <= 0) {
          clearInterval(timer);
          this.buttonText = "获取验证码";
          this.isDisabled = false;
          this.countdown = 60;
        }else{
          this.countdown--;
          this.buttonText = `请等待 ${this.countdown} 秒`;
        }
      }, 1000);
      this.$message.success("验证码已发送,请耐心等待系统响应");
      this.request.post("/user/sendForgetPasswordEmail/" + this.ForgetPasswordForm.email).then(res=>{
        if (res.code==='200'){
          this.$message.success("验证码发送成功");
        }else {
          this.$message.error(res.message);
        }}).catch(err=>{
          console.log(err);
          this.$message.error("验证码发送失败，请稍后再试");
      });
    },

    checkPassword(password) {
      this.showStrengthTooltip = !!password;
      const strongRegex = new RegExp(
          '^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#\$%\^&\*])(?=.{8,})'
      );
      const mediumRegex = new RegExp(
          '^(((?=.*[a-z])(?=.*[A-Z]))|((?=.*[a-z])(?=.*[0-9]))|((?=.*[A-Z])(?=.*[0-9])))(?=.{6,})'
      );
      const hasLowercase = (/[a-z]/.test(password))?"":"建议使用小写字母\n";
      const hasUppercase = (/[A-Z]/.test(password))?"":"建议使用大写字母\n";
      const hasNumber = (/[0-9]/.test(password))?"":"建议使用数字\n";
      const hasSpecialChar = (/[!@#\$%\^&\*]/.test(password))?"":"建议使用特殊符号如@#%^&*\n";
      const hasMinLength = (/^.{8,}/.test(password))?"":"建议使用更长的密码\n";
      this.passwordStrengthExtraText=hasLowercase+hasUppercase+hasNumber+hasSpecialChar+hasMinLength;

      const regex = /\s/;
      const regex2 = /[\u4e00-\u9fa5]/;
      this.passwordHasBlank=regex.test(this.ForgetPasswordForm.password);
      //console.log(this.ForgetPasswordForm.password.length)
      this.passwordHasChinese=regex2.test(this.ForgetPasswordForm.password);
      this.correctPasswordLength=((8<=this.ForgetPasswordForm.password.length)&&(this.ForgetPasswordForm.password.length<=20));

      if (strongRegex.test(password)) {
        this.passwordStrength = 'strong';
        this.passwordStrengthText = '密码强度:强\n'+this.passwordStrengthExtraText;
      } else if (mediumRegex.test(password)) {
        this.passwordStrength = 'medium';
        this.passwordStrengthText = '密码强度:中\n'+this.passwordStrengthExtraText;
      } else {
        this.passwordStrength = 'weak';
        this.passwordStrengthText = '密码强度:弱\n'+this.passwordStrengthExtraText;
      }
    },

  },

}
</script>
<style scoped>
.forget-password {
  position: relative;
}
.forget-password-background {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  /* background-color: #f5f5f5; */
  background-image: url("../image/注册页面背景.jpg");
  background-size: cover;
  background-repeat: no-repeat;
  background-position: center;
  z-index: -1;
}
.forget-password-container {
  position: absolute;
  top: calc(150px);
  left: calc(50% - 250px);
  width: 500px;
  height: 400px;
  background-color: #fff;
  border-radius: 10px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
}
.forget-password-title {
  width: 100%;
  height: 100px;
  line-height: 100px;
  text-align: center;
  font-size: 25px;
  font-weight: bold;
  color: #333;
}
.forget-password-content {
  margin: 0 auto;
  width: 80%;
  height: 200px;
}

</style>