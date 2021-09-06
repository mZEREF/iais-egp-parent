<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>

<div class="amended-service-info-gp">
    <label class="title-font-size">${stepNameMap['SVST006']}</label>
    <div class="amend-preview-info">
        <div class="form-check-gp">
            <div class="row">
                <div class="col-xs-12">
                    <c:forEach items="${currentPreviewSvcInfo.appSvcPersonnelDtoList}" var="appSvcPersonnelDtoList"
                               varStatus="status">
                        <c:set var="oldAppSvcPersonnelDtoList"
                               value="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcPersonnelDtoList[status.index]}"/>
                        <p>
                            <strong class="col-xs-6">
                                Service Personnel
                                <c:if test="${fn:length(currentPreviewSvcInfo.appSvcPersonnelDtoList)>1}">
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
                                                <span class="newVal " attr="${appSvcPersonnelDtoList.designation}">
                                                  <c:out value="${appSvcPersonnelDtoList.designation}"/>
                                                </span>
                                                <br>
                                                <span class="oldVal " attr="${oldAppSvcPersonnelDtoList.designation}"
                                                      style="display: none">
                                                    <c:out value="${oldAppSvcPersonnelDtoList.designation}"/>
                                                </span>
                                            </div>
                                        </td>
                                    </tr>
                                    <c:if test="${'Others' == appSvcPersonnelDtoList.designation || 'Others' == oldAppSvcPersonnelDtoList.designation}">
                                        <tr>
                                            <td class="col-xs-6">
                                            </td>
                                            <td>
                                                <div class="col-xs-12">
                                                    <span class="newVal " attr="${appSvcPersonnelDtoList.otherDesignation}">
                                                        ${appSvcPersonnelDtoList.otherDesignation}
                                                    </span>
                                                    <br>
                                                    <span class="oldVal " attr="${oldAppSvcPersonnelDtoList.otherDesignation}"
                                                          style="display: none">
                                                        ${oldAppSvcPersonnelDtoList.otherDesignation}
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
                                                <span class="newVal " attr="${appSvcPersonnelDtoList.name}">
                                                  <c:out value="${appSvcPersonnelDtoList.name}"/>
                                                  <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/nameDisciplinaryRecordMark.jsp">
                                                      <jsp:param name="profRegNo" value="${appSvcPersonnelDtoList.profRegNo}"/>
                                                      <jsp:param name="personName" value="${appSvcPersonnelDtoList.name}"/>
                                                      <jsp:param name="methodName" value="showThisNameTableNewService"/>
                                                  </jsp:include>
                                                </span>
                                            </div>
                                            <div class="col-xs-6 img-show">
                                                <span class="oldVal "
                                                      attr="${oldAppSvcPersonnelDtoList.name}"
                                                      style="display: none">${oldAppSvcPersonnelDtoList.name}
                                                  <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/nameDisciplinaryRecordMark.jsp">
                                                      <jsp:param name="profRegNo" value="${oldAppSvcPersonnelDtoList.profRegNo}"/>
                                                      <jsp:param name="personName" value="${oldAppSvcPersonnelDtoList.name}"/>
                                                      <jsp:param name="methodName" value="showThisNameTableOldService"/>
                                                  </jsp:include>
                                                </span>
                                            </div>
                                            <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/nameDisciplinaryRecords.jsp">
                                                <jsp:param name="profRegNo" value="${appSvcPersonnelDtoList.profRegNo}"/>
                                                <jsp:param name="cssClass" value="new-img-show"/>
                                            </jsp:include>
                                            <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/nameDisciplinaryRecords.jsp">
                                                <jsp:param name="profRegNo" value="${oldAppSvcPersonnelDtoList.profRegNo}"/>
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
                                                <span class="newVal " attr="${appSvcPersonnelDtoList.profRegNo}">
                                                  <c:out value="${appSvcPersonnelDtoList.profRegNo}"/>
                                                  <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/prsDisciplinaryRecordMark.jsp">
                                                      <jsp:param name="profRegNo" value="${appSvcPersonnelDtoList.profRegNo}"/>
                                                      <jsp:param name="methodName" value="showThisTableNewService"/>
                                                  </jsp:include>
                                                </span>
                                            </div>
                                            <div class="col-xs-6 img-show">
                                                <span class="oldVal" attr="${oldAppSvcPersonnelDtoList.profRegNo}" style="display: none">
                                                    ${oldAppSvcPersonnelDtoList.profRegNo}
                                                    <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/prsDisciplinaryRecordMark.jsp">
                                                        <jsp:param name="profRegNo" value="${oldAppSvcPersonnelDtoList.profRegNo}"/>
                                                        <jsp:param name="methodName" value="showThisTableOldService"/>
                                                    </jsp:include>
                                                </span>
                                            </div>
                                            <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/prsDisciplinaryRecords.jsp">
                                                <jsp:param name="profRegNo" value="${appSvcPersonnelDtoList.profRegNo}"/>
                                                <jsp:param name="cssClass" value="new-img-show"/>
                                            </jsp:include>
                                            <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/prsDisciplinaryRecords.jsp">
                                                <jsp:param name="profRegNo" value="${oldAppSvcPersonnelDtoList.profRegNo}"/>
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
                                                <span class="newVal " attr="${appSvcPersonnelDtoList.wrkExpYear}">
                                                    <c:out value="${appSvcPersonnelDtoList.wrkExpYear}"/>
                                                </span>
                                            </div>
                                            <div class="col-xs-6">
                                                <span class="oldVal " attr="${oldAppSvcPersonnelDtoList.wrkExpYear}"
                                                      style="display: none">
                                                        ${oldAppSvcPersonnelDtoList.wrkExpYear}
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
                                                <span class="newVal " attr="${appSvcPersonnelDtoList.name}">
                                                  <c:out value="${appSvcPersonnelDtoList.name}"/>
                                                </span>
                                            </div>
                                            <div class="col-xs-6">
                                                <span class="oldVal" attr="${oldAppSvcPersonnelDtoList.name}" style="display:none">
                                                  <c:out value="${oldAppSvcPersonnelDtoList.name}"/>
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
                                                <span class="newVal " attr="${appSvcPersonnelDtoList.qualification}">
                                                    <c:out value="${appSvcPersonnelDtoList.qualification}"/>
                                                </span>
                                                <br>
                                                <span class="oldVal " attr="${oldAppSvcPersonnelDtoList.qualification}"
                                                      style="display: none">
                                                        ${oldAppSvcPersonnelDtoList.qualification}
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
                                                <span class="newVal " attr="${appSvcPersonnelDtoList.wrkExpYear}">
                                                  <c:out value="${appSvcPersonnelDtoList.wrkExpYear}"/>
                                                </span>
                                            </div>
                                            <div class="col-xs-6">
                                                <span class="oldVal " attr="${oldAppSvcPersonnelDtoList.wrkExpYear}"
                                                      style="display: none">${oldAppSvcPersonnelDtoList.wrkExpYear}
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
                                                <span class="newVal " attr="${appSvcPersonnelDtoList.personnelType}">
                                                  <iais:code code="${appSvcPersonnelDtoList.personnelType}"/>
                                                </span>
                                            </div>
                                            <div class="col-xs-6">
                                                <span class="oldVal " attr="${oldAppSvcPersonnelDtoList.personnelType}" style="display: none">
                                                  <iais:code code="${oldAppSvcPersonnelDtoList.personnelType}"/>
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
                                                <span class="newVal " attr="${appSvcPersonnelDtoList.name}">
                                                  <c:out value="${appSvcPersonnelDtoList.name}"/>
                                                </span>
                                            </div>
                                            <div class="col-xs-6">
                                                <span class="oldVal " attr="${oldAppSvcPersonnelDtoList.name}" style="display: none">
                                                  <c:out value="${oldAppSvcPersonnelDtoList.name}"/>
                                                </span>
                                            </div>
                                        </td>
                                    </tr>
                                    <c:if test="${appSvcPersonnelDtoList.personnelType == 'SPPT002' || oldAppSvcPersonnelDtoList.personnelType == 'SPPT002'}">
                                        <tr>
                                            <td class="col-xs-6">
                                                <p class="form-check-label" aria-label="premise-1-cytology">
                                                    Qualification
                                                </p>
                                            </td>
                                            <td>
                                                <div class="col-xs-12">
                                                    <span class="newVal " attr="${appSvcPersonnelDtoList.qualification}">
                                                      <c:out value="${appSvcPersonnelDtoList.qualification}"/>
                                                    </span>
                                                    <br>
                                                    <span class="oldVal " attr="${oldAppSvcPersonnelDtoList.qualification}"
                                                          style="display: none">
                                                          <c:out value="${oldAppSvcPersonnelDtoList.qualification}"/>
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
                                                    <span class="newVal " attr="${appSvcPersonnelDtoList.wrkExpYear}">
                                                      <c:out value="${appSvcPersonnelDtoList.wrkExpYear}"/>
                                                    </span>
                                                </div>
                                                <div class="col-xs-6">
                                                    <span class="oldVal " attr="${oldAppSvcPersonnelDtoList.wrkExpYear}"
                                                          style="display: none">${oldAppSvcPersonnelDtoList.wrkExpYear}
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
                                                <span class="newVal " attr="${appSvcPersonnelDtoList.personnelType}">
                                                  <iais:code code="${appSvcPersonnelDtoList.personnelType}"/>
                                                </span>
                                            </div>
                                            <div class="col-xs-6">
                                                <span class="oldVal " attr="${oldAppSvcPersonnelDtoList.personnelType}" style="display: none">
                                                  <iais:code code="${oldAppSvcPersonnelDtoList.personnelType}"/>
                                                </span>
                                            </div>
                                        </td>
                                    </tr>
                                    <c:if test="${appSvcPersonnelDtoList.personnelType == 'SPPT004' || oldAppSvcPersonnelDtoList.personnelType == 'SPPT004'}"
                                          var="hasPrsNo">
                                        <tr>
                                            <td class="col-xs-6">
                                                <p class="form-check-label" aria-label="premise-1-cytology">
                                                    Name
                                                </p>
                                            </td>
                                            <td>
                                                <div class="col-xs-6 img-show">
                                                    <span class="newVal " attr="${appSvcPersonnelDtoList.name}">
                                                      <c:out value="${appSvcPersonnelDtoList.name}"/>
                                                      <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/nameDisciplinaryRecordMark.jsp">
                                                          <jsp:param name="profRegNo" value="${appSvcPersonnelDtoList.profRegNo}"/>
                                                          <jsp:param name="personName" value="${appSvcPersonnelDtoList.name}"/>
                                                          <jsp:param name="methodName" value="showThisNameTableNewService"/>
                                                      </jsp:include>
                                                    </span>
                                                </div>
                                                <div class="col-xs-6 img-show">
                                                    <span class="oldVal "
                                                          attr="${oldAppSvcPersonnelDtoList.name}"
                                                          style="display: none">${oldAppSvcPersonnelDtoList.name}
                                                      <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/nameDisciplinaryRecordMark.jsp">
                                                          <jsp:param name="profRegNo" value="${oldAppSvcPersonnelDtoList.profRegNo}"/>
                                                          <jsp:param name="personName" value="${oldAppSvcPersonnelDtoList.name}"/>
                                                          <jsp:param name="methodName" value="showThisNameTableOldService"/>
                                                      </jsp:include>
                                                    </span>
                                                </div>
                                                <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/nameDisciplinaryRecords.jsp">
                                                    <jsp:param name="profRegNo" value="${appSvcPersonnelDtoList.profRegNo}"/>
                                                    <jsp:param name="cssClass" value="new-img-show"/>
                                                </jsp:include>
                                                <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/nameDisciplinaryRecords.jsp">
                                                    <jsp:param name="profRegNo" value="${oldAppSvcPersonnelDtoList.profRegNo}"/>
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
                                                    <span class="newVal " attr="${appSvcPersonnelDtoList.name}">
                                                      <c:out value="${appSvcPersonnelDtoList.name}"/>
                                                    </span>
                                                </div>
                                                <div class="col-xs-6">
                                                    <span class="oldVal " attr="${oldAppSvcPersonnelDtoList.name}" style="display: none">
                                                      <c:out value="${oldAppSvcPersonnelDtoList.name}"/>
                                                    </span>
                                                </div>
                                            </td>
                                        </tr>
                                    </c:if>
                                    <c:if test="${appSvcPersonnelDtoList.personnelType == 'SPPT001' || oldAppSvcPersonnelDtoList.personnelType == 'SPPT001'}">
                                        <tr>
                                            <td class="col-xs-6">
                                                <p class="form-check-label" aria-label="premise-1-cytology">
                                                    Designation
                                                </p>
                                            </td>
                                            <td>
                                                <div class="col-xs-12">
                                                  <span class="newVal " attr="${appSvcPersonnelDtoList.designation}">
                                                          ${appSvcPersonnelDtoList.designation}
                                                  </span>
                                                    <br>
                                                    <span class="oldVal " attr="${oldAppSvcPersonnelDtoList.designation}"
                                                          style="display: none">
                                                            ${oldAppSvcPersonnelDtoList.designation}
                                                    </span>
                                                </div>
                                            </td>
                                        </tr>
                                    </c:if>
                                    <c:if test="${'Others' == appSvcPersonnelDtoList.designation || 'Others' == oldAppSvcPersonnelDtoList.designation}">
                                        <tr>
                                            <td class="col-xs-6">
                                            </td>
                                            <td>
                                                <div class="col-xs-12">
                                                    <span class="newVal " attr="${appSvcPersonnelDtoList.otherDesignation}">
                                                          ${appSvcPersonnelDtoList.otherDesignation}
                                                    </span>
                                                    <br>
                                                    <span class="oldVal " attr="${oldAppSvcPersonnelDtoList.otherDesignation}"
                                                          style="display: none">
                                                            ${oldAppSvcPersonnelDtoList.otherDesignation}
                                                    </span>
                                                </div>
                                            </td>
                                        </tr>
                                    </c:if>
                                    <c:if test="${appSvcPersonnelDtoList.personnelType == 'SPPT001' || oldAppSvcPersonnelDtoList.personnelType == 'SPPT001'
                                        || appSvcPersonnelDtoList.personnelType == 'SPPT002' || oldAppSvcPersonnelDtoList.personnelType == 'SPPT002'}">
                                        <tr>
                                            <td class="col-xs-6">
                                                <p class="form-check-label" aria-label="premise-1-cytology">
                                                    Qualification
                                                </p>
                                            </td>
                                            <td>
                                                <div class="col-xs-12">
                                                    <span class="newVal " attr="${appSvcPersonnelDtoList.qualification}">
                                                          ${appSvcPersonnelDtoList.qualification}
                                                    </span>
                                                    <br>
                                                    <span class="oldVal " attr="${oldAppSvcPersonnelDtoList.qualification}"
                                                          style="display: none">
                                                            ${oldAppSvcPersonnelDtoList.qualification}
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
                                                    <span class="newVal " attr="${appSvcPersonnelDtoList.wrkExpYear}">
                                                        <c:out value="${appSvcPersonnelDtoList.wrkExpYear}"/>
                                                    </span>
                                                </div>
                                                <div class="col-xs-6">
                                                    <span class="oldVal " attr="${oldAppSvcPersonnelDtoList.wrkExpYear}" style="display: none">
                                                        <c:out value="${oldAppSvcPersonnelDtoList.wrkExpYear}"/>
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
                                                    <span class="newVal" attr="${appSvcPersonnelDtoList.profRegNo}">
                                                      <c:out value="${appSvcPersonnelDtoList.profRegNo}"/>
                                                      <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/prsDisciplinaryRecordMark.jsp">
                                                          <jsp:param name="profRegNo" value="${appSvcPersonnelDtoList.profRegNo}"/>
                                                          <jsp:param name="methodName" value="showThisTableNewService"/>
                                                      </jsp:include>
                                                    </span>
                                                </div>
                                                <div class="col-xs-6 img-show">
                                                    <span class="oldVal" attr="${oldAppSvcPersonnelDtoList.profRegNo}" style="display: none">
                                                        ${oldAppSvcPersonnelDtoList.profRegNo}
                                                        <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/prsDisciplinaryRecordMark.jsp">
                                                            <jsp:param name="profRegNo" value="${oldAppSvcPersonnelDtoList.profRegNo}"/>
                                                            <jsp:param name="methodName" value="showThisTableOldService"/>
                                                        </jsp:include>
                                                    </span>
                                                </div>
                                                <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/prsDisciplinaryRecords.jsp">
                                                    <jsp:param name="profRegNo" value="${appSvcPersonnelDtoList.profRegNo}"/>
                                                    <jsp:param name="cssClass" value="new-img-show"/>
                                                </jsp:include>
                                                <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/prsDisciplinaryRecords.jsp">
                                                    <jsp:param name="profRegNo" value="${oldAppSvcPersonnelDtoList.profRegNo}"/>
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
                                                <span class="newVal " attr="${appSvcPersonnelDtoList.name}">
                                                    <c:out value="${appSvcPersonnelDtoList.name}"/>
                                                </span>
                                            </div>
                                            <div class="col-xs-6 img-show">
                                                <span class="oldVal " attr="${oldAppSvcPersonnelDtoList.name}" style="display: none">
                                                        ${oldAppSvcPersonnelDtoList.name}
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
                                                <span class="newVal " attr="${appSvcPersonnelDtoList.qualification}">
                                                        ${appSvcPersonnelDtoList.qualification}
                                                </span>
                                                <br>
                                                <span class="oldVal " attr="${oldAppSvcPersonnelDtoList.qualification}"
                                                      style="display: none">
                                                        ${oldAppSvcPersonnelDtoList.qualification}
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
                                                <span class="newVal " attr="${appSvcPersonnelDtoList.wrkExpYear}">
                                                    <c:out value="${appSvcPersonnelDtoList.wrkExpYear}"/>
                                                </span>
                                            </div>
                                            <div class="col-xs-6">
                                                <span class="oldVal " attr="${oldAppSvcPersonnelDtoList.wrkExpYear}" style="display: none">
                                                        ${oldAppSvcPersonnelDtoList.wrkExpYear}
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
        </div>
    </div>
</div>