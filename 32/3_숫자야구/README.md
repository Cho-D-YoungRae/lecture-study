# Chapter 3. 숫자 야구

require VS import

- require
  - 노드의 모듈 시스템
  - `module.exports`로 모듈을 만들고 `require`로 모듈을 불러온다.
- import
  - ES2015(ES6)의 모듈 시스템
  - `export`로 모듈을 만들고 `import`로 모듈을 불러온다.
  - `export default NumberBaseball` -> `import NumberBaseball from './NumberBaseball'`
  - `export const hello = 'hello'` -> `import { hello } from './NumberBaseball'`
- import 와 require 과 호환도 됨
  - `import React from 'react'`
  - `const React = require('react')`
- 원래는 `export`/`import`와 `module.exports`/`require`는 다름
  - 웹팩에서는 노드로 웹팩을 돌리기 때문에 `module.exports`/`require` 를 사용해야함 -> 노드는 export 를 지원하지 않음
  - 원래 리액트에서는 `export`/`import`, 노드에서는 `module.exports`/`require` 를 사용해야함
  - 웹팩에 들어있는 바벨이 `import` 를 `require` 로 바꿔줌

리액트에서 반복문 돌리는 것이 살짝 까다롭고(map), 가독성도 안 좋고, 성능도 좋지않음

```js
<ul>
  {[
    {fruit: "사과", taste: "맛있다"},
    {fruit: "바나나", taste: "달다"},
    {fruit: "포도", taste: "시다"},
    {fruit: "귤", taste: "맛없다"},
  ].map((item, i) =>
          <li key={item.fruit}>{i}. <b>{item.fruit}</b> - {item.taste}</li>
  )}
</ul>
```

- map을 쓰면서 key를 써야함
  - 화면에는 표시되지 않음
  - 리액트가 성능 최적화할 때 사용
  - 고유한 값을 사용해야함
- key 에 i 를 사용할 경우 성능 최적화가 안됨
  - i 가 바뀌면 리액트가 다시 렌더링을 해야함
  - i 대신에 고유한 값을 사용해야함
  - 요소가 추가만 되는 배열인 경우 i 를 써도 괜찮긴 함 -> 삭제가 없는 경우

리액트에서 많은 문제가 props 에서 발생함

- 자식 하나만 있는게 아니라 자식의 자식까지 컴포넌트가 점점 깊어질 수 있음
- 이런 것을 잘 관리하기 위한 것이 `redux`, `mobx`, `context api` 등등
- 렌더링이 자주 발생될 수 있음

리액트에는 context 가 있고, 리덕스가 먼저 나오긴 했지만 이후 리덕스도 내부적으로 context 를 쓰도록 변경됨

렌더링이 많이 발생되는 것을 발견하기

- React Devtools - Highlight Updates

state 에 배열, 객체를 중첩으로 복잡하게 하지 않는 것이 좋음 -> 실수하기 좋음

리렌더링되는 경우

- 부모가 리렌더링되면 자식도 리렌더링됨
- props 가 바뀔 때
- state 가 바뀔 때

부모가 리렌더링되도 자식이 리렌더링되지 않게 하는 법

- shouldComponentUpdate
- PureComponent
  - state, props 가 바뀌었을 때만 리렌더링됨
- React.memo -> 함수 컴포넌트에서 사용

그러면 PureComponent 만 쓰냐??

- 컴포넌트가 복잡해지면 PureComponent 만으로는 부족함
- Component 는 shouldComponentUpdate 를 통해 좀 더 커스터마이징 가능
- 실제로는 state, props 가 변경되어도 리렌더링이 필요없는 경우가 있음

어쩃든 성능 최적화는 처음부터 너무 생각하기보다는 문제가 생겼을때 해결하는 것이 좋음

`createRef` 를 사용하면 클래스 컴포넌트에서 ref 를 함수 컴포넌트의 useRef 훅스처럼 사용할 수 있음

render() 안에 setState 들어가면 무한 렌더링 발생하므로 사용하면 안됨

props 를 자식 컴포넌트가 받아서 바꾸면 안됨

- props 는 부모가 바꿔줘야함
- props 는 부모의 state 이므로 자식에서 바꿔주면 부모에서 문제가 발생할 수 있음
- 부모로 받은 props 를 state 로 넣을 수 있음
- props 를 자식이 바꿔줘야 할 경우 props 를 state 로 넣어서 사용
- 하지만 좋은 구조는 아님