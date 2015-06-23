'use strict'

function profileCtrl (user, flash) {
  var vm = this

  vm.user = user

  vm.submit = submit

  function submit (form) {
    if (form.$invalid) return

    return vm.user.$save().$promise
      .then(function (data) {
        flash('success', 'Profile updated succesfully')
      })
      .catch(function (error) {
        vm.error = error.data.error
      })
  }
}

angular
  .module('app.user')
  .controller('profileCtrl', profileCtrl)
