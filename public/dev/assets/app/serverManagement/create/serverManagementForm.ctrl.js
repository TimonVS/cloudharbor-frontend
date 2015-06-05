'use strict';

function serverManagementFormCtrl ($scope, $http, CloudService, Server) {

  // ------------------------------------------------------------------
  // Initialization
  // ------------------------------------------------------------------

  var vm = this

  // Function assignment
  vm.generateToken = generateToken
  vm.onSubmit = onSubmit

  // Variable assignment
  vm.form = {}
  vm.server = {}

  vm.sshKeys = CloudService.sshKeys()
  vm.serverOptions = CloudService.serverOptions()

  // ------------------------------------------------------------------
  // Actions
  // ------------------------------------------------------------------

  function generateToken () {
    if (generateActive) return

    var generateActive = true

    // todo: implement in back-end
    $http.get('https://cloudharbor-reverse-proxy.herokuapp.com/https://discovery.etcd.io/new').then(function (response) {
      vm.form.discovery_token = response.data
      generateActive = false
    })
  }

  function onSubmit (form) {
    if (form.$invalid) return

    var request = {
      'name': vm.server.name,
      'image': 'coreos-stable',
      'region': vm.server.region.slug,
      'size': vm.server.size,
      'ipv6': vm.server.ipv6 || false,
      'backups': vm.server.backups || false,
      'ssh_keys': [770829]
    }

    vm.busy = true

    Server.create(request, success, error)

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
  .module('app.serverManagement')
  .controller('serverManagementFormCtrl', serverManagementFormCtrl);
