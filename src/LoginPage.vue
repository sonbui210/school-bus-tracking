<template>
  <div id="login">
    <el-row type="flex" class="login-container row-bg" justify="center">
      <el-col :xs="24" :sm="8" :md="8" :lg="8" :xl="8">
        <el-card shadow="always" class="login-form-header" body-style="color: white;">
          <h2 class="login-label">Đăng nhập</h2>
        </el-card>
        <el-card shadow="never" class="login-form-body">
          <el-row class="login-form-content" type="flex" justify="center">
            <el-col :span="20">
              <el-form ref="form">
                <el-form-item>
                  <el-input placeholder="Tên đăng nhập" v-model="user.username" @keyup.enter.native="login">
                    <i slot="prefix" class="el-input__icon el-icon-user"></i>
                  </el-input>
                </el-form-item>
                <el-form-item>
                  <el-input v-model="user.password" placeholder="Mật khẩu" show-password @keyup.enter.native="login">
                    <i slot="prefix" class="el-input__icon el-icon-lock"></i>
                  </el-input>
                </el-form-item>
                <el-form-item>
                  <span style="float: left">
                    Bạn chưa có tài khoản?

                    <a href="/register">
                      <el-button type="text">Đăng ký </el-button>
                    </a>
                  </span>
                  <el-button style="float: right" type="primary" @click="login">Đăng nhập</el-button>
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
import { setLocal } from "./lib/storage";
import { app } from "./lib/app";

export default {
  components: { SocialAuth },
  data() {
    return {
      user: {
        username: "",
        password: "",
      },
    };
  },
  methods: {
    async login() {
      let error = this.checkLogin();

      if (error.length > 0) {
        this.alert("error", error);
        return;
      } else {
        let response = await api.login(this.user);

        if (response) {
          this.alert("success", Message.LOGIN_SUCCESSFULL);
          setLocal({
            token: response.data.accessToken,
          });
          app.user = response.data.user.user;

          this.$router.push("/");
        }
      }
    },
    checkLogin() {
      if (!this.user.username || !this.user.password) {
        return Message.EMPTY_FIELD;
      }
      return "";
    },
  },
};
</script>
