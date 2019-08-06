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


    $('#new-image').on('click', function (e) {
        $('').append("");
    });


    $('#input-container').on('click',function (e) {
        $('#file').click();
    });


    $('#file').on('change', function (e) {
        readURL(this);
    });

    function readURL(input) {
        console.log(input);
        if (input.files && input.files[0]) {
            var reader = new FileReader();

            reader.onload = function (e) {
                $('#preview-container').show();
                $('#preview').attr('src', e.target.result);
            };

            reader.readAsDataURL(input.files[0]);
        }
    }


});