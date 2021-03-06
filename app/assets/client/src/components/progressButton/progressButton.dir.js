'use strict';

function progressButton () {
  return {
    restrict: 'AE',
    templateUrl: 'components/progressButton/progressButton.tpl.html',
    transclude: true,
    replace: true,
    scope: {
      action: '&',
      size: '@',
      type: '@',
      icon: '@'
    },
    link: function ($scope, $element, $attr) {
      if (!$scope.size) $scope.size = 'md'
      if (!$scope.type) $scope.type = 'primary'

      $element.bind('click', function () {
        $attr.$set('disabled', true)

        try {
          $scope.action().finally(function () {
            $attr.$set('disabled', false)
          })
        }
        catch (error) {
          $attr.$set('disabled', false)
        }
      })
    }
  }
}

angular
  .module('app.components')
  .directive('chProgressButton', progressButton)
