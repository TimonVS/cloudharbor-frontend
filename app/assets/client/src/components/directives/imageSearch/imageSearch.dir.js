'use strict';

function imageSearch (dockerHubApi) {
  return {
    restrict: 'EA',
    templateUrl: 'components/directives/imageSearch/imageSearch.tpl.html',
    link: function ($scope, $element) {

      // ------------------------------------------------------------------
      // Initialization
      // ------------------------------------------------------------------

      $scope.pagination = {
        current: 1
      }
      $scope.results = []
      $scope.search = search

      // ------------------------------------------------------------------
      // Actions
      // ------------------------------------------------------------------

      $scope.changePage = function (num) {
        search($scope.search.input, {pageNum: num})
      }

      $scope.selectImage = function (image) {
        $scope.$emit('imageSelected', image)
      }

      function search (query, params) {
        $scope.busy = true

        dockerHubApi.search(query, params)
          .then(function (response) {
            $scope.busy = false

            if (response.status !== 200) {
              $scope.search.error = {query: query, params: params}
              return
            }

            $scope.results = response.data
          })
      }

      // ------------------------------------------------------------------
      //
      // ------------------------------------------------------------------

      var inputElement = angular.element($element.find('input')[0])

      // Select existing text upon click
      inputElement.on('click', function () {
        inputElement.select()
      })

      $scope.$watch('search.input', function (query) {
        if (query === undefined) return delete $scope.results
        search(query)
      })
    }
  }
}

angular
  .module('app.util')
  .directive('chImageSearch', imageSearch)
