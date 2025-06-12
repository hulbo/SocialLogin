import "./App.css"; // 스타일 시트(App.css) 임포트
import Todo from "./Todo"; // 할 일(Todo) 컴포넌트 임포트
import React, { useState, useEffect } from "react"; // React 및 훅(useState, useEffect) 임포트
import {
  Container, // UI 컨테이너
  List, // 리스트 컴포넌트
  Paper, // 카드 형태의 UI 요소
  Grid, // 반응형 레이아웃 제공
  Button, // 버튼 컴포넌트
  AppBar, // 상단 내비게이션 바
  Toolbar, // 내비게이션 바 내부 레이아웃
  Typography, // 텍스트 스타일링
} from "@mui/material";
import AddTodo from "./AddTodo"; // 할 일 추가(AddTodo) 컴포넌트 임포트
import { call, signout } from "./service/ApiService"; // API 호출 및 로그아웃 기능 임포트

// 메인 컴포넌트(App) 정의
function App() {
  // 할 일 목록 상태 관리
  const [items, setItems] = useState([]);
  // 로딩 상태 관리 (초기값: true)
  const [loading, setLoading] = useState(true);

  // 컴포넌트가 처음 렌더링될 때 서버에서 할 일 목록 가져오기
  useEffect(() => {
    call("/todo", "GET", null)
      .then((response) => {
        setItems(response.data); // API 응답 데이터로 할 일 목록 업데이트
      })
      .catch((error) => {
        console.error("Error fetching todo items:", error); // 오류 발생 시 콘솔 출력
      })
      .finally(() => {
        setLoading(false); // API 응답 후 로딩 상태 해제
      });
  }, []);

  // 새로운 할 일 추가 함수
  const addItem = (item) => {
    call("/todo", "POST", item).then((response) => setItems(response.data)); // 서버에 새 할 일 추가 후 목록 업데이트
  };

  // 기존 할 일 수정 함수
  const editItem = (item) => {
    call("/todo", "PUT", item).then((response) => setItems(response.data)); // 서버에서 할 일 업데이트 후 목록 반영
  };

  // 할 일 삭제 함수
  const deleteItem = (item) => {
    call("/todo", "DELETE", item).then((response) => setItems(response.data)); // 서버에서 할 일 삭제 후 목록 갱신
  };

  // 할 일 목록 렌더링 (할 일이 있을 경우만 표시)
  let todoItems = items.length > 0 && (
    <Paper style={{ margin: 16 }}> {/* 카드 형태 UI 적용 */}
      <List>
        {items.map((item) => ( /* 할 일 목록을 순회하며 Todo 컴포넌트 생성 */
          <Todo
            item={item}
            key={item.id} /* 각 아이템의 고유 ID 사용 */
            editItem={editItem}
            deleteItem={deleteItem}
          />
        ))}
      </List>
    </Paper>
  );

  // 내비게이션 바 (로그아웃 버튼 포함)
  let navigationBar = (
    <AppBar position="static"> {/* 고정된 내비게이션 바 */}
      <Toolbar>
        <Grid justifyContent="space-between" container>
          <Grid item>
            <Typography variant="h6">오늘의 할일</Typography> {/* 내비게이션 타이틀 */}
          </Grid>
          <Grid item>
            <Button color="inherit" raised onClick={signout}> {/* 로그아웃 버튼 */}
              로그아웃
            </Button>
          </Grid>
        </Grid>
      </Toolbar>
    </AppBar>
  );

  // 할 일 목록 페이지 UI 구성
  let todoListPage = (
    <div>
      {navigationBar} {/* 내비게이션 바 포함 */}
      <Container maxWidth="md"> {/* 중앙 정렬된 컨테이너 */}
        <AddTodo addItem={addItem} /> {/* 할 일 추가 폼 */}
        <div className="TodoList">{todoItems}</div> {/* 할 일 목록 표시 */}
      </Container>
    </div>
  );

  // 로딩 화면
  let loadingPage = <h1> 로딩중.. </h1>;
  let content = loadingPage; // 기본적으로 로딩 화면을 표시

  // 로딩이 끝나면 할 일 목록 페이지를 표시
  if (!loading) {
    content = todoListPage;
  }

  // 최종적으로 렌더링될 화면 반환
  return <div className="App">{content}</div>;
}

export default App; // App 컴포넌트 내보내기