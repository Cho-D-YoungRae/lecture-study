import React, {Component} from 'react';
import Ball from './Ball';

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

class Lotto extends Component {
  state = {
    winNumbers: getWinNumbers(),
    winBalls: [],
    bonus: null,
    redo: false,
  };

  timeouts = [];

  componentDidMount() {
    this.runTimeouts();
  }

  runTimeouts() {
    for (let i = 0; i < this.state.winNumbers.length - 1; i++) {
      // let 으로 선언된 변수는 클로저 문제가 안생김
      this.timeouts[i] = setTimeout(() => {
        this.setState((prevState) => {
          return {
            winBalls: [...prevState.winBalls, this.state.winNumbers[i]],
          }
        });
      }, (i + 1) * 1000)
    }
    this.timeouts[6] = setTimeout(() => {
      this.setState({
        bonus: this.state.winNumbers[6],
        redo: true,
      });
    }, 7000);
  }

  componentDidUpdate(prevProps, prevState, snapshot) {
    console.log("componentDidUpdate");
    // 업데이트하고 싶은 상황을 잘 처리해야 함
    // 이런 설정을 하지 않으면 setState 할 때마다 componentDidUpdate 가 실행됨
    if (prevState.redo === true && this.state.redo === false) {
      console.log("real update");
      this.runTimeouts();
    }
  }

  componentWillUnmount() {
    this.timeouts.forEach((v) => {
      clearTimeout(v);
    });
  }

  onClickRedo = () => {
    this.setState({
      winNumbers: getWinNumbers(),
      winBalls: [],
      bonus: null,
      redo: false,
    });
    this.timeouts = [];
  }

  render() {
    const { winBalls, bonus, redo } = this.state;
    return (
      <>
        <div>당첨 숫자</div>
        <div id="결과창">
          {winBalls.map((v) => <Ball key={v} number={v} />)}
        </div>
        <div>보너스!</div>
        {bonus && <Ball number={bonus} />}
        {redo && <button onClick={this.onClickRedo}>한 번 더!</button>}
      </>
    );
  }
}

export default Lotto;