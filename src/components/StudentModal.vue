<template>
  <div>
    <el-dialog :title="title" :visible.sync="studentModalVisible" :close-on-click-modal="false">
      <el-form>
        <el-form-item label="Họ tên: ">
          <el-input v-model="student.fullName" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="MSSV: ">
          <el-input v-model="student.id" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="Lớp: ">
          <el-input v-model="student.className" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="Địa chỉ: ">
          <el-input v-model="student.address" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="Niên khoá: ">
          <el-input v-model="student.schoolYear" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="SĐT: ">
          <el-input v-model="student.phoneNumber" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="Email: ">
          <el-input v-model="student.emailStudent" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="SĐT phụ huynh: ">
          <el-input v-model="student.phoneNumberGrand" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="Email phụ huynh: ">
          <el-input v-model="student.emailGrand" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button type="danger" @click="studentModalVisible = false">Huỷ</el-button>
        <el-button type="primary" @click="addNewStudent()">Thêm</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import { api } from "../lib/api";
import Message from "../constant/Message";

export default {
  props: ["title"],
  data() {
    return {
      student: {},
      studentModalVisible: false,
    };
  },
  methods: {
    showModal() {
      this.studentModalVisible = true;
    },
    setStudent(student) {
      this.student = student;
    },
    async addNewStudent() {
      let response = await api.call("POST", "/api/student/createOrUpdate", this.student);
      if (response.data) {
        this.alert("success", Message.ADD_NEW_STUDENT_SCUCCESS);
        this.studentModalVisible = false;

        this.$emit("studentUpdated", response.data);
      }
    },
  },
};
</script>
