<template>
  <div>
    <el-menu default-active="1" :collapse="isMobile()" style="text-align: left; height: 99vh; " class="el-menu-vertical-demo" router>
      <el-menu-item index="/">
        <i class="el-icon-menu"></i>
        <span slot="title">Danh sách xe bus</span>
      </el-menu-item>
      <el-menu-item index="/account">
        <i class="el-icon-s-custom"></i>
        <span slot="title">Thông tin tài khoản</span>
      </el-menu-item>
      <el-menu-item index="/student">
        <i class="el-icon-user-solid"></i>
        <span slot="title">Quản lý học sinh</span>
      </el-menu-item>
      <el-menu-item index="/user">
        <i class="el-icon-user"></i>
        <span slot="title">Quản lý người dùng</span>
      </el-menu-item>
      <el-menu-item index="/bus">
        <i class="el-icon-truck"></i>
        <span slot="title">Quản lý xe bus</span>
      </el-menu-item>
      <el-menu-item index="/log">
        <i class="el-icon-tickets"></i>
        <span slot="title">Theo dõi học sinh</span>
      </el-menu-item>

      <el-menu-item @click="logout">
        <i class="el-icon-switch-button"></i>
        <span slot="title">Đăng xuất</span>
      </el-menu-item>
    </el-menu>
  </div>
</template>

<script>
import { app } from "../lib/app";
import { setLocal } from "../lib/storage";

export default {
  methods: {
    logout() {
      setLocal({
        token: "",
      });
      app.local.token = "";
      window.location.href = "/login";
    },
  },
};
</script>
