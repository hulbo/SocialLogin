// 백엔드 서버 주소
let backendHost;

// 현재 브라우저의 호스트 이름 가져옴
const hostname = window && window.location && window.location.hostname;

// 개발 환경 처리 변경
if (hostname === "localhost") {
  backendHost = "http://localhost:8080";
}

export const API_BASE_URL = `${backendHost}`;
