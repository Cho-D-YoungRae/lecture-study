"use client";

import React, {createContext, useState} from "react";

type Props = { children : React.ReactNode };

export const TabContext = createContext({
  tab: "rec",
  setTab: (value: "rec" | "fol") => {
  }
});

export default function TabProvider({ children }: Props) {
  const [tab, setTab] = useState('rec');

  return (
    <TabContext.Provider value={{tab, setTab}}>
      {children}
    </TabContext.Provider>
  );
}