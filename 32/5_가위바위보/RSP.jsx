import React, {useEffect, useRef, useState} from "react";

const rspCoords = {
  바위: '0',
  가위: '-142px',
  보: '-284px',
};

const scores = {
  가위: 1,
  바위: 0,
  보: -1,
};

const computerChoice = (imgCoord) => {
  return Object.entries(rspCoords).find(function(v) {
    return v[1] === imgCoord;
  })[0];
}

// 클래스는 가로, 함수는 세로 라고 생각
//                        result, imgCoord, score
// componentDidMount
// componentDidUpdate
// componentWillUnmount

// componentDidMount() {
//   this.setState({
//     imgCoord: 3,
//     score: 1,
//     result: 2,
//   })
// }

// useEffect(() => {
//   setImgCoord();
//   setScore();
// }, [imgCoord, score]);
// useEffect(() => {
//   setResult();
// }, [result]);

const RSP = () => {
  const [result, setResult] = useState("");
  const [imgCoord, setImgCoord] = useState(rspCoords.바위);
  const [score, setScore] = useState(0);
  const interval = useRef();

  // 함수 컴포넌트는 렌더링이 될 때마다 함수가 통째로 다시 실행됨
  // useEffect 안에 있는 것도 매번 실행됨
  // 그래서 1대1 대응은 아니라고 하는 것을 보임

  // componentDidMount, componentDidUpdate 역할(1대1 대응은 아님)
  // 두번째 인자 배열에 무엇을 넣는지에 따라 실행 조건이 달라짐 -> 클로저 문제 해결
  // 배열에는 꼭 useEffect 를 다시 실행할 값만 넣어야 함
  // 빈 배열이면 componentDidMount
  useEffect(() => {
    console.log('다시 실행');
    interval.current = setInterval(changeHand, 100);

    return () => {  // componentWillUnmount 역할
      console.log('종료');
      clearInterval(interval.current);
    }
  }, [imgCoord]);

  const changeHand = () => {
    if (imgCoord === rspCoords.바위) {
      setImgCoord(rspCoords.가위);
    } else if (imgCoord === rspCoords.가위) {
      setImgCoord(rspCoords.보);
    } else if (imgCoord === rspCoords.보) {
      setImgCoord(rspCoords.바위);
    }
  }

  const onClickBtn = (choice) => () => {
    clearInterval(interval.current);
    const myScore = scores[choice];
    const cpuScore = scores[computerChoice(imgCoord)];
    const diff = myScore - cpuScore;
    if (diff === 0) {
      setResult('비겼습니다!');
    } else if ([-1, 2].includes(diff)) {
      setResult('이겼습니다!');
      setScore((prevScore) => prevScore + 1);
    } else {
      setResult('졌습니다!');
      setScore((prevScore) => prevScore - 1);
    }
    setTimeout(() => {
      interval.current = setInterval(changeHand, 100);
    }, 1000);
  }

  return (<>
    <div id="computer" style={{background: `url(https://en.pimg.jp/023/182/267/1/23182267.jpg) ${imgCoord} 0`}}/>
    <div>
      <button id="rock" className="btn" onClick={onClickBtn('바위')}>바위</button>
      <button id="scissor" className="btn" onClick={onClickBtn('가위')}>가위</button>
      <button id="paper" className="btn" onClick={onClickBtn('보')}>보</button>
    </div>
    <div>{result}</div>
    <div>현재 {score}점</div>
  </>);
}

export default RSP;