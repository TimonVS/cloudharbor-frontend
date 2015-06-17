'use strict'

function containerList ($modal, $log) {
  return {
    restrict: 'AE',
    scope: {
      server: '=',
      panel: '='
    },
    replace: true,
    templateUrl: 'app/containerManagement/list/containerList.tpl.html',
    controller: function ($scope) {
      $scope.createContainer = createContainer

      if ($scope.server.status !== 'off') $scope.server.getContainers()

      function createContainer (server) {
        var modalInstance = $modal.open({
          animation: false,
          templateUrl: 'app/containerManagement/create/containerCreateForm.tpl.html',
          controller: 'containerCreateForm',
          controllerAs: 'vm',
          size: 'lg',
          resolve: {
            server: function () {
              return server
            }
          }
        })

        modalInstance.result.then(function () {

        }, function () {
          $log.info('Modal dismissed at: ' + new Date())
        })
      }
    }
  }
}

angular
  .module('app.containerManagement')
  .directive('containerList', containerList)
