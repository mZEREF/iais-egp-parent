
<%
 request.setAttribute("isView","true");
%>
<%@include file="addNewPage.jsp" %>

<script type="text/javascript">
$(document).ready(function () {
disableContent('div.readonly');
unDisableContent('div.canClick');
});
</script>