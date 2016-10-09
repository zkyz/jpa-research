require("jquery");

var angular = require("angular");
require("angular-sanitize");
require("angular-ui-router");

require("../../sass/base.scss");

angular.module("app", [
    "ngSanitize",
    "ui.router"
  ])
  .config(($stateProvider, $urlRouterProvider) => {
    $urlRouterProvider.otherwise("/supervision/user")

    $stateProvider
    .state("supervision-user", {
      url: "/supervision/user",
      controller : "UserController",
      templateUrl : "tpl/user.html"
    })
  })
  .controller("UserController", require("./controller/user"))
  ;