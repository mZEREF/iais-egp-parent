<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<div class="row">
    <div class="col-xs-12">
        <c:forEach items="${currentPreviewSvcInfo.svcPersonnelDto.normalList}" var="normalList"
                   varStatus="status">
            <c:set var="oldNormalList" value="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.svcPersonnelDto.normalList[status.index]}"/>
            <p>
                <strong class="col-xs-6">
                    Service Personnel
                    <c:if test="${fn:length(currentPreviewSvcInfo.svcPersonnelDto.normalList)>1}">
                        ${status.index+1}
                    </c:if>:
                </strong>
            </p>
            <span class="col-xs-6"></span>
            <table aria-describedby="" class="col-xs-12">
                <tr>
                    <td class="col-xs-6">
                        <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Name
                        </p>
                    </td>
                    <td>
                        <div class="col-xs-6">
                                        <span class="newVal " attr="${normalList.name}">
                                            <c:out value="${normalList.name}"/>
                                        </span>
                        </div>
                        <div class="col-xs-6">
                                      <span class="oldVal " attr="${oldNormalList.name}" style="display: none">
                                              ${oldNormalList.name}
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
                        <div class="col-xs-12">
                                        <span class="newVal " attr="${normalList.qualification}"/>
                                              <c:out value="${normalList.qualification}"/>
                                        </span>
                            <br>
                            <span class="oldVal " attr="${oldNormalList.qualification}" style="display: none">
                                   <c:out value="${oldNormalList.qualification}"/>
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
                                        <span class="newVal " attr="${normalList.wrkExpYear}">
                                            <c:out value="${normalList.wrkExpYear}"/>
                                        </span>
                        </div>
                        <div class="col-xs-6">
                                        <span class="oldVal " attr="${oldNormalList.wrkExpYear}" style="display: none">
                                             <c:out value="${oldNormalList.wrkExpYear}"/>
                                        </span>
                        </div>
                    </td>
                </tr>
            </table>
        </c:forEach>
    </div>
</div>