import React from "react";
import {BrowserRouter, Link, Route, Routes} from "react-router-dom";
import NumberBaseball from "../3_숫자야구/NumberBaseballClass";
import RSP from "../5_가위바위보/RSPClass";
import Lotto from "../6_로또/LottoClass";
import GameMatcher from "./GameMatcherClass";

const Games = () => {

  return (
    <>
      <BrowserRouter>
        <div>
          <Link to="/game/number-baseball">숫자야구</Link><br/>
          <Link to="/game/rock-scissors-paper">가위바위보</Link><br/>
          <Link to="/game/lotto-generator">로또추첨기</Link><br/>
          <Link to="/game/index">GameMatcher</Link><br/>
        </div>
        <div>

          <Routes>
            <Route path="/game/:name" component={GameMatcher}/>
          </Routes>
        </div>
      </BrowserRouter>
    </>
  )
}

export default Games;