angular
  .module('util')
  .directive('dockerHubSearch', dockerHubSearch);

function dockerHubSearch ($http, $resource) {
  return {
    restrict: 'EA',
    templateUrl: 'components/directives/dockerHubSearch/dockerHubSearch.tpl.html',
    link: function ($scope, $element, $attr) {
      var SEARCH_URL = 'https://cloudharbor-reverse-proxy.herokuapp.com/https://registry.hub.docker.com/v1/search?q=',
        RESULTS = 25

      var inputElement = angular.element($element.find('input')[0])

      // Select existing text upon click
      inputElement.on('click', function () {
        inputElement.select()
      })

      $scope.$watch('search.input', function (input) {
        if (input === undefined) return delete $scope.results
        search(input)
      })

      $scope.pagination = {
        current: 1
      }

      $scope.changePage = function (num) {
        search($scope.search.input, num)
      }

      function search(input, page) {
        if (!page) var page = 1
        if ($scope.busy) return

        $scope.busy = true

        $http.get(SEARCH_URL + input + '&page=' + page + '&n=' + RESULTS)
          .success(function (data) {
            $scope.busy = false
            $scope.results = data
          })
          .error(function () {
            // todo
            $scope.busy = false
          })
      }
    }
  }
}
