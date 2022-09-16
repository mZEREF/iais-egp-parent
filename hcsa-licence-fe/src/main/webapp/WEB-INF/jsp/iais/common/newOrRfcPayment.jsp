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

    <c:choose>
        <c:when test="${'APTY005' ==AppSubmissionDto.appType}">
            <c:forEach var="svc" items="${appSubmissionDtos}">
                <tr>
                    <td>
                        <p><c:out value="${svc.serviceName}"/></p>
                    </td>
                    <td>
                        <p>Amendment</p>
                    </td>
                    <td>
                        <p><c:out value="${svc.appGrpNo}"/></p>
                    </td>
                    <td>
                        <p><c:out value="${svc.amountStr}"/></p>
                    </td>
                </tr>
            </c:forEach>
        </c:when>

        <c:otherwise>
            <c:forEach items="${AppSubmissionDto.feeInfoDtos}" var="feeInfoDto" varStatus="feeInfoStat">
                <c:set var="baseSvcFeeExt" value="${feeInfoDto.baseSvcFeeExt}"/>
                <c:set var="includedSvcFeeExtList" value="${feeInfoDto.includedSvcFeeExtList}"/>
                <c:set var="bundleSvcFeeExt" value="${feeInfoDto.bundleSvcFeeExt}"/>
                <c:set var="complexSpecifiedFeeExt" value="${feeInfoDto.complexSpecifiedFeeExt}"/>
                <c:set var="simpleSpecifiedFeeExt" value="${feeInfoDto.simpleSpecifiedFeeExt}"/>
                <c:set var="thbSpecifiedFeeExt" value="${feeInfoDto.thbSpecifiedFeeExt}"/>
                <!--base -->
                <c:if test="${not empty baseSvcFeeExt }">
                    <tr>
                        <td>
                            <c:choose>
                                <c:when test="${baseSvcFeeExt.svcNames.size()>1}">
                                    <c:set var="multiplePrem" value="true"/>
                                </c:when>
                                <c:otherwise>
                                    <c:set var="multiplePrem" value="false"/>
                                </c:otherwise>
                            </c:choose>
                            <c:forEach var="svcName" items="${baseSvcFeeExt.svcNames}">
                                <p>
                                    <c:out value="${svcName}"/>
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
                                <c:out value="${AppSubmissionDto.appGrpNo}"/>
                            </p>
                        </td>
                        <td>
                            <p>
                                <c:choose>
                                    <c:when test="${IsCharity && feeInfoStat.index>0}">
                                        <c:out value="$0"/>
                                    </c:when>
                                    <c:otherwise>
                                        <c:out value="${baseSvcFeeExt.amountStr}"/>
                                    </c:otherwise>
                                </c:choose>
                            </p>
                        </td>
                    </tr>
                </c:if>
                <!--included -->
                <c:if test="${not empty includedSvcFeeExtList }">
                    <c:forEach items="${includedSvcFeeExtList}" var="includedSvcFeeExt" >
                        <tr>
                            <td>
                                <c:choose>
                                    <c:when test="${includedSvcFeeExt.svcNames.size()>1}">
                                        <c:set var="multiplePrem" value="true"/>
                                    </c:when>
                                    <c:otherwise>
                                        <c:set var="multiplePrem" value="false"/>
                                    </c:otherwise>
                                </c:choose>
                                <c:forEach var="svcName" items="${includedSvcFeeExt.svcNames}">
                                    <p>
                                        <c:out value="${svcName}"/>
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
                                    <c:out value="${AppSubmissionDto.appGrpNo}"/>
                                </p>
                            </td>
                            <td>
                                <p>
                                    <c:choose>
                                        <c:when test="${IsCharity && feeInfoStat.index>0}">
                                            <c:out value="$0"/>
                                        </c:when>
                                        <c:otherwise>
                                            <c:out value="${includedSvcFeeExt.amountStr}"/>
                                        </c:otherwise>
                                    </c:choose>
                                </p>
                            </td>
                        </tr>
                    </c:forEach>

                </c:if>
                <!--bundleSvcFeeExt -->
                <c:if test="${not empty bundleSvcFeeExt }">
                    <tr>
                        <td>
                            <p>&nbsp;&nbsp;Bundled Fees</p>
                            <c:forEach var="svcName" items="${bundleSvcFeeExt.svcNames}">
                                <p>&nbsp;&nbsp;- <c:out value="${svcName}"/></p>
                            </c:forEach>

                        </td>
                        <td>
                            <p>New Licence</p>
                        </td>
                        <td>
                            <p>
                                <c:out value="${AppSubmissionDto.appGrpNo}"/>
                            </p>
                        </td>
                        <td>
                            <p >
                                <c:out value="${bundleSvcFeeExt.amountStr}"/>
                            </p>
                            <c:forEach var="svcName" items="${bundleSvcFeeExt.svcNames}">
                                <p>&nbsp;&nbsp;Include</p>
                            </c:forEach>
                        </td>
                    </tr>
                </c:if>
                <!--simpleSpecifiedFeeExt -->
                <c:if test="${not empty simpleSpecifiedFeeExt }">
                    <tr>
                        <td>
                            <p>&nbsp;&nbsp;With Specialised Service(s)</p>
                            <c:forEach var="svcName" items="${simpleSpecifiedFeeExt.svcNames}">
                                <p>&nbsp;&nbsp;- <c:out value="${svcName}"/></p>
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
                                <c:out value="${simpleSpecifiedFeeExt.amountStr}"/>
                            </p>
                            <c:forEach var="svcName" items="${simpleSpecifiedFeeExt.svcNames}">
                                <p>&nbsp;&nbsp;Include</p>
                            </c:forEach>
                        </td>
                    </tr>
                </c:if>

                <!--complexSpecifiedFeeExt -->
                <c:if test="${not empty complexSpecifiedFeeExt }">
                    <tr>
                        <td class="breakdown">
                            <p>&nbsp;&nbsp;With Specialised Service(s)</p>
                            <c:forEach var="svcName" items="${complexSpecifiedFeeExt.svcNames}">
                                <p>&nbsp;&nbsp;- <c:out value="${svcName}"/></p>
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
                                <c:out value="${complexSpecifiedFeeExt.amountStr}"/>
                            </p>
                            <c:forEach var="svcName" items="${complexSpecifiedFeeExt.svcNames}">
                                <p>&nbsp;&nbsp;Include</p>
                            </c:forEach>
                        </td>
                    </tr>
                </c:if>

                <!--thbSpecifiedFeeExt -->
                <c:if test="${not empty thbSpecifiedFeeExt }">
                    <tr>
                        <td>
                            <p>&nbsp;&nbsp;With Specialised Service(s)</p>
                            <c:forEach var="svcName" items="${thbSpecifiedFeeExt.svcNames}">
                                <p>&nbsp;&nbsp;- <c:out value="${svcName}"/></p>
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
                                <c:out value="${thbSpecifiedFeeExt.amountStr}"/>
                            </p>
                            <c:forEach var="svcName" items="${thbSpecifiedFeeExt.svcNames}">
                                <p>&nbsp;&nbsp;Include</p>
                            </c:forEach>
                        </td>
                    </tr>
                </c:if>
            </c:forEach>

        </c:otherwise>
    </c:choose>
    <tr>
        <td></td>
        <td></td>
        <td><p>Total amount due:</p></td>
        <td><p><strong> <c:out value="${AppSubmissionDto.amountStr}"/></strong></p></td>
    </tr>
    </tbody>
</table>