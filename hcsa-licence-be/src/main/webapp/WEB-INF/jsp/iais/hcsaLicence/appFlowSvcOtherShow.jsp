<%@ page import="com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts" %>
<c:set var="appPremOthersSubSvcRelDtoList" value="${applicationViewDto.appPremOthersSubSvcRelDtoList}"/>

<c:if test="${!applicationViewDto.transfer}">
  <c:if test="${not empty appPremOthersSubSvcRelDtoList}">
    <c:if test="${'edit' eq appOtherFlag}">
      <iais:row>
        <iais:field value="Other Service <br> Processing Decision" required="true"/>
        <iais:value width="7">
          <div class="table-gp" id = "processRecRfi">
            <table aria-describedby="" class="table">
              <thead>
              <tr>
                <th scope="col" width="5%">S/N</th>
                <th scope="col" width="40%">Other Service</th>
                <th scope="col" width="30%">Recommendation</th>
                <th scope="col" width="25%">Remarks</th>
              </tr>
              </thead>
              <tbody>
              <c:forEach var="appPremOthersSubSvcRelDto" items="${appPremOthersSubSvcRelDtoList}" varStatus="status">
                <tr>
                  <td><c:out value="${status.count}"></c:out></td>
                  <td><c:out value="${appPremOthersSubSvcRelDto.svcName}"/></td>
                  <td>
                    <input class="form-check-input  otherSubSvc-approve" type="radio" name="otherSubSvcRadio${status.index}"
                           value = "approve" aria-invalid="false" <c:if test="${ApplicationConsts.RECORD_STATUS_APPROVE_CODE eq appPremOthersSubSvcRelDto.status}">checked="checked"</c:if>/>&nbsp;&nbsp;&nbsp;
                    <label class="form-check-label"><span class="check-circle"></span>Approve</label>
                    <br>
                    <input class="form-check-input otherSubSvc-reject" type="radio" name="otherSubSvcRadio${status.index}"
                           value = "reject" aria-invalid="false" <c:if test="${ApplicationConsts.RECORD_STATUS_REJECT_CODE eq appPremOthersSubSvcRelDto.status}">checked="checked"</c:if>/>&nbsp;&nbsp;&nbsp;
                    <label class="form-check-label"><span class="check-circle"></span>Reject</label>
                    <br><span class="error-msg" name="iaisErrorMsg" id="error_otherSubSvcRadioError${status.index}"></span>
                  </td>
                  <td>
                    <textarea maxlength="400" id="preInspecRemarks" name="otherSubSvcRemarks${status.index}" cols="10" rows="2" style="font-size:16px"><c:out value="${appPremOthersSubSvcRelDto.remarks}"></c:out></textarea>
                    <br><span class="error-msg" name="iaisErrorMsg" id="error_otherSubSvcRemarksError${status.index}"></span>
                  </td>
                </tr>
              </c:forEach>
              </tbody>
            </table>
            <span class="error-msg" name="iaisErrorMsg" id="error_otherSubSvcApproveOne"></span>
          </div>
        </iais:value>
      </iais:row>
    </c:if>
    <c:if test="${'view' eq appOtherFlag}">
      <iais:row>
        <iais:field value="Other Service <br> Processing Decision"/>
        <iais:value width="7">
          <div class="table-gp" id = "processRecRfi">
            <table aria-describedby="" class="table">
              <thead>
              <tr>
                <th scope="col" width="5%">S/N</th>
                <th scope="col" width="40%">Other Service</th>
                <th scope="col" width="30%">Recommendation</th>
                <th scope="col" width="25%">Remarks</th>
              </tr>
              </thead>
              <tbody>
              <c:forEach var="appPremOthersSubSvcRelDto" items="${appPremOthersSubSvcRelDtoList}" varStatus="status">
                <tr>
                  <td><c:out value="${status.count}"></c:out></td>
                  <td><c:out value="${appPremOthersSubSvcRelDto.svcName}"/></td>
                  <td>
                    <c:choose>
                      <c:when test="${ApplicationConsts.RECORD_STATUS_APPROVE_CODE eq appPremOthersSubSvcRelDto.status}">
                        <c:out value="Approve"/>
                      </c:when>
                      <c:when test="${ApplicationConsts.RECORD_STATUS_REJECT_CODE eq appPremOthersSubSvcRelDto.status}">
                        <c:out value="Reject"/>
                      </c:when>
                      <c:otherwise>
                        <c:out value="N/A"/>
                      </c:otherwise>
                    </c:choose>
                  </td>
                  <td><span style="font-size: 16px"><c:out value="${appPremOthersSubSvcRelDto.remarks}"/></span></td>
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
  function appFlowotherSubSvcShowRadio(recommendation) {
      if('other' == recommendation){
          $('.otherSubSvc-approve').removeAttr("disabled","disabled");
          $('.otherSubSvc-reject').removeAttr("disabled","disabled");
      } else if('reject' == recommendation || 'decisionReject' == recommendation) {
          $('.otherSubSvc-approve').attr("disabled","disabled");
          $('.otherSubSvc-reject').attr("disabled","disabled");
      }else{
          $('.otherSubSvc-approve').removeAttr("disabled","disabled");
          $('.otherSubSvc-reject').removeAttr("disabled","disabled");
      }
  }
</script>