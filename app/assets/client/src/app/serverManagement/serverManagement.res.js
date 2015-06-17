'use strict';

function ServerFactory ($resource, Container, DockerImage) {

  var Server = $resource('/servermanagement/servers/:id/:action', { id: '@id' }, {
    create: {
      method: 'POST',
      params: { action: 'add' }
    },
    query: {
      method: 'GET',
      isArray: true,
      transformResponse: function (data) {
        var result = angular.fromJson(data)
        result.servers.$meta = {}
        return result.servers
      },
      interceptor: {
        response: function (response) {
          response.resource.$meta = response.data.$meta
          return response.resource
        }
      }
    },
    start: {
      method: 'POST',
      params: { action: 'start' }
    },
    stop: {
      method: 'POST',
      params: { action: 'stop' }
    },
    delete: {
      method: 'DELETE',
      params: { action: 'delete' }
    }
  })

  angular.extend(Server.prototype, {
    getContainers: function () {
      var self = this

      this.containers = []

      this.containers = Container.query({ id: this.getIp() }).$promise
        .then(function (data) {
          self.containers = data
        })
        .catch(function (error) {
          console.log(error)
          self.containers.$error = error.data.error
        })
    },
    getImages: function () {
      var self = this

      this.images = []

      this.images = DockerImage.query({ serverUrl: this.getIp() }).$promise
        .then(function (data) {
          self.images = data
        })
        .catch(function (error) {
          console.log(error)
          self.images.$error = error.data.error
        })
    },
    getIp: function () {
      var ip
      this.networks.v4.forEach(function (network) {
        if (network.type === 'public') ip = network.ip_address
      })
      return ip
    }
  })

  return Server

}

// TODO
function serverCache ($cacheFactory) {
  //return $cacheFactory('serverCache')

  var cache = []

  this.get = function () {
    return cache
  }

  this.set = function (data) {
    cache = data
  }

  this.put = function (data) {
    cache.unshift(data)
  }
}

/**
 * @ngdoc module
 * @name app.serverManagement
 * @module app.serverManagement
 *
 * @description
 * The serverManagement module contains all componenents and services to work with server management.
 */

angular
  .module('app.serverManagement')

/**
 * @ngdoc factory
 * @name ServerFactory
 * @module app.serverManagement
 *
 * @description
 * A {@link $resource} factory to create server instances.
 *
 * @usage new Server([params])
 *
 * @param {Object=} params An object containing the initial data for the server
 *
 * @returns {serverManagement.Server} A new server instance.
 *
 * @requires $resource, Container, DockerImage
 *
 */
  .factory('Server', ServerFactory)
