<div class="col-xs-12">
    <p class="ack-font-20"><strong>Submission Successful</strong></p>
</div>

<c:forEach items="${serviceNamesAck}" var="serviceName">
    <div class="col-xs-12">
        <p class="ack-font-20">- <strong><c:out value="${serviceName}"/> </strong></p>
    </div>
</c:forEach>
<br/>
<div class="ack-font-16">
    <div class="col-xs-12">
        A confirmation email will be sent to ${emailAddress}.
        <br/>
        <br/>
    </div>
    <div class="col-xs-12">
        <iais:message key="NEW_ACK005" escape="false"></iais:message>
        <br/>
        <br/>
    </div>
    <div class="col-xs-12">
        Transactional details:
    </div>
    <div class="col-xs-12">
        <div class="table-responsive">
            <table aria-describedby="" class="table">
                <thead>
                <tr>
                    <%--<th scope="col" >Application No.</th>--%>
                    <%--<c:if test="${'Credit' == renewDto.appSubmissionDtos.get(0).paymentMethod or 'NETS'==renewDto.appSubmissionDtos.get(0).paymentMethod}">
                        <th scope="col" >Transactional No.</th>
                    </c:if>--%>
                    <th scope="col" >Transactional No.</th>
                    <th scope="col" >Date & Time</th>
                    <th scope="col" >Amount Deducted</th>
                    <th scope="col" >Payment Method</th>
                </tr>
                </thead>
                <tbody>
                <%--                                        <c:forEach var="AppSubmissionDto" items="${renewDto.appSubmissionDtos}">--%>
                <c:set var="AppSubmissionDto" value="${renewDto.appSubmissionDtos.get(0)}" scope="request"/>
                <tr>
                    <%--<td><c:out value="${AppSubmissionDto.appGrpNo}"/></td>--%>
                    <%--<c:if test="${'Credit'== renewDto.appSubmissionDtos.get(0).paymentMethod or 'NETS'== renewDto.appSubmissionDtos.get(0).paymentMethod}">
                        <td><c:out value="${txnRefNo}"/></td>
                    </c:if>--%>
                    <td>
                        <c:choose>
                            <c:when test="${empty txnRefNo || empty AppSubmissionDto.paymentMethod}">
                                N/A
                            </c:when>
                            <c:otherwise>
                                <c:out value="${txnRefNo}"/>
                            </c:otherwise>
                        </c:choose>
                    </td>
                    <td>
                        <c:choose>
                            <c:when test="${empty txnDt}">
                                N/A
                            </c:when>
                            <c:otherwise>
                                <c:out value="${txnDt}"/>
                            </c:otherwise>
                        </c:choose>
                    </td>
                    <td><c:out value="${totalStr}"/></td>
                    <td>
                        <c:choose>
                            <c:when test="${empty AppSubmissionDto.paymentMethod}">
                                N/A
                            </c:when>
                            <c:otherwise>
                                <iais:code code="${AppSubmissionDto.paymentMethod}"/>
                            </c:otherwise>
                        </c:choose>
                    </td>
                </tr>
                <%--                                        </c:forEach>--%>
                </tbody>
            </table>
        </div>
    </div>
</div>