"use client";

import style from './signup.module.css';
import {redirect} from "next/navigation";
import BackButton from "@/app/(beforeLogin)/_component/BackButton";

export default function SignupModal() {

  const submit = async (formData: FormData) => {
    "use server";

    // 몇몇 검증 코드 추가 가능
    let shouldRedirect = false;
    try {
      const response = await fetch('api/users', {
        method: 'POST',
        body: formData,
        credentials: 'include'
      });
      console.log(response.status);
      if (response.status === 403) {
        return { message: 'user_exists' }
      }

      console.log(await response.json());
      shouldRedirect = true;
    } catch (err) {
      console.error(err);
    }

    if (shouldRedirect) {
      redirect('/home');
    }
  }

  return (
    <>
      <div className={style.modalBackground}>
        <div className={style.modal}>
          <div className={style.modalHeader}>
            <BackButton />
            <div>계정을 생성하세요.</div>
          </div>
          <form action={submit}>
            <div className={style.modalBody}>
              <div className={style.inputDiv}>
                <label className={style.inputLabel} htmlFor="id">아이디</label>
                <input id="id" name="id" className={style.input} type="text" placeholder=""
                       required
                />
              </div>
              <div className={style.inputDiv}>
                <label className={style.inputLabel} htmlFor="name">닉네임</label>
                <input id="name" name="name" className={style.input} type="text" placeholder=""
                       required
                />
              </div>
              <div className={style.inputDiv}>
                <label className={style.inputLabel} htmlFor="password">비밀번호</label>
                <input id="password" name="password" className={style.input} type="password" placeholder=""
                       required
                />
              </div>
              <div className={style.inputDiv}>
                <label className={style.inputLabel} htmlFor="image">프로필</label>
                <input id="image" name="image" className={style.input} type="file" accept="image/*"
                       required
                />
              </div>
            </div>
            <div className={style.modalFooter}>
              <button type="submit" className={style.actionButton}>가입하기</button>
            </div>
          </form>
        </div>
      </div>
    </>)
}