# Chapter 1. 구구단

리액트는 자바스크립트다

리액트는 데이터 중심으로 움직인다

컴포넌트는 데이터와 화면을 하나로 묶어둔 덩어리

옛날에는 화면 먼저 생각하고 데이터를 자바스크립트로 바꿨는데, 리액트는 정반대로 생각해야함

화면의 바뀔 부분을 state 로 만든다고 생각

변하는 것들을 state 로 만듬

예전에는 render() 에서 반드시 최상단에 `<div>` 로 감싸줬어야했는데, 개선이 되어서 쓸데없는 `<div>` 대신 `<>` 로 감싸줄 수 있음

- 이렇게 하면 html 상에서 div 로 감싸져있지 않음
- 쓸데없이 div 로 감싸져있으면 css 적용할때 번거롭거나 하는 등의 문제가 생길 수 있음

react 는 document 같은 것을 잘 사용 안함

- react 가 화면을 컨트롤하게 해주는 것이 좋음
- react 가 화면을 컨트롤하게 해주고 우리는 데이터만 조작한다고 생각
- 화면을 조작할 때 `ref` 사용

state 를 바꾸면 렌더링(`render()`) 발생

- 렌더링을 너무 많이하면 속도가 느려짐
- 렌더링이 오래걸린다면 state 변경될 때 속도에 영향을 줄 수 있음
- 그런 의미에서 class 컴포넌트에서 jsx 안(render 안)에 함수가 들어있으면 렌더링할 때마다 함수가 생성되어야 함
  - class 에서 함수 별도로 빼주자

클래스 컴포넌트에서 `useState` 를 사용하면 `setState` 할 때 state 의 모든 값들을 해주어야함  
-> 함수 컴포넌트에서는 변경되는 값만 부분적으로 변경해주면 되는데...

## 순서

1. like-button.html
2. like-button-jsx.html
3. like-button-jsx-function.html
4. Gugudan.html
