<template>
  <div>
    <el-dialog :title="title" :visible.sync="busDialogVisible" :close-on-click-modal="false" top="10px">
      <el-form>
        <el-form-item label="Mã xe">
          <el-input v-model="bus.code" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="Biển số">
          <el-input v-model="bus.licensePlate" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="Điểm đón">
          <el-select ref="originalSearchMachine" style="width: 100%;" v-model="original" filterable clearable remote reserve-keyword placeholder="Gõ để tìm kiếm" :remote-method="searchOriginal">
            <el-option v-for="(item, index) in originalPlaceList" :key="index" :label="item.name" :value="item"> </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="Điểm dừng">
          <el-select ref="destinationSearchMachine" style="width: 100%;" v-model="destination" filterable clearable remote reserve-keyword placeholder="Gõ để tìm kiếm" :remote-method="searchDestination">
            <el-option v-for="(item, index) in destinationPlaceList" :key="index" :label="item.name" :value="item"> </el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <label style="float: left">Danh sách trạm nghỉ</label> <br />

          <el-select ref="destinationSearchMachine" style="float: left; width: 60%;" v-model="newWayPoint" filterable clearable remote reserve-keyword placeholder="Gõ để tìm kiếm" :remote-method="searchWayPoint">
            <el-option v-for="(item, index) in wayPointList" :key="index" :label="item.name" :value="item"> </el-option>
          </el-select>
          <el-button type="primary" icon="el-icon-circle-plus" style="float: left; margin-left: 16px;" @click="addNewWayPoint">Thêm</el-button>
        </el-form-item>

        <div style="display: flex; align-items: center; flex-wrap: wrap;">
          <el-tag v-for="(waypoint, index) in bus.waypoint" :key="index" closable type="success" style="margin-right: 8px; margin-bottom: 8px;" @close="removeWaypoint(waypoint)">
            {{ waypoint.name }}
          </el-tag>
        </div>

        <el-form-item label="Tài xế">
          <el-input v-model="bus.driver" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="Số điện thoại liên hệ">
          <el-input v-model="bus.phone" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button type="danger" @click="busDialogVisible = false">Huỷ</el-button>
        <el-button v-if="bus.id" type="primary" @click="addNewBus()">Sửa</el-button>
        <el-button v-else type="primary" @click="addNewBus()">Thêm</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import { api } from "../lib/api";

export default {
  props: ["title"],
  watch: {
    bus: function() {
      if (this.bus.route.original) {
        this.originalPlaceList.push(this.bus.route.original);
        this.original = this.bus.route.original;
      } else {
        this.original = {};
        this.originalPlaceList = [];
        this.$refs.originalSearchMachine.cachedOptions = [];
      }

      if (this.bus.route.destination) {
        this.destinationPlaceList.push(this.bus.route.destination);
        this.destination = this.bus.route.destination;
      } else {
        this.destination = {};
        this.destinationPlaceList = [];
        this.$refs.destinationSearchMachine.cachedOptions = [];
      }

      this.bus.waypoint = this.bus.waypoint.map((wp) => JSON.parse(wp));
    },
  },
  data() {
    return {
      busDialogVisible: false,
      bus: {
        id: 0,
        licensePlate: "",
        route: {},
        code: "",
        driver: "",
        phone: "",
        waypoint: [],
      },
      originalPlaceList: [],
      destinationPlaceList: [],
      original: {},
      destination: {},
      newWayPoint: {},
      wayPointList: [],
    };
  },
  methods: {
    showDialog() {
      this.busDialogVisible = true;
    },
    setBus(bus) {
      this.bus = bus;
    },
    async searchOriginal(query) {
      let result = await api.call("GET", "/api/bus/searchPlace?query=" + query);
      if (result.data.status == "OK") {
        this.originalPlaceList = result.items.title.map((p) => {
          return {
            name: p.formatted_address,
            location: p.geometry.location,
            icon: p.icon,
            id: p.place_id,
          };
        });
      }
    },
    async searchDestination(query) {
      let result = await api.call("GET", "/api/bus/searchPlace?query=" + query);
      if (result.data.status == "OK") {
        this.destinationPlaceList = result.items.title.map((p) => {
          return {
            name: p.formatted_address,
            location: p.geometry.location,
            icon: p.icon,
            id: p.place_id,
          };
        });
      }
    },
    async searchWayPoint(query) {
      let result = await api.call("GET", "/api/bus/searchPlace?query=" + query);
      if (result.data.status == "OK") {
        this.wayPointList = result.items.title.map((p) => {
          return {
            name: p.formatted_address,
            location: p.geometry.location,
            icon: p.icon,
            id: p.place_id,
          };
        });
      }
    },
    async addNewBus() {
      this.bus.route = { original: this.original, destination: this.destination };
      this.bus.waypoint = this.bus.waypoint.map((wp) => JSON.stringify(wp));

      let response = await api.call("POST", "/api/bus/addNewBus", this.bus);
      if (!response) return;
      if (response.data) {
        this.bus = {
          id: 0,
          licensePlate: "",
          route: {},
          code: "",
          driver: "",
          phone: "",
          waypoint: [],
        };
        this.alert("success", "Thêm thành công!");
        this.$emit("addedNewBus", response.data);
        this.busDialogVisible = false;
      }
    },
    addNewWayPoint() {
      this.bus.waypoint.push(this.newWayPoint);
      this.newWayPoint = {};
    },
    removeWaypoint(wp) {
      this.bus.waypoint.splice(this.bus.waypoint.indexOf(wp), 1);
    },
  },
};
</script>
