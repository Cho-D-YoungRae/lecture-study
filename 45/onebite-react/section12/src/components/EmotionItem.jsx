import './EmotionItem.css'
import React from 'react';
import {getEmotionImage} from "../util/get-emotion-image.js";

function EmotionItem({ id, name, selected }) {
  return (
    <div className={`EmotionItem ${selected ? `EmotionItem_on_${id}` : ""}`}>
      <img className="emotion_img" src={getEmotionImage(id)}/>
      <div className="emotion_name">
        {name}
      </div>
    </div>
  );
}

export default EmotionItem;
