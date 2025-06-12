import React from "react"; // React 라이브러리 임포트
import { Container, Grid, Typography, TextField, Button } from "@mui/material"; // Material-UI 컴포넌트 임포트
import { Link } from "react-router-dom"; // React Router를 사용한 네비게이션 링크 컴포넌트 임포트
import { signin, socialLogin } from "./service/ApiService"; // 로그인 관련 API 호출 함수 임포트

// 로그인 페이지 컴포넌트 정의
function Login() {
  // 일반 로그인 폼 제출 이벤트 핸들러
  const handleSubmit = (event) => {
    event.preventDefault(); // 기본 폼 제출 동작 방지

    const data = new FormData(event.target); // FormData를 사용하여 폼 데이터 가져오기
    const username = data.get("username"); // username 필드 값 가져오기
    const password = data.get("password"); // password 필드 값 가져오기

    // 로그인 API 호출
    signin({ username: username, password: password });
  };

  // 소셜 로그인 이벤트 핸들러
  const handleSocialLogin = (provider) => {
    socialLogin(provider); // 선택한 소셜 로그인 공급자로 인증 요청 보내기
  };

  return (
    // Material-UI Container를 사용하여 중앙 정렬된 로그인 폼을 제공
    <Container component="main" maxWidth="xs" style={{ marginTop: "8%" }}>
      <Grid container spacing={2}>
        <Grid item xs={12}>
          <Typography component="h1" variant="h5"> {/* 로그인 페이지 제목 */}
            로그인
          </Typography>
        </Grid>
      </Grid>
      
      <form noValidate onSubmit={handleSubmit}> {/* 로그인 폼 */}
        <Grid container spacing={2}> 
          {/* Grid 컨테이너: 내부 요소를 정렬하고 레이아웃을 구성 */}
          {/* spacing={2} 설정으로 내부 Grid 아이템 간 여백을 지정 */}
          <Grid item xs={12}> 
            {/* Grid 아이템: 하나의 행(한 줄) 전체를 차지하도록 설정 (xs={12} → 100% 너비) */}
            <TextField
              variant="outlined"  // 외곽선 스타일의 입력 필드 적용
              required  // 필수 입력 필드로 설정 (값이 없으면 제출 불가능)
              fullWidth  // 입력 필드가 부모 요소의 전체 너비를 차지하도록 설정
              id="username"  // 해당 입력 필드의 HTML ID (고유한 식별자)
              label="아이디"  // 사용자에게 표시될 입력 필드의 라벨
              name="username"  // 폼 데이터 전송 시 사용될 필드 이름 (username)
              autoComplete="username"  // 브라우저 자동 완성 기능 활성화
            /> 
            {/* 아이디 입력 필드: 사용자가 아이디를 입력하는 곳 */}
          </Grid>
          <Grid item xs={12}>
            <TextField
              variant="outlined"
              required
              fullWidth
              name="password"
              label="패스워드"
              type="password"
              id="password"
              autoComplete="current-password"
            /> {/* 비밀번호 입력 필드 */}
          </Grid>
          <Grid item xs={12}>
            <Button type="submit" fullWidth variant="contained" color="primary">
              로그인
            </Button> {/* 로그인 버튼 */}
          </Grid>

          {/* 소셜 로그인 버튼들 */}
          <Grid item xs={12}>
            <Button
              onClick={() => handleSocialLogin("google")}
              fullWidth
              variant="contained"
              style={{ backgroundColor: "#000" }}
            >
              구글로 로그인하기
            </Button>
          </Grid>
          <Grid item xs={12}>
            <Button
              onClick={() => handleSocialLogin("naver")}
              fullWidth
              variant="contained"
              style={{ backgroundColor: "#000" }}
            >
              네이버로 로그인하기
            </Button>
          </Grid>
          <Grid item xs={12}>
            <Button
              onClick={() => handleSocialLogin("kakao")}
              fullWidth
              variant="contained"
              style={{ backgroundColor: "#000" }}
            >
              카카오로 로그인하기
            </Button>
          </Grid>
          <Grid item xs={12}>
            <Button
              onClick={() => handleSocialLogin("github")}
              fullWidth
              variant="contained"
              style={{ backgroundColor: "#000" }}
            >
              깃허브로 로그인하기
            </Button>
          </Grid>

          {/* 회원가입 링크 */}
          <Grid item>
            <Link to="/signup" variant="body2">
              계정이 없습니까? 여기서 가입 하세요.
            </Link>
          </Grid>
        </Grid>
      </form>
    </Container>
  );
}

export default Login; // Login 컴포넌트를 외부에서 사용할 수 있도록 내보내기