'use strict';

function serverManagementCtrl ($scope, Server, serverCache, servers) {

  // ------------------------------------------------------------------
  // Initialization
  // ------------------------------------------------------------------

  var vm = this

  vm.servers = servers

  // Pagination
  vm.pagination = {
    from: 0,
    limit: 10,
    ranges: [10, 25, 50, 100]
  }

  vm.changePage = function (pageNum) {
    vm.pagination.from = (pageNum - 1) * vm.pagination.limit
  }

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
