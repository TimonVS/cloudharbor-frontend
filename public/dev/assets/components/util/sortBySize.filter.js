'use strict'

function sortBySize (lodash) {
  return lodash.memoize(function (input) {
    if (!input) return input
    if (input.length === 0) return input

    // Simulating Enum
    var SizeEnum = Object.freeze({
      'KB': {
        value: 0,
        name: 'KB'
      },
      'MB': {
        value: 1,
        name: 'MB'
      },
      'GB': {
        value: 2,
        name: 'GB'
      },
      'TB': {
        value: 3,
        name: 'TB'
      }
    })

    function getSizeExtension (string) {
      return string.substring(string.length - 2).toUpperCase()
    }

    input.sort(function (a, b) {
      if (SizeEnum[getSizeExtension(a)].value === SizeEnum[getSizeExtension(b)].value) {
        if (parseInt(a.slice(0, -2), 10) > parseInt(b.slice(0, -2), 10)) return 1
        else return -1
      }

      if (SizeEnum[getSizeExtension(a)].value > SizeEnum[getSizeExtension(b)].value) {
        return 1
      }

      if (SizeEnum[getSizeExtension(a)].value < SizeEnum[getSizeExtension(b)].value) {
        return -1
      }
    })

    return input
  })
}

angular
  .module('app.components')
  .filter('sortBySize', sortBySize)
