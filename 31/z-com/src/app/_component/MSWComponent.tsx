"use client";

import {useEffect} from "react";

export const MSWComponent = () => {
  useEffect(() => {
    if (typeof window !== 'undefined' && process.env.NEXT_PUBLIC_API_MOCKING === 'enabled') {
      require("@/mocks/browser");
    }
  }, []);

  return null;
}