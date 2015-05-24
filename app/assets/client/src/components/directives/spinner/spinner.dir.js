'use strict';

angular
  .module('util')
  .directive('spinner', spinner);

function spinner () {
  return {
    restrict: 'EA',
    templateUrl: 'components/directives/spinner/spinner.tpl.html'
  }
}
