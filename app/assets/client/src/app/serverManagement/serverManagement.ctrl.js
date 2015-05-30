'use strict';

function serverManagementCtrl (Server) {

  var vm = this

  vm.servers = []
  vm.offset = 0
  vm.limit = 10

  vm.get = function () {
    vm.busy = true

    Server.query({}, function (servers, headers) {
      vm.servers = servers
      vm.busy = false
      console.log(servers)
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

}

angular
  .module('app.serverManagement')
  .controller('serverManagementCtrl', serverManagementCtrl);
