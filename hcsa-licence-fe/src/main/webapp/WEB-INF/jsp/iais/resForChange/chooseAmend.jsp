<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<style>
.form-check .form-check-text {
    color: #333333;
    font-size: 1.6rem;
    font-weight: 400;
    position: relative;
    padding-left: 0px;
    min-width: 16px;
    min-height: 16px;
}
</style>
<webui:setLayout name="iais-internet"/>
<%@include file="../common/dashboard.jsp" %>
<div class="main-content">
<form class="form-inline" method="post" id="menuListForm" action=<%=process.runtime.continueURL()%>>
    <div class="container">
        <div class="row">
            <div class="center-content">
                <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
                <input type="hidden" name="crud_action_type" value="">
                <input type="hidden" id="hiddenIndex" name="hiddenIndex" value=""/>
                <br/>
                <div class="col-md-12 col-xs-12">
                    <h2>Please select the type of amendment</h2>
                </div>
                <div class="row" style="min-height: 200px;">
                    <div class="col-md-12 col-xs-12">
                        <p><span class="error-msg">${ErrorMsg}</span></p>
                    </div>
                    <input type="hidden" id="amendTypeHidden" value="${AmendType}"/>
                    <div class="col-md-6 col-xs-12">
                        <div class="form-check">
                            <input class="form-check-input amendType" id="amend-licensee" type="radio" name="amendType" <c:if test="${AmendTypeValue == '0'}">checked="checked"</c:if>
                                   value="0"/>
                            <label class="form-check-label"><span for="amend-licensee" class="check-circle"></span></label>
                            <label class="form-check-text" style="padding-top: 2%;">Change of Licensee</label>
                        </div>
                    </div>
                    <div class="col-md-6 col-xs-12">
                        <div class="form-check">
                            <input class="form-check-input amendType" id="amend-licence" type="radio" name="amendType" <c:if test="${AmendTypeValue == '1'}">checked="checked"</c:if>
                                   value="1"/>
                            <label class="form-check-label"><span for="amend-licence" class="check-circle"></span></label>
                            <label class="form-check-text" style="padding-top: 2%;">Change of Licence Information</label>
                        </div>
                    </div>
                </div>
                <div class="application-tab-footer">
                    <div class="row">
                        <div class="col-xs-12  col-md-6">
                            <p><a style="text-decoration:none;" class="back" id="Back" href="javascript:void(0);"><i class="fa fa-angle-left"></i> Back</a></p>
                        </div>
                        <div class="col-xs-12  col-md-6">
                            <div class="text-right text-center-mobile"><a class="btn btn-primary next" id="Next" href="javascript:void(0);">Next</a></div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <br/>
</form>
</div>
<script>
    $(document).ready(function () {

        amendType();

        $('#Next').click(function () {
            showWaiting();
            $("[name='crud_action_type_form_value']").val('doAmend');
            $('#menuListForm').submit();
        });

        $('#Back').click(function () {
            showWaiting();
            $("[name='crud_action_type']").val('back');
            $('#menuListForm').submit();
        });


    });


    var amendType = function () {
        $('.amendType').click(function () {
            var val = $(this).val();
            if ("1" == val) {
                $('div.amend-type-info').removeClass('hidden');
                $('div.amend-tranfer').addClass('hidden');
            } else {
                $('div.amend-type-info').addClass('hidden');
                $('div.amend-tranfer').removeClass('hidden');
            }
        });
    }

</script>