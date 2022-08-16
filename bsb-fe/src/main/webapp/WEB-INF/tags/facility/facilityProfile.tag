<%@tag description="Facility profile tag of facility registration" %>
<%@taglib prefix="webui" uri="http://www.ecquaria.com/webui" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%@taglib prefix="iais-bsb" uri="http://www.ecq.com/iais-bsb" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%@attribute name="isFifthRf" required="true" type="java.lang.Boolean" %>
<%@attribute name="isPvRf" required="true" type="java.lang.Boolean" %>
<%@attribute name="facProfile" required="true" type="sg.gov.moh.iais.egp.bsb.dto.register.facility.FacilityProfileDto" %>
<%@attribute name="organizationAddress" required="true" type="sg.gov.moh.iais.egp.bsb.dto.info.common.OrgAddressInfo" %>
<%@attribute name="facTypeOps" required="true" type="java.util.List<com.ecquaria.cloud.moh.iais.common.dto.SelectOption>" %>
<%@attribute name="addressTypeOps" required="true" type="java.util.List<com.ecquaria.cloud.moh.iais.common.dto.SelectOption>" %>
<%@attribute name="opvSabinPIMRiskLevelOps" required="true" type="java.util.List<com.ecquaria.cloud.moh.iais.common.dto.SelectOption>" %>
<%@attribute name="specialJsFrag" fragment="true" %>
<%@attribute name="dashboardFrag" fragment="true" %>
<%@attribute name="innerFooterFrag" fragment="true" %>
<%@attribute name="editJudge" type="java.lang.Boolean" %>

<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-internet"/>

<link href="<%=sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT%>/css/bsb/bsb-common.css" rel="stylesheet"/>
<script type="text/javascript" src="<%=sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT%>/js/bsb/bsb-common.js"></script>
<script type="text/javascript" src="<%=sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT%>/js/bsb/bsb-common-node-group.js"></script>
<script type="text/javascript" src="<%=sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT%>/js/bsb/bsb-common-add-section.js"></script>
<script type="text/javascript" src="<%=sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT%>/js/bsb/bsb-common-file.js"></script>
<jsp:invoke fragment="specialJsFrag"/>

<style>
    .align-label-and-input {
        padding-left: 15px;
    }
</style>

