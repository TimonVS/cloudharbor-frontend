'use strict';

function imageManagementCtrl ($scope, $log, $modal, Server, servers) {

  // ------------------------------------------------------------------
  // Initialization
  // ------------------------------------------------------------------

  var vm = this

  vm.servers = servers

  // ------------------------------------------------------------------
  // Actions
  // ------------------------------------------------------------------

  function getAllImages () {
    vm.servers.forEach(function (server) {
      server.getImages()
    })
  }

  getAllImages()

  // ------------------------------------------------------------------
  // Event listeners
  // ------------------------------------------------------------------

}

angular
  .module('app.imageManagement')
  .controller('imageManagementCtrl', imageManagementCtrl);
