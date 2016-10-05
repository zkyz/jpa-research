(function(factory){
	if(typeof define === 'function' && define.amd){
		define(['jquery'], factory);
	}else{
        factory(window.jQuery);
    }
}(function($){
	"use strict"
	
	$.fn.jul = function() {
		var $this = this,
		nav = $(">ul>li", this),
		contents = $("section", this),
		showContent = function(index) {
			if(index < 0 || index >= nav.size()) {
				index = 0
			}

			$this.data("index", index)

			nav.removeClass("active")
				.eq(index).addClass("active")

			contents.filter(":visible").hide()
			contents.eq(index).fadeIn()
		}

		$this
		.data("index", 0)
		.find(">ul").addClass("step clearfix")
			.find(">li>span").addClass("step-text hidden-xs")
			.each(function(i, el){
				$(el).after("<div class='step-number'>" + (i + 1) + "</div>")
			})
			
		/*nav.not("[data-click-disabled]").click(function(e){
			var index = (parseInt($(".step-number", this).text()) || 1) - 1
			showContent(index)
		})*/

		contents.addClass("step-content")
			.find(".step-next").click(function(){
				$(this).trigger("next")
			})

		setTimeout(function(){
			$this.addClass("rendered").find(">*").show()
			showContent(0)
		}, 500)
		
		return {
			"go": function(index, fn) {
				if((typeof fn === "function" && !fn())
						|| (typeof fn === "boolean" && !fn)) return

				showContent(index)
			},
			"next": function(fn) {
				if((typeof fn === "function" && !fn())
					|| (typeof fn === "boolean" && !fn)) return
					
				var i = parseInt($this.data("index")) + 1;
				showContent(i)
			}
		}
	}
}))