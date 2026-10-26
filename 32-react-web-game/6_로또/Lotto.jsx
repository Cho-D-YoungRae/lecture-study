import React, {useCallback, useEffect, useMemo, useRef, useState} from "react";
import Ball from "./Ball";

function getWinNumbers() {
  console.log("genWinNumbers");
  const candidate = Array(45).fill().map((v, i) => i + 1);
  const shuffle = [];
  while(candidate.length > 0) {
    shuffle.push(candidate.splice(Math.floor(Math.random() * candidate.length), 1)[0]);
  }
  const bonusNumber = shuffle[shuffle.length - 1];
  const winNumbers = shuffle.slice(0, 6).sort((p,c) => p - c);
  return [...winNumbers, bonusNumber];
}

// Hooks 는 조건문 안에 절대 넣으면 안되고, 함수나 반복문 안에도 웬만하면 넣지 않는다
// - 실행 순서가 중요함
const Lotto = () => {
  // 아래와 같은 경우 getWinNumbers 가 계속 실행되는 문제가 있음
  // useState 안에 함수를 넣어주면 해당 함수가 실행되지 않음
  // const [winNumbers, setWinNumbers] = useState(getWinNumbers());
  // useMemo 를 사용하면 해당 함수가 다시 실행되지 않음
  // 배열에 요소가 있으면 그 값이 바뀔때만 다시 실행됨
  // useMemo VS useCallback
  // -> useMemo: 결과값을 기억
  // -> useCallback: 함수 자체를 기억
  const lottoNumbers = useMemo(() => getWinNumbers(), []);
  const [winNumbers, setWinNumbers] = useState(lottoNumbers);
  const [winBalls, setWinBalls] = useState([]);
  const [bonus, setBonus] = useState(null);
  const [redo, setRedo] = useState(false)
  const timeouts = useRef([]);

  // 함수 컴포넌트는 리렌더링할 때 함수 전체가 재실행 되는데
  // 그러면서 그 안의 함수들도 재생성됨
  // useCallback 을 통해서 함수를 기억하면 함수가 재생성되지 않음
  // 두번째 인자 배열의 요소가 바뀌면 함수가 재생성됨
  // 자식 컴포넌트에 함수를 넘길 때는 useCallback 을 사용해야 함
  // useCallback 을 사용하지 않으면 함수가 계속 생성되어서 전달되며 자식 컴포넌트는 계속 리렌더링됨

  const onClickRedo = useCallback(() => {
    console.log("onClickRedo");
    setWinNumbers(getWinNumbers());
    setWinBalls([]);
    setBonus(null);
    setRedo(false);
    timeouts.current = [];
  }, []);

  // 배열에 요서가 있으면 componentDidMount, componentDidUpdate 역할을 함
  useEffect(() => {
    for (let i = 0; i < winNumbers.length - 1; i++) {
      // let 으로 선언된 변수는 클로저 문제가 안생김
      timeouts.current[i] = setTimeout(() => {
        setWinBalls((prevBalls) => {
          return [...prevBalls, winNumbers[i]];
        });
      }, (i + 1) * 1000)
    }
    timeouts.current[6] = setTimeout(() => {
      setBonus(winNumbers[6]);
      setRedo(true);
    }, 7000);
    return () => {
      timeouts.current.forEach((v) => {
        clearTimeout(v);
      });
    }
  }, [timeouts.current]);

  // componentDidUpdate 만 하도록, componentDidMount X
  // const mounted = useRef(false);
  // useEffect(() => {
  //   if (!mounted.current) {
  //     mounted.current = true;
  //   } else {
  //     console.log("componentDidUpdate");
  //   }
  // }, [바뀌는_값]);

  return (
    <>
      <div>당첨 숫자</div>
      <div id="결과창">
        {winBalls.map((v) => <Ball key={v} number={v} />)}
      </div>
      <div>보너스!</div>
      {bonus && <Ball number={bonus} />}
      {redo && <button onClick={onClickRedo}>한 번 더!</button>}
    </>
  );
}

export default Lotto;