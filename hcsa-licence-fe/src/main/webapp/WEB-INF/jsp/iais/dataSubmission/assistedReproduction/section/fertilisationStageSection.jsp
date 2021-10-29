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
                                       <c:if test="">checked</c:if>
                                       aria-invalid="false">
                                <label class="form-check-label"
                                       for="sourceOfSemenCheck${sourceOfSemenCode}"><span
                                        class="check-square"></span>${sourceOfSemen.codeValue}</label>
                            </div>

                        </c:forEach>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="How many vials of sperm were extracted?" mandatory="true"/>
                    <iais:value width="7" cssClass="col-md-7">
                        <iais:input maxLength="2" type="text" name="extractedSpermVialsNum"  />
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="How many vials of sperm were used in this cycle?" mandatory="true"/>
                    <iais:value width="7" cssClass="col-md-7">
                        <iais:input maxLength="2" type="text" name="usedSpermVialsNum"  />
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
                                       <c:if test="">checked</c:if>
                                       aria-invalid="false">
                                <label class="form-check-label"
                                       for="arTechniquesUsedCheck${arTechniquesUsedCode}"><span
                                        class="check-square"></span>${arTechniquesUsed.codeValue}</label>
                            </div>
                        </c:forEach>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="No. of Fresh Oocytes Inseminated" />
                    <iais:value width="7" cssClass="col-md-7">
                        <iais:input maxLength="2" type="text" name="freshOocytesInseminatedNum" />
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="No. of Fresh Oocytes Microinjected"/>
                    <iais:value width="7" cssClass="col-md-7">
                        <iais:input maxLength="2" type="text" name="freshOocytesMicroinjectedNum"  />
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="No. of Fresh Oocytes Used for GIFT" />
                    <iais:value width="7" cssClass="col-md-7">
                        <iais:input maxLength="2" type="text" name="freshOocytesGiftNum"  />
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="No. of Fresh Oocytes Used for ZIFT" mandatory="true"/>
                    <iais:value width="7" cssClass="col-md-7">
                        <iais:input maxLength="2" type="text" name="freshOocytesZiftNum" />
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="No. of Thawed Oocytes Inseminated"/>
                    <iais:value width="7" cssClass="col-md-7">
                        <iais:input maxLength="2" type="text" name="thawedOocytesInseminatedNum"  />
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="No. of Thawed Oocytes Microinjected" />
                    <iais:value width="7" cssClass="col-md-7">
                        <iais:input maxLength="2" type="text" name="thawedOocytesMicroinjectedNum"  />
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="No. of Thawed Oocytes Used for GIFT" />
                    <iais:value width="7" cssClass="col-md-7">
                        <iais:input maxLength="2" type="text" name="thawedOocytesGiftNum"  />
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="No. of Thawed Oocytes Used for ZIFT" />
                    <iais:value width="7" cssClass="col-md-7">
                        <iais:input maxLength="2" type="text" name="thawedOocytesZiftNum" />
                    </iais:value>
                </iais:row>
            </div>
        </div>
    </div>
</div>