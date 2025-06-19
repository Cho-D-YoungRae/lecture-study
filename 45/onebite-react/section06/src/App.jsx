import './App.css'
import Viewer from "./components/Viewer.jsx";
import Controller from "./components/Controller.jsx";

/**
 * section 태그로 묶는 이유: css 로 스타일링 하기 위함
 */
function App() {
  return (<div className="App">
    <h1>Simple Counter</h1>
    <section>
      <Viewer/>
    </section>
    <section>
      <Controller/>
    </section>
  </div>);
}

export default App
