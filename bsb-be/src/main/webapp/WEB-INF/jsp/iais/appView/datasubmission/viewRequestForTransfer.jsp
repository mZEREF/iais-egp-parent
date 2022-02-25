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

    <div class="panel panel-default">
        <div class="panel-heading completed">
            <h4 class="panel-title">
                <a class="collapsed" data-toggle="collapse" href="#previewBatInfo">Biological Agents &amp;Toxins</a>
            </h4>
        </div>
        <div id="previewBatInfo" class="panel-collapse collapse">
            <div class="panel-body">
                <c:forEach var="item" items="${dataSubInfo.submissionBats}" varStatus="status">
                    <div class="panel-main-content form-horizontal min-row">
                        <div class="form-group">
                            <div class="col-10"><strong>Agents/Toxin ${status.index+1}</strong></div>
                            <div class="clear"></div>
                        </div>
                        <div>
                            <div class="form-group">
                                <label class="col-xs-5 col-md-4 control-label">Schedule Type</label>
                                <div class="col-sm-7 col-md-5 col-xs-7">
                                    <iais:code code="${item.schedule}"/>
                                </div>
                                <div class="clear"></div>
                            </div>
                            <div class="form-group">
                                <label class="col-xs-5 col-md-4 control-label">Biological Agent/Toxin</label>
                                <div class="col-sm-7 col-md-5 col-xs-7"><p>${item.biologicalName}</p></div>
                                <div class="clear"></div>
                            </div>
                            <c:choose>
                                <c:when test="${item.schedule ne 'SCHTYPE006'}">
                                    <div class="form-group">
                                        <label class="col-xs-5 col-md-4 control-label">Expected Quantity of Biological
                                            Agent</label>
                                        <div class="col-sm-7 col-md-5 col-xs-7"><p>${item.expectedQty}</p></div>
                                        <div class="clear"></div>
                                    </div>
                                </c:when>
                                <c:otherwise>
                                    <div class="form-group">
                                        <label class="col-xs-5 col-md-4 control-label">Expected Quantity to
                                            Receive</label>
                                        <div class="col-sm-7 col-md-5 col-xs-7"><p>${item.expectedQty}</p></div>
                                        <div class="clear"></div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-xs-5 col-md-4 control-label">Unit of Measurement</label>
                                        <div class="col-sm-7 col-md-5 col-xs-7"><p>${item.measurementUnit}</p></div>
                                        <div class="clear"></div>
                                    </div>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                </c:forEach>
                <div class="panel-main-content form-horizontal min-row">
                    <div class="form-group">
                        <div class="col-10"><strong>Additional Details</strong></div>
                        <div class="clear"></div>
                    </div>
                    <div>
                        <div class="form-group">
                            <label class="col-xs-5 col-md-4 control-label">Transferring Facility</label>
                            <div class="col-sm-7 col-md-5 col-xs-7"><p>${dataSubInfo.facilityName}</p></div>
                            <div class="clear"></div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div class="col-xs-12">
        <a class="back" href="/bsb-fe/eservice/INTERNET/DataSubInbox"><em class="fa fa-angle-left"></em> Back</a>
    </div>
</div>