<%@ page import="java.util.List" %>
<%@ page import="com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils" %>
<%@ page import="com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.PregnancyOutcomeBabyDto" %>
<%@ page import="com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.PregnancyOutcomeBabyDefectDto" %>
<%@ page import="com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArSuperDataSubmissionDto" %>
<%@ page import="com.ecquaria.cloud.moh.iais.common.utils.ParamUtil" %>
<%@ page import="com.ecquaria.cloud.moh.iais.helper.DataSubmissionHelper" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%
    List<List<String>> defectTypesArray = IaisCommonUtils.genNewArrayList();
    List<String> otherDefectTypes = IaisCommonUtils.genNewArrayList();
    ArSuperDataSubmissionDto arSuperDataSubmissionDto = DataSubmissionHelper.getCurrentArDataSubmission(request);
    List<PregnancyOutcomeBabyDto> pregnancyOutcomeBabyDtos = arSuperDataSubmissionDto.getPregnancyOutcomeStageDto().getPregnancyOutcomeBabyDtos();
    if (IaisCommonUtils.isNotEmpty(pregnancyOutcomeBabyDtos)) {
        for (int i = 0; i < pregnancyOutcomeBabyDtos.size(); i++) {
            PregnancyOutcomeBabyDto pregnancyOutcomeBabyDto = pregnancyOutcomeBabyDtos.get(i);
            List<String> defectTypes = IaisCommonUtils.genNewArrayList();
            otherDefectTypes.add("");
            for (PregnancyOutcomeBabyDefectDto pregnancyOutcomeBabyDefectDto : pregnancyOutcomeBabyDto.getPregnancyOutcomeBabyDefectDtos()) {
                defectTypes.add(pregnancyOutcomeBabyDefectDto.getDefectType());
                if ("POSBDT008".equals(pregnancyOutcomeBabyDefectDto.getDefectType())) {
                    otherDefectTypes.set(i, pregnancyOutcomeBabyDefectDto.getOtherDefectType());
                }
            }
            defectTypesArray.add(defectTypes);
        }
    }
    ParamUtil.setRequestAttr(request, "defectTypesArray", defectTypesArray);
    ParamUtil.setRequestAttr(request, "otherDefectTypes", otherDefectTypes);
%>
<div <c:if test="${pregnancyOutcomeStageDto.babyDetailsUnknown}">style="display:none;"</c:if>>
    <c:forEach items="${pregnancyOutcomeStageDto.pregnancyOutcomeBabyDtos}"
               var="pregnancyOutcomeBabyDto"
               varStatus="status">
        <c:set value="${defectTypesArray[status.index]}" var="defectTypes"/>
        <c:set value="${otherDefectTypes[status.index]}" var="otherDefectType"/>
        <c:set var="displayNum" value="${status.index + 1}"/>
        <iais:row>
            <iais:field width="6" value="Baby ${displayNum} Birth Weight"
                        cssClass="col-md-6"/>
            <iais:value width="5" cssClass="col-md-6" display="true">
                <iais:code code="${pregnancyOutcomeBabyDto.birthWeight}"/>
            </iais:value>
        </iais:row>
        <iais:row>
            <iais:field width="6" value="Baby ${displayNum} Birth Defect"
                        cssClass="col-md-6"/>
            <iais:value width="5" cssClass="col-md-6" display="true">
                <c:out value="${pregnancyOutcomeBabyDto.birthDefect}"/>
            </iais:value>
        </iais:row>
        <div name="defectTypeSectionName"
             <c:if test="${pregnancyOutcomeBabyDto.birthDefect != 'Yes'}">style="display:none;"</c:if>>
            <iais:row>
                <iais:field width="6" value="Baby ${displayNum} Defect Type" cssClass="col-md-6"/>
                <iais:value width="5" cssClass="col-md-6">
                    <c:forEach var="pregnancyOutcomeBabyDefectDto"
                               items="${pregnancyOutcomeBabyDto.pregnancyOutcomeBabyDefectDtos}"
                               varStatus="defectStatus">
                        <p><iais:code code="${pregnancyOutcomeBabyDefectDto.defectType}"/></p>
                    </c:forEach>
                </iais:value>
            </iais:row>
            <div name="otherDefectTypeDivName"
                 <c:if test="${!fn:contains(defectTypes,'POSBDT008')}">style="display:none;"</c:if>>
                <iais:row>
                    <iais:field width="6" value="Baby ${displayNum} Defect Type (Others)"
                                cssClass="col-md-6"/>
                    <iais:value width="5" cssClass="col-md-6" display="true">
                        <c:out value="${otherDefectType}"/>
                    </iais:value>
                </iais:row>
            </div>
        </div>
    </c:forEach>
</div>