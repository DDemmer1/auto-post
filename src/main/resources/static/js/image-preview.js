$(document).ready(function() {

    $( "#image" ).change(function() {
        $("#img-preview").attr("src",$("#image" ).val());
        $("#img-preview-lightbox").attr("href",$("#image" ).val());
    });


    $("#add-image").click(function () {
        $("#div-image").slideToggle("slow");
        map.invalidateSize();
    });

});


