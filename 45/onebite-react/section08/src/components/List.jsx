import './List.css';
import TodoItem from "./TodoItem.jsx";
import {useState} from "react";

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
  }

  return (<div className="List">
    <h4>Todo List 🌱</h4>
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
