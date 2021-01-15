let map, infoWindow, geocoder;
let thisEventLat = parseFloat($("#lat").val());
let thisEventLng = parseFloat($("#lon").val());
let userMarker;


function initMap() {
    console.log(thisEventLat, thisEventLng);
    //Taking the values of the lat and lng of the point we need, then centering the map on the point
    map = new google.maps.Map(document.getElementById("map"), {
        center: { lat: thisEventLat, lng: thisEventLng },
        zoom: 14,
    });
    infoWindow = new google.maps.InfoWindow();

    //Pans the map back to the point if the center of the map changes
    //Currently Commented out as the functionality on edit pages leaves something to be desired
    //If user tries to move the map to change marker or just look around it moves them back after 5 secs
    //Even if they are in the middle of moving the map around. Very annoying, especially if the point is not where you
    //want to change it to

    // map.addListener("center_changed", () => {
    //     // 5 seconds after the center of the map has changed, pan back to the
    //     // marker.
    //     window.setTimeout(() => {
    //         map.panTo({ lat: thisEventLat, lng: thisEventLng});
    //     }, 5000);
    // });

    //Set the POI to be edited on the map
    let editEventCoords = {
        'lat': thisEventLat,
        'lng': thisEventLng
    }
    userMarker = new google.maps.Marker({
        position: editEventCoords,
        draggable: true
    });
    userMarker.setMap(map);

    //Makes it so that dragging the marker updates the lat/long to be submitted
    userMarker.addListener("drag", (event) => {
        $('#lat').val(event.latLng.lat());
        $('#lon').val(event.latLng.lng());
    });

    //When the map is clicked, add a point and fill in the lat/lng values in html using jQuery
    google.maps.event.addListener(map, "click", function(event) {
        placeMarker(event.latLng);
        $('#lat').val(event.latLng.lat());
        $('#lon').val(event.latLng.lng());
    });

    //This is supposed to retrieve the list of POIs in JSON format so we can work with it to display them on the map.
    //For this page, it skips over the POI to be edited, because we need to treat that one differently.
    // See https://java.codeup.com/spring/extra-features/json-response/ for more info
    (function($) {
        var request = $.ajax({'url': '/events.json'});
        request.done(function (events) {
            events.forEach(function(oneEvent) {
                if(oneEvent.lat != thisEventLat && oneEvent.lng != thisEventLng) {
                    drawEvents(oneEvent, infoWindow, map);
                }
            });
        });
    })(jQuery);
}

//Geocoder, searches for input location and centers map on it
function geocodeAddress(geocoder, resultsMap) {
    const address = document.getElementById("address").value;
    geocoder.geocode({address: address}, (results, status) => {
        if (status === "OK") {
            resultsMap.setCenter(results[0].geometry.location);
        } else {
            alert(
                "Geocode was not successful for the following reason: " + status
            );
        }
    });
}

//Adds markers for the events on the map and assigns their infowindow information
function drawEvents(thisEvent, infoWindow, map) {
    //Setting up the proper latLng object notation so it can be read by Google Maps
    let coords = {
        'lat': Number(thisEvent.lat),
        'lng': Number(thisEvent.lon)
    };
    //Creates a marker and assigns some info to it
    let marker = new google.maps.Marker({
        position: coords,
        title: thisEvent.name,
        //Uses placeholder event icon
        icon: "/images/icons/event.png"
    });
    //The line that actually attaches a marker to the map
    marker.setMap(map);
    //This connects the info window to the marker, allowing information, links, any HTML really to be displayed
    google.maps.event.addListener(marker, 'click', function() {
        infoWindow.setContent("<h4>" + thisEvent.name + "</h4>" +
            "<a href='event/" + thisEvent.id + "'>More Info</a>");
        infoWindow.open(map, marker);
    });
}

//if marker already exists on map, move it. if not, create it at the location
function placeMarker(location) {
    if (userMarker) {
        //if marker already was created change position
        userMarker.setPosition(location);
    } else {
        //create a marker
        userMarker = new google.maps.Marker({
            position: location,
            map: map,
        });
    }
}