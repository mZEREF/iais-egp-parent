<div class="amended-service-info-gp">
    <h2>CLINICAL GOVERNANCE OFFICER</h2>
    <div class="amend-preview-info">
        <c:forEach var="cgo" items="${currentPreviewSvcInfo.appSvcCgoDtoList}" varStatus="status">
        <p>Clinical Governance Officer ${status.index+1}:</p>
        <div class="form-check-gp">
            <div class="row">
                <div class="col-xs-12">
                    <table class="col-xs-8">
                        <tr>
                            <td class="col-xs-8">
                                <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Salutation :</p>
                            </td>
                            <td>
                                <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span> <iais:code code="${cgo.salutation}" /></p>
                            </td>
                        </tr>

                        <tr>
                            <td class="col-xs-8">
                                <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Name :</p>
                            </td>
                            <td>
                                <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span> ${cgo.name }</p>
                            </td>
                        </tr>

                        <tr>
                            <td class="col-xs-8">
                                <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>ID Type :</p>
                            </td>
                            <td>
                                <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span> ${cgo.idType }</p>
                            </td>
                        </tr>

                        <tr>
                            <td class="col-xs-8">
                                <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>ID No :</p>
                            </td>
                            <td>
                                <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>${cgo.idNo }</p>
                            </td>
                        </tr>

                        <tr>
                            <td class="col-xs-8">
                                <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Designation :</p>
                            </td>
                            <td>
                                <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span><iais:code code="${cgo.designation}"/> </p>
                            </td>
                        </tr>

                        <tr>
                            <td class="col-xs-8">
                                <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Professional Type :</p>

                            </td>
                            <td>
                                <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>
                                    <c:choose>
                                        <c:when test="${cgo.professionType=='PROF001'  }">Dentist</c:when>
                                        <c:when test="${cgo.professionType=='PROF002'  }">Doctor</c:when>
                                        <c:when test="${cgo.professionType=='PROF003'  }">Nurse</c:when>
                                    </c:choose> </p>
                                </p>
                            </td>
                        </tr>

                        <tr>
                            <td class="col-xs-8">
                                <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Professional No :</p>
                            </td>
                            <td>
                                <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span> ${cgo.professionRegoNo }</p>
                            </td>
                        </tr>

                        <tr>
                            <td class="col-xs-8">
                                <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Specialty :</p>
                            </td>
                            <td>
                                <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span> ${cgo.speciality }</p>
                            </td>
                        </tr>
                        <c:if test="${'other' == cgo.speciality}">
                            <tr>
                                <td class="col-xs-8">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span></p>
                                </td>
                                <td>
                                    <p><c:out value="${cgo.specialityOther}"/></p>
                                </td>
                            </tr>
                        </c:if>
                        <tr>
                            <td class="col-xs-8">
                                <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Subspeciality or relevant qualification :</p>
                            </td>
                            <td>
                                <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span> ${cgo.qualification}</p>
                            </td>
                        </tr>

                        <tr>
                            <td class="col-xs-8">
                                <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Mobile No :</p>
                            </td>
                            <td>
                                <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span> ${cgo.mobileNo}</p>
                            </td>
                        </tr>
                        <tr>
                            <td class="col-xs-8">
                                <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Email Address :</p>
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

