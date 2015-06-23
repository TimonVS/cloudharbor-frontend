'use strict';

function containerCreateFormCtrl ($scope, $http, $modalInstance, server, Container) {

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

  function splitCommands (command) {
    var commands = command.split(' ')
    var result = []
    var begin = 0

    commands.forEach(function (cmd, index) {
      if (cmd.charAt(0) === '"') {
        begin = index
        return
      }
      else if (cmd.charAt(cmd.length - 1) === '"') {
        result.push(commands.slice(begin, index + 1).join(' ').split('"')[1])
        begin = 0
        return
      }
      else if (begin) return
      result.push(cmd)
    })

    return result
  }

  function cancel () {
    $modalInstance.dismiss('cancel');
  }

  function submit (form) {
    if (form.$invalid) return

    vm.busy = true

    var params = { serverUrl: server.getIp() }

    var request = {
      cpuShares: vm.container.cpuShares,
      memory: vm.container.memory
    }

    if (vm.tab === 'images') {
      angular.extend(request, { Image: vm.container.image.RepoTags[0] })
    }
    else if (vm.tab === 'dockerhub') {
      var image = vm.container.dockerHubImage.name.split('/')
      var repo = image[0]
      var name = image[1]

      angular.extend(params, { deployImageName: name, repo: repo })
    }

    if (vm.container.command) {
      var command = splitCommands(vm.container.command)
      angular.extend(request, { Cmd: command })
    }

    if (vm.container.name) {
      params = angular.extend(params, { name: vm.container.name })
    }

    var container = new Container(request)

    if (vm.tab === 'images') {
      container.$create(params)
        .then(function (data) {
          vm.busy = false
          $modalInstance.close(data)
        })
        .catch(function (error) {
          vm.error = error.data.error
          vm.busy = false
        })
    }
    else if (vm.tab === 'dockerhub') {
      container.$deploy(params)
        .then(function (data) {
          vm.busy = false
          $modalInstance.close()
        })
        .catch(function (error) {
          vm.error = error.data.error
          vm.busy = false
        })
    }
  }

  // ------------------------------------------------------------------
  // Events
  // ------------------------------------------------------------------


}

angular
  .module('app.containerManagement')
  .controller('containerCreateForm', containerCreateFormCtrl);
