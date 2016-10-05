(function(w, d){
	if(w.parent && w.parent.jQuery) {
		w.parent.jQuery(d).ready(function(){
			w.$ = function(selector) {
				return w.parent.jQuery(selector, d);
			}
			
			setTimeout(function(){
				w.parent.jQuery(w.parent).trigger("resize");
			}, 1000);
		})
	}
})(window, document);



function save(no) {
	if(!$("#reply-form>textarea").val()) {
		alert("내용을 입력해 주세요.");
		$("#reply-form>textarea").focus();
		return false;
	}
	
	parent.jQuery.ajax({
		type: "POST",
		url: "/reply/comment/save",
		data: {
			no: no,
			content: $("#reply-form>textarea").val()
		},
		success: function(e) {
			location.reload();
		}
	})

	return false;
}