<%--
  Created by IntelliJ IDEA.
  User: FAMS2
  Date: 12/21/2019
  Time: 2:08 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <title>Title</title>
</head>
<body>

</body>
</html><%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>

<%
  //handle to the Engine APIs
  sop.webflow.rt.api.BaseProcessClass process =
          (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");

%>
<webui:setLayout name="iais-internet"/>
<%@ include file="../dashboard.jsp" %>
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
                          <div class="clinical-governance-officer">
                            <!--start -->
                            <%@include file="nuclearMedicineImaging.jsp" %>
                            <!--end -->
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
  <input type="hidden" value="goveOffice" name="pageCon">
  <%@ include file="/include/validation.jsp" %>
</form>

<script type="text/javascript">
    $(document).ready(function() {
        //Binding method
        $('#governanceOfficersBack').click(function(){
            submitForms('laboratoryDisciplines',null,null,'clinical');
        });
        $('#governanceOfficersSaveDraft').click(function(){
            submitForms('governanceOfficers','saveDraft',null,'clinical');
        });
        $('#governanceOfficersNext').click(function(){
            /*   doValidation();
           if (getErrorMsg()) {
               dismissWaiting();
           } else {*/
            var controlFormLi = $('#controlFormLi').val();
            /*submitForms('disciplineAllocation',null,null,controlFormLi);*/
            submitForms('nuclearMedicineImaging',null,null,controlFormLi);
            /*  }*/
        });

    });

</script>





