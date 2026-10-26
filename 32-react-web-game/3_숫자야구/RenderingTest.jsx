 import React, {Component} from "react";

class RenderingTest extends Component {
  state = {
    counter: 0,
  };

  // state 가 변경되지 않았는데(아래와같이)
  // setState 를 호출하기만 하면 렌더링이 다시 일어남 -> state 값이 바뀌는 것과 무관하게
  onClick = () => {
    this.setState({});
  };

  // shouldComponentUpdate 를 사용하여 렌더링을 막을 수 있음
  // render() 와 같이 리액트 컴포넌트에서 제공하는 함수
  shouldComponentUpdate(nextProps, nextState, nextContext) {
    return this.state.counter !== nextState.counter;
  }

  render() {
    console.log("렌더링", this.state);
    return (
      <div>
        <button onClick={this.onClick}>클릭</button>
      </div>
    );
  }
}

export default RenderingTest;