'use strict'

function imageList () {
  return {
    restrict: 'AE',
    scope: {
      server: '='
    },
    templateUrl: 'app/imageManagement/list/imageList.tpl.html',
    controller: function ($scope) {
      $scope.server.getImages()
    }
  }
}

angular
  .module('app.imageManagement')
  .directive('imageList', imageList)
