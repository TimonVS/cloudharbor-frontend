'use strict';

function serverShowCtrl ($state, $timeout, Server, server, Dialog) {

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
        vm.server.status = 'active'
      })
      .catch(function (error) {
        console.log(error)
      })
  }

  function stopServer () {
    return Server.stop({ id: server.id }).$promise
      .then(function (data) {
        vm.server.status = 'off'
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
          $state.go('servers.overview')
        })
        .catch(function (error) {
          console.log(error)
        })
    })
  }

  function checkStatus () {
    return server.$get()
      .then(function (data) {
        if (data.locked) return $timeout(checkStatus(), 10000)
        else vm.server.locked = false
      })
  }

  if (server.locked) checkStatus()
}

angular
  .module('app.serverManagement')
  .controller('serverShowCtrl', serverShowCtrl);
