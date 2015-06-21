'use strict';

function containerCreateFormCtrl ($scope, $modalInstance, server, Container) {

  // ------------------------------------------------------------------
  // Initialization
  // ------------------------------------------------------------------

  var vm = this

  // Function assignment
  vm.submit = submit
  vm.cancel = cancel

  // Variable assignment
  vm.server = server
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

    vm.busy = true

    var request = {
      Image: vm.container.image.RepoTags[0],
      Cmd: ['/bin/sh', '-c', 'while true; do echo hello world; sleep 1; done']
    }

    var container = new Container(request)

    container.$create({ serverUrl: server.getIp() })
      .then(function (data) {
        vm.busy = false
        $modalInstance.close(data)
      })
      .catch(function (error) {
        vm.busy = false
      })
  }

  // ------------------------------------------------------------------
  // Events
  // ------------------------------------------------------------------


}

angular
  .module('app.containerManagement')
  .controller('containerCreateForm', containerCreateFormCtrl);
