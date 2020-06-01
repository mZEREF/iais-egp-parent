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



</script>