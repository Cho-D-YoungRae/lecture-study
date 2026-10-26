 import React, {PureComponent} from "react";

// PureComponent 는 shouldComponentUpdate 를 이미 구현해놓은 것
class RenderingTest extends PureComponent {
  state = {
    counter: 0,
  };

  onClick = () => {
    this.setState({});
  };


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