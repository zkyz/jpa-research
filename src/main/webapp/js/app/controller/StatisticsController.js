app.controller("StatisticsController", function($scope, $http) {
	charim.chosen("statistics");

	$scope.inquiry = {
		startDate: new Date().format("yyyy-MM-dd"),
		endDate: new Date().format("yyyy-MM-dd"),
		orderBy: "READED",
		orderMethod: "DESC"
	}

	$http.get("/category/children?id=-")
		.success(function(data) {
			$scope.groupsHead = data;
		});

	$scope.getGroups = function() {
		$http.get("/category/children?id=" + $scope.groupHead)
			.success(function(data) {
				$scope.groupsChild = data;
				
				$("#emptyGroup").val($scope.groupHead);
				$scope.list.load();
			})
	}
	
	$("select[name=group]").change(function(){
		$scope.list.load()
	})
	
	$("#date-range").daterangepicker({
		locale: {
			format: "YYYY-MM-DD",
            applyLabel: "적용",
            cancelLabel: "닫기"
		},
		startDate: $scope.inquiry.startDate,
		endDate: $scope.inquiry.endDate
	}, function(start, end) {
		$scope.inquiry.startDate = start.format("YYYY-MM-DD"); 
		$scope.inquiry.endDate = end.format("YYYY-MM-DD"); 
		$scope.list.load()
	})

	$scope.list = new List("statistics")
		.setUrl("/supervision/statistics")
		.setParamValue($scope.inquiry)
		.setSort("READED", "HWP_DOWN", "DOC_DOWN", "COMMENT")
		.setReduce({
			"TITLE": 30
		})
		.load();
})