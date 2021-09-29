<%--
  Created by IntelliJ IDEA.
  User: ZiXian
  Date: 2021/6/18
  Time: 15:42
  To change this template use File | Settings | File Templates.
--%>
<input type="hidden" name="applicationType" value="${AppSubmissionDto.appType}"/>
<input type="hidden" name="rfiObj" value="<c:if test="${requestInformationConfig == null}">0</c:if><c:if test="${requestInformationConfig != null}">1</c:if>"/>

<div class="row">
    <div class="col-xs-12 col-md-12 text-right">
        <c:if test="${AppSubmissionDto.needEditController }">
            <input id="isEditHiddenVal" type="hidden" name="isEdit" value="0"/>
            <c:if test="${('APTY005' ==AppSubmissionDto.appType || 'APTY004' ==AppSubmissionDto.appType) && requestInformationConfig == null}">
                <div class="app-font-size-16">
                    <a class="back" id="RfcSkip" href="javascript:void(0);">
                        Skip<span style="display: inline-block;">&nbsp;</span><em class="fa fa-angle-right"></em>
                    </a>
                </div>
            </c:if>
            <c:set var="canEdit" value="${AppSubmissionDto.appEditSelectDto.serviceEdit}"/>
        </c:if>
    </div>
</div>

<c:set var="premBusinessMap" value="${premAlignBusinessMap}"/>
<c:forEach var="appGrpPremisesDto" items="${AppSubmissionDto.appGrpPremisesDtoList}" varStatus="status">
    <c:forEach var="premBusinessItem" items="${premBusinessMap}" varStatus="premBusinessStatus">
        <c:if test="${premBusinessItem.key == appGrpPremisesDto.premisesIndexNo}">
            <c:set var="businessDto" value="${premBusinessItem.value}"/>
        </c:if>
    </c:forEach>
    <div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true">
        <div class="panel panel-default">
            <div class="panel-heading " id="business-heading"  role="tab">
                <h4 class="panel-title">
                    <strong >
                        <c:choose>
                            <c:when test="${'ONSITE' == appGrpPremisesDto.premisesType}">
                                <c:out value="Premises"/>: <c:out value="${appGrpPremisesDto.address}"/>
                            </c:when>
                            <c:when test="${'CONVEYANCE' == appGrpPremisesDto.premisesType}">
                                <c:out value="Conveyance"/>: <c:out value="${appGrpPremisesDto.address}"/>
                            </c:when>
                            <c:when test="${'OFFSITE'  == appGrpPremisesDto.premisesType}">
                                <c:out value="Off-site"/>: <c:out value="${appGrpPremisesDto.address}"/>
                            </c:when>
                            <c:when test="${'EASMTS'  == appGrpPremisesDto.premisesType}">
                                <c:out value="Conveyance"/>: <c:out value="${appGrpPremisesDto.address}"/>
                            </c:when>
                        </c:choose>
                    </strong>
                </h4>
            </div>
            <div class="businessContent panel-collapse collapse in"  role="tabpanel" aria-labelledby="business-heading">
                <input type="hidden" class ="isPartEdit" name="isPartEdit${status.index}" value="0"/>
                <input type="hidden" class="businessIndexNo" name="businessIndexNo${status.index}" value="${businessDto.businessIndexNo}"/>
                <div class="row panel-body" style="padding-left: 6%">
                    <div class="panel-main-content">
                        <div class="col-md-12 col-xs-12">
                            <div class="edit-content">
                                <c:if test="${'true' == canEdit && (!isRfi || (isRfi && appGrpPremisesDto.rfiCanEdit))}">
                                    <p>
                                    <div class="text-right app-font-size-16">
                                        <a class="edit businessEdit" href="javascript:void(0);">
                                            <em class="fa fa-pencil-square-o"></em><span>&nbsp;</span>Edit
                                        </a>
                                    </div>
                                    </p>
                                </c:if>
                            </div>
                        </div>
                        <div class="col-md-12 col-xs-12">
                            <div class="row control control-caption-horizontal">
                                <div class=" form-group form-horizontal formgap">
                                    <div class="control-label formtext col-md-5 col-xs-5">
                                        <label  class="control-label control-set-font control-font-label">Business Name <a class="btn-tooltip styleguide-tooltip" href="javascript:void(0);" data-toggle="tooltip" data-html="true" title="&lt;p&gt;<iais:message key="NEW_ACK028"></iais:message>&lt;/p&gt;">i</a></label>
                                        <span class="mandatory">*</span>
                                    </div>
                                    <div class="col-md-7 col-xs-12">
                                        <iais:input cssClass="businessName" maxLength="100" type="text" name="businessName${status.index}" value="${businessDto.businessName}"></iais:input>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</c:forEach>

<script>
    $(document).ready(function () {
        doEdite();

        var appType = $('input[name="applicationType"]').val();
        var rfiObj = $('input[name="rfiObj"]').val();
        //rfc,renew,rfi
        if (('APTY005' == appType || 'APTY004' == appType) || '1' == rfiObj) {
            disabledPage();
        }
    });

    var doEdite = function () {
        $('a.businessEdit').click(function () {
            var $currContent = $(this).closest('div.businessContent');
            $currContent.find('input.isPartEdit').val('1');
            $currContent.find('.edit-content').addClass('hidden');
            $currContent.find('input[type="text"]').prop('disabled', false);
            $currContent.find('input[type="text"]').css('border-color', '');
            $currContent.find('input[type="text"]').css('color', '');

            $('#isEditHiddenVal').val('1');
        });
    }
</script>