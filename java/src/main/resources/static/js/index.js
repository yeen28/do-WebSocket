// 랜덤 채팅방 입장 함수
function joinRandomRoom() {
    // UUID 생성 (간단한 구현)
    const uuid = 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function(c) {
        const r = Math.random() * 16 | 0;
        const v = c == 'x' ? r : (r & 0x3 | 0x8);
        return v.toString(16);
    });
    window.location.href = '/room/' + uuid;
}

// 페이지 로드 시 이벤트 리스너 등록
document.addEventListener('DOMContentLoaded', function() {
    const randomButton = document.getElementById('random-room-btn');
    if (randomButton) {
        randomButton.addEventListener('click', joinRandomRoom);
    }
}); 