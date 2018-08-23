

// Car Info Object that will feed our control panal
var car = {
      engineRpm: 1,
      vehicleSpeed: 1,
      coolantTemp: 0,
      fuel: 0,
      location: {
        lat: 0.0,
        lng: 0.0
      },
      
      mpg: 0,
      vin: ""
    };
    
    var vin = "";
    var marker = null;
    var map ="";
	var oldTemp = 1;
	var oldFuel = 1;

// Vin Dictionary for the Navbar 
vinDictionary={Truck: 'ZA9UF78J7FA190717',Van:'SCEDT26T0BD007019',Car:'CAR2014HELWAN'};

// Image Dictionary for each Vin
imageDictionary={Truck: './images/Car1.PNG',Van:'./images/Car2.PNG',Car:'./images/Car3.PNG'};

// change the vin & image when user click on navbar list item 
$(function(){ 
    $(".navbar-default .navbar-nav > li").click(function(e){ 
        $("#_carImage").empty();
        var txt = $(e.target).text();
        var img = document.createElement("img");
        img.src = imageDictionary[txt];
        var src= document.getElementById("_carImage"); 
        src.appendChild(img);
     $("img").attr("width","180px");
         $("img").attr("height","180px");
        
       // alert(imageDictionary[txt]);
        $(this).addClass("my-active").siblings().removeClass("my-active");
		
        setVin(vinDictionary[txt]);
		
        
    });
});

//onload page 
$( document ).ready(function() {
        $("#_carImage").empty();
        var img = document.createElement("img");
        img.src = "./images/Car1.PNG";
        var src= document.getElementById("_carImage"); 
        src.appendChild(img);
         $("img").attr("width","180px");
         $("img").attr("height","180px");
		
        setVin(vinDictionary['Truck']);
});


    function setVin(newVin) {
        vin = newVin;
        
    }

// Display Google Map 
function initAutocomplete() {

       
         
         map = new google.maps.Map(document.getElementById('map'), {

            zoom: 14,
            mapTypeId: 'roadmap'
        });
        var pos = {
                        lat: 29.333,
                        lng: 32.345
                    };

        map.setCenter(pos);
           
      
    }

// senf GET request to the Spring Data Rest API to get the Selected Vin data
  function getCarInfo() {
      
     // startMeter();/////////////////////////////
      if(vin != "")
      {
          
        selectedVin = vin;  
          
      
     var fullURL =  "http://localhost:8080/carinfo/"+selectedVin;
    
        $.ajax({

            type: "GET",
            url: fullURL,
            
            success: function (carInfo) {

                
                car.vin = selectedVin;
                
                car.engineRpm = Math.round(carInfo["rpm"]);
                car.vehicleSpeed = Math.round(carInfo["vehicleSpeed"]);
                car.coolantTemp = Math.round(carInfo["coolantTemp"]);
                car.fuel = Math.round(carInfo["fuelLevelInput"] * 100)/100;
                car.location.lat =parseFloat (carInfo["latitude"]);
                car.location.lng =parseFloat (carInfo["longitude"]);
                car.mpg = Math.round(carInfo["mpgInstantaneous"] * 10) / 10;
                map.panTo(car.location);
                addMarker();
                startMeter();
//alert(car.vehicleSpeed +" -- "+car.engineRpm );
		
             //   alert(carInfo["rpm"] + " => " +JSON.stringify(carInfo) );
            }, 
            error:(function() {
               car.vin = "";
               car.engineRpm = 0;
               car.vehicleSpeed = 0;
               car.coolantTemp = 0;
               car.fuel = 0;
               car.location.lat = 0.0;
               car.location.lng = 0.0;               
               car.mpg = 0;
            
         //       alert("ERROR !");
            }),
			statusCode: {
    404: function() {
		  car.vin = "";
               car.engineRpm = 0;
               car.vehicleSpeed = 0;
               car.coolantTemp = 0;
               car.fuel = 0;
               car.location.lat = 0.0;
               car.location.lng = 0.0;               
               car.mpg = 0;
			   startMeter();
			   marker = null;
			   addMarker();
      console.log("ERROR! Selected Vin Not Found");
    }
  }
			

        })
		}
    }


function addMarker()
{ 
map.setCenter(car.location);
if(marker != null){
	marker.setPosition(car.location);
	}
	else
	{
		var carIcon = {
        anchor: new google.maps.Point(35,35),
        origin: new google.maps.Point(0, 0),
        scaledSize: new google.maps.Size(70, 70),
        url: 'images/car_marker.png'
    };
		 marker = new google.maps.Marker({
                position: car.location,
                map: map,
                animation: google.maps.Animation.BOUNCE,
                icon: carIcon

            });
		
	}
    
    
    map.setCenter(car.location);
   
			

    
}


// Display Meters For car info ( coolant Temp - Fuel - Speed - RPM)

function startMeter()
{

     oldTemp = car.coolantTemp;
    
    	
    
     $(".coolant-temp-bar").circularProgress({
        line_width: 6,
        color: "#CD5C5C",
        starting_position: 127.5, // 12.00 o' clock position, 25 stands for 3.00 o'clock (clock-wise)
        percent: oldTemp, // percent starts from
        degree: true,
		counter_clockwise: false,
        percentage:false,
        text: "COOLANT TEMP"
    }).circularProgress('animate',car.coolantTemp, 100);
	oldTemp = car.coolantTemp;
    
    	
	
    oldFuel = car.fuel;
        
    $(".fuel-bar").circularProgress({
        line_width: 6,
        color: "#CD5C5C",
        starting_position: 50, // 12.00 o' clock position, 25 stands for 3.00 o'clock (clock-wise)
        percent: oldFuel, // percent starts from
        percentage: true,
		counter_clockwise: false,
        degree:false,
        text: "FUEL"
    }).circularProgress('animate',car.fuel, 100);
	oldFuel = car.fuel;
	
	///////////////////////////////////
    const speedMeters = document.querySelectorAll('svg[data-value] .speedmeter');
speedMeters.forEach( (path) => {
  // Get the length of the path
  let length = path.getTotalLength();
    

  // Get the value of the meter
  let value = car.vehicleSpeed;
//alert(value);
  document.getElementById('_speed').textContent = car.vehicleSpeed.toString();
   // alert(car.vehicleSpeed.toString());
  // Calculate the percentage of the total length
  let to = length * ((100 - value) / 100);
    
  // Trigger Layout in Safari hack https://jakearchibald.com/2013/animated-line-drawing-svg/
  path.getBoundingClientRect();
  // Set the Offset
  path.style.strokeDashoffset = Math.max(0, to);  
});
    
    ////////////////////////////////////
    const rpmMeters = document.querySelectorAll('svg[data-value] .rpmmeter');

rpmMeters.forEach( (path) => {
  // Get the length of the path
  let length = path.getTotalLength();

  // Get the value of the meter
  let value = car.engineRpm;
//alert(value);
  document.getElementById('_rpm').textContent = car.engineRpm.toString();
   // alert(car.engineRpm.toString());
  // Calculate the percentage of the total length
  let to = length * ((10000 - value) / 10000);
  // Trigger Layout in Safari hack https://jakearchibald.com/2013/animated-line-drawing-svg/
  path.getBoundingClientRect();
    
  // Set the Offset
  path.style.strokeDashoffset = Math.max(0, to);  
});
    
    // 

       
    


}

setTimeout(initAutocomplete, 2000);
    
setInterval(getCarInfo,3000);
