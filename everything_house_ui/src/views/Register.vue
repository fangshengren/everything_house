<template>
  <div class="register-container">
    <div class="register-logo"></div>
    <div class="register-background"></div>
  <div class="register-view">
    <el-card class="register-card">
      <h2 class="register-title">用户注册</h2>
      <el-form ref="registerForm" :model="form" label-width="80px" size="small" :rules="rules">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="form.username" @input="checkUserName" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input type="password" v-model="form.password" @input="checkPassword" autocomplete="off" show-password prefix-icon="el-icon-lock"></el-input>
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
          <el-input type="password" v-model="form.confirmPassword" autocomplete="off" show-password prefix-icon="el-icon-lock"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="register()">注册</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
  </div>
</template>

<script>
export default {
  name: "Register",
  data() {
    return {
      form: {
        username: '',
        password: '',
        confirmPassword: ''
      },
      passwordStrength: '',
      showStrengthTooltip: false,
      passwordStrengthText: '',
      passwordStrengthExtraText: '',
      hasAndCorrectConfirmPassword:false,
      userNameHasBlank:null,
      passwordHasBlank:null,
      passwordHasChinese:null,
      correctUserNameLength:false,
      correctPasswordLength:false,
      rules: {
        username: [
          { required: true, message: '请输入用户名', trigger: 'blur' },
          { min: 3, max: 20, message: '用户名长度应在5到20个字符之间,且不包含空格', trigger: 'blur' }
        ],
        password: [
          { required: true, message: '请输入密码', trigger: 'blur' },
          { min: 8, max: 20, message: '密码长度应在8到20个字符之间,且不包含空格和中文', trigger: 'blur' }
        ],
        confirmPassword: [
          { required: true, message: '请再次输入密码', trigger: 'blur' },
          {
            validator: (rule, value, callback) => {
              if (value !== this.form.password) {
                callback(new Error('两次输入的密码不一致'));
              } else {
                callback();
                this.hasAndCorrectConfirmPassword=true;
              }
            },
            trigger: 'blur'
          }
        ]
      }
    };
  },
  methods: {
    checkUserName(){
      const regex = /\s/;
      this.userNameHasBlank=regex.test(this.form.username);
      // console.log(this.form.username.length)
      // console.log((5<=this.form.username.length)&&(this.form.username.length<=20));
      this.correctUserNameLength=((5<=this.form.username.length)&&(this.form.username.length<=20));
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
      this.passwordHasBlank=regex.test(this.form.password);
      console.log(this.form.password.length)
      this.passwordHasChinese=regex2.test(this.form.password);
      this.correctPasswordLength=((8<=this.form.password.length)&&(this.form.password.length<=20));

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
    async register() {
      if(!this.hasAndCorrectConfirmPassword){
        this.$message.error("您还没有确认密码或者两次密码输入不一致");
      }else if(!this.correctPasswordLength){
        this.$message.error("密码长度不符合要求，请更改您的密码");
      }
      else if(!this.correctUserNameLength){
        this.$message.error("用户名长度不符合要求，请更改您的用户名");
      }
      else if(this.userNameHasBlank){
        this.$message.error("用户名中含有空格，请更改您的用户名");
      }
      else if(this.passwordHasBlank){
        this.$message.error("密码中含有空格，请更改您的密码");
      }
      else if(this.passwordHasChinese){
        this.$message.error("密码中含有中文，请更改您的密码");
      }
      else{
      try {
        const response = await this.request.post('/user/register', this.form);
        if (response.code === '200') {
          this.$message.success('注册成功');
          // 跳转到登录页面
          await this.$router.push('/login');
        } else{
          this.$message.error(response.msg || '注册失败');
        }
      } catch (error) {
        this.$message.error('注册失败，请稍后重试');
        console.error('Error in register():', error);
      }
      }
    }
  }
}
</script>
