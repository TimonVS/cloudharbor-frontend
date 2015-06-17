'use strict'

function profileCtrl (user) {
  var vm = this

  vm.user = user

  vm.submit = submit

  function submit (form) {
    if (form.$invalid) return

    return vm.user.$save()
  }
}

angular
  .module('app.user')
  .controller('profileCtrl', profileCtrl)
