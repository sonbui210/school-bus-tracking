<template>
  <div style="display: flex; flex-flow: column; width: 100%;">
    <el-row style="margin: 16px;">
      <el-col>
        <el-input style="width: 30%; float: left;" placeholder="Nhập để tìm kiếm..." v-model="searchKey"></el-input>
        <el-select style=" float: left; margin-left: 16px; width: 100px;" v-model="status" placeholder="Trạng thái">
          <el-option label="Tất cả" value="all"> </el-option>
          <el-option label="Lên xe" value="on"> </el-option>
          <el-option label="Xuống xe" value="off"> </el-option>
        </el-select>
        <el-button type="primary" style="float: left; margin-left: 16px;" icon="el-icon-search" @click="doFilter">Lọc</el-button>
      </el-col>
    </el-row>
    <el-table :data="logs" height="750px" style="width: 100%">
      <el-table-column prop="message" label="Thông tin" width="580"> </el-table-column>
      <el-table-column prop="createAt" label="Thời gian" width="180"> </el-table-column>
      <el-table-column label="Trạng thái">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.status == 1" style="width: 80px; text-align: center;" type="primary">Lên xe</el-tag>
          <el-tag v-if="scope.row.status == 0" style="width: 80px; text-align: center;" type="danger">Xuống xe</el-tag>
        </template>
      </el-table-column>
<!--
      <el-table-column label="Hình ảnh">
        <template slot-scope="scope">
          <el-image style="width: 100px; height: 100px" :src="'https://dataschoolbus.herokuapp.com/api/files/downloadFile/' + scope.row.image"></el-image>
        </template>
      </el-table-column>
-->
    </el-table>
    <el-pagination
      style="margin-top: 16px; margin-bottom: 16px;"
      background
      layout="prev, pager, next"
      :total="totalElements"
      @next-click="
        page = page + 1;
        fetchLogs();
      "
      @prev-click="
        page = page - 1;
        fetchLogs();
      "
      :current-page="page"
      @current-change="currentPageChanged"
    >
    </el-pagination>
  </div>
</template>

<script>
import { api } from "../lib/api";

export default {
  data() {
    return {
      logs: [],
      page: 1,
      size: 10,
      totalElements: 0,
      searchKey: "",
      status: "all",
    };
  },
  methods: {
    async fetchLogs() {
      let resp = await api.call("GET", "/api/studentTracking?page=" + (this.page - 1) + "&pageSize=" + this.size, null);
      if (resp.data) {
        this.logs = resp.data.content;
        this.totalElements = resp.data.totalElements;
      }
    },
    currentPageChanged(page) {
      this.page = page;
      this.fetchLogs();
    },
    async doFilter() {
      if (this.searchKey.length == 0 && this.status == "all") {
        this.fetchLogs();
      } else {
        this.page = 1;
        let request = {
          page: this.page,
          pageSize: this.size,
          searchKey: this.searchKey,
          status: this.status,
        };
        let resp = await api.call("POST", "/api/studentTracking", request);
        if (resp.data) {
          this.logs = resp.data.content;
          this.totalElements = resp.data.totalElements;
        }
      }
    },
  },
  created() {
    this.fetchLogs();
  },
};
</script>
