<%--
  Created by IntelliJ IDEA.
  User: yichen
  Date: 2/11/2020
  Time: 11:53 PM
  To change this template use File | Settings | File Templates.
--%>




<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://www.ecq.com/iais"   prefix="iais"%>
<webui:setLayout name="iais-intranet"/>

<%
  sop.webflow.rt.api.BaseProcessClass process =
          (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>



<webui:setLayout name="iais-intranet"/>


<div class="main-content">
  <form id="mainForm" method="post" action=<%=process.runtime.continueURL()%>>
    <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>
    <input type="hidden" name="currentValidateId" value="">
    <div class="bg-title"><h2>Acknowledgement</h2></div>

    <p><c:out value="${ackMsg}"></c:out></p>

    <div class="text-right text-center-mobile">
      <a class="btn btn-primary next" href="javascript:void(0);" onclick="javascript:doCancel();">Done</a>
    </div>
  </form>
</div>


<script type="text/javascript">
    function doCancel(){
        SOP.Crud.cfxSubmit("mainForm","doCancel");
    }

</script>






