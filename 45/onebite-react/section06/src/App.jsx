import './App.css'
import Viewer from "./components/Viewer.jsx";
import Controller from "./components/Controller.jsx";
import {useState} from "react";

/**
 * section 태그로 묶는 이유: css 로 스타일링 하기 위함
 */
function App() {
  const [count, setCount] = useState(0);

  const onClickButton = (value) => {
    setCount((prevCount) => prevCount + value);
  }

  return (<div className="App">
    <h1>Simple Counter</h1>
    <section>
      <Viewer count={count}/>
    </section>
    <section>
      <Controller onClickButton={onClickButton}/>
    </section>
  </div>);
}

export default App
