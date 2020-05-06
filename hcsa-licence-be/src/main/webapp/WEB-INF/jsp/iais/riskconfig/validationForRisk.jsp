<%@ page import="org.springframework.util.StringUtils" %>
<script type="text/javascript">
    window.onload = function(){
        clearErrorMsg();
        <%
        String errorMsg = (String) request.getAttribute("errorMsg");
        if (!StringUtils.isEmpty(errorMsg)) {
        %>
        doValidationParseForRisk('<%=errorMsg%>');
        <%
        }
        %>
    }

    function doValidationParseForRisk(data){
        if(data != null && data != "[]" && data != ''){
            $("#iaisErrorFlag").val("BLOCK");
            var results = JSON.parse(data);

            for(var i= 0 ; i< results.length ; i ++){
                for(var key in results[i]){
                    var error_key="error_" + key.replace(/\./g,'\\.');
                    if (document.getElementById(error_key)) {
                        $("#"+error_key).show();
                        if (error_key == 'error_topErrorDiv'
                            || error_key.indexOf('noEscapeXml') > 0) {
                            document.getElementById(error_key).innerHTML = results[i][key]+"<br/><br/>";
                        } else {
                            document.getElementById(error_key).innerHTML = formatHTMLEnCode(results[i][key])+"<br/><br/>";
                        }
                    }
                }
            }
        }
    }
</script>