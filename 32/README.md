# [웹 게임을 만들며 배우는 React](https://www.inflearn.com/course/web-game-react/dashboard)

> [github](https://github.com/ZeroCho/react-webgame)

- [ ] 2024/02/20 ~ ing

## React 18

### `useLayoutEffect`

- `useEffect` 는 화면이 그려진 후 실행
- `useLayoutEffect` 는 화면이 그려지기 전에 실행
- 기본으로는 `useEffect` 를 사용
- 아래 상황에서 `useLayoutEffect` 사용
  - state 업데이트 등으로 화면 깜빡임 발생
  - 간발의 차로 리렌더링
- `useEffect` 의 실행 순서를 앞당긴다고 생각

### `useTransition`

- 바로 업데이트할 것과 나중에 업데이트할 것을 구분
- 로딩화면 제공
- ie) 검색 결과 목록 등 API 호출해야하는 것들은 늦게 업데이트될 수 있음

### `useDeferredValue`

- 업데이트가 늦어져도 되는 것
