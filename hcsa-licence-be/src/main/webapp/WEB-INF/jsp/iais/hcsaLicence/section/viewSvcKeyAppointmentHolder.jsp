<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<div class="amended-service-info-gp">
    <label class="title-font-size">${stepNameMap['SVST014']}</label>
    <div class="amend-preview-info">
        <div class="form-check-gp">
            <div class="row">
                <div class="col-xs-12">
                    <c:forEach items="${currentPreviewSvcInfo.appSvcKeyAppointmentHolderDtoList}" var="keyAppointmentHolder"
                               varStatus="status">
                        <c:set var="oldKah" value="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcKeyAppointmentHolderDtoList[status.index]}" />
                        <p><strong class="col-xs-6">Key Appointment Holder <c:if
                                test="${fn:length(currentPreviewSvcInfo.appSvcKeyAppointmentHolderDtoList)>1}">${status.index+1}</c:if>:</strong>
                        </p>
                        <span class="col-xs-6"></span>
                        <table aria-describedby="" class="col-xs-12">
                            <tr>
                                <th scope="col" style="display: none"></th>
                                <td class="col-xs-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Salutation
                                    </p>
                                </td>
                                <td>
                                    <div class="col-xs-6">
                                      <span class="newVal " attr="${keyAppointmentHolder.salutation}">
                                          <iais:code code="${keyAppointmentHolder.salutation}"/>
                                      </span>
                                    </div>
                                    <div class="col-xs-6">
                                      <span class="oldVal " attr="${oldKah.salutation}" style="display: none">
                                          <iais:code code="${oldKah.salutation}"/>
                                      </span>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="col-xs-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology">
                                        <span class="check-square"></span>Name
                                    </p>
                                </td>
                                <td>
                                    <div class="col-xs-6">
                                        <span class="newVal " attr="${keyAppointmentHolder.name}"><c:out value="${keyAppointmentHolder.name}"/></span>
                                    </div>
                                    <div class="col-xs-6">
                                        <span class="oldVal " attr="${oldKah.name}" style="display: none">
                                            <c:out value="${oldKah.name}"/>
                                        </span>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="col-xs-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology">
                                        <span class="check-square"></span>ID Type
                                    </p>
                                </td>
                                <td>
                                    <div class="col-xs-6">
                                        <span class="newVal " attr="<iais:code code="${keyAppointmentHolder.idType}"/>"><iais:code code="${keyAppointmentHolder.idType}"/></span>
                                    </div>
                                    <div class="col-xs-6">
                                        <span class="oldVal " attr="<iais:code code="${oldKah.idType}"/>" style="display: none">
                                            <iais:code code="${oldKah.idType}"/>
                                        </span>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="col-xs-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology">
                                        <span class="check-square"></span>ID No.
                                    </p>
                                </td>
                                <td>
                                    <div class="col-xs-6 img-show">
                                        <span class="newVal" attr="${keyAppointmentHolder.idNo}">
                                            <c:out value="${keyAppointmentHolder.idNo}"/>
                                            <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/disciplinaryRecordMark.jsp">
                                                <jsp:param name="idNo" value="${keyAppointmentHolder.idNo}"/>
                                                <jsp:param name="methodName" value="showThisTableNewService"/>
                                            </jsp:include>
                                        </span>
                                    </div>
                                    <div class="col-xs-6 img-show">
                                        <span class="oldVal" attr="${oldKah.idNo}" style="display: none">
                                            <c:out value="${oldKah.idNo}"/>
                                            <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/disciplinaryRecordMark.jsp">
                                                <jsp:param name="idNo" value="${oldKah.idNo}"/>
                                                <jsp:param name="methodName" value="showThisTableOldService"/>
                                            </jsp:include>
                                        </span>
                                    </div>
                                    <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/disciplinaryRecords.jsp">
                                        <jsp:param name="idNo" value="${keyAppointmentHolder.idNo}"/>
                                        <jsp:param name="cssClass" value="new-img-show"/>
                                    </jsp:include>
                                    <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/disciplinaryRecords.jsp">
                                        <jsp:param name="idNo" value="${oldKah.idNo}"/>
                                        <jsp:param name="cssClass" value="old-img-show"/>
                                    </jsp:include>
                                </td>
                            </tr>
                            <tr>
                                <td class="col-xs-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology">
                                        <span class="check-square"></span>Nationality
                                    </p>
                                </td>
                                <td>
                                    <div class="col-xs-6">
                                        <span class="newVal " attr="${keyAppointmentHolder.nationality}">
                                            <iais:code code="${keyAppointmentHolder.nationality}"/>
                                        </span>
                                    </div>
                                    <div class="col-xs-6">
                                        <span class="oldVal " attr="${oldKah.nationality}" style="display: none">
                                            <iais:code code="${oldKah.nationality}"/>
                                        </span>
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