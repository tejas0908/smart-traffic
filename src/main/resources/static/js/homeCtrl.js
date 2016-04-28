angular.module('traffic-mgmt-webapp')

.controller('HomeCtrl', function($scope, $interval, SocketService, CanvasService, $timeout) {
  var canvas = document.getElementById('myCanvas');
  var context = canvas.getContext('2d');

  $scope.roads = {
    2:{
      conjestionLevel:30,
      loop:null
    },
    4:{
      conjestionLevel:30,
      loop:null
    },
    6:{
      conjestionLevel:30,
      loop:null
    },
    8:{
      conjestionLevel:30,
      loop:null
    }
  };

  $scope.signals = {
    2:{
        defaultGreenIntervalTime:5000,
        currentGreenIntervalTime:5000
      },
    4:{
      defaultGreenIntervalTime:5000,
      currentGreenIntervalTime:5000
      },
      6:{
        defaultGreenIntervalTime:5000,
        currentGreenIntervalTime:5000
        },
        8:{
          defaultGreenIntervalTime:5000,
          currentGreenIntervalTime:5000
          }
  };

$scope.currentGreenSignal = {
  signalId:2
}

var startLoop = function(roadId){
  console.log("Vehicles on road ", roadId, "are moving every ", (100-$scope.roads[roadId].conjestionLevel+5)*100, " milliseconds");
  $scope.roads[roadId].loop = $interval(function(){
      console.log("Updating the vehicle movement update for road ",roadId);
  }, (100-$scope.roads[roadId].conjestionLevel+5)*100);
};

  var startLoops = function(){
      for (var roadId in $scope.roads) {
        startLoop(roadId);
      }
  };

var updateGreenSignal = function(keys, currentKeysIndex){
    $scope.currentGreenSignal.signalId = keys[currentKeysIndex];
    setTimeout(function(){
      if(currentKeysIndex==keys.length-1){
        updateGreenSignal(keys,0)
      }else{
        updateGreenSignal(keys,currentKeysIndex+1)
      }
    }, $scope.signals[keys[currentKeysIndex]].currentGreenIntervalTime);
};

  var _init = function() {
    CanvasService.draw(context);
    startLoops();
    var keys = Object.keys($scope.signals);
    updateGreenSignal(keys,0)
    };

  _init();

$scope.updateConjestion = function(roadId){
  $interval.cancel($scope.roads[roadId].loop);
  startLoop(roadId);
};

  // $scope.sendUpdatedAssetState = function() {
  //   SocketService.emit('simlation-update-asset-status', msg);
  // };

});
