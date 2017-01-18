var webpack = require('webpack')
var path = require('path')

var CopyWebpackPlugin = require('copy-webpack-plugin');

module.exports = {
  watch: true,
  context: __dirname,
  entry: {
    app: './index.js',
    vendor: [
      'jquery',
      'angular',
      'angular-route'
    ]
  },
  output: {
    path: __dirname + '/build/assets/',
    filename: 'bank.bundle.js',
    publicPath: "./assets/"
  },
  module: {
    loaders: [
      {
        test: /\.js$/,
        exclude: /(node_modules|bower_components)/,
        loader: 'babel-loader',
        query: {
          presets: ['es2015']
        }
      },
      {
        test: /\.css$/,
        loader: "css-loader",
        options: { relativeUrls: false }
      },
      {
        test: /\.html$/,
        loader: "ng-cache?prefix=[dir]&-url"
      },
      {
        test: /\.(jp?g|png|gif|svg)$/i,
        loader:'file'
      },
      {
        test: /\.eot(\?v=\d+\.\d+\.\d+)?$/,
        loader: "file",
      },
      {
        test: /\.ttf(\?v=\d+\.\d+\.\d+)?$/,
        loader: "url?limit=10000&mimetype=application/octet-stream"
      }
    ]
  },
  plugins: [
    new webpack.ProvidePlugin({
      $: "jquery",
      jQuery: "jquery"
    }),
    new webpack.optimize.CommonsChunkPlugin(/* chunkName= */"vendor", /* filename= */"vendor.bundle.js"),
    new webpack.HotModuleReplacementPlugin()
  ],
  devServer: {
    hot: true,
    inline: true
  }
};
