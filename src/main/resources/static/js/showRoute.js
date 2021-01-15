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


    // Blur marker on the map
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
        suppressMarkers: true,
    });
    console.log(markers)

    const startMarker = new google.maps.Marker({
        position: markers[0].location,
        map,
        icon: {
            url: "http://maps.google.com/mapfiles/ms/icons/green-dot.png"
        }
    });


    if(markers[markers.length-1].location.lat === markers[0].location.lat){
        const endMarker = new google.maps.Marker({
            position: markers[markers.length-1],
            map,
            icon: {
                url: "http://maps.google.com/mapfiles/ms/icons/green-dot.png"
            }
        });
    } else {
        const endMarker = new google.maps.Marker({
            position: markers[markers.length-1].location,
            map,
            icon: {
                url: "http://maps.google.com/mapfiles/ms/icons/red-dot.png"
            }
        });
    }

    for(let i = 1; i < markers.length-1; i++) {
        let midMarker = new google.maps.Marker({
            position: markers[i].location,
            map,
            icon: {
                url: "http://maps.google.com/mapfiles/ms/icons/yellow-dot.png"
            }
        });
    }

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
            optimizeWaypoints: false,
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
                // if (markers[markers.length-1].lat === markers[0].lat + .000000000000001) {
                    for (let i = 0; i < route.legs.length; i++) {
                        const routeSegment = i + 1;

                        totalDistance += parseFloat(route.legs[i].distance.text);
                        totalDuration += parseInt(route.legs[i].duration.text);


                        console.log(route.legs[i].distance.text)

                        summaryPanel.innerHTML +=
                            "<b>Leg: " + routeSegment + "</b><br>";
                        summaryPanel.innerHTML += route.legs[i].start_address + "<br> to <br>";
                        summaryPanel.innerHTML += route.legs[i].end_address + "<br>";
                        summaryPanel.innerHTML += route.legs[i].distance.text + "<br>";
                        summaryPanel.innerHTML += route.legs[i].duration.text + "<br><hr><br>"
                    }
                // } else {
                //     for (let i = 0; i < route.legs.length; i++) {
                //         const routeSegment = i + 1;
                //
                //         totalDistance += parseFloat(route.legs[i].distance.text);
                //         totalDuration += parseInt(route.legs[i].duration.text);
                //
                //
                //         console.log(route.legs[i].distance.text)
                //
                //         summaryPanel.innerHTML +=
                //             "<b>Leg: " + routeSegment + "</b><br>";
                //         summaryPanel.innerHTML += route.legs[i].start_address + "<br> to <br>";
                //         summaryPanel.innerHTML += route.legs[i].end_address + "<br>";
                //         summaryPanel.innerHTML += route.legs[i].distance.text + "<br>";
                //         summaryPanel.innerHTML += route.legs[i].duration.text + "<br><hr><br>"
                //     }
                // }




            } else {
                window.alert("Directions request failed due to " + status);
            }
        }
    );
}


        // initMap();