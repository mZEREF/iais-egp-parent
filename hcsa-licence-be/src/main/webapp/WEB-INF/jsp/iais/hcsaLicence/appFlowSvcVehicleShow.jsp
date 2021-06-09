<%--
  Created by IntelliJ IDEA.
  User: ShiCheng_Xu
  Date: 2021/6/9
  Time: 15:14
  To change this template use File | Settings | File Templates.
--%>
<c:if test="${'APTY005' eq applicationViewDto.applicationDto.applicationType}">
  <c:set var="appFlowSvcVehicleDtos" value="${applicationViewDto.vehicleRfcShowDtos}"/>
</c:if>
<c:if test="${'APTY005' ne applicationViewDto.applicationDto.applicationType}">
  <c:set var="appFlowSvcVehicleDtos" value="${applicationViewDto.appSvcVehicleDtos}"/>
</c:if>
<c:if test="${'edit' eq appVehicleFlag}">
  <iais:field value="Vehicle Recommendations" required="true"/>
  <iais:value width="7">
    <div class="table-gp" id = "processRecRfi">
      <table class="table">
        <thead>
        <tr align="center">
          <th width="5%">S/N</th>
          <th width="30%">Vehicle</th>
          <th width="30%">Recommendation</th>
          <th width="35%">Remarks</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="appVehicleNo" items="${appFlowSvcVehicleDtos}" varStatus="status">
          <tr>
            <td><c:out value="${varStatus.count}"></c:out></td>
            <td><c:out value="${appVehicleNo.vehicleName}"/></td>
            <td>
              <input class="form-check-input" type="radio" name="vehicleNoRadio${varStatus.index}" value = "approve" aria-invalid="false" <c:if test="${'VEST002' eq appVehicleNo.status}">checked="checked"</c:if>/>
              <label class="form-check-label"><span class="check-circle"></span>Approve</label>
              &nbsp;
              <input class="form-check-input" type="radio" name="vehicleNoRadio${varStatus.index}" value = "reject" aria-invalid="false" <c:if test="${'VEST003' eq appVehicleNo.status}">checked="checked"</c:if>/>
              <label class="form-check-label"><span class="check-circle"></span>Reject</label>
              <br><span class="error-msg" name="iaisErrorMsg" id="error_vehicleNoRadioError${varStatus.index}"></span>
            </td>
            <td>
              <input type="text" name="vehicleNoRemarks${varStatus.count}" maxlength="400" value="${appVehicleNo.remarks}" />
              <br><span class="error-msg" name="iaisErrorMsg" id="error_vehicleNoRemarksError${varStatus.index}"></span>
            </td>
          </tr>
        </c:forEach>
        </tbody>
      </table>
      <a style="float:left;padding-top: 1.1%;" class="back" href="/main-web/eservice/INTRANET/MohBackendInbox?fromOther=1"><em class="fa fa-angle-left"></em> Back</a>
    </div>
  </iais:value>
</c:if>
<c:if test="${'view' eq appVehicleFlag}">
  <iais:field value="Vehicle Recommendations"/>
  <iais:value width="7">
    <div class="table-gp" id = "processRecRfi">
      <table class="table">
        <thead>
        <tr align="center">
          <th width="5%">S/N</th>
          <th width="30%">Vehicle</th>
          <th width="30%">Recommendation</th>
          <th width="35%">Remarks</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="appVehicleNo" items="${appFlowSvcVehicleDtos}" varStatus="status">
          <tr>
            <td><c:out value="${varStatus.count}"></c:out></td>
            <td><c:out value="${appVehicleNo.vehicleName}"/></td>
            <td>
              <c:choose>
                <c:when test="${'VEST002' eq appVehicleNo.status}">
                  <c:out value="Approve"/>
                </c:when>
                <c:when test="${'VEST003' eq appVehicleNo.status}">
                  <c:out value="Reject"/>
                </c:when>
                <c:otherwise>
                  <c:out value="N/A"/>
                </c:otherwise>
              </c:choose>
            </td>
            <td><span style="font-size: 16px"><c:out value="${appVehicleNo.remarks}"/></span></td>
          </tr>
        </c:forEach>
        </tbody>
      </table>
      <a style="float:left;padding-top: 1.1%;" class="back" href="/main-web/eservice/INTRANET/MohBackendInbox?fromOther=1"><em class="fa fa-angle-left"></em> Back</a>
    </div>
  </iais:value>
</c:if>