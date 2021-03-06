'use strict';

function serverShowCtrl ($state, $timeout, Server, server, Dialog, notifications, flash) {

  // ------------------------------------------------------------------
  // Initialization
  // ------------------------------------------------------------------

  var vm = this

  // Variable assignment
  vm.server = server

  // Function assignment
  vm.startServer = startServer
  vm.stopServer = stopServer
  vm.rebootServer = rebootServer
  vm.deleteServer = deleteServer

  // ------------------------------------------------------------------
  // Actions
  // ------------------------------------------------------------------

  function startServer () {
    return Server.start({ id: server.id }).$promise
      .then(function () {
        vm.server.status = 'Active'
      })
      .catch(function (error) {
        flash('danger', error.error)
      })
  }

  function stopServer () {
    return Server.stop({ id: server.id }).$promise
      .then(function () {
        vm.server.status = 'Off'
      })
      .catch(function (error) {
        flash('danger', error.error)
      })
  }

  function rebootServer () {
    return Server.reboot({ id: server.id }).$promise
      .then(function () {
        vm.server.status = 'Active'
      })
      .catch(function (error) {
        flash('danger', error.error)
      })
  }

  function deleteServer () {
    return Dialog.confirm({
      message: 'Are you sure you want to delete this server?',
      action: 'Delete server'
    }).then(function () {
      Server.delete({ id: server.id }).$promise
        .then(function () {
          $state.go('servers.overview')
        })
        .catch(function (error) {
          flash('danger', error.error)
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

  // ------------------------------------------------------------------
  // Events
  // ------------------------------------------------------------------

  notifications.get('notification', function (event, notification) {
    if (notification.message.body === 'Server rebooted') {
      vm.server.status = notification.message.status
    }
  })

}

angular
  .module('app.serverManagement')
  .controller('serverShowCtrl', serverShowCtrl);
