'use strict';

function ContainerFactory ($resource) {

  var Container = $resource('/management/containers/:id/:ip/:action', { id: '@Id' }, {
    query: {
      method: 'GET',
      isArray: true
    },
    create: {
      method: 'POST',
      params: { name: '@name' }
    },
    deploy: {
      method: 'POST',
      params: { name: 'hello-world', tag: 'latest', action: 'deploy' }
    },
    start: {
      method: 'POST',
      params: { action: 'start', serverUrl: '@serverUrl', id: '@id' }
    },
    stop: {
      method: 'POST',
      params: { action: 'stop', serverUrl: '@serverUrl', id: '@id' }
    }
  })

  return Container

}

angular
  .module('app.containerManagement')
  .factory('Container', ContainerFactory)
