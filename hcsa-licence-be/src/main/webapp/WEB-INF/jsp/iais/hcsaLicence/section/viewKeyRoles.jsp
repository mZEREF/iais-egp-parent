<div class="panel panel-default">
    <div class="panel-heading" id="headingOne" role="tab">
        <h4 class="panel-title">
            <a class="collapsed" role="button" data-toggle="collapse" href="#keyRoles" aria-expanded="true"
               aria-controls="collapseOne">Key Roles</a>
        </h4>
    </div>
    <div class="panel-collapse collapse" id="keyRoles" role="tabpanel" aria-labelledby="headingOne">
        <div class="panel-body">
            <p class="text-right">
                <!--<input class="form-check-input" id="primaryCheckbox" type="checkbox" name="editCheckbox" aria-invalid="false" value="primary">-->
            </p>
            <div class="panel-main-content postion-relative">

                <div class="row" style="margin-top: 1%;margin-bottom: 1%">
                    <div class="col-md-6">
                        <label>Licensee Information</label>
                    </div>
                    <div class="col-md-6">
                    </div>
                </div>

                <div class="row">
                    <div class="col-md-8">
                        Who is the Licensee?
                    </div>
                    <div class="col-md-6">
                        <c:set var="entityType" value="${newLicenceDto.licenseeEntityDto.entityType}"/>
                        <c:if test="${empty entityType || '-' == entityType}" var="invalidType">
                            <iais:code code="${newLicenceDto.licenseeType}" />
                        </c:if>
                        <c:if test="${not invalidType}">
                            <iais:code code="${entityType}" />
                        </c:if>
                    </div>
                </div>

                <div class="row img-show">
                    <div class="col-md-6">
                        UEN Number
                    </div>
                    <div class="col-md-6">
                        <div class="col-md-6">
                            <span class="newVal " attr="${newLicenceDto.uenNo}">
                                ${newLicenceDto.uenNo}
                                <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/disciplinaryRecordMark.jsp">
                                    <jsp:param name="idNo" value="${newLicenceDto.uenNo}"/>
                                    <jsp:param name="methodName" value="showThisTableNew"/>
                                </jsp:include>
                            </span>
                        </div>
                        <div class="col-md-6">
                            <span class="oldVal " style="display: none" attr="${oldLicenceDto.uenNo}">
                                ${oldLicenceDto.uenNo}
                                <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/disciplinaryRecordMark.jsp">
                                    <jsp:param name="idNo" value="${oldLicenceDto.uenNo}"/>
                                    <jsp:param name="methodName" value="showThisTableOld"/>
                                </jsp:include>
                            </span>
                        </div>
                    </div>
                </div>
                <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/disciplinaryRecords.jsp">
                    <jsp:param name="idNo" value="${newLicenceDto.uenNo}"/>
                    <jsp:param name="cssClass" value="new-img-show"/>
                </jsp:include>
                <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/disciplinaryRecords.jsp">
                    <jsp:param name="idNo" value="${oldLicenceDto.uenNo}"/>
                    <jsp:param name="cssClass" value="old-img-show"/>
                </jsp:include>
                <div class="row">
                    <div class="col-md-6">
                        Name of Licensee
                    </div>
                    <div class="col-md-6">
                        <div class="col-md-6 ">
                                <span class="newVal " attr="${newLicenceDto.name}">
                                  <c:out value="${newLicenceDto.name}"/>
                                </span>
                        </div>
                        <div class="col-md-6">
                            <span class="oldVal " attr="${oldLicenceDto.name}" style="display: none"><c:out value="${oldLicenceDto.name}"/></span>
                        </div>
                    </div>
                </div>

                <div class="row">
                    <div class="col-md-6">
                        Postal Code
                    </div>
                    <div class="col-md-6">
                        <div class="col-md-6">
                            <span class="newVal " attr="${newLicenceDto.postalCode}"><c:out value="${newLicenceDto.postalCode}"/></span>
                        </div>
                        <div class="col-md-6">
                            <span class="oldVal " attr="${oldLicenceDto.postalCode}" style="display: none"><c:out value="${oldLicenceDto.postalCode}"/></span>

                        </div>
                    </div>
                </div>

                <div class="row">
                    <div class="col-md-6">
                        Address Type
                    </div>
                    <div class="col-md-6">
                        <div class="col-md-6">
                            <span class="newVal " attr="${newLicenceDto.addrType}"><iais:code code="${newLicenceDto.addrType}"/></span>

                        </div>
                        <div class="col-md-6">
                            <span class="oldVal" attr="${oldLicenceDto.addrType}" style="display: none"><iais:code code="${oldLicenceDto.addrType}"/></span>
                        </div>
                    </div>
                </div>

                <div class="row">
                    <div class="col-md-6">
                        Blk No.
                    </div>
                    <div class="col-md-6">
                        <div class="col-md-6">
                            <span class="newVal " attr="${newLicenceDto.unitNo}"><c:out value="${newLicenceDto.unitNo}"/></span>
                        </div>
                        <div class="col-md-6">
                            <span class="oldVal " attr="${oldLicenceDto.unitNo}" style="display: none"><c:out value="${oldLicenceDto.unitNo}"/></span>
                        </div>
                    </div>
                </div>

                <div class="row">
                    <div class="col-md-6">
                        Floor No.
                    </div>
                    <div class="col-md-6">
                        <div class="col-md-6">
                            <span class="newVal " attr="${newLicenceDto.floorNo}"><c:out value="${newLicenceDto.floorNo}"/></span>

                        </div>
                        <div class="col-md-6">
                            <span class="oldVal " attr="${oldLicenceDto.floorNo}" style="display: none"><c:out value="${oldLicenceDto.floorNo}"/></span>
                        </div>
                    </div>
                </div>

                <div class="row">
                    <div class="col-md-6">
                        Unit No.
                    </div>
                    <div class="col-md-6">
                        <div class="col-md-6">
                            <span class="newVal " attr="${newLicenceDto.unitNo}"><c:out value="${newLicenceDto.unitNo}"/></span>
                        </div>
                        <div class="col-md-6">
                            <span class="oldVal " attr="${oldLicenceDto.unitNo}" style="display: none">${oldLicenceDto.unitNo}</span>

                        </div>
                    </div>
                </div>

                <div class="row">
                    <div class="col-md-6">
                        Street Name
                    </div>
                    <div class="col-md-6">
                        <div class="col-md-6">
                            <span class="newVal " attr="${newLicenceDto.streetName}"><c:out value="${newLicenceDto.streetName}"/></span>
                        </div>
                        <div class="col-md-6">
                            <span class="oldVal " attr="${oldLicenceDto.streetName}" style="display: none"><c:out value="${oldLicenceDto.streetName}"/></span>

                        </div>
                    </div>
                </div>

                <div class="row">
                    <div class="col-md-6">
                        Building Name
                    </div>
                    <div class="col-md-6">
                        <div class="col-md-6">
                            <span class="newVal " attr="${newLicenceDto.buildingName}"><c:out value="${newLicenceDto.buildingName}"/></span>
                        </div>
                        <div class="col-md-6">
                            <span class="oldVal " attr="${oldLicenceDto.buildingName}" style="display: none"><c:out value="${oldLicenceDto.buildingName}"/></span>

                        </div>
                    </div>
                </div>

                <div class="row">
                    <div class="col-md-6">
                        Office Telephone No.
                    </div>
                    <div class="col-md-6">
                        <div class="col-md-6">
                            <span class="newVal " attr="${newLicenceDto.officeTelNo}"><c:out value="${newLicenceDto.officeTelNo}"/></span>
                        </div>
                        <div class="col-md-6">
                            <span class="oldVal " attr="${oldLicenceDto.officeTelNo}" style="display: none"><c:out value="${oldLicenceDto.officeTelNo}"/></span>

                        </div>
                    </div>
                </div>

                <div class="row">
                    <div class="col-md-6">
                        Office Email Address
                    </div>
                    <div class="col-md-6">
                        <div class="col-md-6">
                            <span class="newVal " attr="${newLicenceDto.emilAddr}"><c:out value="${newLicenceDto.emilAddr}"/></span>
                        </div>
                        <div class="col-md-6">
                            <span class="oldVal " attr="${oldLicenceDto.emilAddr}" style="display: none"><c:out value="${oldLicenceDto.emilAddr}"/></span>

                        </div>
                    </div>
                </div>
                <c:forEach items="${appSubmissionDto.boardMember}" var="Board" varStatus="status">
                    <div class="row" style="margin-top: 1%;margin-bottom: 1%">
                        <div class="col-md-6"><label>Board Member ${status.index+1}</label></div>
                    </div>
                    <div class="row">
                        <div class="col-md-6">
                            Salutation
                        </div>
                        <div class="col-md-6">
                            <span class="newVal " attr="${Board.salutation}"><c:out value="${Board.salutation}"/></span>
                        </div>
                    </div>


                    <div class="row">
                        <div class="col-md-6">
                            Name
                        </div>
                        <div class="col-md-6">
                            <div class="col-md-6">
                                <span class="newVal " attr="${Board.name}">${Board.name}</span>
                            </div>
                        </div>
                    </div>

                    <div class="row img-show">
                        <div class="col-md-6">
                            ID No.
                        </div>
                        <div class="col-md-6">
                            <div class="col-md-6">
                                <span class="newVal " attr="${Board.idNo}">
                                    ${Board.idNo}
                                    <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/disciplinaryRecordMark.jsp">
                                        <jsp:param name="idNo" value="${Board.idNo}"/>
                                        <jsp:param name="methodName" value="showThisTableNew"/>
                                    </jsp:include>
                                </span>
                            </div>
                        </div>
                    </div>
                    <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/disciplinaryRecords.jsp">
                        <jsp:param name="idNo" value="${Board.idNo}"/>
                        <jsp:param name="cssClass" value="new-img-show"/>
                    </jsp:include>

                    <div class="row">
                        <div class="col-md-6">
                            Designation
                        </div>
                        <div class="col-md-6">
                            <div class="col-md-6">
                                <span  class="newVal " attr="${Board.designation}"><iais:code code="${Board.designation}"/></span>
                            </div>
                        </div>
                    </div>


                </c:forEach>

                <c:forEach items="${appSubmissionDto.authorisedPerson}" var="Authorised" varStatus="status">
                    <div class="row" style="margin-top: 1%;margin-bottom: 1%">
                        <div class="col-md-6">
                            <label>Authorised Person ${status.index+1}</label>
                        </div>
                    </div>

                    <div class="row">
                        <div class="col-md-6">
                            Name
                        </div>
                        <div class="col-md-6">
                            <div class="col-md-6">
                                <span  class="newVal " attr="${Authorised.displayName}">${Authorised.displayName}</span>
                            </div>
                        </div>
                    </div>

                    <div class="row img-show">
                        <div class="col-md-6">
                            ID No.
                        </div>
                        <div class="col-md-6">
                            <div  class="col-md-6">
                                <span  class="newVal " attr="${Authorised.idNumber}">
                                    ${Authorised.idNumber}
                                    <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/disciplinaryRecordMark.jsp">
                                        <jsp:param name="idNo" value="${Authorised.idNumber}"/>
                                        <jsp:param name="methodName" value="showThisTableNew"/>
                                    </jsp:include>
                                </span>
                            </div>
                        </div>
                    </div>
                    <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/disciplinaryRecords.jsp">
                        <jsp:param name="idNo" value="${Authorised.idNumber}"/>
                        <jsp:param name="cssClass" value="new-img-show"/>
                    </jsp:include>

                    <div class="row">
                        <div class="col-md-6">
                            Designation
                        </div>
                        <div class="col-md-6">
                            <div class="col-md-6">
                                <span  class="newVal " attr="${Authorised.designation}"><iais:code code="${Authorised.designation}"/></span>
                            </div>
                        </div>
                    </div>

                    <div class="row">
                        <div class="col-md-6">
                            Office Telephone
                        </div>
                        <div class="col-md-6">
                            <div  class="col-md-6">
                                <span  class="newVal " attr="${Authorised.officeTelNo}">${Authorised.officeTelNo}</span>
                            </div>
                        </div>
                    </div>

                    <div class="row">
                        <div class="col-md-6">
                            Mobile No.
                        </div>
                        <div class="col-md-6">
                            <div class="col-md-6">
                                <span class="newVal" attr="${Authorised.mobileNo}">${Authorised.mobileNo}</span>
                            </div>
                        </div>
                    </div>


                    <div class="row">
                        <div class="col-md-6">
                            Email Address
                        </div>
                        <div class="col-md-6">
                            <div class="col-md-6">
                                <span class="newVal " attr="${Authorised.email}">${Authorised.email}</span>
                            </div>
                        </div>
                    </div>


                </c:forEach>

            </div>
        </div>
    </div>
</div>