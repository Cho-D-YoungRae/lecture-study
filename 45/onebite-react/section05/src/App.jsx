import './App.css'
import Header from "./components/Header";
import Main from "./components/Main.jsx";
import Footer from "./components/Footer.jsx";
import Button from "./components/Button.jsx";
import {useState} from "react";
import StateComponent from "./components/StateComponent.jsx";

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
      <StateComponent/>
    </>
  )
}

export default App
