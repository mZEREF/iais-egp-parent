     <div class="panel-heading">
         <h4 class="panel-title">
        <strong>
            Details of Donor(s)
          </strong>
       </h4>
    </div>

     <div id="arStageDetails" class="panel-collapse collapse in">
         <c:set var="arDonorDtos" value="${arCycleStageDto.arDonorDtos}"/>
         <c:forEach items="${arDonorDto}" var="arDonorDtos">
             <c:set var="arDonorIndex" value="${arDonorDto.arDonorIndex}"/>
            <div class="panel-body">
             <div class="panel-main-content form-horizontal">
                <iais:row>
                    <iais:field width="5" value="Please Indicate" mandatory="true"/>
                    <iais:value width="7" cssClass="col-md-7">

                    </iais:value>
                    <span id="error_currentArTreatment" name="iaisErrorMsg" class="error-msg"></span>
                </iais:row>

                 <iais:row>
                     <iais:field width="5" value="Donor" mandatory="true"/>
                     <iais:value width="7" cssClass="col-md-7" label="true">
                         <c:out value="${arDonorDto.donorsSize}"/>
                     </iais:value>
                 </iais:row>

                 <iais:row>
                     <iais:field width="5" value="Is this a Directed Donation?" mandatory="true"/>
                     <iais:value width="3" cssClass="col-md-3">
                         <div class="form-check">
                             <input class="form-check-input"
                                    type="radio"
                                    name="directedDonation${arDonorIndex}"
                                    value="1"
                                    id="directedDonation${arDonorIndex}RadioYes"
                                    <c:if test="${arDonorDto.directedDonation}">checked</c:if>
                                    aria-invalid="false">
                             <label class="form-check-label"
                                    for="directedDonation${arDonorIndex}RadioYes"><span
                                     class="check-circle"></span>Yes</label>
                         </div>
                     </iais:value>
                     <iais:value width="4" cssClass="col-md-4">
                         <div class="form-check">
                             <input class="form-check-input" type="radio"
                                    name="directedDonation${arDonorIndex}"
                                    value="0"
                                    id="directedDonation${arDonorIndex}RadioNo"
                                    <c:if test="${!arDonorDto.directedDonation}">checked</c:if>
                                    aria-invalid="false">
                             <label class="form-check-label"
                                    for="directedDonation${arDonorIndex}RadioNo"><span
                                     class="check-circle"></span>No</label>
                         </div>
                     </iais:value>
                 </iais:row>

                 <iais:row>
                     <iais:field width="5" value="ID No." mandatory="true"/>
                     <iais:value width="3" cssClass="col-md-3">
                         <iais:select name="idType${arDonorIndex}" firstOption="Please Select" codeCategory="CATE_ID_DS_ID_TYPE" value="${arDonorDto.idType}"
                                      cssClass="idTypeSel${arDonorIndex}"/>
                     </iais:value>
                     <iais:value width="4" cssClass="col-md-4">
                         <iais:input maxLength="20" type="text" name="idNumber${arDonorIndex}" value="${arDonorDto.idNumber}" />
                     </iais:value>
                 </iais:row>

                 <iais:row id="donorSampleCodeId${arDonorIndex}Row">
                     <iais:field width="5" value="Donor Sample Code / ID" mandatory="true"/>
                     <iais:value width="7" cssClass="col-md-7">
                         <iais:input maxLength="100" type="text" name="donorSampleCodeId${arDonorIndex}" id="donorSampleCodeId${arDonorIndex}" value="${arDonorDto.donorSampleCodeId}" />
                     </iais:value>
                 </iais:row>

                 <iais:row>
                     <iais:field width="5" value="Source (i.e. AR Centre or Bank Name)" mandatory="true"/>
                     <iais:value width="7" cssClass="col-md-7">
                         <iais:select name="source${arDonorIndex}" firstOption="Please Select" codeCategory="CATE_ID_DS_ID_TYPE" value="${arDonorDto.source}"
                                      cssClass="source${arDonorIndex}"/>
                     </iais:value>
                 </iais:row>

                 <iais:row id="otherSource${arDonorIndex}Row" >
                     <iais:field width="5" value="Source (Others)" mandatory="true"/>
                     <iais:value width="7" cssClass="col-md-7">
                         <iais:input maxLength="100" type="text" name="otherSource${arDonorIndex}" id="otherSource${arDonorIndex}" value="${arDonorDto.otherSource}" />
                     </iais:value>
                 </iais:row>

                 <iais:row>
                     <iais:field width="5" value="Donor's Age at Donation" mandatory="true"/>
                     <iais:value width="4" cssClass="col-md-4">
                         <iais:select name="donorAgeDonation${arDonorIndex}" firstOption="Please Select" codeCategory="CATE_ID_DS_ID_TYPE" value="${arDonorDto.donorAgeDonation}"
                                      cssClass="donorAgeDonation${arDonorIndex}"/>
                     </iais:value>
                     <iais:value width="3" cssClass="col-md-3" display="true">
                         <a class="validateDonor" onclick="validateDonor('${arDonorIndex}')">
                             Validate Donor
                         </a>

                     </iais:value>
                 </iais:row>
                 <iais:row>
                     <iais:value width="5" cssClass="col-md-3" display="true">
                         <a class="addDonor"  onclick="addDonor()"style="text-decoration:none;">Add Donor Details</a>
                     </iais:value>
                 </iais:row>
             </div>
             </div>
     </c:forEach>
     </div>