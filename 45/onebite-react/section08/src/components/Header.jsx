import './Header.css';
import {memo} from "react";

/*
매우 단순한 컴포넌트를 메모로 최적화할 필요는 없을 수도 있다. -> 메모도 연산이 필요. 메모리도 사용
TodoItem 같이 유저의 행동에 따라 많아질 수 있는 것 혹은 함수들을 많이 가지고 있어서 무거운 컴포넌트 등에 한해서 적용

그리고 기능 구현이 우선이기 때문에 최적화는 마지막에 하자
 */
export function Header() {
  return (<div className="Header">
    <h3>오늘은 📆</h3>
    <h1>{new Date().toDateString()}</h1>
  </div>);
}

export default memo(Header);