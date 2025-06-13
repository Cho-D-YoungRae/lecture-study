function task(a, b, callback) {
  setTimeout(() => {
    const sum = a + b;
    callback(sum);
  }, 1000);
}

task(1, 2, (value) => {
  console.log(value);
});

function orderFood(callback) {
  setTimeout(() => {
    const food = "Pizza";
    callback(food);
  }, 2000);
}

function cooldownFood(food, callback) {
  setTimeout(() => {
    const cooldownedFood = `${food} (cooled down)`;
    callback(cooldownedFood);
  }, 2000);
}

function freezeFood(food, callback) {
  setTimeout(() => {
    const freezedFood = `${food} (frozen)`;
    callback(freezedFood);
  }, 1500);
}

orderFood((food) => {
  console.log(`Ordered food: ${food}`);
  cooldownFood(food, (cooldownedFood) => {
    console.log(cooldownedFood);
    freezeFood(cooldownedFood, (freezedFood) => {
      console.log(freezedFood);
    });
  });
});
