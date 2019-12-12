<script type="text/javascript">
    window.onload = function(){
        clearErrorMsg();
        <%
        String errorMsg = (String) request.getAttribute("errorMsg");
        %>
        doValidationParse(<%=errorMsg%>);
        <%
        }
        %>
    }
</script>