<%@include file="/WEB-INF/jsp/iais/include/showErrorMsg.jsp" %>
<jsp:invoke fragment="dashboardFrag"/>
<form method="post" id="mainForm" enctype="multipart/form-data" action="<%=process.runtime.continueURL()%>">
    <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
    <input type="hidden" name="action_type" value="">
    <input type="hidden" name="action_value" value="">
    <input type="hidden" name="action_additional" value="">

    <input type="hidden" id="section_repeat_section_idx_name" value="sectionIdx" readonly disabled>
    <input type="hidden" id="section_deleted_repeat_section_idx_name" value="deletedSectionIdx" readonly disabled>
    <input type="hidden" id="section_repeat_section_id_prefix" value="profileInfoSection" readonly disabled>
    <input type="hidden" id="section_repeat_header_title_prefix" value="Facility Profile " readonly disabled>
    <input type="hidden" id="section_repeat_section_group_id" value="sectionGroup" readonly disabled>
    <input type="hidden" id="section_repeat_separator" value="--v--" readonly disabled>
    <input type="hidden" name="sectionIdx" value="<%=sg.gov.moh.iais.egp.bsb.util.TableDisplayUtil.indexes(facProfile.getInfoList().size())%>">
    <input type="hidden" id="deletedSectionIdx" value=""/>
    <input type="hidden" id="deleteExistFiles" name="deleteExistFiles" value="">
    <input type="hidden" id="deleteNewFiles" name="deleteNewFiles" value="">

    <input id="multiUploadTrigger" type="file" multiple="multiple" style="display: none"/>
    <input id="echoReloadTrigger" type="file" style="display: none"/>
    <div id="fileUploadInputDiv" style="display: none"></div>

    <div class="main-content">
        <div class="container">
            <div class="row">
                <div class="col-xs-12">
                    <div class="tab-gp steps-tab">
                        <%@include file="/WEB-INF/jsp/iais/facRegistration/InnerNavTab.jsp" %>
                        <div class="tab-content">
                            <div class="tab-pane fade in active">
                                <div id="facInfoPanel" role="tabpanel">
                                    <%@include file="/WEB-INF/jsp/iais/facRegistration/subStepNavTab.jsp" %>
                                    <div class="form-horizontal">
                                        <c:if test="${editJudge}"><div class="text-right app-font-size-16"><a id="edit" href="javascript:void(0)"><em class="fa fa-pencil-square-o"></em>Edit</a></div></c:if>

                                        <div id="sectionGroup">
                                        <c:forEach var="info" items="${facProfile.infoList}" varStatus="status">
                                            <c:set var="sectionCurIdx" value="${status.index}" scope="request"/>
                                            <section id="profileInfoSection--v--${status.index}">
                                                <c:choose>
                                                    <c:when test="${facProfile.infoList.size() > 1 and status.index eq 0}">
                                                        <div class="form-group"><h3 class="col-xs-9 col-sm-10 col-md-11" style="border-bottom: 1px solid black">Facility Profile ${status.count}</h3></div>
                                                    </c:when>
                                                    <c:when test="${facProfile.infoList.size() > 1 and status.index gt 0}">
                                                        <div class="form-group"><h3 class="col-xs-9 col-sm-10 col-md-11" style="border-bottom: 1px solid black">Facility Profile ${status.count}</h3><div class="col-sm-1"><h4 class="text-danger"><em data-current-idx="${status.index}" class="fa fa-times-circle del-size-36 cursorPointer removeBtn"></em></h4></div></div>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <div class="form-group"><h3 class="col-xs-9 col-sm-10 col-md-11" style="border-bottom: 1px solid black">Facility Profile</h3></div>
                                                    </c:otherwise>
                                                </c:choose>
                                            <div class="form-group ">
                                                <div class="col-sm-5 control-label">
                                                    <label for="facName--v--${status.index}">Facility Name <span class="mandatory otherQualificationSpan">*</span></label>
                                                </div>
                                                <div class="col-sm-6 col-md-7">
                                                    <input maxLength="250" type="text" autocomplete="off" name="facName--v--${status.index}" id="facName--v--${status.index}" value='<c:out value="${info.facName}"/>'/>
                                                    <span data-err-ind="facName--v--${status.index}" class="error-msg"></span>
                                                </div>
                                            </div>

                                            <div class="form-group ">
                                                <div class="col-sm-5 control-label">
                                                    <label for="facType--v--${status.index}">Type of Facility <span class="mandatory otherQualificationSpan">*</span></label>
                                                </div>
                                                <div class="col-sm-6 col-md-7">
                                                    <select name="facType--v--${status.index}" class="facTypeDropdown--v--${status.index}" id="facType--v--${status.index}" data-custom-ind="facType">
                                                        <option value="">Please Select</option>
                                                        <c:forEach var="type" items="${facTypeOps}">
                                                            <option value="${type.value}" <c:if test="${type.value eq info.facType}">selected="selected"</c:if> >${type.text}</option>
                                                        </c:forEach>
                                                    </select>
                                                    <span data-err-ind="facType--v--${status.index}" class="error-msg"></span>
                                                </div>
                                            </div>

                                            <iais-bsb:single-constant constantName="FACILITY_TYPE_OTHERS" classFullName="sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants" attributeKey="othersFacilityTypeCode"/>
                                            <%--@elvariable id="othersFacilityTypeCode" type="java.lang.String"--%>
                                            <div class="form-group " id="facTypeDetailsFormGroup--v--${status.index}" <c:if test="${info.facType ne othersFacilityTypeCode}">style="display: none"</c:if>>
                                                <div class="col-sm-5 control-label">
                                                    <label for="facTypeDetails--v--${status.index}">Details of type of facility <span class="mandatory otherQualificationSpan">*</span></label>
                                                </div>
                                                <div class="col-sm-6 col-md-7">
                                                    <input maxLength="250" type="text" autocomplete="off" name="facTypeDetails--v--${status.index}" id="facTypeDetails--v--${status.index}" value='<c:out value="${info.facTypeDetails}"/>'/>
                                                    <span data-err-ind="facTypeDetails--v--${status.index}" class="error-msg"></span>
                                                </div>
                                            </div>

                                            <div class="form-group ">
                                                <div class="col-sm-5 control-label">
                                                    <label>Is the Facility address the same as the company address? <span class="mandatory otherQualificationSpan">*</span></label>
                                                </div>
                                                <div class="col-sm-6 col-md-7">
                                                    <div class="form-check col-xs-4" style="margin-top: 8px">
                                                        <input type="radio" class="form-check-input" name="isSameAddress--v--${status.index}" id="isSameAddress--v--${status.index}" data-custom-ind="isSameAddress" value="Y" <c:if test="${info.sameAddress eq 'Y'}">checked="checked"</c:if> />
                                                        <label for="isSameAddress--v--${status.index}" class="form-check-label"><span class="check-circle"></span>Yes</label>
                                                    </div>
                                                    <div class="form-check col-xs-8" style="margin-top: 8px">
                                                        <input type="radio" class="form-check-input" name="isSameAddress--v--${status.index}" id="notSameAddress--v--${status.index}" data-custom-ind="isSameAddress" value="N" <c:if test="${info.sameAddress eq 'N'}">checked="checked"</c:if> />
                                                        <label for="notSameAddress--v--${status.index}" class="form-check-label"><span class="check-circle"></span>No</label>
                                                    </div>
                                                    <span data-err-ind="sameAddress--v--${status.index}" class="error-msg"></span>
                                                </div>
                                            </div>

                                            <iais-bsb:single-constant constantName="ADDRESS_TYPE_APT_BLK" classFullName="com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts" attributeKey="aptBlk"/>
                                            <iais-bsb:single-constant constantName="ADDRESS_TYPE_WITHOUT_APT_BLK" classFullName="com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts" attributeKey="withoutAptBlk"/>
                                            <%--@elvariable id="aptBlk" type="java.lang.String"--%>
                                            <%--@elvariable id="withoutAptBlk" type="java.lang.String"--%>
                                            <div id="isSameAddrSection--v--${status.index}" <c:if test="${info.sameAddress eq null}">style="display: none"</c:if>>
                                                <div id="isSameAddrSectionY--v--${status.index}" <c:if test="${info.sameAddress ne 'Y'}">style="display: none"</c:if>>
                                                    <div class="form-group">
                                                        <div class="col-sm-5 control-label">
                                                            <label for="postalCodeY--v--${status.index}">Postal Code <span class="mandatory otherQualificationSpan">*</span></label>
                                                        </div>
                                                        <div class="col-sm-6 col-md-7">
                                                            <label id="postalCodeY--v--${status.index}" class="align-label-and-input">${organizationAddress.postalCode}</label>
                                                        </div>
                                                    </div>

                                                    <div class="form-group ">
                                                        <div class="col-sm-5 control-label">
                                                            <label for="addressTypeY--v--${status.index}">Address Type <span class="mandatory otherQualificationSpan">*</span></label>
                                                        </div>
                                                        <div class="col-sm-6 col-md-7">
                                                            <label id="addressTypeY--v--${status.index}" class="align-label-and-input"><iais:code code="${organizationAddress.addressType}"/></label>
                                                        </div>
                                                    </div>


                                                    <div class="form-group ">
                                                        <div class="col-sm-5 control-label">
                                                            <label for="blockY--v--${status.index}">Block / House No. <span id="aptMandatorySameAddressBlk" class="mandatory otherQualificationSpan" <c:if test="${organizationAddress.addressType ne aptBlk}">style="display:none;"</c:if>>*</span></label>
                                                        </div>
                                                        <div class="col-sm-6 col-md-7">
                                                            <label id="blockY--v--${status.index}" class="align-label-and-input">${organizationAddress.blockNo}</label>
                                                        </div>
                                                    </div>

                                                    <div class="form-group ">
                                                        <div class="col-sm-5 control-label">
                                                            <label for="floorY--v--${status.index}">Floor No. <span id="aptMandatorySameAddressFloor" class="mandatory otherQualificationSpan" <c:if test="${organizationAddress.addressType ne aptBlk}">style="display:none;"</c:if>>*</span></label>
                                                        </div>
                                                        <div class="col-sm-6 col-md-7">
                                                            <label id="floorY--v--${status.index}" class="align-label-and-input">${organizationAddress.floor}</label>
                                                        </div>
                                                    </div>

                                                    <div class="form-group ">
                                                        <div class="col-sm-5 control-label">
                                                            <label for="unitNoY--v--${status.index}">Unit No. <span id="aptMandatorySameAddressUnit" class="mandatory otherQualificationSpan" <c:if test="${organizationAddress.addressType ne aptBlk}">style="display:none;"</c:if>>*</span></label>
                                                        </div>
                                                        <div class="col-sm-6 col-md-7">
                                                            <label id="unitNoY--v--${status.index}" class="align-label-and-input">${organizationAddress.unitNo}</label>
                                                        </div>
                                                    </div>

                                                    <div class="form-group ">
                                                        <div class="col-sm-5 control-label">
                                                            <label for="streetNameY--v--${status.index}">Street Name <span id="aptMandatorySameAddressStreet" class="mandatory otherQualificationSpan" <c:if test="${organizationAddress.addressType ne withoutAptBlk}">style="display:none;"</c:if>>*</span></label>
                                                        </div>
                                                        <div class="col-sm-6 col-md-7">
                                                            <label id="streetNameY--v--${status.index}" class="align-label-and-input">${organizationAddress.street}</label>
                                                        </div>
                                                    </div>

                                                    <div class="form-group ">
                                                        <div class="col-sm-5 control-label">
                                                            <label for="buildingNameY--v--${status.index}">Building Name</label>
                                                        </div>
                                                        <div class="col-sm-6 col-md-7">
                                                            <label id="buildingNameY--v--${status.index}" class="align-label-and-input">${organizationAddress.building}</label>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div id="isSameAddrSectionN--v--${status.index}" <c:if test="${info.sameAddress ne 'N'}">style="display: none"</c:if>>
                                                    <%
                                                        sg.gov.moh.iais.egp.bsb.dto.register.facility.FacilityProfileDto profileDto = (sg.gov.moh.iais.egp.bsb.dto.register.facility.FacilityProfileDto) request.getAttribute("facProfile");
                                                        int sectionCurIdx = (int) request.getAttribute("sectionCurIdx");
                                                        // rather than use profile DTO, use an address DTO should be better
                                                        sg.gov.moh.iais.egp.bsb.dto.register.facility.FacilityProfileInfo notSameAddressDto = profileDto.getInfoList().get(sectionCurIdx);
                                                        if (!"N".equals(notSameAddressDto.getSameAddress())) {
                                                            notSameAddressDto = new sg.gov.moh.iais.egp.bsb.dto.register.facility.FacilityProfileInfo();
                                                        }
                                                        request.setAttribute("notSameAddrDto", notSameAddressDto);
                                                    %>
                                                    <div class="form-group">
                                                        <div class="col-sm-5 control-label">
                                                            <label for="postalCodeN--v--${status.index}">Postal Code <span class="mandatory otherQualificationSpan">*</span></label>
                                                        </div>
                                                        <div class="col-sm-5">
                                                            <input maxLength="6" type="text" autocomplete="off" name="postalCode--v--${status.index}" id="postalCodeN--v--${status.index}" value='<c:out value="${notSameAddrDto.postalCode}"/>' oninput="value=value.replace(/[^\d]/g,'')"/>
                                                            <span data-err-ind="postalCode--v--${status.index}" class="error-msg"></span>
                                                        </div>
                                                        <div class="col-sm-2">
                                                            <a id="retrieveAddressBtn--v--${status.index}" data-custom-ind="retrieveAddressBtn" href="javascript:void(0)">Retrieve your address</a>
                                                        </div>
                                                    </div>

                                                    <div class="form-group ">
                                                        <div class="col-sm-5 control-label">
                                                            <label for="addressType--v--${status.index}">Address Type <span class="mandatory otherQualificationSpan">*</span></label>
                                                        </div>
                                                        <div class="col-sm-6 col-md-7">
                                                            <select name="addressType--v--${status.index}" class="addressTypeDropdown--v--${status.index}" id="addressType--v--${status.index}" data-custom-ind="addressType">
                                                                <option value="">Please Select</option>
                                                                <c:forEach var="type" items="${addressTypeOps}">
                                                                    <option value="${type.value}" <c:if test="${type.value eq notSameAddrDto.addressType}">selected="selected"</c:if> >${type.text}</option>
                                                                </c:forEach>
                                                            </select>
                                                            <span data-err-ind="addressType--v--${status.index}" class="error-msg"></span>
                                                        </div>
                                                    </div>


                                                    <div class="form-group ">
                                                        <div class="col-sm-5 control-label">
                                                            <label for="blockN--v--${status.index}">Block / House No. <span id="aptMandatoryBlk--v--${status.index}" class="mandatory otherQualificationSpan" <c:if test="${notSameAddrDto.addressType ne aptBlk}">style="display:none;"</c:if>>*</span></label>
                                                        </div>
                                                        <div class="col-sm-6 col-md-7">
                                                            <input maxLength="10" type="text" autocomplete="off" name="block--v--${status.index}" id="blockN--v--${status.index}" value='<c:out value="${notSameAddrDto.block}"/>'/>
                                                            <span data-err-ind="block--v--${status.index}" class="error-msg"></span>
                                                        </div>
                                                    </div>

                                                    <div class="form-group ">
                                                        <div class="col-sm-5 control-label">
                                                            <label for="floorN--v--${status.index}">Floor No. <span id="aptMandatoryFloor--v--${status.index}" class="mandatory otherQualificationSpan" <c:if test="${notSameAddrDto.addressType ne aptBlk}">style="display:none;"</c:if>>*</span></label>
                                                        </div>
                                                        <div class="col-sm-6 col-md-7">
                                                            <input maxLength="3" type="text" autocomplete="off" name="floor--v--${status.index}" id="floorN--v--${status.index}" value='<c:out value="${notSameAddrDto.floor}"/>'/>
                                                            <span data-err-ind="floor--v--${status.index}" class="error-msg"></span>
                                                        </div>
                                                    </div>

                                                    <div class="form-group ">
                                                        <div class="col-sm-5 control-label">
                                                            <label for="unitNoN--v--${status.index}">Unit No. <span id="aptMandatoryUnit--v--${status.index}" class="mandatory otherQualificationSpan" <c:if test="${notSameAddrDto.addressType ne aptBlk}">style="display:none;"</c:if>>*</span></label>
                                                        </div>
                                                        <div class="col-sm-6 col-md-7">
                                                            <input maxLength="5" type="text" autocomplete="off" name="unitNo--v--${status.index}" id="unitNoN--v--${status.index}" value='<c:out value="${notSameAddrDto.unitNo}"/>'/>
                                                            <span data-err-ind="unitNo--v--${status.index}" class="error-msg"></span>
                                                        </div>
                                                    </div>

                                                    <div class="form-group ">
                                                        <div class="col-sm-5 control-label">
                                                            <label for="streetNameN--v--${status.index}">Street Name <span id="aptMandatoryStreet--v--${status.index}" class="mandatory otherQualificationSpan" <c:if test="${notSameAddrDto.addressType ne withoutAptBlk}">style="display:none;"</c:if>>*</span></label>
                                                        </div>
                                                        <div class="col-sm-6 col-md-7">
                                                            <input maxLength="32" type="text" autocomplete="off" name="streetName--v--${status.index}" id="streetNameN--v--${status.index}" value='<c:out value="${notSameAddrDto.streetName}"/>'/>
                                                            <span data-err-ind="streetName--v--${status.index}" class="error-msg"></span>
                                                        </div>
                                                    </div>

                                                    <div class="form-group ">
                                                        <div class="col-sm-5 control-label">
                                                            <label for="buildingNameN--v--${status.index}">Building Name</label>
                                                        </div>
                                                        <div class="col-sm-6 col-md-7">
                                                            <input maxLength="45" type="text" autocomplete="off" name="buildingName--v--${status.index}" id="buildingNameN--v--${status.index}" value='<c:out value="${notSameAddrDto.building}"/>'/>
                                                            <span data-err-ind="buildingName--v--${status.index}" class="error-msg"></span>
                                                        </div>
                                                    </div>
                                                </div>

                                                <div class="form-group ">
                                                    <div class="col-sm-5 control-label">
                                                        <label>Is the facility a Protected Place? <span class="mandatory otherQualificationSpan">*</span></label>
                                                        <a class="btn-tooltip styleguide-tooltip" href="javascript:void(0);" data-toggle="tooltip" data-html="true" title="" data-original-title="<p>Refers to a facility that has been gazetted as a Protected Place under the Infrastructure Protection Act</p>">i</a>
                                                    </div>
                                                    <div class="col-sm-6 col-md-7">
                                                        <div class="form-check col-xs-4" style="margin-top: 8px">
                                                            <input type="radio" class="form-check-input" name="protectedPlace--v--${status.index}" id="isAProtectedPlace--v--${status.index}" data-custom-ind="gazetted" value="Y" <c:if test="${info.facilityProtected eq 'Y'}">checked="checked"</c:if> />
                                                            <label for="isAProtectedPlace--v--${status.index}" class="form-check-label">Yes<span class="check-circle"></span></label>
                                                        </div>
                                                        <div class="form-check col-xs-4" style="margin-top: 8px">
                                                            <input type="radio" class="form-check-input" name="protectedPlace--v--${status.index}" id="notAProtectedPlace--v--${status.index}" data-custom-ind="gazetted" value="N" <c:if test="${info.facilityProtected eq 'N'}">checked="checked"</c:if> />
                                                            <label for="notAProtectedPlace--v--${status.index}" class="form-check-label">No<span class="check-circle"></span></label>
                                                        </div>
                                                        <span data-err-ind="facilityProtected--v--${status.index}" class="error-msg"></span>
                                                    </div>
                                                </div>
                                            </div>

                                            <div id="docUploadDiv--v--${status.index}" class="document-content" <c:if test="${info.facilityProtected ne 'Y'}">style="display: none"</c:if>>
                                                <div>
                                                    <ul>
                                                        <li>The maximum file size per document is 10 MB.</li>
                                                        <li>Acceptable file formats: JPG, PNG, PDF, CSV, DOCX, JPEG, XLS, DOC and XLSX.</li>
                                                    </ul>
                                                </div>
                                                <div class="document-upload-gp">
                                                    <div class="document-upload-list">
                                                        <h3>Gazette Order <span class="mandatory otherQualificationSpan">*</span></h3>
                                                        <div class="file-upload-gp">
                                                            <c:forEach var="fileInfo" items="${info.savedDocMap.values()}">
                                                                <c:set var="repoId"><iais:mask name="file" value="${fileInfo.repoId}"/></c:set>
                                                                <div id="${repoId}FileDiv">
                                                                    <a href="/bsb-web/ajax/doc/download/facReg/profile/repo/${repoId}" style="text-decoration: underline"><span id="${repoId}Span">${fileInfo.filename}</span></a>(<fmt:formatNumber value="${fileInfo.size/1024.0}" type="number" pattern="0.0"/>KB)<button
                                                                        type="button" class="btn btn-secondary btn-sm" onclick="deleteSavedFile('${repoId}')">Delete</button><button
                                                                        type="button" class="btn btn-secondary btn-sm" onclick="reloadSavedFile('${repoId}', 'gazetteOrder')">Reload</button>
                                                                    <span data-err-ind="${fileInfo.repoId}" class="error-msg"></span>
                                                                </div>
                                                            </c:forEach>
                                                            <c:forEach var="fileInfo" items="${info.newDocMap.values()}">
                                                                <c:set var="tmpId"><iais:mask name="file" value="${fileInfo.tmpId}"/></c:set>
                                                                <div id="${tmpId}FileDiv">
                                                                    <a href="/bsb-web/ajax/doc/download/facReg/profile/new/${tmpId}" style="text-decoration: underline"><span id="${tmpId}Span">${fileInfo.filename}</span></a>(<fmt:formatNumber value="${fileInfo.size/1024.0}" type="number" pattern="0.0"/>KB)<button
                                                                        type="button" class="btn btn-secondary btn-sm" onclick="deleteNewFile('${tmpId}')">Delete</button><button
                                                                        type="button" class="btn btn-secondary btn-sm" onclick="reloadNewFile('${tmpId}', 'gazetteOrder')">Reload</button>
                                                                    <span data-err-ind="${fileInfo.tmpId}" class="error-msg"></span>
                                                                </div>
                                                            </c:forEach>
                                                            <a class="btn file-upload btn-secondary" data-upload-file="gazetteOrder--v--${status.index}" href="javascript:void(0);">Upload</a>
                                                            <span data-err-ind="gazetteOrder--v--${status.index}" class="error-msg"></span>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>

                                            <c:if test="${isPvRf or isFifthRf}">
                                            <div id="personInChargeSection--v--${status.index}">
                                                <div class="form-group ">
                                                    <div class="col-sm-5 control-label">
                                                        <label><strong>Person In Charge</strong></label>
                                                    </div>
                                                </div>

                                                <div class="form-group ">
                                                    <div class="col-sm-5 control-label">
                                                        <label for="inChargePersonName--v--${status.index}">Name <span class="mandatory otherQualificationSpan">*</span></label>
                                                    </div>
                                                    <div class="col-sm-6 col-md-7">
                                                        <input maxLength="66" type="text" autocomplete="off" name="inChargePersonName--v--${status.index}" id="inChargePersonName--v--${status.index}" value='<c:out value="${info.inChargePersonName}"/>'/>
                                                        <span data-err-ind="inChargePersonName--v--${status.index}" class="error-msg"></span>
                                                    </div>
                                                </div>

                                                <div class="form-group ">
                                                    <div class="col-sm-5 control-label">
                                                        <label for="inChargePersonDesignation--v--${status.index}">Designation <span class="mandatory otherQualificationSpan">*</span></label>
                                                    </div>
                                                    <div class="col-sm-6 col-md-7">
                                                        <input maxLength="66" type="text" autocomplete="off" name="inChargePersonDesignation--v--${status.index}" id="inChargePersonDesignation--v--${status.index}" value='<c:out value="${info.inChargePersonDesignation}"/>'/>
                                                        <span data-err-ind="inChargePersonDesignation--v--${status.index}" class="error-msg"></span>
                                                    </div>
                                                </div>

                                                <div class="form-group ">
                                                    <div class="col-sm-5 control-label">
                                                        <label for="inChargePersonEmail--v--${status.index}">Email <span class="mandatory otherQualificationSpan">*</span></label>
                                                    </div>
                                                    <div class="col-sm-6 col-md-7">
                                                        <input maxLength="66" type="text" autocomplete="off" name="inChargePersonEmail--v--${status.index}" id="inChargePersonEmail--v--${status.index}" value='<c:out value="${info.inChargePersonEmail}"/>'/>
                                                        <span data-err-ind="inChargePersonEmail--v--${status.index}" class="error-msg"></span>
                                                    </div>
                                                </div>

                                                <div class="form-group ">
                                                    <div class="col-sm-5 control-label">
                                                        <label for="inChargePersonContactNo--v--${status.index}">Contact No. <span class="mandatory otherQualificationSpan">*</span></label>
                                                    </div>
                                                    <div class="col-sm-6 col-md-7">
                                                        <input maxLength="20" type="text" autocomplete="off" name="inChargePersonContactNo--v--${status.index}" id="inChargePersonContactNo--v--${status.index}" value='<c:out value="${info.inChargePersonContactNo}"/>'/>
                                                        <span data-err-ind="inChargePersonContactNo--v--${status.index}" class="error-msg"></span>
                                                    </div>
                                                </div>
                                            </div>
                                            </c:if>

                                            <c:if test="${isPvRf}">
                                            <div id="inventoryInfoSection--v--${status.index}">
                                                <div class="form-group ">
                                                    <div class="col-sm-5 control-label">
                                                        <label><strong>Inventory Information</strong></label>
                                                    </div>
                                                </div>

                                                <div class="form-group ">
                                                    <div class="col-sm-5 control-label">
                                                        <label>OPV/Sabin 1 IM <span class="mandatory otherQualificationSpan">*</span></label>
                                                    </div>
                                                    <div class="col-sm-6 col-md-7">
                                                        <div class="form-check col-xs-4" style="margin-top: 8px">
                                                            <input type="radio" class="form-check-input" name="opvSabin1IM--v--${status.index}" id="opvSabin1IMY--v--${status.index}" data-custom-ind="opvSabin" value="Y" <c:if test="${info.opvSabin1IM eq 'Y'}">checked="checked"</c:if> />
                                                            <label for="opvSabin1IMY--v--${status.index}" class="form-check-label">Yes<span class="check-circle"></span></label>
                                                        </div>
                                                        <div class="form-check col-xs-4" style="margin-top: 8px">
                                                            <input type="radio" class="form-check-input" name="opvSabin1IM--v--${status.index}" id="opvSabin1IMN--v--${status.index}" data-custom-ind="opvSabin" value="N" <c:if test="${info.opvSabin1IM eq 'N'}">checked="checked"</c:if> />
                                                            <label for="opvSabin1IMN--v--${status.index}" class="form-check-label">No<span class="check-circle"></span></label>
                                                        </div>
                                                        <span data-err-ind="opvSabin1IM--v--${status.index}" class="error-msg"></span>
                                                    </div>
                                                </div>

                                                <div id="opvSabin1IMSubSection--v--${status.index}" data-custom-ind="opvSabinSubSection" <c:if test="${info.opvSabin1IM ne 'Y'}">style="display: none"</c:if>>
                                                    <div class="col-sm-12 control-label" style="font-size: 14px; padding: 0 0 15px 0;">
                                                        (Please specify the reason for retention of the IM and the expected destruction date.)
                                                    </div>
                                                    <div class="form-group ">
                                                        <div class="col-sm-5 control-label">
                                                            <label for="opvSabin1IMExpectedDestructDt--v--${status.index}">Expected Destruction Date <span class="mandatory otherQualificationSpan">*</span></label>
                                                        </div>
                                                        <div class="col-sm-6 col-md-7">
                                                            <input type="text" autocomplete="off" name="opvSabin1IMExpectedDestructDt--v--${status.index}" id="opvSabin1IMExpectedDestructDt--v--${status.index}" data-date-start-date="01/01/1900" value="<c:out value="${info.opvSabin1IMExpectedDestructDt}"/>" placeholder="dd/mm/yyyy" maxlength="10" class="date_picker form-control"/>
                                                            <span data-err-ind="opvSabin1IMExpectedDestructDt--v--${status.index}" class="error-msg"></span>
                                                        </div>
                                                    </div>

                                                    <div class="form-group">
                                                        <div class="col-sm-5 control-label">
                                                            <label for="opvSabin1IMRetentionReason--v--${status.index}">Reason For Retention <span class="mandatory otherQualificationSpan">*</span></label>
                                                        </div>
                                                        <div class="col-sm-6 col-md-7">
                                                            <textarea maxLength="1000" class="col-xs-12" style="margin-bottom: 15px;" name="opvSabin1IMRetentionReason--v--${status.index}" id="opvSabin1IMRetentionReason--v--${status.index}" rows="5"><c:out value="${info.opvSabin1IMRetentionReason}"/></textarea>
                                                            <span data-err-ind="opvSabin1IMRetentionReason--v--${status.index}" class="error-msg"></span>
                                                        </div>
                                                    </div>
                                                </div>

                                                <div class="form-group ">
                                                    <div class="col-sm-5 control-label">
                                                        <label>OPV/Sabin 2 IM</label>
                                                    </div>
                                                    <div class="col-sm-6 col-md-7">
                                                        <div class="form-check col-xs-4" style="margin-top: 8px">
                                                            <input type="radio" disabled class="form-check-input" name="opvSabin2IM--v--${status.index}" id="opvSabin2IMY--v--${status.index}" data-custom-ind="opvSabin" value="Y"/>
                                                            <label for="opvSabin2IMY--v--${status.index}" class="form-check-label">Yes<span class="check-circle"></span></label>
                                                        </div>
                                                        <div class="form-check col-xs-4" style="margin-top: 8px">
                                                            <input type="radio" disabled class="form-check-input" name="opvSabin2IM--v--${status.index}" id="opvSabin2IMN--v--${status.index}" data-custom-ind="opvSabin" value="N" checked="checked"/>
                                                            <label for="opvSabin2IMN--v--${status.index}" class="form-check-label">No<span class="check-circle"></span></label>
                                                        </div>
                                                        <span data-err-ind="opvSabin2IM--v--${status.index}" class="error-msg"></span>
                                                    </div>
                                                </div>

                                                <div class="form-group ">
                                                    <div class="col-sm-5 control-label">
                                                        <label>OPV/Sabin 3 IM <span class="mandatory otherQualificationSpan">*</span></label>
                                                    </div>
                                                    <div class="col-sm-6 col-md-7">
                                                        <div class="form-check col-xs-4" style="margin-top: 8px">
                                                            <input type="radio" class="form-check-input" name="opvSabin3IM--v--${status.index}" id="opvSabin3IMY--v--${status.index}" data-custom-ind="opvSabin" value="Y" <c:if test="${info.opvSabin3IM eq 'Y'}">checked="checked"</c:if> />
                                                            <label for="opvSabin3IMY--v--${status.index}" class="form-check-label">Yes<span class="check-circle"></span></label>
                                                        </div>
                                                        <div class="form-check col-xs-4" style="margin-top: 8px">
                                                            <input type="radio" class="form-check-input" name="opvSabin3IM--v--${status.index}" id="opvSabin3IMN--v--${status.index}" data-custom-ind="opvSabin" value="N" <c:if test="${info.opvSabin3IM eq 'N'}">checked="checked"</c:if> />
                                                            <label for="opvSabin3IMN--v--${status.index}" class="form-check-label">No<span class="check-circle"></span></label>
                                                        </div>
                                                        <span data-err-ind="opvSabin3IM--v--${status.index}" class="error-msg"></span>
                                                    </div>
                                                </div>

                                                <div id="opvSabin3IMSubSection--v--${status.index}" data-custom-ind="opvSabinSubSection" <c:if test="${info.opvSabin3IM ne 'Y'}">style="display: none"</c:if>>
                                                    <div class="col-sm-12 control-label" style="font-size: 14px; padding: 0 0 15px 0;">
                                                        (Please specify the reason for retention of the IM and the expected destruction date.)
                                                    </div>
                                                    <div class="form-group ">
                                                        <div class="col-sm-5 control-label">
                                                            <label for="opvSabin3IMExpectedDestructDt--v--${status.index}">Expected Destruction Date</label>
                                                            <span class="mandatory otherQualificationSpan">*</span>
                                                        </div>
                                                        <div class="col-sm-6 col-md-7">
                                                            <input type="text" autocomplete="off" name="opvSabin3IMExpectedDestructDt--v--${status.index}" id="opvSabin3IMExpectedDestructDt--v--${status.index}" data-date-start-date="01/01/1900" value="<c:out value="${info.opvSabin3IMExpectedDestructDt}"/>" placeholder="dd/mm/yyyy" maxlength="10" class="date_picker form-control"/>
                                                            <span data-err-ind="opvSabin3IMExpectedDestructDt--v--${status.index}" class="error-msg"></span>
                                                        </div>
                                                    </div>

                                                    <div class="form-group">
                                                        <div class="col-sm-5 control-label">
                                                            <label for="opvSabin3IMRetentionReason--v--${status.index}">Reason For Retention</label>
                                                            <span class="mandatory otherQualificationSpan">*</span>
                                                        </div>
                                                        <div class="col-sm-6 col-md-7">
                                                            <textarea maxLength="1000" class="col-xs-12" style="margin-bottom: 15px;" name="opvSabin3IMRetentionReason--v--${status.index}" id="opvSabin3IMRetentionReason--v--${status.index}" rows="5"><c:out value="${info.opvSabin3IMRetentionReason}"/></textarea>
                                                            <span data-err-ind="opvSabin3IMRetentionReason--v--${status.index}" class="error-msg"></span>
                                                        </div>
                                                    </div>
                                                </div>

                                                <div class="form-group ">
                                                    <div class="col-sm-5 control-label">
                                                        <label>OPV/Sabin 1 PIM <span class="mandatory otherQualificationSpan">*</span></label>
                                                    </div>
                                                    <div class="col-sm-6 col-md-7">
                                                        <div class="form-check col-xs-4" style="margin-top: 8px">
                                                            <input type="radio" class="form-check-input" name="opvSabin1PIM--v--${status.index}" id="opvSabin1PIMY--v--${status.index}" data-custom-ind="opvSabin" value="Y" <c:if test="${info.opvSabin1PIM eq 'Y'}">checked="checked"</c:if> />
                                                            <label for="opvSabin1PIMY--v--${status.index}" class="form-check-label">Yes<span class="check-circle"></span></label>
                                                        </div>
                                                        <div class="form-check col-xs-4" style="margin-top: 8px">
                                                            <input type="radio" class="form-check-input" name="opvSabin1PIM--v--${status.index}" id="opvSabin1PIMN--v--${status.index}" data-custom-ind="opvSabin" value="N" <c:if test="${info.opvSabin1PIM eq 'N'}">checked="checked"</c:if> />
                                                            <label for="opvSabin1PIMN--v--${status.index}" class="form-check-label">No<span class="check-circle"></span></label>
                                                        </div>
                                                        <span data-err-ind="opvSabin1PIM--v--${status.index}" class="error-msg"></span>
                                                    </div>
                                                </div>

                                                <div id="opvSabin1PIMSubSection--v--${status.index}" data-custom-ind="opvSabinSubSection" <c:if test="${info.opvSabin1PIM ne 'Y'}">style="display: none"</c:if>>
                                                    <div class="col-sm-12 control-label" style="font-size: 14px; padding: 0 0 15px 0;">
                                                        (For each PIM type, please specify the highest risk level associated with the work involving the PIM and the reason for retention of the PIM.)
                                                    </div>
                                                    <div class="form-group ">
                                                        <div class="col-sm-5 control-label">
                                                            <label for="opvSabin1PIMRiskLevel--v--${status.index}">Risk Level</label>
                                                            <span class="mandatory otherQualificationSpan">*</span>
                                                        </div>
                                                        <div class="col-sm-6 col-md-7">
                                                            <select name="opvSabin1PIMRiskLevel--v--${status.index}" class="opvSabin1PIMRiskLevelDropdown--v--${status.index}" id="opvSabin1PIMRiskLevel--v--${status.index}">
                                                                <option value="">Please Select</option>
                                                                <c:forEach var="type" items="${opvSabinPIMRiskLevelOps}">
                                                                    <option value="${type.value}" <c:if test="${type.value eq info.opvSabin1PIMRiskLevel}">selected="selected"</c:if> >${type.text}</option>
                                                                </c:forEach>
                                                            </select>
                                                            <span data-err-ind="opvSabin1PIMRiskLevel--v--${status.index}" class="error-msg"></span>
                                                        </div>
                                                    </div>

                                                    <div class="form-group">
                                                        <div class="col-sm-5 control-label">
                                                            <label for="opvSabin1PIMRetentionReason--v--${status.index}">Reason For Retention</label>
                                                            <span class="mandatory otherQualificationSpan">*</span>
                                                        </div>
                                                        <div class="col-sm-6 col-md-7">
                                                            <textarea maxLength="1000" class="col-xs-12" style="margin-bottom: 15px;" name="opvSabin1PIMRetentionReason--v--${status.index}" id="opvSabin1PIMRetentionReason--v--${status.index}" rows="5"><c:out value="${info.opvSabin1PIMRetentionReason}"/></textarea>
                                                            <span data-err-ind="opvSabin1PIMRetentionReason--v--${status.index}" class="error-msg"></span>
                                                        </div>
                                                    </div>
                                                </div>

                                                <div class="form-group ">
                                                    <div class="col-sm-5 control-label">
                                                        <label>OPV/Sabin 2 PIM <span class="mandatory otherQualificationSpan">*</span></label>
                                                    </div>
                                                    <div class="col-sm-6 col-md-7">
                                                        <div class="form-check col-xs-4" style="margin-top: 8px">
                                                            <input type="radio" class="form-check-input" name="opvSabin2PIM--v--${status.index}" id="opvSabin2PIMY--v--${status.index}" data-custom-ind="opvSabin" value="Y" <c:if test="${info.opvSabin2PIM eq 'Y'}">checked="checked"</c:if> />
                                                            <label for="opvSabin2PIMY--v--${status.index}" class="form-check-label">Yes<span class="check-circle"></span></label>
                                                        </div>
                                                        <div class="form-check col-xs-4" style="margin-top: 8px">
                                                            <input type="radio" class="form-check-input" name="opvSabin2PIM--v--${status.index}" id="opvSabin2PIMN--v--${status.index}" data-custom-ind="opvSabin" value="N" <c:if test="${info.opvSabin2PIM eq 'N'}">checked="checked"</c:if> />
                                                            <label for="opvSabin2PIMN--v--${status.index}" class="form-check-label">No<span class="check-circle"></span></label>
                                                        </div>
                                                        <span data-err-ind="opvSabin2PIM--v--${status.index}" class="error-msg"></span>
                                                    </div>
                                                </div>

                                                <div id="opvSabin2PIMSubSection--v--${status.index}" data-custom-ind="opvSabinSubSection" <c:if test="${info.opvSabin2PIM ne 'Y'}">style="display: none"</c:if>>
                                                    <div class="col-sm-12 control-label" style="font-size: 14px; padding: 0 0 15px 0;">
                                                        (For each PIM type, please specify the highest risk level associated with the work involving the PIM and the reason for retention of the PIM.)
                                                    </div>
                                                    <div class="form-group ">
                                                        <div class="col-sm-5 control-label">
                                                            <label for="opvSabin2PIMRiskLevel--v--${status.index}">Risk Level</label>
                                                            <span class="mandatory otherQualificationSpan">*</span>
                                                        </div>
                                                        <div class="col-sm-6 col-md-7">
                                                            <select name="opvSabin2PIMRiskLevel--v--${status.index}" class="opvSabin2PIMRiskLevelDropdown--v--${status.index}" id="opvSabin2PIMRiskLevel--v--${status.index}">
                                                                <option value="">Please Select</option>
                                                                <c:forEach var="type" items="${opvSabinPIMRiskLevelOps}">
                                                                    <option value="${type.value}" <c:if test="${type.value eq info.opvSabin2PIMRiskLevel}">selected="selected"</c:if> >${type.text}</option>
                                                                </c:forEach>
                                                            </select>
                                                            <span data-err-ind="opvSabin2PIMRiskLevel--v--${status.index}" class="error-msg"></span>
                                                        </div>
                                                    </div>

                                                    <div class="form-group">
                                                        <div class="col-sm-5 control-label">
                                                            <label for="opvSabin2PIMRetentionReason--v--${status.index}">Reason For Retention</label>
                                                            <span class="mandatory otherQualificationSpan">*</span>
                                                        </div>
                                                        <div class="col-sm-6 col-md-7">
                                                            <textarea maxLength="1000" class="col-xs-12" style="margin-bottom: 15px;" name="opvSabin2PIMRetentionReason--v--${status.index}" id="opvSabin2PIMRetentionReason--v--${status.index}" rows="5"><c:out value="${info.opvSabin2PIMRetentionReason}"/></textarea>
                                                            <span data-err-ind="opvSabin2PIMRetentionReason--v--${status.index}" class="error-msg"></span>
                                                        </div>
                                                    </div>
                                                </div>

                                                <div class="form-group ">
                                                    <div class="col-sm-5 control-label">
                                                        <label>OPV/Sabin 3 PIM <span class="mandatory otherQualificationSpan">*</span></label>
                                                    </div>
                                                    <div class="col-sm-6 col-md-7">
                                                        <div class="form-check col-xs-4" style="margin-top: 8px">
                                                            <input type="radio" class="form-check-input" name="opvSabin3PIM--v--${status.index}" id="opvSabin3PIMY--v--${status.index}" data-custom-ind="opvSabin" value="Y" <c:if test="${info.opvSabin3PIM eq 'Y'}">checked="checked"</c:if> />
                                                            <label for="opvSabin3PIMY--v--${status.index}" class="form-check-label">Yes<span class="check-circle"></span></label>
                                                        </div>
                                                        <div class="form-check col-xs-4" style="margin-top: 8px">
                                                            <input type="radio" class="form-check-input" name="opvSabin3PIM--v--${status.index}" id="opvSabin3PIMN--v--${status.index}" data-custom-ind="opvSabin" value="N" <c:if test="${info.opvSabin3PIM eq 'N'}">checked="checked"</c:if> />
                                                            <label for="opvSabin3PIMN--v--${status.index}" class="form-check-label">No<span class="check-circle"></span></label>
                                                        </div>
                                                        <span data-err-ind="opvSabin3PIM--v--${status.index}" class="error-msg"></span>
                                                    </div>
                                                </div>

                                                <div id="opvSabin3PIMSubSection--v--${status.index}" data-custom-ind="opvSabinSubSection" <c:if test="${info.opvSabin3PIM ne 'Y'}">style="display: none"</c:if>>
                                                    <div class="col-sm-12 control-label" style="font-size: 14px; padding: 0 0 15px 0;">
                                                        (For each PIM type, please specify the highest risk level associated with the work involving the PIM and the reason for retention of the PIM.)
                                                    </div>
                                                    <div class="form-group ">
                                                        <div class="col-sm-5 control-label">
                                                            <label for="opvSabin3PIMRiskLevel--v--${status.index}">Risk Level</label>
                                                            <span class="mandatory otherQualificationSpan">*</span>
                                                        </div>
                                                        <div class="col-sm-6 col-md-7">
                                                            <select name="opvSabin3PIMRiskLevel--v--${status.index}" class="opvSabin3PIMRiskLevelDropdown--v--${status.index}" id="opvSabin3PIMRiskLevel--v--${status.index}">
                                                                <option value="">Please Select</option>
                                                                <c:forEach var="type" items="${opvSabinPIMRiskLevelOps}">
                                                                    <option value="${type.value}" <c:if test="${type.value eq info.opvSabin3PIMRiskLevel}">selected="selected"</c:if> >${type.text}</option>
                                                                </c:forEach>
                                                            </select>
                                                            <span data-err-ind="opvSabin3PIMRiskLevel--v--${status.index}" class="error-msg"></span>
                                                        </div>
                                                    </div>

                                                    <div class="form-group">
                                                        <div class="col-sm-5 control-label">
                                                            <label for="opvSabin3PIMRetentionReason--v--${status.index}">Reason For Retention</label>
                                                            <span class="mandatory otherQualificationSpan">*</span>
                                                        </div>
                                                        <div class="col-sm-6 col-md-7">
                                                            <textarea maxLength="1000" class="col-xs-12" style="margin-bottom: 15px;" name="opvSabin3PIMRetentionReason--v--${status.index}" id="opvSabin3PIMRetentionReason--v--${status.index}" rows="5"><c:out value="${info.opvSabin3PIMRetentionReason}"/></textarea>
                                                            <span data-err-ind="opvSabin3PIMRetentionReason--v--${status.index}" class="error-msg"></span>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                            </c:if>
                                            </section>
                                        </c:forEach>
                                        </div>
                                        <c:if test="${isPvRf}">
                                        <div class="form-group">
                                            <div class="col-12">
                                                <a id="addNewProfileSection" style="text-decoration: none" href="javascript:void(0)">+ Add Facility Profile</a>
                                            </div>
                                        </div>
                                        </c:if>

                                        <div class="modal fade" id="invalidPostalCodeModal" role="dialog">
                                            <div class="modal-dialog modal-dialog-centered" role="document">
                                                <div class="modal-content">
                                                    <div class="modal-body">
                                                        <div class="row">
                                                            <div class="col-md-12"><span>The postal code is invalid</span></div>
                                                        </div>
                                                    </div>
                                                    <div class="modal-footer" style="justify-content: center">
                                                        <button type="button" class="btn btn-primary btn-lg" data-dismiss="modal">OK</button>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>

                                        <div class="modal fade" id="notGazetteModal" role="dialog">
                                            <div class="modal-dialog modal-dialog-centered" role="document">
                                                <div class="modal-content">
                                                    <div class="modal-body">
                                                        <div class="row">
                                                            <div class="col-md-12"><span>Please note that the facility has to be a Protected Place in order to get an Approval to Possess First Schedule Part II, Second Schedule biological agent and/or Fifth Schedule toxin unless otherwise informed by MOH</span></div>
                                                        </div>
                                                    </div>
                                                    <div class="modal-footer" style="justify-content: center">
                                                        <button type="button" class="btn btn-primary btn-lg" data-dismiss="modal">OK</button>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <jsp:invoke fragment="innerFooterFrag"/>
                            </div>
                        </div>
                        <%@include file="/WEB-INF/jsp/iais/include/jumpAfterDraft.jsp" %>
                    </div>
                </div>
            </div>
        </div>
    </div>
</form>