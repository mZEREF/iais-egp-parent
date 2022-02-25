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
                <c:forEach var="tsNotList" items="${dataSubInfo.submissionBats}" varStatus="status">
                    <div class="panel-main-content form-horizontal min-row">
                        <div class="form-group">
                            <div class="col-10">
                                <strong>Agents/Toxin ${status.index+1}</strong>
                            </div>
                            <div class="clear"></div>
                        </div>
                        <div>
                            <div class="form-group">
                                <label class="col-xs-5 col-md-4 control-label">Transfer Code</label>
                                <div class="col-sm-7 col-md-5 col-xs-7">
                                    <p></p>
                                </div>
                                <div class="clear"></div>
                            </div>
                            <div class="form-group">
                                <label class="col-xs-5 col-md-4 control-label">Schedule Type</label>
                                <div class="col-sm-7 col-md-5 col-xs-7">
                                    <p>${tsNotList.schedule}</p></div>
                                <div class="clear"></div>
                            </div>
                            <div class="form-group">
                                <label class="col-xs-5 col-md-4 control-label">Biological Agent/Toxin</label>
                                <div class="col-sm-7 col-md-5 col-xs-7">
                                    <p>${tsNotList.biologicalName}</p>
                                </div>
                                <div class="clear"></div>
                            </div>
                            <c:choose>
                                <c:when test="${tsNotList.schedule eq 'SCHTYPE006'}">
                                    <div class="form-group">
                                        <label class="col-xs-5 col-md-4 control-label">Quantity to Transfer</label>
                                        <div class="col-sm-7 col-md-5 col-xs-7">
                                            <p>${tsNotList.actualQty}</p>
                                        </div>
                                        <div class="clear"></div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-xs-5 col-md-4 control-label">Unit of Measurement</label>
                                        <div class="col-sm-7 col-md-5 col-xs-7">
                                            <p>${tsNotList.measurementUnit}</p>
                                        </div>
                                        <div class="clear"></div>
                                    </div>
                                </c:when>
                                <c:otherwise>
                                    <div class="form-group">
                                        <label class="col-xs-5 col-md-4 control-label">Type of Transfer</label>
                                        <div class="col-sm-7 col-md-5 col-xs-7">
                                            <p>${tsNotList.handleType}</p>
                                        </div>
                                        <div class="clear"></div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-xs-5 col-md-4 control-label">Quantity of Biological
                                            Agent</label>
                                        <div class="col-sm-7 col-md-5 col-xs-7">
                                            <p>${tsNotList.actualQty}</p>
                                        </div>
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
                            <label class="col-xs-5 col-md-4 control-label">Receiving Facility</label>
                            <div class="col-sm-7 col-md-5 col-xs-7">
                                <p>${dataSubInfo.facilityReceiving}</p>
                            </div>
                            <div class="clear"></div>
                        </div>
                        <div class="form-group">
                            <label class="col-xs-5 col-md-4 control-label">Date of Expected Transfer</label>
                            <div class="col-sm-7 col-md-5 col-xs-7">
                                <p>${dataSubInfo.expectedTransferDate}</p>
                            </div>
                            <div class="clear"></div>
                        </div>
                        <div class="form-group">
                            <label class="col-xs-5 col-md-4 control-label">Expected Arrival Time at Receiving
                                Facility</label>
                            <div class="col-sm-7 col-md-5 col-xs-7">
                                <p>${dataSubInfo.receivingFacilityExpectedArrivalTime}</p>
                            </div>
                            <div class="clear"></div>
                        </div>
                        <div class="form-group">
                            <label class="col-xs-5 col-md-4 control-label">Name of Courier Service Provider</label>
                            <div class="col-sm-7 col-md-5 col-xs-7">
                                <p>${dataSubInfo.providerName}</p></div>
                            <div class="clear"></div>
                        </div>
                        <div class="form-group">
                            <label class="col-xs-5 col-md-4 control-label">Remarks</label>
                            <div class="col-sm-7 col-md-5 col-xs-7">
                                <p>${dataSubInfo.remarks}</p></div>
                            <div class="clear"></div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <%@include file="submissionDoc.jsp" %>
</div>