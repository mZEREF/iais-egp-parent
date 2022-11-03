<%@ page import="com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts" %>
<c:if test="${!applicationViewDto.applicationGroupDto.transfer}">
  <c:if test="${not empty changedSpecialServiceList}">
    <c:if test="${'edit' eq appSpecialFlag}">
      <iais:row>
        <c:forEach var="appPremSpecialSubSvcRelDto" items="${changedSpecialServiceList}" varStatus="status">
          <c:if test="${appPremSpecialSubSvcRelDto.actCode==ApplicationConsts.RECORD_ACTION_CODE_ADD}">
            <iais:field value="Specified Service  <br> Recommendation (Addition)"/>
          </c:if>
          <c:if test="${appPremSpecialSubSvcRelDto.actCode==ApplicationConsts.RECORD_ACTION_CODE_REMOVE}">
            <iais:field value="Specified Service  <br> Recommendation (Removal)"/>
          </c:if>
          <iais:value width="7">
            <div class="table-gp" id = "processRecRfi">
              <table aria-describedby="" class="table">
                <thead>
                  <tr>
                    <th scope="col" width="5%">S/N</th>
                    <th scope="col" width="40%">Specified Service</th>
                    <th scope="col" width="30%">Recommendation</th>
                    <th scope="col" width="25%">Remarks</th>
                  </tr>
                </thead>
                <tbody>
                  <tr>
                    <td><c:out value="${status.count}"></c:out></td>
                    <td><c:out value="${appPremSpecialSubSvcRelDto.svcName}"/></td>
                    <td>
                      <input class="form-check-input  specialSubSvc-approve" type="radio" name="specialSubSvcRadio${appPremSpecialSubSvcRelDto.svcId}"
                             value = "approve" aria-invalid="false" <c:if test="${ApplicationConsts.RECORD_STATUS_APPROVE_CODE eq appPremSpecialSubSvcRelDto.status}">checked="checked"</c:if>/>&nbsp;&nbsp;&nbsp;
                      <label class="form-check-label"><span class="check-circle"></span>Approve</label>
                      <br>
                      <input class="form-check-input specialSubSvc-reject" type="radio" name="specialSubSvcRadio${appPremSpecialSubSvcRelDto.svcId}"
                             value = "reject" aria-invalid="false" <c:if test="${ApplicationConsts.RECORD_STATUS_REJECT_CODE eq appPremSpecialSubSvcRelDto.status}">checked="checked"</c:if>/>&nbsp;&nbsp;&nbsp;
                      <label class="form-check-label"><span class="check-circle"></span>Reject</label>
                      <br><span class="error-msg" name="iaisErrorMsg" id="error_specialSubSvcRadioError${appPremSpecialSubSvcRelDto.svcId}"></span>
                    </td>
                    <td>
                      <textarea maxlength="400" id="preInspecRemarks" name="specialSubSvcRemarks${appPremSpecialSubSvcRelDto.svcId}" cols="10" rows="2" style="font-size:16px"><c:out value="${appPremSpecialSubSvcRelDto.remarks}"></c:out></textarea>
                      <br><span class="error-msg" name="iaisErrorMsg" id="error_specialSubSvcRemarksError${appPremSpecialSubSvcRelDto.svcId}"></span>
                    </td>
                  </tr>
                </tbody>
              </table>
            </div>
          </iais:value>
        </c:forEach>
      </iais:row>
    </c:if>
    <c:if test="${'view' eq appSpecialFlag}">
      <iais:row>
        <c:forEach var="appPremSpecialSubSvcRelDto" items="${changedSpecialServiceList}" varStatus="status">
          <c:if test="${appPremSpecialSubSvcRelDto.actCode==ApplicationConsts.RECORD_ACTION_CODE_ADD}">
            <iais:field value="Specified Service  <br> Recommendation (Addition)"/>
          </c:if>
          <c:if test="${appPremSpecialSubSvcRelDto.actCode==ApplicationConsts.RECORD_ACTION_CODE_REMOVE}">
            <iais:field value="Specified Service  <br> Recommendation (Removal)"/>
          </c:if>
          <iais:value width="7">
            <div class="table-gp" id = "processRecRfi">
              <table aria-describedby="" class="table">
                <thead>
                <tr>
                  <th scope="col" width="5%">S/N</th>
                  <th scope="col" width="40%">Specified Service</th>
                  <th scope="col" width="30%">Recommendation</th>
                  <th scope="col" width="25%">Remarks</th>
                </tr>
                </thead>
                <tbody>
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