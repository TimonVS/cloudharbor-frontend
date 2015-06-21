'use strict';

function ContainerFactory ($resource) {

  var Container = $resource('/management/containers/:action/:id/:name/:serverUrl', { id: '@id' }, {
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
    }
  })

  return Container

}

angular
  .module('app.containerManagement')
  .factory('Container', ContainerFactory)
