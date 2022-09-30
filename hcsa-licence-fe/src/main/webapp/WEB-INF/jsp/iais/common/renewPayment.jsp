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
                            <p>${serviceName}</p>
                        </c:forEach>
                    </td>
                    <td style="border-top: none;">Renewal</td>
                    <td style="border-top: none;">${gradualFeeExt.appGroupNo}</td>
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
                                <c:out value="${svcName}"/>
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
                            <c:out value="${baseSvcFeeExt.appGrpNo}"/>
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
                                Renewal
                            </p>
                        </td>
                        <td>
                            <p>
                                <c:out value="${includedSvcFeeExt.appGrpNo}"/>
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
                        <p>Renewal</p>
                    </td>
                    <td>
                        <p>
                            <c:out value="${bundleSvcFeeExt.appGrpNo}"/>
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
                            <p>Include</p>
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
                            <p>Include</p>
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
                            <p>Include</p>
                        </c:forEach>
                    </td>
                </tr>
            </c:if>
        </c:forEach>

        <c:forEach items="${rfcAppSubmissionDtos}" var="rfcAppSubmissionDto">
            <tr>
                <td>
                    <p><c:out value="${rfcAppSubmissionDto.serviceName}"></c:out></p>
                </td>
                <td>
                    Amendment
                </td>
                <td>
                    <p>${rfcAppSubmissionDto.appGrpNo}</p>
                </td>
                <td>
                    <p>${rfcAppSubmissionDto.amountStr}</p>
                </td>
            </tr>
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
                        <p>-&nbsp;${laterFeeDetail.svcNames.get(0)}</p>
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
            <td><p>Total amount due:</p></td>
            <td><p><strong><c:out value="${totalStr}"></c:out></strong></p></td>
        </tr>
        </tbody>
    </table>
</div>
