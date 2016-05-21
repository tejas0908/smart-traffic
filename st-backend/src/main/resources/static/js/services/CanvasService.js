angular.module('traffic-mgmt-webapp')

.service('CanvasService',function($rootScope){
          this.draw = function(context) {
            //context.clearRect(0, 0, $rootScope.canvas.width,
            //    $rootScope.canvas.length);
            context.beginPath();
            context.rect(0, 300, 1500, 50);
            context.fillStyle = 'gray';
            context.fill();
            //context.lineWidth = 1;
            //context.strokeStyle = 'black';
            //context.stroke();

            context.beginPath();
            context.rect(0, 350, 1500, 50);
            context.fillStyle = 'gray';
            context.fill();
            //context.lineWidth = 1;
            //context.strokeStyle = 'black';
            //context.stroke();

            context.beginPath();
            context.rect(700, 0, 50, 750);
            context.fillStyle = 'gray';
            context.fill();
            //context.lineWidth = 0;
            //context.strokeStyle = 'black';
            //context.stroke();

            context.beginPath();
            context.rect(750, 0, 50, 750);
            context.fillStyle = 'gray';
            context.fill();
            //context.lineWidth = 1;
            //context.strokeStyle = 'black';
            //context.stroke();

            context.beginPath();
            context.rect(748, 0, 4, 300);
            context.fillStyle = 'brown';
            context.fill();

            context.beginPath();
            context.rect(748, 400, 4, 300);
            context.fillStyle = 'brown';
            context.fill();

            context.beginPath();
           context.rect(0, 348, 700, 4);
            context.fillStyle = 'brown';
            context.fill();

            context.beginPath();
           context.rect(800, 348, 700, 4);
            context.fillStyle = 'brown';
            context.fill();
            // context.closePath();

            context.beginPath();
            context.setLineDash([10]);
            context.moveTo(0,325);
            context.lineTo(700,325);
            context.lineWidth = 2;
            context.strokeStyle = 'white';
            //context.closePath();
            context.stroke();

            context.beginPath();
            context.setLineDash([10]);
            context.moveTo(800,325);
            context.lineTo(1500,325);
            context.lineWidth = 2;
            context.strokeStyle = 'white';
            //context.closePath();
            context.stroke();

            context.beginPath();
            context.setLineDash([10]);
            context.moveTo(0,375);
            context.lineWidth = 2;
            context.lineTo(700,375);
            context.strokeStyle = 'white';
            //context.closePath();
            context.stroke();

            context.beginPath();
            context.setLineDash([10]);
            context.moveTo(800,375);
            context.lineTo(1500,375);
            context.lineWidth = 2;
            context.strokeStyle = 'white';
            //context.closePath();
            context.stroke();

            context.beginPath();
            context.setLineDash([10]);
            context.moveTo(725,0);
            context.lineTo(725,300);
            context.lineWidth = 2;
            context.strokeStyle = 'white';
            //context.closePath();
            context.stroke();

            context.beginPath();
            context.setLineDash([10]);
            context.moveTo(725,400);
            context.lineTo(725,700);
            context.lineWidth = 2;
            context.strokeStyle = 'white';
            //context.closePath();
            context.stroke();

            context.beginPath();
            context.setLineDash([10]);
            context.moveTo(775,0);
            context.lineTo(775,300);
            context.lineWidth = 2;
            context.strokeStyle = 'white';
            //context.closePath();
            context.stroke();

            context.beginPath();
            context.setLineDash([10]);
            context.moveTo(775,400);
            context.lineTo(775,700);
            context.lineWidth = 2;
            context.strokeStyle = 'white';
            //context.closePath();
            context.stroke();


            // var obj = document.getElementById("slider1");
            //        obj.style.position = "fixed";
            //        obj.style.top = "263px";
            //        obj.style.left = "20px";
        };
    })
