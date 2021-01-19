<script>
    var submit = function (action,value,additional) {
        $('input[name="crud_action_type"]').val(action);
        $('input[name="crud_action_type_form"]').val(value);
        $("input[name='crud_action_additional']").val(additional);
        $('#mainForm').submit();
    }
</script>