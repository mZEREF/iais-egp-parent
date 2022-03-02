<%@include file="../common/commonImport.jsp"%>
<%@ include file="content/msgContentDashboard.jsp" %>
<%@ include file="content/msgContent.jsp" %>
<script type="text/javascript">
    function submit(action) {
        $("[name='msg_view_type']").val(action);
        $("#msgContentForm").submit();
    }

    function cotToApp() {
        submit("toApp");
    }

    function cotToLic() {
        submit("toLic");
    }

    function cotToMsg() {
        submit('toMsg');
    }

</script>