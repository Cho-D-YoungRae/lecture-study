const React = require("react");
const {Component} = React;

class WordRelayClass extends Component {
  state = {
    word: '영래초',
    value: '',
    result: '',
  };

  onSubmitForm = (e) => {
    e.preventDefault();
    if (this.state.word[this.state.word.length - 1] === this.state.value[0]) {
      this.setState({
        result: "딩동댕",
        word: this.state.value,
        value: "",
      })
    } else {
      this.setState({
        result: "땡",
        value: "",
      })
    }

    this.input.focus();
  }

  onChangeInput = (e) => {
    this.setState({ value: e.currentTarget.value });
  }

  onRefInput = (c) => {
    this.input = c;
  }

  render() {
    return (<>
      <div>{this.state.word}</div>
      <form onSubmit={this.onSubmitForm}>
        <input ref={this.onRefInput} value={this.state.value} onChange={this.onChangeInput}/>
        <button>입력!</button>
      </form>
    </>)
  }
}

module.exports = WordRelayClass;