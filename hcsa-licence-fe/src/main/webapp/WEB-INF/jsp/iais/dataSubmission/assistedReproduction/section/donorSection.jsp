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

             </div>
             </div>
     </c:forEach>
     </div>