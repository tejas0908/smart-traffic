angular.module('traffic-mgmt-webapp')

.factory('SocketService',function($rootScope, $stomp){

        //  var myIoSocket = io.connect($rootScope.baseURL,{path:'/smart-traffic'});  ,socketFactory
        //
        //   mySocket = socketFactory({
        //     ioSocket: myIoSocket
        //   });
        //
        // return mySocket;



      //  $stomp
      //.connect('/smart-traffic')


      //.then(function (frame) {
        //console.log("Web Socket Connected successfully ",frame);
        // var subscription = $stomp.subscribe('/dest', function (payload, headers, res) {
        //   $scope.payload = payload
        // }, {
        //   'headers': 'are awesome'
        // });
        //
        // // Unsubscribe
        // subscription.unsubscribe()
        //
        // // Send message
        // $stomp.send('/dest', {
        //   message: 'body'
        // }, {
        //   priority: 9,
        //   custom: 42 // Custom Headers
        // });
        //
        // // Disconnect
        // $stomp.disconnect(function () {
        //   $log.info('disconnected')
        // });
    //  });
        return null;
    })
