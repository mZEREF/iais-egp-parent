<div class="panel panel-default" style="${!empty donorDtos ?  '' : 'display: none;'}">
     <div class="panel-heading ${headingSign}">
         <h4  class="panel-title" >
        <a href="#donorDtoDetails" data-toggle="collapse"  >
            Details of Donor(s)
          </a>
       </h4>
    </div>

     <div id="donorDtoDetails" class="panel-collapse collapse in">
         <div class="panel-body">
         <c:forEach items="${donorDtos}" var="donorDto">
             <c:set var="arDonorIndex" value="${donorDto.arDonorIndex}"/>
             <div class="panel-main-content form-horizontal">
                <iais:row cssClass="usedDonorOocyteControlClass yesUsedDonorOocyteControl">
                    <iais:field width="5" value="Please Indicate" />
                    <iais:value width="7" cssClass="col-md-7" display="true">
                        <c:forEach items="${donorDto.pleaseIndicateValues}" var="pleaseIndicateValue" varStatus="status">
                            <c:if test="${status.index != 0}"><br></c:if> <iais:code code="${pleaseIndicateValue}"/>
                        </c:forEach>
                    </iais:value>
                </iais:row>

                 <iais:row>
                     <iais:field width="5" value="Donor ${donorDto.arDonorIndex+1}" />
                     <iais:value width="7" cssClass="col-md-7" display="true">
                     </iais:value>
                 </iais:row>

                 <iais:row cssClass="usedDonorOocyteControlClass yesUsedDonorOocyteControl">
                     <iais:field width="5" value="Is this a Directed Donation?" />
                     <iais:value width="7" cssClass="col-md-7" display="true">
                         <c:out value="${donorDto.directedDonation ? 'Yes' : 'No'}"/>
                     </iais:value>
                 </iais:row>

                 <c:if test="${donorDto.directedDonation}">
                     <iais:row cssClass="usedDonorOocyteControlClass yesUsedDonorOocyteControl">
                         <iais:field width="5" value="ID Type" />
                         <iais:value width="7" cssClass="col-md-7" display="true">
                             <iais:code code="${donorDto.idType}"/>
                         </iais:value>
                     </iais:row>
                     <iais:row cssClass="usedDonorOocyteControlClass yesUsedDonorOocyteControl">
                         <iais:field width="5" value="ID No." />
                         <iais:value width="7" cssClass="col-md-7" display="true">
                             <c:out value="${donorDto.idNumber}" />
                         </iais:value>
                     </iais:row>
                 </c:if>

                 <c:if test="${!donorDto.directedDonation}">

                     <iais:row cssClass="usedDonorOocyteControlClass" id="donorSampleCodeId${arDonorIndex}Row" >
                         <iais:field width="5" value="Donor Sample Code / ID" />
                         <iais:value width="7" cssClass="col-md-3" display="true">
                             <iais:optionText value="${donorDto.idType}" selectionOptions="donorSampleDropDown"/>
                         </iais:value>
                     </iais:row>
                     <iais:row>
                         <iais:field width="5" value="" />
                         <iais:value width="7" cssClass="col-md-4" display="true">
                             <c:out value="${donorDto.donorSampleCode}" />
                         </iais:value>
                     </iais:row>
                 <iais:row  cssClass="usedDonorOocyteControlClass" id="source${arDonorIndex}Row" style="${donorDto.directedDonation ? 'display: none;' : ''}">
                     <iais:field width="5" value="Source (i.e. AR Centre or Bank Name)" />
                     <iais:value width="7" cssClass="col-md-7" display="true">
                         <iais:optionText value="${donorDto.source}" selectionOptions="donorSourseDropDown"/>
                     </iais:value>
                 </iais:row>

                 <c:if test="${donorDto.source == DataSubmissionConsts.AR_SOURCE_OTHER}">
                 <iais:row cssClass="usedDonorOocyteControlClass" id="otherSource${arDonorIndex}Row" >
                     <iais:field width="5" value="Source (Others)" />
                     <iais:value width="7" cssClass="col-md-7" display="true">
                         <c:out value="${donorDto.otherSource}" />
                     </iais:value>
                 </iais:row>
                 </c:if>
                 </c:if>

                 <c:if test="${not empty donorDto.ageList}">
                 <iais:row cssClass="usedDonorOocyteControlClass yesUsedDonorOocyteControl">
                     <iais:field width="5" value="Donor's Age at Donation" />
                     <iais:value width="7" cssClass="col-md-7" display="true">
                         <c:forEach items="${donorDto.ageList}" var="age">
                             <c:if test="${age.value == donorDto.age}">
                                 <c:out value="${age.text}" />
                             </c:if>
                         </c:forEach>
                     </iais:value>
                 </iais:row>
                 </c:if>


                 <c:if test="${preViewAr eq '1'}">
                     <iais:row cssClass="usedDonorOocyteControlClass yesUsedDonorOocyteControl">
                         <iais:field width="5" value="Donor's Age at Donation" />
                         <iais:value width="7" cssClass="col-md-7" display="true">
                             <c:out value="${donorDto.age}" />
                         </iais:value>
                     </iais:row>
                 </c:if>

                 <c:if test="${donorDto.directedDonation}">
                     <iais:row id="relation${arDonorIndex}Row">
                         <iais:field width="5" value="Donor relation to patient" />
                         <iais:value width="7" cssClass="col-md-7" display="true">
                             <iais:code code="${donorDto.relation}"/>
                         </iais:value>
                     </iais:row>
                 </c:if>
                 <h3></h3>
             </div>
        </c:forEach>
             <c:if test="${!empty donorDtos}">
             <%@include file="../common/patientInventoryTable.jsp" %>
             </c:if>
     </div>
     </div>
</div>