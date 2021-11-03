<div class="panel panel-default" style="${arCycleStageDto.usedDonorOocyte ?  '' : 'display: none;'}">
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
                <iais:row cssClass="usedDonorOocyteControlClass yesUsedDonorOocyteControl">
                    <iais:field width="5" value="Please Indicate" />
                    <iais:value width="7" cssClass="col-md-7" display="true">
                        <c:forEach items="${arDonorDto.pleaseIndicateValues}" var="pleaseIndicateValue" varStatus="status">
                            <c:if test="${status.index != 0}"><br></c:if> <iais:code code="${pleaseIndicateValue}"/>
                        </c:forEach>
                    </iais:value>
                </iais:row>

                 <iais:row>
                     <iais:field width="5" value="Donor" />
                     <iais:value width="7" cssClass="col-md-7" display="true">
                         <c:out value="${arDonorDto.arDonorIndex+1}"/>
                     </iais:value>
                 </iais:row>

                 <iais:row cssClass="usedDonorOocyteControlClass yesUsedDonorOocyteControl">
                     <iais:field width="5" value="Is this a Directed Donation?" />
                     <iais:value width="7" cssClass="col-md-7" display="true">
                         <c:out value="${arDonorDto.directedDonation ? 'Yes' : 'No'}"/>
                     </iais:value>
                 </iais:row>

                 <c:if test="${arDonorDto.directedDonation}">
                     <iais:row cssClass="usedDonorOocyteControlClass yesUsedDonorOocyteControl">
                         <iais:field width="5" value="ID No." />
                         <iais:value width="3" cssClass="col-md-3" display="true">
                             <iais:code code="${arDonorDto.idType}"/>
                         </iais:value>
                         <iais:value width="4" cssClass="col-md-4" display="true">
                             <c:out value="${arDonorDto.idNumber}" />
                         </iais:value>
                     </iais:row>
                 </c:if>

                 <c:if test="${!arDonorDto.directedDonation}">
                 <iais:row cssClass="usedDonorOocyteControlClass" id="donorSampleCodeId${arDonorIndex}Row">
                     <iais:field width="5" value="Donor Sample Code / ID" />
                     <iais:value width="7" cssClass="col-md-7" display="true">
                         <c:out value="${arDonorDto.donorSampleCode}" />
                     </iais:value>
                 </iais:row>

                 <iais:row  cssClass="usedDonorOocyteControlClass" id="source${arDonorIndex}Row" style="${arDonorDto.directedDonation ? 'display: none;' : ''}">
                     <iais:field width="5" value="Source (i.e. AR Centre or Bank Name)" />
                     <iais:value width="7" cssClass="col-md-7" display="true">
                         <iais:code code="${arDonorDto.source}"/>
                     </iais:value>
                 </iais:row>

                 <c:if test="${arDonorDto.source == 'Others'}">
                 <iais:row cssClass="usedDonorOocyteControlClass" id="otherSource${arDonorIndex}Row" >
                     <iais:field width="5" value="Source (Others)" />
                     <iais:value width="7" cssClass="col-md-7" display="true">
                         <c:out value="${arDonorDto.otherSource}" />
                     </iais:value>
                 </iais:row>
                 </c:if>
                 </c:if>

                 <iais:row cssClass="usedDonorOocyteControlClass yesUsedDonorOocyteControl">
                     <iais:field width="5" value="Donor's Age at Donation" />
                     <iais:value width="7" cssClass="col-md-7" display="true">
                         <c:out value="${arDonorDto.age}" />
                     </iais:value>
                 </iais:row>
                 <h3></h3>
             </div>
        </c:forEach>
     </div>
     </div>
</div>