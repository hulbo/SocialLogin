import React from "react"; // React 라이브러리 임포트
import { Navigate } from "react-router-dom"; // React Router의 Navigate 컴포넌트 임포트 (페이지 이동 처리)

// 소셜 로그인 후 리디렉션 처리 컴포넌트
const SocialLogin = (props) => {
  // URL에서 특정 파라미터 값을 가져오는 함수
  const getUrlParameter = (name) => {
    let search = window.location.search; // 현재 URL의 쿼리 문자열 가져오기
    let params = new URLSearchParams(search); // URLSearchParams 객체를 이용하여 파라미터 분석
    return params.get(name); // 지정된 파라미터 값 반환
  };

  // URL에서 "token" 파라미터 값 가져오기 (OAuth 인증 후 서버에서 전달됨)
  const token = getUrlParameter("token");

  console.log("토큰 파싱: " + token); // 콘솔에 토큰 값 출력 (디버깅용)

  if (token) {
    console.log("로컬스토리지에 토큰 저장" + token);
    localStorage.setItem("ACCESS_TOKEN", token); // 로컬스토리지에 액세스 토큰 저장

    return (
      <Navigate
        to={{
          pathname: "/", // 로그인 성공 시 홈 페이지("/")로 이동
          state: { from: props.location }, // 이전 위치 정보 저장 (필요한 경우 활용 가능)
        }}
      />
    );
  } else {
    return (
      <Navigate
        to={{
          pathname: "/login", // 토큰이 없으면 로그인 페이지로 이동
          state: { from: props.location }, // 이전 위치 정보 저장
        }}
      />
    );
  }
};

export default SocialLogin; // 해당 컴포넌트를 외부에서 사용할 수 있도록 export