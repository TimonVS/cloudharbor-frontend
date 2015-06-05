'use strict';

function navMenu ($location) {
  return {
    restrict: 'E',
    templateUrl: 'components/directives/navMenu/navMenu.tpl.html'
  }
}

angular
  .module('app.util')
  .directive('chNavMenu', navMenu)
