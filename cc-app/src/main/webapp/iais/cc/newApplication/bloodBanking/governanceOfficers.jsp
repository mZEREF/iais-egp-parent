<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>

<%
    //handle to the Engine APIs
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");

%>
<webui:setLayout name="iais-cc"/>
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
                                                <h2 class="service-title">SERVICE 1 OF 2: <b>Blood Banking</b></h2>
                                                <div class="visible-xs visible-sm servive-subtitle">
                                                    <p>Step 2 of 5</p>
                                                    <h3>Clinical Governance Officers</h3>
                                                </div>
                                                <ul class="progress-tracker">
                                                    <li class="tracker-item completed" data-service-step="laboratory-disciplines">Laboratory Disciplines</li>
                                                    <li class="tracker-item active" data-service-step="clinical-governance-officer">Clinical Governance Officers</li>
                                                    <li class="tracker-item disabled" data-service-step="discipline-allocation">Discipline Allocation</li>
                                                    <li class="tracker-item disabled" data-service-step="principal-officers">Principal Officers</li>
                                                    <li class="tracker-item disabled">Documents</li>
                                                </ul>
                                                <div class="application-service-steps">
                                                    <div class="clinical-governance-officer">
                                                        <iframe id="__egovform-iframe" name="__egovform-iframe" frameborder="no" width="100%" heihgt="100%" scrolling="no" src="./CR_Form2.jsp"></iframe>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div class="application-tab-footer">
                                    <div class="row">
                                        <div class="col-xs-12 col-sm-6"><a class="back" id="governanceOfficersBack"><i class="fa fa-angle-left"></i> Back</a></div>
                                        <div class="col-xs-12 col-sm-6">
                                            <div class="button-group"><a class="btn btn-secondary" id = "governanceOfficersSaveDraft">Save as Draft</a><a class="next btn btn-primary disabled hidden" id = "governanceOfficersNext"><i class="fa fa-angle-left"></i>) Next</a><a class="next btn btn-primary disabled" data-goto="clinical-governance-officer">Next</a></div>
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
        $('#governanceOfficersBack').click(function(){
            submitForms('laboratoryDisciplines',null,null,'blood');
        });
        $('#governanceOfficersSaveDraft').click(function(){
            submitForms('governanceOfficers','saveDraft',null,'blood');
        });
        $('#governanceOfficersNext').click(function(){
            submitForms('isciplineAllocation',null,null,'blood');
        });

    });

</script>




