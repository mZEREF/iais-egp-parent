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

        submit: function (formName, action, value, additional, formValue) {
            showWaiting();
            var inputs = document.getElementById(formName).getElementsByTagName("input");
            if(inputs.length != 0){
                for (var i = 0; i < inputs.length; i++) {
                    if('crud_action_type' == inputs[i].name) {
                        inputs[i].value = action;
                    } else if('crud_action_value' == inputs[i].name) {
                        inputs[i].value = value;
                    } else if('crud_action_additional' == inputs[i].name) {
                        inputs[i].value = additional;
                    } else if('crud_action_type_form_value' == inputs[i].name) {
                        inputs[i].value = formValue;
                    }
                }
            }
            document.getElementById(formName).submit();
        },

        markSubmit: function (form, action, paramNameId, paramValue) {
            showWaiting();
            $('#' + paramNameId).val(paramValue);

            document.getElementById('crud_action_type').value = action;
            document.getElementById('crud_action_value').value = paramValue;
            document.getElementById(form).submit();
        },

        clearClickStatus: function (divName) {
            $("."+ divName +" input").val("");
            $("."+ divName +" input").attr("disabled", null);
            $("."+ divName +" input[type='checkbox']").removeAttr('checked');
            $("."+ divName +" input[type='radio']").removeAttr('checked');
            $("."+ divName +" option[text = 'Please Select']").val("selected", "selected");
            $(".current").text("Please Select");
            $("."+ divName +" option").val("");
        },

        disableButton: function (obj, id) {
            if ($(obj).prop("checked")){
                $('#' + id).attr("class", "btn btn-primary next");
            }else {
                $('#' + id).attr("class", "btn btn-primary disabled");
            }
        },
    }

    $("#crud_search_button").click(function () {
        var val = $("#crud_search_button").attr("value");
        if (val != null && val != ''){
            showWaiting();
            document.getElementById('crud_action_type').value = val;
            document.getElementById("mainForm").submit();
        }
    })

    $("#crud_cancel_link").click(function () {
        var val = $("#crud_cancel_link").attr("value");
        if (val != null && val != ''){
            document.getElementById('crud_action_type').value = val;
            document.getElementById("mainForm").submit();
        }
    })

    $("#crud_clear_button").click(function () {
        $(".form-horizontal input[type='checkbox']").prop('checked', false);
        $(".form-horizontal input[type='radio']").prop('checked', false);
        $(".form-horizontal .current").text("Please Select")
        $(".form-horizontal input").val("");
        $(".form-horizontal select").each(function(index, ele) {
            ele.selectedIndex = 0;
            $(ele).niceSelect("update");
        });
        $(".error-msg").text("");
    })

    function jumpToPagechangePage(){
        document.getElementById('crud_action_type').value = 'changePage';
        document.getElementById("mainForm").submit();
    }

    function sortRecords(sortFieldName,sortType){
        document.getElementById('crud_action_type').value = 'sortRecords';
        document.getElementById('crud_action_value').value = sortFieldName;
        document.getElementById('crud_action_additional').value = sortType;
        document.getElementById("mainForm").submit();
    }

    $("#backLastPageId").click(function () {
        document.getElementById('crud_action_type').value = 'doBack';
        document.getElementById("mainForm").submit();
    })

    function jsonToHtmlTable(str, id) {
        if (str == undefined || str == '' ){
            $("#" + id).html("<span> No record found. </span>")
            return
        }

        const strToObj = JSON.parse(str)
        let doStartTag = "<table width = \"50%\" border = \"1\">".concat("<thead>").concat("<tr>").concat("<th  >Field</th>").concat("<th  >Value</th>").concat("</tr>")
        let doEndTag =  "</thead></table>"
        let jsonToHtmlTable = doStartTag.concat(buildHtmlTable(strToObj)).concat(doEndTag)
        $("#" + id).html(jsonToHtmlTable)
    }

    function buildHtmlTable(strToObj) {
        let result = "<tr>";
        $.each(strToObj, function(i) {
            let fieldName = i
            let value = strToObj[i]
            if (isJSON(value)){
                let subJson = JSON.parse(value)
                let subStartTag = result.concat("<tr><td>").concat(fieldName).concat("</td>").concat("<td>").concat('<table width="100%" border="1">').concat("<thead>").concat("<tr></tr>")
                let subEndTag = ("</thead>").concat("</table>").concat("</td>").concat("</tr>")
                result = subStartTag.concat(buildHtmlTable(subJson)).concat(subEndTag)
            }else {
                result = result.concat("<tr><td>").concat(fieldName).concat("</td>").concat("<td>").concat(value).concat("</td>").concat("</tr>")
            }
        })
        result = result.concat("</tr>")
        return result
    }

    function isJSON(str) {
        if (typeof str == 'string') {
            try {
                let obj = JSON.parse(str);
                if(typeof obj == 'object' && obj ){
                    return true;
                }else{
                    return false;
                }

            } catch(e) {
                console.log('error：'+ e);
                return false;
            }
        }else if (typeof str == 'object'){
            return true
        }
    }



</script>