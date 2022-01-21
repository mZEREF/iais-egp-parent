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
<c:if test="${not empty appFlowSvcVehicleDtos}">
  <c:if test="${'edit' eq appVehicleFlag}">
    <iais:row>
      <iais:field value="Vehicle Recommendations" required="true"/>
      <iais:value width="7">
        <div class="table-gp" id = "processRecRfi">
          <table aria-describedby="" class="table">
            <thead>
              <tr>
                <th scope="col" width="5%">S/N</th>
                <th scope="col" width="25%">Vehicle</th>
                <th scope="col" width="35%">Recommendation</th>
                <th scope="col" width="35%">Remarks</th>
              </tr>
            </thead>
            <tbody>
            <c:forEach var="appVehicleNo" items="${appFlowSvcVehicleDtos}" varStatus="status">
              <tr>
                <td><c:out value="${status.count}"></c:out></td>
                <td><c:out value="${appVehicleNo.displayName}"/></td>
                <td>
                  <input class="form-check-input vehicle-approve" type="radio" name="vehicleNoRadio${status.index}" value = "approve" aria-invalid="false" <c:if test="${'VEST002' eq appVehicleNo.status}">checked="checked"</c:if>/>&nbsp;&nbsp;&nbsp;
                  <label class="form-check-label"><span class="check-circle"></span>Approve</label>
                  <br>
                  <input class="form-check-input vehicle-reject" type="radio" name="vehicleNoRadio${status.index}" value = "reject" aria-invalid="false" <c:if test="${'VEST003' eq appVehicleNo.status}">checked="checked"</c:if>/>&nbsp;&nbsp;&nbsp;
                  <label class="form-check-label"><span class="check-circle"></span>Reject</label>
                  <br><span class="error-msg" name="iaisErrorMsg" id="error_vehicleNoRadioError${status.index}"></span>
                </td>
                <td>
                  <textarea maxlength="400" id="preInspecRemarks" name="vehicleNoRemarks${status.index}" cols="20" rows="2" style="font-size:16px"><c:out value="${appVehicleNo.remarks}"></c:out></textarea>
                  <br><span class="error-msg" name="iaisErrorMsg" id="error_vehicleNoRemarksError${status.index}"></span>
                </td>
              </tr>
            </c:forEach>
            </tbody>
          </table>
          <span class="error-msg" name="iaisErrorMsg" id="error_vehicleApproveOne"></span>
        </div>
      </iais:value>
    </iais:row>
  </c:if>
  <c:if test="${'view' eq appVehicleFlag}">
    <iais:row>
      <iais:field value="Vehicle Recommendations"/>
      <iais:value width="7">
        <div class="table-gp" id = "processRecRfi">
          <table aria-describedby="" class="table">
            <thead>
            <tr>
              <th scope="col" width="5%">S/N</th>
              <th scope="col" width="30%">Vehicle</th>
              <th scope="col" width="30%">Recommendation</th>
              <th scope="col" width="35%">Remarks</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="appVehicleNo" items="${appFlowSvcVehicleDtos}" varStatus="status">
              <tr>
                <td><c:out value="${status.count}"></c:out></td>
                <td><c:out value="${appVehicleNo.displayName}"/></td>
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
        </div>
      </iais:value>
    </iais:row>
  </c:if>
</c:if>
<script type="text/javascript">
  function appFlowVehicleShowRadio(recommendation) {
      if('other' == recommendation){
          $('.vehicle-approve').removeAttr("disabled","disabled");
          $('.vehicle-reject').removeAttr("disabled","disabled");
      } else if('reject' == recommendation || 'decisionReject' == recommendation) {
          $('.vehicle-approve').attr("disabled","disabled");
          $('.vehicle-reject').attr("disabled","disabled");
      }else{
          $('.vehicle-approve').removeAttr("disabled","disabled");
          $('.vehicle-reject').removeAttr("disabled","disabled");
      }
  }
</script>