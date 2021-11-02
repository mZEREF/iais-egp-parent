 <div class="panel panel-default">
    <div class="panel-heading">
        <h4 class="panel-title">
            <strong>
                Fertilisation
            </strong>
        </h4>
    </div>
    <div id="patientDetails" class="panel-collapse collapse in">
        <div class="panel-body">
            <div class="panel-main-content form-horizontal">
                <c:set var="fertilisationDto" value="${arSuperDataSubmissionDto.fertilisationDto}" />
                <iais:row>
                    <iais:field  value="Source of Semen" mandatory="true"/>
                    <iais:value width="3" cssClass="col-md-3">
                        <c:forEach items="${sourceOfSemens}" var="sourceOfSemen">
                            <c:set var="sourceOfSemenCode" value="${sourceOfSemen.code}"/>
                            <div class="form-check col-xs-12" >
                                <input class="form-check-input" type="checkbox"
                                       name="sourceOfSemen"
                                       value="${sourceOfSemenCode}"
                                       id="sourceOfSemenCheck${sourceOfSemenCode}"
                                <c:forEach var="sosObj" items="${fertilisationDto.sosList}">
                                       <c:if test="${sosObj == sourceOfSemenCode}">checked</c:if>
                                </c:forEach>
                                       aria-invalid="false">
                                <label class="form-check-label"
                                       for="sourceOfSemenCheck${sourceOfSemenCode}"><span
                                        class="check-square"></span>${sourceOfSemen.codeValue}</label>
                            </div>

                        </c:forEach>
                        <span class="error-msg" name="iaisErrorMsg" id="error_sourceOfSemen"></span>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="How many vials of sperm were extracted?" mandatory="true"/>
                    <iais:value width="7" cssClass="col-md-7">
                        <iais:input maxLength="2" type="text" name="extractedSpermVialsNum" value="${fertilisationDto.extractedSpermVialsNum}"  />
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="How many vials of sperm were used in this cycle?" mandatory="true"/>
                    <iais:value width="7" cssClass="col-md-7">
                        <iais:input maxLength="2" type="text" name="usedSpermVialsNum" value="${fertilisationDto.usedSpermVialsNum}" />
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="AR Techniques Used" mandatory="true"/>
                    <iais:value width="7" cssClass="col-md-7">
                        <c:forEach items="${arTechniquesUseds}" var="arTechniquesUsed">
                            <c:set var="arTechniquesUsedCode" value="${arTechniquesUsed.code}"/>
                            <div class="form-check col-xs-12" >
                                <input class="form-check-input" type="checkbox"
                                       name="arTechniquesUsed"
                                       value="${arTechniquesUsedCode}"
                                       id="arTechniquesUsedCheck${arTechniquesUsedCode}"
                                       <c:forEach var="atuObj" items="${fertilisationDto.atuList}">
                                        <c:if test="${atuObj == arTechniquesUsedCode}">checked</c:if>
                                       </c:forEach>
                                       aria-invalid="false">
                                <label class="form-check-label"
                                       for="arTechniquesUsedCheck${arTechniquesUsedCode}"><span
                                        class="check-square"></span>${arTechniquesUsed.codeValue}</label>
                            </div>
                        </c:forEach>
                        <span class="error-msg" name="iaisErrorMsg" id="error_arTechniquesUsed"></span>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="No. of Fresh Oocytes Inseminated" />
                    <iais:value width="7" cssClass="col-md-7">
                        <iais:input maxLength="2" type="text" name="freshOocytesInseminatedNum" value="${fertilisationDto.freshOocytesInseminatedNum}" />
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="No. of Fresh Oocytes Microinjected"/>
                    <iais:value width="7" cssClass="col-md-7">
                        <iais:input maxLength="2" type="text" name="freshOocytesMicroInjectedNum" value="${fertilisationDto.freshOocytesMicroInjectedNum}" />
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="No. of Fresh Oocytes Used for GIFT" />
                    <iais:value width="7" cssClass="col-md-7">
                        <iais:input maxLength="2" type="text" name="freshOocytesGiftNum" value="${fertilisationDto.freshOocytesGiftNum}" />
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="No. of Fresh Oocytes Used for ZIFT" />
                    <iais:value width="7" cssClass="col-md-7">
                        <iais:input maxLength="2" type="text" name="freshOocytesZiftNum" value="${fertilisationDto.freshOocytesZiftNum}" />
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="No. of Thawed Oocytes Inseminated"/>
                    <iais:value width="7" cssClass="col-md-7">
                        <iais:input maxLength="2" type="text" name="thawedOocytesInseminatedNum" value="${fertilisationDto.thawedOocytesInseminatedNum}"  />
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="No. of Thawed Oocytes Microinjected" />
                    <iais:value width="7" cssClass="col-md-7">
                        <iais:input maxLength="2" type="text" name="thawedOocytesMicroinjectedNum"  value="${fertilisationDto.thawedOocytesMicroinjectedNum}" />
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="No. of Thawed Oocytes Used for GIFT" />
                    <iais:value width="7" cssClass="col-md-7">
                        <iais:input maxLength="2" type="text" name="thawedOocytesGiftNum" value="${fertilisationDto.thawedOocytesGiftNum}"  />
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="No. of Thawed Oocytes Used for ZIFT" />
                    <iais:value width="7" cssClass="col-md-7">
                        <iais:input maxLength="2" type="text" name="thawedOocytesZiftNum" value="${fertilisationDto.thawedOocytesZiftNum}"  />
                    </iais:value>
                </iais:row>
            </div>
        </div>
    </div>
</div>