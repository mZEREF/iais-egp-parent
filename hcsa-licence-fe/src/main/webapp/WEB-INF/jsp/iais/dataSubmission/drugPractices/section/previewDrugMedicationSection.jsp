<c:set var="drugPrescribedDispensedDto" value="${dpSuperDataSubmissionDto.drugPrescribedDispensedDto}" />
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<c:set var="drugMedication" value="${drugPrescribedDispensedDto.drugMedication}" />
<c:set var="drugSubmission" value="${drugPrescribedDispensedDto.drugSubmission}" />
<div class="panel panel-default">
    <div class="panel-heading ${headingSign}">
        <h4  class="panel-title" >
            <a class="collapsed" href="#medication" data-toggle="collapse"  >
                Medication Details
            </a>
        </h4>
    </div>
    <div id="medication" class="panel-collapse collapse">
        <div class="panel-body">
            <div class="panel-main-content form-horizontal">
                <c:forEach items="${drugPrescribedDispensedDto.drugMedicationDtos}" var="drugMedicationDto" varStatus="idxStatus">
                    <c:set var="index" value="${idxStatus.index}"/>
                    <iais:row>
                        <div class="col-sm-6 control-label formtext col-md-8">
                            <div class="cgo-header">
                                <strong>Medication <span>${index+1}</span></strong>
                            </div>
                        </div>
                    </iais:row>
                    <c:if test="${drugSubmission.drugType != 'DPD001'}">
                        <iais:row>
                            <iais:field width="5" value="Batch No."/>
                            <iais:value width="7" display="true">
                                <c:out value="${drugMedicationDto.batchNo}" />
                            </iais:value>
                        </iais:row>
                    </c:if>
                    <iais:row>
                        <label class="col-xs-5 col-md-4 control-label" >
                            <c:choose>
                                <c:when test="${drugSubmission.medication == 'MED001'}">
                                    Strength (&micro;g/hr)
                                </c:when>
                                <c:otherwise>
                                    Strength (mg)
                                </c:otherwise>
                            </c:choose>
                        </label>
                        <iais:value width="7" display="true">
                            <c:out value="${drugMedicationDto.strength}" />
                        </iais:value>
                    </iais:row>
                    <c:if test="${drugMedicationDto.excess eq 'Y'}">
                        <c:set var="toolMsg"><iais:message key="DS_ERR062"/></c:set>
                    </c:if>
                    <iais:row>
                        <iais:field width="5" value="Quantity" info="${toolMsg}"/>
                        <iais:value width="7" display="true">
                            <c:out value="${drugMedicationDto.quantity}" />
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field width="5" value="Frequency" mandatory="false"/>
                        <iais:value width="7" cssClass="col-md-7"  display="true">
                            <iais:code code="${drugMedicationDto.frequency}"/>
                        </iais:value>
                    </iais:row>
                    <iais:row  style="${drugMedicationDto.frequency eq 'FRE009' ? '' : 'display: none'}">
                        <iais:field width="5" value="Other-Frequency"/>
                        <iais:value width="7" display="true" cssClass="col-md-7">
                            <c:out value="${drugMedicationDto.otherFrequency}"/>
                        </iais:value>
                    </iais:row>

                </c:forEach>
                <iais:row>
                    <iais:field width="5" value="Remarks"/>
                    <iais:value width="7" display="true">
                        <c:out value="${drugSubmission.remarks}" />
                    </iais:value>
                </iais:row>
            </div>
        </div>
    </div>
</div>
