const { merge } = require('webpack-merge');
const common = require('./webpack.common.js');

module.exports = merge(common, {
  mode: 'development',
  devtool: 'inline-source-map',
  devServer: {
    port: 8084,
    liveReload: true,
    hot: true,
    open: true,
    static: ['./'],
  },
});
