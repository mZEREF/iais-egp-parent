<div class="row">
    <table aria-describedby="" class="col-xs-12">
        <tr>
            <div class="col-xs-12">
                <p class="bold">${title}</p>
            </div>
        </tr>
        <tr>
            <th scope="col" style="display: none"></th>
            <td class="col-xs-6">
                <div class="form-check-label" aria-label="premise-1-cytology">
                    <span class="check-square"></span>Salutation
                </div>
            </td>
            <td>
                <div class="col-xs-6 col-md-6">
                    <div class="newVal" attr="${person.salutation}">
                        <iais:code code="${person.salutation}"/>
                    </div>
                </div>
                <div class="col-xs-6 col-md-6">
                    <div class=" oldVal" attr="${oldPerson.salutation}" style="display: none">
                        <iais:code code="${oldPerson.salutation}"/>
                    </div>
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
                    <span class="check-square"></span>Designation
                </div>
            </td>
            <td>
                <div class="col-xs-6 col-md-6">
                    <div class="newVal " attr="${person.designation}">
                        <iais:code code="${person.designation}"/>
                    </div>
                </div>
                <div class="col-xs-6 col-md-6">
                    <div class="oldVal " attr="${oldPerson.designation}" style="display: none">
                        <iais:code code="${oldPerson.designation}"/>
                    </div>
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
                    <div class="col-xs-6">
                        <div class="newVal" attr="<c:out value="${person.otherDesignation}" />">
                            <c:out value="${person.otherDesignation}"/>
                        </div>
                    </div>
                    <div class="col-xs-6">
                        <div class="oldVal " attr="<c:out value="${oldPerson.otherDesignation}" />" style="display: none">
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
                        <c:out value="${person.typeOfCurrRegi}" />
                    </div>
                </div>
                <div class="col-xs-6">
                    <div class="oldVal " attr="<c:out value="${oldPerson.typeOfCurrRegi}" />" style="display: none">
                        <c:out value="${oldPerson.typeOfCurrRegi}" />
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
                    <div class="newVal " attr="<c:out value="${person.currRegiDate}" />">
                       <c:out value="${person.currRegiDate}" />
                    </div>
                </div>
                <div class="col-xs-6">
                    <div class="oldVal " attr="<c:out value="${oldPerson.currRegiDate}" />" style="display: none">
                        <c:out value="${oldPerson.currRegiDate}" />
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
                    <div class="newVal " attr="${person.praCerEndDate}">
                       <c:out value="${person.praCerEndDate}" />
                    </div>
                </div>
                <div class="col-xs-6">
                    <div class="oldVal " attr="${oldPerson.praCerEndDate}" style="display: none">
                        <c:out value="${oldPerson.praCerEndDate}" />
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
                       <c:out value="${person.typeOfRegister}" />
                    </div>
                </div>
                <div class="col-xs-6">
                    <div class="oldVal " attr="<c:out value="${oldPerson.typeOfRegister}" />" style="display: none">
                        <c:out value="${oldPerson.typeOfRegister}" />
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
                        <c:out value="${oldPerson.subSpeciality}" />
                    </div>
                </div>
            </td>
        </tr>
        <tr>
            <td class="col-xs-6">
                <div class="form-check-label" aria-label="premise-1-cytology">
                    <span class="check-square"></span>Other Specialties
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
                        <c:out value="${oldPerson.specialityOther}" />
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
                    <div class="newVal " attr="${person.specialtyGetDate}">
                        <c:out value="${person.specialtyGetDate}"/>
                    </div>
                </div>
                <div class="col-xs-6">
                    <div class="oldVal " attr="${oldPerson.specialtyGetDate}" style="display: none">
                        <c:out value="${oldPerson.specialtyGetDate}" />
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
                    <span class="check-square"></span>Relevant working experience(Years)
                </div>
            </td>
            <td>
                <div class="col-xs-6">
                    <div class="newVal " attr="<c:out value="${person.wrkExpYear}"/>">
                        <c:out value="${person.wrkExpYear}"/>
                    </div>
                </div>
                <div class="col-xs-6">
                    <div class="oldVal " attr="<c:out value="${oldPerson.wrkExpYear}"/>" style="display: none">
                        <c:out value="${oldPerson.wrkExpYear}"/>
                    </div>
                </div>
            </td>
        </tr>

        <c:if test="${isShowMore==1}">
            <tr>
                <td class="col-xs-6">
                    <div class="form-check-label" aria-label="premise-1-cytology">
                        <span class="check-square"></span>Expiry Date (BCLS and AED)
                    </div>
                </td>
                <td>
                    <div class="col-xs-6">
                        <div class="newVal " attr="<c:out value="${person.bclsExpiryDate}"/>">
                            <c:out value="${person.bclsExpiryDate}"/>
                        </div>
                    </div>
                    <div class="col-xs-6">
                        <div class="oldVal " attr="<c:out value="${oldPerson.bclsExpiryDate}"/>" style="display: none">
                            <c:out value="${oldPerson.bclsExpiryDate}"/>
                        </div>
                    </div>
                </td>
            </tr>
        </c:if>
    </table>
</div>