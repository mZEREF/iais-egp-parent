<%--
  Created by IntelliJ IDEA.
  User: ZiXian
  Date: 2021/4/25
  Time: 18:00
  To change this template use File | Settings | File Templates.
--%>
<c:forEach var="stepSchem" items="${currentPreviewSvcInfo.hcsaServiceStepSchemeDtos}">
    <c:if test="${stepSchem.stepCode == 'SVST010'}">
        <c:set var="currStepName" value="${stepSchem.stepName}"/>
    </c:if>
</c:forEach>
<div class="amended-service-info-gp">
    <label style="font-size: 2.2rem">${currStepName}</label>
    <div class="amend-preview-info">
        <div class="form-check-gp">
            <div class="row">
                <div class="col-xs-12">
                    <c:forEach items="${currentPreviewSvcInfo.appSvcChargesPageDto.generalChargesDtos}" var="generalChargesDto" varStatus="gcStatus">
                        <div  class="col-xs-12" style="margin-bottom: 1%;margin-top: 1%">
                            <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span><strong>General Conveyance Charges<c:if test="${currentPreviewSvcInfo.appSvcChargesPageDto.generalChargesDtos.size() > 1}"> ${gcStatus.index+1}</c:if></strong></p>
                        </div>
                        <table class="col-xs-12">
                            <tr>
                                <td class="col-xs-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Type of Charge</p>
                                </td>
                                <td>
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span><iais:code code="${generalChargesDto.chargesType}"/></p>
                                </td>
                            </tr>
                            <tr>
                                <td class="col-xs-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Amount From</p>
                                </td>
                                <td>
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span><iais:code code="${generalChargesDto.minAmount}"/></p>
                                </td>
                            </tr>
                            <tr>
                                <td class="col-xs-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Amount To</p>
                                </td>
                                <td>
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span><iais:code code="${generalChargesDto.maxAmount}"/></p>
                                </td>
                            </tr>
                            <tr>
                                <td class="col-xs-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Remarks</p>
                                </td>
                                <td>
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span><iais:code code="${generalChargesDto.remarks}"/></p>
                                </td>
                            </tr>
                        </table>
                    </c:forEach>
                </div>

                <div class="col-xs-12">
                    <c:forEach items="${currentPreviewSvcInfo.appSvcChargesPageDto.otherChargesDtos}" var="otherChargesDto" varStatus="ocStatus">
                        <div  class="col-xs-12" style="margin-bottom: 1%;margin-top: 1%">
                            <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span><strong>Medical Equipment and Other Charges<c:if test="${currentPreviewSvcInfo.appSvcChargesPageDto.otherChargesDtos.size() > 1}"> ${ocStatus.index+1}</c:if></strong></p>
                        </div>
                        <table class="col-xs-12">
                            <tr>
                                <td class="col-xs-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Category</p>
                                </td>
                                <td>
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span><iais:code code="${otherChargesDto.chargesCategory}"/></p>
                                </td>
                            </tr>
                            <tr>
                                <td class="col-xs-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Type of Charge</p>
                                </td>
                                <td>
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span><iais:code code="${otherChargesDto.chargesType}"/></p>
                                </td>
                            </tr>
                            <tr>
                                <td class="col-xs-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Amount From</p>
                                </td>
                                <td>
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span><iais:code code="${otherChargesDto.minAmount}"/></p>
                                </td>
                            </tr>
                            <tr>
                                <td class="col-xs-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Amount To</p>
                                </td>
                                <td>
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span><iais:code code="${otherChargesDto.maxAmount}"/></p>
                                </td>
                            </tr>
                            <tr>
                                <td class="col-xs-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Remarks</p>
                                </td>
                                <td>
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span><iais:code code="${otherChargesDto.remarks}"/></p>
                                </td>
                            </tr>
                        </table>
                    </c:forEach>
                </div>
            </div>
        </div>
    </div>
</div>
