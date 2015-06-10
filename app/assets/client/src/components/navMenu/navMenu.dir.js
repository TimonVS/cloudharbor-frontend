'use strict';

function navMenu ($location) {
  return {
    restrict: 'E',
    templateUrl: 'components/navMenu/navMenu.tpl.html'
  }
}

angular
  .module('app.components')
  .directive('chNavMenu', navMenu)
