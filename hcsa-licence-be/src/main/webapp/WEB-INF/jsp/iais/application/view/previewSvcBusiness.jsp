<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<div class="amended-service-info-gp">
    <label class="svc-title">${currStepName}</label>
    <div class="amend-preview-info">
        <div class="form-check-gp">
            <div class="row">
                <div class="col-xs-12">
                    <c:forEach items="${currentPreviewSvcInfo.appSvcBusinessDtoList}" var="businessDto" varStatus="status">
                        <div  class="col-xs-12" style="margin-bottom: 1%;margin-top: 1%">
                            <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>
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
                                : ${businessDto.premAddress}</p>
                        </div>
                        <table aria-describedby="" class="col-xs-12">
                            <thead style="display: none">
                            <tr><th scope="col"></th></tr>
                            </thead>
                            <tr>
                                <td class="col-xs-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Business Name</p>
                                </td>
                                <td>
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span><c:out value="${businessDto.businessName}"/></p>
                                </td>
                            </tr>
                        </table>
                    </c:forEach>
                </div>
            </div>
        </div>
    </div>
</div>
