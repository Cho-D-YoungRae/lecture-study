import React, {useCallback, useEffect, useRef} from "react";
import {CLICK_CELL, CHANGE_TURN} from "./TicTacToe";

const Td = ({ dispatch, rowIndex, cellIndex, cellData }) => {
  console.log(`[${rowIndex}, ${cellIndex}] rendered`);

  // useRef, useEffect 를 사용하여 리렌더링이 발생하는 원인을 파악하는 방법
  const ref = useRef([]);
  useEffect(() => {
    // ref 는 바뀌는데 props 는 안 바뀔 때가 있음 -> 그걸 확인하기 위해 사용
    // 바뀌는게 있으면
    console.log(rowIndex === ref.current[0], cellIndex === ref.current[1], cellData === ref.current[2], dispatch === ref.current[3]);
    ref.current = [rowIndex, cellIndex, cellData, dispatch];
  }, [rowIndex, cellIndex, cellData, dispatch]); // 각종 props


  const onClickTd = useCallback(() => {
    console.log(rowIndex, cellIndex);
    if (cellData) {
      return;
    }
    // useReducer 의 dispatch 는 비동기
    // -> redux 는 동기적으로 바뀌어서 헷갈릴 수 있음
    // -> react 가 state 가 비동기적으로 바뀌는 것을 생각해보면 됨
    // 비동기인 state 에서 뭔가를 처리하려면 useEffect 를 사용해야함
    dispatch({type: CLICK_CELL, row: rowIndex, cell: cellIndex});
  }, [cellData]);

  return (
    <td onClick={onClickTd}>{cellData}</td>
  )
}

export default Td;
