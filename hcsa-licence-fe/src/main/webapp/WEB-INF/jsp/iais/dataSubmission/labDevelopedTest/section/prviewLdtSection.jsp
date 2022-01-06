<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="com.ecquaria.cloud.moh.iais.service.CessationFeService" %>
<%@ page import="com.ecquaria.cloud.helper.SpringContextHelper" %>
<%@ page import="com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.LdtSuperDataSubmissionDto" %>
<%@ page import="com.ecquaria.cloud.moh.iais.helper.DataSubmissionHelper" %>
<%@ page import="com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DsLaboratoryDevelopTestDto" %>
<%@ page import="com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PremisesDto" %>
<%
    LdtSuperDataSubmissionDto ldtSuperDataSubmissionDto = DataSubmissionHelper.getCurrentLdtSuperDataSubmissionDto(request);
    DsLaboratoryDevelopTestDto dsLaboratoryDevelopTestDto = ldtSuperDataSubmissionDto.getDsLaboratoryDevelopTestDto();
    String hciName = dsLaboratoryDevelopTestDto.getHciCode();
    CessationFeService cessationFeService = SpringContextHelper.getContext().getBean(CessationFeService.class);
    PremisesDto premisesDto = cessationFeService.getPremiseByHciCodeName(dsLaboratoryDevelopTestDto.getHciCode());
    if (premisesDto != null) {
        hciName = premisesDto.getHciName();
    }
%>
<c:set value="${LdtSuperDataSubmissionDto.dsLaboratoryDevelopTestDto}" var="dsLaboratoryDevelopTestDto"/>
<div class="row">
    <div class="col-xs-12">
        <div class="tab-gp">
            <div class="tab-content">
                <div class="tab-pane fade in active">
                    <div class="row form-horizontal">

                        <iais:row>
                            <iais:field value="Name of Laboratory" width="6" cssClass="col-md-6"/>
                            <iais:value width="6" cssClass="col-md-6" display="true">
                                <c:out value="<%=hciName%>"/>
                            </iais:value>
                        </iais:row>

                        <iais:row>
                            <iais:field value="Name of LDT Test" width="6" cssClass="col-md-6"/>
                            <iais:value width="6" cssClass="col-md-6" display="true">
                                <c:out value="${dsLaboratoryDevelopTestDto.ldtTestName}"/>
                            </iais:value>
                        </iais:row>

                        <iais:row>
                            <iais:field value="Intended Purpose of Test" width="6" cssClass="col-md-6"/>
                            <iais:value width="6" cssClass="col-md-6" display="true">
                                <c:out value="${dsLaboratoryDevelopTestDto.intendedPurpose}"/>
                            </iais:value>
                        </iais:row>

                        <iais:row>
                            <iais:field value="Date LDT was made or will be made available" width="6"
                                        cssClass="col-md-6"/>
                            <iais:value width="6" cssClass="col-md-6" display="true">
                                <fmt:formatDate value="${dsLaboratoryDevelopTestDto.ldtDate}" pattern="dd/MM/yyyy"/>
                            </iais:value>
                        </iais:row>

                        <iais:row>
                            <iais:field value="Person responsible for the test" width="6" cssClass="col-md-6"/>
                            <iais:value width="6" cssClass="col-md-6" display="true">
                                <c:out value="${dsLaboratoryDevelopTestDto.responsePerson}"/>
                            </iais:value>
                        </iais:row>

                        <iais:row>
                            <iais:field value="Designation" width="6" cssClass="col-md-6"/>
                            <iais:value width="6" cssClass="col-md-6" display="true">
                                <c:out value="${dsLaboratoryDevelopTestDto.designation}"/>
                            </iais:value>
                        </iais:row>

                        <iais:row>
                            <iais:field width="6" cssClass="col-md-6" value="Status of Test"/>
                            <iais:value width="6" cssClass="col-md-6" display="true">
                                <c:if test="${dsLaboratoryDevelopTestDto.testStatus == '1'}">Active</c:if>
                                <c:if test="${dsLaboratoryDevelopTestDto.testStatus == '0'}">Inactive</c:if>
                            </iais:value>
                        </iais:row>

                        <iais:row>
                            <iais:field value="Remarks" width="6" cssClass="col-md-6" required="false"/>
                            <iais:value width="6" cssClass="col-md-6" display="true">
                                <c:out value="${dsLaboratoryDevelopTestDto.remarks}"/>
                            </iais:value>
                        </iais:row>

                    </div>
                </div>
            </div>
        </div>
    </div>
</div>