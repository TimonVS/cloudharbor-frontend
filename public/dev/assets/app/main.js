'use strict';

angular.module('app', [
  'ngRoute',
  'test'])

  angular.module('app').directive('test', function () {
    return {
      restrict: 'AEC',
      template: '<b>Testy</b>'
    };
  });
