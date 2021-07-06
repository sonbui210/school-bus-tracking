<template>
  <div style="padding: 16px; display: flex; flex-flow: column; align-items: flex-start">
    <BusModal @addedNewBus="fetchAllBusList()" ref="busModal" :title="busModalTitle" />

    <div style="width: 100%;">
      <h4 style="float: left">Danh sách xe Bus</h4>
      <el-button v-if="busRoles[0].authority === 'ADMIN'" type="primary" style="float: right" icon="el-icon-circle-plus" @click="addOrEditBus('add', newBus)">Thêm xe bus</el-button>
    </div>

    <el-table :data="busList" style="width: 100%" height="500">
      <el-table-column fixed prop="code" label="Mã xe" width="100"> </el-table-column>
      <el-table-column fixed prop="licensePlate" label="Biển số" width="120" align="center"> </el-table-column>
      <el-table-column prop="route" label="Tuyến đường" width="250">
        <template slot-scope="scope">
          <span style="word-break: break-word"><b>Điểm đầu:</b> {{ scope.row.route.original.name }}</span> <br />
          <span style="word-break: break-word"><b>Điểm cuối:</b> {{ scope.row.route.destination.name }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="status" label="Trạng thái" width="130"> </el-table-column>
      <el-table-column prop="driver" label="Tài xế" width="180"> </el-table-column>
      <el-table-column prop="phone" label="Điện thoại liên hệ" width="200" align="center"> </el-table-column>
      <el-table-column v-if="busRoles[0].authority === 'ADMIN'" fixed="right" label="Thao tác" width="120">
        <template slot-scope="scope">
          <el-button type="text" size="small" @click="addOrEditBus('edit', scope.row)">Sửa</el-button>
          <el-button type="text" size="small" @click="deleteBus(scope.row)">Xoá</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
import { api } from "../lib/api";

export default {
  components: {
    BusModal: () => import("./BusModal"),
  },
  data() {
    return {
      busRoles: [],
      busList: [],
      busModalTitle: "Thêm mới xe Bus",
      newBus: {
        id: 0,
        licensePlate: "",
        route: "",
        code: "",
        driver: "",
        phone: "",
        waypoint: [],
      },
    };
  },
  methods: {
    async fetchRolse() {
      let res = await api.call("GET", "/api/auth/me", null);
      if (res.data) {
        this.busRoles = res.data.roles.map((b) =>{
          return{
            authority: b.authority,
          }
        });
        
      }
    },
    addOrEditBus(type, bus) {
      if (type == "add") {
        this.busModalTitle = "Thêm mới xe Bus";
      } else {
        this.busModalTitle = "Chỉnh sửa thông tin xe Bus";
      }

      this.$refs.busModal.showDialog();
      this.$refs.busModal.setBus(bus);
    },
    async fetchAllBusList() {
      let response = await api.call("GET", "/api/bus", null);
      if (response.data) {
        this.busList = response.data.map((b) => {
          return {
            code: b.code,
            id: b.id,
            driver: b.driver,
            licensePlate: b.licensePlate,
            phone: b.phone,
            route: JSON.parse(b.route),
            status: b.status,
            waypoint: b.waypoint,
          };
        });
      }
    },
    async deleteBus(bus) {
      let response = await api.call("GET", "/api/bus/deleteById?busId=" + bus.id);
      if (response.data) {
        this.alert("success", "Xoá thành công!");
        this.fetchAllBusList();
      }
    },
  },
  beforeMount() {
    this.fetchAllBusList();
    this.fetchRolse();
  },
};
</script>
