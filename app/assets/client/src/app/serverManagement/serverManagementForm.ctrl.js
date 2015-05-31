'use strict';

function serverManagementFormCtrl (CloudService) {

  var vm = this

  // Function assignment
  vm.openForm = openForm
  vm.onSubmit = onSubmit

  // Variable assignment
  vm.isOpen = true
  vm.model = {}

  CloudService.serverOptions().$promise.then(function (data) {
    vm.serverOptions = data
  })

  function openForm () {
    vm.isOpen = true
  }

  function onSubmit () {}

  // ------------------------------------------------------------------
  // Event listeners
  // ------------------------------------------------------------------


}

angular
  .module('app.containerManagement')
  .controller('serverManagementFormCtrl', serverManagementFormCtrl);
