import React, { useState, useEffect } from "react"; // React 및 상태 관리 훅 임포트
import {
  ListItem, // 리스트 아이템 컴포넌트 (할 일 하나를 표시)
  ListItemText, // 리스트 아이템 내부의 텍스트 표시
  InputBase, // 사용자 입력 필드
  Checkbox, // 완료 여부를 나타내는 체크박스
  ListItemSecondaryAction, // 삭제 버튼을 위한 공간
  IconButton, // 아이콘 버튼 (삭제 기능)
} from "@mui/material";
import DeleteOutlined from "@mui/icons-material/DeleteOutlined"; // 삭제 아이콘

// Todo 컴포넌트 정의
const Todo = (props) => {
  // 현재 할 일 아이템을 상태로 저장 (부모 컴포넌트에서 받은 값)
  const [item, setItem] = useState(props.item);
  // 입력 필드를 읽기 전용으로 설정하는 상태 (수정 모드 관리)
  const [readOnly, setReadOnly] = useState(true);

  // 부모 컴포넌트에서 전달된 삭제 및 수정 함수
  const deleteItem = props.deleteItem;
  const editItem = props.editItem;

  // 입력 필드 변경 시 실행되는 이벤트 핸들러 (할 일 제목 업데이트)
  const editEventHandler = (e) => {
    setItem({ ...item, title: e.target.value }); // 기존 상태를 유지하며 title 업데이트
  };

  // 체크박스 변경 시 실행되는 이벤트 핸들러 (완료 상태 변경)
  const checkboxEventHandler = (e) => {
    item.done = e.target.checked; // item 객체의 done 값 변경
    editItem(item); // 변경된 값 부모 컴포넌트로 전달
  };

  // 삭제 버튼 클릭 시 실행되는 이벤트 핸들러
  const deleteEventHandler = () => {
    deleteItem(item); // 부모 컴포넌트에서 해당 아이템 삭제
  };

  // 입력 필드를 수정 모드로 변경
  const turnOffReadOnly = () => {
    setReadOnly(false); // 읽기 전용 모드 해제
  };

  // Enter 키 입력 시 수정 완료 후 읽기 전용 모드로 변경
  const turnOnReadOnly = (e) => {
    if (e.key === "Enter" && readOnly === false) { 
      setReadOnly(true); // 다시 읽기 전용 모드로 변경
      editItem(item); // 수정된 값을 부모 컴포넌트에 전달
    }
  };

  return (
    <ListItem>
      {/* 완료 여부를 표시하는 체크박스 */}
      <Checkbox checked={item.done} onChange={checkboxEventHandler} />
      
      {/* 할 일 제목을 표시하는 입력 필드 */}
      <ListItemText>
        <InputBase
          inputProps={{
            "aria-label": "naked", // 접근성 라벨 지정
            readOnly: readOnly, // 읽기 전용 상태에 따라 변경
          }}
          onClick={turnOffReadOnly} // 클릭하면 수정 가능 상태로 변경
          onKeyDown={turnOnReadOnly} // Enter 입력 시 수정 완료
          onChange={editEventHandler} // 입력 값 변경 시 상태 업데이트
          type="text"
          id={item.id} // 각 아이템의 고유 ID 지정
          name={item.id}
          value={item.title} // 현재 할 일 제목을 표시
          multiline={true} // 여러 줄 입력 가능
          fullWidth={true} // 부모 요소의 전체 너비를 차지하도록 설정
        />
      </ListItemText>

      {/* 삭제 버튼 */}
      <ListItemSecondaryAction>
        <IconButton aria-label="Delete Todo" onClick={deleteEventHandler}>
          <DeleteOutlined /> {/* 삭제 아이콘 */}
        </IconButton>
      </ListItemSecondaryAction>
    </ListItem>
  );
};

export default Todo; // 외부에서 사용할 수 있도록 컴포넌트를 내보내기