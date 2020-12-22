let markers = [{location: {lat: 31.165141833483684, lng: -99.27938967601061 }},{location: {lat: 31.052265437002188, lng: -97.46664553538561 }},{location: {lat: 30.24890311353849, lng: -97.86215334788561 }}]

function initMap() {
    const map = new google.maps.Map(document.getElementById("map"), {
        zoom: 4,
        center: {lat: 34.7062978, lng: -116.1274117},
    });


    const directionsService = new google.maps.DirectionsService();
    const directionsRenderer = new google.maps.DirectionsRenderer({
        draggable: true,
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
    const waypts = [];

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
                let time = document.getElementById("time");

                let totalDistance = 0;
                let totalDuration = 0;

                // For each route, display summary information.
                for (let i = 0; i < route.legs.length-1; i++) {
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

                console.log(totalDuration)

                distance.value = totalDistance;
                time.value = totalDuration;



            } else {
                window.alert("Directions request failed due to " + status);
            }
        }
    );
}


        // initMap();