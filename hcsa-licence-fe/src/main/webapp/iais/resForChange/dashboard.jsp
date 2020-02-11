

<script type="text/javascript">
  
    function doSubmitForm(action,value,additional){
        $("[name='crud_action_type_form_value']").val(action);
        

        var mainForm = document.getElementById('menuListForm');
        mainForm.submit();
    }

    function doBack(action ) {
        $("[name='crud_action_type_value']").val('back');
        $("[name='crud_action_type_form_value']").val(action);
        var mainForm = document.getElementById('menuListForm');
        mainForm.submit();

    }
  
  
  


</script>


