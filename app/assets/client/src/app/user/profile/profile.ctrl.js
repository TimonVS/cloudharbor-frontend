'use strict'

function profileCtrl () {
  var vm = this

  vm.user = {
    userName: 'timonvs',
    password: 'abc123',
    passwordRepeat: 'abc123',
    email: 'timonvanspronsen@outlook.com',
    firstName: 'Timon',
    prefix: 'van',
    lastName: 'Spronsen'
  }
}

angular
  .module('app.user')
  .controller('profileCtrl', profileCtrl)
