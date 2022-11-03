<%@ page import="com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts" %>
<c:if test="${!applicationViewDto.applicationGroupDto.transfer}">
  <c:if test="${not empty changedOtherServiceList}">
    <c:if test="${'edit' eq appOtherFlag}">
      <iais:row>
        <c:forEach var="appPremOthersSubSvcRelDto" items="${changedOtherServiceList}" varStatus="status">
          <c:if test="${appPremOthersSubSvcRelDto.actCode==ApplicationConsts.RECORD_ACTION_CODE_ADD}">
            <iais:field value="Other Service Processing <br> Decision (Addition)"/>
          </c:if>
          <c:if test="${appPremOthersSubSvcRelDto.actCode==ApplicationConsts.RECORD_ACTION_CODE_REMOVE}">
            <iais:field value="Other Service Processing <br> Decision (Removal)"/>
          </c:if>
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
                  <tr>
                    <td><c:out value="${status.count}"></c:out></td>
                    <td><c:out value="${appPremOthersSubSvcRelDto.svcName}"/></td>
                    <td>
                      <input class="form-check-input  otherSubSvc-approve" type="radio" name="otherSubSvcRadio${appPremOthersSubSvcRelDto.svcId}"
                             value = "approve" aria-invalid="false" <c:if test="${ApplicationConsts.RECORD_STATUS_APPROVE_CODE eq appPremOthersSubSvcRelDto.status}">checked="checked"</c:if>/>&nbsp;&nbsp;&nbsp;
                      <label class="form-check-label"><span class="check-circle"></span>Approve</label>
                      <br>
                      <input class="form-check-input otherSubSvc-reject" type="radio" name="otherSubSvcRadio${appPremOthersSubSvcRelDto.svcId}"
                             value = "reject" aria-invalid="false" <c:if test="${ApplicationConsts.RECORD_STATUS_REJECT_CODE eq appPremOthersSubSvcRelDto.status}">checked="checked"</c:if>/>&nbsp;&nbsp;&nbsp;
                      <label class="form-check-label"><span class="check-circle"></span>Reject</label>
                      <br><span class="error-msg" name="iaisErrorMsg" id="error_otherSubSvcRadioError${appPremOthersSubSvcRelDto.svcId}"></span>
                    </td>
                    <td>
                      <textarea maxlength="400" id="preInspecRemarks" name="otherSubSvcRemarks${appPremOthersSubSvcRelDto.svcId}" cols="10" rows="2" style="font-size:16px"><c:out value="${appPremOthersSubSvcRelDto.remarks}"></c:out></textarea>
                      <br><span class="error-msg" name="iaisErrorMsg" id="error_otherSubSvcRemarksError${appPremOthersSubSvcRelDto.svcId}"></span>
                    </td>
                  </tr>
                </tbody>
              </table>
            </div>
          </iais:value>
        </c:forEach>
      </iais:row>
    </c:if>
    <c:if test="${'view' eq appOtherFlag}">
      <iais:row>
        <c:forEach var="appPremOthersSubSvcRelDto" items="${changedOtherServiceList}" varStatus="status">
          <c:if test="${appPremOthersSubSvcRelDto.actCode==ApplicationConsts.RECORD_ACTION_CODE_ADD}">
            <iais:field value="Other Service Processing <br> Decision (Addition)"/>
          </c:if>
          <c:if test="${appPremOthersSubSvcRelDto.actCode==ApplicationConsts.RECORD_ACTION_CODE_REMOVE}">
            <iais:field value="Other Service Processing <br> Decision (Removal)"/>
          </c:if>
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
                </tbody>
              </table>
            </div>
          </iais:value>
        </c:forEach>
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