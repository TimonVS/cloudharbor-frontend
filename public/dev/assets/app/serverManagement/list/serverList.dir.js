'use strict'

function serverList () {
  return {
    restrict: 'AE',
    scope: {
      servers: '='
    },
    transclude: true,
    replace: true,
    link: function (scope, element, attrs, ctrl, transclude) {
      transclude(scope, function (clone, scope) {
        element.append(clone)
      })
    }
  }
}

angular
  .module('app.serverManagement')
  .directive('serverList', serverList)
