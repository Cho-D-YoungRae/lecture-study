import React, {Component, createRef} from "react";
import Try from "./TryClass";

// 이 함수도 클래스 안에 들어갈 수 있음
// this 를 사용하지 않을 때는 클래스 밖에 둘 수 있음
// 밖에 빼면 다른 곳에서도 사용할 수 있다는 장점은 있을 수 있음...
// Hooks 로 바꿀때도 클래스에 독립적으로 존재하기 때문에 편함
function getNumbers() { // 숫자 네 개를 겹치지 않고 랜덤하게 뽑는 함수
  const candidate = [1, 2, 3, 4, 5, 6, 7, 8, 9];
  const array = [];
  for (let i = 0; i < 4; i++) {
    const chosen = candidate.splice(Math.floor(Math.random() * (9 - i)), 1)[0];
    array.push(chosen);
  }
  return array;
}

class NumberBaseball extends Component {

  state = {
    result: '',
    value: '',
    answer: getNumbers(),
    tries: [],
  }

  // 화살표 함수를 사용하지 않고 아래와 같이 사용할 경우
  // this 를 사용할 수가 없어서(this 가 undefined) this.state, this.setState 를 사용할 수 없음
  // 아래와 같은 형태의 함수를 사용하고 싶으면 constructor 를 사용해서 그 안에 this.onSubmitForm = this.onSubmitForm.bind(this) 를 해줘야 함
  // 화살표 함수를 사용하면 bind(this) 를 자동으로 해줌
  // onSubmitForm() {
  // }
  onSubmitForm = (e) => {
    e.preventDefault();
    console.log(this.state.answer);
    if (this.state.value === this.state.answer.join("")) {
      const result = "홈런!"
      // 예전 값을 이용해 현재 값을 만들 때는 함수형 setState 를 사용해야 함
      // 그래야 setState 를 연달아 사용할 때 문제가 발생하지 않음
      this.setState((prevState) => {
        return {
          result: result,
          tries: [...prevState.tries, {try: this.state.value, result: result}]
        }
      })
    } else {
      const answerArray = this.state.value.split("").map((v) => parseInt(v));
      if (this.state.tries.length >= 9) {
        this.setState({
          result: `10번 넘게 틀려서 실패! 답은 ${this.state.answer.join(",")}였습니다!`
        });
        alert("게임을 다시 시작합니다.");
        this.setState({
          value: "",
          answer: getNumbers(),
          tries: []
        })
      } else {
        let strike = 0;
        let ball = 0;
        for (let i = 0; i < 4; i++) {
          if (answerArray[i] === this.state.answer[i]) {
            strike += 1;
          } else if (this.state.answer.includes(answerArray[i])) {
            ball += 1;
          }
        }
        console.log(strike, ball);
        this.setState((prevState) => {
          return {
            tries: [...prevState.tries, {
              try: this.state.value,
              result: `${strike} 스트라이크, ${ball} 볼입니다.`
            }],
            value: ""
          }
        })
      }
    }
    this.inputRef.current.focus();
  }

  onChangeInput = (e) => {
    this.setState({
      value: e.target.value
    })
  }

  inputRef = createRef()

  // 리액트에서는 input 태그의 maxlength 속성을 maxLength 으로 사용해야 함
  // Try 를 컴포넌트로 빼주면서
  // -> 코드 관리를 깔끔하게
  // -> 반복문에서 성능 문제가 많이 발생하는데, 성능 최적화하기 좋음
  // -> 재사용성
  // -> NumberBaseball 은 Try 의 부모 컴포넌트
  render() {
    return (<>
      {/*jsx 주석은 이렇게 해야함*/}
      {/*js 의 블럭 주석을 중괄호로 감싸줬다고 생각*/}
      <h1>{this.state.result}</h1>
      <form onSubmit={this.onSubmitForm}>
        <input ref={this.inputRef} maxLength={4} value={this.state.value} onChange={this.onChangeInput}/>
        <button>입력!</button>
      </form>
      <div>시도: {this.state.tries.length}</div>
      <ul>
        {this.state.tries.map((v, i) =>
          <Try key={i + 1} tryInfo={v}></Try>
        )}
      </ul>
    </>)
  }
}

export default NumberBaseball;