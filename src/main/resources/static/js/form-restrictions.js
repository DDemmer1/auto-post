$(document).ready(function () {

    //Add current date as min attribute in date-picker
    let today = new Date();
    var month = today.getMonth() + 1;
    if (today.getMonth() < 10) {
        month = "0" + month
    }

    let now = today.getFullYear() + "-" + month + "-" + today.getDate()
    $("#date").attr("min", now);


    //Validation of form. Triggered by submit
    $('#post-form').submit(function () {

        if($('#time').val()==""){
            alert("Please enter a time");
            return false;
        }

        //Validate time
        let currentDateTime = new Date();
        let formDateTime = new Date($("#date").val() + " " + $('#time').val() + ":00");
        if (currentDateTime > formDateTime) {
            alert(formDateTime.toLocaleDateString() + " " + formDateTime.toTimeString() + " is in the past. Please enter new Time!");
            return false;
        }

        if (testImage() == false) {
            alert("Image could not be loaded! Please check the Image URL!");
            return false;
        }

        var content = $("#content").val();
        var imageurl = $("#image").val();
        var upload0 = typeof $("#preview-0").attr('src') != 'undefined' ? $("#preview-0").attr('src') : "";
        var upload1 = typeof $("#preview-1").attr('src') != 'undefined' ? $("#preview-1").attr('src') : "";
        var upload2 = typeof $("#preview-2").attr('src') != 'undefined' ? $("#preview-2").attr('src') : "";
        var upload3 = typeof $("#preview-3").attr('src') != 'undefined' ? $("#preview-3").attr('src') : "";
        var upload4 = typeof $("#preview-4").attr('src') != 'undefined' ? $("#preview-4").attr('src') : "";
        var upload5 = typeof $("#preview-5").attr('src') != 'undefined' ? $("#preview-5").attr('src') : "";
        var upload6 = typeof $("#preview-6").attr('src') != 'undefined' ? $("#preview-6").attr('src') : "";
        var upload7 = typeof $("#preview-7").attr('src') != 'undefined' ? $("#preview-7").attr('src') : "";
        var upload8 = typeof $("#preview-8").attr('src') != 'undefined' ? $("#preview-8").attr('src') : "";
        var upload9 = typeof $("#preview-9").attr('src') != 'undefined' ? $("#preview-9").attr('src') : "";

        if ((content === "") && (upload0 === "") && (imageurl === "") && (upload1 === "") && (upload2 === "") && (upload3 === "") && (upload4 === "") && (upload5 === "") && (upload6 === "") && (upload7 === "") && (upload8 === "") && (upload9 === "")) {
            alert("No content or image to schedule");
            return false;
        }
        $(".se-pre-con").fadeIn("slow");
        return true;
    });
});


var max_file_size = 5000000;
var to_large_message = "File is too large. Only images up to " +max_file_size/1000000+"mb are allowed"

$("#file-0").on("change", function () {
    if (this.files[0].size > max_file_size) {
        alert(to_large_message);
        $(this).val('');
    }
});

$("#file-1").on("change", function () {
    if (this.files[0].size > max_file_size) {
        alert(to_large_message);
        $(this).val('');
    }
});

$("#file-2").on("change", function () {
    if (this.files[0].size > max_file_size) {
        alert(to_large_message);
        $(this).val('');
    }
});

$("#file-3").on("change", function () {
    if (this.files[0].size > max_file_size) {
        alert(to_large_message);
        $(this).val('');
    }
});
$("#file-4").on("change", function () {
    if (this.files[0].size > max_file_size) {
        alert(to_large_message);
        $(this).val('');
    }
});

$("#file-5").on("change", function () {
    if (this.files[0].size > max_file_size) {
        alert(to_large_message);
        $(this).val('');
    }
});
$("#file-6").on("change", function () {
    if (this.files[0].size > max_file_size) {
        alert(to_large_message);
        $(this).val('');
    }
});

$("#file-7").on("change", function () {
    if (this.files[0].size > max_file_size) {
        alert(to_large_message);
        $(this).val('');
    }
});

$("#file-8").on("change", function () {
    if (this.files[0].size > max_file_size) {
        alert(to_large_message);
        $(this).val('');
    }
});

$("#file-9").on("change", function () {
    if (this.files[0].size > max_file_size) {
        alert(to_large_message);
        $(this).val('');
    }
});


function testImage() {
    if ($("#img-preview").attr("src") == "") {
        return true;
    }
    return $("#img-preview")[0].complete && $("#img-preview")[0].naturalHeight !== 0;
}


