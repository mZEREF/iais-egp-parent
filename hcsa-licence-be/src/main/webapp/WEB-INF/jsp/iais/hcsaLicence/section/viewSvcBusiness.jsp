<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>

<div class="amended-service-info-gp">
    <label class="title-font-size">${currStepName}</label>
    <div class="amend-preview-info">
        <div class="form-check-gp">
            <div class="row">
                <div class="col-xs-12">
                <c:forEach var="businessDto" items="${currentPreviewSvcInfo.appSvcBusinessDtoList}" varStatus="status">
                    <c:set var="oldBusiness" value="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcBusinessDtoList[status.index]}"/>
                    <div class="col-xs-12">
                        <span class="newVal " attr="${businessDto.premType}:${businessDto.premAddress}">
                        <strong >
                            <c:choose>
                                <c:when test="${'ONSITE' == businessDto.premType}">
                                    <c:out value="Premises"/>
                                </c:when>
                                <c:when test="${'CONVEYANCE' == businessDto.premType}">
                                    <c:out value="Conveyance"/>
                                </c:when>
                                <c:when test="${'OFFSITE'  == businessDto.premType}">
                                    <c:out value="Off-site"/>
                                </c:when>
                                <c:when test="${'EASMTS'  == businessDto.premType}">
                                    <c:out value="Conveyance"/>
                                </c:when>
                            </c:choose>
                        </strong>
                        : ${businessDto.premAddress}
                        </span>
                    </div>
                    <div class="col-xs-12">
                        <span class="oldVal " style="display: none" attr="${oldBusiness.premType}:${oldBusiness.premAddress}">
                        <strong >
                            <c:choose>
                                <c:when test="${'ONSITE' == oldBusiness.premType}">
                                    <c:out value="Premises"/>
                                </c:when>
                                <c:when test="${'CONVEYANCE' == oldBusiness.premType}">
                                    <c:out value="Conveyance"/>
                                </c:when>
                                <c:when test="${'OFFSITE'  == oldBusiness.premType}">
                                    <c:out value="Off-site"/>
                                </c:when>
                                <c:when test="${'EASMTS'  == oldBusiness.premType}">
                                    <c:out value="Conveyance"/>
                                </c:when>
                            </c:choose>
                        </strong>
                        : ${oldBusiness.premAddress}
                        </span>
                    </div>
                    <span class="col-xs-6"></span>
                    <table  class="col-xs-12" aria-describedby="">
                        <thead style="display: none">
                        <tr>
                            <th scope="col"></th>
                        </tr>
                        </thead>
                        <tr>
                            <td class="col-xs-6">
                                <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Business Name</p>
                            </td>
                            <td >
                                <div class="col-xs-6">
                                    <span class="newVal " attr="${businessDto.businessName}">
                                        <c:out value="${businessDto.businessName}"></c:out>
                                    </span>
                                </div>
                                <div class="col-xs-6">
                                    <span class="oldVal " style="display: none" attr="${oldBusiness.businessName}">
                                        <c:out value="${oldBusiness.businessName}"></c:out>
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