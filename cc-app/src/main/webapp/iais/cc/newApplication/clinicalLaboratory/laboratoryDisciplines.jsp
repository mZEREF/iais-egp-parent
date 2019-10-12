<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>

<%
    //handle to the Engine APIs
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");

    String actionFrom1 = ParamUtil.getString(request,IaisEGPConstant.CRUD_ACTION_TYPE_FORM);
    if(StringUtil.isEmpty(actionFrom1)){
        actionFrom1 = (String)ParamUtil.getRequestAttr(request,IaisEGPConstant.CRUD_ACTION_TYPE_FORM);
    }

//    String pages = "/iais/clinical/newApplication/laboratory.jsp";
//    if(!"clinical".equals(actionFrom1)){
//        pages = "/iais/clinical/newApplication/bloodBank.jsp";
//    }

%>
<webui:setLayout name="iais-cc"/>
<%@ include file="../dashboard.jsp" %>
<form method="post" id="mainForm" class="__egovform" action=<%=process.runtime.continueURL()%>>
    <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
    <input type="hidden" name="crud_action_type_form" value="">
    <div class="main-content">
        <div class="container">
            <div class="row">
                <div class="col-xs-12">
                    <div class="tab-gp steps-tab">
                        <%@ include file="../navTabs.jsp" %>
                        <div class="tab-content">
                            <div class="tab-pane active" id="serviceInformationTab" role="tabpanel">
                                <div class="multiservice">
                                    <div class="tab-gp side-tab clearfix">
                                        <%@ include file="../formTabs.jsp" %>
                                        <div class="tab-content">
                                            <div class="tab-pane active" id="clinicalLab" role="tabpanel">
                                                <h2 class="service-title">SERVICE 1 OF 2: <b>CLINICAL LABORATORY</b></h2>
                                                <div class="visible-xs visible-sm servive-subtitle">
                                                    <p>Step 1 of 5</p>
                                                    <h3>Laboratory Disciplines</h3>
                                                </div>
                                                <ul class="progress-tracker">
                                                    <li class="tracker-item active" data-service-step="laboratory-disciplines">Laboratory Disciplines</li>
                                                    <li class="tracker-item disabled" data-service-step="clinical-governance-officer">Clinical Governance Officers</li>
                                                    <li class="tracker-item disabled" data-service-step="discipline-allocation">Discipline Allocation</li>
                                                    <li class="tracker-item disabled" data-service-step="principal-officers">Principal Officers</li>
                                                    <li class="tracker-item disabled">Documents</li>
                                                </ul>
                                                <div class="application-service-steps">
                                                    <div class="laboratory-disciplines">
                                                        <h2>Laboratory Disciplines</h2>
                                                        <p>Please select the service disciplines you would like to apply at your premises.</p>
                                                        <iframe id="__egovform-iframe" name="__egovform-iframe" frameborder="no" width="100%" heihgt="100%" scrolling="no" src="./CR_Discipline.jsp"></iframe>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div class="application-tab-footer">
                                    <div class="row">
                                        <div class="col-xs-12 col-sm-6"><a class="back" href="application-document.html"><i class="fa fa-angle-left"></i> Back</a></div>
                                        <div class="col-xs-12 col-sm-6">
                                            <div class="button-group"><a class="btn btn-secondary" href="#">Save as Draft</a><a class="next btn btn-primary disabled hidden" href="application-preview.html"><i class="fa fa-angle-left"></i>) Next</a><a class="next btn btn-primary disabled" data-goto="clinical-governance-officer">Next</a></div>
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




