const React = require('react');
const ReactDom = require('react-dom/client');

const WordRelay = require('./WordRelay');

// 아래와 같이 jsx 문법을 사용할 때는 파일 확장자를 jsx 로 해주는 것이 보기 좋음
// -> 인지하는 데 도움을 주는 것
ReactDom.createRoot(document.querySelector('#root')).render(<WordRelay />);
