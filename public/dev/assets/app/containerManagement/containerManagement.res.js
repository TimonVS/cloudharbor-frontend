'use strict';

function ContainerFactory ($resource) {

  var Container = $resource('/management/containers/:action/:id', { id: '@id' }, {
    query: {
      method: 'GET',
      isArray: true
    },
    create: {
      method: 'POST'
    }
  })

  return Container

}

angular
  .module('app.containerManagement')
  .factory('Container', ContainerFactory)
