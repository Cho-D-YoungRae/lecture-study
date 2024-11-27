This is a [Next.js](https://nextjs.org) project bootstrapped with [`create-next-app`](https://nextjs.org/docs/app/api-reference/cli/create-next-app).

## Getting Started

First, run the development server:

```bash
npm run dev
# or
yarn dev
# or
pnpm dev
# or
bun dev
```

Open [http://localhost:3000](http://localhost:3000) with your browser to see the result.

You can start editing the page by modifying `app/page.tsx`. The page auto-updates as you edit the file.

This project uses [`next/font`](https://nextjs.org/docs/app/building-your-application/optimizing/fonts) to automatically optimize and load [Geist](https://vercel.com/font), a new font family for Vercel.

## Learn More

To learn more about Next.js, take a look at the following resources:

- [Next.js Documentation](https://nextjs.org/docs) - learn about Next.js features and API.
- [Learn Next.js](https://nextjs.org/learn) - an interactive Next.js tutorial.

You can check out [the Next.js GitHub repository](https://github.com/vercel/next.js) - your feedback and contributions are welcome!

## Deploy on Vercel

The easiest way to deploy your Next.js app is to use the [Vercel Platform](https://vercel.com/new?utm_medium=default-template&filter=next.js&utm_source=create-next-app&utm_campaign=create-next-app-readme) from the creators of Next.js.

Check out our [Next.js deployment documentation](https://nextjs.org/docs/app/building-your-application/deploying) for more details.

---

## 학습

### 클라이언트 컴포넌트로 전환하기

모든 컴포넌트는 서버 컴포넌트 -> 넥스트 서버에서 돈다

원래 리액트는 클라이언트에서 돌아야함

서버 컴포넌트이면 async 로 비동기로 컴포넌트 만들 수도 있음

대신에 서버 컴포넌트는 훅 사용 못함 => "use client"

그렇다고 모드 클라이언트 컴포넌트로 하냐? -> 그럼 서버 컴포넌트 장점 사용 못함

서버 컴포넌트는 데이터를 다룰 때 유용함

### default.tsx

패러랠 라우트에 대한 기본 값

없을 경우 null 반환

### 인터셉팅 라우트

레이아웃에서 기본 폴더의 페이지들은 children으로 받고 @modal 폴더의 페이지들은 modal로 받음

인터셉팅 라우트는 기본 폴더의 페이지와 @modal 폴더의 페이지가 다르도록 하는 것

> children 은 app/(beforeLogin)/page.tsx, modal 은 app/@modal/i/flow/login/page.tsx

클라이언트에서 라우팅할 때만 인터셉팅 라우트가 적용

패러랠 라우트와 인터셉팅 라우트를 활용하면 기존 화면에서 url 이 바뀐 채 모달을 띄울 수 있다.

그러면 기본 폴더의 페이지는 필요없나? -> 새로고침 했을 때 사용됨, 혹은 브라우저 통해서 처음 접속했을 때

### private folder

주소 창에 안 뜸

서버 컴포넌트는 클라이언트 컴포넌트를 임포트 해도 되는데, 클라이언트 컴포넌트는 서버 컴포넌트를 임포트 하지 말자

> 서버 컴포넌트가 클라이언트 컴포넌트가 됨

### 로그인 모달에서 발생하는 문제 해결하기(router.replace)

서버에서 리다이렉트하면 인터셉팅이 제대로 되지 않음

클라이언트에서 링크를 통해서 이동해야 인터셉팅이 제대로 됨

useRouter 사용

router.replace (/login -> /i/flow/login): 뒤로 가기 시에 /login 이전으로 이동

router.push (/login -> /i/flow/login): 뒤로 가기 시에 /login

모달이 여러개이면 모달 디렉토리를 추가하고 레이아웃에 추가하면 됨

### useSelectedLayoutSegment로 ActiveLink 만들기

useSelectedLayoutSegment 현재 선택된 레이아웃 세그먼트를 가져옴

하위 세그먼트까지 모두 가져오고 싶으면 useSelectedLayoutSegments()를 사용

## 홈탭 만들면서 Context API 적용해보기

처음에 css 할 때 background-color 주면 영역을 보기 쉬워서 좋음. 개발 완료 후 뺴면 됨.

css 에서도 변수 사용 가능

css 모듈 더 사용하고 싶다면 sass 사용

컨텍스트 API는 최적화가 필요할 수 있음 -> 사용해보다가 필요가 생기면

컨텍스트 API 사용하려면 영향을 미치려는 컴포넌트들의 부모여야 함

컨텍스트 API 는 클라이언트 컴포넌트

## PostForm 만들기(타이핑 외우기)

폼 같은 건 웬만하면 클라이언트 컴포넌트

폼의 submit 이벤트는 `e.preventDefault();` 추가하고 시작

## 게시글 만들며 dayjs 사용해보기

몇 초전, 몇 분전, 등.. 기능 구현하기 좋음

새로 시작하는 프로젝트라면 이게 좋음

## classnames로 클래스 합성하기(feat. npmtrends로 라이브러리 고르기)

클래스 이름을 여러개 가질 수 있는데, 조건부로도 설정하고 싶음 -> classnames 사용

> 검색해보니 현재는 clsx 가 더 높음

## /compose/tweet 만들기

클릭하는 버튼이 많은 곳은 웬만하면 클라이언트 컴포넌트

클라이언트 컴포넌트 만들고 나중에 필요에 따라 이벤트를 별도 컴포넌트로 분리해서 서버 컴포넌트로 만들 수있음

기본적으로 state 사용하거나 클릭 등이 있으면 클라이언트 컴포넌트

## usePathname과 /explore 페이지

usePathname은 현재 경로를 가져옴 -> 클라이언트 컴포넌트 (훅은 웬만하면 클라이언트 컴포넌트)

특정 영역에서만 클라이언트 컴포넌트 기능을 사용해서되서 서버 컴포넌트 전체를 클라이언트 컴포넌트로 만들기는 아까움

- 해당 영역만 분리

라디오 버튼을 바꾸고 싶다 -> div 와 ref 로 만드는 것

## useSearchParams와 프로필, /search 페이지

searchParams 프롭스를 사용하면 쿼리파라미터를 받을 수 있음

page.tsx 에는 searchParams 프롭스가 기본적으로 들어있음

보통 컴포넌트로 빼는 것은 클라이언트 컴포넌트가 많음

자식 컴포넌트로 서치파라미터를 넘겨주려면

- 서버 컴포넌트에서 넘겨주거나
- 클라이언트 컴포넌트라면 useSearchParams 사용

마우스 얹었는데 주소가 안뜨면 a태그가 아닐 수 있음 -> 클라이언트 라우팅

## 이벤트 캡처링과 /status/[id] 페이지

onClickCapture 이벤트 캡처링

클릭 이벤트와 a 태그 등 이벤트가 겹칠 때 이벤트 캡처링을 사용

## faker.js와 /photo/[photoId]

searchParams 와 달리 params 를 사용하면 주소의 slug 를 가져올 수 있음

패러랠 라우트할 때는 페이지가 있는데 모달 사용하지 않는곳 default.tsx 를 잊지 말자

## msw 세팅과 버전 업그레이드

package.json 의 devDependencies 에도 기록해주는 것이 좋음

msw 가 자동으로 mockServiceWorker.js 을 브라우저에 설치

mockServiceWorker.js 는 실제 서버 주소로 보내는 주소를 가로채서 가짜 응답을 보내줌

개발 환경용 주소, 배포 환경용 주소 분기처리를 하지 않아도 됨
- 실제 주소로만 요청을 보내고 개발 환경에서는 mockServiceWorker.js 가 가짜 응답을 보내줌

개발환경에서 에러를 발생시켜보거나, 로그인을 안하고 접근하도록 하거나 등을 테스트할 수 있음

넥스트에서는 mockServiceWorker 사용이 살짝 애매함

- 넥스트는 서버에서도 돌고 클라이언트에서 돔
- 서버사이드렌더링 할 때도 mockServiceWorker 가 동작해야함
- 넥스트에서 서버쪽에서 mockServiceWorker 를 자연스럽게 사용할 방식이 나오지 않음
- 여기서는 임시로 노드서버를 활용

`npm i -D @mswjs/http-middleware express cors` ` npm i --save-dev @types/express @types/cors` msw 가짜 목 서버 만들때 필요한 라이브러리(서버에서 돌릴때)

`"mock": "npx tsx watch ./src/mocks/http.ts"` package.json scripts 에 추가

## next용 msw 컴포넌트와 .env

MSW 사용하기 위해 컴포넌트를 생성해야 함

NEXT_PUBLIC 이 붙은 값은 브라우저에서 접근 가능한 환경 변수

NEXT_PUBLIC 없으면 서버에서만 사용가능한 환경 변수

`typeof window !== undefined` 를 msw 컴포넌트에 추가해야 함
- 브라우저에서만 동작하도록 보장하는 것
