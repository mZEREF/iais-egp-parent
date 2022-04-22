<script type="text/javascript">
    $(function () {
        <%String requestExtension = (String) request.getAttribute("requestExtension");%>
        judgeDisplay('<%=requestExtension%>');
    });
</script>