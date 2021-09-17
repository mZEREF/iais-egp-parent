<div class="col-xs-12">
    <br/>
    <p class="ack-font-20"><strong>Submission successful</strong></p>
</div>

<div class="col-xs-12">
    <c:choose>
        <c:when test="${not empty allSvcNames}"><%-- RFC --%>
            <c:forEach items="${allSvcNames}" var="name">
                <p class="ack-font-20">- <strong><c:out value="${name}"/> </strong></p>
            </c:forEach>
        </c:when>
        <c:otherwise>
            <c:forEach items="${hcsaServiceDtoList}" var="list">
                <p class="ack-font-20">- <strong><c:out value="${list.svcName}"/> </strong></p>
            </c:forEach>
        </c:otherwise>
    </c:choose>
</div>

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
                    <th scope="col" >Application No.</th>
                    <%--<c:if test="${'Credit'==AppSubmissionDto.paymentMethod or 'NETS'==AppSubmissionDto.paymentMethod}">
                        <th scope="col" >Transactional No.</th>
                    </c:if>--%>
                    <c:if test="${requestInformationConfig == null}">
                        <th scope="col" >Transactional No.</th>
                    </c:if>
                    <th scope="col" >Date & Time</th>
                    <th scope="col" >Amount Deducted</th>
                    <th scope="col" >Payment Method</th>
                </tr>
                </thead>
                <tbody>
                <c:choose>
                    <c:when test="${'APTY005' ==AppSubmissionDto.appType}">
                        <c:forEach var="ackPageAppSubmission" items="${ackPageAppSubmissionDto}">
                            <tr>
                                <td><c:out value="${ackPageAppSubmission.appGrpNo}"/></td>
                                    <%--<c:if test="${'Credit'==AppSubmissionDto.paymentMethod or 'NETS'==AppSubmissionDto.paymentMethod}">
                                        <td><c:out value="${txnRefNo}"/></td>
                                    </c:if>--%>
                                <c:if test="${requestInformationConfig == null}">
                                    <td>
                                        <c:choose>
                                            <c:when test="${ackPageAppSubmission.amount == null || ackPageAppSubmission.amount == 0}">
                                                N/A
                                            </c:when>
                                            <c:when test="${empty txnRefNo || ackPageAppSubmission.paymentMethod == null}">
                                                N/A
                                            </c:when>
                                            <c:otherwise>
                                                <c:out value="${txnRefNo}"/>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                </c:if>
                                <td><c:out value="${txnDt}"/></td>
                                <td>
                                    <c:choose>
                                        <c:when test="${ackPageAppSubmission.amount == null}">
                                            N/A
                                        </c:when>
                                        <c:otherwise>
                                            <c:out value="${ackPageAppSubmission.amountStr}"/>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td>
                                    <c:choose>
                                        <c:when test="${ackPageAppSubmission.paymentMethod == null}">
                                            N/A
                                        </c:when>
                                        <c:otherwise>
                                            <iais:code code="${ackPageAppSubmission.paymentMethod}"/>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                            </tr>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <tr>
                            <td><c:out value="${AppSubmissionDto.appGrpNo}"/></td>
                                <%--<c:if test="${'Credit'==AppSubmissionDto.paymentMethod or 'NETS'==AppSubmissionDto.paymentMethod}">
                                    <td><c:out value="${txnRefNo}"/></td>
                                </c:if>--%>
                            <c:if test="${requestInformationConfig == null}">
                                <td>
                                    <c:choose>
                                        <c:when test="${empty txnRefNo || AppSubmissionDto.paymentMethod==null}">
                                            N/A
                                        </c:when>
                                        <c:otherwise>
                                            <c:out value="${txnRefNo}"/>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                            </c:if>
                            <td><c:out value="${txnDt}"/></td>
                            <td><c:if test="${AppSubmissionDto.amountStr==null}">N/A</c:if>
                                <c:if test="${AppSubmissionDto.amountStr!=null}">
                                    <c:out value="${AppSubmissionDto.amountStr}"/>
                                </c:if>
                            </td>
                            <td>
                                <c:choose>
                                    <c:when test="${AppSubmissionDto.paymentMethod==null}">
                                        N/A
                                    </c:when>
                                    <c:otherwise>
                                        <iais:code code="${AppSubmissionDto.paymentMethod}"/>

                                    </c:otherwise>
                                </c:choose>
                            </td>
                        </tr>
                    </c:otherwise>
                </c:choose>
                </tbody>
            </table>
        </div>
    </div>
</div>