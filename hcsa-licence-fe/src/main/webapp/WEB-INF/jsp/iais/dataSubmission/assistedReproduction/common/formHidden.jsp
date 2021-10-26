<%@ page import="com.ecquaria.cloud.moh.iais.dto.memorypage.PaginationHandler" %>
<%@ page import="com.ecquaria.cloud.moh.iais.common.utils.ParamUtil" %>
<%@ page import="java.util.Set" %>
<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%
    //handle to the Engine APIs
    sop.webflow.rt.api.BaseProcessClass proce =
            (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>
<input type="hidden" name="sopEngineTabRef" value="<%=proce.rtStatus.getTabRef()%>">
<input type="hidden" name="iaisErrorFlag" id="iaisErrorFlag"/>
<input type="hidden" name="crud_action_value" id="crud_action_value"/>
<input type="hidden" name="crud_action_additional" id="crud_action_additional"/>
<input type="hidden" id = "baseContextPath" value="<%=request.getContextPath()%>">
<input type="hidden" name="crud_type" id="crud_type"/>

<script type="text/javascript">

    <%if (ParamUtil.getRequestAttr(request, "memoryPagingLoading__Flag_Attr") != null) {%>
    $(document).ready(function() {
        <%
          Set<String> pageSet = (Set<String>) ParamUtil.getRequestAttr(request, "memoryPagingLoading__Flag_Attr");
          for (String divStr : pageSet) {
              PaginationHandler hand = (PaginationHandler) ParamUtil.getSessionAttr(request, divStr + "__SessionAttr");
              if (hand != null) {
        %>
        initMemoryPage('<%=divStr%>', '<%=hand.getCheckType()%>', <%=hand.getCurrentPageNo()%>);
        <%
              }
          }
        %>
    });
    <%}%>

</script>
