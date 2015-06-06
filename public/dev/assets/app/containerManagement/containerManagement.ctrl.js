'use strict';

function containerManagementCtrl ($scope, $log, $modal, Server, servers) {

  // ------------------------------------------------------------------
  // Initialization
  // ------------------------------------------------------------------

  var vm = this

  vm.servers = servers

  vm.getServers = getServers
  vm.createContainer = createContainer

  // Pagination
  vm.pagination = {
    limit: 10,
    from: 0
  }

  vm.changePage = function (pageNum) {
    vm.from = (pageNum - 1) * vm.limit
  }

  // ------------------------------------------------------------------
  // Actions
  // ------------------------------------------------------------------

  function getServers () {
    vm.busy = true

    Server.query().$promise.then(function (data) {
        vm.servers = data
        vm.busy = false
        getAllContainers()
      })
      .catch(function (error) {

      })
  }

  function getAllContainers () {
    vm.servers.forEach(function (server) {
      server.getContainers()
    })
  }

  function createContainer (server) {
    var modalInstance = $modal.open({
      animation: false,
      templateUrl: 'app/containerManagement/create/containerCreateForm.tpl.html',
      controller: 'containerCreateForm',
      controllerAs: 'vm',
      size: 'lg'
    })

    modalInstance.result.then(function (selectedItem) {
      vm.selected = selectedItem;
    }, function () {
      $log.info('Modal dismissed at: ' + new Date())
    })
  }

  getAllContainers()

  // ------------------------------------------------------------------
  // Event listeners
  // ------------------------------------------------------------------

}

angular
  .module('app.containerManagement')
  .controller('containerManagementCtrl', containerManagementCtrl);
