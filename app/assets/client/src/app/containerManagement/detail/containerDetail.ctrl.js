'use strict'

function containerDetail (container, Container, $stateParams) {

  // ------------------------------------------------------------------
  // Initialization
  // ------------------------------------------------------------------

  var vm = this

  vm.container = container
  var params = { id: container.Id, serverUrl: $stateParams.serverUrl }

  vm.processes = {
    'Titles': [
      'USER',
      'PID',
      '%CPU',
      '%MEM',
      'VSZ',
      'RSS',
      'TTY',
      'STAT',
      'START',
      'TIME',
      'COMMAND'
    ],
    'Processes': [
      ['root','20147','0.0','0.1','18060','1864','pts/4','S','10:06','0:00','bash'],
      ['root','20271','0.0','0.0','4312','352','pts/4','S+','10:07','0:00','sleep']
    ]
  }

  // Function assignment
  vm.startContainer = startContainer
  vm.stopContainer = stopContainer

  // ------------------------------------------------------------------
  // Actions
  // ------------------------------------------------------------------

  function startContainer () {
    return Container.start(params).$promise
      .then(function (data) {
        console.log(data)
      })
      .catch(function (error) {
        console.log(error)
      })
  }

  function stopContainer () {

  }

  function pauseContainer () {

  }

  // ------------------------------------------------------------------
  // Events
  // ------------------------------------------------------------------

}

angular
  .module('app.containerManagement')
  .controller('containerDetailCtrl', containerDetail)
