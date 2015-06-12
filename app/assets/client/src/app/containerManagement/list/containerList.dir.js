'use strict'

function containerList () {
  return {
    restrict: 'AE',
    scope: {
      server: '='
    },
    replace: true,
    templateUrl: 'app/containerManagement/list/containerList.tpl.html',
    controller: function ($scope) {
      if ($scope.server.status !== 'off') $scope.server.getContainers()
    }
  }
}

angular
  .module('app.containerManagement')
  .directive('containerList', containerList)
