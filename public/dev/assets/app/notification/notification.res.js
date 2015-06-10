'use strict';

function NotificationsFactory ($resource) {

  var Notifications = $resource('/notifications', { id: '@id' }, {
    query: {
      method: 'GET',
      isArray: true
    }
  })

  Notifications.latest = [
    { id: 1, userId: "123", message: { id: 12, body: "Test bericht"}, notificationType: "ContainerCreate" },
    { id: 2, userId: "123", message: { id: 12, body: "Test bericht"}, notificationType: "ContainerDelete" }
  ]

  return Notifications

}

angular
  .module('app.notification')
  .factory('Notifications', NotificationsFactory)
