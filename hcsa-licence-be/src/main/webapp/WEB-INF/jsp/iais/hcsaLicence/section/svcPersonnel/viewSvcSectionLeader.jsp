<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<div class="amended-service-info-gp">
    <label class="title-font-size">${currStepName}</label>
    <div class="amend-preview-info">
        <div class="form-check-gp">
            <div class="row">
                <div class="">
                    <c:forEach items="${currentPreviewSvcInfo.appSvcSectionLeaderList}" var="sectionLeader"
                               varStatus="status">
                        <c:set var="oldSectionLeader" value="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcSectionLeaderList[status.index]}"/>
                        <p>
                            <strong class="col-xs-10">
                                Section Leader
                                <c:if test="${fn:length(currentPreviewSvcInfo.appSvcSectionLeaderList)>1}">
                                    ${status.index+1}
                                </c:if>:
                            </strong>
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
                                        <span class="newVal " attr="${sectionLeader.salutation}">
                                            <iais:code code="${sectionLeader.salutation}"/>
                                        </span>
                                    </div>
                                    <div class="col-xs-6">
                                        <span class="oldVal " attr="${oldSectionLeader.salutation}" style="display: none">
                                            <iais:code code="${oldSectionLeader.salutation}"/>
                                        </span>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="col-xs-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Name
                                    </p>
                                </td>
                                <td>
                                    <div class="col-xs-6">
                                        <span class="newVal " attr="${sectionLeader.name}">
                                            <c:out value="${sectionLeader.name}"/>
                                        </span>
                                    </div>
                                    <div class="col-xs-6">
                                      <span class="oldVal " attr="${oldSectionLeader.name}" style="display: none">
                                              ${oldSectionLeader.name}
                                      </span>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="col-xs-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Qualification
                                    </p>
                                </td>
                                <td>
                                    <div class="col-xs-6">
                                        <span class="newVal " attr="<iais:code code="${sectionLeader.qualification}"/>">
                                            <iais:code code="${sectionLeader.qualification}"/>
                                        </span>
                                    </div>
                                    <div class="col-xs-6">
                                        <span class="oldVal " attr="<iais:code code="${oldSectionLeader.qualification}"/>" style="display: none">
                                            <iais:code code="${oldSectionLeader.qualification}"/>
                                        </span>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="col-xs-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Working
                                        Experience (in terms of years)</p>
                                </td>
                                <td>
                                    <div class="col-xs-6">
                                        <span class="newVal " attr="${sectionLeader.wrkExpYear}">
                                            <c:out value="${sectionLeader.wrkExpYear}"/>
                                        </span>
                                    </div>
                                    <div class="col-xs-6">
                                        <span class="oldVal " attr="${oldSectionLeader.wrkExpYear}" style="display: none">
                                            ${oldSectionLeader.wrkExpYear}
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