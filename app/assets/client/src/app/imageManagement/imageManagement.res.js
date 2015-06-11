'use strict'

function ImageFactory ($resource) {

  var Image = $resource('/management/images/:name/:action', {}, {

  })

  return Image

}

angular
  .module('app.imageManagement')
  .factory('Image', ImageFactory)
