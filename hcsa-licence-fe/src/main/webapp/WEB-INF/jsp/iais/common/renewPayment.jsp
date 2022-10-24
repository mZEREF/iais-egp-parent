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

        <c:forEach items="${feeInfoDtoList}" var="feeInfoDto" varStatus="feeInfoStat">
            <c:set var="baseSvcFeeExt" value="${feeInfoDto.baseSvcFeeExt}"/>
            <c:set var="includedSvcFeeExtList" value="${feeInfoDto.includedSvcFeeExtList}"/>
            <c:set var="bundleSvcFeeExt" value="${feeInfoDto.bundleSvcFeeExt}"/>
            <c:set var="complexSpecifiedFeeExt" value="${feeInfoDto.complexSpecifiedFeeExt}"/>
            <c:set var="simpleSpecifiedFeeExt" value="${feeInfoDto.simpleSpecifiedFeeExt}"/>
            <c:set var="thbSpecifiedFeeExt" value="${feeInfoDto.thbSpecifiedFeeExt}"/>
            <c:set var="gradualFeeExt" value="${feeInfoDto.gradualFeeExt}"/>
            <!--gradualFee -->
            <c:if test="${not empty gradualFeeExt }">
                <tr>
                    <td style="border-top: none;" colspan="4">
                        <p><em>To facilitate the transition from PHMCA to HCSA, the following licence(s) in your application are eligible for fee reductionin this transition period:</em></p>
                    </td>
                    <td style="border-top: none;"></td>
                    <td style="border-top: none;"></td>
                    <td style="border-top: none;"></td>
                </tr>
                <tr>
                    <td style="border-top: none;">
                        <p><u>Fees for eliqible HCSA licences</u></p>
                    </td>
                    <td style="border-top: none;"></td>
                    <td style="border-top: none;"></td>
                    <td style="border-top: none;"></td>
                </tr>
                <tr>
                    <td style="border-top: none;">
                        <c:forEach var="serviceName" items="${gradualFeeExt.svcNames}">
                            <p><strong>${serviceName}</strong></p>
                        </c:forEach>
                    </td>
                    <td style="border-top: none;">Renewal</td>
                    <td style="border-top: none;">${gradualFeeExt.appGroupNo}-0${feeInfoStat.index+1}</td>
                    <td style="border-top: none;"><p>${gradualFeeExt.amountStr}</p></td>
                </tr>

                <tr>
                    <td>
                        <p><u>Previous Fees under PHMCA</u></p>
                    </td>
                    <td></td>
                    <td></td>
                    <td><p>${gradualFeeExt.oldAmountStr}</p></td>
                </tr>

                <tr>
                    <td style="border-top: none;">
                        <p><u>Total Increase in Fees</u></p>
                    </td>
                    <td style="border-top: none;"></td>
                    <td style="border-top: none;"></td>
                    <td style="border-top: none;"><p>${gradualFeeExt.increaseAmountStr}</p></td>
                </tr>

                <tr>
                    <td style="border-top: none;">
                        <p><u>${gradualFeeExt.reduction}</u></p>
                    </td>
                    <td style="border-top: none;"></td>
                    <td style="border-top: none;"></td>
                    <td style="border-top: none;"><p>${gradualFeeExt.gradualSpreadStr}</p></td>
                </tr>

            </c:if>
            <!--base -->
            <c:if test="${not empty baseSvcFeeExt }">
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
                        <p>
                            Renewal
                        </p>
                    </td>
                    <td>
                        <p>
                            <c:out value="${baseSvcFeeExt.appGroupNo}-0${feeInfoStat.index+1}"/>
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
                                    &nbsp;&nbsp;<strong><c:out value="${svcName}"/></strong>
                                </p>
                            </c:forEach>
                        </td>
                        <td>
                            <p>
                                Renewal
                            </p>
                        </td>
                        <td>
                            <p>
                                <c:out value="${includedSvcFeeExt.appGroupNo}-0${feeInfoStat.index+1}"/>
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
                            <p>&nbsp;&nbsp;<strong><c:out value="${svcName}"/></strong></p>
                        </c:forEach>

                    </td>
                    <td>
                        <p>Renewal</p>
                    </td>
                    <td>
                        <p>
                            <c:out value="${bundleSvcFeeExt.appGroupNo}-0${feeInfoStat.index+1}"/>
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
            <!--simpleSpecifiedFeeExt -->
            <c:if test="${not empty simpleSpecifiedFeeExt or not empty complexSpecifiedFeeExt }">
                <tr>
                    <td>
                        <p>&nbsp;&nbsp;With Specialised Service(s)</p>
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
                        <p>&nbsp;&nbsp;With Specialised Service(s)</p>
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

        <c:forEach items="${rfcAppSubmissionDtos}" var="svc" varStatus="index">
            <c:forEach items="${svc.feeInfoDtos}" var="feeInfoDto" varStatus="feeInfoStat">
                <c:set var="baseSvcFeeExt" value="${feeInfoDto.baseSvcFeeExt}"/>
                <c:set var="complexSpecifiedFeeExt" value="${feeInfoDto.complexSpecifiedFeeExt}"/>
                <c:set var="simpleSpecifiedFeeExt" value="${feeInfoDto.simpleSpecifiedFeeExt}"/>
                <c:set var="thbSpecifiedFeeExt" value="${feeInfoDto.thbSpecifiedFeeExt}"/>
                <c:if test="${not empty baseSvcFeeExt }">
                    <tr>
                        <td>
                            <p><strong><c:out value="${svc.serviceName}"/></strong></p>
                            <p>
                                (${baseSvcFeeExt.address})
                            </p>
                        </td>
                        <td>
                            <p>Amendment</p>
                        </td>
                        <td>
                            <p><c:out value="${svc.appGrpNo}-0${index.index+1}"/></p>
                        </td>
                        <td>
                            <p>
                                <c:out value="${baseSvcFeeExt.amountStr}"/>
                            </p>
                        </td>
                    </tr>
                </c:if>
                <c:if test="${empty baseSvcFeeExt }">
                    <tr>
                        <td>
                            <p><strong><c:out value="${svc.serviceName}"/></strong></p>
                        </td>
                        <td>
                            <p>Amendment</p>
                        </td>
                        <td>
                            <p><c:out value="${svc.appGrpNo}-0${index.index+1}"/></p>
                        </td>
                        <td>
                            <p>
                                <c:out value="${svc.amountStr}"/>
                            </p>
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
                            <p>&nbsp;&nbsp;With Specialised Service(s)</p>
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
