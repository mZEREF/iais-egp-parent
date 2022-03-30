<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<h3 class="col-12 pl-0" style="border-bottom: 1px solid black">Facility Profile</h3>
<%--@elvariable id="facProfile" type="sg.gov.moh.iais.egp.bsb.dto.register.facility.FacilityProfileDto"--%>
<%--@elvariable id="organizationAddress" type="sg.gov.moh.iais.egp.bsb.dto.info.common.OrgAddressInfo"--%>

<div class="form-group ">
    <div class="col-sm-5 control-label">
        <label for="facName">Facility Name</label>
        <span class="mandatory otherQualificationSpan">*</span>
    </div>
    <div class="col-sm-6 col-md-7">
        <input maxLength="250" type="text" autocomplete="off" name="facName" id="facName" value='<c:out value="${facProfile.facName}"/>'/>
        <span data-err-ind="facName" class="error-msg"></span>
    </div>
</div>


<div class="form-group ">
    <div class="col-sm-5 control-label">
        <label for="facType">Type of Facility</label>
        <span class="mandatory otherQualificationSpan">*</span>
    </div>
    <div class="col-sm-6 col-md-7">
        <select name="facType" id="facType">
            <option value="">Please Select</option>
            <c:forEach var="type" items="${facTypeOps}">
            <option value="${type.value}" <c:if test="${type.value eq facProfile.facType}">selected="selected"</c:if> >${type.text}</option>
            </c:forEach>
        </select>
        <span data-err-ind="facType" class="error-msg"></span>
    </div>
</div>

<div class="form-group ">
    <div class="col-sm-5 control-label">
        <label>Is the Facility address the same as the company address?</label>
        <span class="mandatory otherQualificationSpan">*</span>
    </div>
    <div class="col-sm-6 col-md-7">
        <div class="col-sm-4 col-md-2" style="margin-top: 8px">
            <label for="isSameAddress">Yes</label>
            <input type="radio" name="isSameAddress" id="isSameAddress" value="Y" <c:if test="${facProfile.sameAddress eq 'Y'}">checked="checked"</c:if> />
        </div>
        <div class="col-sm-4 col-md-2" style="margin-top: 8px">
            <label for="notSameAddress">No</label>
            <input type="radio" name="isSameAddress" id="notSameAddress" value="N" <c:if test="${facProfile.sameAddress eq 'N'}">checked="checked"</c:if> />
        </div>
        <span data-err-ind="isSameAddr" class="error-msg"></span>
    </div>
</div>

