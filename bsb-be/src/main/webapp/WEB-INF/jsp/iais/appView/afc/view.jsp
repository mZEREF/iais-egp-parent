<div class="panel panel-default">
    <div class="panel-heading completed">
        <h4 class="panel-title">
            <a class="collapsed" data-toggle="collapse" href="#previewFacInfo">Facility Certifier Informations</a>
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
                        <div class="col-sm-7 col-md-5 col-xs-7"><p>${orgProfile.orgName}</p></div>
                        <div class="clear"></div>
                    </div>
                    <div class="form-group">
                        <label class="col-xs-5 col-md-4 control-label">Facility Address</label>
                        <div class="col-sm-7 col-md-5 col-xs-7"><p>${orgProfile.streetName} ${orgProfile.building} ${orgProfile.floor} - ${orgProfile.unitNo} ${orgProfile.postalCode}</p></div>
                        <div class="clear"></div>
                    </div>
                </div>
            </div>
            <div class="panel-main-content form-horizontal min-row">
                <div class="form-group">
                    <div class="col-10"><strong>Facility Certifier Team Member</strong></div>
                    <div class="clear"></div>
                </div>
                <c:forEach var="tMember" items="${orgCerTeam.certifierTeamMemberList}" varStatus="status">
                    <div>
                        <c:if test="${orgCerTeam.certifierTeamMemberList.size() > 1}">
                            <div class="form-group">
                                <label class="col-xs-5 col-md-4 control-label">Team Member ${status.index + 1}</label>
                                <div class="clear"></div>
                            </div>
                        </c:if>
                        <div class="form-group">
                            <label class="col-xs-5 col-md-4 control-label">Name</label>
                            <div class="col-sm-7 col-md-5 col-xs-7"><p>${tMember.memberName}</p></div>
                            <div class="clear"></div>
                        </div>
                        <div class="form-group">
                            <label class="col-xs-5 col-md-4 control-label">NRIC/FIN</label>
                            <div class="col-sm-7 col-md-5 col-xs-7"><p>${tMember.idType}</p></div>
                            <div class="clear"></div>
                        </div>
                        <div class="form-group">
                            <label class="col-xs-5 col-md-4 control-label">Date of Birth</label>
                            <div class="col-sm-7 col-md-5 col-xs-7"><p>${tMember.birthDate}</p></div>
                            <div class="clear"></div>
                        </div>
                        <div class="form-group">
                            <label class="col-xs-5 col-md-4 control-label">Sex</label>
                            <div class="col-sm-7 col-md-5 col-xs-7"><p>${tMember.sex}</p></div>
                            <div class="clear"></div>
                        </div>
                        <div class="form-group">
                            <label class="col-xs-5 col-md-4 control-label">Nationality</label>
                            <div class="col-sm-7 col-md-5 col-xs-7"><p>${tMember.nationality}</p></div>
                            <div class="clear"></div>
                        </div>
                        <div class="form-group">
                            <label class="col-xs-5 col-md-4 control-label">Job Designation</label>
                            <div class="col-sm-7 col-md-5 col-xs-7"><p>${tMember.jobDesignation}</p></div>
                            <div class="clear"></div>
                        </div>
                        <div class="form-group">
                            <label class="col-xs-5 col-md-4 control-label">Contect No.</label>
                            <div class="col-sm-7 col-md-5 col-xs-7"><p>${tMember.telNo}</p></div>
                            <div class="clear"></div>
                        </div>
                    </div>
                </c:forEach>
            </div>
            <div class="panel-main-content form-horizontal min-row">
                <div class="form-group">
                    <div class="col-10"><strong>Facility Certifier Administrator</strong></div>
                    <div class="clear"></div>
                </div>
                <div>
                    <div class="form-group">
                        <label class="col-xs-5 col-md-4 control-label">Main Administrator</label>
                        <div class="clear"></div>
                    </div>
                    <div class="form-group">
                        <label class="col-xs-5 col-md-4 control-label">Name</label>
                        <div class="col-sm-7 col-md-5 col-xs-7"><p>${orgAdmin.mainAdmin.adminName}</p></div>
                        <div class="clear"></div>
                    </div>
                    <div class="form-group">
                        <label class="col-xs-5 col-md-4 control-label">NRIC/FIN</label>
                        <div class="col-sm-7 col-md-5 col-xs-7"><p>${orgAdmin.mainAdmin.idNo}</p></div>
                        <div class="clear"></div>
                    </div>
                    <div class="form-group">
                        <label class="col-xs-5 col-md-4 control-label">Nationality</label>
                        <div class="col-sm-7 col-md-5 col-xs-7"><p>${orgAdmin.mainAdmin.nationality}</p></div>
                        <div class="clear"></div>
                    </div>
                    <div class="form-group">
                        <label class="col-xs-5 col-md-4 control-label">Designation</label>
                        <div class="col-sm-7 col-md-5 col-xs-7"><p>${orgAdmin.mainAdmin.designation}</p></div>
                        <div class="clear"></div>
                    </div>
                    <div class="form-group">
                        <label class="col-xs-5 col-md-4 control-label">Contect No.</label>
                        <div class="col-sm-7 col-md-5 col-xs-7"><p>${orgAdmin.mainAdmin.contactNo}</p></div>
                        <div class="clear"></div>
                    </div>
                    <div class="form-group">
                        <label class="col-xs-5 col-md-4 control-label">Email Address</label>
                        <div class="col-sm-7 col-md-5 col-xs-7"><p>${orgAdmin.mainAdmin.email}</p></div>
                        <div class="clear"></div>
                    </div>
                    <div class="form-group">
                        <label class="col-xs-5 col-md-4 control-label">Employment Start Date</label>
                        <div class="col-sm-7 col-md-5 col-xs-7"><p>${orgAdmin.mainAdmin.employmentStartDate}</p></div>
                        <div class="clear"></div>
                    </div>
                </div>
                <div>
                    <div class="form-group">
                        <label class="col-xs-5 col-md-4 control-label">Alternative Administrator</label>
                        <div class="clear"></div>
                    </div>
                    <div class="form-group">
                        <label class="col-xs-5 col-md-4 control-label">Name</label>
                        <div class="col-sm-7 col-md-5 col-xs-7"><p>${orgAdmin.alternativeAdmin.adminName}</p></div>
                        <div class="clear"></div>
                    </div>
                    <div class="form-group">
                        <label class="col-xs-5 col-md-4 control-label">NRIC/FIN</label>
                        <div class="col-sm-7 col-md-5 col-xs-7"><p>${orgAdmin.alternativeAdmin.idNo}</p></div>
                        <div class="clear"></div>
                    </div>
                    <div class="form-group">
                        <label class="col-xs-5 col-md-4 control-label">Nationality</label>
                        <div class="col-sm-7 col-md-5 col-xs-7"><p>${orgAdmin.alternativeAdmin.nationality}</p></div>
                        <div class="clear"></div>
                    </div>
                    <div class="form-group">
                        <label class="col-xs-5 col-md-4 control-label">Designation</label>
                        <div class="col-sm-7 col-md-5 col-xs-7"><p>${orgAdmin.alternativeAdmin.designation}</p></div>
                        <div class="clear"></div>
                    </div>
                    <div class="form-group">
                        <label class="col-xs-5 col-md-4 control-label">Contect No.</label>
                        <div class="col-sm-7 col-md-5 col-xs-7"><p>${orgAdmin.alternativeAdmin.contactNo}</p></div>
                        <div class="clear"></div>
                    </div>
                    <div class="form-group">
                        <label class="col-xs-5 col-md-4 control-label">Email Address</label>
                        <div class="col-sm-7 col-md-5 col-xs-7"><p>${orgAdmin.alternativeAdmin.email}</p></div>
                        <div class="clear"></div>
                    </div>
                    <div class="form-group">
                        <label class="col-xs-5 col-md-4 control-label">Employment Start Date</label>
                        <div class="col-sm-7 col-md-5 col-xs-7"><p>${orgAdmin.alternativeAdmin.employmentStartDate}</p></div>
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
            <a class="collapsed" data-toggle="collapse" href="#previewDocs">Primary Documents</a>
        </h4>
    </div>
    <div id="previewDocs" class="panel-collapse collapse">
        <div class="panel-body">
            <div class="panel-main-content form-horizontal min-row">
                <c:forEach var="doc" items="${docSettings}">
                    <c:set var="savedFileList" value="${savedFiles.get(doc.type)}" />
                    <c:if test="${not empty savedFileList}">
                        <div class="form-group">
                            <div class="col-10"><strong>${doc.typeDisplay}</strong></div>
                            <div class="clear"></div>
                        </div>
                        <div>
                            <c:forEach var="file" items="${savedFileList}">
                                <c:set var="tmpId" value="${MaskUtil.maskValue('file', file.repoId)}"/>
                                <div class="form-group">
                                    <div class="col-10"><p><a href="javascript:void(0)" onclick="downloadFile('saved', '${tmpId}')">${file.filename}</a>(${String.format("%.1f", file.size/1024.0)}KB)</p></div>
                                    <div class="clear"></div>
                                </div>
                            </c:forEach>
                        </div>
                    </c:if>
                </c:forEach>
            </div>
        </div>
    </div>
</div>