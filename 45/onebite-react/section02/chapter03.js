let arr = [1, 2, 3];

let [a, b] = arr;
console.log(a, b); // 1 2 3

let [one, two, three, four = 4] = arr;
console.log(one, two, three, four); // 1 2 3 4

let person = {
  name: "Cho",
  age: 29,
  hobby: "Soccer",
}

let {name, age, hobby} = person;
console.log(name, age, hobby); // Cho 29 Soccer

let {
  age: myAge,
  extra = "hello",
} = person;

console.log(myAge, extra); // 29 hello

const func = ({name, age, hobby, extra}) => {
  console.log(name, age, hobby, extra);
}

func(person);