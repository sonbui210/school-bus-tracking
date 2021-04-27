<template>
  <div style="padding: 32px;">
    <el-tabs v-model="activeName">
      <el-tab-pane label="Thông tin tài khoản" name="first">
        <el-form ref="form">
          <el-form-item label="Tên tài khoản">
            <el-input placeholder="Tên đăng nhập" v-model="user.username"> </el-input>
          </el-form-item>
          <el-form-item label="Email">
            <el-input v-model="user.email" placeholder="Email"> </el-input>
          </el-form-item>
          <el-form-item label="Số điện thoại">
            <el-input v-model="user.phone" placeholder="Số điện thoại"> </el-input>
          </el-form-item>
          <el-form-item>
            <el-button style="float: right" type="primary" @click="updateUserInfo">Cập nhật thông tin</el-button>
          </el-form-item>
        </el-form>
      </el-tab-pane>
      <el-tab-pane label="Thay đổi mật khẩu" name="second">
        <el-form ref="form">
          <el-form-item label="Nhập mật khẩu">
            <el-input v-model="newPassword" show-password> </el-input>
          </el-form-item>
          <el-form-item label="Nhập lại mật khẩu">
            <el-input v-model="reNewPassword" show-password> </el-input>
          </el-form-item>

          <el-form-item>
            <el-button style="float: right" type="primary" @click="updatePassword">Cập nhật mật khẩu</el-button>
          </el-form-item>
        </el-form>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script>
import { app } from "../lib/app";
import { api } from "../lib/api";

export default {
  data() {
    return {
      user: {
        username: "",
        email: "",
        phone: "",
      },
      activeName: "first",
      newPassword: "",
      reNewPassword: "",
    };
  },
  methods: {
    async updateUserInfo() {
      let resp = await api.call("POST", "/api/user/update", this.user);
      if (resp.data) {
        this.alert("success", "Cập nhật thông tin thành công!");
      }
    },
    async updatePassword() {
      if (this.newPassword.length == 0) {
        this.alert("error", "Vui lòng nhập mật khẩu mới");
        return;
      } else if (this.newPassword != this.reNewPassword) {
        this.alert("error", "Mật khẩu không khớp");
        return;
      } else {
        let request = {
          userId: this.user.id,
          newPassword: this.newPassword,
        };
        let resp = await api.call("POST", "/api/user/changePassword", request);
        if (resp.data) {
          this.alert("success", "Mật khẩu đã được cập nhật");
          this.activeName = "first";
        }
      }
    },
  },
  created() {
    this.user = app.user;
  },
};
</script>
