import { fileURLToPath, URL } from "node:url";

import { defineConfig } from "vite";
import vue from "@vitejs/plugin-vue";
import vueJsx from "@vitejs/plugin-vue-jsx";

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [vue(), vueJsx()],
  resolve: {
    alias: {
      "@": fileURLToPath(new URL("./src", import.meta.url)),
    },
  },
  // CORS 프론트엔드에서 해결하기
  server: {
    proxy: {
      "/api": {
        target:"http://localhost:8080",
        rewrite: (path) => path.replace(/^\/api/, "")
      }
    }
  }
});
