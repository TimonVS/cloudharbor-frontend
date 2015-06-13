'use strict';

function CloudServiceFactory ($resource, lodash) {

  var CloudService = $resource('/servermanagement/:action', {}, {
    serverOptions: {
      method: 'GET',
      params: { action: 'server-options' },
      ignoreLoadingBar: true,
      transformResponse: function (data) {
        data = angular.fromJson(data)

        var regex = /(?:\d*\.)?\d+/g

        data.regions.map(function (region) {
          region.group = region.name.replace(regex, '').trim()

          return region
        })

        return { regions: data.regions }
      }
    },
    sshKeys: {
      method: 'GET',
      params: { action: 'ssh-keys' },
      isArray: true,
      ignoreLoadingBar: true,
      transformResponse: function (data) {
        return angular.fromJson(data).sshKeys
      }
    }
  })

  function sortBySize() {
    var sizes = ['2gb', '4gb', '512mb', '8gb', '1gb']
    var initial = sizes.pop()
    var sorted = [initial]

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

    String.prototype.getSizeExtension = function () {
      return this.substring(this.length - 2).toUpperCase()
    }

    sizes.forEach(function (item) {
      if (SizeEnum[initial.getSizeExtension()].value === SizeEnum[item.getSizeExtension()].value) {
        if (item.slice(0, -2) >= initial.slice(0, -2)) sorted.push(item)
        else sorted.unshift(item)
      }
      else if (SizeEnum[initial.getSizeExtension()].value >= SizeEnum[item.getSizeExtension()].value) {
        sorted.unshift(item)
      }
      else if (SizeEnum[initial.getSizeExtension()].value < SizeEnum[item.getSizeExtension()].value) {
        sorted.push(item)
      }
    })
  }

  return CloudService

}

angular
  .module('app.serverManagement')
  .factory('CloudService', CloudServiceFactory)
