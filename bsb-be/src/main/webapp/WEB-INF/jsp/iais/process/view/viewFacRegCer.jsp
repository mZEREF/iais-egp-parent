
    <div class="main-content">
        <div class="container">
            <div class="row">
                <div class="col-xs-12">
                    <div class="tab-gp steps-tab">
                        <div class="tab-content">
                            <div class="tab-pane fade in active">
                                <div id="previewSubmitPanel" role="tabpanel">
                                    <div class="preview-gp">
                                        <div class="row">
                                            <div class="col-xs-12">
                                                <div class="panel-group" role="tablist" aria-multiselectable="true">
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
                                                                            <div class="col-sm-7 col-md-5 col-xs-7"><p>${submitDetailsDto.facilityCertifierRegisterDto.profileDto.orgName}</p></div>
                                                                            <div class="clear"></div>
                                                                        </div>
                                                                        <div class="form-group">
                                                                            <label class="col-xs-5 col-md-4 control-label">Facility Address</label>
                                                                            <div class="col-sm-7 col-md-5 col-xs-7"><p>${submitDetailsDto.facilityCertifierRegisterDto.profileDto.streetName} ${submitDetailsDto.facilityCertifierRegisterDto.profileDto.building} ${submitDetailsDto.facilityCertifierRegisterDto.profileDto.floor} - ${submitDetailsDto.facilityCertifierRegisterDto.profileDto.unitNo} ${submitDetailsDto.facilityCertifierRegisterDto.profileDto.postalCode}</p></div>
                                                                            <div class="clear"></div>
                                                                        </div>
                                                                    </div>
                                                                </div>
                                                                <div class="panel-main-content form-horizontal min-row">
                                                                    <div class="form-group">
                                                                        <div class="col-10"><strong>Facility Certifier Team Member</strong></div>
                                                                        <div class="clear"></div>
                                                                    </div>
                                                                    <c:forEach var="tMember" items="${submitDetailsDto.facilityCertifierRegisterDto.certifyingTeamDto.certifierTeamMemberList}" varStatus="status">
                                                                        <div>
                                                                            <c:if test="${submitDetailsDto.facilityCertifierRegisterDto.certifyingTeamDto.certifierTeamMemberList.size() > 1}">
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
                                                                            <div class="col-sm-7 col-md-5 col-xs-7"><p>${submitDetailsDto.facilityCertifierRegisterDto.administratorDto.mainAdmin.adminName}</p></div>
                                                                            <div class="clear"></div>
                                                                        </div>
                                                                        <div class="form-group">
                                                                            <label class="col-xs-5 col-md-4 control-label">NRIC/FIN</label>
                                                                            <div class="col-sm-7 col-md-5 col-xs-7"><p>${submitDetailsDto.facilityCertifierRegisterDto.administratorDto.mainAdmin.idNo}</p></div>
                                                                            <div class="clear"></div>
                                                                        </div>
                                                                        <div class="form-group">
                                                                            <label class="col-xs-5 col-md-4 control-label">Nationality</label>
                                                                            <div class="col-sm-7 col-md-5 col-xs-7"><p>${submitDetailsDto.facilityCertifierRegisterDto.administratorDto.mainAdmin.nationality}</p></div>
                                                                            <div class="clear"></div>
                                                                        </div>
                                                                        <div class="form-group">
                                                                            <label class="col-xs-5 col-md-4 control-label">Designation</label>
                                                                            <div class="col-sm-7 col-md-5 col-xs-7"><p>${submitDetailsDto.facilityCertifierRegisterDto.administratorDto.mainAdmin.designation}</p></div>
                                                                            <div class="clear"></div>
                                                                        </div>
                                                                        <div class="form-group">
                                                                            <label class="col-xs-5 col-md-4 control-label">Contect No.</label>
                                                                            <div class="col-sm-7 col-md-5 col-xs-7"><p>${submitDetailsDto.facilityCertifierRegisterDto.administratorDto.mainAdmin.contactNo}</p></div>
                                                                            <div class="clear"></div>
                                                                        </div>
                                                                        <div class="form-group">
                                                                            <label class="col-xs-5 col-md-4 control-label">Email Address</label>
                                                                            <div class="col-sm-7 col-md-5 col-xs-7"><p>${submitDetailsDto.facilityCertifierRegisterDto.administratorDto.mainAdmin.email}</p></div>
                                                                            <div class="clear"></div>
                                                                        </div>
                                                                        <div class="form-group">
                                                                            <label class="col-xs-5 col-md-4 control-label">Employment Start Date</label>
                                                                            <div class="col-sm-7 col-md-5 col-xs-7"><p>${submitDetailsDto.facilityCertifierRegisterDto.administratorDto.mainAdmin.employmentStartDate}</p></div>
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
                                                                            <div class="col-sm-7 col-md-5 col-xs-7"><p>${submitDetailsDto.facilityCertifierRegisterDto.administratorDto.alternativeAdmin.adminName}</p></div>
                                                                            <div class="clear"></div>
                                                                        </div>
                                                                        <div class="form-group">
                                                                            <label class="col-xs-5 col-md-4 control-label">NRIC/FIN</label>
                                                                            <div class="col-sm-7 col-md-5 col-xs-7"><p>${submitDetailsDto.facilityCertifierRegisterDto.administratorDto.alternativeAdmin.idNo}</p></div>
                                                                            <div class="clear"></div>
                                                                        </div>
                                                                        <div class="form-group">
                                                                            <label class="col-xs-5 col-md-4 control-label">Nationality</label>
                                                                            <div class="col-sm-7 col-md-5 col-xs-7"><p>${submitDetailsDto.facilityCertifierRegisterDto.administratorDto.alternativeAdmin.nationality}</p></div>
                                                                            <div class="clear"></div>
                                                                        </div>
                                                                        <div class="form-group">
                                                                            <label class="col-xs-5 col-md-4 control-label">Designation</label>
                                                                            <div class="col-sm-7 col-md-5 col-xs-7"><p>${submitDetailsDto.facilityCertifierRegisterDto.administratorDto.alternativeAdmin.designation}</p></div>
                                                                            <div class="clear"></div>
                                                                        </div>
                                                                        <div class="form-group">
                                                                            <label class="col-xs-5 col-md-4 control-label">Contect No.</label>
                                                                            <div class="col-sm-7 col-md-5 col-xs-7"><p>${submitDetailsDto.facilityCertifierRegisterDto.administratorDto.alternativeAdmin.contactNo}</p></div>
                                                                            <div class="clear"></div>
                                                                        </div>
                                                                        <div class="form-group">
                                                                            <label class="col-xs-5 col-md-4 control-label">Email Address</label>
                                                                            <div class="col-sm-7 col-md-5 col-xs-7"><p>${submitDetailsDto.facilityCertifierRegisterDto.administratorDto.alternativeAdmin.email}</p></div>
                                                                            <div class="clear"></div>
                                                                        </div>
                                                                        <div class="form-group">
                                                                            <label class="col-xs-5 col-md-4 control-label">Employment Start Date</label>
                                                                            <div class="col-sm-7 col-md-5 col-xs-7"><p>${submitDetailsDto.facilityCertifierRegisterDto.administratorDto.alternativeAdmin.employmentStartDate}</p></div>
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
                                                                        <c:set var="docFiles" value="${primaryDoc.get(doc.type)}"/>
                                                                        <c:if test="${not empty docFiles}">
                                                                            <div class="form-group">
                                                                                <div class="col-10"><strong>${doc.typeDisplay}</strong></div>
                                                                                <div class="clear"></div>
                                                                            </div>
                                                                            <div>
                                                                                <c:forEach var="file" items="${docFiles}">
                                                                                    <div class="form-group">
                                                                                        <div class="col-10"><p>${file.filename}(${String.format("%.1f", file.size/1024.0)}KB)</p></div>
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
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>