import './App.css'
import {Link, Route, Routes, useNavigate} from "react-router-dom";
import Home from "./pages/Home.jsx";
import New from "./pages/New.jsx";
import Diary from "./pages/Dirary.jsx";
import NotFound from "./pages/NotFound.jsx";
import {getEmotionImage} from "./util/get-emotion-image.js";
import Button from "./components/Button.jsx";
import Header from "./components/Header.jsx";

/*
1. "/": 모든 일기를 조회하는 Home 페이지
2. "/new": 새로운 일기를 작성하는 New 페이지
3, "/diary": 일기를 상세히 조회하는 Diary 페이지

Routes 바깥에 위치한 컴포넌트는 어떤 페이지로 가도 표출된다.
> 라우트 안의 요소만 페이지에 따라 렌더링이 되는 것이고 그 외부는 일반적인 리액트처럼 렌더링 되어 있는 것임므로

Link, useNavigate CSR 방식. a 태그를 사용하면 CSR 방식을 사용 못함.

public 과 assets 차이

- public 안의 정적 파일은 url 을 통해 접근, assets 안의 정적 파일을 import 사용
- assets 내부의 이미지는 vite 에서 최적화 이루어짐
  - 빌드 후 실행 시 이미지를 캐싱할 수 있도록 함
 */
function App() {

  return (
    <>
      <Header
        title="Header"
        leftChild={<Button text="left"/>}
        rightChild={<Button text="right"/>}
      />
      <Button/>

      <Routes>
        <Route path="/" element={<Home/>}/>
        <Route path="/new" element={<New/>}/>
        <Route path="/diary/:id" element={<Diary/>}/>
        <Route path="*" element={<NotFound/>}/>
      </Routes>
    </>
  );
}

export default App
