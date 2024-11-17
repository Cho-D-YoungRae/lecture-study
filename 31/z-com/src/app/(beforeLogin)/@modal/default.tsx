export default function Default() {
  return null;
}

// 주소가 z.com 일 때는 children -> page.tsx, modal -> default.tsx
// 주소가 z.com/i/flow/login 일 때는 children -> i/flow/login/page.tsx, modal -> @modal/i/flow/login/page.tsx