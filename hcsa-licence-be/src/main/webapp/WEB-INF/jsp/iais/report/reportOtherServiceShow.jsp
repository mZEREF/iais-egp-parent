<%@ page import="com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts" %>

<c:if test="${('RETYPE007_edit' eq appOtherFlag || 'view' eq appOtherFlag) && (not empty changedOtherServiceList)}">
  <c:forEach var="appPremOthersSubSvcRelDto" items="${changedOtherServiceList}" varStatus="status">
    <tr>
      <c:if test="${appPremOthersSubSvcRelDto.actCode==ApplicationConsts.RECORD_ACTION_CODE_ADD}">
        <td class="col-xs-4"><p>Other Service Processing Decision (Addition) <c:if test="${'RETYPE007_edit' eq appOtherFlag}"> <strong style="color:#ff0000;"> *</strong></c:if></p></td>
      </c:if>
      <c:if test="${appPremOthersSubSvcRelDto.actCode==ApplicationConsts.RECORD_ACTION_CODE_REMOVE}">
        <td class="col-xs-4"><p>Other Service Processing Decision (Removal) <c:if test="${'RETYPE007_edit' eq appOtherFlag}"> <strong style="color:#ff0000;"> *</strong></c:if></p></td>
      </c:if>
      <td class="col-xs-5">
        <div class="table-gp" >
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
              <c:if test="${'RETYPE007_edit' eq appOtherFlag}">
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
              </c:if>
              <c:if test="${'view' eq appOtherFlag}">
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
              </c:if>
            </tbody>
          </table>
        </div>
      </td>
      <td class="col-xs-3"/>
    </tr>
  </c:forEach>
</c:if>
<script type="text/javascript">
    function doChangeOtherServiceShow(value) {
        if("IRE001"  == value || "IRE002" == value || "IRE007" == value){
            $('.otherSubSvc-approve').removeAttr("disabled","disabled");
            $('.otherSubSvc-reject').removeAttr("disabled","disabled");
        } else if("IRE003" ==  value || "IRE008" == value ) {
            $('.otherSubSvc-approve').prop('checked', false);
            $('.otherSubSvc-reject').prop('checked', true);
            $('.otherSubSvc-approve').attr("disabled","disabled");
            $('.otherSubSvc-reject').attr("disabled","disabled");
        }else{
            $('.otherSubSvc-approve').removeAttr("disabled","disabled");
            $('.otherSubSvc-reject').removeAttr("disabled","disabled");
        }
    }
</script>