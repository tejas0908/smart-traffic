angular.module('traffic-mgmt-webapp')

.controller('DemoCtrl', function($scope, $interval, CanvasService, $timeout, $stomp, $http) {
  var canvas = document.getElementById('myCanvas2');
  var context = canvas.getContext('2d');

  // $scope.road2Image = {};
  // $scope.road4Image = {};
  // $scope.road6Image = {};
  // $scope.road8Image = {};

  $scope.counter = {
    value: 0
  };
  $scope.roads = {
    2: {
      conjestionLevel: 30,
      imageFile: null,
      imageSource:null,
      nrOfvehicles:null,
      boxes:[],
      loop: null
    },
    4: {
      conjestionLevel: 30,
      imageFile: null,
      imageSource:null,
      nrOfvehicles:null,
      boxes:[],
      loop: null
    },
    6: {
      conjestionLevel: 30,
      imageFile: null,
      imageSource:null,
      nrOfvehicles:null,
      boxes:[],
      loop: null
    },
    8: {
      conjestionLevel: 30,
      imageFile: null,
      imageSource:null,
      nrOfvehicles:null,
      boxes:[],
      loop: null
    }
  };

  $scope.signals = {
    2: {
      defaultGreenIntervalTime: 5000,
      currentGreenIntervalTime: 5000,
      history: [],
      counter: 0
    },
    4: {
      defaultGreenIntervalTime: 5000,
      currentGreenIntervalTime: 5000,
      history: [],
      counter: 0
    },
    6: {
      defaultGreenIntervalTime: 5000,
      currentGreenIntervalTime: 5000,
      history: [],
      counter: 0
    },
    8: {
      defaultGreenIntervalTime: 5000,
      currentGreenIntervalTime: 5000,
      history: [],
      counter: 0
    }
  };

  var historyRecordsSize = 5;

  $scope.currentGreenSignal = {
    signalId: 2
  }

  var flushOutHistory = function(signalId) {
    var currentHistorySize = $scope.signals[signalId].history.length;
    if (currentHistorySize == historyRecordsSize) {
      $scope.signals[signalId].history.splice(0, 1)
    }
  };

  var addRecordIntoHistory = function(signalId, record) {
    $timeout(function() {
      var currentHistorySize = $scope.signals[signalId].history.length;
      if (currentHistorySize == 0) {
        flushOutHistory(signalId);
        $scope.signals[signalId].history.push(record);
      } else {
        if ($scope.signals[signalId].history[currentHistorySize - 1].signalStatus == 0 && record.signalStatus == 1) {
          flushOutHistory(signalId);
          $scope.signals[signalId].history.push(record);
        } else {
          if ($scope.signals[signalId].history[currentHistorySize - 1].signalStatus == 1) {
            flushOutHistory(signalId);
            $scope.signals[signalId].history.push(record);
          }
        }
      }

    }, 0);
  };

  var createHistoryRecord = function(greenSignalId) {
    for (var signalId in $scope.signals) {
      if (signalId == greenSignalId) {
        addRecordIntoHistory(signalId, {
          signalStatus: 1,
          time: new Date()
        });
      } else {
        addRecordIntoHistory(signalId, {
          signalStatus: 0,
          time: new Date()
        });
      }
    }
  };

  var counterLoop;

  var startCounter = function() {
    $interval.cancel(counterLoop);
    counterLoop = $interval(function() {
      if ($scope.counter.value > 1) {
        $scope.counter.value--;
        //startCounter();
      }
    }, 1000);

  };
  var updateGreenSignal = function(keys, currentKeysIndex) {
    $scope.currentGreenSignal.signalId = keys[currentKeysIndex];
    $scope.counter.value = Math.floor($scope.signals[keys[currentKeysIndex]].currentGreenIntervalTime / 1000);
    startCounter();
    createHistoryRecord(keys[currentKeysIndex]);
    $timeout(function() {
      if (currentKeysIndex == keys.length - 1) {
        updateGreenSignal(keys, 0)
      } else {
        updateGreenSignal(keys, currentKeysIndex + 1)
      }
    }, $scope.signals[keys[currentKeysIndex]].currentGreenIntervalTime);
  };

  var _init = function() {
    CanvasService.draw(context);
    var keys = Object.keys($scope.signals);
    updateGreenSignal(keys, 0)
  };

  _init();


  // $stomp
  // .connect('/smart-traffic')
  // .then(function (frame) {
  //       console.log("Web Socket Connected successfully ",frame);
  //       var subscription = $stomp.subscribe('/topic/traffic-intervals',
  //                                 function (payload, headers, res) {
  //                                     console.log("Data received from web socket=",payload);
  //                                     for (var i = 0; i < payload.intervals.length; i++) {
  //                                       $scope.signals[payload.intervals[i].roadId].currentGreenIntervalTime = payload.intervals[i].interval;
  //                                     }
  //                                 });
  // });

  // ImageService.postData('C:/Users/m1020387/Pictures/performance.png',function(response){
  //   console.log(">>>>>>>Image uploaded successfully=",response);
  // },$scope);

$scope.responseStatus = {
  value:false
};

$scope.setImage = function(element,roadId){
  var reader = new FileReader();
  reader.onload = function(event) {
    console.log("evnt target=", event.target);
    $scope.roads[roadId].imageSource = event.target.result
    $scope.$apply()
  }
  reader.readAsDataURL(element.files[0]);
}

  $scope.upload = function() {
    // var dataObj = {
    //   road2:$scope.roads[2].imageFile,
    //   road4:$scope.roads[4].imageFile,
    //   road6:$scope.roads[6].imageFile,
    //   road8:$scope.roads[8].imageFile
    // };
    //
    // Upload.upload({
    //   url: '/get-signal-times',
    //   data: dataObj
    // }).then(function(resp) {
    //   console.log('Success ' + resp.config.data.file.name + 'uploaded. Response: ' + resp.data);
    // }, function(resp) {
    //   console.log('Error status: ' + resp.status);
    // }, function(evt) {
    //   var progressPercentage = parseInt(100.0 * evt.loaded / evt.total);
    //   console.log('progress: ' + progressPercentage);
    // });

    //--------------------------------------- other upload technique-----------------------------//
    $scope.responseStatus.value = true;
    var fd = new FormData();

    console.log("File 2=", $scope.roads[2].imageFile);

    fd.append('road2', $scope.roads[2].imageFile);
    fd.append('road4', $scope.roads[4].imageFile);
    fd.append('road6', $scope.roads[6].imageFile);
    fd.append('road8', $scope.roads[8].imageFile);

    // fd.append('road2', $scope.road2Image.value);
    // fd.append('road4', $scope.road4Image.value);
    // fd.append('road6', $scope.road6Image.value);
    // fd.append('road8', $scope.road8Image.value);

    $http.post('/get-signal-times', fd, {
        // this cancels AngularJS normal serialization of request
        transformRequest: angular.identity,
        // this lets browser set `Content-Type: multipart/form-data`
        // header and proper data boundary
        headers: {
          'Content-Type': undefined
        }
      })
      .success(function(response) {
        console.log("Got response", response);
        $scope.responseStatus.value = false;
        for (var i = 0; i < response.signalTimes.length; i++) {
          $scope.signals[response.signalTimes[i].roadId].currentGreenIntervalTime = response.signalTimes[i].time;
          $scope.roads[response.signalTimes[i].roadId].nrOfvehicles = response.signalTimes[i].vehicleCount;
          $scope.roads[response.signalTimes[i].roadId].boxes = response.signalTimes[i].boxes;
        }
		drawBoxes();
      })
      .error(function(err) {
          $scope.responseStatus.value = false;
        console.log("Error. response", err);
      });

  };


var drawBoxes = function(){
  var boxCanvas=document.getElementById("boxcanvas");
  var boxCanvasCtx=boxCanvas.getContext("2d");
  boxCanvasCtx.clearRect(0, 0, boxCanvasCtx.width, boxCanvasCtx.height);
  var img2=document.getElementById("img2");
  boxCanvasCtx.drawImage(img2,0,0);
  var boxCanvas2 = document.getElementById('boxcanvas');
  var boxCanvasCtx2 = boxCanvas2.getContext('2d');

for (var i = 0; i < $scope.roads[2].boxes.length; i++) {
	console.log("box=",$scope.roads[2].boxes[i]);
  boxCanvasCtx2.beginPath();
  boxCanvasCtx2.rect($scope.roads[2].boxes[i].minX, $scope.roads[2].boxes[i].minY, $scope.roads[2].boxes[i].maxX-$scope.roads[2].boxes[i].minX, $scope.roads[2].boxes[i].maxY-$scope.roads[2].boxes[i].minY);
  //boxCanvasCtx2.fillStyle = 'yellow';
  //boxCanvasCtx2.fill();
  boxCanvasCtx2.lineWidth = 3;
  boxCanvasCtx2.strokeStyle = 'red';
  boxCanvasCtx2.stroke();
};
};

});
