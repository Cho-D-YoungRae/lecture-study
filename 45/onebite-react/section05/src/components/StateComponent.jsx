import React, {useState} from 'react';

/**
 * state 가 아니라 단순한 변수를 사용하면, 값을 변경하더라도 리렌더링이 발생하지 않는다.
 */
function StateComponent(props) {

  const [count, setCount] = useState(0);
  const [light, setLight] = useState("OFF");

  return (
    <>
      <h1>{count}</h1>
      <button onClick={() => setCount(count + 1)}>
        +
      </button>
      <h1>{light}</h1>
      <button onClick={() => setLight(light === "OFF" ? "ON" : "OFF")}>
        {light === "OFF" ? "켜기" : "끄기"}
      </button>
    </>
  );
}

export default StateComponent;