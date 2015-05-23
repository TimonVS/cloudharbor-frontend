angular
  .module('util')
  .directive('dockerHubSearch', ['$http', '$resource', dockerHubSearch]);

function dockerHubSearch ($http, $resource) {
  return {
    restrict: 'EA',
    templateUrl: 'components/directives/dockerHubSearch/dockerHubSearch.tpl.html',
    link: function ($scope) {
      var SEARCH_URL = 'https://cloudharbor-reverse-proxy.herokuapp.com/https://registry.hub.docker.com/v1/search?q=';
      var test = '&page=1&n=25';

      $scope.search = function (terms) {
        $http.get(SEARCH_URL + terms + test)
          .success(function (data) {
            $scope.results = data;
          });
      }
    }
  };
}
