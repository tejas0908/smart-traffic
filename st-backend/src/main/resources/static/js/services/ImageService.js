/*
 **  Service provider
 */
angular.module('traffic-mgmt-webapp').service('ImageService', function($http, $q, $rootScope) {
  var getData = function(url) {

    var deferred = $q.defer();
    $http.get('/ctolabs/cbir/api/v1.0/retrieve_url?imageurl=' + url).success(function(data) {
      deferred.resolve(data);
    }).error(function() {
      deferred.reject('An error occured while fetching items');
    });

    return deferred.promise;
  };

  var postData = function(imagePath, callback, scope) {
    var win = function(r) {
      //console.log('Code = ' + r.responseCode + ' response:' + r.response);
      callback(r.response);
    }

    var fail = function(error) {
      console.log('An error has occurred: Code = ' + error.code);
      console.log('upload error source ' + error.source);
      console.log('upload error target ' + error.target);
    }

    var options = new FileUploadOptions();
    options.fileKey = 'road1';

    //options.fileName = '';
    //options.mimeType = 'text/plain';

    var params = {};

    //params.value1 = 'test';
    //params.value2 = 'param';

    options.params = params;

    var ft = new FileTransfer();

    // ft.onprogress = function(progressEvent) {
    //   if (progressEvent.lengthComputable) {
    //     scope.uploadedFilePercent.value = parseInt((progressEvent.loaded / progressEvent.total) * 100, 10);
    //
    //     //scope.apply();
    //     //scope.digest();
    //     //console.log('>>>'+$rootScope.uploadedFilePercent.value);
    //   } else {
    //     console.log('progressEvent.lengthComputable not computable');
    //   }
    // };
    //
    // scope.uploadedFilePercent.value = 0;
    ft.upload(imagePath, '/get-signal-times', win, fail, options);

  };

  return {
    getData: getData,
    postData: postData
  };
});
