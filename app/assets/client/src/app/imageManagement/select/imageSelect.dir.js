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
