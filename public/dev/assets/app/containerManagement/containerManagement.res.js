'use strict';

function ContainerFactory ($resource) {

  var Container = $resource('/management/containers/:id/:name/:ip/:action', { id: '@Id' }, {
    query: {
      method: 'GET',
      isArray: true
    },
    create: {
      method: 'POST'
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
