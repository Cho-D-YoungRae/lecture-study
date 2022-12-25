import { createRouter, createWebHistory } from "vue-router";
import HomeView from "../views/HomeView.vue";
import WriteView from "../views/WriteView.vue";
import ReadView from "../views/ReadView.vue";

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: "/",
      name: "home",
      component: HomeView,
    },
    {
      path: "/write",
      name: "write",
      component: WriteView
    },
    {
      path: "/read/:postId",
      name: "read",
      component: ReadView,
      props: true // path variable props 로 받을 수 있도록
    }
  ],
});

export default router;
