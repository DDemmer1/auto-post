jQuery(document).ready(function ($) {

    //Makes table row clickable
    $('.clickable-row').click(function () {
        window.location = $(this).data('href');
    });


    //Makes table row clickable
    $('.non-clickable').click(function () {
        window.location = $(this).data('href');
    });


    $(document).ready(function(){
        $("[data-toggle='tooltip']").tooltip();
    });


    //stops bubbeling of clickable row in checkbox
    $('.checkbox').click(function (event) {
        event.stopImmediatePropagation();
    });


    //Alert before account deletion
    $('.btn-delete-account').click(function (event) {
        let answer = confirm('Do you realy want to delete your account? This will remove all of your autoPost-data irreversible ')
        if(answer === true){
            window.location.href='/deleteaccount';
        }
    });

    //selects all posts
    $('#select-all').on('click',function(){
        if(this.checked){
            $('.checkbox').each(function(){
                this.checked = true;
            });
        }else{
            $('.checkbox').each(function(){
                this.checked = false;
            });
        }
    });

    //sets timezone
    var offset = new Date().getTimezoneOffset();
    $('#timezone').val(offset);

    //Lightbox preview for post images
    $(document).on('click', "[data-toggle='lightbox']", function (event) {
        event.preventDefault();
        $(this).ekkoLightbox();
    });


    //Emoji selector
    $(function () {
        window.emojiPicker = new EmojiPicker({
            emojiable_selector: '[data-emojiable=true]',
            assetsPath: '/emoji-picker/img/',
            popupButtonClasses: 'fa fa-smile-o'
        });
        window.emojiPicker.discover();
        //set preserve formatting on wysiwyg editor
        $('.emoji-wysiwyg-editor').addClass('text-input');
    });


    //activate tab toggle in post editor
    $('#imagetype a').on('click', function (e) {
        e.preventDefault()
        $(this).tab('show')
    });



    //Alert before account deletion
    $('.btn-delete-all').click(function (event) {
        let answer = confirm('Are you sure you want to delete all unposted posts? The Posts will be irreversibly deleted from the autoPost app.')
        if(answer === true){
            $.post(window.location.href.split('?')[0] + '/delete', function( data ) {
                location.reload();
            });
        }
    });

});


//Picture Upload widget
let currentImg = 0;
let map = new Map();
map.set(0,true);
map.set(1,true);
map.set(2,true);
map.set(3,true);
map.set(4,true);
map.set(5,true);
map.set(6,true);
map.set(7,true);
map.set(8,true);
map.set(9,true);


if(indexArray){
    indexArray.forEach(function(item, index, array) {
        map.set(item,false);
    });
}

$("#input-container").on('click',function (e) {
    console.log(currentImg);
    if(checkIfMapIsFull()){
            $("#file-" + currentImg).click();
        } else{
            console.log("Could not be posted. Map is full");
        }

});

function checkIfMapIsFull(){
    for (var index of map.keys()) {
        if(map.get(index) === true){
            currentImg = index;
            console.log("Found empty slot " + index);
            return true;
        }
    }
    console.log("Map is full");
    return false;
}

function deleteInput(input){
    let index = $(input).attr("index");
    map.set(parseInt(index),true);
    console.log(index);
    $("#preview-container-" + index).removeClass("d-inline-block");
    $("#preview-container-" + index).hide();
    $("#preview-" + index).attr("src","");
    console.log($("#file-" + index).val(""));
}

function readURL(input) {
    if (input.files && input.files[0]) {
        var reader = new FileReader();
        reader.onload = function (e) {
            $("#preview-" + currentImg).attr('src', e.target.result);
            $("#preview-container-" + currentImg).addClass("d-inline-block");
            $("#preview-container-" + currentImg).show();
            map.set(parseInt(currentImg),false);
            createNewInput();
        };
        reader.readAsDataURL(input.files[0]);
    }
}

function createNewInput() {
    checkIfMapIsFull();
    $("#all-previews").append("<div id=\"preview-container-"+ currentImg + "\" class=\"crop ml-1 mr-1\" style=\"display: none\">\n" +
        "                            <div index=\""+ currentImg +"\" class=\"close-picture-button\" onclick=\"deleteInput(this)\">\n" +
        "                                <i class=\"fa fa-times\" aria-hidden=\"true\"></i>\n" +
        "                            </div>" +
        "                           <img class=\"upload-preview\" id=\"preview-"+ currentImg +"\" src=\"\">\n" +
        "                        </div>");

    $("#upload").append("<input accept=\"image/*\" data-toggle=\"tooltip\"\n" +
        "                       onchange='readURL(this)'" +
        "                       title=\"Upload an image. Only files up to 2mb are allowed\" data-placement=\"bottom\"\n" +
        "                       class=\"btn btn-facebook text-white hidden-input\" type=\"file\" name=\"file-"+ currentImg +"\" id=\"file-"+ currentImg +"\">");
}



