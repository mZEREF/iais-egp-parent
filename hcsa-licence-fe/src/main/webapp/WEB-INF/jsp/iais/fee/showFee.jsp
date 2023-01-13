<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ page import="com.ecquaria.cloud.moh.iais.common.utils.Formatter" %>
<%
    //handle to the Engine APIs
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");


%>
<webui:setLayout name="iais-internet"/>
<%@include file="../common/dashboard.jsp"%>
<form  method="post" id="mainForm"   action=<%=process.runtime.continueURL()%>>
    <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>
    <c:set var="applicationType" value="${'APTY002' ==calculateFeeConditionDto.applicationType ? 'New Licence':'Renewal'}"/>

    <div class="container">
        <div class="row">
            <div class="col-xs-12">

                <div class="preview-gp">
                    <div class="row">
                        <div class="col-xs-12">
                            &nbsp;
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-xs-12">
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
                                    <c:when test="${'APTY005' ==calculateFeeConditionDto.applicationType}">

                                        <c:forEach items="${feeDto.feeInfoDtos}" var="feeInfoDto" varStatus="feeInfoStat">
                                            <c:set var="baseSvcFeeExt" value="${feeInfoDto.baseSvcFeeExt}"/>
                                            <c:set var="complexSpecifiedFeeExt" value="${feeInfoDto.complexSpecifiedFeeExt}"/>
                                            <c:set var="simpleSpecifiedFeeExt" value="${feeInfoDto.simpleSpecifiedFeeExt}"/>
                                            <c:set var="thbSpecifiedFeeExt" value="${feeInfoDto.thbSpecifiedFeeExt}"/>
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
                                                        <p>Amendment</p>
                                                    </td>
                                                    <td>
                                                        <p><c:out value="${baseSvcFeeExt.appGroupNo}-0${AppSubmissionDto.appGrpNo== baseSvcFeeExt.appGroupNo? (grpStat.index+1): (feeInfoStat.index+1)}"/></p>
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
                                                        <p><strong><c:out value="${AppSubmissionDto.serviceName}"/></strong></p>
                                                    </td>
                                                    <td>
                                                        <p>Amendment</p>
                                                    </td>
                                                    <td>
                                                        <p><c:out value="${AppSubmissionDto.appGrpNo}-0${feeInfoStat.index+1}"/></p>
                                                    </td>
                                                    <td>
                                                        <p>
                                                            <c:out value="${AppSubmissionDto.amountStr}"/>
                                                        </p>
                                                    </td>
                                                </tr>
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

                                    </c:when>

                                    <c:otherwise>
                                        <c:forEach items="${feeDto.feeInfoDtos}" var="feeInfoDto" varStatus="feeInfoStat">
                                            <c:set var="baseSvcFeeExt" value="${feeInfoDto.baseSvcFeeExt}"/>
                                            <c:set var="includedSvcFeeExtList" value="${feeInfoDto.includedSvcFeeExtList}"/>
                                            <c:set var="bundleSvcFeeExt" value="${feeInfoDto.bundleSvcFeeExt}"/>
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
                                                            <p>${applicationType}</p>
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
                                                        <p>&nbsp;&nbsp;</p>
                                                        <c:forEach var="svcIndex" items="${baseSvcFeeExt.svcIndexList}">
                                                            <p>${applicationType}</p>
                                                            <p>&nbsp;&nbsp;</p>
                                                        </c:forEach>
                                                        <c:forEach var="svcIndex" items="${includedSvcFeeExtRoot.svcIndexList}">
                                                            <p>${applicationType}</p>
                                                            <p>&nbsp;&nbsp;</p>
                                                        </c:forEach>
                                                    </td>
                                                    <td>
                                                        <p>&nbsp;&nbsp;</p>
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
                                                        <c:forEach var="svcIndex" items="${bundleSvcFeeExt.svcIndexList}">
                                                            <p>${applicationType}</p>
                                                            <p>&nbsp;&nbsp;</p>
                                                        </c:forEach>
                                                    </td>
                                                    <td>
                                                        <c:forEach var="svcIndex" items="${bundleSvcFeeExt.svcIndexList}">
                                                            <p><c:out value="${bundleSvcFeeExt.appGroupNo}-0${svcIndex}"/></p>
                                                            <p>&nbsp;&nbsp;</p>
                                                        </c:forEach>
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
                                                        <c:forEach var="svcName" items="${laterFeeDetail.svcNames}">
                                                            <p><strong><c:out value="${svcName}"/></strong></p>
                                                        </c:forEach>
                                                    </td>
                                                    <td style="border-top: none;">N/A</td>
                                                    <td style="border-top: none;">N/A</td>
                                                    <td style="border-top: none;"><p>${laterFeeDetail.lateFeeAmoumtStr}</p></td>
                                                </tr>
                                            </c:forEach>
                                        </c:forEach>

                                    </c:otherwise>
                                </c:choose>
                                <tr>
                                    <td></td>
                                    <td></td>
                                    <td><p><strong>Total&nbsp;${feeDto.feeDetail}</strong></p></td>
                                    <td><p><c:out value="${feeDto.total}"/></p></td>
                                </tr>
                                </tbody>
                            </table>
                        </div>
                    </div>
                    <div class="col-xs-12">
                        <div class="row">
                            <div class="col-xs-12">
                                <a href="#" onclick="javascript:$('#mainForm').submit();" ><em class="fa fa-angle-left"> </em> Back</a>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-xs-12">
                                &nbsp;
                            </div>
                        </div>
                    </div>
                </div>

            </div>
        </div>
    </div>
</form>


