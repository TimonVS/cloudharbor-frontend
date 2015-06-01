'use strict';

function ServerFactory ($resource, $q, Container) {

  var Server = $resource('/servermanagement/servers/:action/:id', { id: '@id' }, {
    create: {
      method: 'POST',
      params: { action: 'add' }
    },
    query: {
      method: 'GET',
      isArray: true,
      /*interceptor: {
        response: function (data) {
          console.log(data)
          return data
        },
        responseError: function (error) {
          console.log(error)
          $q.reject(error)
        }
      }*/
      transformResponse: function (data) {
        return angular.fromJson(data).servers
      }
    }
  })

  angular.extend(Server.prototype, {
    getContainers: function () {
      return Container.query({ id: this.networks.v4[0].ip_address })
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
