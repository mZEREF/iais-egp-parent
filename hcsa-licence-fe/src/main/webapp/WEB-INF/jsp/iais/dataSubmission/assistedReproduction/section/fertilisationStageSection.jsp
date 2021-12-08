<script type="text/javascript" src="<%=webroot1%>js/dataSubmission/fertillisationStageSection.js"></script>
 <div class="panel panel-default">
    <div class="panel-heading" style="padding-left: 95px;">
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
                    <iais:field width="6" value="Source of Semen" mandatory="true" cssClass="col-md-6"/>
                    <iais:value width="6" cssClass="col-md-6">
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
                    <iais:field width="6" value="How many vials of sperm were extracted?" mandatory="true" cssClass="col-md-6"/>
                    <iais:value width="6" cssClass="col-md-6">
                        <input type="number" oninput="if(value.length>2)value=value.slice(0,2)" style="margin-bottom: 0px;"
                               name="extractedSpermVialsNum" value="${fertilisationDto.extractedSpermVialsNum}"/>
                        <span class="error-msg" name="iaisErrorMsg" id="error_extractedSpermVialsNum"></span>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="6" value="How many vials of sperm were used in this cycle?" mandatory="true" cssClass="col-md-6"/>
                    <iais:value width="6" cssClass="col-md-6">
                        <input type="number" oninput="if(value.length>2)value=value.slice(0,2)" style="margin-bottom: 0px;"
                               name="usedSpermVialsNum" value="${fertilisationDto.usedSpermVialsNum}"/>
                        <span class="error-msg" name="iaisErrorMsg" id="error_usedSpermVialsNum"></span>
                    </iais:value>
                </iais:row>
                <iais:row>

                    <iais:field width="6" value="AR Techniques Used" mandatory="true" cssClass="col-md-6"/>
                    <iais:value width="6" cssClass="col-md-6">
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
                    <label class="col-xs-6 col-md-6 control-label">No. of Fresh Oocytes Inseminated
                             <span id="ivfMandatory1" class="mandatory">
                            <c:forEach items="${fertilisationDto.atuList}" var="arTechniquesUsed">
                                <c:if test="${arTechniquesUsed =='AR_ATU_001'}">*</c:if>
                            </c:forEach>
                        </span>
                    </label>
                    <%--<iais:field width="5" value="No. of Fresh Oocytes Inseminated" mandatory="true"/>--%>
                    <iais:value width="6" cssClass="col-md-6">
                        <input type="number" oninput="if(value.length>2)value=value.slice(0,2)" style="margin-bottom: 0px;"
                               name="freshOocytesInseminatedNum" value="${fertilisationDto.freshOocytesInseminatedNum}"/>
                        <span class="error-msg" name="iaisErrorMsg" id="error_freshOocytesInseminatedNum"></span>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <label class="col-xs-6 col-md-6 control-label">No. of Fresh Oocytes Microinjected&nbsp;
                        <span id="icsiMandatory1" class="mandatory"><c:forEach items="${fertilisationDto.atuList}" var="arTechniquesUsed">
                            <c:if test="${arTechniquesUsed =='AR_ATU_002'}">*</c:if>
                        </c:forEach></span>
                    </label>
                    <%--<iais:field width="5" value="No. of Fresh Oocytes Microinjected" mandatory="true"/>--%>
                    <iais:value width="6" cssClass="col-md-6">
                        <input type="number" oninput="if(value.length>2)value=value.slice(0,2)" style="margin-bottom: 0px;"
                               name="freshOocytesMicroInjectedNum" value="${fertilisationDto.freshOocytesMicroInjectedNum}"/>
                        <span class="error-msg" name="iaisErrorMsg" id="error_freshOocytesMicroInjectedNum"></span>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <label class="col-xs-6 col-md-6 control-label">No. of Fresh Oocytes Used for GIFT&nbsp;
                        <span id="giftMandatory1" class="mandatory"><c:forEach items="${fertilisationDto.atuList}" var="arTechniquesUsed">
                            <c:if test="${arTechniquesUsed =='AR_ATU_003'}">*</c:if>
                        </c:forEach></span>
                    </label>
                   <%-- <iais:field width="5" value="No. of Fresh Oocytes Used for GIFT" mandatory="true"/>--%>
                    <iais:value width="6" cssClass="col-md-6">
                        <input type="number" oninput="if(value.length>2)value=value.slice(0,2)" style="margin-bottom: 0px;"
                               name="freshOocytesGiftNum" value="${fertilisationDto.freshOocytesGiftNum}"/>
                        <span class="error-msg" name="iaisErrorMsg" id="error_freshOocytesGiftNum"></span>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <label class="col-xs-6 col-md-6 control-label">No. of Fresh Oocytes Used for ZIFT&nbsp;
                        <span id="ziftMandatory1" class="mandatory"><c:forEach items="${fertilisationDto.atuList}" var="arTechniquesUsed">
                            <c:if test="${arTechniquesUsed =='AR_ATU_004'}">*</c:if>
                        </c:forEach></span>
                    </label>
                    <%--<iais:field width="5" value="No. of Fresh Oocytes Used for ZIFT" mandatory="true"/>--%>
                    <iais:value width="6" cssClass="col-md-6">
                        <input type="number" oninput="if(value.length>2)value=value.slice(0,2)" style="margin-bottom: 0px;"
                               name="freshOocytesZiftNum" value="${fertilisationDto.freshOocytesZiftNum}"/>
                        <span class="error-msg" name="iaisErrorMsg" id="error_freshOocytesZiftNum"></span>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <label class="col-xs-6 col-md-6 control-label">No. of Thawed Oocytes Inseminated&nbsp;
                        <span id="ivfMandatory2" class="mandatory"><c:forEach items="${fertilisationDto.atuList}" var="arTechniquesUsed">
                            <c:if test="${arTechniquesUsed =='AR_ATU_001'}">*</c:if>
                        </c:forEach></span>
                    </label>
                    <%--<iais:field width="5" value="No. of Thawed Oocytes Inseminated" mandatory="true"/>--%>
                    <iais:value width="6" cssClass="col-md-6">
                        <input type="number" oninput="if(value.length>2)value=value.slice(0,2)" style="margin-bottom: 0px;"
                               name="thawedOocytesInseminatedNum" value="${fertilisationDto.thawedOocytesInseminatedNum}"/>
                        <span class="error-msg" name="iaisErrorMsg" id="error_thawedOocytesInseminatedNum"></span>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <label class="col-xs-6 col-md-6 control-label">No. of Thawed Oocytes Microinjected&nbsp;
                        <span id="icsiMandatory2" class="mandatory"><c:forEach items="${fertilisationDto.atuList}" var="arTechniquesUsed">
                            <c:if test="${arTechniquesUsed =='AR_ATU_002'}">*</c:if>
                        </c:forEach></span>
                    </label>
                    <%--<iais:field width="5" value="No. of Thawed Oocytes Microinjected" mandatory="true" />--%>
                    <iais:value width="6" cssClass="col-md-6">
                        <input type="number" oninput="if(value.length>2)value=value.slice(0,2)" style="margin-bottom: 0px;"
                               name="thawedOocytesMicroinjectedNum" value="${fertilisationDto.thawedOocytesMicroinjectedNum}"/>
                        <span class="error-msg" name="iaisErrorMsg" id="error_thawedOocytesMicroinjectedNum"></span>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <label class="col-xs-6 col-md-6 control-label">No. of Thawed Oocytes Used for GIFT&nbsp;
                        <span id="giftMandatory2" class="mandatory"><c:forEach items="${fertilisationDto.atuList}" var="arTechniquesUsed">
                            <c:if test="${arTechniquesUsed =='AR_ATU_003'}">*</c:if>
                        </c:forEach></span>
                    </label>
                    <%--<iais:field width="5" value="No. of Thawed Oocytes Used for GIFT" mandatory="true" />--%>
                    <iais:value width="6" cssClass="col-md-6">
                        <input type="number" oninput="if(value.length>2)value=value.slice(0,2)" style="margin-bottom: 0px;"
                               name="thawedOocytesGiftNum" value="${fertilisationDto.thawedOocytesGiftNum}"/>
                        <span class="error-msg" name="iaisErrorMsg" id="error_thawedOocytesGiftNum"></span>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <label class="col-xs-6 col-md-6 control-label">No. of Thawed Oocytes Used for ZIFT&nbsp;
                        <span id="ziftMandatory2" class="mandatory">
                            <c:forEach items="${fertilisationDto.atuList}" var="arTechniquesUsed">
                                 <c:if test="${arTechniquesUsed =='AR_ATU_004'}">*</c:if>
                            </c:forEach>
                        </span>
                    </label>
                    <%--<iais:field width="5" value="No. of Thawed Oocytes Used for ZIFT"  mandatory="true"/>--%>
                    <iais:value width="6" cssClass="col-md-6">
                        <input type="number" oninput="if(value.length>2)value=value.slice(0,2)" style="margin-bottom: 0px;"
                               name="thawedOocytesZiftNum" value="${fertilisationDto.thawedOocytesZiftNum}"/>
                        <span class="error-msg" name="iaisErrorMsg" id="error_thawedOocytesZiftNum"></span>
                    </iais:value>
                </iais:row>

            </div>
        </div>
    </div>
