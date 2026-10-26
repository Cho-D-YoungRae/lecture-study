const promise1 = new Promise(() => {
  // 비동기 작업을 실행하는 함수
  // executor
  setTimeout(() => {
    console.log("Hello, World!");
  }, 1000);
});

console.log(promise1);

const promise2 = new Promise((resolve, reject) => {
  setTimeout(() => {
    console.log("Hello, World!");
    resolve("Success");
  }, 1000);
});

setTimeout(() => {
  console.log(promise2);
}, 2000);

const promise3 = new Promise((resolve, reject) => {
  setTimeout(() => {
    console.log("Hello, World!");
    reject("reject message");
  }, 1000);
});

setTimeout(() => {
  console.log(promise3);
}, 2000);