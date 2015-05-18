'use strict';

angular.module('app', ['ngRoute', 'util'])

  angular.module('app').directive('test', function () {
    return {
      restrict: 'AEC',
      template: '<b>Test</b>'
    };
  });
