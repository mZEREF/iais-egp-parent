<script type="text/javascript">
    $(function () {
        clearAllErrMsg();
        <%String errorMsg = (String) request.getAttribute("errorMsg");%>
        parseAndShowErrorMsg('<%=errorMsg%>');
        $("#bata").click();
    });
</script>