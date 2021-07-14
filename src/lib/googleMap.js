/* eslint-disable */
import { app } from "./app";

var initMap = function() {
  // Create the script tag, set the appropriate attributes
  var script = document.createElement("script");
  script.src = "https://maps.googleapis.com/maps/api/js?key=AIzaSyBvrWzYjC5SmROtfLWwprBmuj2RiQwTqDI&region=VN&language=vn&callback=initMap";
  script.defer = true;

  // Attach your callback function to the `window` object
  window.initMap = function() {
    // JS API is loaded and available
    const infoWindow = new google.maps.InfoWindow();
    const directionsService = new google.maps.DirectionsService();
    const directionsRenderer = new google.maps.DirectionsRenderer();
    // The location of Uluru
    const position = { lat: -25.344, lng: 131.036 };
    // The map, centered at Uluru
    // eslint-disable-next-line no-undef
    const map = new google.maps.Map(document.getElementById("map"), {
      zoom: 16,
      center: position,
    });

    app.map = {
      map: map,
      directionsService: directionsService,
      directionsRenderer: directionsRenderer,
    };

    directionsRenderer.setMap(map);
  };

  // Append the 'script' element to 'head'
  document.head.appendChild(script);
};

function calculateAndDisplayRoute(original, destination, waypointList) {
  app.map.directionsService.route(
    {
      origin: { lat: original.x, lng: original.y },
      destination: { lat: destination.x, lng: destination.y },
      waypoints: waypointList,
      travelMode: google.maps.TravelMode.DRIVING,
    },
    (response, status) => {
      if (status === "OK") {
        app.map.directionsRenderer.setDirections(response);
      } else {
        window.alert("Directions request failed due to " + status);
      }
    }
  );
}

function getCurrentPosition(infoWindow, map) {
  if (navigator.geolocation) {
    navigator.geolocation.getCurrentPosition(
      (position) => {
        const pos = {
          lat: position.coords.latitude,
          lng: position.coords.longitude,
        };
        infoWindow.setPosition(pos);
        infoWindow.setContent("Location found.");
        infoWindow.open(map);
        map.setCenter(pos);

        const marker = new google.maps.Marker({
          position: pos,
          map: map,
        });
      },
      () => {
        handleLocationError(true, infoWindow, map.getCenter());
      }
    );
  } else {
    // Browser doesn't support Geolocation
    handleLocationError(false, infoWindow, map.getCenter());
  }
}

function displayBusRoute(position) {
  var icon = {
    url: "/images/bus-icon.png",
    scaledSize: new google.maps.Size(32, 32),
  };

  if (app.map.marker) {
    app.map.marker.setMap(null);
  }

  app.map.marker = new google.maps.Marker({
    position: { lat: position.x, lng: position.y },
    map: app.map.map,
    icon: icon,
    title: "Xe bus 03",
  });
}

function handleLocationError(browserHasGeolocation, infoWindow, pos) {
  infoWindow.setPosition(pos);
  infoWindow.setContent(browserHasGeolocation ? "Error: The Geolocation service failed." : "Error: Your browser doesn't support geolocation.");
  infoWindow.open(map);
}

export default {
  initMap: initMap,
  calculateAndDisplayRoute: calculateAndDisplayRoute,
  displayBusRoute: displayBusRoute,
};
