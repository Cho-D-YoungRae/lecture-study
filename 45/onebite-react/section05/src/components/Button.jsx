import React from 'react';

function Button({children, text, color = "black"}) {
  const onClickButton = () => {
    console.log(`Button clicked: ${text}`);
  };
  // 이벤트 객체
  // SyntheticBaseEvent(합성 이벤트): 모든 웹 브라우저의 이벤트 객체를 하나로 통일한 형태
  const onMouseEnterButton = (e) => {
    console.log(e);
    console.log(`Mouse entered: ${text}`);
  };

  return (
    <button
      onClick={onClickButton}
      onMouseEnter={onMouseEnterButton}
      style={{ color: color }}
    >
      {text} - {color}
      {children}
    </button>
  );
}

export default Button;