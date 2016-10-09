var webpack = require("webpack");

module.exports = {
	devServer: {
		inline: true,
		port: 8880
	},
	entry: './app/js/app.js',
	output: {
		path: __dirname,
		filename: "./app/js/app.bundle.js"
	},
	module: {
		loaders: [
			/* jQuery */
			{
				test: /\/angular\.js$/,
				loader: 'imports?jQuery=jquery'
			}, {
				test: /\/jquery.js$/,
				loader: 'expose?jQuery'
			},
			/* ES6 */
			{
				test: /\.js$/,
				loader: "babel",
				query: {
					presets: ['es2015']
				},
				exclude: /node_modules/
			},
			/* sass */
			{
				test: /\.scss$/,
				loader: "style!css!sass"
			}
		]
	},
	plugins: [
		/*new webpack.optimize.UglifyJsPlugin({
			compress: { 
				warnings: false
			},
			output: {
				comments: false
			},
			mangle: {
				except: ['$super', '$', 'exports', 'require']
			}
		})*/
	]
};