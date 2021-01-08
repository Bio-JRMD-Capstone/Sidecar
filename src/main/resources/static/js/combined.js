let map, infoWindow, geocoder;

function initMap() {
    map = new google.maps.Map(document.getElementById("map"), {
        center: { lat: 29.4241, lng: -98.4936 },
        zoom: 10,
    });
    geocoder = new google.maps.Geocoder();
    document.getElementById("submit").addEventListener("click", () => {
        geocodeAddress(geocoder, map);
    });
    infoWindow = new google.maps.InfoWindow();

    //To save on typing, I save the relative filepath as a variable since we will be using it a lot just below
    const iconBase = "/css/images/";

    //This array of icons is referenced when drawing a poi. It uses a string (the poi category)
    // to find the appropriate filepath for that category's icon.
    const icons = {
        "bar": {
            icon: iconBase + "bar.png"
        },
        "restaurant": {
            icon: iconBase + "restaurant.png"
        },
        "scenic_view": {
            icon: iconBase + "scenic_view.png"
        },
        "motorcycle_shop": {
            icon: iconBase + "motorcycle_shop.png"
        },
        "repair_shop": {
            icon: iconBase + "repair_shop.png"
        },
        "other": {
            icon: iconBase + "other.png"
        }
    }

    //Pan to current location button
    const locationButton = document.createElement("button");
    locationButton.textContent = "Pan to Current Location";
    locationButton.classList.add("custom-map-control-button");
    map.controls[google.maps.ControlPosition.TOP_RIGHT].push(locationButton);
    locationButton.addEventListener("click", () => {
        // Try HTML5 geolocation.
        if (navigator.geolocation) {
            navigator.geolocation.getCurrentPosition(
                (position) => {
                    const pos = {
                        lat: position.coords.latitude,
                        lng: position.coords.longitude,
                    };
                    infoWindow.setPosition(pos);
                    infoWindow.setContent("Location found.");
                    infoWindow.open(map);
                    map.setCenter(pos);
                },
                () => {
                    handleLocationError(true, infoWindow, map.getCenter());
                }
            );
        } else {
            // Browser doesn't support Geolocation
            handleLocationError(false, infoWindow, map.getCenter());
        }
    });

    //This is supposed to retrieve the list of POIs in JSON format so we can work with it to display them on the map.
    // See https://java.codeup.com/spring/extra-features/json-response/ for more info
    (function($) {
        var request = $.ajax({'url': '/points.json'});
        request.done(function (points) {
            points.forEach(function(point) {
                drawPOIs(point, icons, infoWindow, map);
                console.log(request)
            });
        });
        var requestRoute = $.ajax({'url': '/routes.json'});
        requestRoute.done(function (routes) {
            routes.forEach(function (route) {
                drawRoutes(route, infoWindow, map);
            });
        });
    })(jQuery);

}

//If GeoLocation fails
function handleLocationError(browserHasGeolocation, infoWindow, pos) {
    infoWindow.setPosition(pos);
    infoWindow.setContent(
        browserHasGeolocation
            ? "Error: The Geolocation service failed."
            : "Error: Your browser doesn't support geolocation."
    );
    infoWindow.open(map);
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

//Adds markers for the POIs on the map and assigns their infowindow information
function drawPOIs(poi, icons, infoWindow, map) {
    //Setting up the proper latLng object notation so it can be read by Google Maps
    let coords = {
        'lat': Number(poi.lat),
        'lng': Number(poi.lon)
    };
    //Creates a marker and assigns some info to it
    let marker = new google.maps.Marker({
        position: coords,
        title: poi.name,
        //Looks at the poi type and references the icon array to determine what icon it uses
        icon: icons[poi.category].icon
    });
    //The line that actually attaches a marker to the map
    marker.setMap(map);
    //Allowing the category to be correctly displayed in the infowindow by capitalizing the letters
    //and replacing any underscores with a space
    var categoryString = poi.category.replace(poi.category.charAt(0), poi.category.charAt(0).toUpperCase());
    if(categoryString.includes("_")) {
        categoryString = categoryString.replace(
            categoryString.charAt(categoryString.indexOf("_") + 1),
            categoryString.charAt(categoryString.indexOf("_") + 1).toUpperCase());
        categoryString = categoryString.replace("_", " ");
    }
    //This connects the info window to the marker, allowing information, links, any HTML really to be displayed
    google.maps.event.addListener(marker, 'click', function() {
        infoWindow.setContent("<h6>" + poi.name + "</h6>" +
            "<p><strong>" + categoryString + "</strong><br>" +
            poi.description + "</p>" +
            "<a href='/points/" + poi.id + "'>More Info</a>");
        infoWindow.open(map, marker);
    });
}

//Adds markers for the POIs on the map and assigns their infowindow information
function drawRoutes(route, infoWindow, map) {
    //Setting up the proper latLng object notation so it can be read by Google Maps
    let splitCoords = route.coordinates.split("},{")
    console.log(splitCoords[0])

    let repMark = splitCoords[0].replace("{location: {lat: ", "")

    console.log(repMark)

    repMark = repMark.replace("lng: ", "")

    console.log(repMark)


    repMark = repMark.replace(" }", "")

    console.log(repMark)

    repMark = repMark.split(",")
    console.log(repMark)

    let singleCoords = splitCoords[0]

    let coords = {
        'lat': parseFloat(repMark[0]),
        'lng': parseFloat(repMark[1])
    };

    console.log(coords)

    //Creates a marker and assigns some info to it
    let marker = new google.maps.Marker({
        position: coords,
        title: route.title,

    });
    //The line that actually attaches a marker to the map
    marker.setMap(map);

    //This connects the info window to the marker, allowing information, links, any HTML really to be displayed
    google.maps.event.addListener(marker, 'click', function() {
        infoWindow.setContent("<h4>" + route.title + "</h4>" +
            "</p>" + "Distance" + "</p>" +
            "</p>" + route.distance + "</p>" +
            "<a href='/route/" + route.id + "'>View Route</a>");
        infoWindow.open(map, marker);
    });
}