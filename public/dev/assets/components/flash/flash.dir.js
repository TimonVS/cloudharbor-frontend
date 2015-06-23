'use strict';

function flashFactory (notifications) {
  return function (type, label, params) {
    notifications.send('flash', { type: type, label: label, params: params })
  }
}

function flash ($timeout, notifications) {
  return {
    templateUrl: 'components/flash/flash.tpl.html',
    restrict: 'E',

    link: function postLink ($scope, $element) {

      // ------------------------------------------------------------------
      // Initialization
      // ------------------------------------------------------------------

      $scope.messages = []
      $scope.ticket = false

      var timeout = null;

      // ------------------------------------------------------------------
      // Actions
      // ------------------------------------------------------------------

      $scope.dismissMessage = function (index) {
        $scope.messages.splice(index, 1)
      }

      function startTimer () {
        timeout = $timeout(onTimeout, 5000);
      }

      function stopTimer () {
        $timeout.cancel(timeout);
      }

      function onTimeout () {
        $scope.messages.pop()
      }

      // ------------------------------------------------------------------
      // Events
      // ------------------------------------------------------------------

      notifications.get('flash', function (event, message) {
        $scope.messages.unshift(message)

        //Show ticket link if error
        $scope.ticket = message.type === 'error'

        //Show right color when error
        if (message.type === 'error')
          message.type = 'danger'

        startTimer()
      })

      $element.on('mouseenter', stopTimer)
      $element.on('mouseleave', startTimer)
    }
  }
}

angular.module('app.components')
  .factory('flash', flashFactory)
  .directive('chFlashMessage', flash)
