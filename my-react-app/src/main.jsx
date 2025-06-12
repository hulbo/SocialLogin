import React from 'react'; // React 라이브러리 임포트
import './index.css'; // 전역 스타일 시트 임포트
import { createRoot } from 'react-dom/client'; // React 18 이상에서 사용하는 createRoot API 임포트
import AppRouter from './AppRouter'; // 전체 앱 라우팅을 담당하는 AppRouter 컴포넌트 임포트

// HTML 파일에서 id가 'root'인 DOM 요소를 가져옴
const container = document.getElementById('root');

// React 앱을 해당 DOM 요소에 렌더링할 수 있도록 createRoot를 생성
const root = createRoot(container); 

// AppRouter 컴포넌트를 렌더링하여 앱 실행 (초기 탭 속성 "home" 전달)
root.render(<AppRouter tab="home" />);