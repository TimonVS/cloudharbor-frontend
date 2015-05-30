'use strict';

function serverManagementFormCtrl ($scope) {

  var vm = this

  // Function assignment
  vm.onSubmit = onSubmit

  // Variable assignment
  vm.serverModel = {}

  vm.serverFormFields = [
    {
      key: 'name',
      type: 'input',
      templateOptions: {
        label: 'Server name',
        placeholder: 'Enter server name'
      }
    }
  ]

  function onSubmit () {}

  // ------------------------------------------------------------------
  // Event listeners
  // ------------------------------------------------------------------


}

angular
  .module('app.containerManagement')
  .controller('serverManagementFormCtrl', serverManagementFormCtrl);
