'use strict'

function convertTimestamp (lodash) {
  return lodash.memoize(function (input) {
    if (!lodash.isNumber(input)) return input
    return new Date(input * 1000)
  })
}

angular
  .module('app.components')
  .filter('convertTimestamp', convertTimestamp)
