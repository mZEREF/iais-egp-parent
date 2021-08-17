
<c:set var="isRfi" value="${requestInformationConfig != null}"/>
<c:set var="stepName" value="${serviceStepDto.currentStep.stepName}" scope="request"/>

<input type="hidden" name="applicationType" value="${AppSubmissionDto.appType}"/>
<input type="hidden" name="rfiObj" value="${isRFi ? '1' : '0'}"/>
<input id="isEditHiddenVal" type="hidden" name="isEdit" value="0"/>

<div class="row">
    <div class="col-xs-12 col-md-12 text-right">
        <c:if test="${AppSubmissionDto.needEditController }">
            <input id="isEditHiddenVal" type="hidden" name="isEdit" value="0"/>
            <c:if test="${('APTY005' ==AppSubmissionDto.appType || 'APTY004' ==AppSubmissionDto.appType) && requestInformationConfig == null}">
                <div class="app-font-size-16">
                    <a class="back" id="RfcSkip">Skip<span style="display: inline-block;">&nbsp;</span><em class="fa fa-angle-right"></em></a>
                </div>
            </c:if>
            <c:set var="canEdit" value="${AppSubmissionDto.appEditSelectDto.serviceEdit}"/>
        </c:if>
    </div>
</div>

<div class="row">
    <h4><iais:message key="NEW_ERR0033"/></h4>

    <div class="form-group">
        <div class="row control control-caption-horizontal">
            <div class=" form-group form-horizontal formgap">
                <div class="control-label formtext col-md-5 col-xs-5">
                    <label  class="control-label control-set-font control-font-label">
                        <div style="font-weight: 600;font-size: 2.2rem">
                            <strong><c:out value="${stepName}"/></strong>
                        </div>
                    </label>
                </div>
            </div>
        </div>
    </div>

    <c:choose>
    <c:when test="${empty sectionLeaderList}">
        <c:set var="pageLength" value="1"/>
    </c:when>
    <c:when test="${sectionLeaderConfig.mandatoryCount > sectionLeaderList.size() }">
        <c:set var="pageLength" value="${sectionLeaderConfig.mandatoryCount}"/>
    </c:when>
    <c:otherwise>
        <c:set var="pageLength" value="${sectionLeaderList.size()}"/>
    </c:otherwise>
    </c:choose>
    <input type="hidden" name="slLength" value="${pageLength}" />

    <c:forEach begin="0" end="${pageLength-1}" step="1" varStatus="slStat">
        <c:set var="index" value="${slStat.index}" />
        <c:set var="sectionLeader" value="${sectionLeaderList[index]}"/>
        <%@include file="sectionLeaderDetail.jsp" %>
    </c:forEach>

    <c:if test="${!isRfi}">
        <c:choose>
            <c:when test="${!empty sectionLeaderList}">
                <c:set var="slLength" value="${sectionLeaderList.size()}"/>
            </c:when>
            <c:otherwise>
                <c:choose>
                    <c:when test="${AppSubmissionDto.needEditController}">
                        <c:set var="slLength" value="0"/>
                    </c:when>
                    <c:otherwise>
                        <c:set var="slLength" value="${sectionLeaderConfig.mandatoryCount}"/>
                    </c:otherwise>
                </c:choose>
            </c:otherwise>
        </c:choose>
        <c:set var="needAddPsn" value="true"/>
        <c:choose>
            <c:when test="${sectionLeaderConfig.status =='CMSTAT003'}">
                <c:set var="needAddPsn" value="false"/>
            </c:when>
            <c:when test="${cdLength >= sectionLeaderConfig.maximumCount}">
                <c:set var="needAddPsn" value="false"/>
            </c:when>
            <c:when test="${AppSubmissionDto.needEditController && !canEdit}">
                <c:set var="needAddPsn" value="false"/>
            </c:when>
        </c:choose>
        <div class="col-md-12 col-xs-12 addSectionLeaderDiv <c:if test="${!needAddPsn}">hidden</c:if>">
            <span class="addSectionLeaderBtn" style="color:deepskyblue;cursor:pointer;">
                <span style="">+ Add Another ${stepName}</span>
            </span>
        </div>
    </c:if>
    <script type="text/javascript">
        $(document).ready(function() {
            initSectionLeader();
        });

        function initSectionLeader() {
            addSectionLeaderBtn();
            removeSectionLeader();
            doEdite();
        }

        function refreshSeadLeaderIndex() {
            var slLength = $('.sectionLaderContent').length;
            console.info("length: " + slLength)
            $('input[name="slLength"]').val(slLength);
            if (slLength <= '${sectionLeaderConfig.mandatoryCount}') {
                $('.addSectionLeaderBtn').hide();
            } else {
                $('.addSectionLeaderBtn').show();
            }
            //reset number
            var regExp = /([a-zA-Z_]*)/g;
            var $content = $('div.sectionLaderContent');
            $content.each(function (k,v) {
                //$(this).find('.assign-psn-item').html(k+1);
                var $selector = $(this).find(':input');
                if ($selector.length == 0) {
                    return;
                }
                $selector.each(function () {
                    var type = this.type, tag = this.tagName.toLowerCase(), $input = $(this);
                    var orgName = $input.attr('name');
                    var result = regExp.exec(orgName);
                    var name = !isEmpty(result) && result.length > 0 ? result[0] : orgName;
                    $input.prop('name', name + k);
                    if (orgName == $input.attr('id')) {
                    $input.prop('id', name + k);
                    }
                    if (tag == 'select') {
                        $input.niceSelect("update");
                    }
                });

                /*$(this).find('.salutation').prop('name','salutation'+k);
                $(this).find('.name').prop('name','chassisNum'+k);
                $(this).find('.qualification').prop('name','engineNum'+k);
                $(this).find('.wrkExpYear').prop('name','vehicleIndexNo'+k);
                $(this).find('.slIndexNo').prop('name','slIndexNo'+k);
                $(this).find('.isPartEdit').prop('name','isPartEdit'+k);*/
                <%--<c:if test="${AppSubmissionDto.appType == 'APTY002'}" >
                if (k == 0) {
                    $(this).find('.addSectionLeaderBtn').hide();
                }
                </c:if>--%>
            });
            <c:if test="${!isRfi && (AppSubmissionDto.appType == 'APTY002' || canEdit)}">
            // display add more
            if (slLength < '${sectionLeaderConfig.maximumCount}') {
                $('.addSectionLeaderBtn').show();
            } else {//hidden add more
                $('.addSectionLeaderBtn').hide();
            }
            </c:if>
            $content.each(function (k,v) {
                if (slLength <= 1 && k == 0) {
                    $(this).find('.assign-psn-item').html('');
                } else {
                    $(this).find('.assign-psn-item').html(k+1);
                }
            });
        }

        function addSectionLeaderBtn () {
            $('.addSectionLeaderBtn').unbind('click');
            $('.addSectionLeaderBtn').click(function () {
                showWaiting();
                var slLength = $('.sectionLaderContent').length;

                $.ajax({
                    url: '${pageContext.request.contextPath}/section-leader-html',
                    dataType: 'json',
                    data: {
                        "slLength": slLength
                    },
                    type: 'POST',
                    success: function (data) {
                        if ('200' == data.resCode) {
                            $('.addSectionLeaderDiv').before(data.resultJson+'');
                            // init
                            $('div.sectionLaderContent select').niceSelect();
                            initSectionLeader();
                            refreshSeadLeaderIndex();
                            $('#isEditHiddenVal').val('1');
                        }
                        dismissWaiting();
                    },
                    error: function (data) {
                        console.log("err");
                        dismissWaiting();
                    }
                });
            });
        };

        function removeSectionLeader () {
            $('.removeBtn').unbind('click');
            $('.removeBtn').click(function () {
                showWaiting();
                var slLength = $('.sectionLaderContent').length;
                if (slLength <= '${sectionLeaderConfig.mandatoryCount}') {
                    dismissWaiting();
                    return;
                }
                var $currContent = $(this).closest('div.sectionLaderContent');
                $currContent.remove();
                $('#isEditHiddenVal').val('1');
                refreshSeadLeaderIndex();
                dismissWaiting();
            });
        }

        var doEdite = function () {
            $('.editDiv a').click(function () {
                var $currContent = $(this).closest('div.sectionLaderContent');
                unDisableContent($currContent);
            });
        }
    </script>
</div>

