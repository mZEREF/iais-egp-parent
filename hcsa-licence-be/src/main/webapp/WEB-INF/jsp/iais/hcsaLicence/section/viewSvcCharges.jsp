<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>

<div class="amended-service-info-gp">
    <label class="title-font-size">${stepNameMap['SVST010']}</label>
    <div class="amend-preview-info">
        <div class="form-check-gp">
            <div class="row">
                <div class="col-xs-12">
                <c:set value="${currentPreviewSvcInfo.appSvcChargesPageDto}" var="appSvcChargesPageDto"></c:set>
                <c:forEach items="${appSvcChargesPageDto.generalChargesDtos}" var="generalChargesDtos" varStatus="status">
                    <p><strong class="col-xs-6">General Conveyance Charges <c:if
                            test="${fn:length(currentPreviewSvcInfo.appSvcChargesPageDto.generalChargesDtos)>1}">${status.index+1}</c:if>:</strong><span
                            class="col-xs-4 col-md-4"></span>
                    </p>
                    <span class="col-xs-6"></span>
                    <table aria-describedby="" class="col-xs-12">
                        <tr>
                            <th scope="col" style="display: none"></th>
                            <td class="col-xs-6">
                                <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Type of Charge</p>
                            </td>
                            <td>
                                <div class="col-xs-12">
                                    <span class="newVal " attr="${generalChargesDtos.chargesType}">
                                        <iais:code code="${generalChargesDtos.chargesType}"></iais:code>
                                    </span>
                                    <br>
                                    <span class="oldVal "  style="display: none" attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcChargesPageDto.generalChargesDtos[status.index].chargesType}">
                                     <iais:code code="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcChargesPageDto.generalChargesDtos[status.index].chargesType}"></iais:code>
                                    </span>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td  class="col-xs-6">
                                <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Amount From</p>
                            </td>
                            <td>
                                <div class="col-xs-6">
                                  <span class="newVal " attr="${generalChargesDtos.minAmount}">
                                          ${generalChargesDtos.minAmount}
                                  </span>
                                </div>
                                <div class="col-xs-6">
                                  <span class="oldVal "  style="display: none" attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcChargesPageDto.generalChargesDtos[status.index].minAmount}">
                                          ${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcChargesPageDto.generalChargesDtos[status.index].minAmount}
                                  </span>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td class="col-xs-6">
                                <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Amount To</p>
                            </td>
                            <td>
                                <div class="col-xs-6">
                                  <span class="newVal " attr="${generalChargesDtos.maxAmount}">
                                          ${generalChargesDtos.maxAmount}
                                  </span>
                                </div>
                                <div class="col-xs-6">
                                  <span class="oldVal "  style="display: none" attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcChargesPageDto.generalChargesDtos[status.index].maxAmount}">
                                          ${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcChargesPageDto.generalChargesDtos[status.index].maxAmount}
                                  </span>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td class="col-xs-6">
                                <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Remarks</p>
                            </td>
                            <td>
                                <div class="col-xs-12">
                                    <span class="newVal " attr="${generalChargesDtos.remarks}">
                                          ${generalChargesDtos.remarks}
                                    </span>
                                    <br>
                                    <span class="oldVal "  style="display: none" attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcChargesPageDto.generalChargesDtos[status.index].remarks}">
                                            ${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcChargesPageDto.generalChargesDtos[status.index].remarks}
                                    </span>
                                </div>

                            </td>
                        </tr>
                    </table>
                </c:forEach>
                <c:forEach items="${appSvcChargesPageDto.otherChargesDtos}" var="otherChargesDtos" varStatus="status">
                    <p><strong class="col-xs-6">Medical Equipment and Other Charges <c:if
                            test="${fn:length(currentPreviewSvcInfo.appSvcChargesPageDto.otherChargesDtos)>1}">${status.index+1}</c:if>:</strong><span
                            class="col-xs-4 col-md-4"></span>
                    </p>
                    <span class="col-xs-6"></span>
                    <table aria-describedby="" class="col-xs-12">
                        <tr>
                            <th scope="col" style="display: none"></th>
                            <td class="col-xs-6">
                                <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Category</p>
                            </td>
                            <td>
                                <div class="col-xs-12">
                                    <span class="newVal " attr="${otherChargesDtos.chargesCategory}">
                                        <iais:code code="${otherChargesDtos.chargesCategory}"></iais:code>
                                    </span>
                                    <br>
                                    <span class="oldVal "  style="display: none" attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcChargesPageDto.otherChargesDtos[status.index].chargesCategory}">
                                        <iais:code code="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcChargesPageDto.otherChargesDtos[status.index].chargesCategory}"></iais:code>
                                    </span>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td  class="col-xs-6">
                                <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Type of Charge</p>
                            </td>
                            <td>
                                <div class="col-xs-12">
                                  <span class="newVal " attr="${otherChargesDtos.chargesType}">
                                    <iais:code code="${otherChargesDtos.chargesType}"></iais:code>
                                  </span>
                                  <br>
                                  <span class="oldVal " style="display: none" attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcChargesPageDto.otherChargesDtos[status.index].chargesType}">
                                     <iais:code code="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcChargesPageDto.otherChargesDtos[status.index].chargesType}"></iais:code>
                                  </span>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td class="col-xs-6">
                                <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Amount From</p>
                            </td>
                            <td>
                                <div class="col-xs-6">
                                  <span class="newVal " attr="${otherChargesDtos.minAmount}">
                                          ${otherChargesDtos.minAmount}
                                  </span>
                                </div>
                                <div class="col-xs-6">
                                  <span class="oldVal " style="display: none"  attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcChargesPageDto.otherChargesDtos[status.index].minAmount}">
                                          ${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcChargesPageDto.otherChargesDtos[status.index].minAmount}
                                  </span>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td class="col-xs-6">
                                <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Amount To</p>
                            </td>
                            <td>
                                <div class="col-xs-6">
                                  <span class="newVal " attr="${otherChargesDtos.maxAmount}">
                                          ${otherChargesDtos.maxAmount}
                                  </span>
                                </div>
                                <div class="col-xs-6">
                                  <span class="oldVal " style="display: none"  attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcChargesPageDto.otherChargesDtos[status.index].maxAmount}">
                                          ${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcChargesPageDto.otherChargesDtos[status.index].maxAmount}
                                  </span>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td class="col-xs-6">
                                <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Remarks</p>
                            </td>
                            <td>
                                <div class="col-xs-12">
                                  <span class="newVal " attr="${otherChargesDtos.remarks}">
                                          ${otherChargesDtos.remarks}
                                  </span>
                                    <span class="oldVal " style="display: none"  attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcChargesPageDto.otherChargesDtos[status.index].remarks}">
                                            ${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcChargesPageDto.otherChargesDtos[status.index].remarks}
                                    </span>
                                </div>
                            </td>
                        </tr>
                    </table>
                </c:forEach>
                </div>
            </div>
        </div>
    </div>
</div>