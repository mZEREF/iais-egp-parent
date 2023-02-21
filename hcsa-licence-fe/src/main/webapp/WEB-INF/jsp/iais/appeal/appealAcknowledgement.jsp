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
      <div class="main-content">
        <div class="container">
          <br/>
          <div class="row">
            <div class="col-lg-12 col-xs-12 cesform-box">
              <div class="table-responsive">
                <table aria-describedby="" class="table">
                  <thead>
                  <tr>
                    <c:if test="${type=='licence'}"> <th scope="col" style="text-align: center;border: 1px solid #dee2e6;">Licence No.</th></c:if>
                    <c:if test="${type=='application'}"> <th scope="col" style="text-align: center;border: 1px solid #dee2e6;">Application No.</th></c:if>
                    <th scope="col" style="text-align: center;border: 1px solid #dee2e6;">Service Name</th>
                    <th scope="col" style="text-align: center;border: 1px solid #dee2e6;">HCI Name</th>
                    <th scope="col" style="text-align: center;border: 1px solid #dee2e6;">HCI Address</th>
                  </tr>
                  </thead>
                  <tbody>
                  <c:choose>
                    <c:when test="${not empty hciNames}">
                      <c:forEach items="${hciNames}" var="hciName" varStatus="stauts">
                        <tr >
                          <c:if test="${stauts.first}">
                            <c:if test="${type=='application'}"><td style="text-align: center;border: 1px solid #dee2e6;" rowspan="${hciNames.size()}">${applicationNo}</td>  </c:if>
                            <c:if test="${type=='licence'}"><td style="text-align: center;border: 1px solid #dee2e6;" rowspan="${hciNames.size()}">${licenceNo}</td></c:if>
                            <td style="text-align: center;border: 1px solid #dee2e6;" rowspan="${hciNames.size()}">${serviceName}</td>
                          </c:if>
                          <td style="text-align: center;border: 1px solid #dee2e6;">${hciName}</td>
                          <td style="text-align: center;border: 1px solid #dee2e6;">${hciAddress[stauts.index]}</td>
                        </tr>
                      </c:forEach>
                    </c:when>
                    <c:when test="${empty hciNames}">
                      <tr >
                        <c:if test="${type=='application'}"><td style="text-align: center;border: 1px solid #dee2e6;">${applicationNo}</td>  </c:if>
                        <c:if test="${type=='licence'}"><td style="text-align: center;border: 1px solid #dee2e6;">${licenceNo}</td></c:if>
                        <td style="text-align: center;border: 1px solid #dee2e6;">${serviceName}</td>
                        <td style="text-align: center;border: 1px solid #dee2e6;"></td>
                        <td style="text-align: center;border: 1px solid #dee2e6;"></td>
                      </tr>
                    </c:when>
                  </c:choose>
                  </tbody>
                </table>
              </div>
            </div>
          </div>
          <br/>
        </div>
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
