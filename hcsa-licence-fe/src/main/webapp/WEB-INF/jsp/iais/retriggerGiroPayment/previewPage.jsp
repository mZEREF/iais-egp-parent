<%--
  Created by IntelliJ IDEA.
  User: ZiXian
  Date: 2020/10/27
  Time: 14:52
  To change this template use File | Settings | File Templates.
--%>
<%@ page import="com.ecquaria.cloud.RedirectUtil" %>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais"%>
<%
    //handle to the Engine APIs
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>
<webui:setLayout name="iais-internet"/>
<%@ include file="../common/dashboard.jsp" %>
<form method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
    <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
    <input type="hidden" name="crud_action_type_tab" value="">
    <div class="main-content">
        <div class="container">
            <div class="row">
                <div class="col-xs-12">
                    <div class="tab-gp steps-tab">
                        <div class="tab-content">
                            <div class="tab-pane active" id="previewTab" role="tabpanel">
                                <div class="preview-gp">
                                    <div class="row">
                                    </div>
                                    <div class="row">
                                        <div class="col-xs-12">
                                            <div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true">
                                                <%@include file="../common/previewPremises.jsp"%>
                                                <%@include file="../common/previewPrimary.jsp"%>
                                                <c:forEach var="hcsaServiceDto" items="${hcsaServiceDtoList}" varStatus="status" >
                                                    <div class="panel panel-default svc-content">
                                                        <input type="hidden" name="iframeId" value="svcIframe${status.index}"/>
                                                        <input type="hidden" name="maskId" value="<iais:mask name="svc${status.index}" value="${hcsaServiceDto.id}"/>"/>
                                                        <input type="hidden" name="maskName" value="svc${status.index}"/>

                                                        <div class="panel-heading "  id="headingServiceInfo" role="tab">
                                                            <h4 class="panel-title svcTitle"><a  class="svc-pannel-collapse collapsed"  role="button" data-toggle="collapse" href="#collapseServiceInfo${status.index}" aria-expanded="true" aria-controls="collapseServiceInfo">Service Related Information - ${hcsaServiceDto.svcName}</a></h4>
                                                        </div>

                                                        <div class=" panel-collapse collapse" id="collapseServiceInfo${status.index}" role="tabpanel" aria-labelledby="headingServiceInfo${status.index}" >
                                                            <div class="panel-body">
                                                                <c:if test="${AppSubmissionDto.appEditSelectDto==null||AppSubmissionDto.appEditSelectDto.serviceEdit}">
                                                                    <p class="mb-0">
                                                                    <div class="text-right app-font-size-16">
                                                                        <a href="#" class="doSvcEdit"><em class="fa fa-pencil-square-o"></em>Edit</a>
                                                                        <input type="hidden" value="${hcsaServiceDto.svcCode}" name="svcCode" />
                                                                    </div>
                                                                    </p>
                                                                </c:if>
                                                                <input type="hidden" value="0" name="svcCount" />
                                                            </div>
                                                        </div>
                                                    </div>
                                                </c:forEach>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div class="application-tab-footer">
                                    <div class="row">
                                        <div class="col-xs-12 col-sm-9"></div>
                                        <div class="col-xs-12 col-sm-2">
                                            <a class="next btn btn-primary" id = "Next">Make Payment</a></div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</form>

<script type="text/javascript">

    $(document).ready(function() {
        $('#Next').click(function () {
            $('#mainForm').submit();
        });
    });



    $('.svcTitle').click(function () {
        var $svcEle = $(this).closest('div.svc-content');
        var svcCount = $svcEle.find('input[name="svcCount"]').val();
        if(0 != svcCount){
            return;
        }
        showWaiting();
        var maskName = $svcEle.find('input[name="maskName"]').val();
        var maskId = $svcEle.find('input[name="maskId"]').val();
        var iframeId = $svcEle.find('input[name="iframeId"]').val();
        $svcEle.find('input[name="svcCount"]').val(1);
        var url ='${pageContext.request.contextPath}<%=RedirectUtil.appendCsrfGuardToken("/eservice/INTERNET/MohServiceRelatedInformation/1/PrepareView",request)%>&'+maskName+'='+maskId+'&maskName='+maskName+'&iframeId='+iframeId;
        var iframeHtml = "<iframe id=\""+ iframeId+ "\"  class=\"svc-iframe\" title=\"\" src=\""+ url +"\"  width=\"100%\" frameborder =\"0\" ></iframe>"
        $svcEle.find('input[name="svcCount"]').after(iframeHtml);

    });

</script>

