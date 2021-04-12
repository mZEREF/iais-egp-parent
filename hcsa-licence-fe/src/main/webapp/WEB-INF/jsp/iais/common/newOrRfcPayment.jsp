<table class="table">
    <thead>
    <tr>
        <th>Service</th>
        <th>Application Type</th>
        <th>Application No.</th>
        <th>Amount</th>
    </tr>
    </thead>
    <tbody>
    <c:set var="onlySpec" value="false"/>
    <c:forEach var="appSvcDto" items="${AppSubmissionDto.appSvcRelatedInfoDtoList}">
        <c:if test="${!empty appSvcDto.relLicenceNo}">
            <c:set var="onlySpec" value="true"/>
        </c:if>
    </c:forEach>
    <c:choose>
        <c:when test="${'APTY005' ==AppSubmissionDto.appType}">
            <c:forEach var="svc" items="${appSubmissionDtos}">
                <tr>
                    <td>
                        <p><c:out value="${svc.serviceName}"></c:out></p>
                    </td>
                    <td>
                        <p>Amendment</p>
                    </td>
                    <td>
                        <p><c:out value="${svc.appGrpNo}"></c:out></p>
                    </td>
                    <td>
                        <p><c:out value="${svc.amountStr}"></c:out></p>
                    </td>
                </tr>
            </c:forEach>
        </c:when>

        <c:when test="${AppSubmissionDto.groupLic && 'APTY002' == AppSubmissionDto.appType &&!onlySpec}">
            <c:forEach items="${AppSubmissionDto.feeInfoDtos}" var="feeInfoDto">
                <c:set var="baseSvcFeeExt" value="${feeInfoDto.baseSvcFeeExt}"/>
                <c:set var="complexSpecifiedFeeExt" value="${feeInfoDto.complexSpecifiedFeeExt}"/>
                <c:set var="simpleSpecifiedFeeExt" value="${feeInfoDto.simpleSpecifiedFeeExt}"/>
                <tr>
                    <td>
                        <c:forEach var="svcName" items="${baseSvcFeeExt.svcNames}">
                            <p>
                                <c:out value="${svcName}"></c:out>
                                &nbsp;(Group - ${AppSubmissionDto.appGrpPremisesDtoList.size()} Premises)
                            </p>
                        </c:forEach>
                    </td>
                    <td>
                        <p>New Licence</p>
                    </td>
                    <td>
                        <p>
                            <c:out value="${AppSubmissionDto.appGrpNo}"></c:out>
                        </p>
                    </td>
                    <td>
                        <p><c:out value="${baseSvcFeeExt.totalAmoumtStr}"></c:out></p>
                    </td>
                </tr>
                <!--complexSpecifiedFeeExt -->
                <c:if test="${complexSpecifiedFeeExt.svcNames.size()>0 }">
                    <tr>
                        <td class="breakdown">
                            <p>&nbsp;&nbsp;
                                <em>Complex Special Licensable Services
                                    <c:if test="${complexSpecifiedFeeExt.svcNames.size()>1}">(${complexSpecifiedFeeExt.svcNames.size()}) </c:if>
                                    (1 x ${AppSubmissionDto.appGrpPremisesDtoList.size()} premises)
                                </em>
                            </p>
                            <c:forEach var="svcName" items="${complexSpecifiedFeeExt.svcNames}">
                                <p>&nbsp;&nbsp;- <c:out value="${svcName}"></c:out></p>
                            </c:forEach>
                        </td>
                        <td>
                            <p></p>
                        </td>
                        <td>
                            <p> </p>
                        </td>
                        <td>
                            <p >
                                <c:out value="${complexSpecifiedFeeExt.totalAmoumtStr}"></c:out>
                            </p>
                        </td>
                    </tr>
                </c:if>
            </c:forEach>
        </c:when>

        <c:when test="${'APTY002' == AppSubmissionDto.appType && onlySpec}">
            <c:forEach items="${AppSubmissionDto.feeInfoDtos}" var="feeInfoDto">
                <c:set var="baseSvcFeeExt" value="${feeInfoDto.baseSvcFeeExt}"/>
                <c:set var="complexSpecifiedFeeExt" value="${feeInfoDto.complexSpecifiedFeeExt}"/>
                <c:set var="simpleSpecifiedFeeExt" value="${feeInfoDto.simpleSpecifiedFeeExt}"/>
                <c:if test="${!empty baseSvcFeeExt}">
                    <tr>
                        <td>
                            <c:forEach var="svcName" items="${baseSvcFeeExt.svcNames}">
                                <p>
                                    <c:out value="${svcName}"></c:out>
                                    <c:if test="${multiplePrem}">
                                        &nbsp;(Premises #${stat.index+1})
                                    </c:if>
                                </p>
                            </c:forEach>
                        </td>
                        <td>
                            <p>
                                New Licence
                            </p>
                        </td>
                        <td>
                            <p>
                                <c:out value="${AppSubmissionDto.appGrpNo}"></c:out>
                            </p>
                        </td>
                        <td>
                            <p>
                                <c:choose>
                                    <c:when test="${'OFFSITE' == premises.premisesType && !onlyOffsite}">
                                        <c:out value="$0"></c:out>
                                    </c:when>
                                    <c:otherwise>
                                        <c:out value="${baseSvcFeeExt.amountStr}"></c:out>
                                    </c:otherwise>
                                </c:choose>
                            </p>
                        </td>
                    </tr>
                </c:if>
                <tr>
                    <td>
                        <p><em>Complex Special Licensable Services <c:if test="${complexSpecifiedFeeExt.svcNames.size()>1}">(${complexSpecifiedFeeExt.svcNames.size()})</c:if></em></p>
                        <c:forEach var="svcName" items="${complexSpecifiedFeeExt.svcNames}">
                            <p>- <c:out value="${svcName}"></c:out></p>
                        </c:forEach>

                    </td>
                    <td>
                        <p>&nbsp;</p>
                        <p>
                            New Licence
                        </p>
                    </td>
                    <td>
                        <p>&nbsp;</p>
                        <p>
                            <c:out value="${AppSubmissionDto.appGrpNo}"></c:out>
                        </p>
                    </td>
                    <td>
                        <p>&nbsp;</p>
                        <p>
                            <c:out value="${complexSpecifiedFeeExt.amountStr}"></c:out>
                        </p>
                    </td>
                </tr>
            </c:forEach>
        </c:when>
        <c:otherwise>
            <c:choose>
                <c:when test="${AppSubmissionDto.appGrpPremisesDtoList.size()>1}">
                    <c:set var="multiplePrem" value="true"/>
                </c:when>
                <c:otherwise>
                    <c:set var="multiplePrem" value="false"/>
                </c:otherwise>
            </c:choose>
            <c:forEach items="${AppSubmissionDto.appGrpPremisesDtoList}" var="premises" varStatus="stat">
                <c:forEach items="${AppSubmissionDto.feeInfoDtos}" var="feeInfoDto" varStatus="feeInfoStat">
                    <c:set var="baseSvcFeeExt" value="${feeInfoDto.baseSvcFeeExt}"/>
                    <c:set var="complexSpecifiedFeeExt" value="${feeInfoDto.complexSpecifiedFeeExt}"/>
                    <c:set var="simpleSpecifiedFeeExt" value="${feeInfoDto.simpleSpecifiedFeeExt}"/>
                    <!--todo:includedSvcFeeExtList -->
                    <!--base -->
                    <%--<c:if test="${!AppSubmissionDto.onlySpecifiedSvc}">--%>
                    <tr>
                        <td>
                            <c:forEach var="svcName" items="${baseSvcFeeExt.svcNames}">
                                <p>
                                    <c:out value="${svcName}"></c:out>
                                    <c:if test="${multiplePrem}">
                                        &nbsp;(Premises #${stat.index+1})
                                    </c:if>
                                </p>
                            </c:forEach>
                        </td>
                        <td>
                            <p>
                                New Licence
                            </p>
                        </td>
                        <td>
                            <p>
                                <c:out value="${AppSubmissionDto.appGrpNo}"></c:out>
                            </p>
                        </td>
                        <td>
                            <p>
                                <c:choose>
                                    <c:when test="${IsCharity && feeInfoStat.index>0}">
                                        <c:out value="$0"></c:out>
                                    </c:when>
                                    <c:when test="${'OFFSITE' == premises.premisesType && !onlyOffsite}">
                                        <c:out value="$0"></c:out>
                                    </c:when>
                                    <c:otherwise>
                                        <c:out value="${baseSvcFeeExt.amountStr}"></c:out>
                                    </c:otherwise>
                                </c:choose>
                            </p>
                        </td>
                    </tr>

                    <!--simpleSpecifiedFeeExt -->
                    <c:if test="${simpleSpecifiedFeeExt.svcNames.size()>0 }">
                        <tr>
                            <td>
                                <p>&nbsp;&nbsp;Simple Special Licensable Services</p>
                                <c:forEach var="svcName" items="${simpleSpecifiedFeeExt.svcNames}">
                                    <p>&nbsp;&nbsp;- <c:out value="${svcName}"></c:out></p>
                                </c:forEach>

                            </td>
                            <td>
                                <p></p>
                            </td>
                            <td>
                                <p> </p>
                            </td>
                            <td>
                                <p >
                                    <c:out value="${simpleSpecifiedFeeExt.amountStr}"></c:out>
                                </p>
                            </td>
                        </tr>
                    </c:if>

                    <!--complexSpecifiedFeeExt -->
                    <c:if test="${complexSpecifiedFeeExt.svcNames.size()>0 }">
                        <tr>
                            <td class="breakdown">
                                <p>&nbsp;&nbsp;<em>Complex Special Licensable Services <c:if test="${complexSpecifiedFeeExt.svcNames.size()>1}">(${complexSpecifiedFeeExt.svcNames.size()})</c:if></em></p>
                                <c:forEach var="svcName" items="${complexSpecifiedFeeExt.svcNames}">
                                    <p>&nbsp;&nbsp;- <c:out value="${svcName}"></c:out></p>
                                </c:forEach>
                            </td>
                            <td>
                                <p></p>
                            </td>
                            <td>
                                <p> </p>
                            </td>
                            <td>
                                <p >
                                    <c:out value="${complexSpecifiedFeeExt.amountStr}"></c:out>
                                </p>
                            </td>
                        </tr>
                    </c:if>

                </c:forEach>
            </c:forEach>
        </c:otherwise>
    </c:choose>
    <tr>
        <td></td>
        <td></td>
        <td><p>Total amount due:</p></td>
        <td><p><strong> <c:out value="${AppSubmissionDto.amountStr}"></c:out></strong></p></td>
    </tr>
    </tbody>
</table>