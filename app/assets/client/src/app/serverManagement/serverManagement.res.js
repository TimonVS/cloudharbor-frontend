'use strict';

function ServerFactory ($resource, Container) {

  var Server = $resource('/servermanagement/servers/:id', { id: '@id' }, {
    query: { method: 'GET' }
  })

  angular.extend(Server.prototype, {
    getContainers: function () {
      return Container.query()
    }
  })

  return Server

}

function CloudServiceFactory ($resource) {

  var CloudService = $resource('/servermanagement/:action', {}, {
    serverOptions: {
      method: 'GET',
      params: { action: 'server-options' },
      transformResponse: function (data) {
        data = angular.fromJson(data)

        var regex = /(?:\d*\.)?\d+/g

        data.regions.map(function (region) {
          region.group = region.name.replace(regex, '').trim()

          return region
        })

        return { regions: data.regions }
      }
    }
  })

  return CloudService

}


angular
  .module('app.serverManagement')
  .factory('Server', ServerFactory)
  .factory('CloudService', CloudServiceFactory)
