'use strict';

function ContainerFactory ($resource, $q) {

  var Container = $resource('/management/containers/:action/:id', { id: '@Id' }, {
    query: {
      method: 'GET',
      isArray: true
    },
    create: { method: 'POST' }
  })

  return Container

}

angular
  .module('app.containerManagement')
  .factory('Container', ContainerFactory)
