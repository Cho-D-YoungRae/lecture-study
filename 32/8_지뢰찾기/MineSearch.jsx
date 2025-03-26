import React, {createContext, useEffect, useMemo, useReducer} from "react";
import Table from "./Table";
import Form from "./Form";

export const Code = {
  MINE: -7,
  NORMAL: -1,
  QUESTION: -2,
  FLAG: -3,
  QUESTION_MINE: -4,
  FLAG_MINE: -5,
  CLICKED_MINE: -6,
  OPENED: 0, // 0 이상이면 다 opened
}

// createContext 함수 인자는 초깃값
export const TableContext = createContext({
  tableData: [],
  halted: true,
  dispatch: () => {
  },
});

const initialState = {
  tableData: [],
  data: {
    row: 0,
    col: 0,
    mine: 0,
  },
  timer: 0,
  result: "",
  halted: true,
  openedCount: 0,
};

function plantMine(row, col, mine) {
  console.log(`plantMine=> row: ${row}, col: ${col}, mine: ${mine}`);
  let candidate = Array(row * col).fill().map((_, i) => i);
  let shuffle = [];
  while (candidate.length > row * col - mine) {
    let chosen = candidate.splice(Math.floor(Math.random() * candidate.length), 1)[0];
    shuffle.push(chosen);
  }

  const data = [];
  for (let i = 0; i < row; i++) {
    const rowData = [];
    data.push(rowData);
    for (let j = 0; j < col; j++) {
      rowData.push(Code.NORMAL);
    }
  }

  for (let k = 0; k < shuffle.length; k++) {
    const ver = Math.floor(shuffle[k] / col);
    const hor = shuffle[k] % col;
    data[ver][hor] = Code.MINE;
  }

  console.log("plantMine() => data");
  console.log(data);
  return data;
}

export const ActionType = {
  START_GAME : "START_GAME",
  OPEN_CELL : "OPEN_CELL",
  CLICK_MINE : "CLICK_MINE",
  FLAG_CELL : "FLAG_CELL",
  QUESTION_CELL : "QUESTION_CELL",
  NORMALIZE_CELL : "NORMALIZE_CELL",
  INCREMENT_TIMER : "INCREMENT_TIMER",
};


