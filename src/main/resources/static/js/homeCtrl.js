angular.module('traffic-mgmt-webapp')

.controller('HomeCtrl', function($scope, $interval, SocketService, CanvasService, $timeout, $stomp) {
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
        currentGreenIntervalTime:5000,
        history:[]
      },
    4:{
      defaultGreenIntervalTime:5000,
      currentGreenIntervalTime:5000,
      history:[]
      },
      6:{
        defaultGreenIntervalTime:5000,
        currentGreenIntervalTime:5000,
        history:[]
        },
        8:{
          defaultGreenIntervalTime:5000,
          currentGreenIntervalTime:5000,
          history:[]
          }
  };

 var historyRecordsSize = 7;

$scope.currentGreenSignal = {
  signalId:2
}

var startLoop = function(roadId){
  console.log("Vehicles on road ", roadId, "are moving every ", Math.floor(10000/$scope.roads[roadId].conjestionLevel), " milliseconds");
  $scope.roads[roadId].loop = $interval(function(){
      //console.log("Updating the vehicle movement update for road ",roadId);
      // SocketService.emit('/app/road-ping', {
      //   roadId:roadId/2
      // });

      $stomp.send('/app/road-ping', {
         roadId:roadId
       });
  }, Math.floor(10000/$scope.roads[roadId].conjestionLevel));
};

  var startLoops = function(){
      for (var roadId in $scope.roads) {
        startLoop(roadId);
      }
  };

var flushOutHistory = function(signalId){
  var currentHistorySize = $scope.signals[signalId].history.length;
  if(currentHistorySize==historyRecordsSize){
      $scope.signals[signalId].history.splice(0,1)
  }
};

var addRecordIntoHistory = function(signalId, record){
  $timeout(function(){
    var currentHistorySize = $scope.signals[signalId].history.length;
    if(currentHistorySize==0){
        flushOutHistory(signalId);
        $scope.signals[signalId].history.push(record);
    }else{
        if($scope.signals[signalId].history[currentHistorySize-1].signalStatus==0 && record.signalStatus==1){
          flushOutHistory(signalId);
            $scope.signals[signalId].history.push(record);
        }else{
              if($scope.signals[signalId].history[currentHistorySize-1].signalStatus==1){
                  flushOutHistory(signalId);
                  $scope.signals[signalId].history.push(record);
              }
      }
    }

  }, 0);
};

var createHistoryRecord = function(greenSignalId){
  for (var signalId in $scope.signals) {
      if(signalId == greenSignalId){
          addRecordIntoHistory(signalId,{
            signalStatus:1,
            time:new Date()
          });
      }else{
        addRecordIntoHistory(signalId,{
          signalStatus:0,
          time:new Date()
        });
      }
  }
};

var updateGreenSignal = function(keys, currentKeysIndex){
    $scope.currentGreenSignal.signalId = keys[currentKeysIndex];
    createHistoryRecord(keys[currentKeysIndex]);
    $timeout(function(){
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

// SocketService.on('/topic/traffic-intervals', function(data) {
//     console.log("data came:",data);
//     // for (var i = 0; i < data.intervals.length; i++) {
//     //
//     // }
//   });


$stomp
.connect('/smart-traffic')
.then(function (frame) {
      console.log("Web Socket Connected successfully ",frame);
      var subscription = $stomp.subscribe('/topic/traffic-intervals',
                                function (payload, headers, res) {
                                    console.log("Data received from web socket=",payload);
                                    for (var i = 0; i < payload.intervals.length; i++) {
                                      $scope.signals[payload.intervals[i].roadId].currentGreenIntervalTime = payload.intervals[i].interval;
                                    }
                                });
});


});
