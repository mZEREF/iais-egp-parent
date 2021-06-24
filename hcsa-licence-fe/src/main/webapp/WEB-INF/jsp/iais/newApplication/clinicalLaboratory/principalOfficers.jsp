<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>

<%
    //handle to the Engine APIs
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");

%>
<webui:setLayout name="iais-internet"/>
<%@ include file="../dashboard.jsp" %>
<form method="post" id="mainForm" class="__egovform" action=<%=process.runtime.continueURL()%>>
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
                                                <%@ include file="step.jsp" %>
                                                <div class="application-service-steps">
                                                    <div class="clinical-governance-officer">
                                                        <%@include file="CR_Po.jsp"%>

                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <%@ include file="next.jsp" %>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <%@ include file="/WEB-INF/jsp/include/validation.jsp" %>
    <input type="hidden" name="pageCon" value="prinOffice" >
</form>

<script type="text/javascript">
    $(document).ready(function() {
        //Binding method
        /*if($('#cgoSelect').val()!=""){

            $('.principalOfficers').removeClass('hidden');
            $('#collapsePrincipal').attr("class","in");
        }else {
          /!*  $('.principalOfficers').attr("class",' hidden');*!/
            $('#collapsePrincipal').removeClass(" in");
        }*/
        $('#principalOfficersBack').click(function(){
            var controlFormLi = $('#controlFormLi').val();
            submitForms('disciplineAllocation',null,null,controlFormLi);
        });
        $('#principalOfficersSaveDraft').click(function(){
            submitForms('principalOfficers','saveDraft',null,'clinical');

        });
        $('#principalOfficersNext').click(function(){
            var controlFormLi = $('#controlFormLi').val();
            submitForms('documents',null,'next',controlFormLi);
        });

    });
</script>




