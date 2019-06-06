$(document).ready(function() {

    $( "#image" ).change(function() {
        $("#img-preview").attr("src",$("#image" ).val());
        $("#file").val("");
        $("#img-preview-lightbox").attr("href",$("#image" ).val());
    });


    $("#add-image").click(function () {
        $("#div-image").slideToggle("slow");
        map.invalidateSize();
    });

    $( "#file" ).change(function() {
        $("#image").val("");
        $("#img-preview").attr("src","https://www.splunk.com/content/dam/splunk2/images/resources/spl/placeholder.jpg");
        $("#img-preview-lightbox").attr("href","https://www.splunk.com/content/dam/splunk2/images/resources/spl/placeholder.jpg");
    });

});


