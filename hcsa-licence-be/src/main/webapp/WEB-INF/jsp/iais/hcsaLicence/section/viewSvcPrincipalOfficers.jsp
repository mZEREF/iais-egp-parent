<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>

<div class="amended-service-info-gp">
    <label class="title-font-size">${stepNameMap['SVST004']}</label>
    <div class="amend-preview-info">
        <div class="form-check-gp">
            <div class="row">
                <div class="col-xs-12">
                    <c:set value="1" var="poIndex"></c:set>
                    <c:set value="1" var="dpoIndex"></c:set>
                    <c:forEach items="${currentPreviewSvcInfo.appSvcPrincipalOfficersDtoList}" var="po" varStatus="status">
                        <c:set var="oldPo" value="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcPrincipalOfficersDtoList[status.index]}" />
                        <c:if test="${po.psnType =='PO'}">
                            <p><strong class="col-xs-6">Principal Officer <c:if test="${PO_SIZE>1}">${poIndex}</c:if>:</strong>
                            </p>
                            <c:set var="poIndex" value="${poIndex+1}"></c:set>
                        </c:if>
                        <c:if test="${po.psnType =='DPO'}">
                            <p><strong class="col-xs-6">Nominee <c:if
                                    test="${DPO_SIZE>1}">${dpoIndex}</c:if>:</strong></p>
                            <c:set var="dpoIndex" value="${dpoIndex+1}"></c:set>
                        </c:if>
                        <table aria-describedby="" class="col-xs-12">
                            <tr>
                                <th scope="col" style="display: none"></th>
                                <td class="col-xs-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Salutation</p>
                                </td>
                                <td>
                                    <span class="check-square"></span>
                                    <div class="col-xs-6">
                                        <span class="newVal " attr="${po.salutation}"><iais:code code="${po.salutation}"/></span>
                                    </div>
                                    <div class="col-xs-6">
                                        <span class="oldVal " attr="${oldPo.salutation}" style="display: none">
                                            <iais:code code="${oldPo.salutation}"/>
                                        </span>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="col-xs-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Name</p>
                                </td>
                                <td>
                                    <div class="col-xs-6">
                                        <span class="newVal " attr="${po.name}"><c:out value="${po.name}"/></span>
                                    </div>
                                    <div class="col-xs-6">
                                        <span class="oldVal " attr="${oldPo.name}" style="display: none">
                                                <c:out value="${oldPo.name}"/>
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
                                        <span class="newVal " attr="<iais:code code="${po.idType}"/>"><iais:code code="${po.idType}"/></span>
                                    </div>
                                    <div class="col-xs-6">
                                        <span class="oldVal " attr="<iais:code code="${oldPo.idType}"/>" style="display:none">
                                            <iais:code code="${oldPo.idType}"/>
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
                                        <span class="newVal" attr="${po.idNo}">
                                          <c:out value="${po.idNo}"/>
                                          <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/disciplinaryRecordMark.jsp">
                                              <jsp:param name="idNo" value="${po.idNo}"/>
                                              <jsp:param name="methodName" value="showThisTableNewService"/>
                                          </jsp:include>
                                        </span>
                                    </div>
                                    <div class="col-xs-6 img-show">
                                        <span class="oldVal" attr="${oldPo.idNo}" style="display: none">
                                            <c:out value="${oldPo.idNo}"/>
                                            <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/disciplinaryRecordMark.jsp">
                                                <jsp:param name="idNo" value="${oldPo.idNo}"/>
                                                <jsp:param name="methodName" value="showThisTableOldService"/>
                                            </jsp:include>
                                        </span>
                                    </div>
                                    <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/disciplinaryRecords.jsp">
                                        <jsp:param name="idNo" value="${po.idNo}"/>
                                        <jsp:param name="cssClass" value="new-img-show"/>
                                    </jsp:include>
                                    <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/disciplinaryRecords.jsp">
                                        <jsp:param name="idNo" value="${oldPo.idNo}"/>
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
                                        <span class="newVal " attr="<iais:code code="${po.nationality}"/>">
                                            <iais:code code="${po.nationality}"/>
                                        </span>
                                    </div>
                                    <div class="col-xs-6">
                                        <span class="oldVal " attr="<iais:code code="${oldPo.nationality}"/>" style="display:none">
                                            <iais:code code="${oldPo.nationality}"/>
                                        </span>
                                    </div>
                                </td>
                            </tr>

                            <tr>
                                <td class="col-xs-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology">
                                        <span class="check-square"></span>Designation
                                    </p>
                                </td>
                                <td>
                                    <div class="col-xs-6">
                                        <span class="newVal " attr="${po.designation}"><iais:code code="${po.designation}"/></span>
                                    </div>
                                    <div class="col-xs-6">
                                        <span class="oldVal " attr="${oldPo.designation}" style="display: none">
                                            <iais:code code="${oldPo.designation}"/>
                                        </span>
                                    </div>
                                </td>
                            </tr>
                            <c:if test="${po.designation=='DES999'|| oldPo.designation=='DES999'}">
                                <tr>
                                    <td class="col-xs-6">
                                        <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span></p>
                                    </td>
                                    <td>

                                        <div class="col-xs-6">
                                            <span class="newVal " attr="${po.otherDesignation}">
                                                <iais:code code="${po.otherDesignation}"/>
                                            </span>
                                        </div>
                                        <div class="col-xs-6">
                                            <span class="oldVal " attr="${oldPo.otherDesignation}" style="display: none">
                                                <iais:code code="${oldPo.otherDesignation}"/>
                                            </span>
                                        </div>
                                    </td>
                                </tr>
                            </c:if>
                            <tr>
                                <td class="col-xs-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Office
                                        Telephone No.
                                    </p>
                                </td>
                                <td>
                                    <div class="col-xs-6">
                                        <span class="newVal " attr="${po.officeTelNo}"><c:out value="${po.officeTelNo}"/></span>
                                    </div>
                                    <div class="col-xs-6">
                                        <span class="oldVal " attr="${oldPo.officeTelNo}" style="display: none">
                                            <c:out value="${oldPo.officeTelNo}"/>
                                        </span>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="col-xs-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology">
                                        <span class="check-square"></span>Mobile No.
                                    </p>
                                </td>
                                <td>
                                    <div class="col-xs-6">
                                        <span class="newVal " attr="${po.mobileNo}"><c:out value="${po.mobileNo}"/></span>
                                    </div>
                                    <div class="col-xs-6">
                                        <span class="oldVal " attr="${oldPo.mobileNo}" style="display: none">${oldPo.mobileNo}</span>
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
                                        <span class="newVal " attr="${po.emailAddr}"><c:out value="${po.emailAddr}"/></span>
                                        <br>
                                        <span class="oldVal " attr="${oldPo.emailAddr}" style="display: none">
                                                ${oldPo.emailAddr}
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