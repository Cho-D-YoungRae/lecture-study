import {useNavigate, useParams} from "react-router-dom";
import Header from "../components/Header.jsx";
import Button from "../components/Button.jsx";
import Viewer from "../components/Viewer.jsx";
import useDiary from "../hooks/useDiary.jsx";

function Diary() {
  const {id} = useParams();
  const nav = useNavigate();
  const diary = useDiary(id);

  if (!diary) {
    return (<div>데이터 로딩중...!</div>);
  }

  const {createdDate, emotionId, content} = diary;

  const title = new Date(createdDate).toISOString().slice(0, 10);

  return (
    <div>
      <Header
        title={`${title} 기록`}
        leftChild={<Button
          text="< 뒤로 가기"
          onClick={() => nav(-1)}
        />}
        rightChild={<Button
          text="수정하기"
          onClick={() => nav(`/edit/${id}`)}
        /> }
      />
      <Viewer emotionId={emotionId} content={content}/>
    </div>
  );
}

export default Diary;
