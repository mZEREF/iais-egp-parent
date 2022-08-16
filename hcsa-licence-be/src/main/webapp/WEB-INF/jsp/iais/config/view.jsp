<%@ page import="com.ecquaria.cloud.moh.iais.common.utils.StringUtil" %>
<%
 request.setAttribute("isView","true");
  String action  = (String)ParamUtil.getRequestAttr(request,IaisEGPConstant.CRUD_ACTION_TYPE);
  if(StringUtil.isEmpty(action)){
    action = ParamUtil.getString(request, IaisEGPConstant.CRUD_ACTION_TYPE);
  }
  String title = "Add HCSA Service";
  if("edit".equals(action)){
    request.setAttribute("isEidtAndView","true");
    title = "Preview HCSA Service Edit";
  }else if("delete".equals(action)){
    request.setAttribute("isDelete","true");
    title = "Delete HCSA Service";
  }
  request.setAttribute("title",title);
%>
<%@include file="addNewPage.jsp" %>

<script type="text/javascript">
$(document).ready(function () {
disableContent('div.readonly');
unDisableContent('div.canClick');
});
</script>