<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib prefix="iasi" uri="ecquaria/sop/egov-mc" %>
<webui:setLayout name="iais-internet"/>

<%
  sop.webflow.rt.api.BaseProcessClass process =
          (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<%@include file="./dashboard.jsp" %>
  <div class="container" >
  <form id="mainForm" method="post" style="margin-left: 3%" action=<%=process.runtime.continueURL()%>>
    <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
    <input type="hidden" name="crud_action_type" value="">
    <input type="hidden" name="crud_action_value" value="">
    <input type="hidden" name="crud_action_additional" value="">
    <div class="row" style="margin-left:5px">
      <div style="margin-top: 15px">
        <h1>Acknowledgement</h1>
        <br><br>
      </div>
      <div class="form-group">
        <div class="col-xs-12 col-md-10" style="margin-left: -15px">
          ${substring}
          <strong style="text-decoration: underline">${newApplicationNo}</strong>
          <br><br><br>
        </div>

      </div>
      <div class="table-gp">
        <table aria-describedby="" class="table" border="1px" >
          <tr>
            <c:if test="${type=='licence'}"> <th scope="col" style="text-align: center">Licence No.</th></c:if>
            <c:if test="${type=='application'}"> <th scope="col" style="text-align: center">Application No.</th></c:if>
            <th scope="col" style="text-align: center">Service Name</th>
            <th scope="col" style="text-align: center">HCI Name</th>
            <th scope="col" style="text-align: center">HCI Address</th>
          </tr>
          <c:choose>
            <c:when test="${not empty hciNames}">
              <c:forEach items="${hciNames}" var="hciName" varStatus="stauts">
                <tr>
                  <c:if test="${stauts.first}">
                    <c:if test="${type=='application'}"><td style="text-align: center" rowspan="${hciNames.size()}">${applicationNo}</td>  </c:if>
                    <c:if test="${type=='licence'}"><td style="text-align: center" rowspan="${hciNames.size()}">${licenceNo}</td></c:if>
                    <td style="text-align: center" rowspan="${hciNames.size()}">${serviceName}</td>
                  </c:if>
                  <td style="text-align: center">${hciName}</td>
                  <td style="text-align: center">${hciAddress[stauts.index]}</td>
                </tr>
              </c:forEach>
            </c:when>
            <c:when test="${empty hciNames}">
              <c:if test="${type=='application'}"><td style="text-align: center">${applicationNo}</td>  </c:if>
              <c:if test="${type=='licence'}"><td style="text-align: center">${licenceNo}</td></c:if>
              <td style="text-align: center">${serviceName}</td>
              <td style="text-align: center"></td>
              <td style="text-align: center"></td>
            </c:when>
          </c:choose>


        </table>


      </div>
    </div>
  </form>
</div>
<style>
  .table-gp table.table > tbody > tr > td {
    padding: 15px 25px 15px 0;
    border: 1px solid #000000;
    vertical-align: top;
  }
</style>
<script>

    $('#cancel').click(function () {

        SOP.Crud.cfxSubmit("mainForm", "cancel","cancel","");

    });

</script>

</>
