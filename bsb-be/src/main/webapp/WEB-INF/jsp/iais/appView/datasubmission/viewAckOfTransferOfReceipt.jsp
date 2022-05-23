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
                <div class="text-right app-font-size-16"><a href="#"><em
                        class="fa fa-pencil-square-o"></em>Edit</a>
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

    <div class="panel panel-default">
        <div class="panel-heading completed">
            <h4 class="panel-title">
                <a class="collapsed" data-toggle="collapse" href="#previewBatInfo">Biological Agents &amp;Toxins</a>
            </h4>
        </div>
        <div id="previewBatInfo" class="panel-collapse collapse">
            <div class="panel-body">
                <div class="text-right app-font-size-16"><a href="#" data-step-key="approvalProfile"><em
                        class="fa fa-pencil-square-o"></em>Edit</a>
                </div>
                <c:forEach var="item" items="${dataSubInfo.submissionBats}" varStatus="status">
                    <div class="panel-main-content form-horizontal min-row">
                        <div class="form-group">
                            <div class="col-10"><strong>Agents/Toxin ${status.index+1}</strong></div>
                            <div class="clear"></div>
                        </div>
                        <div>
                            <div class="form-group">
                                <label class="col-xs-5 col-md-4 control-label">Transfer Code</label>
                                <div class="col-sm-7 col-md-5 col-xs-7"><p></p></div>
                                <div class="clear"></div>
                            </div>
                            <div class="form-group">
                                <label class="col-xs-5 col-md-4 control-label">Schedule Type</label>
                                <div class="col-sm-7 col-md-5 col-xs-7">
                                    <p><iais:code code="${item.schedule}"/></p>
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
                                        <label class="col-xs-5 col-md-4 control-label">Type Transferred</label>
                                        <div class="col-sm-7 col-md-5 col-xs-7"><p><iais:code
                                                code="${item.handleType}"/></p></div>
                                        <div class="clear"></div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-xs-5 col-md-4 control-label">Quantity Transferred</label>
                                        <div class="col-sm-7 col-md-5 col-xs-7"><p>${item.transferredQty}</p></div>
                                        <div class="clear"></div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-xs-5 col-md-4 control-label">Quantity of Biological Agent
                                            Received</label>
                                        <div class="col-sm-7 col-md-5 col-xs-7"><p>${item.actualQty}</p></div>
                                        <div class="clear"></div>
                                    </div>
                                </c:when>
                                <c:otherwise>
                                    <div class="form-group">
                                        <label class="col-xs-5 col-md-4 control-label">Quantity Consumed</label>
                                        <div class="col-sm-7 col-md-5 col-xs-7"><p>${item.transferredQty}</p></div>
                                        <div class="clear"></div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-xs-5 col-md-4 control-label">Unit of Measurement</label>
                                        <div class="col-sm-7 col-md-5 col-xs-7"><p><iais:code
                                                code="${item.transferredUnit}"/></p></div>
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
                            <label class="col-xs-5 col-md-4 control-label">Mode of Procurement</label>
                            <div class="col-sm-7 col-md-5 col-xs-7"><p>Importation</p></div>
                            <div class="clear"></div>
                        </div>
                        <div class="form-group">
                            <label class="col-xs-5 col-md-4 control-label">Facility Name</label>
                            <div class="col-sm-7 col-md-5 col-xs-7"><p>${dataSubInfo.facilityName}</p></div>
                            <div class="clear"></div>
                        </div>
                        <div class="form-group">
                            <label class="col-xs-5 col-md-4 control-label">Receiving Facility</label>
                            <div class="col-sm-7 col-md-5 col-xs-7"><p>${dataSubInfo.facilityReceiving}</p></div>
                            <div class="clear"></div>
                        </div>
                        <div class="form-group">
                            <label class="col-xs-5 col-md-4 control-label">Actual Date of Receipt</label>
                            <div class="col-sm-7 col-md-5 col-xs-7"><p>${dataSubInfo.actualArrivalDate}</p></div>
                            <div class="clear"></div>
                        </div>
                        <div class="form-group">
                            <label class="col-xs-5 col-md-4 control-label">Actual Time of Receipt</label>
                            <div class="col-sm-7 col-md-5 col-xs-7"><p>${dataSubInfo.actualArrivalTime}</p></div>
                            <div class="clear"></div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <%@include file="submissionDoc.jsp" %>
    <div class="col-xs-12">
        <a class="back" href="/bsb-web/eservice/INTERNET/DataSubInbox"><em class="fa fa-angle-left"></em> Back</a>
    </div>

</div>