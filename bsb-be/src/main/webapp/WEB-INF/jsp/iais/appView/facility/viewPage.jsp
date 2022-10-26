<%@taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fac" tagdir="/WEB-INF/tags/facility" %>

<%--@elvariable id="needCompare" type="java.lang.Boolean"--%>
<%--@elvariable id="isCertifiedFacility" type="java.lang.Boolean"--%>
<%--@elvariable id="isUncertifiedFacility" type="java.lang.Boolean"--%>
<%--@elvariable id="isRegisteredFacility" type="java.lang.Boolean"--%>
<%--@elvariable id="isSPFifthRegisteredFacility" type="java.lang.Boolean"--%>
<%--@elvariable id="isPolioVirusRegisteredFacility" type="java.lang.Boolean"--%>
<%--@elvariable id="organizationAddress" type="sg.gov.moh.iais.egp.bsb.dto.info.common.OrgAddressInfo"--%>
<%--@elvariable id="facProfile" type="sg.gov.moh.iais.egp.bsb.dto.register.facility.FacilityProfileDto"--%>
<%--@elvariable id="facOperator" type="sg.gov.moh.iais.egp.bsb.dto.register.facility.FacilityOperatorDto"--%>
<%--@elvariable id="facAuth" type="sg.gov.moh.iais.egp.bsb.dto.register.facility.FacilityAuthoriserDto"--%>
<%--@elvariable id="facAdminOfficer" type="sg.gov.moh.iais.egp.bsb.dto.register.facility.FacilityAdminAndOfficerDto"--%>
<%--@elvariable id="facCommittee" type="sg.gov.moh.iais.egp.bsb.dto.register.facility.FacilityCommitteeDto"--%>
<%--@elvariable id="batList" type="java.util.List<sg.gov.moh.iais.egp.bsb.dto.register.bat.BiologicalAgentToxinDto>"--%>
<%--@elvariable id="configList" type="java.util.List<sg.gov.moh.iais.egp.bsb.dto.declaration.DeclarationItemMainInfo>"--%>
<%--@elvariable id="answerMap" type="java.util.Map<java.lang.String, java.lang.String>"--%>
<%--@elvariable id="SELECTED_CLASSIFICATION" type="java.lang.String"--%>
<%--@elvariable id="SELECTED_ACTIVITIES" type="java.util.List<java.lang.String>"--%>
<%--@elvariable id="afc" type="sg.gov.moh.iais.egp.bsb.dto.register.facility.FacilityAfcDto"--%>
<%--@elvariable id="previewSubmit" type="sg.gov.moh.iais.egp.bsb.dto.register.facility.PreviewSubmitDto"--%>
<%--@elvariable id="docSettings" type="java.util.List<sg.gov.moh.iais.egp.bsb.entity.DocSetting>"--%>
<%--@elvariable id="otherDocTypes" type="java.util.Collection<java.lang.String>"--%>
<%--@elvariable id="compareDocMap" type="java.util.Map<java.lang.String, java.util.List<gov.moh.iais.egp.bsb.dto.register.facility.CompareDocRecordInfoDto>>"--%>
<%--@elvariable id="savedFiles" type="java.util.Map<java.lang.String, java.util.List<sg.gov.moh.iais.egp.bsb.dto.file.DocRecordInfo>>"--%>
<%--@elvariable id="maskedEditId" type="java.lang.String"--%>
<%--@elvariable id="compareFacProfiles" type="java.util.List<sg.gov.moh.iais.egp.bsb.dto.compare.CompareWrap>"--%>
<%--@elvariable id="compareFacOperator" type="sg.gov.moh.iais.egp.bsb.dto.compare.CompareWrap"--%>
<%--@elvariable id="compareMainAdmin" type="sg.gov.moh.iais.egp.bsb.dto.compare.CompareWrap"--%>
<%--@elvariable id="compareAlterAdmin" type="sg.gov.moh.iais.egp.bsb.dto.compare.CompareWrap"--%>
<%--@elvariable id="compareOfficers" type="java.util.List<sg.gov.moh.iais.egp.bsb.dto.compare.CompareWrap>"--%>
<%--@elvariable id="compareBioSafetyCommitteeIsDifferent" type="java.lang.Boolean"--%>
<%--@elvariable id="compareAuthorizerIsDifferent" type="java.lang.Boolean"--%>
<%--@elvariable id="compareAfc" type="sg.gov.moh.iais.egp.bsb.dto.compare.CompareWrap"--%>
<%--@elvariable id="compareBatMap" type="java.util.Map<java.lang.String, java.util.List<sg.gov.moh.iais.egp.bsb.dto.compare.CompareWrap>>"--%>
<c:if test="${needCompare}">
    <fac:compare-preview isCfJudge="${isCertifiedFacility}" isUcfJudge="${isUncertifiedFacility}" isRfJudge="${isRegisteredFacility}" isFifthRfJudge="${isSPFifthRegisteredFacility}" isPvRfJudge="${isPolioVirusRegisteredFacility}"
                         compProfile="${organizationAddress}" declarationConfigList="${configList}" declarationAnswerMap="${answerMap}"
                         classification="${SELECTED_CLASSIFICATION}" activities="${SELECTED_ACTIVITIES}"
                         compareFacProfiles="${compareFacProfiles}" compareFacOperator="${compareFacOperator}" compareMainAdmin="${compareMainAdmin}" compareAlterAdmin="${compareAlterAdmin}" compareOfficers="${compareOfficers}"
                         compareBioSafetyCommitteeIsDifferent="${compareBioSafetyCommitteeIsDifferent}" compareAuthorizerIsDifferent="${compareAuthorizerIsDifferent}" compareAfc="${compareAfc}" compareBatMap="${compareBatMap}">
        <jsp:attribute name="docFrag">
            <c:forEach var="doc" items="${docSettings}">
                <div class="form-group">
                    <div class="col-10"><strong>${doc.typeDisplay}</strong></div>
                    <div class="clear"></div>
                </div>
                <c:forEach var="compareWrap" items="${compareDocMap.get(doc.type)}" varStatus="status">
                    <c:set var="oldFile" value="${compareWrap.oldDto}"/>
                    <c:set var="newFile" value="${compareWrap.newDto}"/>
                    <%--@elvariable id="oldFile" type="sg.gov.moh.iais.egp.bsb.dto.file.DocRecordInfo"--%>
                    <%--@elvariable id="newFile" type="sg.gov.moh.iais.egp.bsb.dto.file.DocRecordInfo"--%>
                    <div class="form-group">
                        <c:set var="oldRepoId"><iais:mask name="file" value="${oldFile.repoId}"/></c:set>
                        <div class="col-xs-6"><p data-compare-old="${doc.type}${status.index}" data-val="${oldFile.repoId}"><a href="/bsb-web/ajax/doc/download/repo/${oldRepoId}?filename=${oldFile.filename}">${oldFile.filename}</a>(<fmt:formatNumber value="${oldFile.size/1024.0}" type="number" pattern="0.0"/>KB)</p></div>
                        <c:set var="newRepoId"><iais:mask name="file" value="${newFile.repoId}"/></c:set>
                        <div class="col-xs-6"><p data-compare-new="${doc.type}${status.index}" data-val="${newFile.repoId}" class="compareTdStyle" style="display: none"><a href="/bsb-web/ajax/doc/download/repo/${newRepoId}?filename=${newFile.filename}">${newFile.filename}</a>(<fmt:formatNumber value="${newFile.size/1024.0}" type="number" pattern="0.0"/>KB)</p></div>
                        <div class="clear"></div>
                    </div>
                </c:forEach>
            </c:forEach>
            <c:if test="${not empty otherDocTypes}">
                <div class="form-group">
                    <div class="col-10"><strong>Others</strong></div>
                    <div class="clear"></div>
                </div>
                <div>
                    <c:forEach var="type" items="${otherDocTypes}">
                        <c:forEach var="compareWrap" items="${compareDocMap.get(type)}" varStatus="status">
                            <c:set var="oldFile" value="${compareWrap.oldDto}"/>
                            <c:set var="newFile" value="${compareWrap.newDto}"/>
                            <div class="form-group">
                                <c:set var="oldRepoId"><iais:mask name="file" value="${oldFile.repoId}"/></c:set>
                                <div class="col-xs-6"><p data-compare-old="${doc.type}${status.index}" data-val="${oldFile.repoId}"><a href="/bsb-web/ajax/doc/download/repo/${oldRepoId}?filename=${oldFile.filename}">${oldFile.filename}</a>(<fmt:formatNumber value="${oldFile.size/1024.0}" type="number" pattern="0.0"/>KB)</p></div>
                                <c:set var="newRepoId"><iais:mask name="file" value="${newFile.repoId}"/></c:set>
                                <div class="col-xs-6"><p data-compare-new="${doc.type}${status.index}" data-val="${newFile.repoId}" class="compareTdStyle" style="display: none"><a href="/bsb-web/ajax/doc/download/repo/${newRepoId}?filename=${newFile.filename}">${newFile.filename}</a>(<fmt:formatNumber value="${newFile.size/1024.0}" type="number" pattern="0.0"/>KB)</p></div>
                                <div class="clear"></div>
                            </div>
                        </c:forEach>
                    </c:forEach>
                </div>
            </c:if>
        </jsp:attribute>
    </fac:compare-preview>
