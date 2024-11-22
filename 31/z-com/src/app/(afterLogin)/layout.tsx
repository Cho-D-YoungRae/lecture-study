import React, {ReactNode} from "react";
import style from './layout.module.css';
import Link from "next/link";
import Image from "next/image";
import zLogo from '@/../public/zlogo.png';
import NavMenu from "@/app/(afterLogin)/_component/NavMenu";
import LogoutButton from "@/app/(afterLogin)/_component/LogoutButton";
import TrendSection from "@/app/(afterLogin)/_component/TrendSection";
import FollowRecommend from "@/app/(afterLogin)/_component/FollowRecommend";
import RightSearchZone from "@/app/(afterLogin)/_component/RightSearchZone";

type Props = {
  children: ReactNode,
  modal: ReactNode
};


export default function AfterLoginLayout(
  {children, modal}: Readonly<Props>
) {
  return (
    <div className={style.container}>
      <header className={style.leftSectionWrapper}>
        <section className={style.leftSection}>
          <div className={style.leftSectionFixed}>
            <Link className={style.logo} href={'/home'}>
              <div className={style.logoPill}>
                <Image src={zLogo} alt={'z.com 로고'} width={40}></Image>
              </div>
            </Link>
            <nav>
              <ul>
                <NavMenu />
                <Link href='/compose/tweet' className={style.postButton}>게시하기</Link>
              </ul>
            </nav>
            <LogoutButton/>
          </div>
        </section>
      </header>
      <div className={style.rightSectionWrapper}>
        <div className={style.rightSectionInner}>
          <main className={style.main}>{children}</main>
          <section className={style.rightSection}>
            <RightSearchZone/>
            <TrendSection/>
            <div className={style.followRecommend}>
              <FollowRecommend/>
              <FollowRecommend/>
              <FollowRecommend/>
            </div>
          </section>
        </div>
      </div>
      {modal}
    </div>
  )
}