'use strict';

function ContainerFactory ($resource) {

  var Container = $resource('/containers/:id', { id: '@Id' }, {
    query: { method: 'GET', isArray: true }
  })

  return Container

}

angular
  .module('app.containerManagement')
  .factory('Container', ContainerFactory)
