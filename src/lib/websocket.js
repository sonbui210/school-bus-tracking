import SockJS from "sockjs-client";
import * as StompJs from "@stomp/stompjs";

var stompClient = null;
var connecting = false;

const connect = function(callback) {
  connecting = true;
  //FOR_RELEASE
  const client = new StompJs.Client({
    brokerURL: "ws://dataschoolbus.herokuapp.com/websocket",
    debug: function(str) {
      console.log(str);
    },
    reconnectDelay: 5000,
    heartbeatIncoming: 4000,
    heartbeatOutgoing: 4000,
  });

  client.debug = (s) => {
    console.log(s);
  };

  //Case of wss not work, use SockJs
  // if (typeof WebSocket !== "function") {
  client.webSocketFactory = function() {
    // Note that the URL is different from the WebSocket URL
    return new SockJS("https://dataschoolbus.herokuapp.com/websocket");
  };
  // }

  client.onConnect = function() {
    if (callback) callback(client);
    // Do something, all subscribes must be done is this callback
    // This is needed because this will be executed after a (re)connect
  };

  client.onStompError = function(frame) {
    // Will be invoked in case of error encountered at Broker
    // Bad login/passcode typically will cause an error
    // Complaint brokers will set `message` header with a brief message. Body may contain details.
    // Compliant brokers will terminate the connection after any error
    console.log("Broker reported error: " + frame.headers["message"]);
    console.log("Additional details: " + frame.body);
  };

  client.activate();
};

const disconnect = function() {
  connecting = false;
  if (stompClient !== null) {
    stompClient.disconnect();
  }
};

const isConnected = function() {
  return stompClient !== null && stompClient.connected;
};

const requestConnect = function(callback) {
  if (!isConnected()) {
    if (!connecting) {
      connect(callback);
    } else {
      //connect affter 1s
      setTimeout(() => {
        requestConnect(callback);
      }, 1000);
    }
  } else {
    callback(stompClient);
  }
};

export default {
  connect,
  disconnect,
  isConnected,
  requestConnect,
  stompClient,
};
