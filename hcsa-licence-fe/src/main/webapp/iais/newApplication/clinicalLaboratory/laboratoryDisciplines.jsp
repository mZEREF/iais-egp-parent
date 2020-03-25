<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>

<%
    //handle to the Engine APIs
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");


%>
<webui:setLayout name="iais-internet"/>
<%@ include file="../dashboard.jsp" %>
<style>
    .app-font-size-22{
        font-size: 22px;
    }
</style>
<form method="post" id="mainForm" class="__egovform" action=<%=process.runtime.continueURL()%>>
    <%@ include file="/include/formHidden.jsp" %>

    <input type="hidden" name="paramController" id="paramController" value="com.ecquaria.cloud.moh.iais.action.ClinicalLaboratoryDelegator"/>
    <input type="hidden" name="valEntity" id="valEntity" value="com.ecquaria.cloud.moh.iais.dto.ClinicalOfficerValidateDto"/>
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
                                                    <div class="laboratory-disciplines">
                                                        <p><strong class="app-font-size-22">Laboratory Disciplines</strong></p>
                                                        <p>Please select the service disciplines you would like to apply at your premises.</p>
                                                        <hr id="disciplineHr"/>
                                                        <%@include file="CR_Discipline.jsp"%>
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
    <%@ include file="/include/validation.jsp" %>
    <input type="hidden" name="pageCon" value="checkBox">
</form>

<script type="text/javascript">
    $(document).ready(function() {
        //Binding method
        $('#laboratoryDisciplinesBack').click(function(){
            submit('documents',null,null);
        });
        $('#laboratoryDisciplinesSaveDraft').click(function(){
            submitForms('laboratoryDisciplines','saveDraft',null,'clinical');
        });
        $('#laboratoryDisciplinesNext').click(function(){
          /*  var controlFormLi = $('#controlFormLi').val();
           var aBoolean= $("input[name='control--runtime--1']:checked").length>0;
           if(!aBoolean){
                $('.laboratory-disciplines>span').removeAttr('style');
               return;
           }else {
               $('.laboratory-disciplines>span').attr('style','display: none');
           }
                              */
            var controlFormLi = $('#controlFormLi').val();
            submitForms('governanceOfficers',null,'next',controlFormLi);
        });

        $('#disciplineHr').css('margin-bottom','5px');

    });

</script>




