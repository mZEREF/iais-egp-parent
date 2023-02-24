<table aria-describedby="" class="col-xs-12">
    <tr>
        <th scope="col" style="display: none"></th>
        <td class="col-xs-6">
            <div class="form-check-label" aria-label="premise-1-cytology">
                <span class="check-square"></span>Professional Regn. No.
            </div>
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
                <jsp:param name="profRegNo" value="${practitioners.profRegNo}"/>
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
            <div class="form-check-label" aria-label="premise-1-cytology">
                <span class="check-square"></span>NRIC/FIN No.
            </div>
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
            <div class="form-check-label" aria-label="premise-1-cytology">
                <span class="check-square"></span>Type of Registration
            </div>
        </td>
        <td>
            <div class="col-xs-6 ">
                <span class="newVal" attr="${practitioners.regType}">
                   <c:out value="${practitioners.regType}"/>
                </span>
            </div>
            <div class="col-xs-6 ">
                <span class=" oldVal" attr="${oldPractitioners.regType}" style="display: none">
                     <c:out value="${oldPractitioners.regType}"/>
                </span>
            </div>
        </td>
    </tr>

    <tr>
        <th scope="col" style="display: none"></th>
        <td class="col-xs-6">
            <div class="form-check-label" aria-label="premise-1-cytology">
                <span class="check-square"></span>Name of medical practitioner
            </div>
        </td>
        <td>
            <div class="col-xs-6 ">
                <span class="newVal" attr="${practitioners.name}">
                   <c:out value="${practitioners.name}"/>
                </span>
            </div>
            <div class="col-xs-6 ">
                <span class=" oldVal" attr="${oldPractitioners.name}" style="display: none">
                     <c:out value="${oldPractitioners.name}"/>
                </span>
            </div>
        </td>
    </tr>

    <tr>
        <th scope="col" style="display: none"></th>
        <td class="col-xs-6">
            <div class="form-check-label" aria-label="premise-1-cytology">
                <span class="check-square"></span>Specialties
            </div>
        </td>
        <td>
            <div class="col-xs-6 ">
                <span class="newVal" attr="${practitioners.speciality}">
                   <c:out value="${practitioners.speciality}"/>
                </span>
            </div>
            <div class="col-xs-6 ">
                <span class=" oldVal" attr="${oldPractitioners.speciality}" style="display: none">
                     <c:out value="${oldPractitioners.speciality}"/>
                </span>
            </div>
        </td>
    </tr>

    <tr>
        <th scope="col" style="display: none"></th>
        <td class="col-xs-6">
            <div class="form-check-label" aria-label="premise-1-cytology">
                <span class="check-square"></span>Qualifications
            </div>
        </td>
        <td>
            <div class="col-xs-6 ">
                <span class="newVal" attr="${practitioners.qualification}">
                   <c:out value="${practitioners.qualification}"/>
                </span>
            </div>
            <div class="col-xs-6 ">
                <span class=" oldVal" attr="${oldPractitioners.qualification}" style="display: none">
                     <c:out value="${oldPractitioners.qualification}"/>
                </span>
            </div>
        </td>
    </tr>

    <tr>
        <th scope="col" style="display: none"></th>
        <td class="col-xs-6">
            <div class="form-check-label longWord" aria-label="premise-1-cytology">
                <span class="check-square"></span>
                Is the medical practitioners authorised by MOH to perform Abortion
                (if No, please upload a copy of the Obstetrics & Gynaecology certificate
                and TOP FORM II at the Document page)
            </div>
        </td>
        <td>
            <div class="col-xs-6 ">
                <span class="newVal" attr="${practitioners.medAuthByMoh}">
                    <c:if test="${true eq practitioners.medAuthByMoh}">Yes</c:if>
                    <c:if test="${false eq practitioners.medAuthByMoh}">No</c:if>
                </span>
            </div>
            <div class="col-xs-6 ">
                <span class=" oldVal" attr="${oldPractitioners.medAuthByMoh}" style="display: none">
                     <c:if test="${true eq oldPractitioners.medAuthByMoh}">Yes</c:if>
                    <c:if test="${false eq oldPractitioners.medAuthByMoh}">No</c:if>
                </span>
            </div>
        </td>
    </tr>
</table>
