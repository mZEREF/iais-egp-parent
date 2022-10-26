<div class="row form-horizontal">
    <div class="col-lg-12 col-xs-12 cesform-box">
        <div class="row">
            <div class="col-lg-12 col-xs-12">
                <div class="table-gp tablebox">
                    <div class="form-group ">
                        <label class="col-sm-5 control-label">Organization Name</label>
                        <div class="col-sm-6 col-md-7"><c:out value="${deRegistrationAFCDto.organisationName}"/></div>
                    </div>
                    <div class="form-group ">
                        <label class="col-sm-5 control-label">Organization Address</label>
                        <div class="col-sm-6 col-md-7"><c:out value="${deRegistrationAFCDto.organisationAddress}"/></div>
                    </div>
                    <div class="form-group ">
                        <label class="col-sm-5 control-label">Reasons</label>
                        <div class="col-sm-6 col-md-7"><iais:code code="${deRegistrationAFCDto.reasons}"/></div>
                    </div>
                    <div class="form-group ">
                        <label class="col-sm-5 control-label">Remarks</label>
                        <div class="col-sm-6 col-md-7"><c:out value="${deRegistrationAFCDto.remarks}"/></div>
                    </div>
                    <%@include file="previewDocuments.jsp" %>
                </div>
            </div>
        </div>
    </div>
</div>