<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<div class="row">
    <div>
        <c:forEach items="${currentPreviewSvcInfo.svcPersonnelDto.specialList}" var="specialList" varStatus="status">
            <c:set var="oldSpecialList" value="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.svcPersonnelDto.specialList[status.index]}"/>
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
                    <c:when test="${currentPreviewSvcInfo.serviceCode=='NMI'}">
                        <tr>
                            <td class="col-xs-6">
                                <div class="form-check-label" aria-label="premise-1-cytology">
                                    <span class="check-square"></span>Select Service Personnel
                                </div>
                            </td>
                            <td>
                                <div class="col-xs-6">
                                    <div class="newVal " attr="${specialList.personnelType}">
                                      <iais:code code="${specialList.personnelType}"/>
                                    </div>
                                </div>
                                <div class="col-xs-6">
                                    <div class="oldVal " attr="${oldSpecialList.personnelType}" style="display: none">
                                      <iais:code code="${oldSpecialList.personnelType}"/>
                                    </div>
                                </div>
                            </td>
                        </tr>

<%--                        name--%>
                        <tr>
                            <td class="col-xs-6">
                                <div class="form-check-label" aria-label="premise-1-cytology">
                                    <span class="check-square"></span>Name
                                </div>
                            </td>
                            <td>
                                <div class="col-xs-6 img-show">
                                    <div class="newVal " attr="${specialList.name}">
                                        <c:out value="${specialList.name}"/>
                                        <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/nameDisciplinaryRecordMark.jsp">
                                            <jsp:param name="profRegNo" value="${specialList.profRegNo}"/>
                                            <jsp:param name="personName" value="${specialList.name}"/>
                                            <jsp:param name="methodName" value="showThisNameTableNewService"/>
                                        </jsp:include>
                                    </div>
                                </div>
                                <div class="col-xs-6 img-show">
                                    <div class="oldVal "  attr="${oldSpecialList.name}" style="display: none">${oldSpecialList.name}
                                        <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/nameDisciplinaryRecordMark.jsp">
                                            <jsp:param name="profRegNo" value="${oldSpecialList.profRegNo}"/>
                                            <jsp:param name="personName" value="${oldSpecialList.name}"/>
                                            <jsp:param name="methodName" value="showThisNameTableOldService"/>
                                        </jsp:include>
                                    </div>
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

<%--                        Designation--%>
                        <c:if test="${specialList.personnelType == 'SPPT001' || oldSpecialList.personnelType == 'SPPT001'}">
                            <tr>
                                <td class="col-xs-6">
                                    <div class="form-check-label" aria-label="premise-1-cytology">
                                        <span class="check-square"></span>Designation
                                    </div>
                                </td>
                                <td>
                                    <div class="col-xs-6">
                                       <div class="newVal " attr="${specialList.designation}">
                                              ${specialList.designation}
                                       </div>
                                    </div>
                                    <div class="col-xs-6">
                                        <div class="oldVal " attr="${oldSpecialList.designation}" style="display: none">
                                                ${oldSpecialList.designation}
                                        </div>
                                    </div>
                                </td>
                            </tr>
                        </c:if>



<%--                        otherDesignation--%>
                        <c:if test="${'Others' == specialList.designation || 'Others' == oldSpecialList.designation}">
                            <tr>
                                <td class="col-xs-6">
                                </td>
                                <td>
                                    <div class="col-xs-6">
                                        <div class="newVal " attr="${specialList.otherDesignation}">
                                                ${specialList.otherDesignation}
                                        </div>
                                    </div>
                                    <div class="col-xs-6">
                                        <div class="oldVal " attr="${oldSpecialList.otherDesignation}" style="display: none">
                                                ${oldSpecialList.otherDesignation}
                                        </div>
                                    </div>
                                </td>
                            </tr>
                        </c:if>


<%--                         years  Qualification --%>
                        <c:if test="${specialList.personnelType == 'SPPT001' || oldSpecialList.personnelType == 'SPPT001'
                                        || specialList.personnelType == 'SPPT002' || oldSpecialList.personnelType == 'SPPT002'}">
                            <tr>
                                <td class="col-xs-6">
                                    <div class="form-check-label" aria-label="premise-1-cytology">
                                        <span class="check-square"></span>Qualification
                                    </div>
                                </td>
                                <td>
                                    <div class="col-xs-6">
                                        <div class="newVal " attr="${specialList.qualification}">
                                                ${specialList.qualification}
                                        </div>
                                    </div>
                                    <div class="col-xs-6">
                                        <div class="oldVal " attr="${oldSpecialList.qualification}" style="display: none">
                                                ${oldSpecialList.qualification}
                                        </div>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="col-xs-6">
                                    <div class="form-check-label" aria-label="premise-1-cytology">
                                        <span class="check-square"></span>Relevant working experience (Years)
                                    </div>
                                </td>
                                <td>
                                    <div class="col-xs-6">
                                        <div class="newVal " attr="${specialList.wrkExpYear}">
                                            <c:out value="${specialList.wrkExpYear}"/>
                                        </div>
                                    </div>
                                    <div class="col-xs-6">
                                        <div class="oldVal " attr="${oldSpecialList.wrkExpYear}" style="display: none">
                                            <c:out value="${oldSpecialList.wrkExpYear}"/>
                                        </div>
                                    </div>
                                </td>
                            </tr>
                        </c:if>


                        <c:if test="${'SPPT004' == specialList.personnelType || 'SPPT004' == oldSpecialList.personnelType}">
                            <tr>
                                <td class="col-xs-6">
                                    <div class="form-check-label" aria-label="premise-1-cytology">
                                        <span class="check-square"></span>Professional Regn No.
                                    </div>
                                </td>
                                <td>
                                    <div class="col-xs-6 img-show">
                                        <div class="newVal" attr="${specialList.profRegNo}">
                                          <c:out value="${specialList.profRegNo}"/>
                                          <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/prsDisciplinaryRecordMark.jsp">
                                              <jsp:param name="profRegNo" value="${specialList.profRegNo}"/>
                                              <jsp:param name="methodName" value="showThisTableNewService"/>
                                          </jsp:include>
                                        </div>
                                    </div>
                                    <div class="col-xs-6 img-show">
                                        <div class="oldVal" attr="${oldSpecialList.profRegNo}" style="display: none">
                                            ${oldSpecialList.profRegNo}
                                            <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/prsDisciplinaryRecordMark.jsp">
                                                <jsp:param name="profRegNo" value="${oldSpecialList.profRegNo}"/>
                                                <jsp:param name="methodName" value="showThisTableOldService"/>
                                            </jsp:include>
                                        </div>
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
                </c:choose>
            </table>
        </c:forEach>
    </div>
</div>