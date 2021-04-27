<template>
  <div id="map" style="display: block;"></div>
</template>

<script>
import googleMap from "../lib/googleMap";
import websocket from "../lib/websocket";
import { api } from "../lib/api";

export default {
  props: ["bus"],
  methods: {
    sleep(ms) {
      return new Promise((resolve) => setTimeout(resolve, ms));
    },
    async displayBusRoute() {
      let positionList = [
        { x: 21.027206, y: 105.776667 },
        { x: 21.028731, y: 105.779253 },
        { x: 21.023753794701058, y: 105.77865218257513 },
        { x: 21.022359, y: 105.778987 },
        { x: 21.018846542076524, y: 105.78110908607063 },
        { x: 21.007766, y: 105.790097 },
        { x: 21.01090428054625, y: 105.78775026853079 },
        { x: 20.992178, y: 105.802841 },
        { x: 20.997494, y: 105.812194 },
        { x: 21.005737337662104, y: 105.84340316455577 },
      ];

      for (var i = 0; i < positionList.length; i++) {
        googleMap.displayBusRoute(positionList[i]);
        await this.sleep(3000);
      }
    },
    initWebsocket() {
      websocket.requestConnect((stompClient) => {
        this.messagingSubscription = stompClient.subscribe("/ws-bus/" + this.bus.id, (frame) => {
          var updated = JSON.parse(frame.body);

          if (this.bus.id == updated.busId) {
            let newPosition = { x: updated.lat, y: updated.lng };

            console.log(newPosition);

            googleMap.displayBusRoute(newPosition);
          }
        });
      });
    },
    async fetchBusInfo() {
      let res = await api.call("GET", "/api/bus/findById?busId=" + this.bus.id, null);
      if (res.data) {
        let currentLat = res.data.currentLat;
        let currentLng = res.data.currentLng;

        let position = { x: currentLat, y: currentLng };

        console.log(position);

        googleMap.displayBusRoute(position);
      }
    },
  },
  created() {
    this.initWebsocket();
    googleMap.initMap();

    let route = JSON.parse(this.bus.route);

    let waypointList = [];
    this.bus.waypoint.forEach((wp) => {
      let point = JSON.parse(wp);

      let waypoint = {
        location: point.location,
        stopover: true,
      };

      waypointList.push(waypoint);
    });

    let original = { x: route.original.location.lat, y: route.original.location.lng };
    let destination = { x: route.destination.location.lat, y: route.destination.location.lng };

    setTimeout(() => {
      googleMap.calculateAndDisplayRoute(original, destination, waypointList);
      // this.displayBusRoute();
      this.fetchBusInfo();
    }, 1000);
  },
  beforeDestroy() {
    websocket.disconnect();
  },
};
</script>

<style>
#map {
  height: 100vh;
  /* The height is 400 pixels */
  width: 100%;
  /* The width is the width of the web page */
}
</style>
