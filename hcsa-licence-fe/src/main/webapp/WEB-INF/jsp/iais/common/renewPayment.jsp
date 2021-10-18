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

        <c:if test="${!empty gradualFeeList}">
            <tr>
                <td style="border-top: none;" colspan="4">
                    <p><em>To facilitate the transition from PHMCA to HCSA, the following licence(s) in your application are eligible for fee reductionin this transition period:</em></p>
                </td>
                <td style="border-top: none;"></td>
                <td style="border-top: none;"></td>
                <td style="border-top: none;"></td>
            </tr>

            <c:forEach items="${gradualFeeList}" var="gradualFee">
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
                        <c:forEach var="serviceName" items="${gradualFee.svcNames}">
                            <p>${serviceName}</p>
                        </c:forEach>
                    </td>
                    <td style="border-top: none;">Renewal</td>
                    <td style="border-top: none;">${gradualFee.appGroupNo}</td>
                    <td style="border-top: none;"><p>${gradualFee.amountStr}</p></td>
                </tr>

                <tr>
                    <td>
                        <p><u>Previous Fees under PHMCA</u></p>
                    </td>
                    <td></td>
                    <td></td>
                    <td><p>${gradualFee.oldAmountStr}</p></td>
                </tr>

                <tr>
                    <td style="border-top: none;">
                        <p><u>Total Increase in Fees</u></p>
                    </td>
                    <td style="border-top: none;"></td>
                    <td style="border-top: none;"></td>
                    <td style="border-top: none;"><p>${gradualFee.increaseAmountStr}</p></td>
                </tr>

                <tr>
                    <td style="border-top: none;">
                        <p><u>${gradualFee.reduction}</u></p>
                    </td>
                    <td style="border-top: none;"></td>
                    <td style="border-top: none;"></td>
                    <td style="border-top: none;"><p>${gradualFee.gradualSpreadStr}</p></td>
                </tr>
            </c:forEach>
        </c:if>

        <c:if test="${'mix' != mix}">
            <c:forEach var="AppSubmissionDto" items="${renewDto.appSubmissionDtos}"  varStatus="status">
                <c:if test="${'gradualFee' != AppSubmissionDto.renewalFeeType}">
                    <c:set var="detailFeeDto" value="${AppSubmissionDto.detailFeeDto}"/>
                    <tr>
                        <td>
                            <c:if test="${AppSubmissionDto.isBundledFee==1}">
                                <p>&nbsp;&nbsp;Bundled Fees</p>
                            </c:if>
                            <p>
                                <c:if test="${AppSubmissionDto.isBundledFee==1}">&nbsp;&nbsp;-</c:if>
                                <c:out value="${AppSubmissionDto.serviceName}"/>
                            </p>
                        </td>
                        <td>
                            Renewal
                        </td>
                        <td>
                            <p>${AppSubmissionDto.appGrpNo}</p>
                        </td>
                        <td>
                            <p>${AppSubmissionDto.amountStr}</p>
                        </td>
                    </tr>
                </c:if>
            </c:forEach>
        </c:if>

        <c:if test="${'mix' == mix}">
            <tr>
                <td colspan="4">
                    <p><u>Fees for remaining HCSA licences</u></p>
                </td>
                <td></td>
                <td></td>
                <td></td>
            </tr>
            <c:forEach items="${normalFeeList}" var="normalFee">
                <c:if test="${'gradualFee' != normalFee.lateFeeType}">
                    <tr>
                        <td style="border-top: none;">
                            <c:forEach var="serviceName" items="${normalFee.svcNames}">
                                <p>${serviceName}</p>
                            </c:forEach>
                        </td>
                        <td style="border-top: none;">Renewal</td>
                        <td style="border-top: none;">${normalFee.appGroupNo}</td>
                        <td style="border-top: none;"><p>${normalFee.amountStr}</p></td>
                    </tr>
                </c:if>
            </c:forEach>
        </c:if>

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
