<%@ page import="com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts" %>
<c:if test="${('RETYPE007_edit' eq appSpecialFlag || 'view' eq appSpecialFlag) && (not empty addSpecialServiceList)}">
  <tr>
    <td class="col-xs-4"><p>Specified Service Recommendation (Addition) <c:if test="${'RETYPE007_edit' eq appSpecialFlag}"> <strong style="color:#ff0000;"> *</strong></c:if></p></td>
    <td class="col-xs-8">
      <div class="table-gp">
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
            <c:forEach var="appPremSpecialSubSvcRelDto" items="${addSpecialServiceList}" varStatus="status">
              <c:if test="${'RETYPE007_edit' eq appSpecialFlag}">
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
                    <textarea maxlength="400" name="specialSubSvcRemarks${appPremSpecialSubSvcRelDto.svcId}" cols="10" rows="2" style="font-size:16px"><c:out value="${appPremSpecialSubSvcRelDto.remarks}"></c:out></textarea>
                    <br><span class="error-msg" name="iaisErrorMsg" id="error_specialSubSvcRemarksError${appPremSpecialSubSvcRelDto.svcId}"></span>
                  </td>
                </tr>
              </c:if>
              <c:if test="${'view' eq appSpecialFlag}">
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
              </c:if>
            </c:forEach>
          </tbody>
        </table>
      </div>
    </td>
  </tr>
</c:if>
<c:if test="${('RETYPE007_edit' eq appSpecialFlag || 'view' eq appSpecialFlag) && (not empty removeSpecialServiceList)}">
  <tr>
    <td class="col-xs-4"><p>Specified Service Recommendation (Removal) <c:if test="${'RETYPE007_edit' eq appSpecialFlag}"> <strong style="color:#ff0000;"> *</strong></c:if></p></td>
    <td class="col-xs-8">
      <div class="table-gp">
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
            <c:forEach var="appPremSpecialSubSvcRelDto" items="${removeSpecialServiceList}" varStatus="status">
              <c:if test="${'RETYPE007_edit' eq appSpecialFlag}">
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
                    <textarea maxlength="400" name="specialSubSvcRemarks${appPremSpecialSubSvcRelDto.svcId}" cols="10" rows="2" style="font-size:16px"><c:out value="${appPremSpecialSubSvcRelDto.remarks}"></c:out></textarea>
                    <br><span class="error-msg" name="iaisErrorMsg" id="error_specialSubSvcRemarksError${appPremSpecialSubSvcRelDto.svcId}"></span>
                  </td>
                </tr>
              </c:if>
              <c:if test="${'view' eq appSpecialFlag}">
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
              </c:if>
            </c:forEach>
          </tbody>
        </table>
      </div>
    </td>
  </tr>
</c:if>
<script type="text/javascript">
    function doChangeSpecialServiceShow(value) {
        if("IRE001"  == value || "IRE002" == value || "IRE007" == value){
            $('.specialSubSvc-approve').removeAttr("disabled","disabled");
            $('.specialSubSvc-reject').removeAttr("disabled","disabled");
        } else if("IRE003" ==  value || "IRE008" == value ) {
            $('.specialSubSvc-approve').prop('checked', false);
            $('.specialSubSvc-reject').prop('checked', true);
            $('.specialSubSvc-approve').attr("disabled","disabled");
            $('.specialSubSvc-reject').attr("disabled","disabled");
        }else{
            $('.specialSubSvc-approve').removeAttr("disabled","disabled");
            $('.specialSubSvc-reject').removeAttr("disabled","disabled");
        }
    }
</script>