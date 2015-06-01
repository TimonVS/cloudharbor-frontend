'use strict';

function serverManagementFormCtrl ($scope, CloudService, Server) {

  var vm = this

  // Function assignment
  vm.openForm = openForm
  vm.onSubmit = onSubmit

  // Variable assignment
  vm.isOpen = true
  vm.model = {}
  vm.serverForm = new Server()

  CloudService.serverOptions().$promise.then(function (data) {
    vm.serverOptions = data
  })

  function openForm () {
    vm.isOpen = true
  }

  function onSubmit (form) {
    if (form.$invalid) return

    vm.busy = true

    Server.create({
      'name': vm.serverForm.name,
      'image': 'coreos-stable',
      'region': vm.serverForm.region.slug,
      'size': vm.serverForm.size,
      'ipv6': vm.serverForm.ipv6Enabled || false,
      'backups': vm.serverForm.backupsEnabled || false,
      'ssh_keys': [770829]
    }, success, error)

    function success (response) {
      $scope.$emit('serverCreated', response)
    }

    function error (response) {}
  }

  // ------------------------------------------------------------------
  // Events
  // ------------------------------------------------------------------


}

angular
  .module('app.containerManagement')
  .controller('serverManagementFormCtrl', serverManagementFormCtrl);
