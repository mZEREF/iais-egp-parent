<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<div class="amended-service-info-gp">
    <label class="title-font-size">${stepNameMap['SVST002']}</label>
    <div class="amend-preview-info">
        <c:forEach var="cgo" items="${currentPreviewSvcInfo.appSvcCgoDtoList}" varStatus="status">
            <c:set var="oldCgo" value="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcCgoDtoList[status.index]}" />
            <p>
                <strong class="col-xs-6">Clinical Governance Officer
                    <c:if test="${fn:length(currentPreviewSvcInfo.appSvcCgoDtoList)>1}">${status.index+1}</c:if>:
                </strong>
                <span class="col-xs-4 col-md-4"></span>
            </p>
            <div class="form-check-gp">
                <div class="row">
                    <div class="col-xs-12">
                        <table aria-describedby="" class="col-xs-12">
                            <tr>
                                <th scope="col" style="display: none"></th>
                                <td class="col-xs-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Salutation</p>
                                </td>
                                <td>

                                    <div class="col-xs-6 col-md-6">
                                        <span class="newVal " attr="${cgo.salutation}"><iais:code code="${cgo.salutation}"/></span>
                                    </div>
                                    <div class="col-xs-6 col-md-6">
                                      <span class=" oldVal" attr="${oldCgo.salutation}" style="display: none">
                                          <iais:code code="${oldCgo.salutation}"/>
                                      </span>
                                    </div>
                                </td>
                            </tr>

                            <tr>
                                <td class="col-xs-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Name</p>
                                </td>
                                <td>
                                    <div class="col-xs-6 img-show">
                                      <span class="newVal " attr="${cgo.name}">
                                        <c:out value="${cgo.name}"/>
                                        <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/nameDisciplinaryRecordMark.jsp">
                                            <jsp:param name="profRegNo" value="${cgo.profRegNo}"/>
                                            <jsp:param name="personName" value="${cgo.name}"/>
                                            <jsp:param name="methodName" value="showThisNameTableNewService"/>
                                        </jsp:include>
                                      </span>
                                    </div>
                                    <div class="col-xs-6 img-show">
                                      <span class="oldVal " attr="${oldCgo.name}" style="display: none">
                                        ${oldCgo.name}
                                        <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/nameDisciplinaryRecordMark.jsp">
                                            <jsp:param name="profRegNo" value="${oldCgo.profRegNo}"/>
                                            <jsp:param name="personName" value="${oldCgo.name}"/>
                                            <jsp:param name="methodName" value="showThisNameTableOldService"/>
                                        </jsp:include>
                                      </span>
                                    </div>
                                    <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/nameDisciplinaryRecords.jsp">
                                        <jsp:param name="profRegNo" value="${cgo.profRegNo}"/>
                                        <jsp:param name="cssClass" value="new-img-show"/>
                                    </jsp:include>
                                    <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/nameDisciplinaryRecords.jsp">
                                        <jsp:param name="profRegNo" value="${oldCgo.profRegNo}"/>
                                        <jsp:param name="cssClass" value="old-img-show"/>
                                    </jsp:include>
                                </td>
                            </tr>

                            <tr>
                                <td class="col-xs-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology">
                                        <span class="check-square"></span>ID Type
                                    </p>
                                </td>
                                <td>
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>
                                    <div class="col-xs-6">
                                        <span class="newVal " attr="${cgo.idType}"><iais:code code="${cgo.idType}"/></span>
                                    </div>
                                    <div class="col-xs-6">
                                      <span class=" oldVal" attr="${oldCgo.idType}" style="display: none">
                                          <iais:code code="${oldCgo.idType}"/>
                                      </span>
                                    </div>
                                    </p>
                                </td>
                            </tr>

                            <tr>
                                <td class="col-xs-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>ID No.</p>
                                </td>
                                <td>
                                    <div class="col-xs-6 img-show">
                                        <span class="newVal" attr="${cgo.idNo}">
                                          <c:out value="${cgo.idNo}"/>
                                          <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/disciplinaryRecordMark.jsp">
                                              <jsp:param name="idNo" value="${cgo.idNo}"/>
                                              <jsp:param name="methodName" value="showThisTableNewService"/>
                                          </jsp:include>
                                        </span>
                                    </div>

                                    <div class="col-xs-6 img-show">
                                      <span class="oldVal" attr="${oldCgo.idNo}" style="display: none">
                                          ${oldCgo.idNo}
                                          <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/disciplinaryRecordMark.jsp">
                                              <jsp:param name="idNo" value="${oldCgo.idNo}"/>
                                              <jsp:param name="methodName" value="showThisTableOldService"/>
                                          </jsp:include>
                                      </span>
                                    </div>
                                    <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/disciplinaryRecords.jsp">
                                        <jsp:param name="idNo" value="${cgo.idNo}"/>
                                        <jsp:param name="cssClass" value="new-img-show"/>
                                    </jsp:include>
                                    <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/disciplinaryRecords.jsp">
                                        <jsp:param name="idNo" value="${oldCgo.idNo}"/>
                                        <jsp:param name="cssClass" value="old-img-show"/>
                                    </jsp:include>
                                </td>
                            </tr>
                            <c:if test="${cgo.idType == 'IDTYPE003' || oldCgo.idType == 'IDTYPE003'}">
                            <tr>
                                <td class="col-xs-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology">
                                        <span class="check-square"></span>Nationality
                                    </p>
                                </td>
                                <td>
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>
                                    <div class="col-xs-6 col-md-6">
                                        <span class="newVal " attr="${cgo.nationality}"><iais:code code="${cgo.nationality}"/></span>
                                    </div>
                                    <div class="col-xs-6 col-md-6">
                                      <span class="oldVal " attr="${oldCgo.nationality}" style="display: none">
                                        <iais:code code="${oldCgo.nationality}"/>
                                      </span>
                                    </div>
                                    </p>
                                </td>
                            </tr>
                            </c:if>
                            <tr>
                                <td class="col-xs-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Designation
                                    </p>
                                </td>
                                <td>
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>
                                    <div class="col-xs-6 col-md-6">
                                        <span class="newVal " attr="${cgo.designation}"><iais:code code="${cgo.designation}"/></span>

                                    </div>
                                    <div class="col-xs-6 col-md-6">
                                      <span class="oldVal " attr="${oldCgo.designation}" style="display: none">
                                          <iais:code code="${oldCgo.designation}"/>
                                      </span>
                                    </div>
                                    </p>
                                </td>
                            </tr>
                            <c:if test="${cgo.designation=='DES999'||oldCgo.designation=='DES999'}">
                                <tr>
                                    <td class="col-xs-6">
                                        <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>
                                        </p>
                                    </td>
                                    <td>
                                        <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>
                                        <div class="col-xs-12 col-md-12">
                                            <span class="newVal " attr="${cgo.otherDesignation}"><iais:code code="${cgo.otherDesignation}"/></span>
                                            <br>
                                            <span class="oldVal " attr="${oldCgo.otherDesignation}" style="display: none">
                                                <iais:code code="${oldCgo.otherDesignation}"/>
                                            </span>
                                        </div>
                                        </p>
                                    </td>
                                </tr>
                            </c:if>

                            <tr>
                                <td class="col-xs-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Professional
                                        Type</p>
                                </td>
                                <td>
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>
                                    <div class="col-xs-6">
                                      <span class="newVal " attr="${cgo.professionType}">
                                          <iais:code code="${cgo.professionType}"/>
                                      </span>
                                    </div>
                                    <div class="col-xs-6">
                                      <span class="oldVal" attr="${oldCgo.professionType}" style="display: none">
                                          <iais:code code="${oldCgo.professionType}"/>
                                      </span>
                                    </div>
                                    </p>
                                </td>
                            </tr>
                            <tr>
                                <td class="col-xs-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Professional
                                        Regn. No.</p>
                                </td>
                                <td>
                                    <div class="col-xs-6 img-show">
                                      <span class="newVal " attr="${cgo.profRegNo}">
                                        <c:out value="${cgo.profRegNo}"/>
                                        <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/prsDisciplinaryRecordMark.jsp">
                                            <jsp:param name="profRegNo" value="${cgo.profRegNo}"/>
                                            <jsp:param name="methodName" value="showThisTableNewService"/>
                                        </jsp:include>
                                      </span>
                                    </div>
                                    <div class="col-xs-6 img-show">
                                      <span class="oldVal " attr="${oldCgo.profRegNo}" style="display: none">
                                        <c:out value="${oldCgo.profRegNo}"/>
                                        <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/prsDisciplinaryRecordMark.jsp">
                                            <jsp:param name="profRegNo" value="${oldCgo.profRegNo}"/>
                                            <jsp:param name="methodName" value="showThisTableOldService"/>
                                        </jsp:include>
                                      </span>
                                    </div>
                                    <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/prsDisciplinaryRecords.jsp">
                                        <jsp:param name="profRegNo" value="${cgo.profRegNo}"/>
                                        <jsp:param name="cssClass" value="new-img-show"/>
                                    </jsp:include>
                                    <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/prsDisciplinaryRecords.jsp">
                                        <jsp:param name="profRegNo" value="${oldCgo.profRegNo}"/>
                                        <jsp:param name="cssClass" value="old-img-show"/>
                                    </jsp:include>
                                </td>
                            </tr>
                            <tr>
                                <td class="col-xs-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Specialty
                                    </p>
                                </td>
                                <td>
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>
                                    <div class="col-xs-6">
                                        <span class="newVal " attr="${cgo.speciality}">
                                           <c:out value="${cgo.speciality}"/>
                                        </span>
                                        <br>
                                        <span class="oldVal " attr="${oldCgo.speciality}" style="display: none">
                                            <c:out value="${oldCgo.speciality}"/>
                                        </span>
                                    </div>
                                    </p>
                                </td>
                            </tr>
                            <tr>
                                <td class="col-xs-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Sub-specialty</p>
                                </td>
                                <td>
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>
                                    <div class="col-xs-12">
                                        <span class="newVal " attr="${cgo.subSpeciality}"><c:out value="${cgo.subSpeciality}"/></span>
                                        <br>
                                        <span class="oldVal " attr="${oldCgo.subSpeciality}" style="display: none">
                                            ${oldCgo.subSpeciality}
                                        </span>
                                    </div>
                                    </p>
                                </td>
                            </tr>
                            <tr>
                                <td class="col-xs-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Qualification</p>
                                </td>
                                <td>
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>
                                    <div class="col-xs-12">
                                        <span class="newVal " attr="${cgo.qualification}"><c:out value="${cgo.qualification}"/></span>
                                        <br>
                                        <span class="oldVal " attr="${oldCgo.qualification}" style="display: none">
                                            ${oldCgo.qualification}
                                        </span>
                                    </div>
                                    </p>
                                </td>
                            </tr>
                            <tr>
                                <td class="col-xs-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Other Qualification</p>
                                </td>
                                <td>
                                    <div class="col-xs-6">
                                        <span class="newVal " attr="${cgo.otherQualification}"><c:out value="${cgo.otherQualification}"/></span>
                                        <br>
                                        <span class="oldVal "
                                              attr="${oldCgo.otherQualification}"
                                              style="display: none">${oldCgo.otherQualification}</span>
                                    </div>

                                </td>
                            </tr>
                            <tr>
                                <td class="col-xs-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Mobile
                                        No.</p>
                                </td>
                                <td>
                                    <div class="col-xs-6">
                                        <span class="newVal " attr="${cgo.mobileNo}"><c:out value="${cgo.mobileNo}"/></span>
                                    </div>
                                    <div class="col-xs-6">
                                      <span class="oldVal " attr="${oldCgo.mobileNo}" style="display: none">
                                        ${oldCgo.mobileNo}
                                      </span>
                                    </div>
                                </td>
                            </tr>

                            <tr>
                                <td class="col-xs-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology">
                                        <span class="check-square"></span>Email Address
                                    </p>
                                </td>
                                <td>
                                    <div class="col-xs-12">
                                        <span class="newVal " attr="${cgo.emailAddr}"><c:out value="${cgo.emailAddr}"/></span>
                                        <br>
                                        <span class="oldVal " attr="${oldCgo.emailAddr}" style="display: none">
                                            ${oldCgo.emailAddr}
                                        </span>
                                    </div>

                                </td>
                            </tr>
                        </table>
                    </div>
                </div>
            </div>
        </c:forEach>
    </div>
</div>