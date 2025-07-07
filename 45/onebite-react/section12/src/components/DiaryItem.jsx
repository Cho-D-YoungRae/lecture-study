import './DiaryItem.css';
import React from 'react';
import {getEmotionImage} from "../util/get-emotion-image.js";
import Button from "./Button.jsx";
import {useNavigate} from "react-router-dom";

function DiaryItem({id, emotionId, createdDate, content}) {
  const nav = useNavigate();

  return (
    <div className="DiaryItem">
      <div
        onClick={() => nav(`/diary/${id}`)}
        className={`img_section img_section_${emotionId}`}
      >
        <img src={getEmotionImage(emotionId)}/>
      </div>
      <div
        className="info_section"
        onClick={() => nav(`/diary/${id}`)}
      >
        <div className="created_date">
          {new Date(createdDate).toLocaleDateString()}
        </div>
        <div className="content">
          {content}
        </div>
      </div>
      <div className="button_section">
        <Button
          onClick={() => nav(`/edit/${id}`)}
          text="수정하기"
        />
      </div>
    </div>
  );
}

export default DiaryItem;
