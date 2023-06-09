<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<div class="otherInfoPageContent otherInfoContent" data-prefix="${prefix}">
    <input type="hidden" class ="isPartEdit" name="isPartEdit" value="0"/>
    <input type="hidden" class="otherInfoMedId" name="otherInfoMedId" value="${med.id}"/>
    <div class="col-md-12 col-xs-12">
        <div class="edit-content">
            <c:if test="${canEdit}">
                <div class="text-right app-font-size-16">
                    <a class="edit otherInfoDSEdit" href="javascript:void(0);">
                        <em class="fa fa-pencil-square-o"></em><span>&nbsp;</span>Edit
                    </a>
                </div>
            </c:if>
        </div>
    </div>
    <input name="itSystem" value="" type="hidden">
    <input name="paperSystem" value="" type="hidden">
    <iais:row>
        <iais:field width="5" cssClass="col-md-5" mandatory="true" value="Type of medical records"/>
        <div class="form-check col-md-3">
            <input class="form-check-input medicalTypeIt" name="${prefix}medicalTypeIt" value="1"
                   type="checkbox" aria-invalid="false"
                   <c:if test="${med.medicalTypeIt eq true}">checked="checked"</c:if> onclick="showListItSystem('${prefix}');" data-prefix="${prefix}"/>
            <label class="form-check-label">
                <span class="check-square"></span><c:out value="IT System"/>
            </label>
        </div>
        <div class="form-check col-md-3">
            <input class="form-check-input" name="${prefix}medicalTypePaper" value="1"
                   type="checkbox" aria-invalid="false"
                   <c:if test="${med.medicalTypePaper eq true}">checked="checked"</c:if> />
            <label class="form-check-label">
                <span class="check-square"></span><c:out value="Paper cards"/>
            </label>
        </div>
    </iais:row>
    <iais:row cssClass="row control control-caption-horizontal">
        <iais:field width="5" cssClass="col-md-5" mandatory="" value=""/>
        <iais:value width="7" cssClass="col-md-7 col-xs-12">
            <span class="error-msg" name="iaisErrorMsg" id="error_${prefix}medicalTypeIt"></span>
        </iais:value>
    </iais:row>
    <div class="ListByItSystem <c:if test="${med.medicalTypeIt eq false || empty med.medicalTypeIt}">hidden</c:if>" data-prefix="${prefix}">
        <iais:row>
            <iais:field width="5" cssClass="col-md-5" mandatory="true" value="List of options for IT system and paper cards / IT system only"/>
            <iais:value width="7" cssClass="col-md-7">
                <iais:select id="systemOption" cssClass="systemOption" name="${prefix}systemOption" codeCategory="CATE_ID_OTHER_OPTION"
                             value="${med.systemOption}" firstOption="Please Select" onchange="toggleOnVal(this, 'MED06', '.otherVal')"/>
            </iais:value>
        </iais:row>
        <iais:row cssClass="row control control-caption-horizontal">
            <iais:field width="5" cssClass="col-md-5" mandatory="" value=""/>
            <iais:value width="7" cssClass="col-md-7 col-xs-12">
                <span class="error-msg" name="iaisErrorMsg" id="error_${prefix}systemOption"></span>
            </iais:value>
        </iais:row>
        <iais:row cssClass="${med.systemOption eq 'MED06' ? '' : 'hidden'} otherVal">
            <iais:field width="5" cssClass="col-md-5" mandatory="true" value="Please specify"/>
            <iais:value width="7" cssClass="col-md-7">
                <iais:input maxLength="50" type="text" cssClass="otherSystemOption" name="${prefix}otherSystemOption" value="${med.otherSystemOption}"/>
            </iais:value>
        </iais:row>
    </div>

    <iais:row>
        <iais:field width="5" cssClass="col-md-5" mandatory="true" value="Is clinic open to general public?"/>
        <iais:value width="3" cssClass="form-check col-md-3">
            <input class="form-check-input openToPublic" <c:if test="${true eq med.openToPublic}">checked="checked"</c:if>  type="radio" name="${prefix}openToPublic" value = "1" aria-invalid="false">
            <label class="form-check-label" ><span class="check-circle"></span>Yes</label>
        </iais:value>

        <iais:value width="3" cssClass="form-check col-md-3">
            <input class="form-check-input openToPublic" <c:if test="${false eq med.openToPublic}">checked="checked"</c:if>  type="radio" name="${prefix}openToPublic" value = "0" aria-invalid="false">
            <label class="form-check-label" ><span class="check-circle"></span>No</label>
        </iais:value>
    </iais:row>
    <iais:row cssClass="row control control-caption-horizontal">
        <iais:field width="5" cssClass="col-md-5" mandatory="" value=""/>
        <iais:value width="7" cssClass="col-md-7 col-xs-12">
            <span class="error-msg" name="iaisErrorMsg" id="error_${prefix}openToPublic"></span>
        </iais:value>
    </iais:row>

    <iais:row>
        <iais:field width="5" cssClass="col-md-5" mandatory="true" value="GFA Value (in sqm)"/>
        <iais:value width="7" cssClass="col-md-7">
            <iais:input maxLength="7" type="text" cssClass="gfaValue" name="${prefix}gfaValue" value="${med.gfaValue}"/>
        </iais:value>
    </iais:row>

    <iais:row>
        <iais:field width="5" cssClass="col-md-5" mandatory="true" value="I declare that I have met URA's requirements for gross floor area"/>
        <div class="form-check col-md-3">
            <input class="form-check-input" name="${prefix}dsDeclaration" value="0"
                   type="checkbox" aria-invalid="false"
                   <c:if test="${'0' eq dsDeclaration}">checked="checked"</c:if> />
            <label class="form-check-label">
                <span class="check-square"></span><c:out value=""/>
            </label>
        </div>
    </iais:row>
    <iais:row cssClass="row control control-caption-horizontal">
        <iais:field width="5" cssClass="col-md-5" mandatory="" value=""/>
        <iais:value width="7" cssClass="col-md-7 col-xs-12">
            <span class="error-msg" name="iaisErrorMsg" id="error_${prefix}dsDeclaration"></span>
        </iais:value>
    </iais:row>
</div>
<script>
    function showListItSystem(prefix) {
        let res = $('input.medicalTypeIt[data-prefix="' + prefix + '"]').prop("checked");
        console.log("itSystemIsCheck:"+res);
        if (res == true){
            $('div.ListByItSystem[data-prefix="' + prefix + '"]').removeClass("hidden");
        }else {
            $("#systemOption option:first").prop("selected", 'selected').val("");
            $("#systemOption").val("");
            $(".ListByItSystem .current").text("Please Select");
            $('div.ListByItSystem[data-prefix="' + prefix + '"]').addClass("hidden");
        }
    }

    function toggleOnVal(sel, val, elem) {
        toggleOnSelect(sel, val, $(sel).closest('.form-group').siblings(elem));
    }
</script>

