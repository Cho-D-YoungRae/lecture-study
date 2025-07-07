import './Editor.css';
import React from 'react';
import EmotionItem from "./EmotionItem.jsx";
import Button from "./Button.jsx";

const emotionList = [
  {
    id: 1,
    name: "완전 좋음"
  },
  {
    id: 2,
    name: "좋음"
  },
  {
    id: 3,
    name: "그럭저럭"
  },
  {
    id: 4,
    name: "나쁨"
  },
  {
    id: 5,
    name: "끔찍함"
  },
]

function Editor() {

  const emotionId = 1;

  return (
    <div className="Editor">
      <section className="date_section">
        <h4>오늘의 날짜</h4>
        <input type="date"/>
      </section>
      <section className="emotion_section">
        <h4>오늘의 감정</h4>
        <div className="emotion_list_wrapper">
          {emotionList.map((item) =>
            <EmotionItem
              key={item.id}
              {...item}
              selected={item.id === emotionId}
            />
          )}
        </div>
      </section>
      <section className="content_section">
        <h4>오늘의 일기</h4>
        <textarea placeholder="오늘은 어땠나요?"/>
      </section>
      <section className="button_section">
        <Button text="취소하기"/>
        <Button text="작성 완료" type="POSITIVE"/>
      </section>
    </div>
  );
}

export default Editor;