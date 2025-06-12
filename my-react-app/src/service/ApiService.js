import { API_BASE_URL } from "../api-config"; // API의 기본 URL을 가져옴

// API 호출을 위한 함수
export function call(api, method, request) {
  let headers = new Headers({
    "Content-Type": "application/json", // 요청의 본문을 JSON 형식으로 지정
  });

  // 로컬 스토리지에서 액세스 토큰을 가져와, 있으면 헤더에 추가
  const accessToken = localStorage.getItem("ACCESS_TOKEN");
  if (accessToken && accessToken !== null) {
    headers.append("Authorization", "Bearer " + accessToken);
  }

  // 요청 옵션 설정
  let options = {
    headers: headers,
    url: API_BASE_URL + api, // API 엔드포인트 조합
    method: method, // HTTP 메서드 설정 (GET, POST 등)
  };

  // request 객체가 존재하면 요청 본문을 JSON으로 변환하여 추가
  if (request) {
    options.body = JSON.stringify(request);
  }

  // fetch API를 사용하여 서버에 요청
  return fetch(options.url, options)
    .then((response) => {
      if (response.status === 200) {
        return response.json(); // 응답이 정상일 경우 JSON 변환 후 반환
      } else if (response.status === 403) {
        window.location.href = "/login"; // 인증 실패 시 로그인 페이지로 리다이렉트
      } else {
        new Error(response); // 기타 오류 발생 시 예외 생성
      }
    })
    .catch((error) => {
      console.log("http error"); // 네트워크 또는 기타 오류 발생 시 로깅
      console.log(error);
    });
}

// 사용자 로그인 함수
export function signin(userDTO) {
  return call("/auth/signin", "POST", userDTO).then((response) => {
    if (response.token) {
      localStorage.setItem("ACCESS_TOKEN", response.token); // 로그인 성공 시 토큰 저장
      window.location.href = "/"; // 메인 페이지로 이동
    }
  });
}

// 사용자 로그아웃 함수
export function signout() {
  localStorage.setItem("ACCESS_TOKEN", null); // 토큰을 제거하여 로그아웃 처리
  window.location.href = "/login"; // 로그인 페이지로 이동
}

// 사용자 회원가입 함수
export function signup(userDTO) {
  return call("/auth/signup", "POST", userDTO); // 회원가입 API 호출
}

// 소셜 로그인 함수
export function socialLogin(provider) {
  // 현재 프론트엔드 URL을 동적으로 가져옴
  const frontendUrl = window.location.protocol + "//" + window.location.host;
  console.log("frontendUrl = " + frontendUrl);

  // 선택한 소셜 로그인 제공자를 이용한 인증 요청을 보냄
  window.location.href =
    API_BASE_URL +
    "/oauth2/authorization/" +
    provider +
    "?redirect_url=" +
    frontendUrl; // 로그인 후 리디렉트할 URL 설정
}