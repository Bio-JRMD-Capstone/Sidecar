// let markers = [{location: {lat: 31.165141833483684, lng: -99.27938967601061 }},{location: {lat: 31.052265437002188, lng: -97.46664553538561 }},{location: {lat: 30.24890311353849, lng: -97.86215334788561 }}]
let coords = document.getElementById("coords").textContent.split("},")

// coords.replace("{location: { lat:", "")



console.log(coords)

let repMark = []

for (let i = 0; i < coords.length; i++){
    repMark.push(coords[i].replace("{location: {lat: ", ""))
}

console.log(repMark)

let lngRep = []

for (let i = 0; i < coords.length; i++){
    lngRep.push(repMark[i].replace("lng: ", ""))
}

console.log(lngRep)

let coordsOnly = []

for (let i = 0; i < coords.length; i++){
    coordsOnly.push(lngRep[i].replace(" }", ""))
}

console.log(coordsOnly)

let coordsSelf = []

for (let i = 0; i < coords.length; i++){
    coordsSelf.push(coordsOnly[i].replace("}", ""))
}

console.log(coordsSelf)

let markTest = []

console.log(coordsOnly[0].split(", "));

for (let i = 0; i < coordsOnly.length; i++){
    markTest.push(coordsOnly[i].split(", "))
}

console.log(markTest);

let markParsed = []

for (let i = 0; i < markTest.length; i++){
    let obj = {}
    obj["lat"] = parseFloat(markTest[i][0])
    obj["lng"] = parseFloat(markTest[i][1])
    markParsed.push(obj)
}

console.log(markParsed)

let markers = markParsed.map(n => {
    return {location: n}
});

console.log(markers)


// let icoords = document.getElementById("coords").value.split("},")

console.log(coords)


function initMap() {
    const map = new google.maps.Map(document.getElementById("map"), {
        zoom: 4,
        center: {lat: 34.7062978, lng: -116.1274117},
    });




    // TESTING MARKERS, ADDS BLUE MARKER ON THE MAP
    // let markerBlue = new google.maps.Marker({
    //     map: map,
    //     position: markers[0].location,
    //     icon: {
    //         url: "http://maps.google.com/mapfiles/ms/icons/blue-dot.png"
    //     }
    // });


    const directionsService = new google.maps.DirectionsService();
    const directionsRenderer = new google.maps.DirectionsRenderer({
        draggable: false,
        map,
        panel: document.getElementById("right-panel"),
    });
    console.log(markers)
    displayRoute(
        markers[0],
        markers[markers.length-1],
        directionsService,
        directionsRenderer
    );
    calculateAndDisplayRoute(directionsService, directionsRenderer);
}


function displayRoute(origin, destination, service, display) {
    // markers = markers.map(n => {
    //     const markerMapped = {location: n};
    //     return markerMapped
    // });

    console.log(markers)
    service.route(
        {
            origin: origin,
            destination: destination,
            waypoints: markers,
            travelMode: google.maps.TravelMode.DRIVING,
            avoidTolls: true,
        },
        (result, status) => {
            if (status === "OK") {
                display.setDirections(result);
            } else {
                alert("Could not display directions due to: " + status);
            }
        }
    );

}


