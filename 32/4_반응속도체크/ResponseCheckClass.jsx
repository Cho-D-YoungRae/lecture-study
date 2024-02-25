import React, {Component} from "react";

class ResponseCheck extends Component {

  state = {
    state: "waiting",
    message: "클릭해서 시작하세요.",
  }

  onClickScreen = () => {

  }

  render() {
    return (<>
      <div
        id="screen"
        className={this.state.state}
        onClick={this.onClickScreen}
      >
        {this.state.message}
      </div>
      <div>평균 시간: {this.state.result.reduce((a, c) => a + c) / this.state.result.length}ms</div>
    </>)
  }
}

export default ResponseCheck;