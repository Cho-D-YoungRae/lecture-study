import React, {ReactNode} from "react";
import styles from '@/app/(beforeLogin)/_component/main.module.css';

type Props = {
  children: ReactNode,
  modal: ReactNode
};

export default function Layout({children, modal}: Readonly<Props>) {
  return (
    <div className={styles.container}>
      {children}
      {modal}
    </div>
  );
}
