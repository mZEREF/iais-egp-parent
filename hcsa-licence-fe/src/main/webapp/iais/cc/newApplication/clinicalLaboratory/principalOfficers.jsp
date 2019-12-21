<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>

<%
    //handle to the Engine APIs
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");

%>
<webui:setLayout name="iais-internet"/>
<%@ include file="../dashboard.jsp" %>
<form method="post" id="mainForm" class="__egovform" action=<%=process.runtime.continueURL()%>>
    <%@ include file="/include/formHidden.jsp" %>
    <input type="hidden" name="paramController" id="paramController" value="com.ecquaria.cloud.moh.iais.action.NewApplicationDelegator"/>
    <input type="hidden" name="valEntity" id="valEntity" value="com.ecquaria.cloud.moh.iais.dto.ApplicationValidateDto"/>
    <input type="hidden" name="valProfiles" id="valProfiles" value=""/>

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
                                                        <%@include file="CR_Po.jsp"%>

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
                                            <div class="button-group"><a class="btn btn-secondary" id = "principalOfficersSaveDraft">Save as Draft</a><a class="next btn btn-primary" id="principalOfficersNext" >Next</a></div>
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
    <%@ include file="/include/validation.jsp" %>
    <input type="hidden" name="pageCon" value="prinOffice" >
</form>

<script type="text/javascript">
    $(document).ready(function() {
        //Binding method
        if($('#cgoSelect').val()!=""){

            $('.principalOfficers').removeClass('hidden');
            $('#collapsePrincipal').attr("class","in");
        }else {
          /*  $('.principalOfficers').attr("class",' hidden');*/
            $('#collapsePrincipal').removeClass(" in");
        }
        $('#principalOfficersBack').click(function(){
            var controlFormLi = $('#controlFormLi').val();
            submitForms('disciplineAllocation',null,null,controlFormLi);
        });
        $('#principalOfficersSaveDraft').click(function(){
            submitForms('governanceOfficers','saveDraft',null,'clinical');
        });
        $('#principalOfficersNext').click(function(){
            var controlFormLi = $('#controlFormLi').val();
            submitForms('documents',null,null,controlFormLi);
        });

    });
</script>




