<script type="text/javascript">
    window.onload = function(){
        clearErrorMsg();
        <%
        String errorMsg = (String) request.getAttribute("errorMsg");
        if (!StringUtil.isEmpty(errorMsg)) {
        %>
        doValidationParse(<%=errorMsg%>);
        <%
        }
        %>
    }
</script>