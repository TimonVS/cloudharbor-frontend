'use strict'

function EventSourceFactory ($window) {
  var EventSource = $window.EventSource;

  function parse (obj) {
    var json

    try {
      json = JSON.parse(obj)
    } catch (error) {
      json = {}
    }

    return json
  }

  return function(url){
    var eventSrc = new EventSource(url);

    return {
      on: function(type, callback) {
        eventSrc.addEventListener(type, function(event){
          callback(parse(event.data))
        })
      }
    }
  }
}

angular
  .module('app.components')
  .service('EventSource', EventSourceFactory)
