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
                <a class="collapsed" data-toggle="collapse" href="#previewBatInfo">Biological Agents&amp;Toxins</a>
            </h4>
        </div>
        <div id="previewBatInfo" class="panel-collapse collapse">
            <div class="panel-body">
                <c:forEach var="info" items="${dataSubInfo.submissionBats}" varStatus="status">
                    <div class="panel-main-content form-horizontal min-row">
                        <div class="form-group">
                            <div class="col-10">
                                <strong>Agents/Toxin ${status.index+1}</strong>
                            </div>
                            <div class="clear"></div>
                        </div>
                        <div>
                            <div class="form-group">
                                <label class="col-xs-5 col-md-4 control-label">Schedule Type</label>
                                <div class="col-sm-7 col-md-5 col-xs-7">
                                    <p><iais:code code="${info.schedule}"/></p>
                                </div>
                                <div class="clear"></div>
                            </div>
                            <div class="form-group">
                                <label class="col-xs-5 col-md-4 control-label">Biological Agent/Toxin</label>
                                <div class="col-sm-7 col-md-5 col-xs-7">
                                    <p>${info.biologicalName}</p>
                                </div>
                                <div class="clear"></div>
                            </div>
                            <c:if test="${info.schedule eq 'SCHTYPE006'}">
                                <div class="form-group">
                                    <label class="col-xs-5 col-md-4 control-label">Quantity Disposed</label>
                                    <div class="col-sm-7 col-md-5 col-xs-7">
                                        <p>${info.actualQty}</p>
                                    </div>
                                    <div class="clear"></div>
                                </div>
                                <div class="form-group">
                                    <label class="col-xs-5 col-md-4 control-label">Unit of Measurement
                                        Transferred</label>
                                    <div class="col-sm-7 col-md-5 col-xs-7">
                                        <p><iais:code code="${info.measurementUnit}"/></p>
                                    </div>
                                    <div class="clear"></div>
                                </div>
                            </c:if>
                            <div class="form-group">
                                <label class="col-xs-5 col-md-4 control-label">Complete Destruction/Disposal</label>
                                <div class="col-sm-7 col-md-5 col-xs-7">
                                    <p>Yes</p>
                                </div>
                                <div class="clear"></div>
                            </div>
                            <div class="form-group">
                                <label class="col-xs-5 col-md-4 control-label">Method of Destruction</label>
                                <div class="col-sm-7 col-md-5 col-xs-7">
                                    <p>${info.destructionMethod}</p>
                                </div>
                                <div class="clear"></div>
                            </div>
                            <div class="form-group">
                                <label class="col-xs-5 col-md-4 control-label">Details and/or procedures of
                                    destruction</label>
                                <div class="col-sm-7 col-md-5 col-xs-7">
                                    <p>${info.destructionProceduresDetails}</p>
                                </div>
                                <div class="clear"></div>
                            </div>
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
                            <label class="col-xs-5 col-md-4 control-label">Facility Name</label>
                            <div class="col-sm-7 col-md-5 col-xs-7">
                                <p>${dataSubInfo.facilityName}</p>
                            </div>
                            <div class="clear"></div>
                        </div>
                        <div class="form-group">
                            <label class="col-xs-5 col-md-4 control-label">Remarks</label>
                            <div class="col-sm-7 col-md-5 col-xs-7">
                                <p>${dataSubInfo.remarks}</p>
                            </div>
                            <div class="clear"></div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <%@include file="submissionDoc.jsp" %>
    <div class="col-xs-12">
        <a class="back" href="/bsb-fe/eservice/INTERNET/DataSubInbox"><em class="fa fa-angle-left"></em> Back</a>
    </div>
</div>
