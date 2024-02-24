const path = require("path");
const webpack = require("webpack");
const RefreshWebpackPlugin = require("@pmmmwh/react-refresh-webpack-plugin");


module.exports = {
  name: "word-relay",
  mode: "development", // 실서비스 : production
  devtool: "eval", // 빠르게 하겠다는 뜻..? 실서비스: hidden-source-map
  // resolve 를 사용하면 확장자 생략해줘도 됨
  // entry 가 많아질 수 있는데(jsx, js, css, json, ...)
  resolve: {
    extensions: [".js", ".jsx"],
  },

  // entry, output 이 제일 중요
  entry: {
    app: [
      "./client",
      // client.jsx 에서 WordRelay.jsx 를 불러오기 때문에 작성해줄 필요 없음
      // -> 웹팩이 알아서 해줌
      // "./WordRelay.jsx",
    ],
  }, // 입력

  module: {
    rules: [
      {
        test: /\.jsx?/, // js, jsx 파일에 룰을 적용하겠다는 뜻
        loader: "babel-loader", // 위의 룰은 바벨로더를 사용하겠다는 뜻
        options: {  // 바벨 옵션
          // preset 에 대한 옵션이 또 있을 수 있음
          // preset 은 플러그인들의 모음
          presets: [
            // preset 에 대한 옵션이 필요하면 아래와 같이 사용할 수 있음
            ["@babel/preset-env", {
              targets: {
                browsers: ["last 2 chrome versions"], // browserslist
              },
              debug: true,
            }],
            "@babel/preset-react"
          ],
          plugins: [
            // 리액트 클래스 컴포넌트를 사용할 때 필요한 플러그인
            "@babel/plugin-proposal-class-properties",
            // babel 을 사용할 때 react-refresh 를 사용하겠다는 뜻
            "react-refresh/babel",
          ]
        }
      }
    ]
  },

  // webpack 에서 기본적으로 사용해주는 것 이외에 사용할 plugin
  plugins: [
    // new webpack.LoaderOptionsPlugin({ debug: true }),
    // refresh webpack plugin 을 사용하겠다는 뜻
    new RefreshWebpackPlugin(),
  ],

  // path VS publicPath
  // path : 실제 경로
  // publicPath : 가상 경로
  output: {
    path: path.join(__dirname, "dist"), // __dirname : 현재폴더
    filename: "app.js",
    publicPath: "/dist/",
  }, // 출력

  // webpack-dev-server 설정
  // webpack-dev-server 는 webpack 빌드 후 publicPath 에 있는 파일을 메모리에 저장
  // 변경점을 감지하여 변경점이 있을 때마다 다시 빌드
  devServer: {
    devMiddleware: {
      publicPath: "/dist/",
    },
    static: {
      // 실제로 존재하는 정적파일 경로
      directory: path.resolve(__dirname),
    },
    hot: true,
  }
}