import { app } from "./app";

export var getLocal = () => {
  try {
    var local = localStorage.getItem("webapp");
    app.local = JSON.parse(local) || {};
  } catch (e) {
    console.log(e);
  }
};

export var setLocal = ({ token }) => {
  app.local = Object.assign({}, app.local, { token });
  localStorage.setItem("webapp", JSON.stringify(app.local));
};
