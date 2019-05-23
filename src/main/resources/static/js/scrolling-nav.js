jQuery(document).ready(function ($) {

    //Makes table row clickable
    $(".clickable-row").click(function () {
        window.location = $(this).data("href");
    });

    //stops bubbeling of clickable row in checkbox
    $(".checkbox").click(function (event) {
        event.stopImmediatePropagation();
    });


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

    //Lightbox preview for post images
    $(document).on('click', '[data-toggle="lightbox"]', function (event) {
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
    });


});