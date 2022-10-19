<%@ page import="com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts" %>
<c:set var="appPremSpecialSubSvcRelDtoList" value="${applicationViewDto.appPremSpecialSubSvcRelDtoList}"/>

<c:if test="${!applicationViewDto.applicationGroupDto.transfer}">
  <c:if test="${not empty appPremSpecialSubSvcRelDtoList}">
    <c:if test="${'edit' eq appSpecialFlag}">
      <iais:row>
      <iais:field value="Specialised Service <br> Processing Decision" required="true"/>
      <iais:value width="7">
        <div class="table-gp" id = "processRecRfi">
          <table aria-describedby="" class="table">
            <thead>
              <tr>
                <th scope="col" width="5%">S/N</th>
                <th scope="col" width="40%">Specialised Service</th>
                <th scope="col" width="30%">Recommendation</th>
                <th scope="col" width="25%">Remarks</th>
              </tr>
            </thead>
            <tbody>
              <c:forEach var="appPremSpecialSubSvcRelDto" items="${appPremSpecialSubSvcRelDtoList}" varStatus="status">
              <tr>
                <td><c:out value="${status.count}"></c:out></td>
                <td><c:out value="${appPremSpecialSubSvcRelDto.svcName}"/></td>
                <td>
                  <input class="form-check-input  specialSubSvc-approve" type="radio" name="specialSubSvcRadio${status.index}"
                         value = "approve" aria-invalid="false" <c:if test="${ApplicationConsts.RECORD_STATUS_APPROVE_CODE eq appPremSpecialSubSvcRelDto.status}">checked="checked"</c:if>/>&nbsp;&nbsp;&nbsp;
                  <label class="form-check-label"><span class="check-circle"></span>Approve</label>
                  <br>
                  <input class="form-check-input specialSubSvc-reject" type="radio" name="specialSubSvcRadio${status.index}"
                         value = "reject" aria-invalid="false" <c:if test="${ApplicationConsts.RECORD_STATUS_REJECT_CODE eq appPremSpecialSubSvcRelDto.status}">checked="checked"</c:if>/>&nbsp;&nbsp;&nbsp;
                  <label class="form-check-label"><span class="check-circle"></span>Reject</label>
                  <br><span class="error-msg" name="iaisErrorMsg" id="error_specialSubSvcRadioError${status.index}"></span>
                </td>
                <td>
                  <textarea maxlength="400" id="preInspecRemarks" name="specialSubSvcRemarks${status.index}" cols="10" rows="2" style="font-size:16px"><c:out value="${appPremSpecialSubSvcRelDto.remarks}"></c:out></textarea>
                  <br><span class="error-msg" name="iaisErrorMsg" id="error_specialSubSvcRemarksError${status.index}"></span>
                </td>
              </tr>
            </c:forEach>
            </tbody>
          </table>
          <span class="error-msg" name="iaisErrorMsg" id="error_specialSubSvcApproveOne"></span>
        </div>
      </iais:value>
      </iais:row>
    </c:if>
    <c:if test="${'view' eq appSpecialFlag}">
      <iais:row>
        <iais:field value="Specialised Service <br> Processing Decision"/>
        <iais:value width="7">
          <div class="table-gp" id = "processRecRfi">
            <table aria-describedby="" class="table">
              <thead>
              <tr>
                <th scope="col" width="5%">S/N</th>
                <th scope="col" width="40%">Specialised Service</th>
                <th scope="col" width="30%">Recommendation</th>
                <th scope="col" width="25%">Remarks</th>
              </tr>
              </thead>
              <tbody>
              <c:forEach var="appPremSpecialSubSvcRelDto" items="${appPremSpecialSubSvcRelDtoList}" varStatus="status">
                <tr>
                  <td><c:out value="${status.count}"></c:out></td>
                  <td><c:out value="${appPremSpecialSubSvcRelDto.svcName}"/></td>
                  <td>
                    <c:choose>
                      <c:when test="${ApplicationConsts.RECORD_STATUS_APPROVE_CODE eq appPremSpecialSubSvcRelDto.status}">
                        <c:out value="Approve"/>
                      </c:when>
                      <c:when test="${ApplicationConsts.RECORD_STATUS_REJECT_CODE eq appPremSpecialSubSvcRelDto.status}">
                        <c:out value="Reject"/>
                      </c:when>
                      <c:otherwise>
                        <c:out value="N/A"/>
                      </c:otherwise>
                    </c:choose>
                  </td>
                  <td><span style="font-size: 16px"><c:out value="${appPremSpecialSubSvcRelDto.remarks}"/></span></td>
                </tr>
              </c:forEach>
              </tbody>
            </table>
          </div>
        </iais:value>
      </iais:row>
    </c:if>
  </c:if>
</c:if>

<script type="text/javascript">
  function appFlowSpecialSubSvcShowRadio(recommendation) {
      if('other' == recommendation){
          $('.specialSubSvc-approve').removeAttr("disabled","disabled");
          $('.specialSubSvc-reject').removeAttr("disabled","disabled");
      } else if('reject' == recommendation || 'decisionReject' == recommendation) {
          $('.specialSubSvc-approve').attr("disabled","disabled");
          $('.specialSubSvc-reject').attr("disabled","disabled");
      }else{
          $('.specialSubSvc-approve').removeAttr("disabled","disabled");
          $('.specialSubSvc-reject').removeAttr("disabled","disabled");
      }
  }
</script>