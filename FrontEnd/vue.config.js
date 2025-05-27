// vue.config.js
module.exports = {
  transpileDependencies: ['@dcloudio/uni-h5'],
  configureWebpack: {
    devServer: {
      historyApiFallback: {
        index: '/index.html'
      }
    }
  }
};
