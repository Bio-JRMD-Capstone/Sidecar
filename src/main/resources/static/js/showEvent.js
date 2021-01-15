let map, infoWindow, geocoder;
let thisEventLat = parseFloat($("#lat").val());
let thisEventLng = parseFloat($("#lng").val());

function initMap() {
    //Taking the values of the lat and lng of the thisEvent we need, then centering the map on the thisEvent
    map = new google.maps.Map(document.getElementById("map"), {
        center: { lat: thisEventLat, lng: thisEventLng },
        zoom: 14,
    });
    infoWindow = new google.maps.InfoWindow();

    //Pans the map back to the thisEvent if the center of the map changes
    map.addListener("center_changed", () => {
        // 5 seconds after the center of the map has changed, pan back to the
        // marker.
        window.setTimeout(() => {
            map.panTo({ lat: thisEventLat, lng: thisEventLng});
        }, 5000);
    });

    //This is supposed to retrieve the list of POIs in JSON format so we can work with it to display them on the map.
    // See https://java.codeup.com/spring/extra-features/json-response/ for more info
    (function($) {
        var request = $.ajax({'url': '/events.json'});
        request.done(function (thisEvents) {
            thisEvents.forEach(function(thisEvent) {
                drawEvents(thisEvent, infoWindow, map);
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

//Adds markers for the thisEvents on the map and assigns their infowindow information
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
        //PLaceholder event icon
        icon: "/images/icons/event.png"
    });
    //The line that actually attaches a marker to the map
    marker.setMap(map);
    //This connects the info window to the marker, allowing information, links, any HTML really to be displayed
    google.maps.event.addListener(marker, 'click', function() {
        infoWindow.setContent("<h4>" + thisEvent.name + "</h4>" +
            "<a href='/event/" + thisEvent.id + "'>More Info</a>");
        infoWindow.open(map, marker);
    });
}