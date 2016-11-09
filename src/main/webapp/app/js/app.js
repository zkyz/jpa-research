var $ = require("jquery");
require("tether");
require("bootstrap");
require("chosen-js");

var angular = require("angular");
require("angular-sanitize");
require("angular-ui-router");

// SCSS
require("../../sass/bootstrap.cei.scss");
require("../../sass/chosen.cei.scss");
require("../../sass/layout.scss");
require("../../sass/base.scss");

angular.module("app", [
    "ngSanitize",
    "ui.router"
  ])
  .run(($rootScope, $timeout) => {
    $rootScope
      .$on("$includeContentLoaded", function(event, templateName){
        if(templateName === "menu.html") {
          $("#차림 a[href='']").click(function() {
            $(this).toggleClass("open").next("ul").slideToggle(200);
          })
        }
      });

    $rootScope.$on("$viewContentLoaded", function() {
      $timeout(() => {
        document.body.className = "completed";
      }, 0);
    });
  })
  .config(($stateProvider, $urlRouterProvider) => {

    // default location
    $urlRouterProvider.otherwise("/supervision/user")

    $stateProvider
    .state("user", {
      url: "/user",
      controller : "UserController",
      templateUrl : "tpl/user.html"
    })
  })
  .directive("tooltip", function() {
    return {
      restrict: "A",
      link: function($scope, $element) {
        $element.attr("title", $element.data("tooltip"))
          .tooltip();
      }
    }
  })
  .directive("inputLine", function() {
    return {
      restrict: "C",
      link: function($scope, $element) {
        $("> input", $element)
          .focus(function() {
            $element.addClass("focus");
          })
          .blur(function() {
            $element.removeClass("focus");
          })
      }
    }
  })
  .directive("chosen", function(){
    return {
      restrict: "C",
      link: function($scope, $element) {
        $element.chosen({
          no_results_text: "검색 결과가 없네요.",
          placeholder_text_multiple: "여러개 골라요.",
          placeholder_text_single: " ",
          search_contains: true
        })
      }
    }
  })
  .controller("UserController", require("./controller/user"))
  ;