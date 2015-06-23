'use strict'

function imageSelect () {
  return {
    restrict: 'AE',
    scope: {
      server: '='
    },
    require: 'ngModel',
    replace: true,
    templateUrl: 'app/imageManagement/select/imageSelect.tpl.html',
    link: function (scope, element, attrs, ctrl) {

      // ------------------------------------------------------------------
      // Initialization
      // ------------------------------------------------------------------

      scope.server.getImages()

      // Pagination
      scope.pagination = {
        from: 0,
        limit: 10,
        ranges: [10, 25, 50, 100]
      }

      scope.changePage = function (pageNum) {
        scope.pagination.from = (pageNum - 1) * scope.pagination.limit
      }

      // ------------------------------------------------------------------
      // Actions
      // ------------------------------------------------------------------

      scope.selectImage = function (image) {
        scope.selected = image
        ctrl.$setViewValue(image)
      }
    }
  }
}

angular
  .module('app.imageManagement')
  .directive('imageSelect', imageSelect)
