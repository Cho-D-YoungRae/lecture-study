import './App.css'
import Viewer from "./components/Viewer.jsx";
import Controller from "./components/Controller.jsx";
import {useEffect, useRef, useState} from "react";
import Even from "./components/Even.js";

/**
 * section 태그로 묶는 이유: css 로 스타일링 하기 위함
 */
function App() {
  const [count, setCount] = useState(0);
  const [input, setInput] = useState('');
  const isMount = useRef(false);

  // 상태변화 함수는 비동기로 동작하기 때문에 상태에 대해 원하는 대로 사용하려면 useEffect 사용해야함
  // 이벤트 함수 내에서 상태를 조회하는 등 하면 원하는 대로 동작하지 않을 수 있음
  useEffect(() => {
    console.log(`count: ${count} / input: ${input}`)
  }, [count, input]);
  // 의존성 배열, dependency array, deps

  // 1. 마운트: 탄생
  useEffect(() => {
    console.log("mount");
  }, []);

  // 2. 업데이트: 변화, 리렌더링 (마운트 시점에도 호출)
  //
  useEffect(() => {
    console.log("update");
  });

  // 2-1. 업데이트 상황에서만 (마운트 시점 X)
  useEffect(() => {
    if (!isMount.current) {
      isMount.current = false;
      return;
    }
    console.log("only update");
  });

  // 3.

  const onClickButton = (value) => {
    setCount((prevCount) => prevCount + value);
  }

  return (<div className="App">
    <h1>Simple Counter</h1>
    <section>
      <input
        value={input}
        onChange={(e) => setInput(e.target.value)}
      />
    </section>
    <section>
      <Viewer count={count}/>
      {count % 2 === 0 ? <Even/> : null}
    </section>
    <section>
      <Controller onClickButton={onClickButton}/>
    </section>
  </div>);
}

export default App
