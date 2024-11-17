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

### 2.6 클라이언트 컴포넌트로 전환하기

모든 컴포넌트는 서버 컴포넌트 -> 넥스트 서버에서 돈다

원래 리액트는 클라이언트에서 돌아야함

서버 컴포넌트이면 async 로 비동기로 컴포넌트 만들 수도 있음

대신에 서버 컴포넌트는 훅 사용 못함 => "use client"

그렇다고 모드 클라이언트 컴포넌트로 하냐? -> 그럼 서버 컴포넌트 장점 사용 못함

서버 컴포넌트는 데이터를 다룰 때 유용함

### 2.7 default.tsx

패러랠 라우트에 대한 기본 값

없을 경우 null 반환