</div>
 <script>
     $(document).ready(function () {
         $('input[name=arTechniquesUsed]').change(function () {
             if($('#arTechniquesUsedCheckAR_ATU_001').prop('checked')) {
                 $('#ivfMandatory1').text('*');

                 $('#ivfMandatory2').text('*');
             }else {
                 $('#ivfMandatory1').text('');

                 $('#ivfMandatory2').text('');
             }
             if($('#arTechniquesUsedCheckAR_ATU_002').prop('checked')) {
                 $('#icsiMandatory1').text('*');

                 $('#icsiMandatory2').text('*');
             }else {
                 $('#icsiMandatory1').text('');

                 $('#icsiMandatory2').text('');
             }
             if($('#arTechniquesUsedCheckAR_ATU_003').prop('checked')) {
                 $('#giftMandatory1').text('*');

                 $('#giftMandatory2').text('*');
             }else {
                 $('#giftMandatory1').text('');

                 $('#giftMandatory2').text('');
             }
             if($('#arTechniquesUsedCheckAR_ATU_004').prop('checked')) {
                 $('#ziftMandatory1').text('*');

                 $('#ziftMandatory2').text('*');
             }else {
                 $('#ziftMandatory1').text('');

                 $('#ziftMandatory2').text('');
             }
         });
     });
 </script>