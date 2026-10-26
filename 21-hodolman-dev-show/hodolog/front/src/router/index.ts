import { createRouter, createWebHistory } from "vue-router";
import HomeView from "../views/HomeView.vue";
import WriteView from "../views/WriteView.vue";
import ReadView from "../views/ReadView.vue";
import EditView from "../views/EditView.vue";

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
    },
    {
      path: "/edit/:postId",
      name: "edit",
      component: EditView,
      props: true
    },
  ],
});

export default router;
