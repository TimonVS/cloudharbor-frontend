'use strict';

function ServerFactory ($resource, Container) {

  var Server = $resource('/servers/:id', { id: '@id' }, {
    query: { method: 'GET', isArray: true }
  })

  angular.extend(Server.prototype, {
    getContainers: function () {
      return Container.query()
    }
  })

  return Server

}

angular
  .module('app.serverManagement')
  .factory('Server', ServerFactory)
