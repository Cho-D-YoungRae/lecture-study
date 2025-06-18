import React, {useRef, useState} from 'react';

/**
 * ref 를 사용하지 않고 let 으로 변수를 사용하면 리렌더링 될 때마다 초기화된다.
 * let 을 통해 전역 변수로 사용한다면 해당 컴포넌트가 여러개일 때 값을 공유한다.
 */
function Register(props) {
  const [input, setInput] = useState({
    name: "",
    birth: "",
    country: "",
    bio: ""
  });

  const countRef = useRef(0);
  const inputRef = useRef();


  const onChange = (e) => {
    console.log(e.target.name, e.target.value);
    countRef.current += 1;
    console.log("변경 횟수:", countRef.current);
    setInput({
      ...input,
      [e.target.name]: e.target.value
    })
  };

  const onSubmit = (e) => {
    if (input.name === "") {
      inputRef.current.focus();
    }
  }

  return (
    <div>
      <div>
        <input
          value={input.name}
          onChange={onChange}
          name="name"
          placeholder={"이름"}
          ref={inputRef}
        />
      </div>
      <div>
        <input
          value={input.birth}
          onChange={onChange}
          name="birth"
          type="date"
        />
      </div>

      <div>
        <select value={input.birth} onChange={onChange} name="country">
          <option value=""></option>
          <option value="korea">한국</option>
          <option value="usa">미국</option>
          <option value="uk">영국</option>
        </select>
      </div>

      <div>
        <textarea
          placeholder={"자기소개"}
          value={input.bio}
          onChange={onChange}
          name="bio"
        />
      </div>
      <button onClick={onSubmit}>제출</button>
    </div>

  );
}

export default Register;