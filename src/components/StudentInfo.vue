<template>
  <div style="display: flex; flex-flow: column; width: 100%;">
    <el-row style="margin: 16px;">
      <el-col>
        <el-input style="width: 30%; float: left;" placeholder="Nhập để tìm kiếm..."></el-input>
        </el-select>
        <el-button type="primary" style="float: left; margin-left: 16px;" icon="el-icon-search" >Lọc</el-button>
      </el-col>
    </el-row>

    <div style="margin-top: 0px; width: 100%; height: 100%; backgroundColor: #FFFFFB">
        <div style="margin-top: 20px; width: 20%; float:left">
            <el-image style="width:150px;height: 200px; border-radius: 20px; border: 5px solid #555;" :src="'https://dataschoolbus.herokuapp.com/api/files/downloadFile/'+studentinfo.avatar"></el-image>
        <!--    <img src="/images/sonbui.jpg" alt="Avatar" style="width:200px;border-radius: 20px;">  -->
        </div>

        <div style="margin-top: 20px; width: 80%; float:right">
            <div>
                <div style="width: 30%; float:left">
                    <p style=" text-align:left;">ID học sinh       :  {{studentinfo.id}}</p>
                    <p style=" text-align:left;">Họ và tên         :  {{studentinfo.fullName}}</p>
                    <p style=" text-align:left;">Địa chỉ           :  {{studentinfo.address}}</p>
                    <p style=" text-align:left;">Niên khóa         :  {{studentinfo.schoolYear}}</p>
                    <p style=" text-align:left;">Số điện thoại     :  {{studentinfo.phoneNumber}}</p>
                    <p style=" text-align:left;">Email             :  {{studentinfo.emailStudent}}</p>
                </div>
            
                <div style="width: 50%; float:left">
                    <p style=" text-align:left;">SĐT phụ huynh     :  {{studentinfo.phoneNumberGrand}}</p>
                    <p style=" text-align:left;">Email phụ huynh   :  {{studentinfo.emailGrand}}</p>
                        <div>
                              <p style=" text-align:left;">Trạng thái   : 
                              <el-tag v-if="studentinfo.status == 'ON_BUS'" type="success">Đang trên xe {{ studentinfo != null ? studentinfo.busCodeGet : "Unknow" }}</el-tag>
                              <el-tag v-else type="danger">Chưa lên xe</el-tag>
                              </p>
                        </div>
                </div>
            </div>
        </div>
    </div>


  </div>
</template>

<script>
import { api } from "../lib/api";

export default {
  data() {
    return {
      studentinfo: [],
    };
  },
  methods: {
    async fetchRolse() {
      let res = await api.call("GET", "/api/auth/me", null);
      if (res.data) {
        this.studentinfo = res.data.student_id;
    }
  },
  },
  beforeMount() {
      this.fetchRolse();
    },
};
</script>
