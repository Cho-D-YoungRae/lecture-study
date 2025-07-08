import {DiaryStateContext} from "../App.jsx";
import {useContext, useEffect, useState} from "react";
import {useNavigate} from "react-router-dom";

/*
훅을 쓰는 것은 일반 js 함수로 분리할 수 없다
=> 커스텀 훅
 */
const useDiary = (id) => {
  const nav = useNavigate();
  const data = useContext(DiaryStateContext);
  const [currentDiaryItem, setCurrentDiaryItem] = useState();

  useEffect(() => {
    const currentDiaryItem = data.find(
      (item) => String(item.id) === String(id)
    );

    if (!currentDiaryItem) {
      window.alert("존재하지 않는 일기입니다.");
      nav("/", {replace: true})
    }
    setCurrentDiaryItem(currentDiaryItem);
  }, [id]);

  return currentDiaryItem;
};

export default useDiary;