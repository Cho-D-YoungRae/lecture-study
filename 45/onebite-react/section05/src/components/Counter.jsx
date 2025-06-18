import React, {useState} from 'react';

/**
 * state 가 아니라 단순한 변수를 사용하면, 값을 변경하더라도 리렌더링이 발생하지 않는다.
 */
function Counter(props) {

  const [count, setCount] = useState(0);

  return (
    <>
      <h1>{count}</h1>
      <button onClick={() => setCount(count + 1)}>
        +
      </button>
    </>
  );
}

export default Counter;