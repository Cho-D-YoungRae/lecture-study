<script setup lang="ts">
import axios from "axios";
import {ref} from "vue";
import {useRouter} from "vue-router";

const router = useRouter();

const posts = ref([])

axios.get("/api/posts?page=1&size=5").then(response =>
    response.data.forEach(post => {
      posts.value.push(post)
    })
);

const moveToRead = () => {
  router.push("read")
}
</script>

<template>
  <ul>
    <li v-for="post in posts" :key="post.id">
      <div>
<!--        router-link 를 이용해서 싱글페이지로 화면만 이동되도록 할 수 있다.-->
<!--        a 태그를 사용하면 애플리케이션 전체를 다시 불러오게 됨-->
        <router-link :to="{ name: 'read', params: { postId: post.id } }">{{ post.title }}</router-link>
      </div>
      <div>
        {{ post.content }}
      </div>
    </li>
  </ul>
</template>
