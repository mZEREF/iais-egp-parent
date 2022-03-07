<%@ page import="com.ecquaria.cloud.moh.iais.common.constant.HcsaConsts" %>
<div class="amended-service-info-gp">
    <label style="font-size: 2.2rem">${currStepName}</label>
    <div class="amend-preview-info">
        <div class="form-check-gp">
            <div class="row">
                <div class="col-xs-12">
                    <c:forEach items="${currentPreviewSvcInfo.appSvcClinicalDirectorDtoList}" var="cdDto" varStatus="status">
                        <div  class="col-xs-12" style="margin-bottom: 1%;margin-top: 1%">
                            <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span><strong><%=HcsaConsts.CLINICAL_DIRECTOR%><c:if test="${currentPreviewSvcInfo.appSvcClinicalDirectorDtoList.size() > 1}"> ${status.index+1}</c:if>: </strong></p>
                        </div>
                        <table aria-describedby="" class="col-xs-12">
                            <thead style="display: none">
                            <tr><th scope="col"></th></tr>
                            </thead>
                            <tr>
                                <td class="col-xs-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Professional Board</p>
                                </td>
                                <td>
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span><iais:code code="${cdDto.professionBoard}"/></p>
                                </td>
                            </tr>
                            <tr>
                                <td class="col-xs-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Professional Regn. No.</p>
                                </td>
                                <td>
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span><c:out value="${cdDto.profRegNo}"/></p>
                                </td>
                            </tr>
                            <c:if test="${'MTS' == currentPreviewSvcInfo.serviceCode}">
                                <tr>
                                    <td class="col-xs-6">
                                        <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Not registered with a Professional Board</p>
                                    </td>
                                    <td>
                                        <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>
                                            <c:choose>
                                                <c:when test="${'1' == cdDto.noRegWithProfBoard}">
                                                <div class="form-check active" style="padding-left:0px">
                                                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>
                                                    </p>
                                                </div>
                                                </c:when>
                                            </c:choose>
                                        </p>
                                    </td>
                                </tr>
                            </c:if>
                            <tr>
                                <td class="col-xs-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Salutation </p>
                                </td>
                                <td>
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span><iais:code code="${cdDto.salutation }"/></p>
                                </td>
                            </tr>
                            <tr>
                                <td class="col-xs-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Name </p>
                                </td>
                                <td>
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span><c:out value="${cdDto.name }"/></p>
                                </td>
                            </tr>
                            <tr>
                                <td class="col-xs-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>ID Type </p>
                                </td>
                                <td>
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span><iais:code code="${cdDto.idType}"></iais:code></p>
                                </td>
                            </tr>
                            <tr>
                                <td class="col-xs-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>ID No. </p>
                                </td>
                                <td>
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span><c:out value="${cdDto.idNo }"/></p>
                                </td>
                            </tr>
                            <c:if test="${cdDto.idType == 'IDTYPE003'}">
                            <tr>
                                <td class="col-xs-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology">
                                        <span class="check-square"></span>Nationality
                                    </p>
                                </td>
                                <td>
                                    <p class="form-check-label" aria-label="premise-1-cytology">
                                        <span class="check-square"></span><iais:code code="${cdDto.nationality}" />
                                    </p>
                                </td>
                            </tr>
                            </c:if>
                            <tr>
                                <td class="col-xs-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Designation </p>
                                </td>
                                <td>
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span><iais:code code="${cdDto.designation}"/> </p>
                                </td>
                            </tr>
                            <c:if test="${'DES999' == cdDto.designation}">
                                <tr>
                                    <td class="col-xs-6">
                                    </td>
                                    <td>
                                        <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span><c:out value="${cdDto.otherDesignation}"/> </p>
                                    </td>
                                </tr>
                            </c:if>
                            <tr>
                                <td class="col-xs-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Specialty </p>
                                </td>
                                <td>
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>
                                        <c:out value="${cdDto.speciality}"/> </p>
                                </td>
                            </tr>
                            <tr>
                                <td class="col-xs-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology">
                                        <span class="check-square"></span>Date when specialty was obtained
                                    </p>
                                </td>
                                <td>
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span><c:out value="${cdDto.specialtyGetDateStr}"/> </p>
                                </td>
                            </tr>
                            <tr>
                                <td class="col-xs-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Type of Registration Date </p>
                                </td>
                                <td>
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span><c:out value="${cdDto.typeOfCurrRegi}"/> </p>
                                </td>
                            </tr>
                            <tr>
                                <td class="col-xs-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Current Registration Date </p>
                                </td>
                                <td>
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span><c:out value="${cdDto.currRegiDateStr}"/> </p>
                                </td>
                            </tr>
                            <tr>
                                <td class="col-xs-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Practicing Certificate End Date </p>
                                </td>
                                <td>
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span><c:out value="${cdDto.praCerEndDateStr}"/> </p>
                                </td>
                            </tr>
                            <tr>
                                <td class="col-xs-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Type of Register </p>
                                </td>
                                <td>
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span><c:out value="${cdDto.typeOfRegister}"/> </p>
                                </td>
                            </tr>
                            <tr>
                                <td class="col-xs-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Relevant Experience </p>
                                </td>
                                <td>
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span><c:out value="${cdDto.relevantExperience}"/> </p>
                                </td>
                            </tr>
                            <tr>
                                <td class="col-xs-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Clinical Governance Officer (CGO) holds a valid certification issued by an Emergency Medical Services ("EMS") Medical Directors workshop </p>
                                </td>
                                <td>
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>
                                        <c:choose>
                                            <c:when test="${'1' == cdDto.holdCerByEMS}">
                                                Yes
                                            </c:when>
                                            <c:when test="${'0' == cdDto.holdCerByEMS}">
                                                No
                                            </c:when>
                                        </c:choose>
                                    </p>
                                </td>
                            </tr>
                            <tr>
                                <td class="col-xs-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Expiry Date (ACLS) </p>
                                </td>
                                <td>
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span><c:out value="${cdDto.aclsExpiryDateStr}"/> </p>
                                </td>
                            </tr>
                            <c:if test="${'MTS' == currentPreviewSvcInfo.serviceCode}">
                                <tr>
                                    <td class="col-xs-6">
                                        <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Expiry Date (BCLS and AED) </p>
                                    </td>
                                    <td>
                                        <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span><c:out value="${cdDto.bclsExpiryDateStr}"/> </p>
                                    </td>
                                </tr>
                            </c:if>
                            <tr>
                                <td class="col-xs-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Mobile No. </p>
                                </td>
                                <td>
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span><c:out value="${cdDto.mobileNo}"/> </p>
                                </td>
                            </tr>
                            <tr>
                                <td class="col-xs-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Email Address </p>
                                </td>
                                <td>
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span><c:out value="${cdDto.emailAddr}"/> </p>
                                </td>
                            </tr>
                        </table>
                    </c:forEach>
                </div>
            </div>
        </div>
    </div>
</div>
