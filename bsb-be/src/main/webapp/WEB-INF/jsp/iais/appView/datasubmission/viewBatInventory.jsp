<%--@elvariable id="dataSubInfo" type="sg.gov.moh.iais.egp.bsb.dto.submission.DataSubmissionInfo"--%>
<div class="panel-group" role="tablist" aria-multiselectable="true">
    <div class="panel panel-default">
        <div class="panel-heading completed">
            <h4 class="panel-title">
                <a class="collapsed" data-toggle="collapse" href="#previewFacInfo">Facility Information</a>
            </h4>
        </div>
        <div id="previewFacInfo" class="panel-collapse collapse">
            <div class="panel-body">
                <div class="text-right app-font-size-16"><a href="#"><em class="fa fa-pencil-square-o"></em>Edit</a>
                </div>
                <div class="panel-main-content form-horizontal min-row">
                    <div class="form-group">
                        <div class="col-10"><strong>Facility Profile</strong></div>
                        <div class="clear"></div>
                    </div>
                    <div>
                        <div class="form-group">
                            <label class="col-xs-5 col-md-4 control-label">Facility Name</label>
                            <div class="col-sm-7 col-md-5 col-xs-7">
                                <p>${dataSubInfo.facilityName}</p>
                            </div>
                            <div class="clear"></div>
                        </div>
                        <div class="form-group">
                            <label class="col-xs-5 col-md-4 control-label">Facility Address</label>
                            <div class="col-sm-7 col-md-5 col-xs-7">
                                <p>${dataSubInfo.facilityAddress}</p>
                            </div>
                            <div class="clear"></div>
                        </div>
                        <div class="form-group">
                            <label class="col-xs-5 col-md-4 control-label">Is the facility a Protected Place?</label>
                            <div class="col-sm-7 col-md-5 col-xs-7">
                                <p>${dataSubInfo.facilityIsProtected}</p>
                            </div>
                            <div class="clear"></div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <%@include file="submissionDoc.jsp" %>
</div>
