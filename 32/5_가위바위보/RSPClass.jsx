import React, {Component} from 'react';


// 클래스의 경우 -> constructor -> render -> ref -> componentDidMount
// (setState/props 바뀔때) -> shouldComponentUpdate(true) -> render -> componentDidUpdate
// 부모가 나를 없앴을 때 -> componentWillUnmount -> 소멸

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

export default class RSP extends Component {

  state = {
    result: "",
    imgCoord: rspCoords.바위,
    score: 0,
  }

  interval;

  // 컴포넌트가 첫 렌더링된 후
  // 여기에서 비동기 요청을 많이 함
  // ex) setInterval, setTimeout 을 많이 함
  componentDidMount() {
    // 변수를 바깥에 선언하면 안됨
    // 클로저 문제 발생
    // 비동기 함수가 바깥의 변수를 참조하면 클로저 문제 발생
    // const {imgCoord} = this.state;
    this.interval = setInterval(this.changeHand, 100);
  }

  changeHand = () => {
    const {imgCoord} = this.state;
    if (imgCoord === rspCoords.바위) {
      this.setState({
        imgCoord: rspCoords.가위,
      });
    } else if (imgCoord === rspCoords.가위) {
      this.setState({
        imgCoord: rspCoords.보,
      });
    } else if (imgCoord === rspCoords.보) {
      this.setState({
        imgCoord: rspCoords.바위,
      });
    }
  };

  // 리렌더링 후
  // componentDidUpdate(prevProps, prevState, snapshot) {
  // }

  // 컴포넌트가 제거되기 직전
  // 비동기 요청 정리를 많이 함
  // -> 정리해주지 않으면 메모리 누수 발생 가능
  // ex) setInterval, setTimeout 을 제거함
  componentWillUnmount() {
    clearInterval(this.interval);
  }

  onClickBtn = (choice) => () => {
    clearInterval(this.interval);
    const myScore = scores[choice];
    const cpuScore = scores[computerChoice(this.state.imgCoord)];
    const diff = myScore - cpuScore;
    if (diff === 0) {
      this.setState({
        result: '비겼습니다!',
      });
    } else if ([-1, 2].includes(diff)) {
      this.setState((prevState) => {
        return {
          result: '이겼습니다!',
          score: prevState.score + 1,
        }
      });
    } else {
      this.setState((prevState) => {
        return {
          result: '졌습니다!',
          score: prevState.score - 1,
        }
      });
    }
    setTimeout(() => {
      this.interval = setInterval(this.changeHand, 100);
    }, 2000);
  }

  render() {
    const { result, score, imgCoord } = this.state;
    return (<>
      <div id="computer" style={{background: `url(https://en.pimg.jp/023/182/267/1/23182267.jpg) ${imgCoord} 0`}}/>
      <div>
        <button id="rock" className="btn" onClick={this.onClickBtn('바위')}>바위</button>
        <button id="scissor" className="btn" onClick={this.onClickBtn('가위')}>가위</button>
        <button id="paper" className="btn" onClick={this.onClickBtn('보')}>보</button>
      </div>
      <div>{result}</div>
      <div>현재 {score}점</div>
    </>);
  }
}
