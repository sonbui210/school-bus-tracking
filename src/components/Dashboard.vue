<template>
  <div style="width: 100%;">
    <GoogleMap :key="busKey" v-if="showMap" :bus="selectedBus" />
    <BusList @busSelected="busSelected" v-else />
  </div>
</template>

<script>
import GoogleMap from "./GoogleMap";
import BusList from "./BusList";
export default {
  components: {
    GoogleMap,
    BusList,
  },
  computed: {
    showMap() {
      return this.selectedBus != null;
    },
  },
  data() {
    return {
      selectedBus: null,
      busKey: 0,
    };
  },
  methods: {
    busSelected(bus) {
      this.selectedBus = bus;

      this.busKey += 1;
    },
  },
};
</script>
