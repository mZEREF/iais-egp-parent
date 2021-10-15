<table aria-describedby="" class="table">
    <thead>
    <tr>
        <th scope="col" >Service</th>
        <th scope="col" >Application Type</th>
        <th scope="col" >Application No.</th>
        <th scope="col" >Amount</th>
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
                <c:set var="bundleSvcFeeExt" value="${feeInfoDto.bundleSvcFeeExt}"/>
                <c:set var="complexSpecifiedFeeExt" value="${feeInfoDto.complexSpecifiedFeeExt}"/>
                <c:set var="simpleSpecifiedFeeExt" value="${feeInfoDto.simpleSpecifiedFeeExt}"/>
                <c:if test="${not empty baseSvcFeeExt }">
                    <tr>
                        <td>
                            <c:forEach var="svcName" items="${baseSvcFeeExt.svcNames}">
                                <p>
                                    <c:out value="${svcName}"></c:out>
                                    &nbsp;(Group - ${AppSubmissionDto.appGrpPremisesDtoList.size()} Mode of Service Delivery)
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
                </c:if>
                <!--bundleSvcFeeExt -->
                <c:if test="${not empty bundleSvcFeeExt }">
                    <tr>
                        <td>
                            <p>&nbsp;&nbsp;Bundled Fees</p>
                            <c:forEach var="svcName" items="${bundleSvcFeeExt.svcNames}">
                                <p>&nbsp;&nbsp;- <c:out value="${svcName}"></c:out></p>
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
                            <p >
                                <c:out value="${bundleSvcFeeExt.amountStr}"></c:out>
                            </p>
                        </td>
                    </tr>
                </c:if>
                <!--complexSpecifiedFeeExt -->
                <c:if test="${complexSpecifiedFeeExt.svcNames.size()>0 }">
                    <tr>
                        <td class="breakdown">
                            <p>&nbsp;&nbsp;
                                <em>Complex <iais:code code="CDN004"/>
                                    <c:if test="${complexSpecifiedFeeExt.svcNames.size()>1}">(${complexSpecifiedFeeExt.svcNames.size()}) </c:if>
                                    (1 x ${AppSubmissionDto.appGrpPremisesDtoList.size()} Mode of Service Delivery)
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
                <c:set var="bundleSvcFeeExt" value="${feeInfoDto.bundleSvcFeeExt}"/>
                <c:set var="complexSpecifiedFeeExt" value="${feeInfoDto.complexSpecifiedFeeExt}"/>
                <c:set var="simpleSpecifiedFeeExt" value="${feeInfoDto.simpleSpecifiedFeeExt}"/>
                <c:if test="${!empty baseSvcFeeExt}">
                    <tr>
                        <td>
                            <c:forEach var="svcName" items="${baseSvcFeeExt.svcNames}">
                                <p>
                                    <c:out value="${svcName}"></c:out>
                                    <c:if test="${multiplePrem}">
                                        &nbsp;(Mode of Service Delivery #${stat.index+1})
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
                <!--bundleSvcFeeExt -->
                <c:if test="${not empty bundleSvcFeeExt }">
                    <tr>
                        <td>
                            <p>&nbsp;&nbsp;Bundled Fees</p>
                            <c:forEach var="svcName" items="${bundleSvcFeeExt.svcNames}">
                                <p>&nbsp;&nbsp;- <c:out value="${svcName}"></c:out></p>
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
                            <p >
                                <c:out value="${bundleSvcFeeExt.amountStr}"></c:out>
                            </p>
                        </td>
                    </tr>
                </c:if>
                <c:if test="${not empty complexSpecifiedFeeExt }">
                    <tr>
                        <td>
                            <p><em>Complex <iais:code code="CDN004"/> <c:if test="${complexSpecifiedFeeExt.svcNames.size()>1}">(${complexSpecifiedFeeExt.svcNames.size()})</c:if></em></p>
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
                </c:if>
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
                    <c:set var="bundleSvcFeeExt" value="${feeInfoDto.bundleSvcFeeExt}"/>
                    <c:set var="complexSpecifiedFeeExt" value="${feeInfoDto.complexSpecifiedFeeExt}"/>
                    <c:set var="simpleSpecifiedFeeExt" value="${feeInfoDto.simpleSpecifiedFeeExt}"/>
                    <!--todo:includedSvcFeeExtList -->
                    <!--base -->
                    <%--<c:if test="${!AppSubmissionDto.onlySpecifiedSvc}">--%>
                    <c:if test="${not empty baseSvcFeeExt }">
                        <tr>
                            <td>
                                <c:forEach var="svcName" items="${baseSvcFeeExt.svcNames}">
                                    <p>
                                        <c:out value="${svcName}"></c:out>
                                        <c:if test="${multiplePrem}">
                                            &nbsp;(Mode of Service Delivery #${stat.index+1})
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
                    </c:if>
                    <!--bundleSvcFeeExt -->
                    <c:if test="${not empty bundleSvcFeeExt }">
                        <tr>
                            <td>
                                <p>&nbsp;&nbsp;Bundled Fees</p>
                                <c:forEach var="svcName" items="${bundleSvcFeeExt.svcNames}">
                                    <p>&nbsp;&nbsp;- <c:out value="${svcName}"></c:out></p>
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
                                <p >
                                    <c:out value="${bundleSvcFeeExt.amountStr}"></c:out>
                                </p>
                            </td>
                        </tr>
                    </c:if>
                    <!--simpleSpecifiedFeeExt -->
                    <c:if test="${simpleSpecifiedFeeExt.svcNames.size()>0 }">
                        <tr>
                            <td>
                                <p>&nbsp;&nbsp;Simple <iais:code code="CDN004"/></p>
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
                                <p>&nbsp;&nbsp;<em>Complex <iais:code code="CDN004"/> <c:if test="${complexSpecifiedFeeExt.svcNames.size()>1}">(${complexSpecifiedFeeExt.svcNames.size()})</c:if></em></p>
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