<style>
    input.disabled-placeHolder::-webkit-input-placeholder { /* WebKit, Blink, Edge */
        color:#999999 !important;
    }
    .disabled-placeHolder:-moz-placeholder { /* Mozilla Firefox 4 to 18 */
        color:#999999!important;
    }
    .disabled-placeHolder::-moz-placeholder { /* Mozilla Firefox 19+ */
        color:#999999!important;
    }
    input.disabled-placeHolder:-ms-input-placeholder { /* Internet Explorer 10-11 */
        color:#999999!important;
    }
    input.disabled-placeHolder::-ms-input-placeholder { /* Microsoft Edge */
        color:#999999!important;
    }
    .radio-disabled::before{
        background-color: #999999 !important;
        /*border: 1px solid #999999 !important;*/
    }
    .radio-disabled{
        border-color: #999999 !important;
    }
    input.allDay {
        -ms-transform: scale(2,2); /* IE */
        -moz-transform: scale(2,2); /* FireFox */
        -webkit-transform: scale(2,2); /* Safari and Chrome */
        -o-transform: scale(2,2); /* Opera */
    }

    .input-padding {
        padding-right:5px;
        padding-left: 15px;
    }

    .label-padding {
        padding-left: 0px;
        padding-top: 14px;
        margin-left: -3px;
    }

    .multi-sel-padding {
        padding-bottom: 15px;
    }

    .all-day-position {
        margin: 14px 20px 20px 5px;
        padding: 0;
    }

</style>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<input type="hidden" name="applicationType" value="${AppSubmissionDto.appType}"/>
<input type="hidden" name="rfiObj" value="<c:if test="${requestInformationConfig == null}">0</c:if><c:if test="${requestInformationConfig != null}">1</c:if>"/>

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

    <c:set var="premBusinessMap" value="${premAlignBusinessMap}"/>
    <c:set var="maxCount" value="${maxCount}"/>

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
                            <c:out value="${appGrpPremisesDto.premisesType}"/>: <c:out value="${appGrpPremisesDto.address}"/>
                        </strong>
                    </h4>
                </div>
                <div class="businessContent panel-collapse collapse in"  role="tabpanel" aria-labelledby="business-heading">
                    <input type="hidden" class ="isPartEdit" name="isPartEdit${status.index}" value="0"/>
                    <input type="hidden" class="businessIndexNo" name="businessIndexNo${status.index}" value="${businessDto.businessIndexNo}"/>
                    <input type="hidden" class="currService" name="currService${status.index}" value="${serviceCode}">
                    <div class="row panel-body" style="padding-left: 6%">
                        <div class="panel-main-content">
                            <iais:row cssClass="edit-content">
                                <c:if test="${canEdit}">
                                    <div class="text-right app-font-size-16">
                                        <a class="edit psnEdit" href="javascript:void(0);">
                                            <em class="fa fa-pencil-square-o"></em><span>&nbsp;</span>Edit
                                        </a>
                                    </div>
                                </c:if>
                            </iais:row>

                            <iais:row>
                                <c:set var="info"><iais:message key="NEW_ACK028"></iais:message></c:set>
                                <iais:field width="5" mandatory="true" value="Business Name" info="${info}"/>
                                <iais:value width="7" cssClass="col-md-7">
                                    <iais:input cssClass="businessName" maxLength="100" type="text" name="businessName${status.index}" value="${businessDto.businessName}"></iais:input>
                                </iais:value>
                            </iais:row>
                            <iais:row>
                                <iais:field width="5" mandatory="true" value="Contact No."/>
                                <iais:value width="7" cssClass="col-md-7">
                                    <iais:input  cssClass="contactNo" maxLength="8" type="text" name="contactNo${status.index}" value="${businessDto.contactNo}"></iais:input>
                                </iais:value>
                            </iais:row>
                            <iais:row>
                                <iais:field width="5" mandatory="true" value="Email"/>
                                <iais:value width="7" cssClass="col-md-7">
                                    <iais:input cssClass="emailAddr" maxLength="100" type="text" name="emailAddr${status.index}" value="${businessDto.emailAddr}"></iais:input>
                                </iais:value>
                            </iais:row>
                            <%@include file="operatingHours.jsp"%>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </c:forEach>
</div>

<%@include file="businessFun.jsp"%>
<script>

    $(function() {
        $('.addWeekly').on('click', function () {
            addWeekly($(this).closest('div.weeklyContent'));
        });

        $('.addPubHolDay').on('click', function () {
            addPubHolDay($(this).closest('div.pubHolDayContent'));
        });

        $('.addEvent').on('click', function () {
            addEvent($(this).closest('div.eventContent'));
        });

        clickAllDay();
        removeWeekly();
        removePh();
        removeEvent();

        $("input.allDay").each(function (){
            var $allDayDiv = $(this).closest('div.all-day-div');
            if($(this).is(':checked')){
                disabeleForAllDay($allDayDiv);
            }else{
                unDisableContent($allDayDiv.siblings('.start-div'));
                unDisableContent($allDayDiv.siblings('.end-div'));
            }
        })

    });

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