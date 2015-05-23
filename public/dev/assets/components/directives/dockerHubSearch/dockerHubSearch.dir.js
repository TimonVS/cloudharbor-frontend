angular
  .module('util')
  .directive('dockerHubSearch', dockerHubSearch);

function dockerHubSearch ($http, $resource) {
  return {
    restrict: 'EA',
    templateUrl: 'components/directives/dockerHubSearch/dockerHubSearch.tpl.html',
    link: function () {}
  };
}
