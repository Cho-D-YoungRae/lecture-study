import { useRef, useEffect } from 'react';

// const [running, setRunning] = useState(true);
// useInterval(() => {
//   console.log('안녕');
// }, running ? 1000 : null);
//
// callback 을 왜 tick 이라는 함수로 감쌌는지?
// callback 이 바뀌어도 새로 setInterval 이 안되지만 최신 callback 을 참조 가능
function useInterval(callback, delay) {
  const savedCallback = useRef();

  useEffect(() => {
    savedCallback.current = callback;
  });

  useEffect(() => {
    function tick() {
      savedCallback.current();
    }

    if (delay !== null) {
      let id = setInterval(tick, delay);
      return () => clearInterval(id);
    }
  }, [delay]);

  return savedCallback.current;
}

export default useInterval;