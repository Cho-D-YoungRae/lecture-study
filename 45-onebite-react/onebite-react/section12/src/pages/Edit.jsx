import React, {useContext, useEffect, useState} from 'react';
import {useNavigate, useParams} from "react-router-dom";
import Header from "../components/Header.jsx";
import Button from "../components/Button.jsx";
import {DiaryDispatchContext, DiaryStateContext} from "../App.jsx";
import Editor from "../components/Editor.jsx";
import useDiary from "../hooks/useDiary.jsx";

/*
navigate 는 컴포넌트가 렌더링된 이후에 실행될 수 있다.
=> 첫 렌더링 시 실행하려면 useEffect 내부에서 사용해야 한다.
 */
function Edit() {
  const {id} = useParams();
  const nav = useNavigate();
  const {onDelete, onUpdate} = useContext(DiaryDispatchContext);
  const currentDiaryItem = useDiary(id);

  const onClickDelete = () => {
    if (window.confirm("일기를 정말 삭제할까요? 다시 복구되지 않아요!")) {
      onDelete(id);
      nav("/", {replace: true});
    }
  };

  const onSubmit = (input) => {
    if (window.confirm("일기를 정말 수정할까요?")) {
      onUpdate(
        id,
        input.createdDate.getTime(),
        input.emotionId,
        input.content
      );
      nav("/", {replace: true});
    }
  };

  return (
    <div>
      <Header
        title="일기 수정하기"
        leftChild={<Button
          text="< 뒤로 가기"
          onClick={() => nav(-1)}
        />}
        rightChild={<Button
          text="삭제하기"
          type="NEGATIVE"
          onClick={onClickDelete}
        />}
      />
      <Editor initData={currentDiaryItem} onSubmit={onSubmit} />
    </div>
  );
}

export default Edit;