import Header from "../components/Header.jsx";
import Button from "../components/Button.jsx";
import Editor from "../components/Editor.jsx";
import {useNavigate} from "react-router-dom";
import {DiaryDispatchContext} from "../App.jsx";
import {useContext} from "react";

function New() {
  const nav = useNavigate();
  const {onCreate} = useContext(DiaryDispatchContext);

  const onSubmit = (input) => {
    onCreate(
      input.createdDate.getTime(),
      input.emotionId,
      input.content
    );
    nav("/", {replace: true});
  };

  return (
    <div>
      <Header
        title="새 일기 쓰기"
        leftChild={<Button text="< 뒤로가기" onClick={() => nav(-1)}/>}
      />
      <Editor onSubmit={onSubmit} />
    </div>
  );
}

export default New;
