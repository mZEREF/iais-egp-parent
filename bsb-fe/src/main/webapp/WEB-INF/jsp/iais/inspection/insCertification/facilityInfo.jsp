<%--@elvariable id="reviewAFCReportDto" type="sg.gov.moh.iais.egp.bsb.dto.inspection.afc.ReviewAFCReportDto"--%>
<div class="row">
    <div class="col-xs-12">
        <div class="form-group">
            <div class="col-xs-4 control-label">
                <label style="font-size: large"><strong>Application Number</strong></label>
            </div>
            <div class="col-xs-6">
                <label><c:out value="${reviewAFCReportDto.appNo}"/></label>
            </div>
        </div>
        <br>
        <div class="form-group">
            <div class="col-xs-4 control-label">
                <label style="font-size: large"><strong>Facility Name</strong></label>
            </div>
            <div class="col-xs-6">
                <label><c:out value="${reviewAFCReportDto.facName}"/></label>
            </div>
        </div>
        <br>
        <div class="form-group">
            <div class="col-xs-4 control-label">
                <label style="font-size: large"><strong>Facility Address</strong></label>
            </div>
            <div class="col-xs-6">
                <label><c:out value="${reviewAFCReportDto.facAddress}"/></label>
            </div>
        </div>
        <br>
        <div class="form-group">
            <div class="col-xs-4 control-label">
                <label style="font-size: large"><strong>Inspection Date</strong></label>
            </div>
            <div class="col-xs-6">
                <label><fmt:formatDate value="${reviewAFCReportDto.insDate}" pattern="dd/MM/yyyy"/></label>
            </div>
        </div>
    </div>
</div>