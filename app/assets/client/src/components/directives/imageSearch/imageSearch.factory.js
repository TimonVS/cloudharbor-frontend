'use strict';

angular
  .module('util')
  .factory('dockerHubApi', dockerHubApi);

function dockerHubApi ($http) {
  var apiUrl = 'https://cloudharbor-reverse-proxy.herokuapp.com/https://registry.hub.docker.com/v1/search?q=',
    pages = 25

  function Image (params) {
    angular.extend(this, params)
  }

  Image.search = function (query, params) {
    if (!params) params = {}
    if (!params.limit) params.limit = 25
    var pageNum = params.pageNum || (params.offset ? Math.ceil(params.offset/params.limit) : 1)

    return $http.get(apiUrl + query + '&page=' + pageNum + '&n=' + params.limit)
      .then(function (response) {
        if (response.status !== 200) return response

        response.data.results = response.data.results.map(function (result) {
          return new Image(result)
        })

        return response
      })
  }

  return Image
}
