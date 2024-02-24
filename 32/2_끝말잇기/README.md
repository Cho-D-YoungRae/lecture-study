# Chapter 2. 끝말잇기

웹팩을 사용하는 이유

- html 하나만 있어도 js 를 쓸 수 있는데 실제로는 컴포넌트가 하나로 되어있지 않음
- script 태그를 통해 다른 js 를 가져올 수 있으나 이런식으로 하면 script 간 중복이 발생할 수 있음
- 웹팩을 사용하면 여러개의 js 파일을 합쳐서 1개의 js 파일로 만들어줌
  - 그러면서 쓸데없는 코드들 없애주고[package.json](package.json)
    - ex) console.log
  - 그러면서 babel 도 적용할 수 있고
- 웹팩을 하려면 node 를 알아야 함
  - node 가 백엔드라고 생각하는 것은 오해...
  - node 는 js 를 실행할 수 있는 환경

create-react-app 은 webpack 과 react 환경 세팅 등을 자동으로 해주는 것

package

- `@babel/core` : babel 의 기본적인 기능 -> 최신 문법을 옛날 문법으로 바꿔주는 것
- `@babel/preset-env` : 우리 브라우저에 맞게 알아서 바꿔주는 것
- `@babel/preset-react` : react 의 jsx 를 바꿔주는 것
- `babel-loader` : webpack 이 babel 을 사용할 수 있게 해주는 것
  - webpack.config.js 에서 설정해줘야 함

target vs currentTarget

- target : 이벤트가 시작된 위치(태그)
- currentTarget : 이벤트 핸들러와 연결된 위치(태그)

form 을 사용할 때는 value 와 onChange 를 사용해야 함. 혹은 defaultValue 를 사용해야 함.

`@pmmmwh/react-refresh-webpack-plugin` 를 사용하면 hot reload 가 가능함

`@pmmmwh/react-refresh-webpack-plugin` 를 사용하면 `react-refresh` 를 사용할 수 있음

위 두 플러그인을 사용하지 않아도 리로딩은 가능함 -> 리로딩은 새로고침

- 새로고침되면 기존 데이터가 다 날아감
- 핫 리로딩은 기존 데이터 유지하면서 화면 변경

컨트롤드 인풋 VS 언 컨트롤드 인풋

- 컨트롤드 인풋
  - value 와 onChange 를 사용 -> value 에서는 state 를 사용
- 언컨트롤드 인풋
  - value 와 onChange 를 사용하지 않음
  - defaultValue 는 사용 가능
  - value 를 사용하면 컨트롤드 인풋으로 간주될 수 있음
- 리액트에서는 컨트롤드 인풋을 권장
- 언컨트롤드 인풋이 더 간단하긴 함
  - 앱이 간단하다면 언컨트롤드 인풋으로 충분하지만, 복잡해지면 컨트롤드 인풋을 사용하는 것이 좋음
- value 가 onSubmit 에서만 사용될 경우 언컨트롤드 인풋을 사용해도 됨

언컨트롤드 인풋에서 안되는 것

> [참고](https://goshacmd.com/controlled-vs-uncontrolled-inputs-react/)

- instant field validation : input 을 입력할 때마다 바로바로 검증하는 것
- dynamic inputs: 입력에 따라 input 이 동적으로 변하는 것
- conditionally disabling submit button: 조건에 따라 submit 이 안되도록
  - 언컨트롤드 인풋으로도 비슷한 효과를 할 수 있는데 이거는 submit 버튼이 눌리고, 이벤트 안에서 막는 것이고
  - 컨트롤드 인풋은 submit 버튼 자체를 막는 것
- enforcing input format: 입력 형식을 강제하는 것
- several inputs for one piece of data: 여러개의 input 을 하나의 데이터로 사용하는 것
  - 언컨트롤드 인풋에서는 submit 에서 target 을 통해 조합해야하는데
  - 컨트롤드 인풋에서는 state 에서 조합할 수 있음