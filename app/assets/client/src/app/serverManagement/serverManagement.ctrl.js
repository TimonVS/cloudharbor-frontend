'use strict';

function serverManagementCtrl ($scope, Server) {

  // ------------------------------------------------------------------
  // Initialization
  // ------------------------------------------------------------------

  var vm = this

  vm.servers = []
  vm.offset = 0
  vm.limit = 10

  // ------------------------------------------------------------------
  // Actions
  // ------------------------------------------------------------------

  vm.get = function () {
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

  // inital get
  vm.get()

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
