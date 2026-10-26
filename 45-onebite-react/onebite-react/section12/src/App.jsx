import './App.css'
import {Route, Routes} from "react-router-dom";
import Home from "./pages/Home.jsx";
import New from "./pages/New.jsx";
import Diary from "./pages/Diary.jsx";
import NotFound from "./pages/NotFound.jsx";
import Edit from "./pages/Edit.jsx";
import {createContext, useReducer, useRef} from "react";

const mockData = [
  {
    id: 3,
    createdDate: new Date("2025-07-07").getTime(),
    emotionId: 3,
    content: "3번 일기 내용",
  },
  {
    id: 2,
    createdDate: new Date("2025-07-06").getTime(),
    emotionId: 2,
    content: "2번 일기 내용",
  },
  {
    id: 1,
    createdDate: new Date("2025-06-06").getTime(),
    emotionId: 1,
    content: "1번 일기 내용",
  }
];

function reducer(state, action) {
  switch (action.type) {
    case "CREATE":
      return [action.data, ...state];
    case "UPDATE":
      return state.map((item) =>
        String(item.id) === String(action.data.id) ? action.data : item
      );
    case "DELETE":
      return state.filter((item) => String(item.id) !== String(action.data.id));
    default:
      return state;
  }
}

export const DiaryStateContext = createContext();
export const DiaryDispatchContext = createContext();

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

  const [data, dispatch] = useReducer(reducer, mockData);
  const idRef = useRef(4);

  const onCreate = (createdDate, emotionId, content) => {
    dispatch({
      type: "CREATE",
      data: {
        id: idRef.current++,
        createdDate,
        emotionId,
        content
      }
    });
  };

  const onUpdate = (id, createdDate, emotionId, content) => {
    dispatch({
      type: "UPDATE",
      data: {
        id,
        createdDate,
        emotionId,
        content
      }
    });
  };

  const onDelete = (id) => {
    dispatch({
      type: "DELETE",
      id
    });
  };

  return (
    <>
      <DiaryStateContext.Provider value={data}>
        <DiaryDispatchContext.Provider
          value={{
            onCreate,
            onUpdate,
            onDelete
          }}
        >
          <Routes>
            <Route path="/" element={<Home/>}/>
            <Route path="/new" element={<New/>}/>
            <Route path="/diary/:id" element={<Diary/>}/>
            <Route path="/edit/:id" element={<Edit/>}/>
            <Route path="*" element={<NotFound/>}/>
          </Routes>
        </DiaryDispatchContext.Provider>
      </DiaryStateContext.Provider>
    </>
  );
}

export default App
