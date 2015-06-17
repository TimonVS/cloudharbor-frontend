'use strict'

function notificationList (Notifications) {
  return {
    restrict: 'E',
    templateUrl: 'app/notification/notificationList.tpl.html',
    replace: true,
    scope: {},
    link: function ($scope) {
      $scope.notifications = Notifications.latest
    }
  }
}

angular
  .module('app.notification')
  .directive('notificationList', notificationList)
