'use strict'

function containerList ($modal, $log, Container) {
  return {
    restrict: 'AE',
    scope: {
      server: '=',
      panel: '='
    },
    replace: true,
    templateUrl: 'app/containerManagement/list/containerList.tpl.html',
    controller: function ($scope) {

      // ------------------------------------------------------------------
      // Initialization
      // ------------------------------------------------------------------

      if ($scope.server.status !== 'off') $scope.server.getContainers()

      // Pagination
      $scope.pagination = {
        from: 0,
        limit: 15,
        ranges: [15, 25, 50, 100]
      }

      $scope.changePage = function (pageNum) {
        $scope.pagination.from = (pageNum - 1) * $scope.pagination.limit
      }

      // Function assignment
      $scope.createContainer = createContainer
      $scope.getContainers = getContainers

      // ------------------------------------------------------------------
      // Actions
      // ------------------------------------------------------------------

      function getContainers (server) {
        server.getContainers()
      }

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

        modalInstance.result.then(function (data) {
          console.log(data)
          var container = Container.get({ id: data.Id, serverUrl: server.getIp() })
          server.containers.unshift(container)
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
