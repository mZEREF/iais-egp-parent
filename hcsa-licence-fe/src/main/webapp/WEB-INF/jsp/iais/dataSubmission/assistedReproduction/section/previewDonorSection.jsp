<div class="panel panel-default" style="${!empty donorDtos ?  '' : 'display: none;'}">
     <div class="panel-heading ${headingSign}">
         <h4  class="panel-title" >
        <a class="collapsed" href="#donorDtoDetails" data-toggle="collapse"  >
            Details of Donor(s)
          </a>
       </h4>
    </div>

     <div id="donorDtoDetails" class="panel-collapse collapse">
         <div class="panel-body">
         <c:forEach items="${donorDtos}" var="donorDto">
             <c:set var="arDonorIndex" value="${donorDto.arDonorIndex}"/>
             <div class="panel-main-content form-horizontal">

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
                         <iais:field width="5" value="ID No." info="${donorDto.donorUseSize >= donorResultSize ? donorMessageTip : ''}"/>
                         <iais:value width="7" cssClass="col-md-7" display="true">
                             <c:out value="${donorDto.idNumber}" />
                         </iais:value>
                     </iais:row>
                 </c:if>

                 <c:if test="${!donorDto.directedDonation}">

                     <iais:row cssClass="usedDonorOocyteControlClass" id="donorSampleCodeId${arDonorIndex}Row" >
                         <iais:field width="5" value="Donor Sample Code / ID" info="${donorDto.donorUseSize >= donorResultSize ? donorMessageTip : ''}" />
                         <iais:value width="7" cssClass="col-md-3" display="true">
                             <iais:optionText value="${donorDto.idType}" selectionOptions="donorSampleDropDown"/>
                         </iais:value>
                     </iais:row>
                     <iais:row>
                         <iais:field width="5"   value="" />
                         <iais:value width="7" cssClass="col-md-4" display="true">
                             <c:out value="${donorDto.donorSampleCode}" />
                         </iais:value>
                     </iais:row>
                 <iais:row  cssClass="usedDonorOocyteControlClass" id="source${arDonorIndex}Row" style="${donorDto.directedDonation ? 'display: none;' : ''}">
                     <iais:field width="5" value="Source (i.e. AR Centre or Bank Name)" />
                     <iais:value width="7" cssClass="col-md-7" display="true">
                         <iais:optionText value="${donorDto.sourceAddress}"/>
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


                 <c:if test="${not empty donorDto.donorSampleAgeDtos && donorFrom == 'ar'}">
                     <iais:row cssClass="usedDonorOocyteControlClass">
                         <iais:field width="5" value="Donor Sample Used" />
                         <iais:value width="7" cssClass="col-md-7" display="true">
                             <c:if test="${donorDto.donorIndicateFresh}">Donor's Fresh Oocyte(s) used</br></c:if>
                             <c:if test="${donorDto.donorIndicateFrozen}">Donor's Frozen oocyte(s) used</br></c:if>
                             <c:if test="${donorDto.donorIndicateEmbryo}">Donor's Embryo(s) used</br></c:if>
                             <c:if test="${donorDto.donorIndicateFrozenSperm || arDonorDtodonorIndicateFreshSperm}">Donor's Sperm(s) used</c:if>
                         </iais:value>
                     </iais:row>


                 <c:if test="${donorDto.donorIndicateFresh}">
                     <iais:row cssClass="usedDonorOocyteControlClass yesUsedDonorOocyteControl">
                         <iais:field width="5" value="Use Donor's Fresh Oocytes Collected At Age" />
                         <iais:value width="7" cssClass="col-md-7" display="true">
                             <c:forEach items="${donorDto.ageList}" var="age">
                                 <c:if test="${age.value == donorDto.age}">
                                     <c:out value="${age.text}" />
                                 </c:if>
                             </c:forEach>
                         </iais:value>
                     </iais:row>
                 </c:if>

                 <c:if test="${donorDto.donorIndicateFrozen}">
                     <iais:row cssClass="usedDonorOocyteControlClass yesUsedDonorOocyteControl">
                         <iais:field width="5" value="Use Donor's Frozen Oocytes Collected At Age" />
                         <iais:value width="7" cssClass="col-md-7" display="true">
                             <c:forEach items="${donorDto.frozenOocyteAgeList}" var="frozenOocyteAge">
                                 <c:if test="${frozenOocyteAge.value == donorDto.frozenOocyteAge}">
                                     <c:out value="${frozenOocyteAge.text}" />
                                 </c:if>
                             </c:forEach>
                         </iais:value>
                     </iais:row>
                 </c:if>

                 <c:if test="${donorDto.donorIndicateEmbryo}">
                     <iais:row cssClass="usedDonorOocyteControlClass yesUsedDonorOocyteControl">
                         <iais:field width="5" value="Use Donor's Frozen Embryos Collected At Age" />
                         <iais:value width="7" cssClass="col-md-7" display="true">
                             <c:forEach items="${donorDto.frozenEmbryoAgeList}" var="frozenEmbryoAge">
                                 <c:if test="${frozenEmbryoAge.value == donorDto.frozenEmbryoAge}">
                                     <c:out value="${frozenEmbryoAge.text}" />
                                 </c:if>
                             </c:forEach>
                         </iais:value>
                     </iais:row>
                 </c:if>

                 <c:if test="${donorDto.donorIndicateFrozenSperm}">
                     <iais:row cssClass="usedDonorOocyteControlClass yesUsedDonorOocyteControl">
                         <iais:field width="5" value="Use Donor's Frozen Sperm Collected At Age" />
                         <iais:value width="7" cssClass="col-md-7" display="true">
                             <c:forEach items="${donorDto.frozenSpermAgeList}" var="frozenSpermAge">
                                 <c:if test="${frozenSpermAge.value == donorDto.frozenSpermAge}">
                                     <c:out value="${frozenSpermAge.text}" />
                                 </c:if>
                             </c:forEach>
                         </iais:value>
                     </iais:row>
                 </c:if>

                 <c:if test="${donorDto.donorIndicateFreshSperm}">
                     <iais:row cssClass="usedDonorOocyteControlClass yesUsedDonorOocyteControl">
                         <iais:field width="5" value="Use Donor's Fresh Sperm Collected At Age" />
                         <iais:value width="7" cssClass="col-md-7" display="true">
                             <c:forEach items="${donorDto.freshSpermAgeList}" var="freshSpermAge">
                                 <c:if test="${freshSpermAge.value == donorDto.freshSpermAge}">
                                     <c:out value="${freshSpermAge.text}" />
                                 </c:if>
                             </c:forEach>
                         </iais:value>
                     </iais:row>
                 </c:if>
                 </c:if>

                 <c:if test="${donorFrom == 'iui'}">
                     <iais:row>
                         <iais:field width="5" value="Donor sample used"/>
                         <iais:value width="7" cssClass="col-md-7" display="true">
                             <iais:optionText value="Donor's Sperm(s) used"/>
                         </iais:value>
                     </iais:row>

                     <iais:row id="age${arDonorIndex}Row">
                         <iais:field width="5" value="Age of donor when sperm was collected"/>
                         <iais:value width="7" cssClass="col-md-7" display="true">
                             <c:forEach items="${donorDto.frozenSpermAgeList}" var="frozenSpermAge">
                                 <c:if test="${frozenSpermAge.value == donorDto.frozenSpermAge}">
                                     <c:out value="${frozenSpermAge.text}" />
                                 </c:if>
                             </c:forEach>
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