app.controller("CommentsController", function($scope, $http) {
	charim.chosen("comments");
	
	$scope.comm = {
		remove: function() {
			var datas = $scope.list.getCheckedData();
			
			if(!datas || !datas.length) {
				alert("삭제할 댓글의 체크박스를 선택해 주세요.");
			} 
			else if(confirm("삭제하시겠습니까?")) {
				var param = "", i;

				for(i = 0; i < datas.length; i++) {
					param += "&no=" +  datas[i].NO
						   + "&cno=" + datas[i].CNO;
				}
				
				$.ajax({
					type:	"post",
					url:	"/supervision/comments/delete",
					data:	param,
					success: function(r) {
						$scope.list.load();
					}
				})
			}
		}
	}
	
	$scope.list = new List("statistics")
	.setUrl("/supervision/comments")
	.setFn({
		groupName: function(td, value) {
			switch(value) {
			case "PRECEDENT":	td.innerHTML = "주요판례"; return
			case "CIVILS":		td.innerHTML = "최근 빈발민원"; return
			case "NOTICE":		td.innerHTML = "공지사항"; return
			default: 			td.innerHTML = "모범회신문";
			}
		}
	}) 
	.setReduce({
		"TITLE": 30
	})
	.load();
	
	// 기능 버튼 도움말 활성화 
	$("#fn-buttons>a").tooltip({
		"placement": "bottom"
	})
})