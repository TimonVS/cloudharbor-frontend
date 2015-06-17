'use strict'

function userFactory ($resource) {

  var User = $resource('/user/:action', {}, {
    create: {
      method: 'POST'
    },
    save: {
      method: 'PUT'
    },
    logout: {
      method: 'POST',
      params: { action: 'logout' }
    }
  })

  return User

}

angular
  .module('app.user')
  .factory('User', userFactory)
