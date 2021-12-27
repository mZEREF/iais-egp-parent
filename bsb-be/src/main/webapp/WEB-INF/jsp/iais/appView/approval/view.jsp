<div class="panel panel-default">
    <div class="panel-heading completed">
        <h4 class="panel-title">
            <a class="collapsed" data-toggle="collapse" href="#previewBatInfo">Biological Agents &amp; Toxins</a>
        </h4>
    </div>
    <div id="previewBatInfo" class="panel-collapse collapse">
        <div class="panel-body">
            <c:forEach var="approvalProfile" items="${approvalProfileList}">
                <div class="panel-main-content form-horizontal min-row">
                    <div class="form-group">
                        <div class="col-10"><strong><iais:code code="${approvalProfile.schedule}"/></strong></div>
                        <div class="clear"></div>
                    </div>
                    <c:forEach var="info" items="${approvalProfile.batInfos}">
                        <div>
                            <div class="form-group">
                                <label class="col-xs-5 col-md-4 control-label">List of Agents or Toxins</label>
                                <div class="col-sm-7 col-md-5 col-xs-7"><p>${info.batName}</p></div>
                                <div class="clear"></div>
                            </div>
                        </div>
                        <div>
                            <div class="form-group">
                                <label class="col-xs-5 col-md-4 control-label">Estimated maximum volume (in litres) of production at any one time</label>
                                <div class="col-sm-7 col-md-5 col-xs-7"><p>${info.prodMaxVolumeLitres}</p></div>
                                <div class="clear"></div>
                            </div>
                        </div>
                        <div>
                            <div class="form-group">
                                <label class="col-xs-5 col-md-4 control-label">Method or system used for large scale production</label>
                                <div class="col-sm-7 col-md-5 col-xs-7"><p>${info.lspMethod}</p></div>
                                <div class="clear"></div>
                            </div>
                        </div>
                        <div>
                            <div class="form-group">
                                <label class="col-xs-5 col-md-4 control-label">Mode of Procurement</label>
                                <div class="col-sm-7 col-md-5 col-xs-7"><p>${info.procurementMode}</p></div>
                                <div class="clear"></div>
                            </div>
                        </div>
                        <div>
                            <div class="form-group">
                                <label class="col-xs-5 col-md-4 control-label">Transfer From Facility Name</label>
                                <div class="col-sm-7 col-md-5 col-xs-7"><p>${info.facilityNameOfTransfer}</p></div>
                                <div class="clear"></div>
                            </div>
                        </div>
                        <div>
                            <div class="form-group">
                                <label class="col-xs-5 col-md-4 control-label">Expected Date of Transfer</label>
                                <div class="col-sm-7 col-md-5 col-xs-7"><p>${info.expectedDateOfImport}</p></div>
                                <div class="clear"></div>
                            </div>
                        </div>
                        <div>
                            <div class="form-group">
                                <label class="col-xs-5 col-md-4 control-label">Contact Person from Transferring Facility</label>
                                <div class="col-sm-7 col-md-5 col-xs-7"><p>${info.contactPersonNameOfTransfer}</p></div>
                                <div class="clear"></div>
                            </div>
                        </div>
                        <div>
                            <div class="form-group">
                                <label class="col-xs-5 col-md-4 control-label">Contact No of Contact Person from Transferring Facility</label>
                                <div class="col-sm-7 col-md-5 col-xs-7"><p>${info.impCtcPersonNo}</p></div>
                                <div class="clear"></div>
                            </div>
                        </div>
                        <div>
                            <div class="form-group">
                                <label class="col-xs-5 col-md-4 control-label">Email Address of Contact Person from Transferring Facility</label>
                                <div class="col-sm-7 col-md-5 col-xs-7"><p>${info.contactPersonEmailOfTransfer}</p></div>
                                <div class="clear"></div>
                            </div>
                        </div>
                        <div>
                            <div class="form-group">
                                <label class="col-xs-5 col-md-4 control-label">Facility Address 1</label>
                                <div class="col-sm-7 col-md-5 col-xs-7"><p>${info.transferFacAddr1}</p></div>
                                <div class="clear"></div>
                            </div>
                        </div>
                        <div>
                            <div class="form-group">
                                <label class="col-xs-5 col-md-4 control-label">Facility Address 2</label>
                                <div class="col-sm-7 col-md-5 col-xs-7"><p>${info.transferFacAddr2}</p></div>
                                <div class="clear"></div>
                            </div>
                        </div>
                        <div>
                            <div class="form-group">
                                <label class="col-xs-5 col-md-4 control-label">Facility Address 3</label>
                                <div class="col-sm-7 col-md-5 col-xs-7"><p>${info.transferFacAddr3}</p></div>
                                <div class="clear"></div>
                            </div>
                        </div>
                        <div>
                            <div class="form-group">
                                <label class="col-xs-5 col-md-4 control-label">Country</label>
                                <div class="col-sm-7 col-md-5 col-xs-7"><p>${info.transferCountry}</p></div>
                                <div class="clear"></div>
                            </div>
                        </div>
                        <div>
                            <div class="form-group">
                                <label class="col-xs-5 col-md-4 control-label">City</label>
                                <div class="col-sm-7 col-md-5 col-xs-7"><p>${info.transferCity}</p></div>
                                <div class="clear"></div>
                            </div>
                        </div>
                        <div>
                            <div class="form-group">
                                <label class="col-xs-5 col-md-4 control-label">State</label>
                                <div class="col-sm-7 col-md-5 col-xs-7"><p>${info.transferState}</p></div>
                                <div class="clear"></div>
                            </div>
                        </div>
                        <div>
                            <div class="form-group">
                                <label class="col-xs-5 col-md-4 control-label">Postal Code</label>
                                <div class="col-sm-7 col-md-5 col-xs-7"><p>${info.transferPostalCode}</p></div>
                                <div class="clear"></div>
                            </div>
                        </div>
                        <div>
                            <div class="form-group">
                                <label class="col-xs-5 col-md-4 control-label">Name of Courier Service Provider</label>
                                <div class="col-sm-7 col-md-5 col-xs-7"><p>${info.courierServiceProviderName}</p></div>
                                <div class="clear"></div>
                            </div>
                        </div>
                        <div>
                            <div class="form-group">
                                <label class="col-xs-5 col-md-4 control-label">Remarks</label>
                                <div class="col-sm-7 col-md-5 col-xs-7"><p>${info.remarks}</p></div>
                                <div class="clear"></div>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </c:forEach>
        </div>
    </div>
</div>
<div class="panel panel-default">
    <div class="panel-heading completed">
        <h4 class="panel-title">
            <a class="collapsed" data-toggle="collapse" href="#previewDocs">Primary Documents</a>
        </h4>
    </div>
    <div id="previewDocs" class="panel-collapse collapse">
        <div class="panel-body">
            <div class="panel-main-content form-horizontal min-row">
                <div class="form-group">
                    <div class="col-10"><strong>Uploaded Documents</strong></div>
                    <div class="clear"></div>
                </div>
            </div>
        </div>
    </div>
</div>