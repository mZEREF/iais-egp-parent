<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<div class="amended-service-info-gp">
    <label class="title-font-size">${currStepName}</label>
    <div class="amend-preview-info">
        <div class="form-check-gp">
            <div class="row">
                <div class="">
                    <c:forEach items="${currentPreviewSvcInfo.appSvcMedAlertPersonList}" var="appSvcMedAlertPerson"
                               varStatus="status">
                        <c:set var="oldMap" value="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcMedAlertPersonList[status.index]}" />
                        <p>
                            <strong class="col-xs-6">MedAlert Person
                            <c:if test="${fn:length(currentPreviewSvcInfo.appSvcMedAlertPersonList)>1}">${status.index+1}</c:if>:
                            </strong>
                        </p>
                        <span class="col-xs-6"></span>
                        <table aria-describedby="" class="col-xs-12">

                            <tr>
                                <th scope="col" style="display: none"></th>
                                <td class="col-xs-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><div class="check-square"></div>Salutation
                                    </p>
                                </td>
                                <td>
                                    <div class="col-xs-6">
                                        <div class="newVal " attr="${appSvcMedAlertPerson.salutation}">
                                            <iais:code code="${appSvcMedAlertPerson.salutation}"/>
                                        </div>
                                    </div>
                                    <div class="col-xs-6">
                                        <div class="oldVal " attr="${oldMap.salutation}" style="display: none">
                                            <iais:code code="${oldMap.salutation}"/>
                                        </div>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="col-xs-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><div class="check-square"></div>Name
                                    </p>
                                </td>
                                <td>
                                    <div class="col-xs-6">
                                        <div class="newVal " attr="${appSvcMedAlertPerson.name}"><c:out value="${appSvcMedAlertPerson.name}"/></div>
                                    </div>
                                    <div class="col-xs-6">
                                        <div class="oldVal " attr="${oldMap.name}" style="display: none">
                                              ${oldMap.name}
                                        </div>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="col-xs-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology">
                                        <div class="check-square"></div>ID Type
                                    </p>
                                </td>
                                <td>
                                    <div class="col-xs-6">
                                        <div class="newVal " attr="${appSvcMedAlertPerson.idType}"><iais:code code="${appSvcMedAlertPerson.idType}"/></div>
                                    </div>
                                    <div class="col-xs-6">
                                        <div class="oldVal " attr="${oldMap.idType}" style="display: none">
                                            <iais:code code="${oldMap.idType}"/>
                                        </div>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="col-xs-6 img-show">
                                    <p class="form-check-label" aria-label="premise-1-cytology">
                                        <div class="check-square"></div>ID No.
                                    </p>
                                </td>
                                <td>
                                    <div class="col-xs-6 img-show">
                                        <div class="newVal " attr="${appSvcMedAlertPerson.idNo}">
                                          <c:out value="${appSvcMedAlertPerson.idNo}"/>
                                          <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/disciplinaryRecordMark.jsp">
                                              <jsp:param name="idNo" value="${appSvcMedAlertPerson.idNo}"/>
                                              <jsp:param name="methodName" value="showThisTableNewService"/>
                                          </jsp:include>
                                        </div>
                                    </div>
                                    <div class="col-xs-6 img-show">
                                        <div class="oldVal" attr="${oldMap.idNo}" style="display: none">
                                            ${oldMap.idNo}
                                            <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/disciplinaryRecordMark.jsp">
                                                <jsp:param name="idNo" value="${oldMap.idNo}"/>
                                                <jsp:param name="methodName" value="showThisTableOldService"/>
                                            </jsp:include>
                                        </div>
                                    </div>
                                    <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/disciplinaryRecords.jsp">
                                        <jsp:param name="idNo" value="${appSvcMedAlertPerson.idNo}"/>
                                        <jsp:param name="cssClass" value="new-img-show"/>
                                    </jsp:include>
                                    <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/disciplinaryRecords.jsp">
                                        <jsp:param name="idNo" value="${oldMap.idNo}"/>
                                        <jsp:param name="cssClass" value="old-img-show"/>
                                    </jsp:include>
                                </td>
                            </tr>
                            <c:if test="${appSvcMedAlertPerson.idType == 'IDTYPE003' || oldMap.idType == 'IDTYPE003'}">
                            <tr>
                                <td class="col-xs-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology">
                                        <div class="check-square"></div>Nationality
                                    </p>
                                </td>
                                <td>
                                    <div class="col-xs-6">
                                        <div class="newVal " attr="${appSvcMedAlertPerson.nationality}">
                                            <iais:code code="${appSvcMedAlertPerson.nationality}"/>
                                        </div>
                                    </div>
                                    <div class="col-xs-6">
                                        <div class="oldVal " attr="${oldMap.nationality}" style="display: none">
                                            <iais:code code="${oldMap.nationality}"/>
                                        </div>
                                    </div>
                                </td>
                            </tr>
                            </c:if>
                            <tr>
                                <td class="col-xs-6">
                                    <div class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Mobile
                                        No.</div>
                                </td>
                                <td>
                                    <div class="col-xs-6">
                                        <div class="newVal " attr="${appSvcMedAlertPerson.mobileNo}">
                                            <c:out value="${appSvcMedAlertPerson.mobileNo}"/>
                                        </div>
                                    </div>
                                    <div class="col-xs-6">
                                        <div class="oldVal " attr="${oldMap.mobileNo}" style="display: none">
                                            ${oldMap.mobileNo}
                                        </div>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="col-xs-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology">
                                        <div class="check-square"></div>Email Address
                                    </p>
                                </td>
                                <td>
                                    <div class="col-xs-12">
                                        <div class="newVal " attr="${appSvcMedAlertPerson.emailAddr}">
                                            <c:out value="${appSvcMedAlertPerson.emailAddr}"/>
                                        </div>
                                        <div class="oldVal " attr="${oldMap.emailAddr}" style="display: none">
                                            <c:out value="${oldMap.emailAddr}"/>
                                        </div>
                                    </div>
                                </td>
                            </tr>
                        </table>
                    </c:forEach>
                </div>
            </div>
        </div>
    </div>
</div>