<div id="isSameAddrSection" <c:if test="${facProfile.sameAddress eq null}">style="display: none"</c:if>>
    <div id="isSameAddrSectionY" <c:if test="${facProfile.sameAddress ne 'Y'}">style="display: none"</c:if>>
        <div class="form-group">
            <div class="col-sm-5 control-label">
                <label for="postalCodeY">Postal Code</label>
                <span class="mandatory otherQualificationSpan">*</span>
            </div>
            <div class="col-sm-6 col-md-7">
                <label id="postalCodeY">${organizationAddress.postalCode}</label>
            </div>
        </div>

        <div class="form-group ">
            <div class="col-sm-5 control-label">
                <label for="addressTypeY">Address Type</label>
                <span class="mandatory otherQualificationSpan">*</span>
            </div>
            <div class="col-sm-6 col-md-7">
                <label id="addressTypeY"><iais:code code="${organizationAddress.addressType}"/></label>
            </div>
        </div>


        <div class="form-group ">
            <div class="col-sm-5 control-label">
                <label for="blockY">Block / House No.</label>
            </div>
            <div class="col-sm-6 col-md-7">
                <label id="blockY">${organizationAddress.blockNo}</label>
            </div>
        </div>

        <div class="form-group ">
            <div class="col-sm-5 control-label">
                <label for="floorY">Floor and Unit No.</label>
            </div>
            <div class="col-sm-6 col-md-7">
                <label id="floorY">${organizationAddress.floor} - ${organizationAddress.unitNo}</label>
            </div>
        </div>

        <div class="form-group ">
            <div class="col-sm-5 control-label">
                <label for="streetNameY">Street Name</label>
                <span class="mandatory otherQualificationSpan">*</span>
            </div>
            <div class="col-sm-6 col-md-7">
                <label id="streetNameY">${organizationAddress.street}</label>
            </div>
        </div>

        <div class="form-group ">
            <div class="col-sm-5 control-label">
                <label for="buildingNameY">Building Name</label>
                <span class="mandatory otherQualificationSpan">*</span>
            </div>
            <div class="col-sm-6 col-md-7">
                <label id="buildingNameY">${organizationAddress.building}</label>
            </div>
        </div>
    </div>
    <div id="isSameAddrSectionN" <c:if test="${facProfile.sameAddress ne 'N'}">style="display: none"</c:if>>
        <div class="form-group">
            <div class="col-sm-5 control-label">
                <label for="postalCodeN">Postal Code</label>
                <span class="mandatory otherQualificationSpan">*</span>
            </div>
            <div class="col-sm-5">
                <input maxLength="6" type="text" autocomplete="off" name="postalCode" id="postalCodeN" value='<c:out value="${facProfile.postalCode}"/>' oninput="value=value.replace(/[^\d]/g,'')"/>
                <span data-err-ind="postalCode" class="error-msg"></span>
            </div>
            <div class="col-sm-2">
                <a href="#">Retrieve your address</a>
            </div>
        </div>

        <div class="form-group ">
            <div class="col-sm-5 control-label">
                <label for="addressType">Address Type</label>
                <span class="mandatory otherQualificationSpan">*</span>
            </div>
            <div class="col-sm-6 col-md-7">
                <select name="addressType" id="addressType">
                    <option value="">Please Select</option>
                    <c:forEach var="type" items="${addressTypeOps}">
                        <option value="${type.value}" <c:if test="${type.value eq facProfile.addressType}">selected="selected"</c:if> >${type.text}</option>
                    </c:forEach>
                </select>
                <span data-err-ind="addressType" class="error-msg"></span>
            </div>
        </div>


        <div class="form-group ">
            <div class="col-sm-5 control-label">
                <label for="blockN">Block / House No.</label>
            </div>
            <div class="col-sm-6 col-md-7">
                <input maxLength="10" type="text" autocomplete="off" name="block" id="blockN" value='<c:out value="${facProfile.block}"/>'/>
                <span data-err-ind="block" class="error-msg"></span>
            </div>
        </div>

        <div class="form-group ">
            <div class="col-sm-5 control-label">
                <label for="floorN">Floor and Unit No.</label>
            </div>
            <div class="col-sm-2">
                <input maxLength="4" type="text" autocomplete="off" name="floor" id="floorN" value='<c:out value="${facProfile.floor}"/>'/>
                <span data-err-ind="floor" class="error-msg"></span>
            </div>
            <div class="hidden-xs col-sm-1" style="text-align: center">
                <p>-</p>
            </div>
            <div class="col-sm-3 col-md-4">
                <input maxLength="4" type="text" autocomplete="off" name="unitNo" id="unitNoN" value='<c:out value="${facProfile.unitNo}"/>'/>
                <span data-err-ind="unitNo" class="error-msg"></span>
            </div>
        </div>

        <div class="form-group ">
            <div class="col-sm-5 control-label">
                <label for="streetNameN">Street Name</label>
                <span class="mandatory otherQualificationSpan">*</span>
            </div>
            <div class="col-sm-6 col-md-7">
                <input maxLength="32" type="text" autocomplete="off" name="streetName" id="streetNameN" value='<c:out value="${facProfile.streetName}"/>'/>
                <span data-err-ind="streetName" class="error-msg"></span>
            </div>
        </div>

        <div class="form-group ">
            <div class="col-sm-5 control-label">
                <label for="buildingNameN">Building Name</label>
                <span class="mandatory otherQualificationSpan">*</span>
            </div>
            <div class="col-sm-6 col-md-7">
                <input maxLength="32" type="text" autocomplete="off" name="buildingName" id="buildingNameN" value='<c:out value="${facProfile.building}"/>'/>
                <span data-err-ind="buildingName" class="error-msg"></span>
            </div>
        </div>
    </div>

    <div class="form-group ">
        <div class="col-sm-5 control-label">
            <label>Is the facility a Protected Place </label>
            <a class="btn-tooltip styleguide-tooltip" href="javascript:void(0);" data-toggle="tooltip" data-html="true" title="" data-original-title="<p>Refers to a facility that has been gazetted as a Protected Place under the Infrastructure Protection Act</p>">i</a>
            <span class="mandatory otherQualificationSpan">*</span>
        </div>
        <div class="col-sm-6 col-md-7">
            <div class="form-check col-sm-4 col-md-2" style="margin-top: 8px">
                <input type="radio" class="form-check-input" name="protectedPlace" id="isAProtectedPlace" value="Y" <c:if test="${facProfile.facilityProtected eq 'Y'}">checked="checked"</c:if> />
                <label for="isAProtectedPlace" class="form-check-label">Yes<span class="check-circle"></span></label>
            </div>
            <div class="form-check col-sm-4 col-md-2" style="margin-top: 8px">
                <input type="radio" class="form-check-input" name="protectedPlace" id="notAProtectedPlace" value="N" <c:if test="${facProfile.facilityProtected eq 'N'}">checked="checked"</c:if> />
                <label for="notAProtectedPlace" class="form-check-label">No<span class="check-circle"></span></label>
            </div>
            <span data-err-ind="facilityProtected" class="error-msg"></span>
        </div>
    </div>
