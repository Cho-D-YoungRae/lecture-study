# Chapter 7. 틱택토

useReducer

- 리액트에 훅이 들어오면서 같이 생김
- 리덕스의 핵심기능인 reducer 기능을 그대로 들여옴
- 리덕스와 비슷한 효과를 낼 수 있음
- 그렇다면 Context API 와 useReducer 를 사용하면 리덕스를 사용하지 않아도 될까?
  - 소규모에서는 이 두 조합으로 충분할 수 있음
  - 규모가 커지면 결국 리덕스를 사용하게 됨
  - 이 둘만으로는 비동기 처리가 어려움 -> 비동기 부분 처리를 위해 결국 리덕스를 사용하게 됨

TicTacToe -> Table -> Tr -> Td

- 컴포넌트 계층이 깊어짐
- state 는 가장 부모인 TicTacToe 에서 관리하고, 실제로 클릭하는 애들은 Td -> TicTacToe, Td 간 간격이 큼
- 전달해줘야 하는 데이터가 많으면 더 번거로워짐
- ContextAPI 이런 데이터 전달의 거리가 멀어진 것 자체를 해결해줌
- useReducer 는 state 자체의 개수를 줄임

state 가 있고 특정 이벤트가 발생했을 때

- state 를 수정하려면 -> action 을 dispatch 해야 함
- action 을 어떻게 처리할지 -> reducer 에서 처리함