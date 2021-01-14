let map, infoWindow, geocoder;
var userMarker;

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

    };

    //When the map is clicked, add a point and fill in the lat/lng values in html using jQuery
    google.maps.event.addListener(map, "click", function(event) {
        placeMarker(event.latLng);
        $('#lat').val(event.latLng.lat());
        $('#lon').val(event.latLng.lng());
    });

    //This is supposed to retrieve the list of POIs in JSON format so we can work with it to display them on the map.
    // See https://java.codeup.com/spring/extra-features/json-response/ for more info
    (function($) {
        var request = $.ajax({'url': '/points.json'});
        request.done(function (points) {
            points.forEach(function(point) {
                drawPOIs(point, icons, infoWindow, map);
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
        infoWindow.setContent("<h4>" + poi.name + "</h4>" +
                              "<p><strong>" + categoryString + "</strong><br>" +
                              "<a href='/points/" + poi.id + "'>More Info</a>");
        infoWindow.open(map, marker);
    });
}

//if marker already exists on map, move it. if not, create it at the location
function placeMarker(location) {
    if (userMarker) {
        //if marker already was created change positon
        userMarker.setPosition(location);
    } else {
        //create a marker
        userMarker = new google.maps.Marker({
            position: location,
            map: map,
            draggable: true
        });
    }
}