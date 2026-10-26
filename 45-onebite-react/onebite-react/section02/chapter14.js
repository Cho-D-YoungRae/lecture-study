async function getData() {
  return {
    name: "Cho",
    id: "asdf"
  };
}

console.log(getData());

// await: async 함수 내부에서만 사용 가능. 비동기 함수가 다 처리되기를 기다림
async function printData() {
  const data = await getData();
  console.log(data);
}

printData();