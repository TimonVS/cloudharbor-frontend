'use strict';

angular.module('app', ['ngRoute', 'ngResource', 'util']);

angular.module('app').config(['$httpProvider', function($httpProvider) {
  $httpProvider.defaults.useXDomain = true;
  delete $httpProvider.defaults.headers.common['X-Requested-With'];
}]);

angular.module('app').directive('test', function () {
  return {
    restrict: 'AEC',
    template: '<b>Test</b>'
  };
});
