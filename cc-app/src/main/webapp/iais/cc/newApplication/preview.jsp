<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%
    //handle to the Engine APIs
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>
<webui:setLayout name="iais-cc"/>
<%@ include file="./dashboard.jsp" %>
<form method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
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
                            <div class="tab-pane active" id="previewTab" role="tabpanel">
                                <div class="preview-gp">
                                    <div class="row">
                                        <div class="col-xs-12 col-md-10">
                                            <p>Please note that you will not be able to pay for this application if you have not provided the mandatory information and documents.</p>
                                        </div>
                                        <div class="col-xs-12 col-md-2 text-right">
                                            <p class="print"><a href="#"> <i class="fa fa-print"></i>Print</a></p>
                                        </div>
                                    </div>
                                    <div class="row">
                                        <div class="col-xs-12">
                                            <div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true">
                                                <div class="panel panel-default">
                                                    <div class="panel-heading completed" id="headingPremise" role="tab">
                                                        <h4 class="panel-title"><a role="button" data-toggle="collapse" href="#collapsePremise" aria-expanded="true" aria-controls="collapsePremise">Premises</a></h4>
                                                    </div>
                                                    <div class="panel-collapse collapse in" id="collapsePremise" role="tabpanel" aria-labelledby="headingPremise">
                                                        <div class="panel-body">
                                                            <p class="text-right"><a href="application-premises.html"><i class="fa fa-pencil-square-o"></i>Edit</a></p>
                                                            <div class="panel-main-content">
                                                                <div class="preview-info">
                                                                    <p><b>Premises</b></p>
                                                                    <p>On-site: 16 Raffles Quay #01-03 Hong Leong Building, 048581</p>
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="panel panel-default">
                                                    <div class="panel-heading completed" id="headingOne" role="tab">
                                                        <h4 class="panel-title"><a role="button" data-toggle="collapse" href="#collapseOne" aria-expanded="true" aria-controls="collapseOne">Primary Documents</a></h4>
                                                    </div>
                                                    <div class="panel-collapse collapse in" id="collapseOne" role="tabpanel" aria-labelledby="headingOne">
                                                        <div class="panel-body">
                                                            <p class="text-right mb-0"><a href="application-document.html"><i class="fa fa-pencil-square-o"></i>Edit</a></p>
                                                            <iframe class="elemClass-1561088919456" src="" id="elemId-1561088919456" scrollbar="auto" style="height: 180px;" width="100%" height="100%" frameborder="0"></iframe>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="panel panel-default">
                                                    <div class="panel-heading incompleted" id="headingServiceInfo" role="tab">
                                                        <h4 class="panel-title"><a role="button" data-toggle="collapse" href="#collapseServiceInfo" aria-expanded="true" aria-controls="collapseServiceInfo">Service Related Information</a></h4>
                                                    </div>
                                                    <div class="panel-collapse collapse in" id="collapseServiceInfo" role="tabpanel" aria-labelledby="headingServiceInfo">
                                                        <div class="panel-body">
                                                            <p class="text-right mb-0"><a href="application-service-related-clinical-lab-lab-discipline.html"><i class="fa fa-pencil-square-o"></i>Edit</a></p>
                                                            <iframe class="elemClass-1561088919456" src="" id="elemId-1561088919456" scrollbar="auto" style="height: 955px;" width="100%" height="100%" frameborder="0"></iframe>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="form-check">
                                                <input class="form-check-input" id="verifyInfoCheckbox" type="checkbox" name="verifyInfoCheckbox" aria-invalid="false">
                                                <label class="form-check-label" for="verifyInfoCheckbox"><span class="check-square"></span>Herby I certify that the information I provided is all correct and accurate</label>
                                            </div>
                                            <div class="form-check">
                                                <input class="form-check-input" id="declarationCheckbox" type="checkbox" name="declarationCheckbox" aria-invalid="false">
                                                <label class="form-check-label" for="declarationCheckbox"><span class="check-square"></span>&lt;Sample Declaration&gt;</label>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div class="application-tab-footer">
                                    <div class="row">
                                        <div class="col-xs-12 col-sm-6">
                                            <p><a class="back" href="application-service-related-clinical-lab-lab-discipline.html"><i class="fa fa-angle-left"></i> Back</a></p>
                                        </div>
                                        <div class="col-xs-12 col-sm-6">
                                            <div class="button-group"><a class="btn btn-secondary" href="#">Save as Draft</a><a class="next btn btn-primary disabled" href="#">SUBMIT & PAY </a></div>
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
