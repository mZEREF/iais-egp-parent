<%--
  Created by IntelliJ IDEA.
  User: ZiXian
  Date: 2021/6/7
  Time: 14:58
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<webui:setLayout name="iais-blank"/>
<br/>
<%--<div class="navigation-gp">

</div>--%>
<div class="main-content">
    <div class="container">
        <div class="col-xs-12">
            <div class="dashboard-page-title">
                <h1>${title}</h1>
                <br/>
                <c:if test="${empty renewAck}">
                    <h3>${smallTitle}</h3>
                </c:if>
            </div>
        </div>
        <div class="col-xs-12">
            <br/>
            <label style="font-size: 20px">Submission Successful</label>
        </div>
        <c:choose>
            <c:when test="${'APTY004' ==AppSubmissionDto.appType}">
                <c:forEach items="${serviceNamesAck}" var="serviceName">
                    <div class="col-xs-12">
                        <p class="ack-font-20">- <strong><c:out value="${serviceName}"/> </strong></p>
                    </div>
                </c:forEach>
            </c:when>
            <c:when test="${not empty allSvcNames}"><%-- RFC --%>
                <c:forEach items="${allSvcNames}" var="name">
                    <div class="col-xs-12">
                        <p>- <strong><c:out value="${name}"/></strong></p>
                    </div>
                </c:forEach>
            </c:when>
            <c:when test="${menuRfc=='rfc'}">

                <c:forEach items="${appSubmissionDtos}" var="appSubmissionDto">
                    <div class="col-xs-12">
                        <p>- <strong><c:out value="${appSubmissionDto.appSvcRelatedInfoDtoList[0].serviceName}"></c:out></strong></p>
                    </div>
                </c:forEach>
            </c:when>
            <c:otherwise>
                <c:forEach items="${hcsaServiceDtoList}" var="list">
                    <div class="col-xs-12">
                        <p class="ack-font-20">- <strong><c:out value="${list.svcName}"/> </strong></p>
                    </div>
                </c:forEach>
            </c:otherwise>
        </c:choose>
        <div class="ack-font-16">
            <c:choose>
                <c:when test="${'APTY004' ==AppSubmissionDto.appType}">
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
                </c:when>
                <c:when test="${menuRfc=='rfc'}">
                    <div class="col-xs-12"><p class="ack-font-14">A confirmation email will be sent to ${emailAddress}.</p></div>
                    <div class="col-xs-12"> <p class="ack-font-14"><iais:message key="NEW_ACK005" escape="false"></iais:message></p></div>
                    <c:if test="${dAmount!='$0.0'}">
                        <div class="col-xs-12"><p class="ack-font-14">Transactional details:</p></div>
                        <div class="col-xs-12"><div class="table-responsive">
                            <table aria-describedby="" class="table">
                                <thead>
                                <tr>
                                    <th scope="col" >Application No.</th>
                                        <%--<c:if test="${'Credit'== payMethod or 'NETS'== payMethod}">
                                            <th scope="col" >Transactional No.</th>
                                        </c:if>--%>
                                    <th scope="col" >Transactional No.</th>
                                    <th scope="col" >Date & Time</th>
                                    <th scope="col" >Amount Deducted</th>
                                    <th scope="col" >Payment Method</th>
                                </tr>
                                </thead>
                                <tbody>
                                <tr>
                                    <td>${appSubmissionDtos.get(0).appGrpNo}</td>
                                        <%--<c:if test="${'Credit'== payMethod or 'NETS'== payMethod}">
                                            <td><c:out value="${pmtRefNo}"/></td>
                                        </c:if>--%>
                                    <td>
                                        <c:choose>
                                            <c:when test="${empty txnRefNo || empty payMethod}">
                                                N/A
                                            </c:when>
                                            <c:otherwise>
                                                <c:out value="${txnRefNo}"/>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td><fmt:formatDate value="${createDate}" pattern="dd/MM/yyyy"></fmt:formatDate></td>
                                    <td>${dAmount}</td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${empty payMethod}">N/A</c:when>
                                            <c:otherwise> <iais:code code="${payMethod}"/></c:otherwise>
                                        </c:choose>
                                    </td>
                                </tr>
                                </tbody>
                            </table>
                        </div></div>
                    </c:if>
                </c:when>
                <c:otherwise>
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
                                                <c:if test="${requestInformationConfig == null}">
                                                    <td>
                                                        <c:choose>
                                                            <c:when test="${ackPageAppSubmission.amount == null || ackPageAppSubmission.amount == 0}">
                                                                N/A
                                                            </c:when>
                                                            <c:when test="${empty txnRefNo || ackPageAppSubmission.paymentMethod==null}">
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
                                                        <c:when test="${ackPageAppSubmission.paymentMethod==null}">
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
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</div>

<script type="text/javascript">
    $(document).ready(function () {

        var userAgent = navigator.userAgent;
        var isChrome = userAgent.indexOf("Chrome") > -1 && userAgent.indexOf("Safari") > -1;

        // disabled <a>
        $('a').prop('disabled',true);
        if(isChrome){
            addPrintListener();
            window.print();
        }else{
            window.print();
            window.close();
        }

    });

    var addPrintListener = function () {
        if (window.matchMedia) {
            var mediaQueryList = window.matchMedia('print');
            mediaQueryList.addListener(function(mql) {
                if (mql.matches) {

                } else {
                    window.close();
                }
            });
        }
    }

</script>