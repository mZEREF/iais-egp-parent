<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %>
<%@ page import="com.ecquaria.cloud.moh.iais.common.constant.AppConsts" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ page import="com.ecquaria.cloud.moh.iais.common.utils.MaskUtil" %>
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

<webui:setLayout name="iais-internet"/>
<%@include file="../../common/dashboard.jsp"%>
<form method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
    <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>
    <div class="container">
        <div class="row">
            <div class="col-xs-12">
                <iais:body >
                    <div class="tab-gp col-xs-10" style="left: 8%;">
                        <div class="tab-content row">
                            <hr>
                            <div class="row">
                                <iais:field width="6" value="AR Centre"/>

                                <div class="col-md-6">
                                        ${donorInfoDataSubmissionDto.premisesDto.getPremiseLabel()}
                                </div>
                            </div>
                            <hr>
                            <div class="row">
                                <iais:field width="6" value="Is Sample from a Directed Donation?"/>

                                <div class="col-md-6">
                                    <c:out value="${donorInfoDataSubmissionDto.donorSampleDto.directedDonation ? 'Yes' : 'No'}"/>
                                </div>
                            </div>
<%--                            <hr>--%>
<%--                            <div class="row">--%>
<%--                                <div class="col-md-6">--%>
<%--                                    Donor Relation to Patient--%>
<%--                                </div>--%>
<%--                                <div class="col-md-6">--%>
<%--                                    <c:out value="${donorInfoDataSubmissionDto.donorSampleDto.donorRelation == 'F' ? 'Friend' : 'Relative'}"/>--%>
<%--                                </div>--%>
<%--                            </div>--%>
                            <hr>
                            <div class="row">
                                <iais:field width="6" value="Sample Type"/>

                                <div class="col-md-6">
                                    <iais:code code="${donorInfoDataSubmissionDto.donorSampleDto.sampleType}"/>
                                </div>
                            </div>
                            <hr>
                            <div class="row">
                                <iais:field width="6" value="Is Donor's Identity Known?"/>

                                <div class="col-md-6">
                                    <iais:code code="${donorInfoDataSubmissionDto.donorSampleDto.donorIdentityKnown}"/>
                                </div>
                            </div>
                            <hr>
                            <c:if test="${donorInfoDataSubmissionDto.donorSampleDto.directedDonation }">
                                <div class="row">
                                    <iais:field width="6" value="ID Type"/>

                                    <div class="col-md-6">
                                        <iais:code code="${donorInfoDataSubmissionDto.donorSampleDto.idType}"/>
                                    </div>
                                </div>
                                <hr>
                                <div class="row">
                                    <iais:field width="6" value="ID No."/>

                                    <div class="col-md-6">
                                        <c:out value="${donorInfoDataSubmissionDto.donorSampleDto.idNumber}" />
                                    </div>
                                </div>
                                <hr>
                                <div class="row">
                                    <iais:field width="6" value="Name"/>

                                    <div class="col-md-6">
                                        <c:out value="${donorInfoDataSubmissionDto.donorSampleDto.donorName}" />
                                    </div>
                                </div>
                                <hr>
                            </c:if>
                            <div class="row">
                                <iais:field width="6" value="Donor Sample Code"/>

                                <div class="col-md-6">
                                    <c:out value="${donorInfoDataSubmissionDto.donorSampleDto.donorSampleCode}" />
                                </div>
                            </div>
                            <hr>
                            <div class="row">
                                <iais:field width="6" value="Name of Bank / AR Centre where Sample is from"/>

                                <div class="col-md-6">
                                    <c:out value="${donorInfoDataSubmissionDto.donorSampleDto.sampleFromHciCode}" />
                                </div>
                            </div>
                            <hr>
                            <br>

                            <div class="row">
                                <b class="col-md-12" style="font-size:2.0rem">
                                    Donor Samples
                                </b>
                            </div>
                            <div class="table-gp">
                                <table aria-describedby="" class="table">
                                    <thead>
                                    <tr >
                                        <iais:sortableHeader field="Age" needSort="false"
                                                             value="Age when Samples Collected"/>
                                        <iais:sortableHeader needSort="false"
                                                             field="Availability"
                                                             value="Availability"/>
                                    </tr>
                                    </thead>
                                    <tbody class="form-horizontal">
                                    <c:choose>
                                        <c:when test="${empty donorInfoDataSubmissionDto.donorSampleDto.donorSampleAgeDtos}">
                                            <tr>
                                                <td colspan="15">
                                                    <iais:message key="GENERAL_ACK018"
                                                                  escape="true"/>
                                                </td>
                                            </tr>
                                        </c:when>
                                        <c:otherwise>
                                            <c:forEach var="donorSampleAge" items="${donorInfoDataSubmissionDto.donorSampleDto.donorSampleAgeDtos}">
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
                                                            <c:if test="${(donorSampleAge.age>40 or donorSampleAge.age<21) && (donorInfoDataSubmissionDto.donorSampleDto.sampleType == 'DST003')}">
                                                                <a class="donor-tooltip styleguide-tooltip flag2" style="float: right" href="javascript:void(0);"
                                                                   data-toggle="tooltip"
                                                                   data-html="true"
                                                                   title="&lt;p&gt;<iais:message key="DS_ERR044" escape="false"/>&lt;/p&gt;">!</a>
                                                            </c:if>
                                                            <c:if test="${(donorSampleAge.age>35 or donorSampleAge.age<21) && (donorInfoDataSubmissionDto.donorSampleDto.sampleType == 'DST001' || donorInfoDataSubmissionDto.donorSampleDto.sampleType == 'DST002')}">
                                                                <a class="donor-tooltip styleguide-tooltip flag2" style="float: right" href="javascript:void(0);"
                                                                   data-toggle="tooltip"
                                                                   data-html="true"
                                                                   title="&lt;p&gt;<iais:message key="DS_ERR045" escape="false"/>&lt;/p&gt;">!</a>
                                                            </c:if>
                                                        </p>
                                                    </td>
                                                    <td style="vertical-align:middle;">
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
                            <a href="#" onclick="javascript:$('#mainForm').submit();" ><em class="fa fa-angle-left"> </em> Back</a>
                        </div>
                    </div>
                </iais:body>
            </div>
        </div>
    </div>
</form>

