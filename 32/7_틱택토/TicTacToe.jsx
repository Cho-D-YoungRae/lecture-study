import React, {useCallback, useEffect, useReducer, useState} from "react";
import Table from "./Table";

const initialState = {
  winner: '',
  turn: 'O',
  tableData: [
    ['', '', ''],
    ['', '', ''],
    ['', '', '']
  ],
  recentCell: [-1, -1],
};

// state 를 어떻게 바꿀지
export const SET_WINNER = "SET_WINNER";
export const CLICK_CELL = "CLICK_CELL";
export const CHANGE_TURN = "SET_TURN";
export const RESET_GAME = "RESET_GAME";

const reducer = (state, action) => {
  switch (action.type) {
    case SET_WINNER:
      // state.winner = action.winner; 이렇게 하면 안됨
      // 새로운 객체를 만들어서 리턴해야함
      return {
        ...state,
        winner: action.winner,
      };
    case CLICK_CELL: {
      const tableData = [...state.tableData];
      tableData[action.row] = [...tableData[action.row]]; // immer 라이브러리로 가독성 해결 가능
      tableData[action.row][action.cell] = state.turn;
      return {
        ...state,
        tableData,
        recentCell: [action.row, action.cell],
      }
    }
    case CHANGE_TURN: {
      return {
        ...state,
        turn: state.turn === "O" ? "X" : "O"
      }
    }
    case RESET_GAME: {
      return {
        ...state,
        turn: 'O',
        tableData: [
          ['', '', ''],
          ['', '', ''],
          ['', '', '']
        ],
        recentCell: [-1, -1],
      }
    }
    default:
      return state;
  }
}

const TicTacToe = () => {
  // 3 번째 인자는 lazyinit -> 자주 사용되지는 않음
  const [state, dispatch] = useReducer(reducer, initialState);
  const { tableData, turn, winner, recentCell } = state;
  // const [winner, setWinner] =   useState("");
  // const [turn, setTurn] = useState("O");
  // const [tableData, setTableData] = useState([
  //   ['', '', ''],
  //   ['', '', ''],
  //   ['', '', '']
  // ]);

  const onClickTable = useCallback(() => {
    // dispatch 안에 들어가는 인자를 action 객체라고 함
    // dispatch 하는 액션을 실행한다
    // 액션을 디스패치 할 때마다 리듀서가 실행됨
    dispatch({ type: SET_WINNER, winner: "O" });
  }, []);

  useEffect(() => {
    const [row, cell] = recentCell;
    if (row < 0) {
      return;
    }

    let win = false;
    if (tableData[row][0] === turn && tableData[row][1] === turn && tableData[row][2] === turn) {
      win = true;
    }
    if (tableData[0][cell] === turn && tableData[1][cell] === turn && tableData[2][cell] === turn) {
      win = true;
    }
    if (tableData[0][0] === turn && tableData[1][1] === turn && tableData[2][2] === turn) {
      win = true;
    }
    if (tableData[0][2] === turn && tableData[1][1] === turn && tableData[2][0] === turn) {
      win = true;
    }

    if (win) {  // 승리 시
      dispatch({ type: SET_WINNER, winner: turn });
      dispatch({ type: RESET_GAME });
    } else {  // 무승부 검사
      let all = true; // all 이 true 면 무승부라는 뜻
      // 빈 칸이 있는지 확인
      tableData.forEach((row) => {
        row.forEach((cell) => {
          if (!cell) {
            all = false;
          }
        });
      });
      if (all) {
        dispatch({ type: RESET_GAME });
      } else {
        dispatch({ type: CHANGE_TURN });
      }
    }

  }, [state.tableData]);

  return (
    <>
      <Table tableData={state.tableData} dispatch={dispatch}></Table>
      {winner && <div>{winner}님의 승리</div>}
    </>
  )
};

export default TicTacToe;