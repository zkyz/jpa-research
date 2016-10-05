(function(p){
	if(p) {
		window.onload = function() {
			var e = p.document.getElementById("for-content").style,
			b = document.body
			
			e.width = b.scrollWidth + "px"
			e.height = b.scrollHeight + "px"
			b.style.overflow = "hidden"

			p.onresize()
		}

		p.onresize = function() {
			var a = p.$("#article-content")
			a.height(p.$("#ter").height() - a.offset().top)
		}
	}	
})(window.parent)
