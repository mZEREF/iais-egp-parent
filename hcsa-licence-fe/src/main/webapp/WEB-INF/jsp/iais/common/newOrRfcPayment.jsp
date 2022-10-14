<%@ page import="com.ecquaria.cloud.moh.iais.common.utils.Formatter" %>
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
                            <c:forEach var="svcName" items="${baseSvcFeeExt.svcNames}">
                                <p>
                                    <c:out value="${svcName}"/>
                                </p>
                                <p>
                                    (${baseSvcFeeExt.address})
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
                                <c:out value="${baseSvcFeeExt.amountStr}"/>
                            </p>
                        </td>
                    </tr>
                </c:if>
                <!--included -->
                <c:if test="${not empty includedSvcFeeExtList }">
                    <c:forEach items="${includedSvcFeeExtList}" var="includedSvcFeeExt" >
                        <tr>
                            <td>
                                <p>Bundled Fees</p>
                                <c:forEach var="svcName" items="${includedSvcFeeExt.svcNames}">
                                    <p>
                                        &nbsp;&nbsp;<c:out value="${svcName}"/>
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
                                    <c:out value="${includedSvcFeeExt.amountStr}"/>
                                </p>
                                <c:forEach var="svcName" items="${includedSvcFeeExt.svcNames}">
                                    <p>Include</p>
                                </c:forEach>
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
                                <p>Include</p>
                            </c:forEach>
                        </td>
                    </tr>
                </c:if>
                <!--SpecifiedFeeExt -->
                <c:if test="${not empty simpleSpecifiedFeeExt or not empty complexSpecifiedFeeExt }">
                    <tr>
                        <td>
                            <p>&nbsp;&nbsp;With Specialised Service(s)</p>
                            <c:if test="${not empty simpleSpecifiedFeeExt }">
                                <c:forEach var="svcNameSs" items="${simpleSpecifiedFeeExt.svcNames}">
                                    <p>&nbsp;&nbsp;- <c:out value="${svcNameSs}"/></p>
                                </c:forEach>
                            </c:if>

                            <c:if test="${not empty complexSpecifiedFeeExt }">
                                <c:forEach var="svcNameCs" items="${complexSpecifiedFeeExt.svcNames}">
                                    <p>&nbsp;&nbsp;- <c:out value="${svcNameCs}"/></p>
                                </c:forEach>
                            </c:if>

                        </td>
                        <td>
                            <p></p>
                        </td>
                        <td>
                            <p> </p>
                        </td>
                        <td>
                            <p >
                                <c:choose>
                                    <c:when test="${empty simpleSpecifiedFeeExt}">
                                        <c:out value="${complexSpecifiedFeeExt.amountStr}"/>
                                    </c:when>
                                    <c:when test="${empty complexSpecifiedFeeExt}">
                                        <c:out value="${simpleSpecifiedFeeExt.amountStr}"/>
                                    </c:when>
                                    <c:otherwise>
                                        <c:out value="${Formatter.formatterMoney(simpleSpecifiedFeeExt.amount+complexSpecifiedFeeExt.amount)}"/>
                                    </c:otherwise>
                                </c:choose>
                            </p>
                            <c:if test="${not empty simpleSpecifiedFeeExt }">
                                <c:forEach var="svcName" items="${simpleSpecifiedFeeExt.svcNames}">
                                    <p>Include</p>
                                </c:forEach>
                            </c:if>

                            <c:if test="${not empty complexSpecifiedFeeExt }">
                                <c:forEach var="svcName" items="${complexSpecifiedFeeExt.svcNames}">
                                    <p>Include</p>
                                </c:forEach>
                            </c:if>
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
                                <p>Include</p>
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
        <td><p>Total${FeeDetail}</p></td>
        <td><p><strong> <c:out value="${AppSubmissionDto.amountStr}"/></strong></p></td>
    </tr>
    </tbody>
</table>