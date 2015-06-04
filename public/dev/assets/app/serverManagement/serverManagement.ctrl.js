'use strict';

function serverManagementCtrl ($scope, Server) {

  var vm = this

  vm.servers = []
  vm.offset = 0
  vm.limit = 10

  vm.get = function () {
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

  // inital get
  vm.get()

  // ------------------------------------------------------------------
  // Events
  // ------------------------------------------------------------------

  $scope.$on('serverCreated', function (event, data) {
    event.stopPropagation()
    console.log(data)
    Server.get({ id: data.success }).$promise.then(function (response) {
      vm.servers.push(response)
    })
  })

}

angular
  .module('app.serverManagement')
  .controller('serverManagementCtrl', serverManagementCtrl);