function calculateAndDisplayRoute(directionsService, directionsRenderer) {
    const waypts = [];

    for (let i = 1; i < markers.length-1; i++) {
        waypts.push({
            location: markers[i],
            stopover: true,
        });

    }
    directionsService.route(
        {
            origin: markers[0],
            destination: markers[markers.length-1],
            waypoints: waypts,
            optimizeWaypoints: true,
            travelMode: google.maps.TravelMode.DRIVING,
        },
        (response, status) => {
            if (status === "OK") {
                directionsRenderer.setDirections(response);
                const route = response.routes[0];
                const summaryPanel = document.getElementById("directions-panel");
                summaryPanel.innerHTML = "";
                let distance = document.getElementById("distance");
                let time = document.getElementById("time");

                let totalDistance = 0;
                let totalDuration = 0;

                let randfunc = function (x){
                    return x;
                }

                // For each route, display summary information.
                if (markers[markers.length-1].lat === markers[0].lat + .000000000000001) {
                    for (let i = 1; i < route.legs.length; i++) {
                        const routeSegment = i;

                        totalDistance += parseFloat(route.legs[i].distance.text);
                        totalDuration += parseInt(route.legs[i].duration.text);


                        console.log(route.legs[i].distance.text)

                        summaryPanel.innerHTML +=
                            "<b>Route Segment: " + routeSegment + "</b><br>";
                        summaryPanel.innerHTML += route.legs[i].start_address + "<br> to <br>";
                        summaryPanel.innerHTML += route.legs[i].end_address + "<br>";
                        summaryPanel.innerHTML += route.legs[i].distance.text + "<br>";
                        summaryPanel.innerHTML += route.legs[i].duration.text + "<br><hr><br>"
                    }
                } else {
                    for (let i = 0; i < route.legs.length; i++) {
                        const routeSegment = i + 1;

                        totalDistance += parseFloat(route.legs[i].distance.text);
                        totalDuration += parseInt(route.legs[i].duration.text);


                        console.log(route.legs[i].distance.text)

                        summaryPanel.innerHTML +=
                            "<b>Route Segment: " + routeSegment + "</b><br>";
                        summaryPanel.innerHTML += route.legs[i].start_address + "<br> to <br>";
                        summaryPanel.innerHTML += route.legs[i].end_address + "<br>";
                        summaryPanel.innerHTML += route.legs[i].distance.text + "<br>";
                        summaryPanel.innerHTML += route.legs[i].duration.text + "<br><hr><br>"
                    }
                }




            } else {
                window.alert("Directions request failed due to " + status);
            }
        }
    );
}

let loadTotal = 0;

