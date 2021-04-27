<template>
  <div id="login">
    <el-row type="flex" class="login-container row-bg" justify="center">
      <el-col :xs="24" :sm="8" :md="8" :lg="8" :xl="8">
        <el-card shadow="always" class="login-form-header" body-style="color: white;">
          <h2 class="login-label">Đăng ký tài khoản</h2>
        </el-card>
        <el-card shadow="never" class="login-form-body" style="height: 600px;">
          <el-row class="login-form-content" type="flex" justify="center">
            <el-col :span="20">
              <el-form ref="form">
                <el-form-item>
                  <el-input placeholder="Tên đăng nhập" v-model="user.username" @keyup.enter.native="register">
                    <i slot="prefix" class="el-input__icon el-icon-user"></i>
                  </el-input>
                </el-form-item>
                <el-form-item>
                  <el-input v-model="user.password" placeholder="Mật khẩu" show-password @keyup.enter.native="register">
                    <i slot="prefix" class="el-input__icon el-icon-lock"></i>
                  </el-input>
                </el-form-item>
                <el-form-item>
                  <el-input v-model="user.rePassword" placeholder="Nhập lại mật khẩu" show-password @keyup.enter.native="register">
                    <i slot="prefix" class="el-input__icon el-icon-lock"></i>
                  </el-input>
                </el-form-item>
                <el-form-item>
                  <el-input v-model="user.email" placeholder="Nhập địa chỉ email" @keyup.enter.native="register">
                    <i slot="prefix" class="el-input__icon el-icon-message"></i>
                  </el-input>
                </el-form-item>
                <el-form-item>
                  <el-input v-model="user.phone" placeholder="Nhập số điện thoại" @keyup.enter.native="register">
                    <i slot="prefix" class="el-input__icon el-icon-phone"></i>
                  </el-input>
                </el-form-item>
                <el-form-item>
                  <span style="float: left">
                    Bạn đã có tài khoản?

                    <a href="/login">
                      <el-button type="text">Đăng nhập </el-button>
                    </a>
                  </span>
                  <el-button style="float: right" type="primary" @click="register">Đăng ký</el-button>
                </el-form-item>
              </el-form>
            </el-col>
          </el-row>
          <el-divider content-position="center">hoặc</el-divider>
          <SocialAuth />
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<style scoped>
@import "./assets/css/login-page.css";
@import url("https://fonts.googleapis.com/css2?family=Roboto:ital,wght@0,300;0,900;1,700&display=swap");
</style>

<script>
import SocialAuth from "./components/SocialAuth";
import Message from "./constant/Message";
import { api } from "./lib/api";

export default {
  components: { SocialAuth },
  data() {
    return {
      user: {
        username: "",
        password: "",
        rePassword: "",
        phone: "",
      },
    };
  },
  methods: {
    async register() {
      let error = this.checkRegister();

      if (error.length > 0) {
        this.alert("error", error);
        return;
      } else {
        let response = await api.call("POST", "/api/auth/register", this.user);

        if (response.data) {
          this.alert("success", Message.REGISTER_SUCCESSFULL);

          this.$router.push("/login");
        } else {
          this.alert("error", "Đăng ký thất bại!");
        }
      }
    },
    checkRegister() {
      if (!this.user.username || !this.user.password || !this.user.phone) {
        return Message.EMPTY_FIELD;
      }

      if (this.user.password != this.user.rePassword) {
        return Message.PASSWORD_NOT_MATCH;
      }
      return "";
    },
  },
};
</script>
