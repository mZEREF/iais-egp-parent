<%@ page import="org.springframework.util.StringUtils" %>
<script type="text/javascript">
    window.onload = function(){
        clearErrorMsg();
        <%
        String errorMsg = (String) request.getAttribute("errorMsg");
        if (!StringUtils.isEmpty(errorMsg)) {
            if (errorMsg.indexOf("\n")!= -1){
                errorMsg = errorMsg.replace("\n", " ");
            }
        %>
        setTimeout(doValidationParse,100,'<%=errorMsg%>');
        <%
        }
        %>
    }
</script>