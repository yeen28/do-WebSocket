// 전역 변수
let stompClient = null;
let username = null;
let roomId = null;

// 페이지 로드 시 초기화
document.addEventListener('DOMContentLoaded', function() {
    // Thymeleaf에서 전달된 roomId 값 가져오기
    roomId = document.getElementById('roomIdValue').value;
    
    // 이벤트 리스너 등록
    document.getElementById('usernameForm').addEventListener('submit', connect);
    document.getElementById('messageForm').addEventListener('submit', sendMessage);
});

// WebSocket 연결 함수
function connect(event) {
    if (event) {
        event.preventDefault();
    }
    
    username = document.getElementById('username').value.trim();
    if (username) {
        document.getElementById('username-page').style.display = 'none';
        document.getElementById('chat-page').style.display = 'block';
        
        const socket = new SockJS('/ws');
        stompClient = Stomp.over(socket);
        
        stompClient.connect({}, onConnected, onError);
    }
}

// 연결 성공 시 호출되는 함수
function onConnected() {
    // 채팅방 구독
    stompClient.subscribe('/topic/room.' + roomId, onMessageReceived);
    
    // 사용자 입장 알림
    stompClient.send("/app/chat.addUser", 
        {}, 
        JSON.stringify({
            sender: username,
            type: 'JOIN',
            room: roomId
        })
    );
    
    document.getElementById('room-name').textContent = '채팅방 ID: ' + roomId;
}

// 연결 오류 시 호출되는 함수
function onError(error) {
    console.error('연결 오류:', error);
    alert('서버에 연결할 수 없습니다. 잠시 후 다시 시도해주세요.');
}

// 메시지 전송 함수
function sendMessage(event) {
    event.preventDefault();
    
    const messageInput = document.getElementById('message');
    const messageContent = messageInput.value.trim();
    
    if (messageContent && stompClient) {
        const chatMessage = {
            sender: username,
            content: messageContent,
            type: 'CHAT',
            room: roomId
        };
        
        stompClient.send("/app/chat.sendMessage", {}, JSON.stringify(chatMessage));
        messageInput.value = '';
    }
}

// 메시지 수신 시 호출되는 함수
function onMessageReceived(payload) {
    const message = JSON.parse(payload.body);
    const messageArea = document.getElementById('messageArea');
    
    const messageElement = document.createElement('div');
    
    if (message.type === 'JOIN' || message.type === 'LEAVE') {
        messageElement.classList.add('system-message');
        messageElement.textContent = message.content;
    } else {
        messageElement.classList.add('message');
        
        if (message.sender === username) {
            messageElement.classList.add('sent');
        } else {
            messageElement.classList.add('received');
            
            const senderElement = document.createElement('div');
            senderElement.classList.add('sender');
            senderElement.textContent = message.sender;
            messageElement.appendChild(senderElement);
        }
        
        const textElement = document.createElement('div');
        textElement.textContent = message.content;
        messageElement.appendChild(textElement);
        
        const timestampElement = document.createElement('div');
        timestampElement.classList.add('timestamp');
        
        // 타임스탬프 표시 형식 변경
        const timestamp = new Date(message.timestamp);
        const formattedTime = timestamp.toLocaleTimeString([], {hour: '2-digit', minute:'2-digit'});
        
        timestampElement.textContent = formattedTime;
        messageElement.appendChild(timestampElement);
    }
    
    messageArea.appendChild(messageElement);
    messageArea.scrollTop = messageArea.scrollHeight;
} 