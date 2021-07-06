<template>
  <div style="width: 97%; padding: 0 16px; ">
    <el-row style="margin-top: 16px; margin-bottom:-16px">
      <el-col>
        <el-input style="width: 30%; float: left;" placeholder="Nhập để tìm kiếm..." v-model="searchKey"></el-input>
        <el-button type="primary" style="float: left; margin-left: 16px;" icon="el-icon-search" @click="searchStudent">Tìm kiếm</el-button>
      </el-col>
    </el-row>
    <StudentModal @studentUpdated="updateStudentList" ref="studentModal" :title="studentModalTitle" />
    <div style="width: 100%;  padding-top: 16px;">
      <h3 style="float: left">Danh sách học sinh</h3>
      <el-button v-if="roless === 'ADMIN'" icon="el-icon-circle-plus" style="float: right; margin: 16px 0" type="primary" @click="addOrEditStudent('add', {})">Thêm mới</el-button>
    </div>

    <el-table ref="singleTable" :data="students" highlight-current-row style="width: 100%">
      <el-table-column type="index" width="50"> </el-table-column>
      <el-table-column property="id" label="ID" width="120"> </el-table-column>
      <el-table-column property="fullName" label="Họ tên" width="250"> </el-table-column>
      <el-table-column property="status" label="Trạng thái" width="150">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.status == 'ON_BUS'" type="success">Đang trên xe {{ scope.row.bus != null ? scope.row.bus.code : "Unknow" }}</el-tag>
          <el-tag v-else type="danger">Chưa lên xe</el-tag>
        </template>
      </el-table-column>
      <el-table-column property="className" label="Lớp"> </el-table-column>
      <el-table-column property="address" label="Địa chỉ"> </el-table-column>
      <el-table-column property="schoolYear" label="Niên khoá"> </el-table-column>
      <el-table-column v-if="roless === 'ADMIN'" fixed="right" label="Operations" width="240">
        <template slot-scope="scope">
          <el-button icon="el-icon-edit" @click="addOrEditStudent('edit', scope.row)" type="button" size="small">Chỉnh sửa</el-button>
          <el-button icon="el-icon-delete" type="button" size="small">Xoá</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
import { api } from "../lib/api";

export default {
  components: {
    StudentModal: () => import("../components/StudentModal"),
  },
  data() {
    return {
      students: [],
      studentModalTitle: "Thêm mới học sinh",
      searchKey: "",
      busRoles: [],
      roless: "",
    };
  },
  methods: {
    async fetchStudents() {
      let resp = await api.call("GET", "/api/student");
      if (resp.data) {
        this.students = resp.data;
      }
    },
    addOrEditStudent(type, student) {
      if (type == "add") {
        this.busModalTitle = "Thêm mới học sinh";
      } else {
        this.busModalTitle = "Chỉnh sửa thông tin học sinh";
      }

      this.$refs.studentModal.showModal();
      this.$refs.studentModal.setStudent(student);
    },
    updateStudentList(student) {
      let existed = false;
      this.students.forEach((s) => {
        if (s.id == student.id) {
          existed = true;
        }
      });

      if (!existed) {
        this.students.unshift(student);
      }
    },
    async searchStudent() {
      if (this.searchKey.length > 0) {
        let resp = await api.call("GET", "/api/student/search?searchKey=" + this.searchKey);
        if (resp.data) {
          this.students = resp.data;
        }
      } else {
        this.fetchStudents();
      }
    },
    async fetchRolse() {
      let res = await api.call("GET", "/api/auth/me", null);
      if (res.data) {
        this.busRoles = res.data.roles.map((b) =>{
          return{
            authority: b.authority,
          }
        });
        this.roless = this.busRoles[0].authority
      } else {this.roless = "null"}
    },
  },
  beforeMount() {
    this.fetchRolse();
  },
  created() {
    this.fetchStudents();
  },
};
</script>
