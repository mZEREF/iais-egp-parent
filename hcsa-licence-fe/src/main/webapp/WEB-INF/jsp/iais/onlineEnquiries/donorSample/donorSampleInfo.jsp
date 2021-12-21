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
                    <div class="col-xs-12">
                        <div class="tab-content row">
                            <div class=" col-md-12">

                                <hr>
                                <div class="row">
                                    <div class="col-md-6">
                                        AR Centre
                                    </div>
                                    <div class="col-md-6">
                                            ${donorInfoDataSubmissionDto.premisesDto.getPremiseLabel()}
                                    </div>
                                </div>
                                <hr>
                                <div class="row">
                                    <div class="col-md-6">
                                        Is Sample from a Directed Donation?
                                    </div>
                                    <div class="col-md-6">
                                        <c:out value="${donorInfoDataSubmissionDto.donorSampleDto.directedDonation ? 'Yes' : 'No'}"/>
                                    </div>
                                </div>
                                <hr>
                                <div class="row">
                                    <div class="col-md-6">
                                        Donor Relation to Patient
                                    </div>
                                    <div class="col-md-6">
                                        <c:out value="${donorInfoDataSubmissionDto.donorSampleDto.donorRelation == 'F' ? 'Friend' : 'Relative'}"/>
                                    </div>
                                </div>
                                <hr>
                                <div class="row">
                                    <div class="col-md-6">
                                        Sample Type
                                    </div>
                                    <div class="col-md-6">
                                        <iais:code code="${donorInfoDataSubmissionDto.donorSampleDto.sampleType}"/>
                                    </div>
                                </div>
                                <hr>
                                <div class="row">
                                    <div class="col-md-6">
                                        Is Donor's Identity Known?
                                    </div>
                                    <div class="col-md-6">
                                        <iais:code code="${donorInfoDataSubmissionDto.donorSampleDto.donorIdentityKnown}"/>
                                    </div>
                                </div>
                                <hr>
                                <div class="row">
                                    <div class="col-md-6">
                                        ID Type
                                    </div>
                                    <div class="col-md-6">
                                        <iais:code code="${donorInfoDataSubmissionDto.donorSampleDto.idType}"/>
                                    </div>
                                </div>
                                <hr>
                                <div class="row">
                                    <div class="col-md-6">
                                        ID No.
                                    </div>
                                    <div class="col-md-6">
                                        <c:out value="${donorInfoDataSubmissionDto.donorSampleDto.idNumber}" />
                                    </div>
                                </div>
                                <hr>
                                <div class="row">
                                    <div class="col-md-6">
                                        Name
                                    </div>
                                    <div class="col-md-6">
                                        <c:out value="${donorInfoDataSubmissionDto.donorSampleDto.donorName}" />
                                    </div>
                                </div>
                                <hr>

                            </div>

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
                                                <tr>
                                                    <td style="vertical-align:middle;">
                                                        <c:out value="${donorSampleAge.age}"/>
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
                        <div class="row ">
                            <a href="#" onclick="javascript:$('#mainForm').submit();" ><em class="fa fa-angle-left"> </em> Back</a>
                        </div>
                    </div>
                </iais:body>
            </div>
        </div>
    </div>
</form>

