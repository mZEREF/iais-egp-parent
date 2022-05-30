<input type="hidden" id="counselling" value="${counselling}"/>

<script>
    $(document).ready(function() {
        $("#counsellingPlaceValue").blur(autoCompleteValue());
    });
    function autoCompleteValue(){
        var counselling =  $("#counselling").val();
        var availableTags = counselling.split("|");
        $("#counsellingPlaceValue").autocomplete({
            source: availableTags,autoFocus:true
        });
    }
</script>