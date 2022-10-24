<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<div class="row">
    <div>
    <c:forEach items="${currentPreviewSvcInfo.svcPersonnelDto.arPractitionerList}" var="arPractitionerList" varStatus="status">
        <c:set var="oldArPractitionerList" value="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.svcPersonnelDto.arPractitionerList[status.index]}"/>
        <p>
            <strong class="col-xs-6">
                AR Practitioner
                <c:if test="${fn:length(currentPreviewSvcInfo.svcPersonnelDto.arPractitionerList)>1}">
                    ${status.index+1}
                </c:if>:
            </strong>
        </p>
        <span class="col-xs-6"></span>
        <table aria-describedby="" class="col-xs-12">
<%--                Name--%>
        <tr>
            <td class="col-xs-6">
                <p class="form-check-label" aria-label="premise-1-cytology">
                    Name
                </p>
            </td>
            <td>
                <div class="col-xs-6 img-show">
                    <div class="newVal " attr="${arPractitionerList.name}">
                      <c:out value="${arPractitionerList.name}"/>
                      <jsp:include
                              page="/WEB-INF/jsp/iais/hcsaLicence/section/nameDisciplinaryRecordMark.jsp">
                          <jsp:param name="profRegNo" value="${arPractitionerList.profRegNo}"/>
                          <jsp:param name="personName" value="${arPractitionerList.name}"/>
                          <jsp:param name="methodName" value="showThisNameTableNewService"/>
                      </jsp:include>
                    </div>
                </div>
                <div class="col-xs-6 img-show">
                    <div class="oldVal "
                          attr="${oldArPractitionerList.name}"
                          style="display: none">${oldArPractitionerList.name}
                      <jsp:include
                              page="/WEB-INF/jsp/iais/hcsaLicence/section/nameDisciplinaryRecordMark.jsp">
                          <jsp:param name="profRegNo" value="${oldArPractitionerList.profRegNo}"/>
                          <jsp:param name="personName" value="${oldArPractitionerList.name}"/>
                          <jsp:param name="methodName" value="showThisNameTableOldService"/>
                      </jsp:include>
                    </div>
                </div>
                <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/nameDisciplinaryRecords.jsp">
                    <jsp:param name="profRegNo" value="${arPractitionerList.profRegNo}"/>
                    <jsp:param name="cssClass" value="new-img-show"/>
                </jsp:include>
                <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/nameDisciplinaryRecords.jsp">
                    <jsp:param name="profRegNo" value="${oldArPractitionerList.profRegNo}"/>
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
                    <div class="newVal " attr="${arPractitionerList.designation}">
                           <iais:code code="${arPractitionerList.designation}"/>
                    </div>
                </div>
                <div class="col-xs-6">
                    <div class="oldVal " attr="${oldArPractitionerList.designation}"
                          style="display: none">
                         <iais:code code="${oldArPractitionerList.designation}"/>
                    </div>
                </div>
            </td>
        </tr>

<%--    otherdesignation--%>
        <c:if test="${'DES999' == arPractitionerList.designation || 'DES999' == oldArPractitionerList.designation}">
            <tr>
                <td class="col-xs-6">
                    <p class="form-check-label" aria-label="premise-1-cytology">
                        OtherDesignation
                    </p>
                </td>
                <td>
                    <div class="col-xs-6">
                        <div class="newVal " attr="${arPractitionerList.otherDesignation}">
                                ${arPractitionerList.otherDesignation}
                        </div>
                    </div>
                    <div class="col-xs-6">
                        <div class="oldVal " attr="${oldArPractitionerList.otherDesignation}" style="display: none">
                                ${oldArPractitionerList.otherDesignation}
                        </div>
                    </div>
                </td>
            </tr>
        </c:if>

