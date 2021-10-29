<div class="panel panel-default">
     <div class="panel-heading">
         <h4  class="panel-title" data-toggle="collapse" href="#arDonorDtoDetails">
        <strong>
            Details of Donor(s)
          </strong>
       </h4>
    </div>

     <div id="arDonorDtoDetails" class="panel-collapse collapse in">
         <div class="panel-body">
         <c:set var="arDonorDtos" value="${arCycleStageDto.arDonorDtos}"/>
         <c:forEach items="${arDonorDtos}" var="arDonorDto">
             <c:set var="arDonorIndex" value="${arDonorDto.arDonorIndex}"/>
             <div class="panel-main-content form-horizontal">
                <iais:row>
                    <iais:field width="5" value="Please Indicate" mandatory="true"/>
                    <iais:value width="7" cssClass="col-md-7">
                        <c:forEach items="${arDonorDto.pleaseIndicateValues}" var="pleaseIndicateValue" varStatus="status">
                            <c:if test="${status.index != 0}"><br></c:if> <iais:code code="${pleaseIndicateValue}"/>
                        </c:forEach>
                    </iais:value>
                    <span id="error_currentArTreatment${arDonorDto.arDonorIndex}" name="iaisErrorMsg" class="error-msg"></span>
                </iais:row>

                 <iais:row>
                     <iais:field width="5" value="Donor" mandatory="true"/>
                     <iais:value width="7" cssClass="col-md-7" label="true">
                         <c:out value="${arDonorDto.arDonorIndex+1}"/>
                     </iais:value>
                 </iais:row>

                 <iais:row>
                     <iais:field width="5" value="Is this a Directed Donation?" mandatory="true"/>
                     <iais:value width="7" cssClass="col-md-7">
                         <c:out value="${arDonorDto.directedDonation ? 'Yes' : 'No'}"/>
                     </iais:value>
                 </iais:row>

                 <iais:row>
                     <iais:field width="5" value="ID No." mandatory="true"/>
                     <iais:value width="3" cssClass="col-md-3">
                         <iais:code code="${arDonorDto.idType}"/>
                     </iais:value>
                     <iais:value width="4" cssClass="col-md-4">
                         <c:out value="${arDonorDto.idNumber}" />
                     </iais:value>
                 </iais:row>

                 <iais:row id="donorSampleCodeId${arDonorIndex}Row">
                     <iais:field width="5" value="Donor Sample Code / ID" mandatory="true"/>
                     <iais:value width="7" cssClass="col-md-7">
                         <c:out value="${arDonorDto.donorSampleCodeId}" />
                     </iais:value>
                 </iais:row>

                 <iais:row id="source${arDonorIndex}Row" style="${arDonorDto.directedDonation ? 'display: none;' : ''}">
                     <iais:field width="5" value="Source (i.e. AR Centre or Bank Name)" mandatory="true"/>
                     <iais:value width="7" cssClass="col-md-7">
                         <iais:code code="${arDonorDto.source}"/>
                     </iais:value>
                 </iais:row>

                 <iais:row id="otherSource${arDonorIndex}Row" >
                     <iais:field width="5" value="Source (Others)" mandatory="true"/>
                     <iais:value width="7" cssClass="col-md-7">
                         <c:out value="${arDonorDto.otherSource}" />
                     </iais:value>
                 </iais:row>

                 <iais:row>
                     <iais:field width="5" value="Donor's Age at Donation" mandatory="true"/>
                     <iais:value width="7" cssClass="col-md-7">
                         <c:out value="${arDonorDto.donorAgeDonation}" />
                     </iais:value>
                 </iais:row>
                 <h3></h3>
             </div>
        </c:forEach>
     </div>
     </div>
</div>