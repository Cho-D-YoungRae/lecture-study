import React, {memo} from 'react';

// 원래는
// const Try = (props) => {} 이렇게 쓰는데
// 아래와 같이 구조분해 가능
const Try = memo(({tryInfo}) => {
  return (
    <li>
      <div>{tryInfo.try}</div>
      <div>{tryInfo.result}</div>
    </li>
  )
});

// memo 를 씌우면 개발자 도구의 태그 이름이 바뀌기 때문에 Try 로 표출되도록 이름 설정
Try.displayName = 'Try';

export default Try;