import React, {Component} from "react";

class ResponseCheck extends Component {

  state = {
    state: "waiting",
    message: "클릭해서 시작하세요.",
    result: [],
  }

  // 이 값은 변경되어도 렌더링 안됨
  timeout;
  startTime;
  endTime;

  onClickScreen = () => {
    const {state, message, result} = this.state;
    if (state === "waiting") {
      this.setState({
        state: "ready",
        message: "초록색이 되면 클릭하세요.",
      });

      this.timeout = setTimeout(() => {
        this.setState({
          state: "now",
          message: "지금 클릭",
        });
        this.startTime = new Date();
      }, Math.floor(Math.random() * 1000) + 2000); // 2~3초 랜덤
    } else if (state === "ready") { // 성급하게 클릭
      clearTimeout(this.timeout);
      this.setState({
        state: "waiting",
        message: "너무 성급하시군요! 초록색이 된 후에 클릭하세요.",
      });
    } else if (state === "now") { // 반응속도 체크
      this.endTime = new Date();
      console.log(`startTime: ${this.startTime}, endTime: ${this.endTime}`);
      console.log(`result: ${this.endTime - this.startTime}ms`);
      this.setState((prevState) => {
        return {
          state: "waiting",
          message: "클릭해서 시작하세요.",
          result: [...prevState.result, this.endTime - this.startTime],
        }
      });
    }

  }

  onReset = () => {
    this.setState({
      result: [],
    });
  }

  renderAverage = () => {
    return this.state.result.length === 0
      ? null
      : <>
        <div>평균 시간: {this.state.result.reduce((a, c) => a + c) / this.state.result.length}ms</div>
        <button onClick={this.onReset}>리셋</button>
      </>
  }

  render() {
    const {state, message} = this.state;
    return (<>
        <div
          id="screen"
          className={state}
          onClick={this.onClickScreen}
        >
          {message}
        </div>
        {/*false, undefined, null 은 jsx 에서 태그 없음을 의미*/}
        {/*React 에서 조건문 사용하는 방법*/}
        {/*{this.state.result.length === 0*/}
        {/*    ? null*/}
        {/*    : <div>평균 시간: {this.state.result.reduce((a, c) => a + c) / this.state.result.length}ms</div>*/}
        {/*}*/}
        {/*{this.state.result.length !== 0*/}
        {/*    && <div>평균 시간: {this.state.result.reduce((a, c) => a + c) / this.state.result.length}ms</div>*/}
        {/*}*/}
        {this.renderAverage()}
      </>
    );
  }
}

export default ResponseCheck;