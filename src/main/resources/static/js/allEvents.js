let map, infoWindow, geocoder;
let userLocation = $("#location").text();

function initMap() {
    map = new google.maps.Map(document.getElementById("map"), {
        center: {lat: 39.63476588674744, lng: -101.15442912683487 },
        zoom: 5
    });

    geocoder = new google.maps.Geocoder();
    infoWindow = new google.maps.InfoWindow();

    //Grabs the user's zipcode from the HTML and centers map on the location
    setLocation(geocoder, map, userLocation);

    //Event listener for enter location button
    document.getElementById("submit").addEventListener("click", () => {
        const address = document.getElementById("address").value;
        geocodeAddress(geocoder, map, address);
    });

    //To save on typing, I save the relative filepath as a variable since we will be using it a lot just below
    const iconBase = "/css/images/";

    //This array of icons is referenced when drawing a poi. It uses a string (the poi category)
    // to find the appropriate filepath for that category's icon.
    const icons = {
        "Club Event": {
            icon: iconBase + "club_event.png"
        },
        "Group Ride": {
            icon: iconBase + "group_ride.png"
        },
        "Charity Event": {
            icon: iconBase + "charity_event.png"
        },
        "Fundraiser": {
            icon: iconBase + "fundraiser.png"
        },
        "Looking to ride": {
            icon: iconBase + "looking_to_ride.png"
        }
    };

    //This is supposed to retrieve the list of POIs in JSON format so we can work with it to display them on the map.
    // See https://java.codeup.com/spring/extra-features/json-response/ for more info
    (function($) {
        var request = $.ajax({'url': '/events.json'});
        request.done(function (events) {
            events.forEach(function(thisEvent) {
                drawEvents(thisEvent, icons, infoWindow, map);
            });
        });
    })(jQuery);
}

//Geocoder, searches for input location and centers map on it
function geocodeAddress(geocoder, resultsMap, location) {
    geocoder.geocode({address: location}, (results, status) => {
        if (status === "OK") {
            resultsMap.setCenter(results[0].geometry.location);
        } else {
            alert(
                "Geocode was not successful for the following reason: " + status
            );
        }
    });
}

//Takes in an address and geocodes it, then sets the map to a zoom level of 9
function setLocation(geocoder, map, location) {
    if (location) {
        geocodeAddress(geocoder, map, location);
        map.setZoom(9);
    }
}

//Adds markers for the Events on the map and assigns their infowindow information
function drawEvents(thisEvent, icons, infoWindow, map) {
    //Setting up the proper latLng object notation so it can be read by Google Maps
    let coords = {
        'lat': Number(thisEvent.lat),
        'lng': Number(thisEvent.lon)
    };
    //Creates a marker and assigns some info to it
    let marker = new google.maps.Marker({
        position: coords,
        title: thisEvent.name,
        //Uses the base event Icon
        icon: "/images/icons/event.png"

    });
    //The line that actually attaches a marker to the map
    marker.setMap(map);

    //This connects the info window to the marker, allowing information, links, any HTML really to be displayed
    google.maps.event.addListener(marker, 'click', function() {
        infoWindow.setContent("<h6>" + thisEvent.name + "</h6>" +
            // "<p><strong>" + thisEvent + "</strong><br>" +
            thisEvent.description + "</p>" +
            "<a href='/event/" + thisEvent.id + "'>More Info</a>");
        infoWindow.open(map, marker);
    });
}