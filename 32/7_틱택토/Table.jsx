import React from "react";
import Tr from "./Tr";

const Table = ({ onClick, tableData, dispatch }) => {
  return (
    <>
      <table onClick={onClick}>
        <tbody>
          {Array(tableData.length).fill().map((tr, i) =>
            <Tr key={i} rowIndex={i} rowData={tableData[i]} dispatch={dispatch}></Tr>
          )}
        </tbody>
      </table>
    </>
  )
}

export default Table;