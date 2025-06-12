import React, { useState } from "react"; // React 및 useState 훅을 임포트하여 상태 관리

import { Button, Grid, TextField } from "@mui/material"; // Material-UI의 UI 컴포넌트 임포트

// 할 일을 추가하는 컴포넌트
const AddTodo = (props) => {
  // useState를 사용하여 상태(item) 초기화 (title 값만 포함)
  const [item, setItem] = useState({ title: "" }); 
  const addItem = props.addItem; // 부모 컴포넌트에서 전달된 addItem 함수 가져오기

  // 버튼 클릭 시 할 일 추가 함수
  const onButtonClick = () => {
    addItem(item); // 입력된 할 일을 부모 컴포넌트로 전달하여 추가
    setItem({ title: "" }); // 입력 필드 초기화
  };

  // 입력 필드 변경 이벤트 핸들러 (값을 실시간으로 업데이트)
  const onInputChange = (e) => {
    setItem({ title: e.target.value }); // 입력 값을 상태에 업데이트
    console.log(item); // 콘솔에 현재 입력값 출력 (디버깅용)
  };

  // Enter 키 입력 시 자동으로 추가 버튼 클릭 처리
  const enterKeyEventHandler = (e) => {
    if (e.key === "Enter") { // Enter 키가 눌렸다면
      onButtonClick(); // 할 일 추가 함수 실행
    }
  };

  return (
    // Grid 컨테이너를 사용하여 할 일 입력 UI 구성 (반응형 레이아웃)
    <Grid container style={{ marginTop: 20 }}> 
      {/* 입력 필드 영역 (전체 너비의 11/12 차지) */}
      <Grid xs={11} md={11} item style={{ paddingRight: 16 }}>
        <TextField
          placeholder="Add Todo here" // 입력 필드의 플레이스홀더 (사용자에게 안내)
          fullWidth // 부모 요소의 전체 너비를 차지하도록 설정
          onChange={onInputChange} // 입력 필드 값 변경 시 실행되는 핸들러
          onKeyDown={enterKeyEventHandler}
          // onKeyPress={enterKeyEventHandler} // 키 입력 시 엔터키 감지 핸들러
          value={item.title} // 현재 입력된 값과 상태를 연결
        />
      </Grid>

      {/* 버튼 영역 (전체 너비의 1/12 차지) */}
      <Grid xs={1} md={1} item>
        <Button
          fullWidth // 버튼이 부모 요소의 전체 너비를 차지하도록 설정
          style={{ height: "100%" }} // 버튼 높이를 입력 필드와 동일하게 설정
          color="secondary" // Material-UI에서 제공하는 색상 (secondary)
          variant="outlined" // 외곽선 스타일의 버튼
          onClick={onButtonClick} // 버튼 클릭 시 할 일 추가
        >
          +
        </Button>
      </Grid>
    </Grid>
  );
};

export default AddTodo; // 컴포넌트를 외부에서 사용할 수 있도록 내보내기