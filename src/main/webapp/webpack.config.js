const webpack = require("webpack");

module.exports = {
	entry: [
		"./app/js/app.js"
	],
	devServer: {
		inline: true,
		port: 8880
	},
	output: {
		filename: "./app/js/app.bundle.js"
	},
	module: {
		loaders: [
			{ test: /jquery\.js$/, loader: "expose?jQuery" },
			{ test: /tether\.js$/, loader: "expose?Tether" },
			{ test: /bootstrap\.min\.js$/, loader: "imports?jQuery=jquery" },
			{ test: /angular\.js$/, loader: "imports?jQuery=jquery" },
			{ test: /(woff2?|svg|jpe?g|png|gif|ico)$/, loader: "url?limit=10000" },
			{ test: /(ttf|eot)$/, loader: "file" },
			{ test: /html$/, loader: "html" },
			/* ES6 */
			{
				test: /\.js$/,
				loader: "babel",
				query: {
					presets: ['es2015']
				},
				exclude: [/node_modules/]
			},
			/* sass */
			{
				test: /\.scss$/, 
				loader: 'style!css!sass'
			}
		]
	},
	plugins: [
		new webpack.ProvidePlugin({
			"$": "jquery",
			"window.jQuery": "jquery",
			"window.Tether": "tether"
		})
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