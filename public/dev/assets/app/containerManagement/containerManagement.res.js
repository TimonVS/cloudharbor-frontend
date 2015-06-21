'use strict';

function ContainerFactory ($resource) {

  var Container = $resource('/management/containers/:id/:ip/:action/:deployImageName', { id: '@Id' }, {
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
      params: { deployImageName: 'test-page', repo: 'ksdn117', tag: 'latest', action: 'deploy' }
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
