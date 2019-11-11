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
                        <h2 class="service-title">SERVICE 1 OF 2: <b>CLINICAL LABORATORY</b></h2>
                        <div class="visible-xs visible-sm servive-subtitle">
                          <p>Step 2 of 5</p>
                          <h3>Clinical Governance Officers</h3>
                        </div>
                        <ul class="progress-tracker">
                          <li class="tracker-item completed" data-service-step="laboratory-disciplines">Laboratory Disciplines</li>
                          <li class="tracker-item completed" data-service-step="clinical-governance-officer">Clinical Governance Officers</li>
                          <li class="tracker-item active" data-service-step="discipline-allocation">Discipline Allocation</li>
                          <li class="tracker-item disabled" data-service-step="principal-officers">Principal Officers</li>
                          <li class="tracker-item disabled">Documents</li>
                        </ul>
                        <div class="application-service-steps">
                          <div class="discipline-allocation">
                            <h2>Discipline Allocation</h2>
                            <p>Please ensure that each laboratory discipline is assigned to a clinical governance officer.</p>
                            <div class="table-gp">
                              <input type="hidden" name="premId" value="67bf3139-5222-4c02-9e38-7b1153b871f6"/>
                              <table class="table discipline-table">
                                <thead>
                                <tr>
                                  <th>Premises</th>
                                  <th>Laboratory Disciplines</th>
                                  <th>Clinical Governance Officers</th>
                                </tr>
                                </thead>
                                <tbody>
                                <tr>
                                  <td rowspan="4">
                                    <p class="visible-xs visible-sm table-row-title">Premises</p>
                                    <input type="hidden" name="premId" value="${PremisesHciName}" />
                                    <p>16 Raffles Quay # 01-03, 048581</p>
                                  </td>
                                  <td>
                                    <p class="visible-xs visible-sm table-row-title">Laboratory Disciplines</p>
                                    <input type="hidden" name="laboratoryDisciplines" value="085E2ADD-8500-EA11-BE77-000C29D29DB0"/>
                                    <p>Blood Banking</p>
                                  </td>
                                  <td>
                                    <p class="visible-xs visible-sm table-row-title">Clinical Governance Officers</p>
                                    <select name="cgoIdNo" class="table-select officer-allocation-select" id="officerAllocationSelect1" aria-labelledby="officerAllocationSelect1">
                                      <option value="S1358436B">Option 1</option>
                                      <option value="S9514254J">Option 2</option>
                                    </select>
                                  </td>
                                </tr>
                                <tr>
                                  <td>
                                    <p class="visible-xs visible-sm table-row-title">Laboratory Disciplines</p>
                                    <input type="hidden" name="laboratoryDisciplines" value="2278150C-FB00-EA11-BE77-000C29D29DB0"/>
                                    <p>Nuclear Medicine Service</p>
                                  </td>
                                  <td>
                                    <p class="visible-xs visible-sm table-row-title">Clinical Governance Officers</p>
                                    <select name="cgoIdNo" class="table-select officer-allocation-select" id="officerAllocationSelect2" aria-labelledby="officerAllocationSelect2">
                                      <option value="S1358436B">Option 1</option>
                                      <option value="S9514254J">Option 2</option>
                                    </select>
                                  </td>
                                </tr>
                                </tbody>
                              </table>
                              <p>Click <a href="#">here</a> to assign a laboratory discipline to multiple clinical governance officers.</p>
                            </div>
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
                      <div class="button-group"><a class="btn btn-secondary" id = "governanceOfficersSaveDraft">Save as Draft</a><a class="next btn btn-primary" data-goto="clinical-governance-officer" id="governanceOfficersNext">Next</a></div>
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

        $('#governanceOfficersNext').click(function(){
            submitForms('principalOfficers',null,null,'clinical');
        });

    });

</script>