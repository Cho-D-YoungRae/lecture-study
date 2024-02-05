import './App.css';
import {useState} from 'react';

function Header(props) {
  return (
    <h1><a href="/" onClick={event => {
      event.preventDefault();
      props.onChangeMode();
    }}>{props.title}</a></h1>
  );
}

function Nav(props) {
  const lis = [];
  for (let i = 0; i < props.topics.length; i++) {
    const topic = props.topics[i];
    lis.push(<li key={topic.id}><a id={topic.id} href={"/read/" + topic.id} onClick={
      event => {
        event.preventDefault();
        props.onChangeMode(event.target.id);
      }
    }>{topic.title}</a></li>);
  }
  return(
    <nav>
      <ol>
        {lis}
      </ol>
    </nav>
  );
}

function Article(props) {
  return (
    <article>
      <h2>{props.title}</h2>
      {props.content}
    </article>
  );
}

function Create(props) {
  return (
    <article>
      <h2>Create</h2>
      <form onSubmit={(event) => {
        event.preventDefault();
        const title = event.target.title.value;
        const content = event.target.content.value;
        props.onCreate(title, content);
      }}>
        <p><input type="text" name="title" placeholder="title"/></p>
        <p><textarea name="content" placeholder="content"></textarea></p>
        <p><input type="submit" value="Create"/></p>
      </form>
    </article>
  )
}

function Update(props) {
  const [title, setTitle] = useState(props.title);
  const [body, setBody] = useState(props.body);
  return (
    <article>
      <h2>Update</h2>
      <form onSubmit={(event) => {
        event.preventDefault();
        const title = event.target.title.value;
        const content = event.target.content.value;
        props.onUpdate(title, content);
      }}>
        <p><input type="text" name="title" placeholder="title" value={title} onChange={event=>{
          console.log(event.target.value);
          setTitle(event.target.value);
        }}/></p>
        <p><textarea name="content" placeholder="content" value={body} onChange={event=>{
          console.log(event.target.value);
          setBody(event.target.value);
        }}></textarea></p>
        <p><input type="submit" value="Update"/></p>
      </form>
    </article>
  )
}

function App() {
  const [mode, setMode] = useState("WELCOME");
  const [id, setId] = useState(null);
  const [nextId, setNextId] = useState(4);
  const [topics, setTopics] = useState([
    {id: 1, title: "HTML", content: "HTML is ..."},
    {id: 2, title: "CSS", content: "CSS is ..."},
    {id: 3, title: "JavaScript", content: "JavaScript is ..."}
  ]);

  let content = null;
  let contextControl = null;
  if (mode === "WELCOME") {
    content = <Article title="Welcome" content="Hello, WEB"></Article>;
  } else if (mode === "READ") {
    let title, body = null;
    for(let i = 0; i < topics.length; i++) {
      const topic = topics[i];
      if (topic.id === Number(id)) {
        title = topic.title;
        body = topic.content;
        break;
      }
    }
    content = <Article title={title} content={body}></Article>;
    contextControl = <li><a href={"/update/" + id} onClick={event => {
      event.preventDefault();
      setMode("UPDATE");
    }}>Update</a></li>
  } else if (mode === "CREATE") {
    content = <Create onCreate={(_title, _content) => {
      const newTopic = {id:nextId, title:_title, content:_content};
      const newTopics = [...topics, newTopic];
      setTopics(newTopics);
      setMode("READ");
      setId(nextId);
      setNextId(nextId + 1);
    }}></Create>;
  } else if (mode === "UPDATE") {
    let title, body = null;
    for(let i = 0; i < topics.length; i++) {
      const topic = topics[i];
      if (topic.id === Number(id)) {
        title = topic.title;
        body = topic.content;
        break;
      }
    }
    content = <Update title={title} body={body} onUpdate={(title, body) => {
      console.log(title, body);
      const newTopics = [...topics];
      const updatedTopic = {id: id, title:title, content:body};
      for(let i = 0; i < newTopics.length; i++) {
        if (newTopics[i].id === id) {
          newTopics[i] = updatedTopic;
          break;
        }
      }
      setTopics(newTopics);
      setMode("READ"); 
    }}></Update>
  }
  return (
    <div className="App">
      <Header title="React" onChangeMode={() => 
        setMode("WELCOME")}></Header>
      <Nav topics={topics} onChangeMode={(_id) => {
        setMode("READ");
        setId(_id);
      }}></Nav>
      {content}
      <ul>
        <li>
          <a href="/create" onClick = {(event) => {
            event.preventDefault();
            setMode("CREATE");
          }}>Create</a>
        </li>
        {contextControl}
      </ul>
    </div>
  );
}

export default App;
