'use strict';

function serverManagementFormCtrl ($http) {

  var vm = this

  // Function assignment
  vm.openForm = openForm
  vm.onSubmit = onSubmit

  // Variable assignment
  vm.isOpen = true
  vm.model = {}

  // todo: refactor to resource
  $http.get('/servermanagement/server-options').then(function (response) {
    vm.serverOptions = response.data
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
