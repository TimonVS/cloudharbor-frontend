'use strict';

function serverManagementCtrl ($scope, Server, serverCache, servers) {

  // ------------------------------------------------------------------
  // Initialization
  // ------------------------------------------------------------------

  var vm = this

  vm.servers = servers

  // ------------------------------------------------------------------
  // Actions
  // ------------------------------------------------------------------

  vm.getServers = function () {
    vm.busy = true

    Server.query().$promise.then(function (data) {
      vm.servers = data
      vm.busy = false
    })
  }

  vm.getContainers = function (server) {
    server.getContainers().$promise.then(function (data) {
      server.containers = data
    })
  }

  // ------------------------------------------------------------------
  // Events
  // ------------------------------------------------------------------

  $scope.$on('serverCreated', function (event, data) {
    event.stopPropagation()

    Server.get({ id: data.success }).$promise.then(function (response) {
      vm.servers.unshift(response)
    })
  })

}

angular
  .module('app.serverManagement')
  .controller('serverManagementCtrl', serverManagementCtrl);
