'use strict';

function serverShowCtrl ($scope, Server, server) {

  // ------------------------------------------------------------------
  // Initialization
  // ------------------------------------------------------------------

  var vm = this

  vm.server = server

  // ------------------------------------------------------------------
  // Actions
  // ------------------------------------------------------------------

  server.getContainers()

}

angular
  .module('app.serverManagement')
  .controller('serverShowCtrl', serverShowCtrl);
