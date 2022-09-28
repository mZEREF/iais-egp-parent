<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<div class="row">
    <div>
        <c:forEach items="${currentPreviewSvcInfo.svcPersonnelDto.nurseList}" var="nurseList" varStatus="status">
            <c:set var="oldNurseList"
                   value="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.svcPersonnelDto.nurseList[status.index]}"/>
            <p>
                <strong class="col-xs-6">
                    Nurse
                    <c:if test="${fn:length(currentPreviewSvcInfo.svcPersonnelDto.nurseList)>1}">
                        ${status.index+1}
                    </c:if>:
                </strong>
            </p>
            <span class="col-xs-6"></span>
            <table aria-describedby="" class="col-xs-12">
                    <%--                Salutation--%>
                <tr>
                    <td class="col-xs-6">
                        <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Salutation
                        </p>
                    </td>
                    <td>
                        <div class="col-xs-6">
                            <div class="newVal " attr="${nurseList.salutation}">
                                <iais:code code="${nurseList.salutation}"/>
                            </div>
                        </div>
                        <div class="col-xs-6">
                            <div class="oldVal " attr="${oldNurseList.salutation}" style="display: none">
                                <iais:code code="${oldNurseList.salutation}"/>
                            </div>
                        </div>
                    </td>
                </tr>

                    <%--                Name--%>
                <tr>
                    <td class="col-xs-6">
                        <p class="form-check-label" aria-label="premise-1-cytology">
                            Name
                        </p>
                    </td>
                    <td>
                        <div class="col-xs-6 img-show">
                            <div class="newVal " attr="${nurseList.name}">
                                <c:out value="${nurseList.name}"/>
                                <jsp:include
                                        page="/WEB-INF/jsp/iais/hcsaLicence/section/nameDisciplinaryRecordMark.jsp">
                                    <jsp:param name="profRegNo" value="${nurseList.profRegNo}"/>
                                    <jsp:param name="personName" value="${nurseList.name}"/>
                                    <jsp:param name="methodName" value="showThisNameTableNewService"/>
                                </jsp:include>
                            </div>
                        </div>
                        <div class="col-xs-6 img-show">
                            <div class="oldVal "
                                 attr="${oldNurseList.name}"
                                 style="display: none">${oldNurseList.name}
                                <jsp:include
                                        page="/WEB-INF/jsp/iais/hcsaLicence/section/nameDisciplinaryRecordMark.jsp">
                                    <jsp:param name="profRegNo" value="${oldNurseList.profRegNo}"/>
                                    <jsp:param name="personName" value="${oldNurseList.name}"/>
                                    <jsp:param name="methodName" value="showThisNameTableOldService"/>
                                </jsp:include>
                            </div>
                        </div>
                        <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/nameDisciplinaryRecords.jsp">
                            <jsp:param name="profRegNo" value="${nurseList.profRegNo}"/>
                            <jsp:param name="cssClass" value="new-img-show"/>
                        </jsp:include>
                        <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/nameDisciplinaryRecords.jsp">
                            <jsp:param name="profRegNo" value="${oldNurseList.profRegNo}"/>
                            <jsp:param name="cssClass" value="old-img-show"/>
                        </jsp:include>
                    </td>
                </tr>

                    <%--                designation--%>
                <tr>
                    <td class="col-xs-6">
                        <p class="form-check-label" aria-label="premise-1-cytology">
                            Designation
                        </p>
                    </td>
                    <td>
                        <div class="col-xs-6">
                            <div class="newVal " attr="${nurseList.designation}">
                                <iais:code code="${nurseList.designation}"/>
                            </div>
                        </div>
                        <div class="col-xs-6">
                            <div class="oldVal " attr="${oldNurseList.designation}" style="display: none">
                                <iais:code code="${oldNurseList.designation}"/>
                            </div>
                        </div>
                    </td>
                </tr>
                    <%--    otherdesignation--%>
                <c:if test="${'DES999' == nurseList.designation || 'DES999' == oldNurseList.designation}">
                    <tr>
                        <td class="col-xs-6">
                            <p class="form-check-label" aria-label="premise-1-cytology">
                                OtherDesignation
                            </p>
                        </td>
                        <td>
                            <div class="col-xs-6">
                                <div class="newVal " attr="${nurseList.otherDesignation}">
                                        ${nurseList.otherDesignation}
                                </div>
                            </div>

                            <div class="col-xs-6">
                                <div class="oldVal " attr="${oldNurseList.otherDesignation}" style="display: none">
                                        ${oldNurseList.otherDesignation}
                                </div>
                            </div>

                        </td>
                    </tr>
                </c:if>


                    <%--                       Professional Board--%>
                <tr>
                    <td class="col-xs-6">
                        <p class="form-check-label" aria-label="premise-1-cytology">
                            Professional Board
                        </p>
                    </td>
                    <td>
                        <div class="col-xs-6">
                            <div class="newVal " attr="${nurseList.professionBoard}">
                                <iais:code code="${nurseList.professionBoard}"/>
                            </div>
                        </div>
                        <div class="col-xs-6">
                            <div class="oldVal " attr="${oldNurseList.professionBoard}" style="display: none">
                                <iais:code code="${oldNurseList.professionBoard}"/>
                            </div>
                        </div>
                    </td>
                </tr>

                    <%--                        Professional Type--%>
                <tr>
                    <td class="col-xs-6">
                        <p class="form-check-label" aria-label="premise-1-cytology">
                            Professional Type
                        </p>
                    </td>
                    <td>
                        <div class="col-xs-6">
                            <div class="newVal " attr="${nurseList.professionType}">
                                <iais:code code="${nurseList.professionType}"/>
                            </div>
                        </div>
                        <div class="col-xs-6">
                            <div class="oldVal " attr="${oldNurseList.professionType}" style="display: none">
                                <iais:code code="${oldNurseList.professionType}"/>
                            </div>
                        </div>
                    </td>
                </tr>

                    <%--    No--%>
                <tr>
                    <td class="col-xs-6">
                        <p class="form-check-label" aria-label="premise-1-cytology">
                            Professional Regn. No.
                        </p>
                    </td>
                    <td>
                        <div class="col-xs-6 img-show">
                            <div class="newVal " attr="${nurseList.profRegNo}">
                                <c:out value="${nurseList.profRegNo}"/>
                                <jsp:include
                                        page="/WEB-INF/jsp/iais/hcsaLicence/section/prsDisciplinaryRecordMark.jsp">
                                    <jsp:param name="profRegNo" value="${nurseList.profRegNo}"/>
                                    <jsp:param name="methodName" value="showThisTableNewService"/>
                                </jsp:include>
                            </div>
                        </div>
                        <div class="col-xs-6 img-show">
                            <div class="oldVal" attr="${oldNurseList.profRegNo}" style="display: none">
                                    ${oldNurseList.profRegNo}
                                <jsp:include
                                        page="/WEB-INF/jsp/iais/hcsaLicence/section/prsDisciplinaryRecordMark.jsp">
                                    <jsp:param name="profRegNo"
                                               value="${oldNurseList.profRegNo}"/>
                                    <jsp:param name="methodName" value="showThisTableOldService"/>
                                </jsp:include>
                            </div>
                        </div>
                        <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/prsDisciplinaryRecords.jsp">
                            <jsp:param name="profRegNo" value="${nurseList.profRegNo}"/>
                            <jsp:param name="cssClass" value="new-img-show"/>
                        </jsp:include>
                        <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/prsDisciplinaryRecords.jsp">
                            <jsp:param name="profRegNo" value="${oldNurseList.profRegNo}"/>
                            <jsp:param name="cssClass" value="old-img-show"/>
                        </jsp:include>
                    </td>
                </tr>


                    <%--    Type of Current Registration--%>
                <tr>
                    <td class="col-xs-6">
                        <p class="form-check-label" aria-label="premise-1-cytology">
                            Type of Current Registration
                        </p>
                    </td>
                    <td>
                        <div class="col-xs-6">
                            <div class="newVal " attr="${nurseList.typeOfCurrRegi}">
                                <c:out value="${nurseList.typeOfCurrRegi}"/>
                            </div>
                        </div>
                        <div class="col-xs-6">
                            <div class="oldVal " attr="${oldNurseList.typeOfCurrRegi}" style="display: none">
                                    ${oldNurseList.typeOfCurrRegi}
                            </div>
                        </div>
                    </td>
                </tr>

                    <%--    Current Registration Date--%>
                <tr>
                    <td class="col-xs-6">
                        <p class="form-check-label" aria-label="premise-1-cytology">
                            Current Registration Date
                        </p>
                    </td>
                    <td>
                        <div class="col-xs-6">
                            <div class="newVal " attr="${nurseList.currRegiDate}">
                                <c:out value="${nurseList.currRegiDate}"/>
                            </div>
                        </div>
                        <div class="col-xs-6">
                            <div class="oldVal " attr="${oldNurseList.currRegiDate}" style="display: none">
                                    ${oldNurseList.currRegiDate}
                            </div>
                        </div>
                    </td>
                </tr>

                    <%--    Practicing Certificate End Date--%>
                <tr>
                    <td class="col-xs-6">
                        <p class="form-check-label" aria-label="premise-1-cytology">
                            Practicing Certificate End Date
                        </p>
                    </td>
                    <td>
                        <div class="col-xs-6">
                            <div class="newVal " attr="${nurseList.praCerEndDate}">
                                <c:out value="${nurseList.praCerEndDate}"/>
                            </div>
                        </div>
                        <div class="col-xs-6">
                            <div class="oldVal " attr="${oldNurseList.praCerEndDate}" style="display: none">
                                    ${oldNurseList.praCerEndDate}
                            </div>
                        </div>
                    </td>
                </tr>

                    <%--    Type of Register--%>
                <tr>
                    <td class="col-xs-6">
                        <p class="form-check-label" aria-label="premise-1-cytology">
                            Type of Register
                        </p>
                    </td>
                    <td>
                        <div class="col-xs-6">
                            <div class="newVal " attr="${nurseList.typeOfRegister}">
                                <c:out value="${nurseList.typeOfRegister}"/>
                            </div>
                        </div>
                        <div class="col-xs-6">
                            <div class="oldVal " attr="${oldNurseList.typeOfRegister}" style="display: none">
                                    ${oldNurseList.typeOfRegister}
                            </div>
                        </div>
                    </td>
                </tr>


                    <%--Specialty--%>
                <tr>
                    <td class="col-xs-6">
                        <p class="form-check-label" aria-label="premise-1-cytology">
                            Specialty
                        </p>
                    </td>
                    <td>
                        <div class="col-xs-6">
                            <div class="newVal " attr="${nurseList.speciality}">
                                <c:out value="${nurseList.speciality}"/>
                            </div>
                        </div>
                        <div class="col-xs-6">
                            <div class="oldVal " attr="${oldNurseList.speciality}"  style="display: none">
                                    ${oldNurseList.speciality}
                            </div>
                        </div>
                    </td>
                </tr>

                    <%--                        Sub-Specialty--%>
                <tr>
                    <td class="col-xs-6">
                        <p class="form-check-label" aria-label="premise-1-cytology">
                            Sub-Specialty
                        </p>
                    </td>
                    <td>
                        <div class="col-xs-6">
                            <div class="newVal " attr="${nurseList.subSpeciality}">
                                <c:out value="${nurseList.subSpeciality}"/>
                            </div>
                        </div>
                        <div class="col-xs-6">
                            <div class="oldVal " attr="${oldNurseList.subSpeciality}" style="display: none">
                                    ${oldNurseList.subSpeciality}
                            </div>
                        </div>
                    </td>
                </tr>


                    <%--                        Other Specialties--%>
                <tr>
                    <td class="col-xs-6">
                        <p class="form-check-label" aria-label="premise-1-cytology">
                            Other Specialties
                        </p>
                    </td>
                    <td>
                        <div class="col-xs-6">
                            <div class="newVal " attr="${nurseList.specialityOther}">
                                <c:out value="${nurseList.specialityOther}"/>
                            </div>
                        </div>
                        <div class="col-xs-6">
                            <div class="oldVal " attr="${oldNurseList.specialityOther}" style="display: none">
                                    ${oldNurseList.specialityOther}
                            </div>
                        </div>
                    </td>
                </tr>


                    <%--    Date when specialty was obtained--%>
                <tr>
                    <td class="col-xs-6">
                        <p class="form-check-label" aria-label="premise-1-cytology">
                            Date when specialty was obtained
                        </p>
                    </td>
                    <td>
                        <div class="col-xs-6">
                            <div class="newVal " attr="${nurseList.specialtyGetDate}">
                                <c:out value="${nurseList.specialtyGetDate}"/>
                            </div>
                        </div>
                        <div class="col-xs-6">
                            <div class="oldVal " attr="${oldNurseList.specialtyGetDate}" style="display: none">
                                    ${oldNurseList.specialtyGetDate}
                            </div>
                        </div>
                    </td>
                </tr>


                    <%--    Qualification--%>
                <tr>
                    <td class="col-xs-6">
                        <p class="form-check-label" aria-label="premise-1-cytology">
                            Qualification
                        </p>
                    </td>
                    <td>
                        <div class="col-xs-6">
                            <div class="newVal " attr="${nurseList.qualification}">
                                <c:out value="${nurseList.qualification}"/>
                            </div>
                        </div>
                        <div class="col-xs-6">
                            <div class="oldVal " attr="${oldNurseList.qualification}" style="display: none">
                                    ${oldNurseList.qualification}
                            </div>
                        </div>
                    </td>
                </tr>


                    <%--    Relevant working experience(Years)--%>
                <tr>
                    <td class="col-xs-6">
                        <p class="form-check-label" aria-label="premise-1-cytology">
                            Relevant working experience(Years)
                        </p>
                    </td>
                    <td>
                        <div class="col-xs-6">
                            <div class="newVal " attr="${nurseList.wrkExpYear}">
                                <c:out value="${nurseList.wrkExpYear}"/>
                            </div>
                        </div>
                        <div class="col-xs-6">
                            <div class="oldVal " attr="${oldNurseList.wrkExpYear}" style="display: none">
                                    ${oldNurseList.wrkExpYear}
                            </div>
                        </div>
                    </td>
                </tr>

                    <%--    Expiry Date (BCLS and AED)--%>
                <tr>
                    <td class="col-xs-6">
                        <p class="form-check-label" aria-label="premise-1-cytology">
                            Expiry Date (BCLS and AED)
                        </p>
                    </td>
                    <td>
                        <div class="col-xs-6">
                            <div class="newVal " attr="${nurseList.bclsExpiryDate}">
                                <c:out value="${nurseList.bclsExpiryDate}"/>
                            </div>
                        </div>
                        <div class="col-xs-6">
                            <div class="oldVal " attr="${oldNurseList.bclsExpiryDate}" style="display: none">
                                    ${oldNurseList.bclsExpiryDate}
                            </div>
                        </div>
                    </td>
                </tr>


                    <%--                        Expiry Date(CPR)--%>
                <tr>
                    <td class="col-xs-6">
                        <p class="form-check-label" aria-label="premise-1-cytology">
                            Expiry Date (BCLS and AED)
                        </p>
                    </td>
                    <td>
                        <div class="col-xs-6">
                            <div class="newVal "
                                 attr="${nurseList.cprExpiryDate}">
                                <c:out value="${nurseList.cprExpiryDate}"/>
                            </div>
                        </div>
                        <div class="col-xs-6">
                            <div class="oldVal " attr="${oldNurseList.cprExpiryDate}" style="display: none">
                                    ${oldNurseList.cprExpiryDate}
                            </div>
                        </div>
                    </td>
                </tr>
            </table>
        </c:forEach>
    </div>
</div>