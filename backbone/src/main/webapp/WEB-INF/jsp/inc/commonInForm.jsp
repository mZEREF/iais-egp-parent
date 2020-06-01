<%@ page import="com.ecquaria.cloud.moh.iais.common.utils.ParamUtil" %>
<%@ page import="com.ecquaria.cloud.moh.iais.dto.memorypage.PaginationHandler" %>
<%@ page import="java.util.Set" %>
<%
  //handle to the Engine APIs
  sop.webflow.rt.api.BaseProcessClass process =
          (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>
<input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
<input type="hidden" id = "premisesTypeValue" value="${appGrpPremisesDto.premisesType}">
<input type="hidden" id = "baseContextPath" value="<%=request.getContextPath()%>">

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