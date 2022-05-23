<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<div class="amended-service-info-gp">
    <label class="svc-title">${currStepName}</label>
    <div class="amend-preview-info">
        <c:forEach var="keyAppointmentHoldeDto" items="${currentPreviewSvcInfo.appSvcKeyAppointmentHolderDtoList}" varStatus="status">

            <div class="form-check-gp">
                <div class="row">
                    <div class="col-xs-12">
                        <div  class="col-xs-12" style="margin-bottom: 1%;margin-top: 1%">
                            <p><strong>Key Appointment Holder<c:if test="${currentPreviewSvcInfo.appSvcKeyAppointmentHolderDtoList.size() > 1}"> ${status.index+1}</c:if>:</strong></p>
                        </div>
                        <table aria-describedby="" class="col-xs-12">
                            <thead style="display: none">
                            <tr><th scope="col"></th> </tr>
                            </thead>
                            <tr>
                                <td class="col-xs-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Salutation </p>
                                </td>
                                <td>
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span> <iais:code code="${keyAppointmentHoldeDto.salutation}" /></p>
                                </td>
                            </tr>

                            <tr>
                                <td class="col-xs-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Name </p>
                                </td>
                                <td>
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span> ${keyAppointmentHoldeDto.name }</p>
                                </td>
                            </tr>

                            <tr>
                                <td class="col-xs-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>ID Type </p>
                                </td>
                                <td>
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span><iais:code code="${keyAppointmentHoldeDto.idType}"></iais:code></p>
                                </td>
                            </tr>
                            <tr>
                                <td class="col-xs-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>ID No. </p>
                                </td>
                                <td>
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span><c:out value="${keyAppointmentHoldeDto.idNo }"/></p>
                                </td>
                            </tr>

                            <tr>
                                <td class="col-xs-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology">
                                        <span class="check-square"></span>Country of issuance
                                    </p>
                                </td>
                                <td>
                                    <p class="form-check-label" aria-label="premise-1-cytology">
                                        <span class="check-square"></span><iais:code code="${keyAppointmentHoldeDto.nationality}" />
                                    </p>
                                </td>
                            </tr>

                        </table>
                    </div>
                </div>
            </div>
        </c:forEach>
    </div>

</div>


