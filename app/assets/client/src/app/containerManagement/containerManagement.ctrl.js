'use strict';

function containerManagementCtrl ($scope, $log, $modal, Server, servers) {

  // ------------------------------------------------------------------
  // Initialization
  // ------------------------------------------------------------------

  var vm = this

  // Variable assignment
  vm.servers = servers
  vm.pagination = {
    limit: 10,
    from: 0
  }

  // Function assignment
  vm.getServers = getServers
  vm.changePage = changePage

  // ------------------------------------------------------------------
  // Actions
  // ------------------------------------------------------------------

  function getServers () {
    vm.busy = true

    Server.query().$promise.then(function (data) {
        vm.servers = data
        vm.busy = false
      })
      .catch(function (error) {

      })
  }

  function changePage (pageNum) {
    vm.from = (pageNum - 1) * vm.limit
  }

  // ------------------------------------------------------------------
  // Event listeners
  // ------------------------------------------------------------------

}

angular
  .module('app.containerManagement')
  .controller('containerManagementCtrl', containerManagementCtrl);
