<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib prefix="iasi" uri="ecquaria/sop/egov-mc" %>
<webui:setLayout name="iais-internet"/>

<%
  sop.webflow.rt.api.BaseProcessClass process =
          (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>

  <div class="main-content">
  <form id="mainForm" method="post" action=<%=process.runtime.continueURL()%>>
    <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
    <input type="hidden" name="crud_action_type" value="">
    <input type="hidden" name="crud_action_value" value="">
    <input type="hidden" name="crud_action_additional" value="">
    <div style="margin-top: 15px">
      <h1>Acknowledgement</h1>
      <br><br>
    </div>
    <div class="form-group">
      <div class="col-xs-12 col-md-10">
        Your appeal application has been successfully submitted, the application number is
          <strong style="text-decoration: underline">${newApplicationNo}</strong>
        <br><br><br>
      </div>

    </div>
    <div class="table-gp">
      <table class="table" border="1px">
      <tr>
        <td style="text-align: center">Application No.</td>
        <td style="text-align: center">Service Name</td>
        <td style="text-align: center">HCI Name</td>
        <td style="text-align: center">HCI Address</td>
      </tr>
        <c:forEach items="${hciNames}" var="hciName">
        <tr>
          <td style="text-align: center">${applicationNo}</td>
          <td style="text-align: center">${serviceName}</td>
          <td style="text-align: center">${hciName}</td>
          <td style="text-align: center">${hciAddress}</td>
        </tr>
        </c:forEach>
      </table>


    </div>
  </form>
</div>
<script>



</script>

</>
