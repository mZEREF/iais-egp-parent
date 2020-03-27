<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%
  //handle to the Engine APIs
  sop.webflow.rt.api.BaseProcessClass process =
          (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>
<%--<webui:setLayout name="egp-blank"/>--%>
<webui:setLayout name="iais-blank"/>
<%--<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>--%>

<div class="panel-main-content" style="margin: 2%">

  <%@include file="../../common/previewSvcDisciplines.jsp"%>
  <%@include file="../../common/previewSvcGovernanceOfficer.jsp"%>
  <%@include file="../../common/previewSvcAllocation.jsp"%>
  <%@include file="../../common/previewSvcPrincipalOfficers.jsp"%>
  <%@include file="../../common/previewSvcDocument.jsp"%>

</div>

<script type="text/javascript">
    $(document).ready(function(){


    });

</script>