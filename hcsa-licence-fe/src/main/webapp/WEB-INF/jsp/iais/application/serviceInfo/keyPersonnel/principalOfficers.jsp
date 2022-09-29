<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>

<%@include file="/WEB-INF/jsp/iais/application/common/personFun.jsp" %>
<%@include file="/WEB-INF/jsp/iais/application/common/prsLoad.jsp" %>

<%--appSvcDeputyPrincipalOfficersDtoList--%>
<c:set var="personList" value="${currSvcInfoDto.appSvcPrincipalOfficersDtoList}"/>
<c:set var="dpoList" value="${currSvcInfoDto.appSvcNomineeDtoList}"/>

<input type="hidden" name="applicationType" value="${AppSubmissionDto.appType}"/>
<input type="hidden" id="isEditHiddenVal" class="person-content-edit" name="isEdit" value="${!isRfi && AppSubmissionDto.appType == 'APTY002'? '1' : '0'}"/>
<input type="hidden" class="dpo-person-content-edit" name="isEditDpo" value="${!isRfi && AppSubmissionDto.appType == 'APTY002'? '1' : '0'}"/>
<input type="hidden" class="dpo-select-edit" name="isEditDpoSelect" value="${!isRfi && AppSubmissionDto.appType == 'APTY002'? '1' : '0'}"/>

<%--<c:set var="prepsn" value="po"/>--%>
<%--<c:set var="psnContent" value="person-content"/>--%>

