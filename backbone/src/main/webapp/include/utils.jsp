<%--
  Created by IntelliJ IDEA.
  User: yichen
  Date: 2/15/2020
  Time: 1:33 PM
  To change this template use File | Settings | File Templates.
--%>

<script>
    var Utils = {
        getFileName: function(o){
            var pos = o.lastIndexOf("\\");
            return o.substring(pos + 1);
        },

        submit: function (form, action, value) {
            SOP.Crud.cfxSubmit(form, action, value);
        },

        clearClickStatus: function () {
            $(".form-horizontal input").val("");
            $(".form-horizontal input[type='checkbox']").removeAttr('checked');
            $(".form-horizontal input[type='radio']").removeAttr('checked');
            $(".form-horizontal option[text = 'Please Select']").val("selected", "selected");
            $(".current").text("Please Select");
            $(".form-horizontal option").val("");
        },
    }

    $("#crud_search_button").click(function () {
        var val = $("#crud_search_button").attr("value");
        if (val != null && val != ''){
            SOP.Crud.cfxSubmit("mainForm", val);
        }
    })

    $("#crud_cancel_link").click(function () {
        var val = $("#crud_cancel_link").attr("value");
        if (val != null && val != ''){
            SOP.Crud.cfxSubmit("mainForm", val);
        }
    })

    $("#crud_clear_button").click(function () {
        $(".form-horizontal input").val("");
        $(".form-horizontal input[type='checkbox']").removeAttr('checked');
        $(".form-horizontal input[type='radio']").removeAttr('checked');
        $(".form-horizontal option[text = 'Please Select']").val("selected", "selected");

        $(".form-horizontal .current").text("Please Select")
        $(".form-horizontal option").val("");
    })

    function jumpToPagechangePage(){
        SOP.Crud.cfxSubmit("mainForm", "changePage");
    }

    function sortRecords(sortFieldName,sortType){
        SOP.Crud.cfxSubmit("mainForm","sortRecords",sortFieldName,sortType);
    }
</script>