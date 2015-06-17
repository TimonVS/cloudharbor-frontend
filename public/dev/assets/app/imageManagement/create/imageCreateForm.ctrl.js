'use strict';

function imageCreateFormCtrl ($scope, $modalInstance, server, DockerImage) {

  // ------------------------------------------------------------------
  // Initialization
  // ------------------------------------------------------------------

  var vm = this
  console.log(server)

  // Function assignment
  vm.submit = submit
  vm.cancel = cancel

  // Variable assignment
  vm.form = {}
  vm.image = {}

  // ------------------------------------------------------------------
  // Actions
  // ------------------------------------------------------------------

  function cancel () {
    $modalInstance.dismiss('cancel')
  }

  function submit (form) {
    if (form.$invalid) return

    vm.busy = true

    var request = {
      name: vm.image.image.name,
    }

    var image = new DockerImage(request)

    image.$create({ serverUrl: server.getIp() })
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
  .module('app.imageManagement')
  .controller('imageCreateForm', imageCreateFormCtrl);
