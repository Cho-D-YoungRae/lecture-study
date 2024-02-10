"use client";
import { useParams, useRouter } from "next/navigation";
import { useEffect, useState } from "react";

 // Client Component 로 사용
export default function Update() {
  // onSubmit 은 사용자와 상호작용 -> Server Component 에서 다루지 않음
	const [title, setTitle] = useState('');
	const [body, setBody] = useState('');
	const router = useRouter();
	const params = useParams();
	const id = params.id;
	useEffect(() => {
		fetch(`http://localhost:9999/topics/${id}`)
		.then(resp => resp.json())
		.then(result => {
			setTitle(result.title);
			setBody(result.body);
		});
	}, []);
  return(
    <form
      onSubmit={(event) => {
        event.preventDefault();
				const title = event.target.title.value;
				const body = event.target.body.value;
				const options = {
					method: 'PATCH',
					headers: {
						'Content-Type': 'application/json',
					},
					body: JSON.stringify({ title, body }),
				};
				fetch(`http://localhost:9999/topics/${id}`, options)
					.then((resp) => resp.json())
					.then(result => {
						console.log(result);
						const lastid = result.id;
						// router.refresh()는 서버 컴포넌트를 강제로 다시 랜더링 하도록 하는 기능입니다. 
						// 이 함수를 호출하지 않으면 서버의 데이터를 변경했음에도 서버 컴포넌트가 그대로 입니다.
						router.refresh();
						router.push(`/read/${lastid}`);
					})
      }}
    >
      <p>
        <input type="text" name="title" placeholder="title" value={title} onChange={
					event => setTitle(event.target.value)
				}/>
      </p>
      <p>
        <textarea name="body" placeholder="body" value={body} onChange={
					event => setBody(event.target.value)
				}></textarea>
      </p>
      <p>
        <input type="submit" value="update" />
      </p>
    </form>
  );
}
