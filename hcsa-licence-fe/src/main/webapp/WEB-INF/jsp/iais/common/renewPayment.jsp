<%@ page import="com.ecquaria.cloud.moh.iais.common.utils.Formatter" %>
<div class="table-responsive">
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


        <c:forEach items="${renewAppSubmissionDtos}" var="svc" varStatus="index">
            <c:forEach items="${svc.feeInfoDtos}" var="feeInfoDto" varStatus="feeInfoStat">
                <c:set var="baseSvcFeeExt" value="${feeInfoDto.baseSvcFeeExt}"/>
                <c:set var="includedSvcFeeExtList" value="${feeInfoDto.includedSvcFeeExtList}"/>
                <c:set var="complexSpecifiedFeeExt" value="${feeInfoDto.complexSpecifiedFeeExt}"/>
                <c:set var="simpleSpecifiedFeeExt" value="${feeInfoDto.simpleSpecifiedFeeExt}"/>
                <c:set var="thbSpecifiedFeeExt" value="${feeInfoDto.thbSpecifiedFeeExt}"/>
                <!--base -->
                <c:if test="${not empty baseSvcFeeExt and empty includedSvcFeeExtList}">
                    <tr>
                        <td>
                            <c:forEach var="svcName" items="${baseSvcFeeExt.svcNames}">
                                <p>
                                    <strong><c:out value="${svcName}"/></strong>
                                </p>
                                <p>
                                    (${baseSvcFeeExt.address})
                                </p>
                            </c:forEach>
                        </td>
                        <td>
                            <c:forEach var="svcIndex" items="${baseSvcFeeExt.svcIndexList}">
                                <c:choose>
                                    <c:when test="${'APTY005' ==svc.appType}">
                                        <p>Amendment</p>
                                    </c:when>
                                    <c:otherwise>
                                        <p>Renewal</p>
                                    </c:otherwise>
                                </c:choose>
                                <p>&nbsp;&nbsp;</p>
                            </c:forEach>
                        </td>
                        <td>
                            <c:forEach var="svcIndex" items="${baseSvcFeeExt.svcIndexList}">
                                <p><c:out value="${baseSvcFeeExt.appGroupNo}-0${svcIndex}"/></p>
                                <p>&nbsp;&nbsp;</p>
                            </c:forEach>
                        </td>
                        <td>
                            <p>
                                <c:out value="${baseSvcFeeExt.amountStr}"/>
                            </p>
                        </td>
                    </tr>
                </c:if>
                <!--included -->
                <c:if test="${not empty baseSvcFeeExt and not empty includedSvcFeeExtList }">
                    <c:set var="includedSvcFeeExtRoot" value="${includedSvcFeeExtList[0]}"/>
                        <tr>
                            <td>
                                <p>Bundled Fees</p>
                                <c:forEach var="svcName" items="${baseSvcFeeExt.svcNames}">
                                    <p>
                                        &nbsp;&nbsp;<strong><c:out value="${svcName}"/></strong>
                                    </p>
                                    <p>
                                        (${baseSvcFeeExt.address})
                                    </p>
                                </c:forEach>
                                <c:forEach var="svcName" items="${includedSvcFeeExtRoot.svcNames}">
                                    <p>
                                        &nbsp;&nbsp;<strong><c:out value="${svcName}"/></strong>
                                    </p>
                                    <p>
                                        (${includedSvcFeeExtRoot.address})
                                    </p>
                                </c:forEach>
                            </td>
                            <td>
                                <c:forEach var="svcIndex" items="${baseSvcFeeExt.svcIndexList}">
                                    <c:choose>
                                        <c:when test="${'APTY005' ==svc.appType}">
                                            <p>Amendment</p>
                                        </c:when>
                                        <c:otherwise>
                                            <p>Renewal</p>
                                        </c:otherwise>
                                    </c:choose>
                                    <p>&nbsp;&nbsp;</p>
                                </c:forEach>
                                <c:forEach var="svcIndex" items="${includedSvcFeeExtRoot.svcIndexList}">
                                    <c:choose>
                                        <c:when test="${'APTY005' ==svc.appType}">
                                            <p>Amendment</p>
                                        </c:when>
                                        <c:otherwise>
                                            <p>Renewal</p>
                                        </c:otherwise>
                                    </c:choose>
                                    <p>&nbsp;&nbsp;</p>
                                </c:forEach>
                            </td>
                            <td>
                                <c:forEach var="svcIndex" items="${baseSvcFeeExt.svcIndexList}">
                                    <p><c:out value="${baseSvcFeeExt.appGroupNo}-0${svcIndex}"/></p>
                                    <p>&nbsp;&nbsp;</p>
                                </c:forEach>
                                <c:forEach var="svcIndex" items="${includedSvcFeeExtRoot.svcIndexList}">
                                    <p><c:out value="${includedSvcFeeExtRoot.appGroupNo}-0${svcIndex}"/></p>
                                    <p>&nbsp;&nbsp;</p>
                                </c:forEach>
                            </td>
                            <td>
                                <p>
                                    <c:out value="${Formatter.formatterMoney(baseSvcFeeExt.amount+includedSvcFeeExtRoot.amount)}"/>
                                </p>
                                <p>Include</p>
                                <c:forEach var="svcName" items="${includedSvcFeeExtRoot.svcNames}">
                                    <p>&nbsp;&nbsp;</p>
                                    <p>Include</p>
                                </c:forEach>
                            </td>
                        </tr>

                    <c:forEach items="${includedSvcFeeExtList}" var="includedSvcFeeExt" >
                        <c:if test="${not empty includedSvcFeeExt.includeSsFeeExtDto or not empty includedSvcFeeExt.includeCsFeeExtDto }">
                            <tr>
                                <td>
                                    <p>&nbsp;&nbsp;With Specified Service(s) / Discipline(s)</p>
                                    <c:if test="${not empty includedSvcFeeExt.includeSsFeeExtDto }">
                                        <c:forEach var="svcNameSs" items="${includedSvcFeeExt.includeSsFeeExtDto.svcNames}">
                                            <p>&nbsp;&nbsp;<strong><c:out value="${svcNameSs}"/></strong></p>
                                        </c:forEach>
                                    </c:if>

                                    <c:if test="${not empty includedSvcFeeExt.includeCsFeeExtDto }">
                                        <c:forEach var="svcNameCs" items="${includedSvcFeeExt.includeCsFeeExtDto.svcNames}">
                                            <p>&nbsp;&nbsp;<strong><c:out value="${svcNameCs}"/></strong></p>
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
                                            <c:when test="${empty includedSvcFeeExt.includeSsFeeExtDto}">
                                                <c:out value="${includedSvcFeeExt.includeCsFeeExtDto.amountStr}"/>
                                            </c:when>
                                            <c:when test="${empty includedSvcFeeExt.includeCsFeeExtDto}">
                                                <c:out value="${includedSvcFeeExt.includeSsFeeExtDto.amountStr}"/>
                                            </c:when>
                                            <c:otherwise>
                                                <c:out value="${Formatter.formatterMoney(includedSvcFeeExt.includeSsFeeExtDto.amount+includedSvcFeeExt.includeCsFeeExtDto.amount)}"/>
                                            </c:otherwise>
                                        </c:choose>
                                    </p>
                                    <c:if test="${not empty includedSvcFeeExt.includeSsFeeExtDto }">
                                        <c:forEach var="svcName" items="${includedSvcFeeExt.includeSsFeeExtDto.svcNames}">
                                            <p>Include</p>
                                        </c:forEach>
                                    </c:if>

                                    <c:if test="${not empty includedSvcFeeExt.includeCsFeeExtDto }">
                                        <c:forEach var="svcName" items="${includedSvcFeeExt.includeCsFeeExtDto.svcNames}">
                                            <p>Include</p>
                                        </c:forEach>
                                    </c:if>
                                </td>
                            </tr>
                        </c:if>
                    </c:forEach>

                </c:if>
                <!--SpecifiedFeeExt -->
                <c:if test="${not empty simpleSpecifiedFeeExt or not empty complexSpecifiedFeeExt }">
                    <tr>
                        <td>
                            <p>&nbsp;&nbsp;With Specified Service(s) / Discipline(s)</p>
                            <c:if test="${not empty simpleSpecifiedFeeExt }">
                                <c:forEach var="svcNameSs" items="${simpleSpecifiedFeeExt.svcNames}">
                                    <p>&nbsp;&nbsp;<strong><c:out value="${svcNameSs}"/></strong></p>
                                </c:forEach>
                            </c:if>

                            <c:if test="${not empty complexSpecifiedFeeExt }">
                                <c:forEach var="svcNameCs" items="${complexSpecifiedFeeExt.svcNames}">
                                    <p>&nbsp;&nbsp;<strong><c:out value="${svcNameCs}"/></strong></p>
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
                            <p>&nbsp;&nbsp;With Specified Service(s) / Discipline(s)</p>
                            <c:forEach var="svcName" items="${thbSpecifiedFeeExt.svcNames}">
                                <p>&nbsp;&nbsp;<strong><c:out value="${svcName}"/></strong></p>
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

        </c:forEach>
        <c:forEach items="${laterFeeDetailsMap}" var="laterFeeDetailMap">
            <c:set var="laterFeeType" value='${laterFeeDetailMap.key}' />
            <tr>
                <td colspan="4">
                    <p>${laterFeeType}</p>
                    <p><em>Applicable to the following licence(s):</em></p>
                </td>
            </tr>
            <c:forEach items="${laterFeeDetailMap.value}" var="laterFeeDetail">
                <tr>
                    <td style="border-top: none;">
                        <p><STRONG>${laterFeeDetail.svcNames.get(0)}</STRONG></p>
                    </td>
                    <td style="border-top: none;">N/A</td>
                    <td style="border-top: none;">N/A</td>
                    <td style="border-top: none;"><p>${laterFeeDetail.lateFeeAmoumtStr}</p></td>
                </tr>
            </c:forEach>
        </c:forEach>

        <tr>
            <td></td>
            <td></td>
            <td><p>Total&nbsp;</p></td>
            <td><p><strong><c:out value="${totalStr}"></c:out></strong></p></td>
        </tr>
        </tbody>
    </table>
</div>
