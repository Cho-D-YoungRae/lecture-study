import './Main.css';

/**
 * JSX 주의 사항
 * 1. 중괄호 내부에는 자바스크립트 표현식만 넣을 수 있다.
 * 2. 숫자, 문자, 배열 값만 렌더링 된다.
 * 3. 모든 태그는 닫혀있어야 한다.
 * 4. 최상위 태그는 반드시 하나여야만 한다. -> 사용할만한 태그가 없을 경우 빈 태그 사용 가능. 실제 렌더링에는 해당 태그는 없음.
 */
export default function Main() {
  const number = 10;
  const user = {
    name: "cho",
    isLogin: true,
  }

  return (
    <main>
      <h1>main</h1>
      <h2>{number + 10}</h2>
      <h2>{number % 2 === 0 ? "짝수" : "홀수"}</h2>
      {user.isLogin ? (
        <h2>{user.name}님 환영합니다.</h2>
      ) : (
        <h2>로그인 해주세요.</h2>
      )}
      <div style={{
        backgroundColor: "red",
        borderBottom: "5px solid blue",
      }}>
        스타일
      </div>
      <div className="logout">
        외부 스타일
      </div>
    </main>
  );
}