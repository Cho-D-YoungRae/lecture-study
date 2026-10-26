let arr = [1, 2, 3];

for (let i = 0; i < arr.length; i++) {
  console.log(arr[i]); // 1 2 3
}
console.log();

let arr2 = [4, 5, 6, 7, 8, 9];

for (const item of arr2) {
  console.log(item); // 4 5 6 7 8 9
}

let person = {
  name: "Cho",
  age: 29,
  hobby: "Soccer",
}

let keys = Object.keys(person);
console.log(keys); // [ 'name', 'age', 'hobby' ]

for (const item of keys) {
  console.log(item, person[item]);
}

let values = Object.values(person);
console.log(values); // [ 'Cho', 29, 'Soccer' ]

for (const key in person) {
  console.log(key, person[key]);
}