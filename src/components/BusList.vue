<template>
  <div style="width: 100%; margin: 16px;">
    <el-row>
      <el-col>
        <el-input style="width: 55%; float: left;" placeholder="Nhập mã xe bus để tìm kiếm..." v-model="searchKey"></el-input>
        <el-button type="primary" style="float: left; margin-left: 16px;" icon="el-icon-search" @click="searchBus">Tìm kiếm</el-button>
      </el-col>
    </el-row>
    <el-row :gutter="30" style="margin-top: 32px; margin-right: 16px">
      <el-col :xs="20" :sm="20" :md="6" :lg="6" :xl="6" v-for="(bus, index) in busList" :key="index" style="margin-bottom: 16px;">
        <el-badge :value="bus.code" class="item" style="width: 100%;">
          <el-card :body-style="{ padding: '0px', height: '350px', width: '100%' }">
            <img src="/images/bus.jpg" width="200px" height="200px" class="image" style="margin-top: 16px;" />
            <div style="padding: 14px;">
              <span>{{ getBusOriginalPlace(bus) }} - {{ getBusDestinationPlace(bus) }}</span>
              <div style="position: absolute; bottom: 0; left: 35%;">
                <time class="time">{{ bus.licensePlate }}</time> <br />
                <el-button type="text" class="button" @click="selectBus(bus)">Xem bản đồ</el-button>
              </div>
            </div>
          </el-card>
        </el-badge>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { api } from "../lib/api";

export default {
  data() {
    return {
      busList: [],
      searchKey: "",
    };
  },
  methods: {
    selectBus(bus) {
      this.$emit("busSelected", bus);
    },
    async fetchBusList() {
      let res = await api.call("GET", "/api/bus", null);
      if (res.data) {
        this.busList = res.data;
      }
    },
    getBusOriginalPlace(bus) {
      let route = JSON.parse(bus.route);
      //let original = route.original;
      //return original.name.split(",")[0];
      //let originalBus = route.substring(route.search("\"name\":\""),route.search(","));
      return route[0];
    },
    getBusDestinationPlace(bus) {
      let route = JSON.parse(bus.route);
      //let destination = route.destination;
      //return destination.name.split(",")[0];
      //let destinationBus = route.substring(route.search("\"name\":\""),route.search(","));
      return route[0];
    },
    async searchBus() {
      if (this.searchKey) {
        let res = await api.call("GET", "/api/bus/search?searchKey=" + this.searchKey, null);
        if (res.data) {
          this.busList = res.data;
        } else {
          this.alert("error", "Không tìm thấy xe!");
        }
      } else {
        this.fetchBusList();
      }
    },
  },
  beforeMount() {
    this.fetchBusList();
  },
};
</script>
