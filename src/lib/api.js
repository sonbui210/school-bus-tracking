const HOST = "http://localhost:9090";
import { app } from "./app";

import { Loading } from "element-ui";

function call(method, url, body, loading = false) {
  let loadingInstance = loading
    ? Loading.service({
        fullscreen: true,
      })
    : null;
  return new Promise((resolve) => {
    fetch(HOST + url, {
      method: method,
      headers: {
        "Content-Type": "application/json",
        Authorization: "Bearer " + app.local.token,
        "Access-Control-Allow-Origin": "*",
      },
      body: method == "POST" ? JSON.stringify(body) : undefined,
    })
      .then((res) => {
        if (res.status == 401) {
          app.vue.$message({
            showClose: true,
            message: "Phiên đăng nhập đã hết hạn. Vui lòng đăng nhập lại.",
            type: "error",
          });
          window.location.href = "/login";
          return;
        }

        return res.json();
      })
      .then((res) => {
        if (!res) return;
        if (loading) loadingInstance.close();
        if (res.status == "401") {
          window.location.href = "/login";
          return;
        }

        if (res.status == "success") {
          resolve(res);
          return;
        }

        if (res.status == "error") {
          app.vue.$message({
            showClose: true,
            message: res.error.message,
            type: "error",
          });
        }

        if (res.status == 500) {
          app.vue.$message({
            showClose: true,
            message: res.message,
            type: "error",
          });
        }
        resolve(undefined);
      })
      .catch((err) => {
        app.vue.$message({
          showClose: true,
          message: err.message,
          type: "error",
        });

        resolve(undefined);
      });
  });
}

export var api = {
  login: (
    body = {
      username: "",
      password: "",
    }
  ) => call("POST", "/api/auth/login", body),
  me: (body = {}) => call("GET", "/api/auth/me", body),
  call: call,
};
