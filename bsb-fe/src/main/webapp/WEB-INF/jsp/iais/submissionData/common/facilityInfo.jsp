<div class="panel panel-default">
    <div class="panel-heading"><strong>Facility Info</strong></div>
    <div class="row form-horizontal">
        <div class="col-xs-12 col-sm-12" style="padding: 20px 30px 10px 30px; border-radius: 15px;margin: 0 auto">
            <div class="col-xs-12 col-sm-12">
                <div class="panel-main-content form-horizontal min-row">
                    <div class="form-group">
                        <div class="col-10"><strong>Facility Profile</strong></div>
                        <div class="clear"></div>
                    </div>
                    <div>
                        <div class="form-group">
                            <label class="col-xs-5 col-md-4 control-label">Facility Name</label>
                            <div class="col-sm-7 col-md-5 col-xs-7"><p>${facilityInfo.facName}</p></div>
                            <div class="clear"></div>
                        </div>
                        <div class="form-group">
                            <label class="col-xs-5 col-md-4 control-label">Facility Address</label>
                            <div class="col-sm-7 col-md-5 col-xs-7"><p>${facilityInfo.facAddress}</p></div>
                            <div class="clear"></div>
                        </div>
                        <div class="form-group">
                            <label class="col-xs-5 col-md-4 control-label">Is the facility a Protected
                                Place?</label>
                            <div class="col-sm-7 col-md-5 col-xs-7"><p>${facilityInfo.isProtected}</p></div>
                            <div class="clear"></div>
                        </div>
                    </div>
                </div>
                <br/>
                <c:forEach items="${facilityInfo.facAdminList}" var="admin" varStatus="status">
                    <div class="panel-main-content form-horizontal min-row">
                        <div class="form-group">
                            <div class="col-10"><strong>Facility Administrator ${status.index +1}</strong></div>
                            <div class="clear"></div>
                        </div>
                        <div>
                            <div class="form-group">
                                <label class="col-xs-5 col-md-4 control-label">Name</label>
                                <div class="col-sm-7 col-md-5 col-xs-7"><p>${admin.name}</p></div>
                                <div class="clear"></div>
                            </div>
                            <div class="form-group">
                                <label class="col-xs-5 col-md-4 control-label">Nationality</label>
                                <div class="col-sm-7 col-md-5 col-xs-7"><p>${admin.nationality}</p></div>
                                <div class="clear"></div>
                            </div>
                            <div class="form-group">
                                <label class="col-xs-5 col-md-4 control-label">NRIC/FIN</label>
                                <div class="col-sm-7 col-md-5 col-xs-7"><p>${admin.idNumber}</p></div>
                                <div class="clear"></div>
                            </div>
                            <div class="form-group">
                                <label class="col-xs-5 col-md-4 control-label">Designation</label>
                                <div class="col-sm-7 col-md-5 col-xs-7"><p>${admin.designation}</p></div>
                                <div class="clear"></div>
                            </div>
                            <div class="form-group">
                                <label class="col-xs-5 col-md-4 control-label">Contact No.</label>
                                <div class="col-sm-7 col-md-5 col-xs-7"><p>${admin.contactNo}</p></div>
                                <div class="clear"></div>
                            </div>
                            <div class="form-group">
                                <label class="col-xs-5 col-md-4 control-label">Email Address</label>
                                <div class="col-sm-7 col-md-5 col-xs-7"><p>${admin.email}</p></div>
                                <div class="clear"></div>
                            </div>
                            <div class="form-group">
                                <label class="col-xs-5 col-md-4 control-label">Employment Start Date</label>
                                <div class="col-sm-7 col-md-5 col-xs-7"><p>${admin.employmentStartDate}</p></div>
                                <div class="clear"></div>
                            </div>
                        </div>
                    </div>
                </c:forEach>
                <br/>
                <div class="panel-main-content form-horizontal min-row">
                    <div class="form-group">
                        <div class="col-10"><strong>Facility Officer</strong></div>
                        <div class="clear"></div>
                    </div>
                    <div>
                        <div class="form-group">
                            <label class="col-xs-5 col-md-4 control-label">Name</label>
                            <div class="col-sm-7 col-md-5 col-xs-7"><p>${facilityInfo.facOfficer.name}</p></div>
                            <div class="clear"></div>
                        </div>
                        <div class="form-group">
                            <label class="col-xs-5 col-md-4 control-label">Nationality</label>
                            <div class="col-sm-7 col-md-5 col-xs-7"><p>${facilityInfo.facOfficer.nationality}</p></div>
                            <div class="clear"></div>
                        </div>
                        <div class="form-group">
                            <label class="col-xs-5 col-md-4 control-label">NRIC/FIN</label>
                            <div class="col-sm-7 col-md-5 col-xs-7"><p>${facilityInfo.facOfficer.idNumber}</p></div>
                            <div class="clear"></div>
                        </div>
                        <div class="form-group">
                            <label class="col-xs-5 col-md-4 control-label">Designation</label>
                            <div class="col-sm-7 col-md-5 col-xs-7"><p>${facilityInfo.facOfficer.designation}</p></div>
                            <div class="clear"></div>
                        </div>
                        <div class="form-group">
                            <label class="col-xs-5 col-md-4 control-label">Contact No.</label>
                            <div class="col-sm-7 col-md-5 col-xs-7"><p>${facilityInfo.facOfficer.contactNo}</p></div>
                            <div class="clear"></div>
                        </div>
                        <div class="form-group">
                            <label class="col-xs-5 col-md-4 control-label">Email Address</label>
                            <div class="col-sm-7 col-md-5 col-xs-7"><p>${facilityInfo.facOfficer.email}</p></div>
                            <div class="clear"></div>
                        </div>
                        <div class="form-group">
                            <label class="col-xs-5 col-md-4 control-label">Employment Start Date</label>
                            <div class="col-sm-7 col-md-5 col-xs-7"><p>${facilityInfo.facOfficer.employmentStartDate}</p></div>
                            <div class="clear"></div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>