import './App.css'
import Header from "./components/Header";
import Main from "./components/Main.jsx";
import Footer from "./components/Footer.jsx";
import Button from "./components/Button.jsx";
import Counter from "./components/Counter.jsx";
import Bulb from "./components/Bulb.jsx";
import Register from "./components/Register.jsx";
import Register2 from "./components/Register2.jsx";

function App() {

  const buttonProps = {
    text: "블로그",
    color: "blue",
    a: 1,
    b: 2,
    c: 3,
  };

  return (
    <>
      <Header/>
      <Main/>
      <Button text={"메일"} color={"red"}/>
      <Button text={"카페"}/>
      <Button {...buttonProps}>
        <div>자식 요소</div>
      </Button>
      <Footer/>
      <Counter/>
      <Bulb/>
      <Register/>
      <Register2/>
    </>
  )
}

export default App
