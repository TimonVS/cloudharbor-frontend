'use strict';

function ContainerFactory ($resource) {

  var Container = $resource('/management/containers/:action/:id/:name', { id: '@id' }, {
    query: {
      method: 'GET',
      isArray: true
    },
    create: {
      method: 'POST',
      params: { name: 'hello-world', tag: 'latest', action: 'deploy' }
    }
  })

  return Container

}

angular
  .module('app.containerManagement')
  .factory('Container', ContainerFactory)
