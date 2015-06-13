'use strict';

function serverManagementCtrl ($scope, $modal, Server, serverCache, servers) {

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

  vm.getServers = getServers
  vm.createServer = createServer

  // ------------------------------------------------------------------
  // Actions
  // ------------------------------------------------------------------

  function getServers () {
    vm.busy = true

    Server.query().$promise.then(function (data) {
      vm.servers = data
      vm.busy = false
    })
  }

  function createServer () {
    var modalInstance = $modal.open({
      animation: false,
      templateUrl: 'app/serverManagement/create/serverCreateForm.tpl.html',
      controller: 'serverCreateFormCtrl',
      controllerAs: 'vm',
      size: 'lg'
    })

    modalInstance.result
      .then(function (data) {
        // Get server data with id and add the server to the servers array
        Server.get({ id: data.success }).$promise.then(function (response) {
          vm.servers.unshift(response)
        })
      })
      .catch(function (error) {

      })
  }

  // ------------------------------------------------------------------
  // Events
  // ------------------------------------------------------------------

}

angular
  .module('app.serverManagement')
  .controller('serverManagementCtrl', serverManagementCtrl);
