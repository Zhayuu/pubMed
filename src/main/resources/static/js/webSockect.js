import {back} from "./script.js";
import {showResponse} from "./script.js";

let socket, content;

if (!window.WebSocket) {
    window.WebSocket = window.MozWebSocket;
}

if (window.WebSocket) {
    socket = new WebSocket("ws://127.0.0.1:8888/ws");
    console.log(socket);
} else {
    alert("Not support WebSocket");
}

socket.onopen = open;

socket.onmessage = message;

socket.onclose = close;

socket.onerror = error;


function message(event) {

    content = event.data;
    // let data = event.data;
    // content = JSON.parse(data);
   back(content);
    content = content.replace(/\n/g,"<br/>")
    showResponse(content);
}

function open() {
    console.log("WebSocket Connected!");
}

function close() {
    console.log("WebSocket Closed!");
}

function error(event) {
    console.log("Error: " + event.data);
}

function socket_send(message) {
    if (!window.WebSocket) {
        return;
    }
    if (socket.readyState === WebSocket.OPEN) {
        console.log(JSON.stringify(message));
        socket.send(JSON.stringify(message));
        return true;
    } else {
        alert("WebSocket didn't connect!");
    }
}
export { socket, socket_send };
export {content};