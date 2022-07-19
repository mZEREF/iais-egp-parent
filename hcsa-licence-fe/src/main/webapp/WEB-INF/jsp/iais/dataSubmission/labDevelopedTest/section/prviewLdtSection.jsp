<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="com.ecquaria.cloud.moh.iais.service.CessationFeService" %>
<%@ page import="com.ecquaria.cloud.helper.SpringContextHelper" %>
<%@ page import="com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.LdtSuperDataSubmissionDto" %>
<%@ page import="com.ecquaria.cloud.moh.iais.helper.DataSubmissionHelper" %>
<%@ page import="com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DsLaboratoryDevelopTestDto" %>
<%@ page import="com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PremisesDto" %>
<%@ page import="com.ecquaria.cloud.moh.iais.common.utils.StringUtil" %>
<%
    LdtSuperDataSubmissionDto ldtSuperDataSubmissionDto = DataSubmissionHelper.getCurrentLdtSuperDataSubmissionDto(request);
    DsLaboratoryDevelopTestDto dsLaboratoryDevelopTestDto = ldtSuperDataSubmissionDto.getDsLaboratoryDevelopTestDto();
    String hciName = dsLaboratoryDevelopTestDto.getHciCode();
    CessationFeService cessationFeService = SpringContextHelper.getContext().getBean(CessationFeService.class);
    PremisesDto premisesDto = cessationFeService.getPremiseByHciCodeName(dsLaboratoryDevelopTestDto.getHciCode());
    if (premisesDto != null) {
        hciName = StringUtil.escapeHtml(StringUtil.viewHtml(premisesDto.getHciName()) + ", " + premisesDto.getAddress());
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
                            <iais:field value="Name of Laboratory" width="5" cssClass="col-md-5"/>
                            <iais:value width="7" cssClass="col-md-7" display="true">
                                <c:out value="<%=hciName%>"/>
                            </iais:value>
                        </iais:row>

                        <iais:row>
                            <iais:field value="Name of LDT Test" width="5" cssClass="col-md-5"/>
                            <iais:value width="7" cssClass="col-md-7" display="true">
                                <c:out value="${dsLaboratoryDevelopTestDto.ldtTestName}"/>
                            </iais:value>
                        </iais:row>

                        <iais:row>
                            <iais:field value="Intended Purpose of Test" width="5" cssClass="col-md-5"/>
                            <iais:value width="7" cssClass="col-md-7" display="true">
                                <c:out value="${dsLaboratoryDevelopTestDto.intendedPurpose}"/>
                            </iais:value>
                        </iais:row>

                        <iais:row>
                            <iais:field value="Date LDT was made or will be made available" width="5"
                                        cssClass="col-md-5"/>
                            <iais:value width="7" cssClass="col-md-7" display="true">
                                <fmt:formatDate value="${dsLaboratoryDevelopTestDto.ldtDate}" pattern="dd/MM/yyyy"/>
                            </iais:value>
                        </iais:row>

                        <iais:row>
                            <iais:field value="Person responsible for the test" width="5" cssClass="col-md-5"/>
                            <iais:value width="7" cssClass="col-md-7" display="true">
                                <c:out value="${dsLaboratoryDevelopTestDto.responsePerson}"/>
                            </iais:value>
                        </iais:row>

                        <iais:row>
                            <iais:field value="Designation" width="5" cssClass="col-md-5"/>
                            <iais:value width="7" cssClass="col-md-7" display="true">
                                <c:out value="${dsLaboratoryDevelopTestDto.designation}"/>
                            </iais:value>
                        </iais:row>

                        <iais:row>
                            <label class="col-xs-5 col-md-4 control-label col-md-5">Status of Test&nbsp;
                                <a class="btn-tooltip styleguide-tooltip" data-toggle="tooltip"
                                   data-html="true" href="javascript:void(0);"
                                   title='Active - Clinical laboratory continues to offer this LDT in their laboratory.
                                                  Inactive - Clinical laboratory has ceased to make available this LDT in their laboratory.'
                                   style="z-index: 10"
                                   data-original-title="">i</a>
                            </label>
                            <iais:value width="7" cssClass="col-md-7" display="true">
                                <c:if test="${dsLaboratoryDevelopTestDto.testStatus == '1'}">Active</c:if>
                                <c:if test="${dsLaboratoryDevelopTestDto.testStatus == '0'}">Inactive</c:if>
                            </iais:value>
                        </iais:row>

                        <iais:row>
                            <iais:field value="Remarks" width="5" cssClass="col-md-5" required="false"/>
                            <iais:value width="7" cssClass="col-md-7" display="true">
                                <c:out value="${dsLaboratoryDevelopTestDto.remarks}"/>
                            </iais:value>
                        </iais:row>

                        <%@ include file="./previewDsAmendment.jsp" %>
                        <%@ include file="./ldtDeclaration.jsp" %>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>