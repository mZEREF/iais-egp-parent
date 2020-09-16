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
            showWaiting();
            SOP.Crud.cfxSubmit(form, action, value);
        },

        markSubmit: function (form, action, paramNameId, paramValue) {
            showWaiting();
            $('#' + paramNameId).val(paramValue);
            SOP.Crud.cfxSubmit(form, action, paramValue);
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
            if ($(obj).attr("checked")){
                $('#' + id).attr("class", "btn btn-primary next");
            }else {
                $('#' + id).attr("class", "btn btn-primary disabled");
            }
        }
    }

    $("#crud_search_button").click(function () {
        var val = $("#crud_search_button").attr("value");
        if (val != null && val != ''){
            showWaiting();
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
        $(".form-horizontal input[type='checkbox']").removeAttr('checked');
        $(".form-horizontal input[type='radio']").removeAttr('checked');
        $(".form-horizontal .current").text("Please Select")
        $(".form-horizontal input").val("");
        $(".form-horizontal option:first").prop("selected", 'selected').val(null);
    })

    function jumpToPagechangePage(){
        SOP.Crud.cfxSubmit("mainForm", "changePage");
    }

    function sortRecords(sortFieldName,sortType){
        SOP.Crud.cfxSubmit("mainForm","sortRecords",sortFieldName,sortType);
    }

    $("#backLastPageId").click(function () {
        SOP.Crud.cfxSubmit("mainForm", "doBack");
    })



    function jsonToHtmlTable(str, id) {
        if (str == undefined || str == '' ){
            $("#" + id).html("<span> No Record! </span>")
            return
        }

        const strToObj = JSON.parse(str)
        let doStartTag = "<table width = \"50%\" border = \"1\">".concat("<thead>").concat("<tr>").concat("<th>Field</th>").concat("<th>Value</th>").concat("</tr>")
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
                console.log('error：'+ str + '!!!'+ e);
                return false;
            }
        }else if (typeof str == 'object'){
            return true
        }
    }


</script>