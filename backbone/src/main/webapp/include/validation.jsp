<%@ page import="org.springframework.util.StringUtils" %>
<script type="text/javascript">
    window.onload = function(){
        clearErrorMsg();
        <%
        String errorMsg = (String) request.getAttribute("errorMsg");
        if (!StringUtils.isEmpty(errorMsg)) {
        %>
        doValidationParse(<%=errorMsg%>);
        <%
        }
        %>
    }
</script>