<table aria-describedby="" class="col-xs-12">
    <tr>
        <th scope="col" style="display: none"></th>
        <td class="col-xs-6">
            <div class="form-check-label" aria-label="premise-1-cytology">
                <span class="check-square"></span>Salutation
            </div>
        </td>
        <td>
            <div class="col-xs-6 col-md-6">
                <span class="newVal" attr="${person.salutation}">
                    <iais:code code="${person.salutation}"/>
                </span>
            </div>
            <div class="col-xs-6 col-md-6">
                <span class=" oldVal" attr="${oldPerson.salutation}" style="display: none">
                    <iais:code code="${oldPerson.salutation}"/>
                </span>
            </div>
        </td>
    </tr>

    <tr>
        <td class="col-xs-6">
            <div class="form-check-label" aria-label="premise-1-cytology">
                <span class="check-square"></span>Name
            </div>
        </td>
        <td>
            <div class="col-xs-6 img-show">
                <div class="newVal" attr="<c:out value="${person.name}" />">
                    <c:out value="${person.name}"/>
                    <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/nameDisciplinaryRecordMark.jsp">
                        <jsp:param name="profRegNo" value="${person.profRegNo}"/>
                        <jsp:param name="personName" value="${person.name}"/>
                        <jsp:param name="methodName" value="showThisNameTableNewService"/>
                    </jsp:include>
                </div>
            </div>
            <div class="col-xs-6 img-show">
                <div class="oldVal" attr="<c:out value="${oldPerson.name}" />" style="display: none">
                    <c:out value="${oldPerson.name}"/>
                    <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/nameDisciplinaryRecordMark.jsp">
                        <jsp:param name="profRegNo" value="${oldPerson.profRegNo}"/>
                        <jsp:param name="personName" value="${oldPerson.name}"/>
                        <jsp:param name="methodName" value="showThisNameTableOldService"/>
                    </jsp:include>
                </div>
            </div>
            <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/nameDisciplinaryRecords.jsp">
                <jsp:param name="profRegNo" value="${person.profRegNo}"/>
                <jsp:param name="cssClass" value="new-img-show"/>
            </jsp:include>
            <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/nameDisciplinaryRecords.jsp">
                <jsp:param name="profRegNo" value="${oldPerson.profRegNo}"/>
                <jsp:param name="cssClass" value="old-img-show"/>
            </jsp:include>
        </td>
    </tr>

    <tr>
        <td class="col-xs-6">
            <div class="form-check-label" aria-label="premise-1-cytology">
                <span class="check-square"></span>ID Type
            </div>
        </td>
        <td>
            <div class="col-xs-6">
                <span class="newVal" attr="${person.idType}">
                    <iais:code code="${person.idType}"/>
                </span>
            </div>
            <div class="col-xs-6">
                <span class=" oldVal" attr="${oldPerson.idType}" style="display: none">
                  <iais:code code="${oldPerson.idType}"/>
                </span>
            </div>
        </td>
    </tr>

    <tr>
        <td class="col-xs-6">
            <div class="form-check-label" aria-label="premise-1-cytology">
                <span class="check-square"></span>ID No.
            </div>
        </td>
        <td>
            <div class="col-xs-6 img-show">
                <div class="newVal" attr="${person.idNo}">
                    <c:out value="${person.idNo}"/>
                    <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/disciplinaryRecordMark.jsp">
                        <jsp:param name="idNo" value="${person.idNo}"/>
                        <jsp:param name="methodName" value="showThisTableNewService"/>
                    </jsp:include>
                </div>
            </div>

            <div class="col-xs-6 img-show">
                <div class="oldVal" attr="${oldPerson.idNo}" style="display: none">
                    <c:out value="${oldPerson.idNo}"/>
                    <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/disciplinaryRecordMark.jsp">
                        <jsp:param name="idNo" value="${oldPerson.idNo}"/>
                        <jsp:param name="methodName" value="showThisTableOldService"/>
                    </jsp:include>
                </div>
            </div>
            <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/disciplinaryRecords.jsp">
                <jsp:param name="idNo" value="${person.idNo}"/>
                <jsp:param name="cssClass" value="new-img-show"/>
            </jsp:include>
            <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/disciplinaryRecords.jsp">
                <jsp:param name="idNo" value="${oldPerson.idNo}"/>
                <jsp:param name="cssClass" value="old-img-show"/>
            </jsp:include>
        </td>
    </tr>
    <c:if test="${person.idType == 'IDTYPE003' || oldPerson.idType == 'IDTYPE003'}">
        <tr>
            <td class="col-xs-6">
                <div class="form-check-label" aria-label="premise-1-cytology">
                    <span class="check-square"></span>Nationality
                </div>
            </td>
            <td>
                <div class="col-xs-6 col-md-6">
                    <span class="newVal " attr="${person.nationality}">
                        <iais:code code="${person.nationality}"/>
                    </span>
                </div>
                <div class="col-xs-6 col-md-6">
                    <span class="oldVal " attr="${oldPerson.nationality}" style="display: none">
                        <iais:code code="${oldPerson.nationality}"/>
                    </span>
                </div>
            </td>
        </tr>
    </c:if>
    <c:if test="${!isMap}">
        <tr>
            <td class="col-xs-6">
                <div class="form-check-label" aria-label="premise-1-cytology">
                    <span class="check-square"></span>Designation
                </div>
            </td>
            <td>
                <div class="col-xs-6 col-md-6">
                <span class="newVal " attr="${person.designation}">
                    <iais:code code="${person.designation}"/>
                </span>
                </div>
                <div class="col-xs-6 col-md-6">
                <span class="oldVal " attr="${oldPerson.designation}" style="display: none">
                    <iais:code code="${oldPerson.designation}"/>
                </span>
                </div>
            </td>
        </tr>
        <c:if test="${person.designation=='DES999'||oldPerson.designation=='DES999'}">
            <tr>
                <td class="col-xs-6">
                    <div class="form-check-label" aria-label="premise-1-cytology">
                        <span class="check-square"></span>
                    </div>
                </td>
                <td>
                    <div class="col-xs-12 col-md-12">
                        <div class="newVal" attr="<c:out value="${person.otherDesignation}" />">
                            <c:out value="${person.otherDesignation}"/>
                        </div>
                        <div class="oldVal " attr="<c:out value="${oldPerson.otherDesignation}" />"
                             style="display: none">
                            <c:out value="${oldPerson.otherDesignation}"/>
                        </div>
                    </div>
                </td>
            </tr>
        </c:if>

        <tr>
            <td class="col-xs-6">
                <div class="form-check-label" aria-label="premise-1-cytology">
                    <span class="check-square"></span>Professional Board
                </div>
            </td>
            <td>
                <div class="col-xs-6">
                <span class="newVal " attr="${person.professionBoard}">
                    <iais:code code="${person.professionBoard}"/>
                </span>
                </div>
                <div class="col-xs-6">
                <span class="oldVal" attr="${oldPerson.professionBoard}" style="display: none">
                    <iais:code code="${oldPerson.professionBoard}"/>
                </span>
                </div>
            </td>
        </tr>

        <tr>
            <td class="col-xs-6">
                <div class="form-check-label" aria-label="premise-1-cytology">
                    <span class="check-square"></span>Professional Type
                </div>
            </td>
            <td>
                <div class="col-xs-6">
                <span class="newVal " attr="${person.professionType}">
                    <iais:code code="${person.professionType}"/>
                </span>
                </div>
                <div class="col-xs-6">
                <span class="oldVal" attr="${oldPerson.professionType}" style="display: none">
                    <iais:code code="${oldPerson.professionType}"/>
                </span>
                </div>
            </td>
        </tr>
        <tr>
            <td class="col-xs-6">
                <div class="form-check-label" aria-label="premise-1-cytology">
                    <span class="check-square"></span>Professional Regn. No.
                </div>
            </td>
            <td>
                <div class="col-xs-6 img-show">
                    <div class="newVal " attr="${person.profRegNo}">
                        <c:out value="${person.profRegNo}"/>
                        <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/prsDisciplinaryRecordMark.jsp">
                            <jsp:param name="profRegNo" value="${person.profRegNo}"/>
                            <jsp:param name="methodName" value="showThisTableNewService"/>
                        </jsp:include>
                    </div>
                </div>
                <div class="col-xs-6 img-show">
                    <div class="oldVal " attr="${oldPerson.profRegNo}" style="display: none">
                        <c:out value="${oldPerson.profRegNo}"/>
                        <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/prsDisciplinaryRecordMark.jsp">
                            <jsp:param name="profRegNo" value="${oldPerson.profRegNo}"/>
                            <jsp:param name="methodName" value="showThisTableOldService"/>
                        </jsp:include>
                    </div>
                </div>
                <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/prsDisciplinaryRecords.jsp">
                    <jsp:param name="profRegNo" value="${person.profRegNo}"/>
                    <jsp:param name="cssClass" value="new-img-show"/>
                </jsp:include>
                <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/prsDisciplinaryRecords.jsp">
                    <jsp:param name="profRegNo" value="${oldPerson.profRegNo}"/>
                    <jsp:param name="cssClass" value="old-img-show"/>
                </jsp:include>
            </td>
        </tr>
        <tr>
            <td class="col-xs-6">
                <div class="form-check-label" aria-label="premise-1-cytology">
                    <span class="check-square"></span>Type of Current Registration
                </div>
            </td>
            <td>
                <div class="col-xs-6">
                    <div class="newVal " attr="<c:out value="${person.typeOfCurrRegi}" />">
                        <c:out value="${person.typeOfCurrRegi}"/>
                    </div>
                    <div class="oldVal " attr="<c:out value="${oldPerson.typeOfCurrRegi}" />" style="display: none">
                        <c:out value="${oldPerson.typeOfCurrRegi}"/>
                    </div>
                </div>
            </td>
        </tr>
        <tr>
            <td class="col-xs-6">
                <div class="form-check-label" aria-label="premise-1-cytology">
                    <span class="check-square"></span>Current Registration Date
                </div>
            </td>
            <td>
                <div class="col-xs-6">
                    <div class="newVal " attr="<c:out value="${person.currRegiDateStr}" />">
                       <c:out value="${person.currRegiDateStr}"/>
                    </div>
                </div>
                <div class="col-xs-6">
                    <div class="oldVal " attr="<c:out value="${oldPerson.currRegiDateStr}" />" style="display: none">
                        <c:out value="${oldPerson.currRegiDateStr}"/>
                    </div>
                </div>
            </td>
        </tr>
        <tr>
            <td class="col-xs-6">
                <div class="form-check-label" aria-label="premise-1-cytology">
                    <span class="check-square"></span>Practicing Certificate End Date
                </div>
            </td>
            <td>
                <div class="col-xs-6">
                    <div class="newVal " attr="${person.praCerEndDateStr}">
                       <c:out value="${person.praCerEndDateStr}"/>
                    </div>
                </div>
                <div class="col-xs-6">
                    <div class="oldVal " attr="${oldPerson.praCerEndDateStr}" style="display: none">
                        <c:out value="${oldPerson.praCerEndDateStr}"/>
                    </div>
                </div>
            </td>
        </tr>
        <tr>
            <td class="col-xs-6">
                <div class="form-check-label" aria-label="premise-1-cytology">
                    <span class="check-square"></span>Type of Register
                </div>
            </td>
            <td>
                <div class="col-xs-6">
                    <div class="newVal " attr="<c:out value="${person.typeOfRegister}" />">
                       <c:out value="${person.typeOfRegister}"/>
                    </div>
                </div>
                <div class="col-xs-6">
                    <div class="oldVal " attr="<c:out value="${oldPerson.typeOfRegister}" />" style="display: none">
                        <c:out value="${oldPerson.typeOfRegister}"/>
                    </div>
                </div>
            </td>
        </tr>
        <tr>
            <td class="col-xs-6">
                <div class="form-check-label" aria-label="premise-1-cytology">
                    <span class="check-square"></span>Specialty
                </div>
            </td>
            <td>
                <div class="col-xs-6">
                    <div class="newVal " attr="${person.speciality}">
                       <c:out value="${person.speciality}"/>
                    </div>
                </div>
                <div class="col-xs-6">
                    <div class="oldVal " attr="${oldPerson.speciality}" style="display: none">
                        <c:out value="${oldPerson.speciality}"/>
                    </div>
                </div>
            </td>
        </tr>
        <tr>
            <td class="col-xs-6">
                <div class="form-check-label" aria-label="premise-1-cytology">
                    <span class="check-square"></span>Sub-specialty
                </div>
            </td>
            <td>
                <div class="col-xs-6">
                    <div class="newVal " attr="<c:out value="${person.subSpeciality}" />">
                        <c:out value="${person.subSpeciality}"/>
                    </div>
                </div>
                <div class="col-xs-6">
                    <div class="oldVal " attr="<c:out value="${oldPerson.subSpeciality}" />" style="display: none">
                        <c:out value="${oldPerson.subSpeciality}"/>
                    </div>
                </div>
            </td>
        </tr>
        <tr>
            <td class="col-xs-6">
                <div class="form-check-label" aria-label="premise-1-cytology">
                    <span class="check-square"></span>Other Specialities
                </div>
            </td>
            <td>
                <div class="col-xs-6">
                    <div class="newVal " attr="<c:out value="${person.specialityOther}" />">
                        <c:out value="${person.specialityOther}"/>
                    </div>
                </div>
                <div class="col-xs-6">
                    <div class="oldVal " attr="<c:out value="${oldPerson.specialityOther}" />" style="display: none">
                        <c:out value="${oldPerson.specialityOther}"/>
                    </div>
                </div>
            </td>
        </tr>
        <tr>
            <td class="col-xs-6">
                <div class="form-check-label" aria-label="premise-1-cytology">
                    <span class="check-square"></span>Date when specialty was obtained
                </div>
            </td>
            <td>
                <div class="col-xs-6">
                    <div class="newVal " attr="${person.specialtyGetDateStr}">
                        <c:out value="${person.specialtyGetDateStr}"/>
                    </div>
                </div>
                <div class="col-xs-6">
                    <div class="oldVal " attr="${oldPerson.specialtyGetDateStr}" style="display: none">
                        <c:out value="${oldPerson.specialtyGetDateStr}"/>
                    </div>
                </div>
            </td>
        </tr>
        <tr>
            <td class="col-xs-6">
                <div class="form-check-label" aria-label="premise-1-cytology">
                    <span class="check-square"></span>Qualification
                </div>
            </td>
            <td>
                <div class="col-xs-6">
                    <div class="newVal " attr="<c:out value="${person.qualification}"/>">
                        <c:out value="${person.qualification}"/>
                    </div>
                </div>
                <div class="col-xs-6">
                    <div class="oldVal " attr="<c:out value="${oldPerson.qualification}"/>" style="display: none">
                        <c:out value="${oldPerson.qualification}"/>
                    </div>
                </div>
            </td>
        </tr>
        <tr>
            <td class="col-xs-6">
                <div class="form-check-label" aria-label="premise-1-cytology">
                    <span class="check-square"></span>Other Qualification
                </div>
            </td>
            <td>
                <div class="col-xs-6">
                    <div class="newVal " attr="<c:out value="${person.otherQualification}"/>">
                        <c:out value="${person.otherQualification}"/>
                    </div>
                </div>
                <div class="col-xs-6">
                    <div class="oldVal " attr="<c:out value="${oldPerson.otherQualification}"/>" style="display: none">
                        <c:out value="${oldPerson.otherQualification}"/>
                    </div>
                </div>
            </td>
        </tr>
    </c:if>
    <c:if test="${officeTelNo} == 'officeTelNo'">
    <tr>
        <td class="col-xs-6">
            <div class="form-check-label" aria-label="premise-1-cytology">
                <span class="check-square"></span>Office Telephone No.
            </div>
        </td>
        <td>
            <div class="col-xs-6">
                <span class="newVal " attr="${person.officeTelNo}">
                    <c:out value="${person.officeTelNo}"/>
                </span>
            </div>
            <div class="col-xs-6">
                <span class="oldVal " attr="${oldPerson.officeTelNo}" style="display: none">
                    <c:out value="${oldPerson.officeTelNo}"/>
                </span>
            </div>
        </td>
    </tr>
    </c:if>

    <c:if test="${keyPerson != 'keyPerson'}">
        <tr>
            <td class="col-xs-6">
                <div class="form-check-label" aria-label="premise-1-cytology">
                    <span class="check-square"></span>Mobile No.
                </div>
            </td>
            <td>
                <div class="col-xs-6">
                    <div class="newVal " attr="${person.mobileNo}">
                        <c:out value="${person.mobileNo}"/>
                    </div>
                </div>
                <div class="col-xs-6">
                    <div class="oldVal " attr="${oldPerson.mobileNo}" style="display: none">
                        <c:out value="${oldPerson.mobileNo}"/>
                    </div>
                </div>
            </td>
        </tr>

        <tr>
            <td class="col-xs-6">
                <div class="form-check-label" aria-label="premise-1-cytology">
                    <span class="check-square"></span>Email Address
                </div>
            </td>
            <td>
                <div class="col-xs-6">
                    <div class="newVal " attr="${person.emailAddr}">
                        <c:out value="${person.emailAddr}"/>
                    </div>
                </div>
                <div class="col-xs-6">
                    <div class="oldVal " attr="${oldPerson.emailAddr}" style="display: none">
                        <c:out value="${oldPerson.emailAddr}"/>
                    </div>
                </div>
            </td>
        </tr>
    </c:if>
</table>