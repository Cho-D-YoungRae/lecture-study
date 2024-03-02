import React, {memo, useCallback, useContext, useMemo} from "react";
import {ActionType, Code, TableContext} from "./MineSearch";

const getTdStyle = (code) => {
  switch (code) {
    case Code.NORMAL:
    case Code.MINE:
      return {
        background: "#444",
      };
    case Code.CLICKED_MINE:
    case Code.OPENED:
      return {
        background: "white",
      };
    case Code.QUESTION:
    case Code.QUESTION_MINE:
      return {
        background: "yellow",
      };
    case Code.FLAG:
    case Code.FLAG_MINE:
      return {
        background: "red",
      };
    default:
      return {
        background: "white",
      };
  }
}

const getTdText = (code) => {
  switch (code) {
    case Code.NORMAL:
      return "";
    case Code.MINE:
      return "X";
    case Code.CLICKED_MINE:
      return "펑";
    case Code.FLAG:
    case Code.FLAG_MINE:
      return "!";
    case Code.QUESTION:
    case Code.QUESTION_MINE:
      return "?";
    default:
      return code > 0 ? code : "";
  }
}

const Td = memo(({rowIndex, colIndex}) => {
  const {tableData, halted, dispatch} = useContext(TableContext);

  const onClickTd = useCallback(() => {
    if (halted) {
      return;
    }
    switch (tableData[rowIndex][colIndex]) {
      case Code.OPENED:
      case Code.FLAG:
      case Code.FLAG_MINE:
      case Code.QUESTION:
      case Code.QUESTION_MINE:
        return;
      case Code.NORMAL:
        dispatch({type: ActionType.OPEN_CELL, row: rowIndex, col: colIndex});
        return;
      case Code.MINE:
        dispatch({type: ActionType.CLICK_MINE, row: rowIndex, col: colIndex});
        return;
    }
    dispatch({type: ActionType.OPEN_CELL, row: rowIndex, col: colIndex})
  }, [tableData[rowIndex][colIndex], halted]);

  const onRightClickTd = useCallback((e) => {
    e.preventDefault();
    if (halted) {
      return;
    }
    switch (tableData[rowIndex][colIndex]) {
      case Code.NORMAL:
      case Code.MINE:
        dispatch({type: ActionType.FLAG_CELL, row: rowIndex, col: colIndex});
        return;
      case Code.FLAG:
      case Code.FLAG_MINE:
        dispatch({type: ActionType.QUESTION_CELL, row: rowIndex, col: colIndex});
        return;
      case Code.QUESTION:
      case Code.QUESTION_MINE:
        dispatch({type: ActionType.NORMALIZE_CELL, row: rowIndex, col: colIndex});
        return;
      default:
        return;
    }
  }, [tableData[rowIndex][colIndex], halted]);

  // Context API 를 사용하면 컨텍스트 안의 state 가 바뀔 때마다 자식 컴포넌트가 모두 리렌더링된다.
  // 이때 memo 를 사용하면 state 가 바뀌지 않았다면 리렌더링을 막을 수 있다.
  // react devtools 에서 반짝이는 것과 별개로 실제로 리렌더링이 되지 않는다.
  // 이 함수 자체는 실행되지만 실제 리렌더링은 시켜주지 않기 위해 memo 를 사용한다.
  // -> 아래 return 부분이 실제 렌더링되는 부분이므로
  // 아니면 아래를 별도 컴포넌트로 분리할 수도 있음
  return useMemo(() => (
    <>
      <td
        style={getTdStyle(tableData[rowIndex][colIndex])}
        onClick={onClickTd}
        onContextMenu={onRightClickTd}
      >{getTdText(tableData[rowIndex][colIndex])}</td>
    </>
  ), [tableData[rowIndex][colIndex]]);
})

export default Td;