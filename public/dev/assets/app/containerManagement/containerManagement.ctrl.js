'use strict';

function containerManagementCtrl ($scope, $log, $modal, Server) {

  // ------------------------------------------------------------------
  // Initialization
  // ------------------------------------------------------------------

  var vm = this

  vm.getServers = getServers
  vm.getContainers = getContainers
  vm.createContainer = createContainer

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
      getContainers(server)
    })
  }

  function getContainers (server) {
    if (!server.containers) server.containers = []

    server.getContainers().$promise
      .then(function (data) {
        server.containers = data
      })
      .catch(function (error) {
        console.log(error)
        server.containers.$error = 'Error retrieving containers'
      })
  }

  function createContainer (server) {
    var size = 'lg'

    var modalInstance = $modal.open({
      animation: false,
      templateUrl: 'app/containerManagement/create/containerCreateForm.tpl.html',
      controller: 'containerCreateForm',
      controllerAs: 'vm',
      size: size
    })

    modalInstance.result.then(function (selectedItem) {
      vm.selected = selectedItem;
    }, function () {
      $log.info('Modal dismissed at: ' + new Date())
    })
  }

  vm.getServers()

  // ------------------------------------------------------------------
  // Event listeners
  // ------------------------------------------------------------------

  $scope.$on('imageSelected', function (event, data) {
    event.stopPropagation()
    console.log(data)
  })
}

angular
  .module('app.containerManagement')
  .controller('containerManagementCtrl', containerManagementCtrl);
