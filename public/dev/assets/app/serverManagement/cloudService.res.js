'use strict';

function CloudServiceFactory ($resource) {

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
    },
    apiKey: {
      method: 'GET',
      params: { action: 'api-key' }
    },
    addInfo: {
      method: 'POST',
      params: { action: 'add-info' }
    }
  })

  return CloudService

}

angular
  .module('app.serverManagement')
  .factory('CloudService', CloudServiceFactory)
