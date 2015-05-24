angular
  .module('util')
  .factory('dockerHubApi', dockerHubApi);

function dockerHubApi ($http) {
  var apiUrl = 'https://cloudharbor-reverse-proxy.herokuapp.com/https://registry.hub.docker.com/v1/search?q=',
    pages = 25
}
