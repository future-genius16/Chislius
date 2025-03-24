document.addEventListener("DOMContentLoaded", function (options) {
    const connectButton = document.getElementById("connectButton");
    connectButton.onclick = connect;

    const connectPublicButton = document.getElementById("connectPublicButton");
    connectPublicButton.onclick = connectPublic;

    const createPrivateButton = document.getElementById("createPrivateButton");
    createPrivateButton.onclick = createPrivate;

    const connectPrivateButton = document.getElementById("connectPrivateButton");
    connectPrivateButton.onclick = connectPrivate;

    let stompClient = null;
    let username = null;
    let code = null;
    let rooms = null;

    function connect(options) {
        username = document.getElementById('usernameInput').value;
        document.getElementById("username").innerHTML = username;

        const socket = new SockJS('/ws');
        stompClient = Stomp.over(socket);

        stompClient.connect({}, function (frame) {
            console.log('Connected to the server: ' + frame);

            document.getElementById('login').style.display = 'none';

            stompClient.subscribe('/user/' + username + '/queue/code', function (message) {
                const data = JSON.parse(message.body);
                console.log('Код комнаты получен:', data.code);
                if (data.code != null){
                    document.getElementById("code").innerHTML = data.code;
                }
            });

            stompClient.subscribe('/user/' + username + '/errors', function (message) {
                var errorData = JSON.parse(message.body);
                console.error('Ошибка:', errorData.message);
            });

            stompClient.subscribe('/topic/rooms-updates', function (message) {
                rooms = JSON.parse(message.body);
                console.log('Received rooms: ', rooms);
                const infoDiv = document.getElementById('rooms');
                infoDiv.innerHTML = `<p> ${JSON.stringify(rooms)} </p>`;

            });

            sendMessage('/app/connect', null);
        });
    }

    function connectPublic() {
        if (stompClient) {
            sendMessage('/app/connect-public', {username: username});
        }
    }

    function createPrivate() {
        if (stompClient) {
            sendMessage('/app/create-private', {username: username});
        }
    }

    function connectPrivate() {
        if (stompClient) {
            code = document.getElementById('codeInput').value;
            sendMessage('/app/connect-private',{username: username, code: code});
        }
    }

    // Функция для отправки сообщения на сервер
    function sendMessage(destination, body) {
        stompClient.send(destination, {}, JSON.stringify(body));
    }
});
