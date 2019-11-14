<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>

<%
    //handle to the Engine APIs
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");

%>
<webui:setLayout name="iais-internet"/>
<%@ include file="../dashboard.jsp" %>
<form method="post" id="mainForm" class="__egovform" action=<%=process.runtime.continueURL()%>>
    <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
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
                                                <h2 class="service-title">SERVICE 1 OF 2: <strong>CLINICAL LABORATORY</strong></h2>
                                                <div class="visible-xs visible-sm servive-subtitle">
                                                    <p>Step 2 of 5</p>
                                                    <h3>Clinical Governance Officers</h3>
                                                </div>
                                                <ul class="progress-tracker">
                                                    <li class="tracker-item completed" data-service-step="laboratory-disciplines">Laboratory Disciplines</li>
                                                    <li class="tracker-item completed" data-service-step="clinical-governance-officer">Clinical Governance Officers</li>
                                                    <li class="tracker-item completed" data-service-step="discipline-allocation">Discipline Allocation</li>
                                                    <li class="tracker-item active" data-service-step="principal-officers">Principal Officers</li>
                                                    <li class="tracker-item disabled">Documents</li>
                                                </ul>
                                                <div class="application-service-steps">
                                                    <div class="clinical-governance-officer">
                                                        <jsp:include page="/iais/cc/newApplication/clinicalLaboratory/CR_Po.jsp" />
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div class="application-tab-footer">
                                    <div class="row">
                                        <div class="col-xs-12 col-sm-6"><a class="back" id="principalOfficersBack"><em class="fa fa-angle-left"></em> Back</a></div>
                                        <div class="col-xs-12 col-sm-6">
                                            <div class="button-group"><a class="btn btn-secondary" id = "principalOfficersSaveDraft">Save as Draft</a><a class="next btn btn-primary" id="principalOfficersNext" data-goto="clinical-governance-officer">Next</a></div>
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
        //Binding method
        $('#principalOfficersBack').click(function(){
            //submitForms('laboratoryDisciplines',null,null,'clinical');
        });
        $('#principalOfficersSaveDraft').click(function(){
            // submitForms('governanceOfficers','saveDraft',null,'clinical');
        });
        $('#principalOfficersNext').click(function(){
            var controlFormLi = $('#controlFormLi').val();
            submitForms('documents',null,null,controlFormLi);
        });

    });

</script>




