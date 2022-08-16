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

<style>
  body {
    padding-bottom: 0;
  }
</style>

<div id="svcDiv" class="panel-main-content" style="margin: 2%">
  <jsp:include page="/WEB-INF/jsp/iais/view/viewSvcInfo.jsp" />
</div>

<script type="text/javascript">
    $(document).ready(function(){
        var height = $(document).height();
        var iframe = $(parent.document.getElementById('${iframeId}'));
        iframe.css('height',height+10);
        iframe.prop('height',height+10);
        window.parent.dismissWaiting();
    });

</script>