</c:if>
<c:if test="${!needCompare}">
    <fac:preview isCfJudge="${isCertifiedFacility}" isUcfJudge="${isUncertifiedFacility}" isRfJudge="${isRegisteredFacility}" isFifthRfJudge="${isSPFifthRegisteredFacility}" isPvRfJudge="${isPolioVirusRegisteredFacility}"
                 compProfile="${organizationAddress}" facProfile="${facProfile}" facOperator="${facOperator}" facAuth="${facAuth}"
                 facAdminOfficer="${facAdminOfficer}" facCommittee="${facCommittee}"
                 batList="${batList}" afc="${afc}" declarationConfigList="${configList}" declarationAnswerMap="${answerMap}"
                 classification="${SELECTED_CLASSIFICATION}" activities="${SELECTED_ACTIVITIES}"
                 pageAppEditSelectDto="${pageAppEditSelectDto}" viewType="${viewType}" isRfc="${isRfc}">
        <jsp:attribute name="docFrag">
            <c:forEach var="doc" items="${docSettings}">
                <c:set var="savedFileList" value="${savedFiles.get(doc.type)}" />
                <c:if test="${not empty savedFileList}">
                    <div class="form-group">
                        <div class="col-10"><strong>${doc.typeDisplay}</strong></div>
                        <div class="clear"></div>
                    </div>
                    <div>
                        <c:forEach var="file" items="${savedFileList}">
                            <c:set var="repoId"><iais:mask name="file" value="${file.repoId}"/></c:set>
                            <div class="form-group">
                                <div class="col-10"><p><a href="/bsb-web/ajax/doc/download/repo/${repoId}?filename=${file.filename}">${file.filename}</a>(<fmt:formatNumber value="${file.size/1024.0}" type="number" pattern="0.0"/>KB)</p></div>
                                <div class="clear"></div>
                            </div>
                        </c:forEach>
                    </div>
                </c:if>
            </c:forEach>
            <c:if test="${not empty otherDocTypes}">
                <div class="form-group">
                    <div class="col-10"><strong>Others</strong></div>
                    <div class="clear"></div>
                </div>
                <div>
                    <c:forEach var="type" items="${otherDocTypes}">
                        <c:set var="savedFileList" value="${savedFiles.get(type)}" />
                        <c:forEach var="file" items="${savedFileList}">
                            <c:set var="repoId"><iais:mask name="file" value="${file.repoId}"/></c:set>
                            <div class="form-group">
                                <div class="col-10"><p><a href="/bsb-web/ajax/doc/download/repo/${repoId}?filename=${file.filename}">${file.filename}</a>(<fmt:formatNumber value="${file.size/1024.0}" type="number" pattern="0.0"/>KB)</p></div>
                                <div class="clear"></div>
                            </div>
                        </c:forEach>
                    </c:forEach>
                </div>
            </c:if>
        </jsp:attribute>
    </fac:preview>
</c:if>