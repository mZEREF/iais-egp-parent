<c:set var="drugPrescribedDispensedDto" value="${DpSuperDataSubmissionDto.drugPrescribedDispensedDto}" />
<c:set var="drugMedication" value="${drugPrescribedDispensedDto.drugMedication}" />
<div class="panel panel-default">
    <div class="panel-heading ${headingSign}">
        <h4  class="panel-title" >
            <a href="#donorDtoDetails" data-toggle="collapse"  >
                Medication Details
            </a>
        </h4>
    </div>

    <div id="donorDtoDetails" class="panel-collapse collapse in">
        <div class="panel-body">
            <c:forEach items="${drugPrescribedDispensedDto}" var="">
                <div class="panel-main-content form-horizontal">
                    <iais:row>
                        <iais:field width="5" value="Batch No."/>
                        <iais:value width="7" display="true">
                            <c:out value="${drugMedication.batchNo}" />
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field width="5" value="Strength (pg)"/>
                        <iais:value width="7" display="true">
                            <c:out value="${drugMedication.strength}" />
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field width="5" value="Quantity"/>
                        <iais:value width="7" display="true">
                            <c:out value="${drugMedication.quantity}" />
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field width="5" value="Frequency" mandatory="false"/>
                        <iais:value width="7" cssClass="col-md-7"  display="true">
                            <iais:code code="${drugMedication.frequency}"/>
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field width="5" value="Remarks"/>
                        <iais:value width="7" display="true">
                            <c:out value="${drugSubmission.remarks}" />
                        </iais:value>
                    </iais:row>
                </div>
            </c:forEach>
        </div>
    </div>
</div>