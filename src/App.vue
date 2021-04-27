<template>
  <div id="app" v-if="showNavbar()">
    <el-container style="height: 100vh;  border: 1px solid #eee">
      <Navbar />
      <el-container style="width: 100%;">
        <router-view />
      </el-container>
    </el-container>
  </div>
  <div id="app" v-else>
    <router-view />
  </div>
</template>

<script>
import { app } from "./lib/app";
import { api } from "./lib/api";
import { getLocal } from "./lib/storage";
import Navbar from "./components/Navbar";

export default {
  name: "App",
  components: {
    Navbar,
    // HomePage,
  }, 
  methods: {
    async checkAuthorization() {
      if (window.location.href.includes("/login") || window.location.href.includes("/register")) {
        return;
      }
      let res = await api.me();
      if (!res) {
        this.$router.push("/login");
      } else {
        app.user = res.data;
      }
    },
    showNavbar() {
      if (window.location.href.includes("/login") || window.location.href.includes("/register")) return false;
      return true;
    }, 
   },
  created() {
    app.vue = this;
    getLocal();
    this.checkAuthorization();
  }, 
};
</script>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 0px;
  height: 100%;
}
</style>
