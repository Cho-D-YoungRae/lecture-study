import React, {useRef, useState} from "react";

const ResponseCheck = () => {
  const [state, setState] = useState("waiting");
  const [message, setMessage] = useState("클릭해서 시작하세요.");
  const [result, setResult] = useState([]);
  // 클래스에서 this.timeout, this.startTime, this.endTime 로 사용했던 클래스 변수들을
  // Hooks 로 표현할 때는 ref 를 사용
  // ref 는 dom 에 직접 접근할 때도 사용
  // 값을 넣어줄 때, 가져올 때 current 를 사용
  // state VS ref
  // - state: 값이 바뀌면 컴포넌트가 리렌더링
  // - ref: 값이 바뀌어도 컴포넌트가 리렌더링 되지 않음
  // - 값이 바뀌어도 화면에는 영향을 미치지 않고 싶은 값 -> ref 사용
  const timeout = useRef(null);
  const startTime = useRef();
  const endTime = useRef();

  const onClickScreen = () => {
    if (state === "waiting") {
      setState("ready");
      setMessage("초록색이 되면 클릭하세요.");

      timeout.current = setTimeout(() => {
        setState("now");
        setMessage("지금 클릭");
        startTime.current = new Date();
      }, Math.floor(Math.random() * 1000) + 2000); // 2~3초 랜덤
    } else if (state === "ready") { // 성급하게 클릭
      clearTimeout(timeout.current);
      setState("waiting");
      setMessage("너무 성급하시군요! 초록색이 된 후에 클릭하세요.");
    } else if (state === "now") { // 반응속도 체크
      endTime.current = new Date();
      console.log(`startTime: ${startTime.current}, endTime: ${endTime.current}`);
      console.log(`result: ${endTime.current - startTime.current}ms`);
      setState("waiting");
      setMessage("클릭해서 시작하세요.");
      setResult(prev => [...prev, endTime.current - startTime.current]);
    }

  }

  const onReset = () => {

  }

  const renderAverage = () => {
    return (<>
        <div>평균 시간: {result.reduce((a, c) => a + c) / result.length}ms</div>
        <button onClick={onReset}>리셋</button>
      </>)
  }

  return (<>
    <div
      id="screen"
      className={state}
      onClick={onClickScreen}
    >
      {message}
    </div>
    {/*jsx 안에서 if 를 못 쓰지만 함수 안에서는 쓸 수 있어서 아래와 같이 가능*/}
    {/*함수가 즉시 실행 함수여야 함*/}
    {/*클래스에서와 같이 아예 분리하는게 더 깔끔할 수 있음*/}
    {/*반복문도 아래와 같이 할 수 있음*/}
    {(() => {
      if (result.length === 0) {
        return null;
      } else {
        return renderAverage();
      }
    })()}
  </>);
  // jsx 에서 배열을 반환해줄 수 있음 -> key 가 필요
  // 거의 사용되지는 않음
  // return [
  //   <div key="사과">사과</div>,
  //   <div key="배">배</div>,
  //   <div key="감">감</div>,
  // ]
}

export default ResponseCheck;