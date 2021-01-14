let map, infoWindow, geocoder;
let thisEventLat = parseFloat($("#lat").val());
let thisEventLng = parseFloat($("#lng").val());
// let category = $("#category").text();


//Formatting the category correctly for the card
// var categoryString = category.replace(category.charAt(0), category.charAt(0).toUpperCase());
// if(categoryString.includes("_")) {
//     categoryString = categoryString.replace(
//         categoryString.charAt(categoryString.indexOf("_") + 1),
//         categoryString.charAt(categoryString.indexOf("_") + 1).toUpperCase());
//     categoryString = categoryString.replace("_", " ");
// }
// $("#category").text(categoryString);


function initMap() {
    //Taking the values of the lat and lng of the thisEvent we need, then centering the map on the thisEvent
    map = new google.maps.Map(document.getElementById("map"), {
        center: { lat: thisEventLat, lng: thisEventLng },
        zoom: 13,
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
        var request = $.ajax({'url': '/events.json'});
        request.done(function (thisEvents) {
            thisEvents.forEach(function(thisEvent) {
                drawEvents(thisEvent, icons, infoWindow, map);
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

//Adds markers for the thisEvents on the map and assigns their infowindow information
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
        //PLaceholder event icon
        icon: "/images/icons/event.png"
    });
    //The line that actually attaches a marker to the map
    marker.setMap(map);
    //Allowing the category to be correctly displayed in the infowindow by capitalizing the letters
    //and replacing any underscores with a space
    // var categoryString = poi.category.replace(poi.category.charAt(0), poi.category.charAt(0).toUpperCase());
    // if(categoryString.includes("_")) {
    //     categoryString = categoryString.replace(
    //         categoryString.charAt(categoryString.indexOf("_") + 1),
    //         categoryString.charAt(categoryString.indexOf("_") + 1).toUpperCase());
    //     categoryString = categoryString.replace("_", " ");
    // }
    //This connects the info window to the marker, allowing information, links, any HTML really to be displayed
    google.maps.event.addListener(marker, 'click', function() {
        infoWindow.setContent("<h4>" + thisEvent.name + "</h4>" +
            "<a href='/event/" + thisEvent.id + "'>More Info</a>");
        infoWindow.open(map, marker);
    });
}