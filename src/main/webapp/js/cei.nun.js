(function(factory){
	if(typeof define === 'function' && define.amd){
		define(['jquery'], factory);
	}else{
        factory(window.jQuery);
    }
}(function($){
	"use strict"
	
	var CHAR_WIDTH = 9

	$(document).ready(function(){
		$("body")
			.append("<div id='cei-nun' class='tooltip top'><div class='tooltip-arrow'/><div class='tooltip-inner'/></div>")
			
		var simian = $("#cei-nun")

		$("input[type=password]").css({
			"font-family": "monospace",
			"font-size": "18px"
		}).keypress(function(e){
			var key = e.keyChar || e.keyCode,
			input = $(this),
			char = String.fromCharCode(key)

			if(key === 32) // space
			{
				char = "(공백)"
			}
			else if(key === 13) // enter
			{
				return false
			}

			setTimeout(function(input){
				return function() {
					var	pos = input.offset(),
					left = input.val().length * CHAR_WIDTH

					if(left > input.width()) {
						left = input.width()
					}

					simian.css({
						"top":		pos.top - 25,
						"left":		pos.left + left,
						"opacity":	1
					})
					.stop()
					.show()
					.fadeOut(1000)
					.find(".tooltip-inner")
						.html(char)
				}
			}(input))
		})
	})
}))