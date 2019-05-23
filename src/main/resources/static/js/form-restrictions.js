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


        return true;
    });
});


function testImage() {
    if($("#img-preview").attr("src") == ""){
        return true;
    }
    return $("#img-preview")[0].complete && $("#img-preview")[0].naturalHeight !== 0;
}


