import './TodoItem.css';
import React, {memo} from 'react';

// onUpdate, onDelete 를 useCallback 으로 하지 않으면 이 컴포넌트 memo 해도 리렌더링 계속 발생\
// -> memo는 props 를 통해서 리렌더링을 결정하기 때문에
// -> 혹은 memo 의 두번째 인자로 리렌더링 결정 함수를 줄 수 있음
function TodoItem({id, done, content, date, onUpdate, onDelete}) {

  const onChangeCheckbox = () => {
    onUpdate(id);
  };

  const onClickDeleteButton = () => {
    onDelete(id);
  }

  return (<div className="TodoItem">
    <input
      type="checkbox"
      checked={done}
      onChange={onChangeCheckbox}
    />
    <div className="content">{content}</div>
    <div className="date">{new Date(date).toLocaleDateString()}</div>
    <button onClick={onClickDeleteButton}>삭제</button>
  </div>);
}

/*
memo 와 같이 컴포넌트를 받아서 최적화 등의 추가 기능 덧붙여서 새로운 컴포넌트를 반환해주는 것을
고차 컴포넌트(HOC, Higher Order Component) 라고 한다
 */
export default memo(TodoItem);