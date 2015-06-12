'use strict'

function DockerImageFactory ($resource) {

  var DockerImage = $resource('/management/images/:name/:action', { name: '@name' }, {
    create: {
      method: 'POST'
    }
  })

  return DockerImage

}

angular
  .module('app.imageManagement')
  .factory('DockerImage', DockerImageFactory)
