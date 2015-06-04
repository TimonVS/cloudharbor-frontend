'use strict';

function serverManagementFormCtrl ($scope, $http, CloudService, Server) {

  // ------------------------------------------------------------------
  // Initialization
  // ------------------------------------------------------------------

  var vm = this

  // Function assignment
  vm.openForm = openForm
  vm.generateToken = generateToken
  vm.onSubmit = onSubmit

  // Variable assignment
  vm.isOpen = true
  vm.form = {}
  vm.sshKeys = [
    {
      'id': 512189,
      'fingerprint': '3b:16:bf:e4:8b:00:8b:b8:59:8c:a9:d3:f0:19:45:fa',
      'public_key': 'ssh-rsa AAAAB3NzaC1yc2EAAAADAQABAAAAQQDDHr/jh2Jy4yALcK4JyWbVkPRaWmhck3IgCoeOO3z1e2dBowLh64QAM+Qb72pxekALga2oi4GvT+TlWNhzPH4V example',
      'name': 'Macbook Timon'
    }
  ]

  // Get available server sizes and regions
  CloudService.serverOptions().$promise.then(function (data) {
    vm.serverOptions = data
  })

  // ------------------------------------------------------------------
  // Actions
  // ------------------------------------------------------------------

  function openForm () {
    vm.isOpen = true
  }

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
      'name': vm.form.name,
      'image': 'coreos-stable',
      'region': vm.form.region.slug,
      'size': vm.form.size,
      'ipv6': vm.form.ipv6Enabled || false,
      'backups': vm.form.backupsEnabled || false,
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
  .module('app.containerManagement')
  .controller('serverManagementFormCtrl', serverManagementFormCtrl);
