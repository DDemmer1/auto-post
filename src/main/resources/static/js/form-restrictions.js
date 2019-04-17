$(document).ready(function () {


    //Time validation
    let d = new Date();
    var month = d.getMonth() + 1;
    if (d.getMonth() < 10) {
        month = "0" + month
    }

    $("#date").attr("min", d.getFullYear() + "-" + month + "-" + d.getDate());

    $('#post-form').submit(function () {
        let d = new Date();
        let now = new Date("01.01.2000 " + d.getHours() + ":" + d.getMinutes()+":00");
        let formTime = new Date("01.01.2000 " + $('#time').val() +":00");
        console.log(now);

        if (now > formTime) {
            alert("Time is in the past. Please enter new Time!");
            return false;
        }


        //Content or picture != null validation


        if($('#image').val() == "" & $('#content').val() == ""){
            alert("Please enter content or picture!");
            return false;
        }





        return true;
    });
});