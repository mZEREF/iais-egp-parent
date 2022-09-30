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
                <div class="newVal " attr="${anaesthetists.profRegNo}">
                    <c:out value="${anaesthetists.profRegNo}"/>
                    <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/prsDisciplinaryRecordMark.jsp">
                        <jsp:param name="profRegNo" value="${anaesthetists.profRegNo}"/>
                        <jsp:param name="methodName" value="showThisTableNewService"/>
                    </jsp:include>
                </div>
            </div>
            <div class="col-xs-6 img-show">
                <div class="oldVal " attr="${oldAnaesthetists.profRegNo}" style="display: none">
                    <c:out value="${oldAnaesthetists.profRegNo}"/>
                    <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/prsDisciplinaryRecordMark.jsp">
                        <jsp:param name="profRegNo" value="${oldAnaesthetists.profRegNo}"/>
                        <jsp:param name="methodName" value="showThisTableOldService"/>
                    </jsp:include>
                </div>
            </div>
            <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/prsDisciplinaryRecords.jsp">
                <jsp:param name="profRegNo" value="${anaesthetists.profRegNo}"/>
                <jsp:param name="cssClass" value="new-img-show"/>
            </jsp:include>
            <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/prsDisciplinaryRecords.jsp">
                <jsp:param name="profRegNo" value="${oldAnaesthetists.profRegNo}"/>
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
                <span class="newVal " attr="${anaesthetists.idNo}">
                    ${practitioners.idNo}
                        <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/disciplinaryRecordMark.jsp">
                            <jsp:param name="idNo" value="${anaesthetists.idNo}"/>
                            <jsp:param name="methodName" value="showThisTableNewService"/>
                        </jsp:include>
                </span>
            </div>
            <div class="col-xs-6 img-show">
                <span class="oldVal " style="display: none" attr="${oldAnaesthetists.idNo}">
                    ${oldAnaesthetists.idNo}
                    <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/disciplinaryRecordMark.jsp">
                        <jsp:param name="idNo" value="${oldAnaesthetists.idNo}"/>
                        <jsp:param name="methodName" value="showThisTableOldService"/>
                    </jsp:include>
                </span>
            </div>
            <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/disciplinaryRecords.jsp">
                <jsp:param name="idNo" value="${anaesthetists.idNo}"/>
                <jsp:param name="cssClass" value="new-img-show"/>
            </jsp:include>
            <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/disciplinaryRecords.jsp">
                <jsp:param name="idNo" value="${oldAnaesthetists.idNo}"/>
                <jsp:param name="cssClass" value="old-img-show"/>
            </jsp:include>
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
                <span class="newVal" attr="${anaesthetists.name}">
                   <iais:code code="${anaesthetists.name}"></iais:code>
                </span>
            </div>
            <div class="col-xs-6 ">
                <span class=" oldVal" attr="${oldAnaesthetists.name}" style="display: none">
                     <iais:code code="${oldAnaesthetists.name}"></iais:code>
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
                <span class="newVal" attr="${anaesthetists.qualification}">
                   <iais:code code="${anaesthetists.qualification}"></iais:code>
                </span>
            </div>
            <div class="col-xs-6 ">
                <span class=" oldVal" attr="${oldAnaesthetists.qualification}" style="display: none">
                     <iais:code code="${oldAnaesthetists.qualification}"></iais:code>
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
                <span class="newVal" attr="${practitioners.isMedAuthByMoh}">
                    <c:if test="${'1' == practitioners.isMedAuthByMoh}">Yes</c:if>
                    <c:if test="${'0' == practitioners.isMedAuthByMoh}">No</c:if>
                </span>
            </div>
            <div class="col-xs-6 ">
                <span class=" oldVal" attr="${oldPractitioners.isMedAuthByMoh}" style="display: none">
                     <c:if test="${'1' == oldPractitioners.isMedAuthByMoh}">Yes</c:if>
                    <c:if test="${'0' == oldPractitioners.isMedAuthByMoh}">No</c:if>
                </span>
            </div>
        </td>
    </tr>
</table>
