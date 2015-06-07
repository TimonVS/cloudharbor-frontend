'use strict'

function capitalizeFilter () {
  return function (input) {
    return input.charAt(0).toUpperCase() + input.slice(1)
  }
}

angular
  .module('app.components', [])
  .filter('capitalize', capitalizeFilter)
