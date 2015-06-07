'use strict';

function serverShowCtrl ($state, Server, server, Dialog) {

  // ------------------------------------------------------------------
  // Initialization
  // ------------------------------------------------------------------

  var vm = this

  vm.server = server

  vm.startServer = startServer
  vm.stopServer = stopServer
  vm.deleteServer = deleteServer

  // ------------------------------------------------------------------
  // Actions
  // ------------------------------------------------------------------

  function startServer () {
    return Server.start({ id: server.id }).$promise
      .then(function (data) {
        console.log(data)
        vm.server.status = "active"
      })
      .catch(function (error) {
        console.log(error)
      })
  }

  function stopServer () {
    return Server.stop({ id: server.id }).$promise
      .then(function (data) {
        console.log(data)
        vm.server.status = "off"
      })
      .catch(function (error) {
        console.log(error)
      })
  }

  function deleteServer () {
    return Dialog.confirm({
      message: 'Are you sure you want to delete this server?',
      action: 'Delete server'
    }).then(function () {
      Server.delete({ id: server.id }).$promise
        .then(function (data) {
          console.log(data)
          $state.go('servers.overview')
        })
        .catch(function (error) {
          console.log(error)
        })
    })
    /*return Server.delete({ id: server.id }).$promise
      .then(function (data) {
        console.log(data)
        $state.go('servers.overview')
      })
      .catch(function (error) {
        console.log(error)
      })*/
  }

  // Get containers if server is turned on
  if (server.status !== 'off') server.getContainers()

}

angular
  .module('app.serverManagement')
  .controller('serverShowCtrl', serverShowCtrl);
