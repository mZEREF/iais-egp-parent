<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<div class="row">
    <div class="col-xs-12">
        <c:forEach items="${currentPreviewSvcInfo.svcPersonnelDto.specialList}" var="specialList"
                   varStatus="status">
            <c:set var="oldSpecialList"
                   value="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.svcPersonnelDto.specialList[status.index]}"/>
            <p>
                <strong class="col-xs-6">
                    Service Personnel
                    <c:if test="${fn:length(currentPreviewSvcInfo.svcPersonnelDto.specialList)>1}">
                        ${status.index+1}
                    </c:if>:
                </strong>
            </p>
            <span class="col-xs-6"></span>
            <table aria-describedby="" class="col-xs-12">
                <c:choose>
                    <c:when test="${currentPreviewSvcInfo.serviceCode=='BLB'}">
                        <tr>
                            <td class="col-xs-6">
                                <p class="form-check-label" aria-label="premise-1-cytology">
                                    Designation
                                </p>
                            </td>
                            <td>
                                <div class="col-xs-12">
                                                <span class="newVal " attr="${specialList.designation}">
                                                  <c:out value="${specialList.designation}"/>
                                                </span>
                                    <br>
                                    <span class="oldVal " attr="${oldSpecialList.designation}"
                                          style="display: none">
                                                    <c:out value="${oldSpecialList.designation}"/>
                                                </span>
                                </div>
                            </td>
                        </tr>
                        <c:if test="${'Others' == specialList.designation || 'Others' == specialList.designation}">
                            <tr>
                                <td class="col-xs-6">
                                </td>
                                <td>
                                    <div class="col-xs-12">
                                                    <span class="newVal " attr="${specialList.otherDesignation}">
                                                            ${specialList.otherDesignation}
                                                    </span>
                                        <br>
                                        <span class="oldVal " attr="${oldSpecialList.otherDesignation}"
                                              style="display: none">
                                                ${oldSpecialList.otherDesignation}
                                        </span>
                                    </div>
                                </td>
                            </tr>
                        </c:if>


                        <tr>
                            <td class="col-xs-6">
                                <p class="form-check-label" aria-label="premise-1-cytology">
                                    Name
                                </p>
                            </td>
                            <td>
                                <div class="col-xs-6 img-show">
                                                <span class="newVal " attr="${specialList.name}">
                                                  <c:out value="${specialList.name}"/>
                                                  <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/nameDisciplinaryRecordMark.jsp">
                                                      <jsp:param name="profRegNo" value="${specialList.profRegNo}"/>
                                                      <jsp:param name="personName" value="${specialList.name}"/>
                                                      <jsp:param name="methodName" value="showThisNameTableNewService"/>
                                                  </jsp:include>
                                                </span>
                                </div>
                                <div class="col-xs-6 img-show">
                                                <span class="oldVal "
                                                      attr="${oldSpecialList.name}"
                                                      style="display: none">${oldSpecialList.name}
                                                  <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/nameDisciplinaryRecordMark.jsp">
                                                      <jsp:param name="profRegNo" value="${oldSpecialList.profRegNo}"/>
                                                      <jsp:param name="personName" value="${oldSpecialList.name}"/>
                                                      <jsp:param name="methodName" value="showThisNameTableOldService"/>
                                                  </jsp:include>
                                                </span>
                                </div>
                                <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/nameDisciplinaryRecords.jsp">
                                    <jsp:param name="profRegNo" value="${specialList.profRegNo}"/>
                                    <jsp:param name="cssClass" value="new-img-show"/>
                                </jsp:include>
                                <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/nameDisciplinaryRecords.jsp">
                                    <jsp:param name="profRegNo" value="${oldSpecialList.profRegNo}"/>
                                    <jsp:param name="cssClass" value="old-img-show"/>
                                </jsp:include>
                            </td>
                        </tr>






                        <tr>
                            <td class="col-xs-6">
                                <p class="form-check-label" aria-label="premise-1-cytology">
                                    Professional Regn. No.
                                </p>
                            </td>
                            <td>
                                <div class="col-xs-6 img-show">
                                                <span class="newVal " attr="${specialList.profRegNo}">
                                                  <c:out value="${specialList.profRegNo}"/>
                                                  <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/prsDisciplinaryRecordMark.jsp">
                                                      <jsp:param name="profRegNo" value="${specialList.profRegNo}"/>
                                                      <jsp:param name="methodName" value="showThisTableNewService"/>
                                                  </jsp:include>
                                                </span>
                                </div>
                                <div class="col-xs-6 img-show">
                                                <span class="oldVal" attr="${oldSpecialList.profRegNo}" style="display: none">
                                                    ${oldSpecialList.profRegNo}
                                                    <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/prsDisciplinaryRecordMark.jsp">
                                                        <jsp:param name="profRegNo" value="${oldSpecialList.profRegNo}"/>
                                                        <jsp:param name="methodName" value="showThisTableOldService"/>
                                                    </jsp:include>
                                                </span>
                                </div>
                                <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/prsDisciplinaryRecords.jsp">
                                    <jsp:param name="profRegNo" value="${specialList.profRegNo}"/>
                                    <jsp:param name="cssClass" value="new-img-show"/>
                                </jsp:include>
                                <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/prsDisciplinaryRecords.jsp">
                                    <jsp:param name="profRegNo" value="${oldSpecialList.profRegNo}"/>
                                    <jsp:param name="cssClass" value="old-img-show"/>
                                </jsp:include>
                            </td>
                        </tr>
                        <tr>
                            <td class="col-xs-6">
                                <p class="form-check-label" aria-label="premise-1-cytology">
                                    Relevantworking experience (Years)
                                </p>
                            </td>
                            <td>
                                <div class="col-xs-6">
                                                <span class="newVal " attr="${specialList.wrkExpYear}">
                                                    <c:out value="${specialList.wrkExpYear}"/>
                                                </span>
                                </div>
                                <div class="col-xs-6">
                                                <span class="oldVal " attr="${oldSpecialList.wrkExpYear}"
                                                      style="display: none">
                                                        ${oldSpecialList.wrkExpYear}
                                                </span>
                                </div>
                            </td>
                        </tr>
                    </c:when>
                    <c:when test="${currentPreviewSvcInfo.serviceCode=='TSB'}">
                        <tr>
                            <td class="col-xs-6">
                                <p class="form-check-label" aria-label="premise-1-cytology">
                                    Name
                                </p>
                            </td>
                            <td>
                                <div class="col-xs-6 col-md-6">
                                                <span class="newVal " attr="${specialList.name}">
                                                  <c:out value="${specialList.name}"/>
                                                </span>
                                </div>
                                <div class="col-xs-6">
                                                <span class="oldVal" attr="${oldSpecialList.name}" style="display:none">
                                                  <c:out value="${oldSpecialList.name}"/>
                                                </span>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td class="col-xs-6">
                                <p class="form-check-label" aria-label="premise-1-cytology">
                                    Qualification
                                </p>
                            </td>
                            <td>
                                <div class="col-xs-12">
                                                <span class="newVal " attr="${specialList.qualification}">
                                                    <c:out value="${specialList.qualification}"/>
                                                </span>
                                    <br>
                                    <span class="oldVal " attr="${oldSpecialList.qualification}"
                                          style="display: none">
                                            ${oldSpecialList.qualification}
                                    </span>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td class="col-xs-6">
                                <p class="form-check-label" aria-label="premise-1-cytology">
                                    Relevant working experience (Years)
                                </p>
                            </td>
                            <td>
                                <div class="col-xs-6">
                                                <span class="newVal " attr="${specialList.wrkExpYear}">
                                                  <c:out value="${specialList.wrkExpYear}"/>
                                                </span>
                                </div>
                                <div class="col-xs-6">
                                                <span class="oldVal " attr="${oldSpecialList.wrkExpYear}"
                                                      style="display: none">${oldSpecialList.wrkExpYear}
                                                </span>
                                </div>
                            </td>
                        </tr>
                    </c:when>

                    <c:when test="${currentPreviewSvcInfo.serviceCode=='NMA'}">
                        <tr>
                            <td class="col-xs-6">
                                <p class="form-check-label" aria-label="premise-1-cytology">
                                    Select Service Personnel
                                </p>
                            </td>
                            <td>
                                <div class="col-xs-6">
                                                <span class="newVal " attr="${specialList.personnelType}">
                                                  <iais:code code="${specialList.personnelType}"/>
                                                </span>
                                </div>
                                <div class="col-xs-6">
                                                <span class="oldVal " attr="${oldSpecialList.personnelType}" style="display: none">
                                                  <iais:code code="${oldSpecialList.personnelType}"/>
                                                </span>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td class="col-xs-6">
                                <p class="form-check-label" aria-label="premise-1-cytology">
                                    Name
                                </p>
                            </td>
                            <td>
                                <div class="col-xs-6">
                                                <span class="newVal " attr="${specialList.name}">
                                                  <c:out value="${specialList.name}"/>
                                                </span>
                                </div>
                                <div class="col-xs-6">
                                                <span class="oldVal " attr="${oldSpecialList.name}" style="display: none">
                                                  <c:out value="${oldSpecialList.name}"/>
                                                </span>
                                </div>
                            </td>
                        </tr>
                        <c:if test="${specialList.personnelType == 'SPPT002' || oldSpecialList.personnelType == 'SPPT002'}">
                            <tr>
                                <td class="col-xs-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology">
                                        Qualification
                                    </p>
                                </td>
                                <td>
                                    <div class="col-xs-12">
                                                    <span class="newVal " attr="${specialList.qualification}">
                                                      <c:out value="${specialList.qualification}"/>
                                                    </span>
                                        <br>
                                        <span class="oldVal " attr="${oldSpecialList.qualification}"
                                              style="display: none">
                                                          <c:out value="${oldSpecialList.qualification}"/>
                                                    </span>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="col-xs-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology">
                                        Relevant working experience (Years)
                                    </p>
                                </td>
                                <td>
                                    <div class="col-xs-6">
                                                    <span class="newVal " attr="${specialList.wrkExpYear}">
                                                      <c:out value="${specialList.wrkExpYear}"/>
                                                    </span>
                                    </div>
                                    <div class="col-xs-6">
                                                    <span class="oldVal " attr="${oldSpecialList.wrkExpYear}"
                                                          style="display: none">${oldSpecialList.wrkExpYear}
                                                    </span>
                                    </div>
                                </td>
                            </tr>
                        </c:if>
                    </c:when>

                    <c:when test="${currentPreviewSvcInfo.serviceCode=='NMI'}">
                        <tr>
                            <td class="col-xs-6">
                                <p class="form-check-label" aria-label="premise-1-cytology">
                                    Select Service Personnel
                                </p>
                            </td>
                            <td>
                                <div class="col-xs-6">
                                                <span class="newVal " attr="${specialList.personnelType}">
                                                  <iais:code code="${specialList.personnelType}"/>
                                                </span>
                                </div>
                                <div class="col-xs-6">
                                                <span class="oldVal " attr="${oldSpecialList.personnelType}" style="display: none">
                                                  <iais:code code="${oldSpecialList.personnelType}"/>
                                                </span>
                                </div>
                            </td>
                        </tr>
                        <c:if test="${specialList.personnelType == 'SPPT004' || oldSpecialList.personnelType == 'SPPT004'}"
                              var="hasPrsNo">
                            <tr>
                                <td class="col-xs-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology">
                                        Name
                                    </p>
                                </td>
                                <td>
                                    <div class="col-xs-6 img-show">
                                                    <span class="newVal " attr="${specialList.name}">
                                                      <c:out value="${specialList.name}"/>
                                                      <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/nameDisciplinaryRecordMark.jsp">
                                                          <jsp:param name="profRegNo" value="${specialList.profRegNo}"/>
                                                          <jsp:param name="personName" value="${specialList.name}"/>
                                                          <jsp:param name="methodName" value="showThisNameTableNewService"/>
                                                      </jsp:include>
                                                    </span>
                                    </div>
                                    <div class="col-xs-6 img-show">
                                                    <span class="oldVal "
                                                          attr="${oldSpecialList.name}"
                                                          style="display: none">${oldSpecialList.name}
                                                      <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/nameDisciplinaryRecordMark.jsp">
                                                          <jsp:param name="profRegNo" value="${oldSpecialList.profRegNo}"/>
                                                          <jsp:param name="personName" value="${oldSpecialList.name}"/>
                                                          <jsp:param name="methodName" value="showThisNameTableOldService"/>
                                                      </jsp:include>
                                                    </span>
                                    </div>
                                    <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/nameDisciplinaryRecords.jsp">
                                        <jsp:param name="profRegNo" value="${specialList.profRegNo}"/>
                                        <jsp:param name="cssClass" value="new-img-show"/>
                                    </jsp:include>
                                    <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/nameDisciplinaryRecords.jsp">
                                        <jsp:param name="profRegNo" value="${oldSpecialList.profRegNo}"/>
                                        <jsp:param name="cssClass" value="old-img-show"/>
                                    </jsp:include>
                                </td>
                            </tr>
                        </c:if>
                        <c:if test="${not hasPrsNo}">
                            <tr>
                                <td class="col-xs-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology">
                                        Name
                                    </p>
                                </td>
                                <td>
                                    <div class="col-xs-6">
                                                    <span class="newVal " attr="${specialList.name}">
                                                      <c:out value="${specialList.name}"/>
                                                    </span>
                                    </div>
                                    <div class="col-xs-6">
                                                    <span class="oldVal " attr="${oldSpecialList.name}" style="display: none">
                                                      <c:out value="${oldSpecialList.name}"/>
                                                    </span>
                                    </div>
                                </td>
                            </tr>
                        </c:if>
                        <c:if test="${specialList.personnelType == 'SPPT001' || oldSpecialList.personnelType == 'SPPT001'}">
                            <tr>
                                <td class="col-xs-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology">
                                        Designation
                                    </p>
                                </td>
                                <td>
                                    <div class="col-xs-12">
                                                  <span class="newVal " attr="${specialList.designation}">
                                                          ${specialList.designation}
                                                  </span>
                                        <br>
                                        <span class="oldVal " attr="${oldSpecialList.designation}"
                                              style="display: none">
                                                ${oldSpecialList.designation}
                                        </span>
                                    </div>
                                </td>
                            </tr>
                        </c:if>
                        <c:if test="${'Others' == specialList.designation || 'Others' == oldSpecialList.designation}">
                            <tr>
                                <td class="col-xs-6">
                                </td>
                                <td>
                                    <div class="col-xs-12">
                                                    <span class="newVal " attr="${specialList.otherDesignation}">
                                                            ${specialList.otherDesignation}
                                                    </span>
                                        <br>
                                        <span class="oldVal " attr="${oldSpecialList.otherDesignation}"
                                              style="display: none">
                                                ${oldSpecialList.otherDesignation}
                                        </span>
                                    </div>
                                </td>
                            </tr>
                        </c:if>
                        <c:if test="${specialList.personnelType == 'SPPT001' || oldSpecialList.personnelType == 'SPPT001'
                                        || specialList.personnelType == 'SPPT002' || oldSpecialList.personnelType == 'SPPT002'}">
                            <tr>
                                <td class="col-xs-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology">
                                        Qualification
                                    </p>
                                </td>
                                <td>
                                    <div class="col-xs-12">
                                                    <span class="newVal " attr="${specialList.qualification}">
                                                            ${specialList.qualification}
                                                    </span>
                                        <br>
                                        <span class="oldVal " attr="${oldSpecialList.qualification}"
                                              style="display: none">
                                                ${oldSpecialList.qualification}
                                        </span>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="col-xs-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology">
                                        Relevant working experience (Years)
                                    </p>
                                </td>
                                <td>
                                    <div class="col-xs-6">
                                                    <span class="newVal " attr="${specialList.wrkExpYear}">
                                                        <c:out value="${specialList.wrkExpYear}"/>
                                                    </span>
                                    </div>
                                    <div class="col-xs-6">
                                                    <span class="oldVal " attr="${oldSpecialList.wrkExpYear}" style="display: none">
                                                        <c:out value="${oldSpecialList.wrkExpYear}"/>
                                                    </span>
                                    </div>
                                </td>
                            </tr>
                        </c:if>
                        <c:if test="${hasPrsNo}">
                            <tr>
                                <td class="col-xs-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology">
                                        Professional Regn. No.
                                    </p>
                                </td>
                                <td>
                                    <div class="col-xs-6 img-show">
                                                    <span class="newVal" attr="${specialList.profRegNo}">
                                                      <c:out value="${specialList.profRegNo}"/>
                                                      <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/prsDisciplinaryRecordMark.jsp">
                                                          <jsp:param name="profRegNo" value="${specialList.profRegNo}"/>
                                                          <jsp:param name="methodName" value="showThisTableNewService"/>
                                                      </jsp:include>
                                                    </span>
                                    </div>
                                    <div class="col-xs-6 img-show">
                                                    <span class="oldVal" attr="${oldSpecialList.profRegNo}" style="display: none">
                                                        ${oldSpecialList.profRegNo}
                                                        <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/prsDisciplinaryRecordMark.jsp">
                                                            <jsp:param name="profRegNo" value="${oldSpecialList.profRegNo}"/>
                                                            <jsp:param name="methodName" value="showThisTableOldService"/>
                                                        </jsp:include>
                                                    </span>
                                    </div>
                                    <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/prsDisciplinaryRecords.jsp">
                                        <jsp:param name="profRegNo" value="${specialList.profRegNo}"/>
                                        <jsp:param name="cssClass" value="new-img-show"/>
                                    </jsp:include>
                                    <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/prsDisciplinaryRecords.jsp">
                                        <jsp:param name="profRegNo" value="${oldSpecialList.profRegNo}"/>
                                        <jsp:param name="cssClass" value="old-img-show"/>
                                    </jsp:include>
                                </td>
                            </tr>
                        </c:if>
                    </c:when>
                    <c:otherwise>
                        <tr>
                            <th scope="col" style="display: none"></th>
                            <td class="col-xs-6">
                                <p class="form-check-label" aria-label="premise-1-cytology">
                                    Name
                                </p>
                            </td>
                            <td>
                                <div class="col-xs-6 img-show">
                                                <span class="newVal " attr="${specialList.name}">
                                                    <c:out value="${specialList.name}"/>
                                                </span>
                                </div>
                                <div class="col-xs-6 img-show">
                                                <span class="oldVal " attr="${oldSpecialList.name}" style="display: none">
                                                        ${oldSpecialList.name}
                                                </span>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td class="col-xs-6">
                                <p class="form-check-label" aria-label="premise-1-cytology">
                                    Qualification
                                </p>
                            </td>
                            <td>
                                <div class="col-xs-12">
                                                <span class="newVal " attr="${specialList.qualification}">
                                                        ${specialList.qualification}
                                                </span>
                                    <br>
                                    <span class="oldVal " attr="${oldSpecialList.qualification}"
                                          style="display: none">
                                            ${oldSpecialList.qualification}
                                    </span>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td class="col-xs-6">
                                <p class="form-check-label" aria-label="premise-1-cytology">
                                    Relevant working experience (Years)
                                </p>
                            </td>
                            <td>
                                <div class="col-xs-6">
                                                <span class="newVal " attr="${specialList.wrkExpYear}">
                                                    <c:out value="${specialList.wrkExpYear}"/>
                                                </span>
                                </div>
                                <div class="col-xs-6">
                                                <span class="oldVal " attr="${oldSpecialList.wrkExpYear}" style="display: none">
                                                        ${oldSpecialList.wrkExpYear}
                                                </span>
                                </div>
                            </td>
                        </tr>
                    </c:otherwise>
                </c:choose>
            </table>
        </c:forEach>
    </div>
</div>