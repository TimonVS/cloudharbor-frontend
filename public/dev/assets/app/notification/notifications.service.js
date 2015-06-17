'use strict'

function notificationsService ($rootScope, EventSource) {

  var source = new EventSource('/notifications')

  source.on('open', function (event) {
    console.log('Connection was opened.')
  })

  source.on('message', function (event) {
    console.log(event)
    sendMessage('notification', event)
  })

  source.on('error', function (event) {
    console.log('Error - connection was lost.')
  })

  function sendMessage (message, data) {
    data = data || {}
    $rootScope.$emit(message, data)
  }

  function getMessage (message, callback, scope) {
    var unbind = $rootScope.$on(message, callback)

    if (scope) {
      scope.$on('destroy', unbind)
    }
  }

  return {
    send: sendMessage,
    get: getMessage
  }

}

angular
  .module('app.notification')
  .service('notifications', notificationsService)
