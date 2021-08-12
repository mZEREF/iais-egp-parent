<%--
  Created by IntelliJ IDEA.
  User: ZiXian
  Date: 2021/6/21
  Time: 17:52
  To change this template use File | Settings | File Templates.
--%>
<c:forEach var="stepSchem" items="${currentPreviewSvcInfo.hcsaServiceStepSchemeDtos}">
    <c:if test="${stepSchem.stepCode == 'SVST012'}">
        <c:set var="currStepName" value="${stepSchem.stepName}"/>
    </c:if>
</c:forEach>
<div class="amended-service-info-gp">
    <label style="font-size: 2.2rem">${currStepName}</label>
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
