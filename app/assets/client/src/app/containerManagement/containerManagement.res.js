'use strict';

function ContainerFactory ($resource) {

  var Container = $resource('/management/containers/:action/:id/:name', { id: '@id' }, {
    query: {
      method: 'GET',
      isArray: true
    },
    create: {
      method: 'POST',
      params: { name: 'ubuntu', action: 'deploy', serverUrl: '178.62.172.128' }
    }
  })

  return Container

}

angular
  .module('app.containerManagement')
  .factory('Container', ContainerFactory)
