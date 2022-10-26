<div class="row form-horizontal">
    <div class="col-lg-12 col-xs-12 cesform-box">
        <div class="row">
            <div class="col-lg-12 col-xs-12">
                <div class="table-gp tablebox">
                    <div class="form-group ">
                        <label class="col-sm-5 control-label">Facility Name</label>
                        <div class="col-sm-6 col-md-7"><c:out value="${cancellationApprovalDto.facilityName}"/></div>
                    </div>
                    <div class="form-group ">
                        <label class="col-sm-5 control-label">Facility Address</label>
                        <div class="col-sm-6 col-md-7"><c:out value="${cancellationApprovalDto.facilityAddress}"/></div>
                    </div>
                    <div class="form-group ">
                        <label class="col-sm-5 control-label">Approval Type</label>
                        <div class="col-sm-6 col-md-7"><iais:code code="${cancellationApprovalDto.approvalType}"/></div>
                    </div>
                    <div class="form-group ">
                        <label class="col-sm-5 control-label">Biological Agents/Toxins</label>
                        <div class="col-sm-6 col-md-7"><c:out value="${cancellationApprovalDto.biologicalAgentToxin}"/></div>
                    </div>
                    <div class="form-group ">
                        <label class="col-sm-5 control-label">Note</label>
                        <div class="col-sm-6 col-md-7"></div>
                    </div>
                    <div class="form-group ">
                        <label class="col-sm-5 control-label">Physical Possession of BA/T in Facility</label>
                        <div class="col-sm-6 col-md-7"><c:out value="${cancellationApprovalDto.physicalPossession}"/></div>
                    </div>
                    <div class="form-group ">
                        <label class="col-sm-5 control-label">Reasons</label>
                        <div class="col-sm-6 col-md-7"><iais:code code="${cancellationApprovalDto.reasons}"/></div>
                    </div>
                    <div class="form-group ">
                        <label class="col-sm-5 control-label">Remarks</label>
                        <div class="col-sm-6 col-md-7"><c:out value="${cancellationApprovalDto.remarks}"/></div>
                    </div>
                    <%@include file="previewDocuments.jsp" %>
                </div>
            </div>
        </div>
    </div>
</div>