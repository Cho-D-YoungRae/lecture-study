import React, {useState} from "react";
import Try from "./Try";

function getNumbers() { // 숫자 네 개를 겹치지 않고 랜덤하게 뽑는 함수
  const candidate = [1, 2, 3, 4, 5, 6, 7, 8, 9];
  const array = [];
  for (let i = 0; i < 4; i++) {
    const chosen = candidate.splice(Math.floor(Math.random() * (9 - i)), 1)[0];
    array.push(chosen);
  }
  return array;
}

const NumberBaseball = () => {

  const [result, setResult] = useState("");
  const [value, setValue] = useState("");
  // 함수 컴포넌트 특성상 매번 리렌더링될 때마다 함수 전체가 다시 실행되는데
  // 그래도 useState 는 매번 실행되도 처음 한번만 적용됨
  // 그래서 getNumbers() 가 매번 실행되도 처음 한번만 적용됨
  // 그러나 매번 실행되긴 함
  // 매번 실행되지 않도록 하려면 값을 넣지 않고 함수 자체를 넣어주면 됨
  // const [answer, setAnswer] = useState(getNumbers());
  // 함수를 넣으면 처음 렌더링될 때만 실행됨
  // lazy init
  const [answer] = useState(getNumbers);
  const [tries, setTries] = useState([]);

  const onSubmitForm = (e) => {
    e.preventDefault();
    console.log(answer);
    if (value === answer.join("")) {
      const result = "홈런!"
      setResult(result);
      setTries((prevTries) => {
        return [...prevTries, {try: value, result: result}]
      });
    } else {
      const answerArray = value.split("").map((v) => parseInt(v));
      if (tries.length >= 9) {
        this.setState({
          result: `10번 넘게 틀려서 실패! 답은 ${answer.join(",")}였습니다!`
        });
        alert("게임을 다시 시작합니다.");
        setValue("");
        setAnswer(getNumbers());
        setTries([]);
      } else {
        let strike = 0;
        let ball = 0;
        for (let i = 0; i < 4; i++) {
          if (answerArray[i] === answer[i]) {
            strike += 1;
          } else if (answer.includes(answerArray[i])) {
            ball += 1;
          }
        }
        console.log(strike, ball);
        setValue("");
        setTries((prevTries) => {
          return [...prevTries, {try: value, result: `${strike} 스트라이크, ${ball} 볼입니다.`}]
        });
      }
    }
  }

  const onChangeInput = (e) => {
    setValue(e.target.value);
  }

  return (<>
    <h1>{result}</h1>
    <form onSubmit={onSubmitForm}>
      <input maxLength={4} value={value} onChange={onChangeInput}/>
      <button>입력!</button>
    </form>
    <div>시도: {tries.length}</div>
    <ul>
      {tries.map((v, i) =>
        <Try key={i + 1} tryInfo={v}></Try>
      )}
    </ul>
  </>)
}

export default NumberBaseball;