<script setup lang="ts">

import {ref} from "vue";
import axios from "axios";
import {useRouter} from "vue-router";

const title = ref("");
const content = ref("");

const router = useRouter();

const write = function () {
  axios.post("/api/posts", {
    title: title.value,
    content: content.value
  }).then(() => {
    // router.push 를 사용하면 글을 작성하고 home 으로 이동한 뒤
    // 뒤로가기를 하면 다시 글 작성화면으로 들어가짐
    // 이를 막기위해 router.replace 사용
    // router.push({name: "home"});
    router.replace({name: "home"});
  });
}
</script>

<template>

  <div>
    <el-input v-model="title" placeholder="제목을 입력해주세요"/>
  </div>

  <div class="mt-2">
    <el-input v-model="content" type="textarea" rows="15"/>
  </div>

  <div class="mt-2">
    <el-button @click="write" type="primary">글 작성완료</el-button>
  </div>

</template>

<style scoped>

</style>