import React, {useState} from 'react';
import useInput from "../hooks/useInput.js";

/**
 * 훅 주의사항
 * - 함수 컴포넌트, 커스텀 훅 내부에서만 호출 가능
 * - 조건문, 반복문 내부에서 호출 불가
 *  - 서로 다른 훅들의 호출 순서가 꼬일 수 있음
 * - 커스텀 훅 제작 가능
 */

function HookExam(props) {
  const [input, onChange] = useInput();
  const [input2, onChange2] = useInput();

  return (
    <div>
      <input value={input} onChange={onChange}/>
      <input value={input2} onChange={onChange2}/>
    </div>
  );
}

export default HookExam;