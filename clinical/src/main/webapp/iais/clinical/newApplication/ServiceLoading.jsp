<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>

<%
    //handle to the Engine APIs
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");

    String actionFrom1 = ParamUtil.getString(request,IaisEGPConstant.CRUD_ACTION_TYPE_FORM);
    if(StringUtil.isEmpty(actionFrom1)){
        actionFrom1 = (String)ParamUtil.getRequestAttr(request,IaisEGPConstant.CRUD_ACTION_TYPE_FORM);
    }

    String pages = "/iais/clinical/newApplication/laboratory.jsp";
    if(!"clinical".equals(actionFrom1)){
        pages = "/iais/clinical/newApplication/bloodBank.jsp";
    }

%>
<webui:setLayout name="iais-cc"/>
<%@ include file="./dashboard.jsp" %>
<form method="post" id="mainForm" class="__egovform" action=<%=process.runtime.continueURL()%>>
    <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">

    <input type="hidden" name="crud_action_type" value="">
    <input type="hidden" name="crud_action_value" value="">
    <input type="hidden" name="crud_action_additional" value="">
<div class="main-content">
    <div class="container">
        <div class="row">
            <div class="col-xs-12">
                <div class="tab-gp steps-tab">
                    <%@ include file="./navTabs.jsp" %>
                    <div class="tab-content">
                        <div class="tab-pane active" id="serviceInformationTab" role="tabpanel">
                            <div class="multiservice">
                                <div class="tab-gp side-tab clearfix">
                                    <%@ include file="./formTabs.jsp" %>
                                    <div class="tab-content">
                                        <div class="tab-pane active" id="clinicalLab" role="tabpanel">
                                            <jsp:include   page="<%=pages%>" flush="true"/>
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




