<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-internet"/>
<%@include file="../common/dashboard.jsp" %>
<form class="form-inline" method="post" id="menuListForm" action=<%=process.runtime.continueURL()%>>
    <div class="row">
        <div class="container">
            <div class="navigation-gp">
                <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
                <input type="hidden" name="crud_action_type" value="">
                <input type="hidden" id="hiddenIndex" name="hiddenIndex" value=""/>
                <br/>
                <div class="col-xs-12">
                    <h2>Please select the type of amendment</h2>
                </div>
                <div>
                    <div class="col-xs-12">
                        <p><span class="error-msg">${ErrorMsg}</span></p>
                    </div>
                    <input type="hidden" id="amendTypeHidden" value="${AmendType}"/>
                    <div class="col-xs-12">
                        <div class="form-check">
                            <input class="form-check-input amendType" id="amend-licensee" type="radio" name="amendType"
                                   value="0"/>
                            <label class="form-check-label"><span for="amend-licensee" class="check-circle"></span>Change
                                of Licensee</label>
                        </div>
                    </div>
                    <div class="col-xs-12">
                        <div class="form-check">
                            <input class="form-check-input amendType" id="amend-licence" type="radio" name="amendType"
                                   value="1"/>
                            <label class="form-check-label"><span for="amend-licence" class="check-circle"></span>Change
                                of Licence Information</label>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div class="row">
        <div class="container">
            <div class="col-xs-12 col-md-6 text-left">
                <a class="back" id="Back" href="/main-web/eservice/INTERNET/MohInternetInbox"><em
                        class="fa fa-angle-left"></em> Back</a>
            </div>
            <div class="col-xs-12 col-md-6 text-right">
                <a class="btn btn-primary next premiseId" id="Next">Next</a>
            </div>
        </div>

    </div>

</form>
<script>
    $(document).ready(function () {

        amendType();

        $('#Next').click(function () {
            $("[name='crud_action_type_form_value']").val('doAmend');
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