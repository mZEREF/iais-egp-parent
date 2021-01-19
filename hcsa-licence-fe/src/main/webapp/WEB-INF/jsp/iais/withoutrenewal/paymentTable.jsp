<table class="table">
    <thead>
    <tr>
        <th>Service</th>
        <th>Application Type</th>
        <th>Application No.</th>
        <th>Amount</th>
    </tr>
    </thead>
    <p><em>To facilitate the transition frorm PHMCA to HCSA, the following licence(s) in your application are eligible for fee reductionin this transition period:</em></p>
    <p><u>Fees for eliqible HCSA licences</u></p>
    <tbody>
    <c:forEach var="AppSubmissionDto" items="${renewDto.appSubmissionDtos}"  varStatus="status">
        <c:set var="detailFeeDto" value="${AppSubmissionDto.detailFeeDto}"/>
        <tr>
            <td>
                <p><c:out value="${AppSubmissionDto.serviceName}"></c:out></p>
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
                <p><u>Fees for remaining HCSA licences</u></p>
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