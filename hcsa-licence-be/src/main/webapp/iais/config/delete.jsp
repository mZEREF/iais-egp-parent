<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib prefix="iasi" uri="ecquaria/sop/egov-mc" %>
<webui:setLayout name="iais-intranet"/>

<%
  sop.webflow.rt.api.BaseProcessClass process =
          (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>

<div class="main-content">
  <form id="mainForm" method="post" action=<%=process.runtime.continueURL()%>>
    <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
    <input type="hidden" name="crud_action_type" value="">
    <input type="hidden" name="crud_action_value" value="">
    <input type="hidden" name="crud_action_additional" value="">
    <div class="col-lg-12 col-xs-10">
      <div class="bg-title" style="text-align: center">
        <h2> HCSA Service</h2>
      </div>
      <div class="form-group">
        <div class="col-xs-12 col-md-10">
          <h2 class="component-title">Delete HCSA Service</h2>
        </div>
      </div>
      <div class="form-group">
        <div class="col-xs-12 col-md-8">
          <label class="col-xs-12 col-md-8 control-label" for="serviceName">Service Name<span class="mandatory" >*</span></label>
          <div class="col-xs-12 col-md-4">
            <input id="serviceName" type="text">
          </div>
        </div>
      </div>
      <div class="form-group">
        <div class="col-xs-12 col-md-8">
          <label class="col-xs-12 col-md-8 control-label" for="description">Service Description<span class="mandatory" >*</span></label>
          <div class="col-xs-12 col-md-4">
            <input id="description" type="text">
          </div>
        </div>
      </div>
      <div class="form-group">
        <div class="col-xs-12 col-md-8">
          <label class="col-xs-12 col-md-8 control-label" for="displayDescription">Service Display Description<span class="mandatory">*</span></label>
          <div class="col-xs-12 col-md-4">
            <input id="displayDescription" type="text">
          </div>
        </div>
      </div>

      <div class="form-group">
        <div class="col-xs-12 col-md-8">
          <label class="col-xs-12 col-md-8 control-label" for="serviceCode">Service Code<span class="mandatory">*</span></label>
          <div class="col-xs-12 col-md-4">
            <input id="serviceCode" type="text">
          </div>
        </div>
      </div>

      <div class="form-group">
        <div class="col-xs-12 col-md-8">
          <label class="col-xs-12 col-md-8 control-label" for="ServiceType">Service Type<span class="mandatory">*</span></label>
          <div class="col-xs-12 col-md-4">
            <input id="ServiceType" type="text">
          </div>
        </div>
      </div>

      <div class="form-group">
        <div class="col-xs-12 col-md-10">
          <label class="col-xs-12 col-md-10 control-label" >Premises Type<span class="mandatory">*</span></label>
        </div>
      </div>
      <div class="form-group" style="left: 10%">
        <div class="form-check-gp">
          <div class="row">
            <div class="col-xs-12 col-md-3">
              <div class="form-check " style="left: 10%">
                <input class="form-check-input" id="icon3checkboxSample" type="checkbox" name="Onsite" aria-invalid="false">
                <label class="form-check-label" for="icon3checkboxSample"><span class="check-square"></span>Onsite</label>
              </div>
            </div>
            <div class="col-xs-12 col-md-3">
              <div class="form-check ">
                <input class="form-check-input" id="icon4checkboxSample" type="checkbox" name="Offsite" aria-invalid="false">
                <label class="form-check-label" for="icon4checkboxSample"><span class="check-square"></span>Offsite</label>
              </div>
            </div>
            <div class="col-xs-12 col-md-3">
              <div class="form-check ">
                <input class="form-check-input" id="icon5checkboxSample" type="checkbox" name="Conveyance" aria-invalid="false">
                <label class="form-check-label" for="icon5checkboxSample"><span class="check-square"></span>Conveyance</label>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div class="form-group">
        <div class="col-xs-12 col-md-8">
          <label class="col-xs-12 col-md-8 control-label" for="principalOfficer">Principal Officer (PO)<span class="mandatory">*</span></label>
          <div class="col-xs-12 col-md-4">
            <input id="principalOfficer" type="text">
          </div>
        </div>
      </div>


      <div class="form-group">
        <div class="col-xs-12 col-md-8">
          <label class="col-xs-12 col-md-8 control-label" for="DeputyPrincipalOfficer">Deputy Principal Officer (DPO)<span class="mandatory">*</span></label>
          <div class="col-xs-12 col-md-4">
            <input id="DeputyPrincipalOfficer" type="text">
          </div>
        </div>
      </div>


      <div class="form-group">
        <div class="col-xs-12 col-md-8">
          <label class="col-xs-12 col-md-8 control-label" for="ClinicalGovernanceOfficer">Clinical Governance Officer (CGO)<span class="mandatory">*</span></label>
          <div class="col-xs-12 col-md-4">
            <input id="ClinicalGovernanceOfficer" type="text">
          </div>
        </div>
      </div>


      <div class="form-group">
        <div class="col-xs-12 col-md-8">
          <label class="col-xs-12 col-md-8 control-label" for="ServicePersonnel">Service Personnel<span class="mandatory">*</span></label>
          <div class="col-xs-12 col-md-4">
            <input id="ServicePersonnel" type="text">
          </div>
        </div>
      </div>
      <div class="form-group">
        <div class="col-xs-12 col-md-8">
          <label class="col-xs-12 col-md-6 control-label" for="NumberDocument">Number of Service-Related Document to be
            uploaded<span class="mandatory">*</span></label>
          <div class="col-xs-12 col-md-4">
            <input id="NumberDocument" type="text">
          </div>
          <div class="col-xs-12 col-md-2 form-check">   <input class="form-check-input"  type="checkbox" name="Conveyance" aria-invalid="false">
            <label class="form-check-label" ><span class="check-square"></span>Mandatory</label>
          </div>
        </div>
      </div>

      <div class="form-group">
        <div class="col-xs-12 col-md-8">
          <label class="col-xs-12 col-md-6 control-label" for="DescriptionDocument">Description of each Service-Related Document to
            be Uploaded<span class="mandatory">*</span></label>
          <div class="col-xs-12 col-md-4">
            <input id="DescriptionDocument" type="text">
          </div>
          <div class="col-xs-12 col-md-2 form-check">   <input class="form-check-input"  type="checkbox" name="Conveyance" aria-invalid="false">
            <label class="form-check-label"><span class="check-square"></span>Mandatory</label>
          </div>
        </div>
      </div>

      <div class="form-group">
        <div class="col-xs-12 col-md-8">
          <label class="col-xs-12 col-md-6 control-label" for="Numberfields">Number of Service-Related General Info fields to
            be captured<span class="mandatory">*</span></label>
          <div class="col-xs-12 col-md-4">
            <input id="Numberfields" type="text">
          </div>
          <div class="col-xs-12 col-md-2 form-check">   <input class="form-check-input"  type="checkbox" name="Conveyance" aria-invalid="false">
            <label class="form-check-label"><span class="check-square"></span>Mandatory</label>
          </div>
        </div>
      </div>

      <div class="form-group">
        <div class="col-xs-12 col-md-8">
          <label class="col-xs-12 col-md-6 control-label" for="DescriptionGeneral">Description of each Service-Related General Info
            field to be captured*<span class="mandatory">*</span></label>
          <div class="col-xs-12 col-md-4">
            <input id="DescriptionGeneral" type="text">
          </div>
          <div class="col-xs-12 col-md-2 form-check">   <input class="form-check-input"  type="checkbox" name="Conveyance" aria-invalid="false">
            <label class="form-check-label"><span class="check-square"></span>Mandatory</label>
          </div>
        </div>
      </div>

      <div class="form-group">
        <div class="col-xs-12 col-md-8">
          <label class="col-xs-12 col-md-8 control-label" >Service-Related Checklists<span class="mandatory">*</span></label>
          <div class="col-xs-10 col-md-4">
            <div class="components">
              <a class="btn btn-secondary "><span class="view">view</span></a>
            </div>
          </div>
        </div>
      </div>

      <div class="form-group">
        <div class="col-xs-12 col-md-8">
          <label class="col-xs-12 col-md-8 control-label" >Service Risk Score<span class="mandatory">*</span></label>
          <div class="col-xs-10 col-md-4">
            <div class="components">
              <a class="btn btn-secondary "><span class="view">view</span></a>
            </div>
          </div>
        </div>
      </div>

      <div class="form-group">
        <div class="col-xs-12 col-md-8">
          <label class="col-xs-12 col-md-8 control-label" >Service KPI<span class="mandatory">*</span></label>
          <div class="col-xs-10 col-md-4">
            <div class="components">
              <a class="btn btn-secondary "><span class="view">view</span></a>
            </div>
          </div>
        </div>
      </div>

      <div class="form-group">
        <div class="col-xs-12 col-md-8">
          <label class="col-xs-12 col-md-8 control-label" >Service Fees<span class="mandatory">*</span></label>
          <div class="col-xs-10 col-md-4">
            <div class="components">
              <a class="btn btn-secondary "><span class="view">view</span></a>
            </div>
          </div>
        </div>
      </div>

      <div class="form-group">
        <div class="col-xs-12 col-md-8">
          <label class="col-xs-12 col-md-8 control-label" >Service Fee Bundles<span class="mandatory">*</span></label>
          <div class="col-xs-10 col-md-4">
            <div class="components">
              <a class="btn btn-secondary "><span class="view">view</span></a>
            </div>
          </div>
        </div>
      </div>

      <div class="form-group">
        <div class="col-xs-12 col-md-8">
          <label class="col-xs-12 col-md-8 control-label" for="RoutingStages">Service Workflow Routing Stages<span class="mandatory">*</span></label>
          <div class="col-xs-12 col-md-4">
            <input id="RoutingStages" type="text">
          </div>
        </div>
      </div>

      <div class="form-group">
        <div class="col-xs-12 col-md-8">
          <label class="col-xs-12 col-md-8 control-label" for="RoutingScheme">Service Routing Scheme<span class="mandatory">*</span></label>
          <div class="col-xs-12 col-md-4">
            <input id="RoutingScheme" type="text">
          </div>
        </div>
      </div>

      <div class="form-group">
        <div class="col-xs-12 col-md-8">
          <label class="col-xs-12 col-md-8 control-label" for="WorkloadManhours">Service Workload Manhours<span class="mandatory">*</span></label>
          <div class="col-xs-12 col-md-4">
            <input id="WorkloadManhours" type="text">
          </div>
        </div>
      </div>

      <div class="form-group">
        <div class="col-xs-12 col-md-8">
          <label class="col-xs-12 col-md-8 control-label" for="Sub-Types">Service Sub-Types</label>
          <div class="col-xs-12 col-md-4">
            <input id="Sub-Types" type="text">
          </div>
        </div>
      </div>


      <div class="form-group">
        <div class="col-xs-12 col-md-8">
          <iais:row>
            <iais:field value="Choose date" width="12"/>
            <iais:value cssClass="col-xs-7 col-sm-4 col-md-3">
              <iais:datePicker cssClass="form_datetime"  name="conveyancePubHoliday0"/>
            </iais:value>
          </iais:row>
        </div>

      </div>



      <div class="col-xs-12 col-md-12" style="text-align: center">
        <div class="row">
          <div class="col-xs-10 col-md-8">
            <div class="components">
              <a class="btn  btn-secondary" onclick="confirmDelete()"><span class="mandatory">CONFIRM DELETE</span></a>
            </div>
          </div>
        </div>
      </div>
      <div class="col-xs-12 col-md-12" style="text-align: center">
        <div class="row">
          <div class="col-xs-10 col-md-8">
            <div class="components">
            <p style="text-align: center">Version 1</p>
            </div>
          </div>
        </div>
      </div>

    </div>
  </form>
</div>
<style>
  .mandatory{
    color: red;
  }
  .view{
    color: #2199E8;
  }
</style>
<script type="text/javascript">

    function confirmDelete() {
        SOP.Crud.cfxSubmit("mainForm","delete");
    }



</script>
</>
