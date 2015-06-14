'use strict';

function ContainerFactory ($resource) {

  var Container = $resource('/management/containers/:action/:id/:name', { id: '@id' }, {
    query: {
      method: 'GET',
      isArray: true
    },
    create: {
      method: 'POST',
      params: { name: 'hello-world:latest', serverUrl: '188.226.234.229' }
    }
  })

  return Container

}

angular
  .module('app.containerManagement')
  .factory('Container', ContainerFactory)
