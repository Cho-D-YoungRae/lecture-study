import React, {useState} from 'react';

/**
 * 간단한 회원가입 폼
 * 1. 이름
 * 2. 생년월일
 * 3. 국적
 * 4. 자기소개
 *
 * select 의 옵션은 맨 위 값이 처음에 표출되기 때문에 이를 위해 국적이라는 옵션 추가
 */
function Register(props) {
  const [name, setName] = useState("");
  const [birth, setBirth] = useState("");
  const [country, setCountry] = useState("");
  const [bio, setBio] = useState("");

  const onChangeName = (e) => {
    setName(e.target.value);
  };

  const onChangeBirth = (e) => {
    setBirth(e.target.value);
  };

  const onChangeCountry = (e) => {
    setCountry(e.target.value);
  };

  const onChangeBio = (e) => {
    setBio(e.target.value);
  }

  return (
    <div>
      <div>
        <input
          value={name}
          onChange={onChangeName}
          placeholder={"이름"}
        />
      </div>
      <div>
        <input
          value={birth}
          onChange={onChangeBirth}
          type="date"
        />
      </div>

      <div>
        <select value={country} onChange={onChangeCountry}>
          <option value=""></option>
          <option value="korea">한국</option>
          <option value="usa">미국</option>
          <option value="uk">영국</option>
        </select>
      </div>

      <div>
        <textarea
          placeholder={"자기소개"}
          value={bio}
          onChange={onChangeBio}
        />
      </div>
    </div>
  );
}

export default Register;