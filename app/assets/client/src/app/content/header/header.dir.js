'use strict';

function header ($rootScope) {
  return {
    restrict: 'E',
    templateUrl: 'app/content/header/header.tpl.html',
    scope: {
      title: '@'
    },
    controller: function ($scope) {
      $rootScope.pageTitle = $scope.title
    }
  }
}

angular
  .module('app.content')
  .directive('chHeader', header);
