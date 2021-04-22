<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>

<webui:setLayout name="iais-blank"/>
<br/>
<%--<%@include file="../common/dashboard.jsp" %>--%>
<form method="post" class="table-responsive" id="mainForm" action=<%=process.runtime.continueURL()%>>
    <div class="main-content">
        <div class="container">
            <div class="row">
                <c:set var="printFlag" value="test"/>
                <c:forEach begin="0" end="${viewSubmissons.size()-1}" step="1" varStatus="submisonStat">
                    <c:set var="AppSubmissionDto" value="${viewSubmissons[submisonStat.index]}"/>
                    <div class="col-xs-12">
                        <div class="tab-gp steps-tab">
                            <div class="tab-content">
                                <div class="tab-pane active" id="previewTab" role="tabpanel">
                                    <div class="preview-gp">
                                        <c:if test="${submisonStat.first}">
                                            <div class="row">
                                                <br/><br/><br/>
                                            </div>
                                        </c:if>
                                        <div class="row">
                                            <div class="col-xs-12">
                                                <div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true">
                                                    <c:set var="documentIndex" value="${submisonStat.index}"/>
                                                    <%@include file="../common/previewPremises.jsp"%>
                                                    <%@include file="../common/previewPrimary.jsp"%>

                                                    <c:set var="appGrpPremisesDtoList" value="${AppSubmissionDto.appGrpPremisesDtoList}"></c:set>
                                                    <c:forEach var="currentPreviewSvcInfo" items="${AppSubmissionDto.appSvcRelatedInfoDtoList}" varStatus="svcStat">

                                                        <c:set var="reloadDisciplineAllocationMap" value="${currentPreviewSvcInfo.reloadDisciplineAllocationMap}"></c:set>
                                                        <c:set var="ReloadPrincipalOfficers" value="${currentPreviewSvcInfo.reloadPoDtoList}"></c:set>
                                                        <c:set var="ReloadDeputyPrincipalOfficers" value="${currentPreviewSvcInfo.reloadDpoList}"></c:set>
                                                        <c:set var="svcDocConfig" value="${currentPreviewSvcInfo.svcDocConfig}"/>

                                                        <div class="panel panel-default svc-content">
                                                            <div class="panel-heading"  id="headingServiceInfo" role="tab">
                                                                <h4 class="panel-title"><a class="svc-pannel-collapse collapsed a-panel-collapse"  role="button" data-toggle="collapse" href="#collapseServiceInfo${submisonStat.index}${svcStat.index}" aria-expanded="true" aria-controls="collapseServiceInfo">Service Related Information - ${currentPreviewSvcInfo.serviceName}</a></h4>
                                                            </div>

                                                            <div class=" panel-collapse collapse in" id="collapseServiceInfo${submisonStat.index}${svcStat.index}" role="tabpanel" aria-labelledby="headingServiceInfo">
                                                                <div class="panel-body">
                                                                    <div class="panel-main-content">

                                                                        <%@include file="../common/previewSvcInfo.jsp"%>
                                                                    </div>
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </c:forEach>
                                                </div>
                                            </div>
                                        </div>


                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </div>
    </div>

</form>

<script type="text/javascript">
    $(document).ready(function () {
        doPrint();

    });
    var doPrint = function () {
        $('a').prop('disabled',true);
        window.print();
        window.close();
    }

</script>