<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<div class="row">
    <div class="col-xs-12">
        <c:forEach items="${currentPreviewSvcInfo.svcPersonnelDto.embryologistList}" var="embryologistList"
                   varStatus="status">
            <c:set var="oldEmbryologistList"
                   value="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.svcPersonnelDto.embryologistList[status.index]}"/>
            <p>
                <strong class="col-xs-6">
                    Embryologist
                    <c:if test="${fn:length(currentPreviewSvcInfo.svcPersonnelDto.embryologistList)>1}">
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
                                        <span class="newVal " attr="${embryologistList.salutation}">
                                            <iais:code code="${embryologistList.salutation}"/>
                                        </span>
                        </div>
                        <div class="col-xs-6">
                                        <span class="oldVal " attr="${oldEmbryologistList.salutation}"
                                              style="display: none">
                                            <iais:code code="${oldEmbryologistList.salutation}"/>
                                        </span>
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
                                                <span class="newVal " attr="${embryologistList.name}">
                                                  <c:out value="${embryologistList.name}"/>
                                                  <jsp:include
                                                          page="/WEB-INF/jsp/iais/hcsaLicence/section/nameDisciplinaryRecordMark.jsp">
                                                      <jsp:param name="profRegNo"
                                                                 value="${embryologistList.profRegNo}"/>
                                                      <jsp:param name="personName" value="${embryologistList.name}"/>
                                                      <jsp:param name="methodName" value="showThisNameTableNewService"/>
                                                  </jsp:include>
                                                </span>
                        </div>
                        <div class="col-xs-6 img-show">
                                                <span class="oldVal "
                                                      attr="${oldEmbryologistList.name}"
                                                      style="display: none">${oldEmbryologistList.name}
                                                  <jsp:include
                                                          page="/WEB-INF/jsp/iais/hcsaLicence/section/nameDisciplinaryRecordMark.jsp">
                                                      <jsp:param name="profRegNo"
                                                                 value="${oldEmbryologistList.profRegNo}"/>
                                                      <jsp:param name="personName"
                                                                 value="${oldEmbryologistList.name}"/>
                                                      <jsp:param name="methodName" value="showThisNameTableOldService"/>
                                                  </jsp:include>
                                                </span>
                        </div>
                        <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/nameDisciplinaryRecords.jsp">
                            <jsp:param name="profRegNo" value="${embryologistList.profRegNo}"/>
                            <jsp:param name="cssClass" value="new-img-show"/>
                        </jsp:include>
                        <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/nameDisciplinaryRecords.jsp">
                            <jsp:param name="profRegNo" value="${oldEmbryologistList.profRegNo}"/>
                            <jsp:param name="cssClass" value="old-img-show"/>
                        </jsp:include>
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
                                                <span class="newVal " attr="${embryologistList.qualification}">
                                                    <c:out value="${embryologistList.qualification}"/>
                                                </span>
                        </div>
                        <div class="col-xs-6">
                                                <span class="oldVal " attr="${oldEmbryologistList.qualification}"
                                                      style="display: none">
                                                        ${oldEmbryologistList.qualification}
                                                </span>
                        </div>
                    </td>
                </tr>


                    <%--    Working Experience(in term of years)--%>
                <tr>
                    <td class="col-xs-6">
                        <p class="form-check-label" aria-label="premise-1-cytology">
                            Working Experience(in term of years)
                        </p>
                    </td>
                    <td>
                        <div class="col-xs-6">
                                                <span class="newVal " attr="${embryologistList.wrkExpYear}">
                                                    <c:out value="${embryologistList.wrkExpYear}"/>
                                                </span>
                        </div>
                        <div class="col-xs-6">
                                                <span class="oldVal " attr="${oldEmbryologistList.wrkExpYear}"
                                                      style="display: none">
                                                        ${oldEmbryologistList.wrkExpYear}
                                                </span>
                        </div>
                    </td>
                </tr>


                    <%--    Number of AR procedures done under supervision--%>
                <tr>
                    <td class="col-xs-6">
                        <p class="form-check-label" aria-label="premise-1-cytology">
                            Number of AR procedures done under supervision
                        </p>
                    </td>
                    <td>
                        <div class="col-xs-6">
                                                <span class="newVal " attr="${embryologistList.numberSupervision}">
                                                    <c:out value="${embryologistList.numberSupervision}"/>
                                                </span>
                        </div>
                        <div class="col-xs-6">
                                                <span class="oldVal " attr="${oldEmbryologistList.numberSupervision}"
                                                      style="display: none">
                                                        ${oldEmbryologistList.numberSupervision}
                                                </span>
                        </div>
                    </td>
                </tr>


                    <%--    Is the Embryologist authorized?--%>
                <tr>
                    <td class="col-xs-6">
                        <p class="form-check-label" aria-label="premise-1-cytology">
                            Is the Embryologist authorized?
                        </p>
                    </td>
                    <td>
                        <div class="col-xs-2">
                            <c:if test="${embryologistList.embryologistAuthorized == '1' || oldEmbryologistList.embryologistAuthorized == '1'}">
                            <span class="newVal " attr="${embryologistList.embryologistAuthorized}">
                                                    <c:out value="Yes"/>
                                                </span>
                                <div class="col-xs-2">
                                                <span class="oldVal "
                                                      attr="${oldEmbryologistList.embryologistAuthorized}"
                                                      style="display: none">
                                                         <c:out value="Yes"/>
                                                </span>
                                </div>
                            </c:if>
                            <c:if test="${embryologistList.embryologistAuthorized == '0' || oldEmbryologistList.embryologistAuthorized == '0'}">
                            <span class="newVal" attr="${embryologistList.embryologistAuthorized}">
                                                    <c:out value="No"/>
                                                </span>
                                <div class="col-xs-2">
                                                <span class="oldVal "
                                                      attr="${oldEmbryologistList.embryologistAuthorized}"
                                                      style="display: none">
                                                        <c:out value="No"/>
                                                </span>
                                </div>
                            </c:if>
                        </div>
                    </td>

                </tr>


            </table>
        </c:forEach>
    </div>
</div>
