// let stompClient = null;
// let roomId = null;

// function setConnected(connected) {
//     $("#connect").prop("disabled", connected);
//     $("#disconnect").prop("disabled", !connected);
//     if (connected) {
//         $("#conversation").show();
//     } else {
//         $("#conversation").hide();
//     }
//     $("#greetings").html("");
// }

// function connect() {
//     roomId = $("#roomId").val(); // Lấy roomId từ input
//     if (!roomId) {
//         alert("Please enter room ID before connecting.");
//         return;
//     }

//     stompClient = new StompJs.Client({
//         brokerURL: 'ws://localhost:8090/gs-guide-websocket'
//     });

//     stompClient.onConnect = (frame) => {
//         setConnected(true);
//         console.log('Connected: ' + frame);

//         // Subscribe vào đúng topic phòng
//         stompClient.subscribe(`/topic/room/${roomId}`, (message) => {
//             showGreeting(JSON.parse(message.body));
//         });
//     };

//     stompClient.onWebSocketError = (error) => {
//         console.error('WebSocket Error: ', error);
//     };

//     stompClient.onStompError = (frame) => {
//         console.error('Broker Error: ' + frame.headers['message']);
//         console.error('Details: ' + frame.body);
//     };

//     stompClient.activate();
// }

// function disconnect() {
//     if (stompClient !== null) {
//         stompClient.deactivate();
//     }
//     setConnected(false);
//     console.log("Disconnected");
// }

// function sendAnswer() {
//     if (!stompClient || !stompClient.connected) {
//         alert("You must connect first!");
//         return;
//     }

//     const answer = $("#answer").val();
//     const idToken = $("#idToken").val(); // Giả sử bạn có input token

//     stompClient.publish({
//         destination: `/app/room/${roomId}/submitAnswer`,
//         body: JSON.stringify({
//             answer: answer,
//             idToken: idToken
//         })
//     });
// }

// function generateKeywords() {
//     if (!stompClient || !stompClient.connected) {
//         alert("You must connect first!");
//         return;
//     }

//     stompClient.publish({
//         destination: `/app/room/${roomId}/genkeywords`,
//         body: ''
//     });
// }

// function getKeyword() {
//     if (!stompClient || !stompClient.connected) {
//         alert("You must connect first!");
//         return;
//     }

//     const wordId = $("#wordId").val();

//     stompClient.publish({
//         destination: `/app/room/${roomId}/getkeyword/${wordId}`,
//         body: ''
//     });
// }

// function showGreeting(message) {
//     if (typeof message === 'string') {
//         $("#greetings").append("<tr><td>" + message + "</td></tr>");
//     } else if (typeof message === 'object' && message.correct !== undefined) {
//         const result = message.correct ? "Correct!" : "Wrong!";
//         $("#greetings").append(`<tr><td>Answer: ${message.answer} - ${result}</td></tr>`);
//     } else {
//         console.log("Unknown message format:", message);
//     }
// }

// $(function () {
//     $("form").on('submit', (e) => e.preventDefault());
//     $("#connect").click(() => connect());
//     $("#disconnect").click(() => disconnect());
//     $("#sendAnswer").click(() => sendAnswer());
//     $("#genKeywords").click(() => generateKeywords());
//     $("#getKeyword").click(() => getKeyword());
// });
