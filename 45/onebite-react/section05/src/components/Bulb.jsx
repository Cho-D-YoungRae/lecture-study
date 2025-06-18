import React, {useState} from 'react';

/**
 * 리렌더링 발생 상황
 * 1. state
 * 2. props
 * 3. 부모
 * => 관련 없는 state 는 별도 컴포넌트로 분리하여 리렌더링을 최소화한다.
 */
function Bulb() {
  const [light, setLight] = useState("OFF");
  return (
    <div>
      {light === "ON" ? (
        <h1>ON</h1>
      ) : (
        <h1>OFF</h1>
      )}
      <button onClick={() => setLight(light === "OFF" ? "ON" : "OFF")}>
        {light === "OFF" ? "켜기" : "끄기"}
      </button>
    </div>
  );
}

export default Bulb;