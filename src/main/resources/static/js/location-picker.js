function addMapPicker() {
    var mapCenter = [50.941357, 6.958307 ];
    var map = L.map('map', {center : mapCenter, zoom : 9});
    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
        maxZoom: 18,
        attribution: 'Map data © <a href="https://openstreetmap.org">OpenStreetMap</a> contributors'
    }).addTo(map);
    var marker = L.marker(mapCenter).addTo(map);
    function updateMarker(lat, lng) {
        marker
            .setLatLng([lat, lng])
            .bindPopup(marker.getLatLng().lat.toFixed(2) + "/" + marker.getLatLng().lng.toFixed(2))
            .openPopup();
        return false;
    };

    map.on('click', function(e) {
        $('#latInput').val(e.latlng.lat);
        $('#lngInput').val(e.latlng.lng);
        updateMarker(e.latlng.lat, e.latlng.lng);
    });



    $("#find-geo").click(function () {
        if ("geolocation" in navigator){
            navigator.geolocation.getCurrentPosition(function(position){
                // alert("Found your location <br />Lat : "+position.coords.latitude+" </br>Lang :"+ position.coords.longitude);
                $("#lngInput").val(position.coords.longitude);
                $("#latInput").val(position.coords.latitude);
                updateMarker(position.coords.latitude,position.coords.longitude);
            });
        }else{
            alert("Browser doesn't support geolocation tracking!");
        }
    });


    var updateMarkerByInputs = function() {
        return updateMarker( $('#latInput').val() , $('#lngInput').val());
    }
    $('#latInput').on('input', updateMarkerByInputs);
    $('#lngInput').on('input', updateMarkerByInputs);

    $("#add-location").click(function () {
        $("#div-location").slideToggle("slow");
        map.invalidateSize();
    });


}

$(document).ready(function() {
    addMapPicker();
});