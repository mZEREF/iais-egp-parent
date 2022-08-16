
<%
  request.setAttribute("isEdit","true");
  String title = "Edit HCSA Service";
  request.setAttribute("title",title);
%>
<%@include file="addNewPage.jsp" %>

<script type="text/javascript">
    $(document).ready(function () {
        disableContent('div.editReadonly');

    });
</script>

