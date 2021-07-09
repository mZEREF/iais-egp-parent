
<script type="text/javascript">
    function tinymce_updateCharCounter(el, len) {
        $('#' + el.id).prev().find('.char_count').text(len + '/' + el.settings.max_chars);
    }

    function removeHTMLTag(str) {
        str = str.replace(/<\/?[^>]*>/g, '');
        str = str.replace(/[\r\n]/g,"");
        return str;
    }


    function tinymce_getContentLength() {
        var count = removeHTMLTag(tinymce.get(tinymce.activeEditor.id).contentDocument.body.innerText).length;
        console.log(count);
        return count;
    }

</script>
