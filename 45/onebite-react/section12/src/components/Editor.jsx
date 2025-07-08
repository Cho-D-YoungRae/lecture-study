import './Editor.css';
import React, {useState} from 'react';
import EmotionItem from "./EmotionItem.jsx";
import Button from "./Button.jsx";
import {useNavigate} from "react-router-dom";

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
];

function Editor({ onSubmit }) {

  const nav = useNavigate();

  const [input, setInput] = useState({
    createdDate: new Date(),
    emotionId: 3,
    content: "",
  });

  const onChangeInput = (e) => {
    console.log(e.target.name);
    console.log(e.target.value);

    const name = e.target.name;
    let value = e.target.value;

    if (name === "createdDate") {
      value = new Date(e.target.value);
    }

    setInput({
      ...input,
      [name]: value
    });
  };

  const onClickSubmitButton = () => {
    onSubmit(input);
  }

  return (
    <div className="Editor">
      <section className="date_section">
        <h4>오늘의 날짜</h4>
        <input
          type="date"
          name="createdDate"
          onChange={onChangeInput}
          value={input.createdDate.toISOString().slice(0, 10)}
        />
      </section>
      <section className="emotion_section">
        <h4>오늘의 감정</h4>
        <div className="emotion_list_wrapper">
          {emotionList.map((item) =>
            <EmotionItem
              onClick={() => onChangeInput({
                target: {
                  name: "emotionId",
                  value: item.id
                }
              })}
              key={item.id}
              {...item}
              selected={item.id === input.emotionId}
            />
          )}
        </div>
      </section>
      <section className="content_section">
        <h4>오늘의 일기</h4>
        <textarea
          name="content"
          placeholder="오늘은 어땠나요?"
          onChange={onChangeInput}
          value={input.content}
        />
      </section>
      <section className="button_section">
        <Button text="취소하기" onClick={() => nav(-1)}/>
        <Button text="작성 완료" type="POSITIVE" onClick={onClickSubmitButton}/>
      </section>
    </div>
  );
}

export default Editor;