'use strict'

function containerDetail (container) {

  // ------------------------------------------------------------------
  // Initialization
  // ------------------------------------------------------------------

  var vm = this

  vm.container = container

  // Function assignment

  // ------------------------------------------------------------------
  // Actions
  // ------------------------------------------------------------------

  // ------------------------------------------------------------------
  // Events
  // ------------------------------------------------------------------

}

angular
  .module('app.containerManagement')
  .controller('containerDetailCtrl', containerDetail)