<div class="row form-horizontal">
    <c:if test="${AppSubmissionDto.needEditController }">
        <c:if test="${(isRfc || isRenew) && !isRfi}">
            <iais:row>
                <div class="text-right app-font-size-16">
                    <a class="back" id="RfcSkip" href="javascript:void(0);">
                        Skip<span style="display: inline-block;">&nbsp;</span><em class="fa fa-angle-right"></em>
                    </a>
                </div>
            </iais:row>
        </c:if>
        <c:set var="canEdit" value="${AppSubmissionDto.appEditSelectDto.serviceEdit}"/>
    </c:if>

    <div class="panel-group" id="Principal" role="tablist" aria-multiselectable="true">
        <div class="panel panel-default">
            <div class="panel-heading" role="tab">
                <h4 class="panel-title">
                    <a role="button" class="" data-toggle="collapse" href="#PO" aria-expanded="true" aria-controls="PO">
                        <c:out value="${currStepName}"/>
                    </a>
                </h4>
            </div>
            <div id="PO" class="panel-collapse collapse in">
                <div class="panel-body">
                    <div class="panel-main-content">
                        <iais:row>
                            <div class="col-xs-12">
                                <h2 class="app-title">Principal Officer</h2>
                                <p><h4><iais:message key="NEW_ACK024"/></h4></p>
                                <p><span class="error-msg" name="iaisErrorMSg" id="error_poPsnMandatory"></span></p>
                            </div>
                        </iais:row>

                        <c:choose>
                            <c:when test="${empty personList && currStepConfig.mandatoryCount > 1}">
                                <c:set var="personCount" value="${currStepConfig.mandatoryCount}"/>
                            </c:when>
                            <c:when test="${empty personList}">
                                <c:set var="personCount" value="1"/>
                            </c:when>
                            <c:when test="${currStepConfig.mandatoryCount > personList.size() }">
                                <c:set var="personCount" value="${currStepConfig.mandatoryCount}"/>
                            </c:when>
                            <c:otherwise>
                                <c:set var="personCount" value="${personList.size()}"/>
                            </c:otherwise>
                        </c:choose>


                        <c:forEach begin="0" end="${personCount - 1}" step="1" varStatus="status">
                            <c:set var="index" value="${status.index}"/>
                            <c:set var="person" value="${personList[index]}"/>
                            <%@include file="personnelDetail.jsp" %>
                        </c:forEach>

                        <c:if test="${!isRfi}">
                            <div class="form-group col-md-12 col-xs-12 addPoDiv">
                                <span class="addPoBtn" style="color:deepskyblue;cursor:pointer;">
                                    <span style="">+ Add Another <c:out value="${singleName}"/></span>
                                </span>
                            </div>
                        </c:if>

                        <c:choose>
                            <c:when test="${empty dpoList && dpoHcsaSvcPersonnelDto.mandatoryCount > 1}">
                                <c:set var="dpoCount" value="${dpoHcsaSvcPersonnelDto.mandatoryCount}"/>
                            </c:when>
                            <c:when test="${empty dpoList}">
                                <c:set var="dpoCount" value="1"/>
                            </c:when>
                            <c:when test="${dpoHcsaSvcPersonnelDto.mandatoryCount > dpoList.size()}">
                                <c:set var="dpoCount" value="${dpoHcsaSvcPersonnelDto.mandatoryCount}"/>
                            </c:when>
                            <c:otherwise>
                                <c:set var="dpoCount" value="${dpoList.size()}"/>
                            </c:otherwise>
                        </c:choose>

                        <c:if test="${AppSubmissionDto.needEditController }">
                            <c:forEach var="clickEditPage" items="${AppSubmissionDto.clickEditPage}">
                                <c:if test="${'APPSPN05' == clickEditPage}">
                                    <c:set var="isClickEditDpo" value="true"/>
                                </c:if>
                            </c:forEach>
                            <c:choose>
                                <c:when test="${'true' != isClickEditDpo && !(isRfc || isRenew)}">
                                    <input id="isEditDpoHiddenVal" type="hidden" name="isEditDpo" value="0"/>
                                </c:when>
                                <c:otherwise>
                                    <input id="isEditDpoHiddenVal" type="hidden" name="isEditDpo" value="1"/>
                                </c:otherwise>
                            </c:choose>
                            <c:if test="${!isClickEditDpo}">
                                <c:set var="showPreview" value="true"/>
                                <c:set var="canEditDpoEdit" value="${AppSubmissionDto.appEditSelectDto.serviceEdit && dpoHcsaSvcPersonnelDto.mandatoryCount > 0}"/>
                                <div class="<c:if test="${'true' != showPreview}">hidden</c:if>">
                                    <c:choose>
                                        <c:when test="${canEditDpoEdit}">
                                            <div class="text-right app-font-size-16" style="padding-bottom:10px;">
                                                <a id="edit-dpo" class="dpoSelectEdit" href="javascript:void(0);">
                                                    <em class="fa fa-pencil-square-o"></em><span>&nbsp;</span>Edit
                                                </a>
                                            </div>
                                        </c:when>
                                        <c:otherwise>
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                            </c:if>
                        </c:if>

                        <c:if test="${dpoHcsaSvcPersonnelDto.maximumCount > 0}">
                            <iais:row cssClass="dpoDropDownDiv">
                                <c:set var="toolMsg"><iais:message  key="NEW_ACK025"/></c:set>
                                <iais:field width="5" cssClass="col-md-5" value="${currStepName2}" info="${toolMsg}"/>
                                <iais:value width="7" cssClass="col-md-7">
                                    <iais:select cssClass="deputySelect" name="deputyPrincipalOfficer" options="DeputyFlagSelect" value="${currSvcInfoDto.deputyPoFlag}" />
                                </iais:value>
                            </iais:row>
                        </c:if>

                    </div>
                </div>
            </div>
        </div>

        <c:set var="prepsn" value="dpo"/>
        <c:set var="psnContent" value="dpo-person-content"/>
        <c:set var="singleName" value="${singleName2}"/>
        <div class="panel panel-default deputy-panel ${currSvcInfoDto.deputyPoFlag == '1' ? '' : 'hidden'}">
            <div class="panel-heading" role="tab">
                <h4 class="panel-title">
                    <a role="button" class="" data-toggle="collapse" href="#DPO" aria-expanded="true" aria-controls="DPO">
                        <c:out value="${currStepName2}"/>
                    </a>
                </h4>
            </div>
            <div id="DPO" class="panel-collapse collapse in">
                <div class="panel-body">
                    <div class="panel-main-content">
                        <iais:row>
                            <div class="col-xs-12">
                                <h2 class="app-title"><c:out value="${singleName}"/></h2>
                                <p><span class="error-msg" name="iaisErrorMSg" id="error_dpoPsnMandatory"></span></p>
                            </div>
                        </iais:row>


                        <c:forEach begin="0" end="${dpoCount - 1}" step="1" varStatus="status">
                            <c:set var="index" value="${status.index}"/>
                            <c:set var="person" value="${dpoList[index]}"/>
                            <%@include file="personnelDetail.jsp" %>
                        </c:forEach>

                        <c:if test="${!isRfi}">
                            <div class="form-group col-md-12 col-xs-12 addDpoDiv">
                                <span class="addDpoBtn" style="color:deepskyblue;cursor:pointer;">
                                    <span style="">+ Add Another <c:out value="${singleName}"/></span>
                                </span>
                            </div>
                        </c:if>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<script type="text/javascript">
    $(function() {
        let psnContent = '.person-content';
        removePersonEvent(psnContent);
        assignSelectEvent(psnContent);
        psnEditEvent(psnContent);
        profRegNoEvent(psnContent);
        $('.addPoBtn').on('click', function () {
            addPersonnel(psnContent);
        });
        <c:if test="${AppSubmissionDto.needEditController}">
        $(psnContent).each(function () {
            disablePsnContent($(this), psnContent);
        });
        disableContent('.dpoDropDownDiv');
        </c:if>
        // init page
        initPerson(psnContent);
        // check dpo
        editdpoDropDownEvent();
        deputySelectEvent();
        // dpo
        let dpoContent = '.dpo-person-content';
        removePersonEvent(dpoContent);
        assignSelectEvent(dpoContent);
        profRegNoEvent(dpoContent);
        psnEditEvent(dpoContent);
        profRegNoEvent(dpoContent);
        $('.addDpoBtn').on('click', function () {
            addPersonnel(dpoContent);
        });
        <c:if test="${AppSubmissionDto.needEditController}">
        $(dpoContent).each(function () {
            disablePsnContent($(this), dpoContent);
        });
        </c:if>
        initPerson(dpoContent);
    });

    function refreshPersonOthers($target, action) {
        if ($target.hasClass('dpo-person-content')) {
            if (action == 1) {
                removeTag('.addDpoDiv');
            } else {
                const maxDpoCount = eval('${dpoHcsaSvcPersonnelDto.maximumCount}');
                toggleTag('.addDpoDiv', $('div.dpo-person-content').length < maxDpoCount);
            }
        } else {
            if (action == 1) {
                removeTag('.addPoDiv');
            } else {
                const maxPoCount = eval('${currStepConfig.maximumCount}');
                toggleTag('.addPoDiv', $('div.person-content').length < maxPoCount);
            }
        }
    }

    var editdpoDropDownEvent = function() {
        $('#edit-dpo').click(function () {
            hideTag($('#edit-dpo'));
            unDisableContent('.dpoDropDownDiv');
            $('.dpo-select-edit').val('1');
        });
    }

    var deputySelectEvent = function () {
        $('.deputySelect').change(function () {
            let deputyFlag = $(this).val();
            let $mainContent = $(this).closest('div.panel-group');
            if ("1" == deputyFlag) {
                showTag($mainContent.find('div.deputy-panel'));
                $mainContent.find('.dpo-person-content').not(':first').remove();
                let $currContent = $mainContent.find('.dpo-person-content');
                clearFields($currContent);
                $currContent.find('.psnHeader').html('');
                checkPersonContent($currContent, true);
            } else {
                hideTag($mainContent.find('div.deputy-panel'));
            }
        });
    }
</script>
