'use strict';

function ContainerFactory ($resource, $q) {

  var Container = $resource('/management/containers/:id', { id: '@Id' }, {
    query: { method: 'GET', isArray: true }
  })

  return Container

}

angular
  .module('app.containerManagement')
  .factory('Container', ContainerFactory)
