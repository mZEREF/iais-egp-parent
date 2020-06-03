<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
  sop.webflow.rt.api.BaseProcessClass process =
          (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-internet"/>
<form method="post" id="menuListForm" action=<%=process.runtime.continueURL()%>>
  <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
  <input type="hidden" name="crud_action_type_form_value" value="">
  <input type="hidden" name="crud_action_type_value" value="">

  <%@include file="dashboard.jsp" %>
  <%@include file="../common/dashboard.jsp" %>
  <%--Validation fields Start--%>
  <input type="hidden" name="paramController" id="paramController"
         value="com.ecquaria.cloud.moh.iais.action.NewApplicationDelegator"/>
  <input type="hidden" name="valEntity" id="valEntity" value="com.ecquaria.cloud.moh.iais.dto.ApplicationValidateDto"/>
  <input type="hidden" name="valProfiles" id="valProfiles" value=""/>
  <input type="hidden" name="licenceName" value=""/>
  <%--Validation fields End--%>
  <div class="main-content">
    <div class="container">
      <div class="row">
        <div class="col-xs-12">
          <div class="">
            <div class="">
              <div class="tab-pane active" id="premisesTab" role="tabpanel">
                <div class="row premContent">
                  <div class="col-xs-12" style="margin-top:3%;">
                    <div>
                      <h2>${PremisesListQueryDto.premisesType}:${PremisesListQueryDto.address}</h2>
                    </div>
                  </div>
                </div>
                <div class="form-check col-sm-12">
                  <ul>
                    <li>Changes made will be applied to other licences associated with this premises:</li>
                  </ul>
                </div>
                <!--todo:slect prem issue -->
                <%@include file="../common/premisesContent.jsp"%>
                  <div class="application-tab-footer">
                    <div class="row">
                      <div class="col-xs-12 col-sm-6 ">
                        <c:choose>
                          <c:when test="${requestInformationConfig != null}">
                            <a class="back" id="back" href="/main-web/eservice/INTERNET/MohInternetInbox"><em class="fa fa-angle-left"></em> Back</a>
                          </c:when>
                          <c:otherwise>
                            <a class="back" id="back"><em class="fa fa-angle-left"></em> Back</a>
                          </c:otherwise>
                        </c:choose>
                      </div>
                      <div class="col-xs-12 col-sm-6">
                        <div class="button-group">
                          <a class="btn btn-primary next" id="previewAndSub">Preview and Submit</a>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    <input type="text" style="display: none" value="${AckMessage}" id="ackMessage" name="ackMessage">
    <iais:confirm msg="There is a pending application for a licence associated to this premises" callBack="cancel()"  needCancel="false" popupOrder="ackMessageConfim"></iais:confirm>
      <%--Validation Field--%>
      <%@ include file="/WEB-INF/jsp/include/validation.jsp" %>
      <%@include file="../common/premFun.jsp"%>
    </div>
  </div>
</form>


<script>
    var init;
    $(document).ready(function () {
        <!-- init start-->
        if($('#ackMessage').val()=='ACKMESSAGE'){
            $('#ackMessageConfim').modal('show');
        }
        init = 0;
        <!--for reload -->
        reloadPage();
        cl();
        retrieveAddr();
        premSelect();
        <c:if test="${PageCanEdit}">
          var $PremEle = $('#mainPrem');
          unreadonlyPartPage($PremEle);
        </c:if>

        <!-- init end-->
        init = 1;
    });

    function cancel(){
        $('#ackMessageConfim').modal('hide');
    }
    $('#previewAndSub').click(function () {
        doSubmitForm('prePayment','', '');
    });

    $('#back').click(function () {
        doBack('prePremisesList');
    });

    function readonlyPartPage($Ele) {
        $Ele.find('input[type="radio"]').prop('readonly',true);
        $Ele.find('input[type="text"]').prop('readonly',true);
        $Ele.find('input[type="file"]').prop('readonly',true);
        $Ele.find('input[type="checkbox"]').prop('readonly',true);
        $Ele.find('div.nice-select').addClass('disabled');
        <!--add disabled bg color-->
        $Ele.find('input[type="text"]').css('border-color','#ededed');
        $Ele.find('input[type="text"]').css('color','#999');
        $Ele.find('.date_picker').unbind();
    }

    function unreadonlyPartPage($Ele) {
        $Ele.find('input[type="radio"]').prop('readonly',false);
        $Ele.find('input[type="text"]').prop('readonly',false);
        $Ele.find('input[type="file"]').prop('readonly',false);
        $Ele.find('input[type="checkbox"]').prop('readonly',false);
        $Ele.find('div.nice-select').removeClass('disabled');
        <!--remove disabled bg color-->
        $Ele.find('input[type="text"]').css('border-color','');
        $Ele.find('input[type="text"]').css('color','');
        $Ele.find('.date_picker').datepicker({
            format:"dd/mm/yyyy"
        });
    }

    var unbindAllTabs = function () {
        $('#premises').unbind();
        $('#premisesli').unbind();
        $('#documents').unbind();
        $('#documentsli').unbind();
        $('#serviceForms').unbind();
        $('#serviceFormsli').unbind();
        $('#preview').unbind();
        $('#previewli').unbind();
        $('#payment').unbind();
        $('#paymentli').unbind();
    }



</script>