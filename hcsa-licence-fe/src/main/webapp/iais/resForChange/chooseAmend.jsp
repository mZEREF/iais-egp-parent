<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-internet"/>
<br/>
<div class="tab-pane" id="tabApp" role="tabpanel">
    <form class="form-inline" method="post" id="menuListForm" action=<%=process.runtime.continueURL()%>>
        <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
        <input type="hidden" name="crud_action_type" value="">
        <input type="hidden" id="hiddenIndex" name="hiddenIndex" value=""/>
        <div class="row col-xs-12">
            <div class="col-md-3"></div>
            <h2>Please select the type of changes to be made</h2>
            <span class="error-msg">${ErrorMsg}</span>
        </div>
        <div class="main-content">
            <div class="container">
                <div class="row col-xs-12 text-">
                    <div class="row">
                        <div class="col-xs-12 col-md-3">
                        </div>
                        <div class="col-xs-12 col-md-6">
                            <div class="col-xs-6 col-md-6">
                                <input type="hidden" id="amendTypeHidden" value="${AmendType}"/>
                                <div class="form-check">
                                    <input class="form-check-input amendType" id="amend-licensee" type="radio"
                                           name="amendType" value="0"/>
                                    <label class="form-check-label"><span for="amend-licensee"
                                                                          class="check-circle"></span>amend
                                        licensee</label>
                                </div>
                            </div>
                            <div class="col-xs-6 col-md-6">
                                <div class="form-check">
                                    <input class="form-check-input amendType" id="amend-licence" type="radio"
                                           name="amendType" value="1"/>
                                    <label class="form-check-label"><span for="amend-licence"
                                                                          class="check-circle"></span>amend
                                        licence</label>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="row hidden amend-type-info">
                        <div class="col-xs-12 col-md-4">
                        </div>
                        <div class="col-xs-12 col-md-4">
                            <div class="self-assessment-checkbox-gp gradient-light-grey">
                                <p class="assessment-title">Amend Type</p>
                                <div class="form-check-gp">
                                    <c:forEach var="amendType" items="${AmendTypeList}">
                                        <div class="form-check">
                                            <input class="form-check-input" name="amend-licence-type" id="clinical"
                                                   type="checkbox" aria-invalid="false" value="${amendType.value}">
                                            <label class="form-check-label" for="clinical"><span
                                                    class="check-square"></span>${amendType.text}</label>
                                        </div>
                                    </c:forEach>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <br/>
                <div class="row">
                    <div class="col-xs-12 col-md-4">
                        <div class="col-xs-12">
                            <a class="back" id="Back"><em class="fa fa-angle-left"></em> Back</a>
                        </div>
                    </div>
                    <div class="col-xs-10 col-md-3">
                        <div class="components">
                            <a class="btn btn-secondary " id="continue">Continue</a>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <div class="row">
        </div>
    </form>
</div>
<script>
    $(document).ready(function () {

        amendType();

        $('#continue').click(function () {
            $("[name='crud_action_type_form_value']").val('doAmend');
            $('#menuListForm').submit();
        });

        var amendTypeVal = $('#amendTypeHidden').val();
        if ("0" == amendTypeVal) {
            $('#amend-licensee').prop("checked", true);
            $('div.amend-type-info').addClass('hidden');
        } else if ("1" == amendTypeVal) {
            $('#amend-licence').prop("checked", true);
            $('div.amend-type-info').removeClass('hidden');
        }


    });


    var amendType = function () {
        $('.amendType').click(function () {
            var val = $(this).val();
            if ("1" == val) {
                $('div.amend-type-info').removeClass('hidden');
            } else {
                $('div.amend-type-info').addClass('hidden');
            }
        });
    }

</script>