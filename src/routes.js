import Vue from "vue";
import VueRouter from "vue-router";
import HomePage from "./components/HomePage";
import LoginPage from "./LoginPage";
import Student from "./views/Student";
import BusManagement from "./components/BusManagement";
import Register from "./Register";
import Account from "./views/Account";
import TrackingLog from "./components/TrackingLog";
import User from "./views/User";

Vue.use(VueRouter);

export const router = new VueRouter({
  mode: "history",
  linkExactActiveClass: "active",
  routes: [
    {
      path: "/",
      component: HomePage,
    },
    {
      path: "/login",
      component: LoginPage,
    },
    {
      path: "/register",
      component: Register,
    },
    {
      path: "/student",
      component: Student,
    },
    {
      path: "/bus",
      component: BusManagement,
    },
    {
      path: "/account",
      component: Account,
    },
    {
      path: "/log",
      component: TrackingLog,
    },
    {
      path: "/user",
      component: User,
    },
  ],
});
