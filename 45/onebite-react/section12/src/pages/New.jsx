import Header from "../components/Header.jsx";
import Button from "../components/Button.jsx";
import Editor from "../components/Editor.jsx";

function New() {
  return (
    <div>
      <Header
        title="새 일기 쓰기"
        leftChild={<Button text="< 뒤로가기"/>}
      />
      <Editor/>
    </div>
  );
}

export default New;
