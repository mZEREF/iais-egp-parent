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
                <h3>
                    <label ><c:out value="${arSuperDataSubmissionDto.patientInfoDto.patient.name}"/></label>
                    <span style="font-weight:normal"><c:out value="(${arSuperDataSubmissionDto.patientInfoDto.patient.idNumber})"/>
                    </span>
                </h3>
                <iais:row>
                    <iais:field width="5" value="Source of Semen" mandatory="true"/>
                    <iais:value width="7" cssClass="col-md-7">
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
                        <input type="number" oninput="if(value.length>2)value=value.slice(0,2)" style="margin-bottom: 0px;"
                               name="extractedSpermVialsNum" value="${fertilisationDto.extractedSpermVialsNum}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="How many vials of sperm were used in this cycle?" mandatory="true"/>
                    <iais:value width="7" cssClass="col-md-7">
                        <input type="number" oninput="if(value.length>2)value=value.slice(0,2)" style="margin-bottom: 0px;"
                               name="usedSpermVialsNum" value="${fertilisationDto.usedSpermVialsNum}"/>
                        <span class="error-msg" name="iaisErrorMsg" id="error_usedSpermVialsNum"></span>
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
                    <iais:field width="5" value="No. of Fresh Oocytes Inseminated" mandatory="true" />
                    <iais:value width="7" cssClass="col-md-7">
                        <input type="number" oninput="if(value.length>2)value=value.slice(0,2)" style="margin-bottom: 0px;"
                               name="freshOocytesInseminatedNum" value="${fertilisationDto.freshOocytesInseminatedNum}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="No. of Fresh Oocytes Microinjected" mandatory="true"/>
                    <iais:value width="7" cssClass="col-md-7">
                        <input type="number" oninput="if(value.length>2)value=value.slice(0,2)" style="margin-bottom: 0px;"
                               name="freshOocytesMicroInjectedNum" value="${fertilisationDto.freshOocytesMicroInjectedNum}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="No. of Fresh Oocytes Used for GIFT" mandatory="true"/>
                    <iais:value width="7" cssClass="col-md-7">
                        <input type="number" oninput="if(value.length>2)value=value.slice(0,2)" style="margin-bottom: 0px;"
                               name="freshOocytesGiftNum" value="${fertilisationDto.freshOocytesGiftNum}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="No. of Fresh Oocytes Used for ZIFT" mandatory="true"/>
                    <iais:value width="7" cssClass="col-md-7">
                        <input type="number" oninput="if(value.length>2)value=value.slice(0,2)" style="margin-bottom: 0px;"
                               name="freshOocytesZiftNum" value="${fertilisationDto.freshOocytesZiftNum}"/>
                        <span class="error-msg" name="iaisErrorMsg" id="error_freshOocytesZiftNum"></span>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="No. of Thawed Oocytes Inseminated" mandatory="true"/>
                    <iais:value width="7" cssClass="col-md-7">
                        <input type="number" oninput="if(value.length>2)value=value.slice(0,2)" style="margin-bottom: 0px;"
                               name="thawedOocytesInseminatedNum" value="${fertilisationDto.thawedOocytesInseminatedNum}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="No. of Thawed Oocytes Microinjected" mandatory="true" />
                    <iais:value width="7" cssClass="col-md-7">
                        <input type="number" oninput="if(value.length>2)value=value.slice(0,2)" style="margin-bottom: 0px;"
                               name="thawedOocytesMicroinjectedNum" value="${fertilisationDto.thawedOocytesMicroinjectedNum}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="No. of Thawed Oocytes Used for GIFT" mandatory="true" />
                    <iais:value width="7" cssClass="col-md-7">
                        <input type="number" oninput="if(value.length>2)value=value.slice(0,2)" style="margin-bottom: 0px;"
                               name="thawedOocytesGiftNum" value="${fertilisationDto.thawedOocytesGiftNum}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="No. of Thawed Oocytes Used for ZIFT"  mandatory="true"/>
                    <iais:value width="7" cssClass="col-md-7">
                        <input type="number" oninput="if(value.length>2)value=value.slice(0,2)" style="margin-bottom: 0px;"
                               name="thawedOocytesZiftNum" value="${fertilisationDto.thawedOocytesZiftNum}"/>
                        <span class="error-msg" name="iaisErrorMsg" id="error_thawedOocytesZiftNum"></span>
                    </iais:value>
                </iais:row>
            </div>
        </div>
    </div>
</div>