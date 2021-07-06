<template>
  <div style="display: flex; flex-flow: column; width: 100%;">
    <el-row style="margin: 16px;">
      <el-col>
        <el-input style="width: 30%; float: left;" placeholder="Nhập để tìm kiếm..." v-model="searchKey"></el-input>
        <el-button type="primary" style="float: left; margin-left: 16px;" icon="el-icon-search" @click="searchUser">Tìm kiếm</el-button>
      </el-col>
    </el-row>
    <el-table :data="users" height="750px" style="width: 97%; margin-left: 16px;">
      <el-table-column prop="username" label="Tên đăng nhập"> </el-table-column>
      <el-table-column prop="email" label="Email"> </el-table-column>
      <el-table-column prop="phone" label="Số điện thoại"> </el-table-column>
      <el-table-column prop="roles.0.code" label="Role">
      </el-table-column>
      <el-table-column fixed="right" label="Operations" width="240">
        <template slot-scope="scope">
          <el-button @click="updateUserInfo('edit', scope.row)" type="button" size="small">Lưu</el-button>
        </template>
      </el-table-column>
    </el-table>
    
  </div>
</template>

<script>
import { api } from "../lib/api";

export default {
  data() {
    return {
      users: [],
      searchKey: "",
      busRoles: [],
      roless: "",
    };
  },
  methods: {
        async updateUserInfo() {
      let resp = await api.call("POST", "/api/user/update", this.user);
      if (resp.data) {
        this.alert("success", "Cập nhật thông tin thành công!");
      }
    },
    async fetchAllUsers() {
      let resp = await api.call("GET", "/api/user", null);
      if (resp.data) {
        this.users = resp.data;
      }
    },
    async searchUser() {
      if (this.searchKey.length > 0) {
        let resp = await api.call("GET", "/api/user/search?searchKey=" + this.searchKey, null);
        if (resp.data) {
          this.users = resp.data;
        }
      } else {
        this.fetchAllUsers();
      }
    },
  },
  created() {
    this.fetchAllUsers();
  },
};
</script>
