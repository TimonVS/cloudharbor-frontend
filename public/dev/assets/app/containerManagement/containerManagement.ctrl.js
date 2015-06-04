'use strict';

function containerManagementCtrl ($scope, Server) {

  var vm = this

  vm.getServers = function () {
    vm.busy = true

    Server.query({}, function (data) {
      vm.servers = data
      vm.busy = false
    }, function error () {

    })
  }

  vm.getContainers = function (server) {
    server.getContainers().$promise.then(function (data) {
      server.containers = data
    })
  }

  vm.getServers()

  // ------------------------------------------------------------------
  // Event listeners
  // ------------------------------------------------------------------

  $scope.$on('imageSelected', function (event, data) {
    event.stopPropagation()
    console.log(data)
  })
}

angular
  .module('app.containerManagement')
  .controller('containerManagementCtrl', containerManagementCtrl);
