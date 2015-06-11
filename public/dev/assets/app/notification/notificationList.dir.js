'use strict'

function notificationList (Notifications) {
  return {
    restrict: 'E',
    templateUrl: 'app/notification/notificationList.tpl.html',
    replace: true,
    scope: {},
    link: function ($scope) {
      $scope.notifications = Notifications.latest

      var source = new EventSource('/notifications');

      source.addEventListener("message", function(e) {
        console.log(e.data);
        console.log(angular.fromJson(e.data))
      }, false);

      source.addEventListener("open", function(e) {
        console.log("Connection was opened.");
      }, false);

      source.addEventListener("error", function(e) {
        console.log("Error - connection was lost.");
      }, false);
    }
  }
}

angular
  .module('app.notification')
  .directive('notificationList', notificationList)
