<div class="amended-service-info-gp">
    <label style="font-size: 2.2rem">${currStepName}</label>
    <div class="amend-preview-info">
        <c:forEach var="cgo" items="${currentPreviewSvcInfo.appSvcCgoDtoList}" varStatus="status">

            <div class="form-check-gp">
                <div class="row">
                    <div class="col-xs-12">
                        <div  class="col-xs-12" style="margin-bottom: 1%;margin-top: 1%">
                            <p><strong>Clinical Governance Officer<c:if test="${currentPreviewSvcInfo.appSvcCgoDtoList.size() > 1}"> ${status.index+1}</c:if>:</strong></p>
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
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span> <iais:code code="${cgo.salutation}" /></p>
                                </td>
                            </tr>

                            <tr>
                                <td class="col-xs-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Name </p>
                                </td>
                                <td>
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span> ${cgo.name }</p>
                                </td>
                            </tr>
                            <tr>
                                <td class="col-xs-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>ID No. </p>
                                </td>
                                <td>
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>${cgo.idNo }</p>
                                </td>
                            </tr>

                            <tr>
                                <td class="col-xs-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>ID Type </p>
                                </td>
                                <td>
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span><iais:code code="${cgo.idType}"></iais:code></p>
                                </td>
                            </tr>
                            <c:if test="${cgo.idType == 'IDTYPE003'}">
                            <tr>
                                <td class="col-xs-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology">
                                        <span class="check-square"></span>Nationality
                                    </p>
                                </td>
                                <td>
                                    <p class="form-check-label" aria-label="premise-1-cytology">
                                        <span class="check-square"></span><iais:code code="${cgo.nationality}"/>
                                    </p>
                                </td>
                            </tr>
                            </c:if>
                            <tr>
                                <td class="col-xs-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Designation </p>
                                </td>
                                <td>
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span><iais:code code="${cgo.designation}"/> </p>
                                </td>
                            </tr>

                            <c:if test="${'DES999' == cgo.designation}">
                                <tr>
                                    <td class="col-xs-6">
                                    </td>
                                    <td>
                                        <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span><c:out value="${cgo.otherDesignation}"/> </p>
                                    </td>
                                </tr>
                            </c:if>


                            <tr>
                                <td class="col-xs-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Professional Type </p>

                                </td>
                                <td>
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>
                                        <iais:code code="${cgo.professionType}"/>
                                        <%--<c:choose>
                                            <c:when test="${cgo.professionType=='PROF001'  }">Dentist</c:when>
                                            <c:when test="${cgo.professionType=='PROF002'  }">Doctor</c:when>
                                            <c:when test="${cgo.professionType=='PROF003'  }">Nurse</c:when>
                                        </c:choose>--%> </p>
                                    </p>
                                </td>
                            </tr>

                            <tr>
                                <td class="col-xs-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Professional Regn. No. </p>
                                </td>
                                <td>
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span> ${cgo.profRegNo }</p>
                                </td>
                            </tr>

                            <tr>
                                <td class="col-xs-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Specialty </p>
                                </td>
                                <td>
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span> ${cgo.speciality}</p>
                                </td>
                            </tr>

                            <tr>
                                <td class="col-xs-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Sub-specialty</p>
                                </td>
                                <td>
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span> ${cgo.subSpeciality}</p>
                                </td>
                            </tr>

                            <tr>
                                <td class="col-xs-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Qualification</p>
                                </td>
                                <td>
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span> ${cgo.qualification}</p>
                                </td>
                            </tr>
                            <tr>
                                <td class="col-xs-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Other Qualification </p>
                                </td>
                                <td>
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span> ${cgo.otherQualification}</p>
                                </td>
                            </tr>

                            <tr>
                                <td class="col-xs-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Mobile No. </p>
                                </td>
                                <td>
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span> ${cgo.mobileNo}</p>
                                </td>
                            </tr>
                            <tr>
                                <td class="col-xs-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Email Address </p>
                                </td>
                                <td>
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span> ${cgo.emailAddr}</p>
                                </td>
                            </tr>
                        </table>
                    </div>
                </div>
            </div>
        </c:forEach>
    </div>

</div>