<%--    No--%>
                <tr>
        <td class="col-xs-6">
            <p class="form-check-label" aria-label="premise-1-cytology">
                Professional Regn. No.
            </p>
        </td>
        <td>
            <div class="col-xs-6 img-show">
                <div class="newVal " attr="${arPractitionerList.profRegNo}">
                  <c:out value="${arPractitionerList.profRegNo}"/>
                  <jsp:include
                          page="/WEB-INF/jsp/iais/hcsaLicence/section/prsDisciplinaryRecordMark.jsp">
                      <jsp:param name="profRegNo" value="${arPractitionerList.profRegNo}"/>
                      <jsp:param name="methodName" value="showThisTableNewService"/>
                  </jsp:include>
                </div>
            </div>
            <div class="col-xs-6 img-show">
                <div class="oldVal" attr="${oldArPractitionerList.profRegNo}"
                      style="display: none">
                    ${oldArPractitionerList.profRegNo}
                    <jsp:include
                            page="/WEB-INF/jsp/iais/hcsaLicence/section/prsDisciplinaryRecordMark.jsp">
                        <jsp:param name="profRegNo"
                                   value="${oldArPractitionerList.profRegNo}"/>
                        <jsp:param name="methodName" value="showThisTableOldService"/>
                    </jsp:include>
                </div>
            </div>
            <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/prsDisciplinaryRecords.jsp">
                <jsp:param name="profRegNo" value="${arPractitionerList.profRegNo}"/>
                <jsp:param name="cssClass" value="new-img-show"/>
            </jsp:include>
            <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/prsDisciplinaryRecords.jsp">
                <jsp:param name="profRegNo" value="${oldArPractitionerList.profRegNo}"/>
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
                    <div class="newVal " attr="${arPractitionerList.typeOfCurrRegi}">
                        <c:out value="${arPractitionerList.typeOfCurrRegi}"/>
                    </div>
                </div>
                <div class="col-xs-6">
                    <div class="oldVal " attr="${oldArPractitionerList.typeOfCurrRegi}"
                          style="display: none">
                            ${oldArPractitionerList.typeOfCurrRegi}
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
                    <div class="newVal " attr="${arPractitionerList.currRegiDate}">
                        <c:out value="${arPractitionerList.currRegiDate}"/>
                    </div>
                </div>
                <div class="col-xs-6">
                    <div class="oldVal " attr="${oldArPractitionerList.currRegiDate}"
                          style="display: none">
                            ${oldArPractitionerList.currRegiDate}
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
                    <div class="newVal " attr="${arPractitionerList.praCerEndDate}">
                        <c:out value="${arPractitionerList.praCerEndDate}"/>
                    </div>
                </div>
                <div class="col-xs-6">
                    <div class="oldVal " attr="${oldArPractitionerList.praCerEndDate}"
                          style="display: none">
                            ${oldArPractitionerList.praCerEndDate}
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
                    <div class="newVal " attr="${arPractitionerList.typeOfRegister}">
                        <c:out value="${arPractitionerList.typeOfRegister}"/>
                    </div>
                </div>
                <div class="col-xs-6">
                    <div class="oldVal " attr="${oldArPractitionerList.typeOfRegister}"
                          style="display: none">
                            ${oldArPractitionerList.typeOfRegister}
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
                    <div class="newVal " attr="${arPractitionerList.speciality}">
                        <c:out value="${arPractitionerList.speciality}"/>
                    </div>
                </div>
                <div class="col-xs-6">
                    <div class="oldVal " attr="${oldArPractitionerList.speciality}"
                          style="display: none">
                            ${oldArPractitionerList.speciality}
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
                    <div class="newVal " attr="${arPractitionerList.specialtyGetDate}">
                        <c:out value="${arPractitionerList.specialtyGetDate}"/>
                    </div>
                </div>
                <div class="col-xs-6">
                    <div class="oldVal " attr="${oldArPractitionerList.specialtyGetDate}" style="display: none">
                            ${oldArPractitionerList.specialtyGetDate}
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
                    <div class="newVal " attr="${arPractitionerList.qualification}">
                        <c:out value="${arPractitionerList.qualification}"/>
                    </div>
                </div>
                <div class="col-xs-6">
                    <div class="oldVal " attr="${oldArPractitionerList.qualification}"
                          style="display: none">
                            ${oldArPractitionerList.qualification}
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
                    <div class="newVal " attr="${arPractitionerList.wrkExpYear}">
                        <c:out value="${arPractitionerList.wrkExpYear}"/>
                    </div>
                </div>
                <div class="col-xs-6">
                    <div class="oldVal " attr="${oldArPractitionerList.wrkExpYear}" style="display: none">
                            ${oldArPractitionerList.wrkExpYear}
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
                    <div class="newVal " attr="${arPractitionerList.bclsExpiryDate}">
                        <c:out value="${arPractitionerList.bclsExpiryDate}"/>
                    </div>
                </div>
                <div class="col-xs-6">
                    <div class="oldVal " attr="${oldArPractitionerList.bclsExpiryDate}" style="display: none">
                            ${oldArPractitionerList.bclsExpiryDate}
                    </div>
                </div>
            </td>
        </tr>
    </table>
        </c:forEach>
    </div>
    </div>
