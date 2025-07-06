import './App.css'
import Header from "./components/Header.jsx";
import Editor from "./components/Editor.jsx";
import List from "./components/List.jsx";
import {useRef, useState} from "react";

const mockData = [
  {
    id: 3,
    done: false,
    content: "React 공부하기",
    date: new Date().getTime()
  },
  {
    id: 2,
    done: false,
    content: "운동하기",
    date: new Date().getTime()
  },
  {
    id: 1,
    done: false,
    content: "빨래하기",
    date: new Date().getTime()
  }
];

function App() {
  const [todos, setTodos] = useState(mockData);
  const idRef = useRef(4);

  const onCreate = (content) => {
    const newTodo = {
      id: idRef.current++,
      done: false,
      content: content,
      date: new Date().getTime()
    }

    setTodos(todos => [newTodo, ...todos]);
  };

  const onUpdate = (targetId) => {
    setTodos(todos => todos.map((todo) =>
      todo.id === targetId ? {...todo, done: !todo.done} : todo
    ));
  };

  const onDelete = (targetId) => {
    setTodos(todos => todos.filter((todo) =>
      todo.id !== targetId)
    );
  };

  return (
    <div className="App">
      <Header/>
      <Editor onCreate={onCreate}/>
      <List todos={todos} onUpdate={onUpdate} onDelete={onDelete}/>
    </div>
  )
}

export default App