const reducer = (state, action) => {
  switch (action.type) {
    case ActionType.START_GAME:
      return {
        ...state,
        data: {
          row: action.row,
          col: action.col,
          mine: action.mine,
        },
        tableData: plantMine(action.row, action.col, action.mine),
        halted: false,
        openedCount: 0,
        timer: 0,
      };
    case ActionType.OPEN_CELL: {
      const tableData = [...state.tableData];
      tableData.forEach((row, i) => {
        tableData[i] = [...state.tableData[i]];
      });
      const checked = [];
      let openedCount = 0;
      const checkAround = (row, col) => {
        if ([Code.OPENED, Code.FLAG, Code.FLAG_MINE, Code.QUESTION, Code.QUESTION_MINE].includes(tableData[row][col])) {
          return;
        }
        if (row < 0 || row >= tableData.length || col < 0 || col >= tableData[0].length) {
          return;
        }
        if (checked.includes(row + "/" + col)) {
          return;
        } else {
          checked.push(row + "/" + col);
        }
        openedCount += 1;

        let around = [];
        if (tableData[row - 1]) {
          around = around.concat(
            tableData[row - 1][col - 1],
            tableData[row - 1][col],
            tableData[row - 1][col + 1]
          );
        }
        around = around.concat(
          tableData[row][col - 1],
          tableData[row][col + 1]
        );
        if (tableData[row + 1]) {
          around = around.concat(
            tableData[row + 1][col - 1],
            tableData[row + 1][col],
            tableData[row + 1][col + 1]
          );
        }
        const count = around.filter(
          (v) => [Code.MINE, Code.FLAG_MINE, Code.QUESTION_MINE].includes(v)
        ).length;
        tableData[row][col] = count;
        if (count === 0) {
          const near = [];
          if (row - 1 > -1) {
            near.push([row - 1, col - 1]);
            near.push([row - 1, col]);
            near.push([row - 1, col + 1]);
          }
          near.push([row, col - 1]);
          near.push([row, col + 1]);
          if (row + 1 < tableData.length) {
            near.push([row + 1, col - 1]);
            near.push([row + 1, col]);
            near.push([row + 1, col + 1]);
          }
          near.forEach((n) => {
            if (tableData[n[0]][n[1]] !== Code.OPENED) {
              checkAround(n[0], n[1]);
            }
          });
        }
      }

      checkAround(action.row, action.col);
      let halted = false;
      let result = "";
      if (state.data.row * state.data.col - state.data.mine === state.openedCount + openedCount) {
        halted = true;
        result = `${state.timer} 초에 승리하셨습니다!`;
      }
      return {
        ...state,
        tableData,
        openedCount: state.openedCount + openedCount,
        halted,
        result,
      };
    }
    case ActionType.CLICK_MINE: {
      const tableData = [...state.tableData];
      tableData[action.row] = [...state.tableData[action.row]];
      tableData[action.row][action.col] = Code.CLICKED_MINE;
      return {
        ...state,
        tableData,
        halted: true,
      };
    }
    case ActionType.FLAG_CELL: {
      const tableData = [...state.tableData];
      tableData[action.row] = [...state.tableData[action.row]];
      if (tableData[action.row][action.col] === Code.MINE) {
        tableData[action.row][action.col] = Code.FLAG_MINE;
      } else {
        tableData[action.row][action.col] = Code.FLAG;
      }
      return {
        ...state,
        tableData,
      };
    }
    case ActionType.QUESTION_CELL: {
        const tableData = [...state.tableData];
        tableData[action.row] = [...state.tableData[action.row]];
        if (tableData[action.row][action.col] === Code.FLAG_MINE) {
          tableData[action.row][action.col] = Code.QUESTION_MINE;
        } else {
          tableData[action.row][action.col] = Code.QUESTION;
        }
        return {
          ...state,
          tableData,
        };
    }
    case ActionType.NORMALIZE_CELL: {
      const tableData = [...state.tableData];
      tableData[action.row] = [...state.tableData[action.row]];
      if (tableData[action.row][action.col] === Code.QUESTION_MINE) {
        tableData[action.row][action.col] = Code.MINE;
      } else {
        tableData[action.row][action.col] = Code.NORMAL;
      }
      return {
        ...state,
        tableData,
      };
    }
    case ActionType.INCREMENT_TIMER:
      return {
        ...state,
        timer: state.timer + 1,
      }
    default:
      return state;
  }
};

const MineSearch = () => {
  const [state, dispatch] = useReducer(reducer, initialState);
  // context api 를 설정하면 이 하위 컴포넌트들은 props 를 부모들을 거쳐거쳐 받지 않아도 됨

  // dispatch 는 바뀌지 않으므로 배열인자에 state.tableData 만 넣어줌
  const {tableData, halted} = state;
  const value useMemo(() => ({
    tableData: state.tableData,
    halted: state.halted,
    dispatch,
  }), [tableData, halted]);

  useEffect(() => {
    let timer;
    if (!halted) {
      timer = setInterval(() => {
        dispatch({type: ActionType.INCREMENT_TIMER});
      }, 1000);
    }
    return () => clearInterval(timer);
  }, [halted]);

  return (
    <>
      {/*Provider 하위 컴포넌트들이 데이터에 접근 가능*/}
      {/*ContextAPI 가 성능 최적화하기가 어려움*/}
      {/*value 를 아래와 같이 사용하면 안됨*/}
      {/*MinSearch 가 리렌더링 될 때마다 value 객체가 새로 생김*/}
      {/*-> ContextAPI 를 사용하는 자식들도 리렌더링됨*/}
      {/*그래서 매번 새로운 객체가 생기지 않도록 useMemo 를 사용해야함*/}
      {/*<TableContext.Provider value={{*/}
      {/*  tableData: state.tableData,*/}
      {/*  dispatch,*/}
      {/*}}>*/}
      <TableContext.Provider value={value}>
        <Form />
        <div>{state.timer}</div>
        <Table />
        <div>{state.result}</div>
      </TableContext.Provider>
    </>
  )
};

export default MineSearch;