</div>


<div id="docUploadDiv" class="document-upload-gp" <c:if test="${facProfile.facilityProtected ne 'Y'}">style="display: none"</c:if>>
    <div class="document-upload-list">
        <h3>Gazette Order <span class="mandatory otherQualificationSpan">*</span></h3>
        <div class="file-upload-gp">
            <c:forEach var="info" items="${facProfile.savedDocMap.values()}">
                <c:set var="repoId"><iais:mask name="file" value="${info.repoId}"/></c:set>
                <div id="${repoId}FileDiv">
                    <a href="/bsb-fe/ajax/doc/download/facReg/profile/repo/${repoId}" style="text-decoration: underline"><span id="${repoId}Span">${info.filename}</span></a>(<fmt:formatNumber value="${info.size/1024.0}" type="number" pattern="0.0"/>KB)<button
                        type="button" class="btn btn-secondary btn-sm" onclick="deleteSavedFile('${repoId}')">Delete</button><button
                        type="button" class="btn btn-secondary btn-sm" onclick="reloadSavedFile('${repoId}', 'gazetteOrder')">Reload</button>
                    <span data-err-ind="${info.repoId}" class="error-msg"></span>
                </div>
            </c:forEach>
            <c:forEach var="info" items="${facProfile.newDocMap.values()}">
                <c:set var="tmpId"><iais:mask name="file" value="${info.tmpId}"/></c:set>
                <div id="${tmpId}FileDiv">
                    <a href="/bsb-fe/ajax/doc/download/facReg/profile/new/${tmpId}" style="text-decoration: underline"><span id="${tmpId}Span">${info.filename}</span></a>(<fmt:formatNumber value="${info.size/1024.0}" type="number" pattern="0.0"/>KB)<button
                        type="button" class="btn btn-secondary btn-sm" onclick="deleteNewFile('${tmpId}')">Delete</button><button
                        type="button" class="btn btn-secondary btn-sm" onclick="reloadNewFile('${tmpId}', 'gazetteOrder')">Reload</button>
                    <span data-err-ind="${info.tmpId}" class="error-msg"></span>
                </div>
            </c:forEach>
            <a class="btn file-upload btn-secondary" data-upload-file="gazetteOrder" href="javascript:void(0);">Upload</a>
            <span data-err-ind="gazetteOrder" class="error-msg"></span>
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