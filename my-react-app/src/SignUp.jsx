import React from "react"; // React 라이브러리 임포트
import { Container, Grid, Typography, TextField, Button } from "@mui/material"; // Material-UI의 컴포넌트 임포트
import { signup } from "./service/ApiService"; // API 요청을 처리하는 signup 함수 임포트
import { Link } from "react-router-dom"; // React Router를 사용하여 페이지 이동을 위한 Link 컴포넌트 임포트

// 회원가입 페이지 컴포넌트 정의
function SignUp() {
  // 폼 제출 이벤트 핸들러
  const handleSubmit = (event) => {
    event.preventDefault(); // 기본 제출 동작 방지

    const data = new FormData(event.target); // 폼 데이터 객체 생성
    const username = data.get("username"); // 입력된 username 가져오기
    const password = data.get("password"); // 입력된 password 가져오기

    // signup API 호출 및 응답 처리
    signup({ username: username, password: password }).then((response) => {
      window.location.href = "/login"; // 회원가입 후 로그인 페이지로 이동
    });
  };

  return (
    // Material-UI의 Container를 사용하여 중앙 정렬된 폼 생성
    <Container component="main" maxWidth="xs" style={{ marginTop: "8%" }}>
      <form noValidate onSubmit={handleSubmit}> {/* 폼 제출 시 handleSubmit 호출 */}
        <Grid container spacing={2}> {/* 폼 입력 필드들을 위한 Grid 컨테이너 */}
          <Grid item xs={12}>
            <Typography component="h1" variant="h5"> {/* 제목 표시 */}
              계정 생성
            </Typography>
          </Grid>
          <Grid item xs={12}>
            <TextField
              autoComplete="fname"
              name="username"
              variant="outlined"
              required
              fullWidth
              id="username"
              label="아이디"
              autoFocus
            /> {/* 아이디 입력 필드 */}
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
              계정 생성
            </Button> {/* 회원가입 버튼 */}
          </Grid>
          <Grid item xs={12}>
            <Link to="/login" variant="body2">
              이미 계정이 있습니까? 로그인 하세요.
            </Link> {/* 로그인 페이지로 이동하는 링크 */}
          </Grid>
        </Grid>
      </form>
    </Container>
  );
}

export default SignUp; // SignUp 컴포넌트를 외부에서 사용할 수 있도록 내보내기