function initMapClear() {
    loadTotal++;
    console.log(loadTotal)
    if (loadTotal > 2){
        window.location.reload();
    }

    document.getElementById("address").value = "";

    var map;
    let markers = []

    var lat_lng = {lat: 39.63476588674744, lng: -101.15442912683487 };
    map = new google.maps.Map(document.getElementById('map'), {
        zoom: 5,
        center: lat_lng
    });

    geocoder = new google.maps.Geocoder();
    document.getElementById("submit").addEventListener("click", () => {
        geocodeAddress(geocoder, map);
    });

    //Geocoder, searches for input location and centers map on it
    function geocodeAddress(geocoder, resultsMap) {
        const address = document.getElementById("address").value;

        geocoder.geocode({address: address}, (results, status) => {
            if (status === "OK") {
                resultsMap.setCenter(results[0].geometry.location);
                resultsMap.setZoom(10);
            } else {
                alert(
                    "Geocode was not successful for the following reason: " + status
                );
            }
        });
    }


    // let marker = new google.maps.Marker({
    //     map: map,
    //     position: lat_lng,
    //     icon: {
    //         url: "http://maps.google.com/mapfiles/ms/icons/blue-dot.png"
    //     }
    // });
    // This event listener will call addMarker() when the map is clicked.
    map.addListener('click', function(event) {
        addMarker(event.latLng);
    });

    const summaryPanel = document.getElementById("directions-panel");
    summaryPanel.innerHTML = "";

    document.getElementById("distance").value = "";
    document.getElementById("time").value = "";

    var currentId = 0;
    var uniqueId = function () {
        return ++currentId;
    }
// // Adds a marker to the map and push to the array.
    function addMarker(location) {
        var marker = new google.maps.Marker({
            position: location,
            map: map
        });
    }
    ;
    var objLoc = {};
    // Create new marker on single click event on the map
    google.maps.event.addListener(map, 'click', function (event) {
        var id = uniqueId(); // get new id
        var marker = new google.maps.Marker({
            id: id,
            position: event.latLng,
            map: map,
            title: event.latLng.lat() + ', ' + event.latLng.lng(),

        });

        var obj = {};
        obj["lat"] = event.latLng.lat();
        obj["lng"] = event.latLng.lng();
        markers.push(obj);
        Object.assign(objLoc, obj)
        console.log(objLoc)
        console.log(markers)
    });

    document.getElementById("routeCheck").checked = false


    function initMapRoute() {
        const map = new google.maps.Map(document.getElementById("map"), {
            zoom: 4,
            center: {lat: 34.7062978, lng: -116.1274117},
        });
        if (document.getElementById("routeCheck").checked === true) {
            let objRepeat = {}
            objRepeat["lat"] = markers[0].lat + .000000000000001
            objRepeat["lng"] = markers[0].lng

            markers.push(objRepeat)
        }


        const directionsService = new google.maps.DirectionsService();
        const directionsRenderer = new google.maps.DirectionsRenderer({
            draggable: false,
            map,
            panel: document.getElementById("right-panel"),
        });
        console.log(markers)
        displayRoute(
            markers[0],
            markers[markers.length-1],
            directionsService,
            directionsRenderer
        );
        calculateAndDisplayRoute(directionsService, directionsRenderer);
    }
    function displayRoute(origin, destination, service, display) {
        markers = markers.map(n => {
            const markerMapped = {location: n};
            return markerMapped
        });

        console.log(markers)

        console.log(markers[0].location.lat)



        let markersString = [];
        for(let i = 0; i < markers.length; i++){

            markersString.push("{location: {lat: " + markers[i].location.lat + ", lng: " + markers[i].location.lng + " }}");
            console.log(markersString)
        }
        console.log("{location: {lat: " + markers[0].location.lat + ", lng: " + markers[0].location.lng + " }}");

        document.getElementById("coordinates").value = markersString;
        console.log(markers)
        service.route(
            {
                origin: origin,
                destination: destination,
                waypoints: markers,
                travelMode: google.maps.TravelMode.DRIVING,
                avoidTolls: true,
            },
            (result, status) => {
                if (status === "OK") {
                    display.setDirections(result);
                } else {
                    alert("Could not display directions due to: " + status);
                }
            }
        );
    }


    function calculateAndDisplayRoute(directionsService, directionsRenderer) {
        let waypts = [];

        for (let i = 1; i < markers.length; i++) {
            waypts.push({
                location: markers[i],
                stopover: true,
            });

        }

        directionsService.route(
            {
                origin: markers[0],
                destination: markers[markers.length-1],
                waypoints: waypts,
                optimizeWaypoints: true,
                travelMode: google.maps.TravelMode.DRIVING,
            },
            (response, status) => {
                if (status === "OK") {
                    directionsRenderer.setDirections(response);
                    const route = response.routes[0];
                    const summaryPanel = document.getElementById("directions-panel");
                    summaryPanel.innerHTML = "";
                    let distance = document.getElementById("distance");

                    let totalDistance = 0;
                    let totalDuration = 0;

                    // For each route, display summary information.
                    if (document.getElementById("routeCheck").checked === true) {
                        for (let i = 1; i < route.legs.length; i++) {
                            const routeSegment = i;

                            totalDistance += parseFloat(route.legs[i].distance.text);

                            console.log(route.legs[i].distance.text)

                            summaryPanel.innerHTML +=
                                "<b>Route Segment: " + routeSegment + "</b><br>";
                            summaryPanel.innerHTML += route.legs[i].start_address + "<br> to <br>";
                            summaryPanel.innerHTML += route.legs[i].end_address + "<br>";
                            summaryPanel.innerHTML += route.legs[i].distance.text + "<br>";
                            summaryPanel.innerHTML += route.legs[i].duration.text + "<br><hr><br>"
                        }
                    } else {
                        for (let i = 0; i < route.legs.length-1; i++) {
                            const routeSegment = i + 1;

                            totalDistance += parseFloat(route.legs[i].distance.text);
                            totalDuration += parseInt(route.legs[i].duration.text);

                            summaryPanel.innerHTML +=
                                "<b>Route Segment: " + routeSegment + "</b><br>";
                            summaryPanel.innerHTML += route.legs[i].start_address + "<br> to <br>";
                            summaryPanel.innerHTML += route.legs[i].end_address + "<br>";
                            summaryPanel.innerHTML += route.legs[i].distance.text + "<br>";
                            summaryPanel.innerHTML += route.legs[i].duration.text + "<br><hr><br>"
                        }
                    }



                    distance.value = totalDistance.toFixed(2);




                } else {
                    window.alert("Directions request failed due to " + status);
                }
            }
        );
    }

    $(function(){
        $( "#testbtn" ).on( 'click', function (){
            initMapRoute();
        });

        $( "#clearBtn" ).on( 'click', initMap);
    });



    //Closing Brace of INITMAP
}

$(function(){
    // $( "#testbtn" ).on( 'click', function (){
    //     initMapRoute();
    // });

    $( "#clearBtn" ).on( 'click', initMapClear);
});
