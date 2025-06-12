import React from "react"; // React 라이브러리 임포트
import "./index.css"; // 글로벌 스타일 시트 임포트
import App from "./App"; // 메인 페이지 컴포넌트 임포트
import Login from "./Login"; // 로그인 페이지 컴포넌트 임포트
import SignUp from "./SignUp"; // 회원가입 페이지 컴포넌트 임포트
import { BrowserRouter, Routes, Route } from "react-router-dom"; // 라우팅을 위한 구성요소 임포트
import { Typography, Box } from "@mui/material"; // (현재 사용되지는 않지만) UI 컴포넌트 임포트
import SocialLogin from "./SocialLogin"; // 소셜 로그인 처리용 컴포넌트 임포트

// AppRouter 컴포넌트 정의: 애플리케이션의 라우팅 구조 정의
function AppRouter() {
  return (
    <div>
      <BrowserRouter> {/* 브라우저 라우터로 전체 라우팅 기능 감싸기 */}
        <Routes>
          {/* URL 경로에 따라 연결할 컴포넌트 지정 */}
          <Route path="/" element={<App />} /> {/* 루트 경로 → 메인 앱 페이지 */}
          <Route path="login" element={<Login />} /> {/* /login 경로 → 로그인 페이지 */}
          <Route path="signup" element={<SignUp />} /> {/* /signup 경로 → 회원가입 페이지 */}
          <Route path="sociallogin" element={<SocialLogin />} /> {/* /sociallogin 경로 → 소셜 로그인 리디렉션 처리 */}
        </Routes>
      </BrowserRouter>
    </div>
  );
}

export default AppRouter; // AppRouter 컴포넌트를 외부에서 사용할 수 있도록 export