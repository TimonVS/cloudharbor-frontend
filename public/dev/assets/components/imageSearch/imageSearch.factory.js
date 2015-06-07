'use strict';

function dockerHubApi ($http, $cacheFactory) {
  var apiUrl = 'https://cloudharbor-reverse-proxy.herokuapp.com/https://registry.hub.docker.com/v1/search?q=',
    pages = 25

  var $httpDefaultCache = $cacheFactory.get('$http');

  function Image (params) {
    angular.extend(this, params)
  }

  Image.search = function (query, params) {
    if (!params) params = {}
    if (!params.limit) params.limit = 25
    var pageNum = params.pageNum || (params.offset ? Math.ceil(params.offset/params.limit) : 1)

    return $http.get(apiUrl + query + '&page=' + pageNum + '&n=' + params.limit, { cache: true })
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

angular
  .module('app.components')
  .factory('dockerHubApi', dockerHubApi);
