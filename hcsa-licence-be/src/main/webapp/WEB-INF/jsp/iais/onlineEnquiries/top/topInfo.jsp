<%@ page import="com.ecquaria.cloud.moh.iais.common.constant.AuditTrailConsts" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%
    //handle to the Engine APIs
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>
<style>
    hr {
        border-top: 1px solid #bababa;
        margin-bottom: 15px;
    }
</style>

<webui:setLayout name="iais-intranet"/>
<div class="dashboard" >
    <form method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
        <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>
        <input type="hidden" name="back" id="back"/>
        <div class="main-content">
            <div class="row">
                <div class="col-lg-12 col-xs-12">
                    <div class="center-content">
                        <div class="intranet-content">
                            <iais:body >
                                <div class="tab-gp col-xs-10" style="left: 8%;">
                                    <div class="tab-content row">
                                        <hr>
                                        <div class="row">
                                            <iais:field width="6" cssClass="col-md-6" value="AR Centre"/>

                                            <div class="col-md-6">
                                                    ${topDataSubmissionDto.premisesDto.getPremiseLabel()}
                                            </div>
                                        </div>
                                        <hr>
                                        <div class="row">
                                            <iais:field width="6" cssClass="col-md-6" value="Is Sample from a Directed Donation?"/>

                                            <div class="col-md-6">
                                                <c:out value="${topDataSubmissionDto.donorSampleDto.directedDonation ? 'Yes' : 'No'}"/>
                                            </div>
                                        </div>

                                        <hr>
                                        <c:if test="${!topDataSubmissionDto.donorSampleDto.directedDonation }">
                                            <div class="row">
                                                <iais:field width="6" cssClass="col-md-6" value="Sample Type"/>

                                                <div class="col-md-6">
                                                    <iais:code code="${topDataSubmissionDto.donorSampleDto.sampleType}"/>
                                                </div>
                                            </div>
                                            <hr>
                                            <div class="row">
                                                <iais:field width="6" cssClass="col-md-6" value="Is Donor's Identity Known?"/>

                                                <div class="col-md-6">
                                                    <iais:code code="${topDataSubmissionDto.donorSampleDto.donorIdentityKnown}"/>
                                                </div>
                                            </div>
                                            <hr>
                                            <c:if test="${topDataSubmissionDto.donorSampleDto.donorIdentityKnown !='DIK001' }">
                                                <div class="row"  >
                                                    <c:if test="${topDataSubmissionDto.donorSampleDto.donorUseSize > donorResultSize}">
                                                        <iais:field width="6" cssClass="col-md-6" value="Donor Sample Code" info="${donorMessageTip}"/>
                                                    </c:if>
                                                    <c:if test="${topDataSubmissionDto.donorSampleDto.donorUseSize <= donorResultSize}">
                                                        <iais:field width="6" cssClass="col-md-6" value="Donor Sample Code"/>
                                                    </c:if>
                                                    <div class="col-md-6">
                                                        <c:out value="${topDataSubmissionDto.donorSampleDto.donorSampleCode}" />
                                                    </div>
                                                </div>
                                                <hr>
                                            </c:if>

                                        </c:if>
                                        <c:if test="${topDataSubmissionDto.donorSampleDto.directedDonation ||topDataSubmissionDto.donorSampleDto.donorIdentityKnown =='DIK001'}">
                                            <div class="row">
                                                <iais:field width="6" cssClass="col-md-6" value="ID Type"/>

                                                <div class="col-md-6">
                                                    <iais:code code="${topDataSubmissionDto.donorSampleDto.idType}"/>
                                                </div>
                                            </div>
                                            <hr>
                                            <div class="row">
                                                <c:if test="${topDataSubmissionDto.donorSampleDto.donorUseSize > donorResultSize}" >
                                                    <iais:field width="6" cssClass="col-md-6" value="ID No."  info="${donorMessageTip}" />
                                                </c:if>
                                                <c:if test="${topDataSubmissionDto.donorSampleDto.donorUseSize <= donorResultSize}">
                                                    <iais:field width="6" cssClass="col-md-6" value="ID No."   />
                                                </c:if>
                                                <div class="col-md-6">
                                                    <c:out value="${topDataSubmissionDto.donorSampleDto.idNumber}" />
                                                </div>
                                            </div>
                                            <hr>
                                            <div class="row">
                                                <iais:field width="6" cssClass="col-md-6" value="Name"/>

                                                <div class="col-md-6">
                                                    <c:out value="${topDataSubmissionDto.donorSampleDto.donorName}" />
                                                </div>
                                            </div>
                                            <hr>
                                        </c:if>
                                        <c:if test="${!topDataSubmissionDto.donorSampleDto.directedDonation }">
                                            <div class="row">
                                                <iais:field width="6" cssClass="col-md-6" value="Name of Bank / AR Centre where Sample is from"/>

                                                <div class="col-md-6">
                                                    <c:out value="${topDataSubmissionDto.donorSampleDto.sampleFromHciCode}" />
                                                </div>
                                            </div>
                                            <hr>
                                        </c:if>

                                        <br>

                                        <div class="row">
                                            <strong class="col-md-12" style="font-size:2.0rem">
                                                Donor Samples
                                            </strong>
                                        </div>
                                        <div class="table-gp">
                                            <table aria-describedby="" class="table">
                                                <thead>
                                                <tr >
                                                    <iais:sortableHeader field="Age" needSort="false" style="width: 50%;"
                                                                         value="Age when Samples Collected"/>
                                                    <iais:sortableHeader needSort="false" style="width: 50%;"
                                                                         field="Availability"
                                                                         value="Availability"/>
                                                </tr>
                                                </thead>
                                                <tbody class="form-horizontal">
                                                <c:choose>
                                                    <c:when test="${empty topDataSubmissionDto.donorSampleDto.donorSampleAgeDtos}">
                                                        <tr>
                                                            <td colspan="15">
                                                                <iais:message key="GENERAL_ACK018"
                                                                              escape="true"/>
                                                            </td>
                                                        </tr>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <c:forEach var="donorSampleAge" items="${topDataSubmissionDto.donorSampleDto.donorSampleAgeDtos}">
                                                            <style>
                                                                .donor-tooltip {
                                                                    font-size: 1.4rem;
                                                                    font-weight: 700;
                                                                    width: 23px;
                                                                    height: 23px;
                                                                    background: #dc3545;
                                                                    display: inline-block;
                                                                    line-height: 23px;
                                                                    color: white;
                                                                    font-style: italic;
                                                                    border-radius: 50%;
                                                                    padding-left: 8px;
                                                                    padding-right: 10px;
                                                                }
                                                                .form-horizontal p {
                                                                    line-height: 23px;
                                                                }
                                                            </style>
                                                            <tr>
                                                                <td style="vertical-align:middle;">
                                                                    <p style="width: 65px;">
                                                                        <c:out value="${donorSampleAge.age}"/>
                                                                        <c:if test="${(donorSampleAge.age>40 or donorSampleAge.age<21) && (topDataSubmissionDto.donorSampleDto.sampleType == 'DST003')}">
                                                                            <a class="donor-tooltip styleguide-tooltip flag2" style="float: right" href="javascript:void(0);"
                                                                               data-toggle="tooltip"
                                                                               data-html="true"
                                                                               title="&lt;p&gt;<iais:message key="DS_ERR044" escape="false"/>&lt;/p&gt;">!</a>
                                                                        </c:if>
                                                                        <c:if test="${(donorSampleAge.age>35 or donorSampleAge.age<21) && (topDataSubmissionDto.donorSampleDto.sampleType == 'DST001' || topDataSubmissionDto.donorSampleDto.sampleType == 'DST002')}">
                                                                            <a class="donor-tooltip styleguide-tooltip flag2" style="float: right" href="javascript:void(0);"
                                                                               data-toggle="tooltip"
                                                                               data-html="true"
                                                                               title="&lt;p&gt;<iais:message key="DS_ERR045" escape="false"/>&lt;/p&gt;">!</a>
                                                                        </c:if>
                                                                    </p>
                                                                </td>
                                                                <td style="vertical-align:middle;" class="col-md-12">
                                                                    <iais:code code="${donorSampleAge.status}"/>
                                                                </td>
                                                            </tr>
                                                        </c:forEach>
                                                    </c:otherwise>
                                                </c:choose>
                                                </tbody>
                                            </table>
                                        </div>
                                    </div>
                                    <div class="tab-content row">
                                        <a href="#" onclick="javascript:$('#back').val('back');$('#mainForm').submit();" ><em class="fa fa-angle-left"> </em> Back</a>
                                    </div>
                                </div>
                            </iais:body>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </form>
</div>

