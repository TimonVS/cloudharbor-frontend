'use strict';

function containerCreateFormCtrl ($scope, $q, $modalInstance, CloudService, Server) {

  // ------------------------------------------------------------------
  // Initialization
  // ------------------------------------------------------------------

  var vm = this

  // Function assignment
  vm.submit = submit
  vm.cancel = cancel

  // Variable assignment
  vm.form = {}
  vm.container = {}

  // ------------------------------------------------------------------
  // Actions
  // ------------------------------------------------------------------

  function cancel () {
    $modalInstance.dismiss('cancel');
  }

  function submit (form) {
    if (form.$invalid) return

    var request = {
      'macAddress': '',
      'networkDisabled': false,
      'cpuShares': form.cpuShares,
      'cpuset': ''
    }

    vm.busy = true

    Container.create(request, success, error)

    function success (response) {
      vm.busy = false
      $modalInstance.close()
      $scope.$emit('containerCreated', response)
    }

    function error (error) {
      vm.busy = false
    }
  }

  // ------------------------------------------------------------------
  // Events
  // ------------------------------------------------------------------


}

angular
  .module('app.containerManagement')
  .controller('containerCreateForm', containerCreateFormCtrl);
