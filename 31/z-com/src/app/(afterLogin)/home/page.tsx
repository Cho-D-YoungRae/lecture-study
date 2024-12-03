import Tab from "@/app/(afterLogin)/home/_component/Tab";
import style from './home.module.css';
import TabProvider from "@/app/(afterLogin)/home/_component/TabProvider";
import React from "react";
import PostForm from "@/app/(afterLogin)/home/_component/PostForm";
import Post from "@/app/(afterLogin)/_component/Post";
import {dehydrate, QueryClient} from "@tanstack/react-query";

export default async function Home() {
  const queryClient = new QueryClient();
  await queryClient.prefetchQuery({
    queryKey: ['posts', 'recommends'],
    queryFn: getPostRecommends
  });
  const dehydratedState = dehydrate(queryClient);

  return (
    <main className={style.main}>
      <TabProvider>
        <Tab/>
        <PostForm />
        <Post/>
        <Post/>
        <Post/>
        <Post/>
        <Post/>
        <Post/>
        <Post/>
        <Post/>
        <Post/>
        <Post/>
        <Post/>
        <Post/>
        <Post/>
        <Post/>
      </TabProvider>
    </main>
  );
}
