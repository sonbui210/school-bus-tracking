<template>
  <div> 
    <el-menu default-active="1" :collapse="isMobile()" style="text-align: left; height: 100%; " class="el-menu-vertical-demo" router>
      <div style="margin-top: 20px; background-color: #ECF5FF ">
      <span style=" margin-left: 12%;"> User: {{userr}}<br></span>
      <span style=" margin-left: 12%;"> Role: {{roless}}</span>
      </div>
       <el-menu-item v-if="roless === 'PHUHUYNH'" index="/studentinfo">
        <i class="el-icon-s-custom"></i>
        <span slot="title">Thông tin học sinh</span>
      </el-menu-item>
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
      <el-menu-item v-if="roless === 'ADMIN'" index="/user">
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
import { api } from "../lib/api";

export default {
  data() {
    return {
      busRoles: [],
      roless: "",
      userr: ""
    };
  },
  methods: {
    async fetchRolse() {
      let res = await api.call("GET", "/api/auth/me", null);
      if (res.data) {
        this.userr = res.data.username;
        this.busRoles = res.data.roles.map((b) =>{
          return{
            authority: b.authority,
          }
        });
        this.roless = this.busRoles[0].authority
      } else {this.roless = "null"}
    },
    logout() {
      setLocal({
        token: "",
      });
      app.local.token = "";
      window.location.href = "/login";
    },
    
  },
  beforeMount() {
    this.fetchRolse();
  },
};
</script>
