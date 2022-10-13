<table aria-describedby="" class="col-xs-12">
    <tr>
        <th scope="col" style="display: none"></th>
        <td class="col-xs-6">
            <p class="form-check-label" aria-label="premise-1-cytology">
                <span class="check-square"></span>Professional Regn. No.
            </p>
        </td>
        <td>
            <div class="col-xs-6 img-show">
                <div class="newVal " attr="${practitioners.profRegNo}">
                    <c:out value="${practitioners.profRegNo}"/>
                    <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/prsDisciplinaryRecordMark.jsp">
                        <jsp:param name="profRegNo" value="${practitioners.profRegNo}"/>
                        <jsp:param name="methodName" value="showThisTableNewService"/>
                    </jsp:include>
                </div>
            </div>
            <div class="col-xs-6 img-show">
                <div class="oldVal " attr="${oldPractitioners.profRegNo}" style="display: none">
                    <c:out value="${oldPractitioners.profRegNo}"/>
                    <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/prsDisciplinaryRecordMark.jsp">
                        <jsp:param name="profRegNo" value="${oldPractitioners.profRegNo}"/>
                        <jsp:param name="methodName" value="showThisTableOldService"/>
                    </jsp:include>
                </div>
            </div>
            <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/prsDisciplinaryRecords.jsp">
                <jsp:param name="profRegNo" value="${oldPractitioners.profRegNo}"/>
                <jsp:param name="cssClass" value="new-img-show"/>
            </jsp:include>
            <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/prsDisciplinaryRecords.jsp">
                <jsp:param name="profRegNo" value="${oldPractitioners.profRegNo}"/>
                <jsp:param name="cssClass" value="old-img-show"/>
            </jsp:include>
        </td>
    </tr>

    <tr>
        <th scope="col" style="display: none"></th>
        <td class="col-xs-6">
            <p class="form-check-label" aria-label="premise-1-cytology">
                <span class="check-square"></span>NRIC/FIN No.
            </p>
        </td>
        <td>
            <div class="col-xs-6 img-show">
                <span class="newVal " attr="${practitioners.idNo}">
                    ${practitioners.idNo}
                        <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/disciplinaryRecordMark.jsp">
                            <jsp:param name="idNo" value="${practitioners.idNo}"/>
                            <jsp:param name="methodName" value="showThisTableNewService"/>
                        </jsp:include>
                </span>
            </div>
            <div class="col-xs-6 img-show">
                <span class="oldVal " style="display: none" attr="${oldPractitioners.idNo}">
                    ${oldPractitioners.idNo}
                    <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/disciplinaryRecordMark.jsp">
                        <jsp:param name="idNo" value="${oldPractitioners.idNo}"/>
                        <jsp:param name="methodName" value="showThisTableOldService"/>
                    </jsp:include>
                </span>
            </div>
            <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/disciplinaryRecords.jsp">
                <jsp:param name="idNo" value="${practitioners.idNo}"/>
                <jsp:param name="cssClass" value="new-img-show"/>
            </jsp:include>
            <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/disciplinaryRecords.jsp">
                <jsp:param name="idNo" value="${oldPractitioners.idNo}"/>
                <jsp:param name="cssClass" value="old-img-show"/>
            </jsp:include>
        </td>
    </tr>

    <tr>
        <th scope="col" style="display: none"></th>
        <td class="col-xs-6">
            <p class="form-check-label" aria-label="premise-1-cytology">
                <span class="check-square"></span>Type of Registration
            </p>
        </td>
        <td>
            <div class="col-xs-6 ">
                <span class="newVal" attr="${practitioners.regType}">
                   <iais:code code="${practitioners.regType}"></iais:code>
                </span>
            </div>
            <div class="col-xs-6 ">
                <span class=" oldVal" attr="${oldPractitioners.regType}" style="display: none">
                     <iais:code code="${oldPractitioners.regType}"></iais:code>
                </span>
            </div>
        </td>
    </tr>

    <tr>
        <th scope="col" style="display: none"></th>
        <td class="col-xs-6">
            <p class="form-check-label" aria-label="premise-1-cytology">
                <span class="check-square"></span>Name of medical practitioner
            </p>
        </td>
        <td>
            <div class="col-xs-6 ">
                <span class="newVal" attr="${practitioners.name}">
                   <iais:code code="${practitioners.name}"></iais:code>
                </span>
            </div>
            <div class="col-xs-6 ">
                <span class=" oldVal" attr="${oldPractitioners.name}" style="display: none">
                     <iais:code code="${oldPractitioners.name}"></iais:code>
                </span>
            </div>
        </td>
    </tr>

    <tr>
        <th scope="col" style="display: none"></th>
        <td class="col-xs-6">
            <p class="form-check-label" aria-label="premise-1-cytology">
                <span class="check-square"></span>Specialties
            </p>
        </td>
        <td>
            <div class="col-xs-6 ">
                <span class="newVal" attr="${practitioners.speciality}">
                   <iais:code code="${practitioners.speciality}"></iais:code>
                </span>
            </div>
            <div class="col-xs-6 ">
                <span class=" oldVal" attr="${oldPractitioners.speciality}" style="display: none">
                     <iais:code code="${oldPractitioners.speciality}"></iais:code>
                </span>
            </div>
        </td>
    </tr>

    <tr>
        <th scope="col" style="display: none"></th>
        <td class="col-xs-6">
            <p class="form-check-label" aria-label="premise-1-cytology">
                <span class="check-square"></span>Qualifications
            </p>
        </td>
        <td>
            <div class="col-xs-6 ">
                <span class="newVal" attr="${practitioners.qualification}">
                   <iais:code code="${practitioners.qualification}"></iais:code>
                </span>
            </div>
            <div class="col-xs-6 ">
                <span class=" oldVal" attr="${oldPractitioners.qualification}" style="display: none">
                     <iais:code code="${oldPractitioners.qualification}"></iais:code>
                </span>
            </div>
        </td>
    </tr>

    <tr>
        <th scope="col" style="display: none"></th>
        <td class="col-xs-6">
            <p class="form-check-label" aria-label="premise-1-cytology">
                <span class="check-square"></span>Is the medical practitioners authorised by MOH to perform Abortion
                (if No, please upload a copy of the Obstetrics & Gynaecology certificate and
                From 2 at the Document page)
            </p>
        </td>
        <td>
            <div class="col-xs-6 ">
                <span class="newVal" attr="${practitioners.medAuthByMoh}">
                    <c:if test="${'1' == practitioners.medAuthByMoh}">Yes</c:if>
                    <c:if test="${'0' == practitioners.medAuthByMoh}">No</c:if>
                </span>
            </div>
            <div class="col-xs-6 ">
                <span class=" oldVal" attr="${oldPractitioners.medAuthByMoh}" style="display: none">
                     <c:if test="${'1' == oldPractitioners.medAuthByMoh}">Yes</c:if>
                    <c:if test="${'0' == oldPractitioners.medAuthByMoh}">No</c:if>
                </span>
            </div>
        </td>
    </tr>
</table>
