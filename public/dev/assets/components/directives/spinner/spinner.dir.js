'use strict';

function spinner () {
  return {
    restrict: 'EA',
    templateUrl: 'components/directives/spinner/spinner.tpl.html'
  }
}

angular
  .module('app.util')
  .directive('chSpinner', spinner)
