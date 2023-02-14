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
                    <iais:field width="5" value="Who is the Licensee?"/>
                    <iais:value width="7" cssClass="col-md-7" display="true">
                        <div class="col-md-12">
                        <c:set var="entityType" value="${newLicenceDto.licenseeEntityDto.entityType}"/>
                        <c:if test="${empty entityType || '-' == entityType}" var="invalidType">
                            <iais:code code="${newLicenceDto.licenseeType}" />
                        </c:if>
                        <c:if test="${not invalidType}">
                            <iais:code code="${entityType}" />
                        </c:if>
                        </div>
                    </iais:value>
                </div>

                <div class="row img-show">
                    <iais:field width="5" value="UEN Number"/>
                    <iais:value width="7" cssClass="col-md-7" display="true">
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
                    </iais:value>
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
                    <iais:field width="5" value="Name of Licensee"/>
                    <iais:value width="7" cssClass="col-md-7" display="true">
                        <div class="col-md-6 ">
                                <span class="newVal " attr="${newLicenceDto.name}">
                                  <c:out value="${newLicenceDto.name}"/>
                                </span>
                        </div>
                        <div class="col-md-6">
                            <span class="oldVal " attr="${oldLicenceDto.name}" style="display: none"><c:out value="${oldLicenceDto.name}"/></span>
                        </div>
                    </iais:value>
                </div>

                <div class="row">
                    <iais:field width="5" value="Postal Code"/>
                    <iais:value width="7" cssClass="col-md-7" display="true">
                        <div class="col-md-6">
                            <span class="newVal " attr="${newLicenceDto.postalCode}"><c:out value="${newLicenceDto.postalCode}"/></span>
                        </div>
                        <div class="col-md-6">
                            <span class="oldVal " attr="${oldLicenceDto.postalCode}" style="display: none"><c:out value="${oldLicenceDto.postalCode}"/></span>

                        </div>
                    </iais:value>
                </div>

                <div class="row">
                    <iais:field width="5" value="Address Type"/>
                    <iais:value width="7" cssClass="col-md-7" display="true">
                        <div class="col-md-6">
                            <span class="newVal " attr="${newLicenceDto.addrType}"><iais:code code="${newLicenceDto.addrType}"/></span>

                        </div>
                        <div class="col-md-6">
                            <span class="oldVal" attr="${oldLicenceDto.addrType}" style="display: none"><iais:code code="${oldLicenceDto.addrType}"/></span>
                        </div>
                    </iais:value>
                </div>

                <div class="row">
                    <iais:field width="5" value="Blk No."/>
                    <iais:value width="7" cssClass="col-md-7" display="true">
                        <div class="col-md-6">
                            <span class="newVal " attr="${newLicenceDto.blkNo}"><c:out value="${newLicenceDto.blkNo}"/></span>
                        </div>
                        <div class="col-md-6">
                            <span class="oldVal " attr="${oldLicenceDto.blkNo}" style="display: none"><c:out value="${oldLicenceDto.blkNo}"/></span>
                        </div>
                    </iais:value>
                </div>

                <div class="row">
                    <iais:field width="5" value="Floor No."/>
                    <iais:value width="7" cssClass="col-md-7" display="true">
                        <div class="col-md-6">
                            <span class="newVal " attr="${newLicenceDto.floorNo}"><c:out value="${newLicenceDto.floorNo}"/></span>

                        </div>
                        <div class="col-md-6">
                            <span class="oldVal " attr="${oldLicenceDto.floorNo}" style="display: none"><c:out value="${oldLicenceDto.floorNo}"/></span>
                        </div>
                    </iais:value>
                </div>

                <div class="row">
                    <iais:field width="5" value="Unit No."/>
                    <iais:value width="7" cssClass="col-md-7" display="true">
                        <div class="col-md-6">
                            <span class="newVal " attr="${newLicenceDto.unitNo}"><c:out value="${newLicenceDto.unitNo}"/></span>
                        </div>
                        <div class="col-md-6">
                            <span class="oldVal " attr="${oldLicenceDto.unitNo}" style="display: none">${oldLicenceDto.unitNo}</span>

                        </div>
                    </iais:value>
                </div>

                <div class="row">
                    <iais:field width="5" value="Street Name"/>
                    <iais:value width="7" cssClass="col-md-7" display="true">
                        <div class="col-md-6">
                            <span class="newVal " attr="${newLicenceDto.streetName}"><c:out value="${newLicenceDto.streetName}"/></span>
                        </div>
                        <div class="col-md-6">
                            <span class="oldVal " attr="${oldLicenceDto.streetName}" style="display: none"><c:out value="${oldLicenceDto.streetName}"/></span>

                        </div>
                    </iais:value>
                </div>

                <div class="row">
                    <iais:field width="5" value="Building Name"/>
                    <iais:value width="7" cssClass="col-md-7" display="true">
                        <div class="col-md-6">
                            <span class="newVal " attr="${newLicenceDto.buildingName}"><c:out value="${newLicenceDto.buildingName}"/></span>
                        </div>
                        <div class="col-md-6">
                            <span class="oldVal " attr="${oldLicenceDto.buildingName}" style="display: none"><c:out value="${oldLicenceDto.buildingName}"/></span>

                        </div>
                    </iais:value>
                </div>

                <div class="row">
                    <iais:field width="5" value="Office Telephone No."/>
                    <iais:value width="7" cssClass="col-md-7" display="true">
                        <div class="col-md-6">
                            <span class="newVal " attr="${newLicenceDto.officeTelNo}"><c:out value="${newLicenceDto.officeTelNo}"/></span>
                        </div>
                        <div class="col-md-6">
                            <span class="oldVal " attr="${oldLicenceDto.officeTelNo}" style="display: none"><c:out value="${oldLicenceDto.officeTelNo}"/></span>

                        </div>
                    </iais:value>
                </div>

                <div class="row">
                    <iais:field width="5" value="Office Email Address"/>
                    <iais:value width="7" cssClass="col-md-7" display="true">
                        <div class="col-md-6">
                            <span class="newVal " attr="${newLicenceDto.emilAddr}"><c:out value="${newLicenceDto.emilAddr}"/></span>
                        </div>
                        <div class="col-md-6">
                            <span class="oldVal " attr="${oldLicenceDto.emilAddr}" style="display: none"><c:out value="${oldLicenceDto.emilAddr}"/></span>

                        </div>
                    </iais:value>
                </div>
                <c:forEach items="${appSubmissionDto.boardMember}" var="Board" varStatus="status">
                    <div class="row" style="margin-top: 1%;margin-bottom: 1%">
                        <div class="col-md-6"><label>Board Member ${status.index+1}</label></div>
                    </div>
                    <div class="row">
                        <iais:field width="5" value="Salutation"/>
                        <iais:value width="7" cssClass="col-md-7" display="true">
                            <span class="newVal " attr="${Board.salutation}"><c:out value="${Board.salutation}"/></span>
                        </iais:value>
                    </div>


                    <div class="row">
                        <iais:field width="5" value="Name"/>
                        <iais:value width="7" cssClass="col-md-7" display="true">
                            <div class="col-md-6">
                                <span class="newVal " attr="${Board.name}">${Board.name}</span>
                            </div>
                        </iais:value>
                    </div>

                    <div class="row img-show">
                        <iais:field width="5" value="ID No."/>
                        <iais:value width="7" cssClass="col-md-7" display="true">
                            <div class="col-md-6">
                                <span class="newVal " attr="${Board.idNo}">
                                    ${Board.idNo}
                                    <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/disciplinaryRecordMark.jsp">
                                        <jsp:param name="idNo" value="${Board.idNo}"/>
                                        <jsp:param name="methodName" value="showThisTableNew"/>
                                    </jsp:include>
                                </span>
                            </div>
                        </iais:value>
                    </div>
                    <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/disciplinaryRecords.jsp">
                        <jsp:param name="idNo" value="${Board.idNo}"/>
                        <jsp:param name="cssClass" value="new-img-show"/>
                    </jsp:include>

                    <div class="row">
                        <iais:field width="5" value="Designation"/>
                        <iais:value width="7" cssClass="col-md-7" display="true">
                            <div class="col-md-6">
                                <span  class="newVal " attr="${Board.designation}"><iais:code code="${Board.designation}"/></span>
                            </div>
                        </iais:value>
                    </div>


                </c:forEach>

                <c:forEach items="${appSubmissionDto.authorisedPerson}" var="Authorised" varStatus="status">
                    <div class="row" style="margin-top: 1%;margin-bottom: 1%">
                        <div class="col-md-6">
                            <label>Authorised Person ${status.index+1}</label>
                        </div>
                    </div>

                    <div class="row">
                        <iais:field width="5" value="Name"/>
                        <iais:value width="7" cssClass="col-md-7" display="true">
                            <div class="col-md-6">
                                <span  class="newVal " attr="${Authorised.displayName}">${Authorised.displayName}</span>
                            </div>
                        </iais:value>
                    </div>

                    <div class="row img-show">
                        <iais:field width="5" value="ID No."/>
                        <iais:value width="7" cssClass="col-md-7" display="true">
                            <div  class="col-md-6">
                                <span  class="newVal " attr="${Authorised.idNumber}">
                                    ${Authorised.idNumber}
                                    <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/disciplinaryRecordMark.jsp">
                                        <jsp:param name="idNo" value="${Authorised.idNumber}"/>
                                        <jsp:param name="methodName" value="showThisTableNew"/>
                                    </jsp:include>
                                </span>
                            </div>
                        </iais:value>
                    </div>
                    <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/disciplinaryRecords.jsp">
                        <jsp:param name="idNo" value="${Authorised.idNumber}"/>
                        <jsp:param name="cssClass" value="new-img-show"/>
                    </jsp:include>

                    <div class="row">
                        <iais:field width="5" value="Designation"/>
                        <iais:value width="7" cssClass="col-md-7" display="true">
                            <div class="col-md-6">
                                <span  class="newVal " attr="${Authorised.designation}"><iais:code code="${Authorised.designation}"/></span>
                            </div>
                        </iais:value>
                    </div>

                    <div class="row">
                        <iais:field width="5" value="Office Telephone"/>
                        <iais:value width="7" cssClass="col-md-7" display="true">
                            <div  class="col-md-6">
                                <span  class="newVal " attr="${Authorised.officeTelNo}">${Authorised.officeTelNo}</span>
                            </div>
                        </iais:value>
                    </div>

                    <div class="row">
                        <iais:field width="5" value="Mobile No."/>
                        <iais:value width="7" cssClass="col-md-7" display="true">
                            <div class="col-md-6">
                                <span class="newVal" attr="${Authorised.mobileNo}">${Authorised.mobileNo}</span>
                            </div>
                        </iais:value>
                    </div>


                    <div class="row">
                        <iais:field width="5" value="Email Address"/>
                        <iais:value width="7" cssClass="col-md-7" display="true">
                            <div class="col-md-6">
                                <span class="newVal " attr="${Authorised.email}">${Authorised.email}</span>
                            </div>
                        </iais:value>
                    </div>


                </c:forEach>

            </div>
        </div>
    </div>
</div>