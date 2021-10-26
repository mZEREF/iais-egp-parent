<div class="panel panel-default">
    <div class="panel-heading">
        <h4 class="panel-title">
            <strong>
                Details of Patient
            </strong>
        </h4>
    </div>
    <div id="patientDetails" class="panel-collapse collapse in">
        <div class="panel-body">
            <div class="panel-main-content form-horizontal">
                <c:set var="suffix" value="" />
                <c:set var="person" value="${patient}" />
                <%@include file="personSection.jsp" %>
                <iais:row>
                    <iais:field width="5" value="Is AR Centre aware of patient’s previous identification?" mandatory="true"/>
                    <div class="form-check col-md-3 col-xs-3">
                        <input class="form-check-input" <c:if test="${patient.previousIdentification}">checked="checked"</c:if>
                               type="radio" name="previousIdentification" value = "1" aria-invalid="false"
                               onclick="toggleOnCheck(this, 'previousData')">
                        <label class="form-check-label" ><span class="check-circle"></span>Yes</label>
                    </div>
                    <div class="form-check col-md-3 col-xs-3">
                        <input class="form-check-input" <c:if test="${!patient.previousIdentification}">checked="checked"</c:if>
                               type="radio" name="previousIdentification" value = "0" aria-invalid="false"
                               onclick="toggleOnCheck(this, 'previousData', true)">
                        <label class="form-check-label" ><span class="check-circle"></span>No</label>
                    </div>
                    <span class="error-msg col-md-7" name="iaisErrorMsg" id="error_previousIdentification"></span>
                </iais:row>
                <div id="previousData" <c:if test="${!patient.previousIdentification}">style="display:none"</c:if> >
                    <h3>Patient's Previous Identification</h3>
                    <iais:row>
                        <iais:field width="5" value="ID No." mandatory="true"/>
                        <iais:value width="3" cssClass="col-md-3">
                            <iais:select name="preIdType" firstOption="Please Select" codeCategory="CATE_ID_DS_ID_TYPE"
                                         value="${previous.idType}" cssClass="idTypeSel"/>
                        </iais:value>
                        <iais:value width="4" cssClass="col-md-4">
                            <iais:input maxLength="20" type="text" name="preIdNumber" value="${previous.idNumber}" />
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field width="5" value="Nationality" mandatory="true"/>
                        <iais:value width="4" cssClass="col-md-4">
                            <iais:select name="preNationality" firstOption="Please Select" codeCategory="CATE_ID_NATIONALITY"
                                         value="${previous.nationality}" cssClass="preNationalitySel"/>
                        </iais:value>
                        <iais:value width="3" cssClass="col-md-3" display="true">
                            <a class="retrieveIdentification" onclick="retrieveIdentification()">
                                Retrieve Identification
                            </a>
                            <input type="hidden" name="retrievePrevious" value="${not empty previous ? '1' : '0'}"/>
                        </iais:value>
                        <span class="error-msg col-md-12" name="iaisErrorMsg" id="error_retrievePrevious"></span>
                    </iais:row>
                    <iais:row>
                        <iais:field width="5" value="Name"/>
                        <iais:value width="7" cssClass="col-md-7" display="true" id="preName">
                            ${previous.name}
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field width="5" value="Date of Birth"/>
                        <iais:value width="7" cssClass="col-md-7" display="true" id="preBirthDate">
                            ${previous.birthDate}
                        </iais:value>
                    </iais:row>
                </div>
            </div>
        </div>
    </div>
</div>
<iais:confirm msg="GENERAL_ACK018" callBack="$('#noFoundDiv').modal('hide');" popupOrder="noFoundDiv" needCancel="false"
              needFungDuoJi="false"/>
<script type="text/javascript">
    function callCommonAjax(options, callback) {
        if (isEmpty(options)) {
            options = {};
        }
        var url = '${pageContext.request.contextPath}';
        if (!isEmpty(options.url)) {
            url += options.url;
        } else {
            url += '/ar/retrieve-identification';
        }
        console.log(url);
        $.ajax({
            url: url,
            dataType: 'json',
            data: options,
            type: 'POST',
            success: function (data) {
                if (typeof callback === 'function') {
                    callback(data);
                } else if (!isEmpty(callback)) {
                    callFunc(callback, data);
                }
                dismissWaiting();
            },
            error: function (data) {
                console.log("err");
                console.log(data);
                dismissWaiting();
            }
        });
    }

    function retrieveIdentification() {
        var idNo = $('input[name="preIdNumber"]').val();
        var nationality = $('#preNationality').val();
        var options = {
            idNo: idNo,
            nationality: nationality
        }
        callCommonAjax(options, previousPatientCallback);
    }

    function previousPatientCallback(data) {
        if (isEmpty(data)) {
            $('#preName').find('p').text('');
            $('#preBirthDate').find('p').text('');
            $('[name="retrievePrevious"]').val('0');
            $('#noFoundDiv').modal('show');
            return;
        }
        $('[name="retrievePrevious"]').val('1');
        $('#preName').find('p').text(data.name);
        $('#preBirthDate').find('p').text(data.birthDate);
    }

    function callFunc(func) {
        try {
            this[func].apply(this, Array.prototype.slice.call(arguments, 1));
        } catch (e) {
            console.log(e);
        }
    }

    function checkEthinicGroupMantory(nationTag, ethinicGroupLabel) {
        console.log("checkEthinicGroupMantory");
        var $selector = $(nationTag);
        var $target = $(ethinicGroupLabel);
        if ($selector.length <= 0 || $target.length <= 0) {
            return;
        }
        $target.find('.mandatory').remove();
        if ('NAT0001' == $selector.val()) {
            $target.append('<span class="mandatory">*</span>');
        }
    }
</script>