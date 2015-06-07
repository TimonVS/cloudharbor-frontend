'use strict';

function pagination () {
  return {
    restrict: 'EA',
    scope: {
      totalItems: '@',
      itemsPerPage: '=',
      paginationRange: '=',
      onPageChange: '&'
    },
    templateUrl: 'components/pagination/pagination.tpl.html',
    link: function ($scope, $element, $attr) {
      // todo: refactor
      $attr.$observe('totalItems', function (value) {
        if (value) {
          $scope.totalItems = value
          $scope.currentPage = 1
          $scope.numPages = Math.ceil($scope.totalItems / $scope.itemsPerPage)
          $scope.pages = generatePages()
        }
      })

      $scope.goToPage = function (num) {
        $scope.currentPage = num
        $scope.pages = generatePages(num)

        if ($scope.onPageChange) {
          $scope.onPageChange({ pageNumber: num })
        }
      }

      function generatePages (currentNum, paginationRange) {
        if (!currentNum) currentNum = 1
        if (!paginationRange) paginationRange = 9

        var pages = []

        for (var i = 1; i <= $scope.numPages && i <= paginationRange; i++) {
          var page = generatePage(i, currentNum, paginationRange)
          pages.push(page)
        }

        return pages
      }

      function generatePage (i, currentNum, paginationRange) {
        var halfWay = Math.ceil(paginationRange/2)

        if (i === paginationRange) {
          return $scope.numPages
        } else if (i === 1) {
          return i
        } else if (paginationRange < $scope.numPages) {
          if ($scope.numPages - halfWay < currentNum) {
            return $scope.numPages - paginationRange + i;
          } else if (halfWay < currentNum) {
            return currentNum - halfWay + i;
          } else {
            return i
          }
        } else {
          return i
        }
      }
    }
  }
}

angular
  .module('app.components')
  .directive('chPagination', pagination)
