<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %>
<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>

<webui:setLayout name="iais-internet"/>
<br/>
<%@include file="../common/dashboard.jsp" %>
<form class="form-horizontal" method="post" id="LicenceReviewForm" action=<%=process.runtime.continueURL()%>>
    <input type="hidden" name="switch_value" value=""/>
    <div class="main-content">
        <div class="container">
            <div class="row">
                <div class="col-xs-12">
                    <div class="center-content">
                        <div class="licence-renewal-content">
                            <ul class="progress-tracker">
                                <li class="tracker-item disabled">Instructions</li>
                                <li class="tracker-item active">Licence Review</li>
                                <li class="tracker-item disabled">Payment</li>
                                <li class="tracker-item disabled">Acknowledgement</li>
                            </ul>
                            <%--content--%>
                            <div class="tab-pane" id="serviceInformationTab" role="tabpanel">
                                <div class="multiservice">
                                    <div class="tab-gp side-tab clearfix">
                                        <ul class="nav nav-pills nav-stacked hidden-xs hidden-sm" role="tablist">
                                            <c:forEach var="serviceName" items="${serviceNames}" varStatus="status">
                                                <li class="complete ${status.index == '0' ? 'active' : ''}" role="presentation"><a href="#serviceName${status.index}" aria-controls="lorem1" role="tab" data-toggle="tab">${serviceName}</a></li>
                                            </c:forEach>
                                        </ul>
                                        <div class="mobile-side-nav-tab visible-xs visible-sm">
                                            <select id="serviceSelect">
                                                <c:forEach var="serviceName" items="${serviceNames}" varStatus="status">
                                                    <option value="serviceName${status.index}">${serviceName}</option>
                                                </c:forEach>
                                            </select>
                                        </div>
                                        <%--main content--%>
                                        <div class="tab-content">
                                            <%--<div class="tab-pane active" id="previewTab" role="tabpanel">--%>
                                                <%--<div class="preview-gp">--%>

                                                    <c:forEach var="AppSubmissionDto" items="${renewDto.appSubmissionDtos}" varStatus="status">
                                                        <c:set var="AppSubmissionDto" value="${AppSubmissionDto}" scope="request"/>
                                                        <c:set var="documentIndex" value="${status.index}" scope="request"/>
                                                        <div class="tab-pane ${status.index == '0' ? 'active' : ''}" id="serviceName${status.index}" role="tabpanel">
                                                            <div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true">
                                                                <%@include file="../common/previewPremises.jsp"%>
                                                                <%@include file="../common/previewPrimary.jsp"%>
                                                            </div>
                                                        </div>
                                                    </c:forEach>

                                                <%--</div>--%>
                                            <%--</div>--%>
                                        </div>
                                        <%--main content--%>
                                    </div>
                                </div>
                            </div>


                            <%--content--%>
                        </div>
                        <div class="application-tab-footer">
                            <div class="row">
                                <div class="col-xs-12 col-sm-6">
                                    <p><a id="BACK" class="back"><em class="fa fa-angle-left"></em> Back</a></p>
                                </div>
                                <div class="col-xs-12 col-sm-6">
                                    <div class="text-right text-center-mobile"><a class="btn btn-primary" href="#">Preview the Next Service</a></div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</form>
<script>
    $('#BACK').click(function () {
        $('[name="switch_value"]').val('instructions');
        $('#LicenceReviewForm').submit();
    });
</script>