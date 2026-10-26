import './List.css';
import TodoItem from "./TodoItem.jsx";
import {useMemo, useState} from "react";

export function List({ todos, onUpdate, onDelete }) {
  const [search, setSearch] = useState("");

  const onChangeSearch = (e) => {
    setSearch(e.target.value);
  };

  const getFilteredDate = () => {
    if (search === "") {
      return todos;
    }

    return todos.filter((todo) =>
      todo.content.toLowerCase().includes(search.toLowerCase()));
  };

  const {totalCount, doneCount, notDoneCount} = useMemo(() => {
    console.log("getAnalyzedDate 호출");
    const totalCount = todos.length;
    const doneCount = todos.filter((todo) => todo.done).length;
    const notDoneCount = totalCount - doneCount;

    return {
      totalCount,
      doneCount,
      notDoneCount
    };
  }, [todos]);

  return (<div className="List">
    <h4>Todo List 🌱</h4>
    <div>
      <div>total: {totalCount}</div>
      <div>done: {doneCount}</div>
      <div>not done: {notDoneCount}</div>
    </div>
    <input
      placeholder="검색어를 입력하세요"
      value={search}
      onChange={onChangeSearch}
    />
    <div className="todosWrapper">
      {getFilteredDate().map((todo) => {
        return <TodoItem key={todo.id} {...todo} onUpdate={onUpdate} onDelete={onDelete} />
      })}
    </div>
  </div>);
}

export default List;
