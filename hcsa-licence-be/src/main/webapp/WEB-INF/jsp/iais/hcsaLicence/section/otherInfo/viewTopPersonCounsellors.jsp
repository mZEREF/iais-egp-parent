<table aria-describedby="" class="col-xs-12">
    <tr>
        <th scope="col" style="display: none"></th>
        <td class="col-xs-6">
            <p class="form-check-label" aria-label="premise-1-cytology">
                <span class="check-square"></span>Name of certified TOP counsellors(Only Doctor/Nurse)
            </p>
        </td>
        <td>
            <div class="col-xs-6 ">
                <span class="newVal" attr="${counsellors.name}">
                   <iais:code code="${counsellors.name}"></iais:code>
                </span>
            </div>
            <div class="col-xs-6 ">
                <span class=" oldVal" attr="${oldCounsellors.name}" style="display: none">
                     <iais:code code="${oldCounsellors.name}"></iais:code>
                </span>
            </div>
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
                <span class="newVal " attr="${counsellors.idNo}">
                    ${counsellors.idNo}
                        <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/disciplinaryRecordMark.jsp">
                            <jsp:param name="idNo" value="${counsellors.idNo}"/>
                            <jsp:param name="methodName" value="showThisTableNewService"/>
                        </jsp:include>
                </span>
            </div>
            <div class="col-xs-6 img-show">
                <span class="oldVal " style="display: none" attr="${oldCounsellors.idNo}">
                    ${oldAnaesthetists.idNo}
                    <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/disciplinaryRecordMark.jsp">
                        <jsp:param name="idNo" value="${oldCounsellors.idNo}"/>
                        <jsp:param name="methodName" value="showThisTableOldService"/>
                    </jsp:include>
                </span>
            </div>
            <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/disciplinaryRecords.jsp">
                <jsp:param name="idNo" value="${counsellors.idNo}"/>
                <jsp:param name="cssClass" value="new-img-show"/>
            </jsp:include>
            <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/disciplinaryRecords.jsp">
                <jsp:param name="idNo" value="${oldCounsellors.idNo}"/>
                <jsp:param name="cssClass" value="old-img-show"/>
            </jsp:include>
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
                <span class="newVal" attr="${counsellors.qualification}">
                   <iais:code code="${counsellors.qualification}"></iais:code>
                </span>
            </div>
            <div class="col-xs-6 ">
                <span class=" oldVal" attr="${oldCounsellors.qualification}" style="display: none">
                     <iais:code code="${oldCounsellors.qualification}"></iais:code>
                </span>
            </div>
        </td>
    </tr>

    <tr>
        <th scope="col" style="display: none"></th>
        <td class="col-xs-6">
            <p class="form-check-label" aria-label="premise-1-cytology">
                <span class="check-square"></span>My counsellor(s) has attended the TOP counselling refresher course (Please upload the certificates in the document page)
            </p>
        </td>
        <td>
            <div class="col-xs-6 ">
                <span class="newVal" attr="${otherInfo.appSvcOtherInfoTopDto.hasConsuAttendCourse}">
                    <c:if test="${true == otherInfo.appSvcOtherInfoTopDto.hasConsuAttendCourse}">Yes</c:if>
                    <c:if test="${false == otherInfo.appSvcOtherInfoTopDto.hasConsuAttendCourse}">No</c:if>
                </span>
            </div>
            <div class="col-xs-6 ">
                <span class=" oldVal" attr="${oldOtherInfo.appSvcOtherInfoTopDto.hasConsuAttendCourse}" style="display: none">
                    <c:if test="${true == oldOtherInfo.appSvcOtherInfoTopDto.hasConsuAttendCourse}">Yes</c:if>
                    <c:if test="${false == oldOtherInfo.appSvcOtherInfoTopDto.hasConsuAttendCourse}">No</c:if>
                </span>
            </div>
        </td>
    </tr>
</table>
