"use client";
import { useRouter } from "next/navigation";

 // Client Component 로 사용
export default function Create() {
  // onSubmit 은 사용자와 상호작용 -> Server Component 에서 다루지 않음
	const router = useRouter();
  return(
    <form
      onSubmit={(event) => {
        event.preventDefault();
				const title = event.target.title.value;
				const body = event.target.body.value;
				const options = {
					method: 'POST',
					headers: {
						'Content-Type': 'application/json',
					},
					body: JSON.stringify({ title, body }),
				};
				/**
				 * 환경변수는 기밀이 중요한 데이터를 사용할 가능성이 높음
				 * 클라이언트 컴포넌트에 이 환경변수가 노출되면 브라우저에 값이 노출될 수 있음
				 * 환경변수는 서버 컴포넌트에서 접근하는 것이 좋아서 process.env 는 서버 컴포넌트에서 사용 가능
				 * 근데 클라이언트 컴포넌트에서 사용하고 싶다면 NEXT_PUBLIC_ 을 붙여서 사용하면 됨
				 */
				fetch(`${process.env.NEXT_PUBLIC_API_URL}/topics`, options)
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
        <input type="text" name="title" placeholder="title" />
      </p>
      <p>
        <textarea name="body" placeholder="body"></textarea>
      </p>
      <p>
        <input type="submit" value="create" />
      </p>
    </form>
  );
}
