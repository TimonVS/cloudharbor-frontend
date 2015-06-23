'use strict'

function imageList ($modal, notifications) {
  return {
    restrict: 'AE',
    scope: {
      server: '=',
      panel: '='
    },
    replace: true,
    templateUrl: 'app/imageManagement/list/imageList.tpl.html',
    controller: function ($scope) {

      // ------------------------------------------------------------------
      // Initialization
      // ------------------------------------------------------------------

      if ($scope.server.status !== 'off') $scope.server.getImages()

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
      $scope.createImage = createImage
      $scope.getImages = getImages

      // ------------------------------------------------------------------
      // Actions
      // ------------------------------------------------------------------

      function getImages (server) {
        server.getImages()
      }

      function createImage (server) {
        var modalInstance = $modal.open({
          animation: false,
          templateUrl: 'app/imageManagement/create/imageCreateForm.tpl.html',
          controller: 'imageCreateForm',
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
        }, function () {
          $scope.busy = true
        })
      }

      // ------------------------------------------------------------------
      // Events
      // ------------------------------------------------------------------

      notifications.get('notification', function (event, notification) {
        if (notification.message.body === 'Image is created') {
          var image = { RepoTags: [notification.message.name + ':latest'] }
          $scope.server.images.unshift(image)
          $scope.busy = false
        }
      })

    }
  }
}

angular
  .module('app.imageManagement')
  .directive('imageList', imageList)
