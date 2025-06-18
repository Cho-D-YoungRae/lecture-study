import React, {useState} from 'react';

function Register(props) {
  const [input, setInput] = useState({
    name: "",
    birth: "",
    country: "",
    bio: ""
  });


  const onChange = (e) => {
    console.log(e.target.name, e.target.value);
    setInput({
      ...input,
      [e.target.name]: e.target.value
    })
  }

  return (
    <div>
      <div>
        <input
          value={input.name}
          onChange={onChange}
          name="name"
          placeholder={"이름"}
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
    </div>
  );
}

export default Register;