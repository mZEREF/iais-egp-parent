<div class="panel-body">
    <h4>I, hereby declare the following:</h4>
    <div>
        <div class="col-xs-12 form-group">
            <span>The Facility Operator and the Facility Operator Designee are informed of their responsibilities as Facility Operator under the Biological Agents and Toxins Act.</span>
        </div>
        <div class="form-check col-xs-3">
            <input class="form-check-input" type="radio" name="facOpDeInformedResponsibilities" id="facOpDeInformedResponsibilitiesYes" value="Y" <c:if test="${otherInfo.facOpDeInformedResponsibilities eq 'Y'}">checked="checked"</c:if>>
            <label for="facOpDeInformedResponsibilitiesYes" class="form-check-label"><span class="check-circle"></span>Yes</label>
        </div>
        <div class="form-check col-xs-3">
            <input class="form-check-input" type="radio" name="facOpDeInformedResponsibilities" id="facOpDeInformedResponsibilitiesNo" value="N" <c:if test="${otherInfo.facOpDeInformedResponsibilities eq 'N'}">checked="checked"</c:if>>
            <label for="facOpDeInformedResponsibilitiesNo" class="form-check-label"><span class="check-circle"></span>No</label>
        </div>
        <span data-err-ind="facOpDeInformedResponsibilities" class="error-msg col-xs-12 form-group"></span>
    </div>
    <div>
        <div class="col-xs-12 form-group">
            <span>The Biosafety Committee is informed of its responsibilities stipulated under the Biological Agents and Toxins Act.</span>
        </div>
        <div class="form-check col-xs-3">
            <input class="form-check-input" type="radio" name="facCommitteeInformedResponsibilities" id="facCommitteeInformedResponsibilitiesYes" value="Y" <c:if test="${otherInfo.facCommitteeInformedResponsibilities eq 'Y'}">checked="checked"</c:if>>
            <label for="facCommitteeInformedResponsibilitiesYes" class="form-check-label"><span class="check-circle"></span>Yes</label>
        </div>
        <div class="form-check col-xs-3">
            <input class="form-check-input" type="radio" name="facCommitteeInformedResponsibilities" id="facCommitteeInformedResponsibilitiesNo" value="N" <c:if test="${otherInfo.facCommitteeInformedResponsibilities eq 'N'}">checked="checked"</c:if>>
            <label for="facCommitteeInformedResponsibilitiesNo" class="form-check-label"><span class="check-circle"></span>No</label>
        </div>
        <span data-err-ind="facCommitteeInformedResponsibilities" class="error-msg col-xs-12 form-group"></span>
    </div>
    <div>
        <div class="col-xs-12 form-group">
            <span>The facility will develop/has a biorisk management system in place for managing biosafety, biosecurity and dual-use risks, that minimally encompasses the following:</span>
            <br/><span>- Institutional governance and oversight</span>
            <br/><span>- Risk assessment and risk management programme</span>
            <br/><span>- Hazardous waste management for solid and liquid waste</span>
            <br/><span>- Emergency response plan, preparedness and readiness</span>
            <br/><span>- Material management and accountability (including storage, inventory management and inventory audits)</span>
            <br/><span>- Incident investigation and reporting</span>
            <br/><span>- Occupational health and medical surveillance programme</span>
            <br/><span>- Personal protective equipment</span>
            <br/><span>- Personnel management including recruitment, authorization of access to the facility and materials, training (including refresher training) and competency assessments</span>
            <br/><span>- Policy on sharps use, if applicable</span>
            <br/><span>- Policy and procedures for transfer and/or transportation of biological agent/toxin</span>
            <br/><span>- Compliance to applicable legislations and regulations (please indicate where applicable)</span>
        </div>
        <div class="form-check col-xs-3">
            <input class="form-check-input" type="radio" name="bioRiskManagementDeclare" id="bioRiskManagementDeclareYes" value="Y" <c:if test="${otherInfo.bioRiskManagementDeclare eq 'Y'}">checked="checked"</c:if>>
            <label for="bioRiskManagementDeclareYes" class="form-check-label"><span class="check-circle"></span>Yes</label>
        </div>
        <div class="form-check col-xs-3">
            <input class="form-check-input" type="radio" name="bioRiskManagementDeclare" id="bioRiskManagementDeclareNo" value="N" <c:if test="${otherInfo.bioRiskManagementDeclare eq 'N'}">checked="checked"</c:if>>
            <label for="bioRiskManagementDeclareNo" class="form-check-label"><span class="check-circle"></span>No</label>
        </div>
        <span data-err-ind="bioRiskManagementDeclare" class="error-msg col-xs-12 form-group"></span>
    </div>
    <div>
        <div class="col-xs-12 form-group">
            <span>All information provided by me in this application is true and accurate.</span>
        </div>
        <div class="form-check col-xs-3">
            <input class="form-check-input" type="radio" name="infoAuthenticatedDeclare" id="infoAuthenticatedDeclareYes" value="Y" <c:if test="${otherInfo.infoAuthenticatedDeclare eq 'Y'}">checked="checked"</c:if>>
            <label for="infoAuthenticatedDeclareYes" class="form-check-label"><span class="check-circle"></span>Yes</label>
        </div>
        <div class="form-check col-xs-3">
            <input class="form-check-input" type="radio" name="infoAuthenticatedDeclare" id="infoAuthenticatedDeclareNo" value="N" <c:if test="${otherInfo.infoAuthenticatedDeclare eq 'N'}">checked="checked"</c:if>>
            <label for="infoAuthenticatedDeclareNo" class="form-check-label"><span class="check-circle"></span>No</label>
        </div>
        <span data-err-ind="infoAuthenticatedDeclare" class="error-msg col-xs-12 form-group"></span>
    </div>
    <div>
        <div class="col-xs-12 form-group">
            <p>The following is a non-exhaustive list of supporting documents that the facility is required to provide for the application. Some of these may not be available at point of application submission but must be provided subsequently, when available. Please note that incomplete submissions may result in delays to processing or rejection of the application.</p>
            <span>Supporting Documents</span>
            <br/><span>1. Facility Administrative Oversight Plan.
                                                        <a class="btn-tooltip styleguide-tooltip" href="javascript:void(0);" data-toggle="tooltip" data-html="true" title="" data-original-title="<p>Information regarding the Facility Administrative Oversight Plan can be found on the <a href='https://www.moh.gov.sg/biosafety' style='text-decoration: none'>MOH Biosafety website</a></p>">i</a>
                                                        </span>
            <br/><span>2. Documentation of successful facility certification.</span>
            <br/><span>3. Documentation of successful completion of the required biosafety training for the Biosafety Coordinator.</span>
            <br/><span>4. Documentation of approval from relevant ministry or statutory board, where applicable.</span>
            <br/><span>5. Gazette Order (if the facility is a Protected Place under the Infrastructure Protection Act).</span>
        </div>
    </div>
</div>