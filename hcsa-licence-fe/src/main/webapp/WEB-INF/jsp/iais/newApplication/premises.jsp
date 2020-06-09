<%@ page import="com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts" %>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib prefix="ias" uri="http://www.ecq.com/iais" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%
  //handle to the Engine APIs
  sop.webflow.rt.api.BaseProcessClass process =
          (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>

<webui:setLayout name="iais-internet"/>
<%@ include file="./dashboard.jsp" %>
<style>
  .app-font-size-22{
    font-size: 22px;
  }
  .ack-font-16{
    font-size: 16px;
  }
</style>
<form method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
  <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>

  <%--Validation fields Start--%>
  <input type="hidden" name="paramController" id="paramController" value="com.ecquaria.cloud.moh.iais.action.NewApplicationDelegator"/>
  <input type="hidden" name="valEntity" id="valEntity" value="com.ecquaria.cloud.moh.iais.dto.ApplicationValidateDto"/>
  <input type="hidden" name="valProfiles" id="valProfiles" value=""/>
  <input type="hidden" name="crud_action_type_continue" value="">
  <%--Validation fields End--%>
  <div class="main-content">
    <div class="container">
      <div class="row">
        <div class="col-xs-12">
          <div class="tab-gp steps-tab">
            <%@ include file="./navTabs.jsp" %>
            <div class="tab-content  ">
              <div class="tab-pane active" id="premisesTab" role="tabpanel">
                  <input id="isEditHiddenVal" type="hidden" name="isEdit" value="0"/>
                <%--<c:if test="${AppSubmissionDto.needEditController}">--%>
                  <%--<c:forEach var="clickEditPage" items="${AppSubmissionDto.clickEditPage}">--%>
                    <%--<c:if test="${'APPPN01' == clickEditPage}">--%>
                      <%--<c:set var="isClickEdit" value="true"/>--%>
                    <%--</c:if>--%>
                  <%--</c:forEach>--%>

                  <%--<c:choose>--%>
                    <%--<c:when test="${'true' != isClickEdit}">--%>
                      <%--<input id="isEditHiddenVal" type="hidden" name="isEdit" value="0"/>--%>
                    <%--</c:when>--%>
                    <%--<c:otherwise>--%>
                      <%--<input id="isEditHiddenVal" type="hidden" name="isEdit" value="1"/>--%>
                    <%--</c:otherwise>--%>
                  <%--</c:choose>--%>
                  <%--<c:if test="${'true' != isClickEdit}">--%>
                    <%--<c:set var="showPreview" value="true"/>--%>
                    <%--<c:set var="canEdit" value="${AppSubmissionDto.appEditSelectDto.premisesEdit}"/>--%>
                    <%--<div class="premises-summary-preview <c:if test="${'true' != showPreview}">hidden</c:if>">--%>
                      <%--<c:choose>--%>
                        <%--<c:when test="${'true' == canEdit}">--%>
                          <%--<p class="text-right"><a id="edit"><em class="fa fa-pencil-square-o"></em><span style="display: inline-block;">&nbsp;</span>Edit</a></p>--%>
                        <%--</c:when>--%>
                        <%--<c:otherwise>--%>

                        <%--</c:otherwise>--%>
                      <%--</c:choose>--%>
                      <%--<c:forEach var="appGrpPremDto" items="${AppSubmissionDto.appGrpPremisesDtoList}" varStatus="stat">--%>
                        <%--<h3 class="without-header-line">Premises ${stat.index+1}</h3>--%>
                        <%--<p class="premise-address-gp">--%>
                          <%--<span class="premise-type">--%>
                            <%--<strong>--%>
                              <%--<c:if test="${'ONSITE' == appGrpPremDto.premisesType}">--%>
                                <%--<c:out value="On-site: "/>--%>
                              <%--</c:if>--%>
                              <%--<c:if test="${'CONVEYANCE' == appGrpPremDto.premisesType}">--%>
                                <%--<c:out value="Conveyance: "/>--%>
                              <%--</c:if>--%>
                              <%--<c:if test="${'OFFSIET' == appGrpPremDto.premisesType}">--%>
                                <%--<c:out value="Off-site: "/>--%>
                              <%--</c:if>--%>
                            <%--</strong>--%>
                          <%--</span>--%>
                          <%--<span class="premise-address">--%>
                            <%--<c:out value="${appGrpPremDto.address}"/>--%>
                          <%--</span>--%>
                        <%--</p>--%>
                        <%--<c:if test="${'CONVEYANCE' == appGrpPremDto.premisesType}">--%>
                          <%--<p class="vehicle-txt hidden"><strong>Vehicle No:</strong> <span class="vehicle-info">${appGrpPremDto.conveyanceVehicleNo}</span></p>--%>
                        <%--</c:if>--%>
                      <%--</c:forEach>--%>
                    <%--</div>--%>
                  <%--</c:if>--%>
                <%--</c:if>--%>
                <div class="premises-content <%--<c:if test="${'true' == showPreview}">hidden</c:if>--%>" >
                  <div class="row ">
                    <div class="col-xs-12">
                      <div class="premises-txt">
                        <p>Premises are your service operation sites that can either be at a fixed address<strong> - &#34;on-site&#34;</strong>, or in a mobile clinic or ambulance<strong> - &#34;conveyance&#34;</strong>, or provided from a remote location <strong> - &#34;off-site&#34;</strong>.</p>
                      </div>
                    </div>
                  </div>
                  <c:if test="${AppSubmissionDto.appType=='APTY005'||AppSubmissionDto.appType=='APTY004'}">
                    <c:if test="${fn:length(appGrpPremisesDto.licenceDtos)>0}">
                      <div class="form-check col-sm-12">
                        <ul>
                          <li>Changes made will be applied to other licences associated with this premises:</li>
                        </ul>
                      </div>
                    </c:if>
                  </c:if>
                  <!--prem content -->
                  <%@include file="../common/premisesContent.jsp"%>
                </div>
                <div class="row">
                  <div class="col-xs-12" id="addPremBody" hidden>
                    <%--<c:if test="${requestInformationConfig == null && 'APTY005' != AppSubmissionDto.appType && !multiBase && 'APTY004' != AppSubmissionDto.appType && !AppSubmissionDto.onlySpecifiedSvc}">--%>
                    <c:if test="${requestInformationConfig == null && 'APTY005' != AppSubmissionDto.appType && !multiBase && 'APTY004' != AppSubmissionDto.appType }">
                      <button id="addPremBtn" class="btn btn-primary" type="button">Add Premises</button>
                    </c:if>
                  </div>
                </div>
                <div class="application-tab-footer">
                    <c:choose>
                        <c:when test="${('APTY005' ==AppSubmissionDto.appType || 'APTY004' ==AppSubmissionDto.appType) && requestInformationConfig == null}">
                            <%@include file="../common/rfcFooter.jsp"%>
                        </c:when>
                        <c:otherwise>
                            <div class="row">
                                <div class="col-xs-12 col-sm-6 ">
                                  <c:choose>
                                    <c:when test="${DraftConfig != null}">
                                      <a class="back" id="Back" href="/main-web/eservice/INTERNET/MohInternetInbox?initPage=initApp"><em class="fa fa-angle-left"></em> Back</a>
                                    </c:when>
                                    <c:otherwise>
                                      <a class="back" id="Back" href="#"><em class="fa fa-angle-left"></em> Back</a>
                                    </c:otherwise>
                                  </c:choose>
                                </div>
                                <div class="col-xs-12 col-sm-6">
                                    <div class="button-group">
                                        <c:if test="${requestInformationConfig==null}">
                                          <input type="text" style="display: none; " id="selectDraftNo" value="${selectDraftNo}">
                                          <input type="text" style="display: none; " id="saveDraftSuccess" value="${saveDraftSuccess}">
                                          <a class="btn btn-secondary premiseSaveDraft" id="SaveDraft"    >Save as Draft</a>
                                        </c:if>
                                        <a class="btn btn-primary next premiseId" id="Next" >Next</a></div>
                                </div>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
  <%--Validation Field--%>
  <%@ include file="/WEB-INF/jsp/include/validation.jsp" %>
  <%@include file="../common/premFun.jsp"%>
  <input type="hidden" name="pageCon" value="valPremiseList" >
  <%--<c:if test="${ not empty selectDraftNo }">
    <iais:confirm msg="There is an existing draft for the chosen service, if you choose to continue, the draft application will be discarded." callBack="cancelSaveDraft()" popupOrder="saveDraft"  yesBtnDesc="Resume from draft" cancelBtnDesc="Continue" cancelBtnCls="btn btn-primary" yesBtnCls="btn btn-secondary" cancelFunc="saveDraft()"></iais:confirm>
  </c:if>--%>

  <ias:confirm msg="This application has been saved successfully" callBack="cancel()" popupOrder="saveDraft" yesBtnDesc="continue" cancelBtnDesc="exit to inbox" cancelBtnCls="btn btn-primary" yesBtnCls="btn btn-secondary" cancelFunc="jumpPage()"></ias:confirm>

  <input type="text" style="display: none" value="${AckMessage}" id="ackMessage" name="ackMessage">
  <iais:confirm msg="There is a pending application for a licence associated to this premises" callBack="" popupOrder="ackMessageConfim"></iais:confirm>
  <input type="text" style="display:none;" value="${hciNameUsed}" name="hciNameUsedInput" id="hciNameUsedInput">
  <iais:confirm msg="The HCI name you have keyed in is currently in used" needCancel="false" callBack="Continue()" popupOrder="hciNameUsed" yesBtnDesc="Continue" ></iais:confirm>

</form>
<script type="text/javascript">
    var init;
    $(document).ready(function() {
        cl();
        $("select[name='onSiteAddressType']").trigger('change');
        $("select[name='conveyanceAddrType']").trigger('change');
        <!-- init start-->
        init = 0;
        if($('#ackMessage').val()=='ACKMESSAGE'){
            $('#ackMessageConfim').modal('show');
        }
        if($('#saveDraftSuccess').val()=='success'){
            $('#saveDraft').modal('show');
        }
        if($('#hciNameUsedInput').val()=='hciNameUsed'){
            $('#hciNameUsed').modal('show');
        }
        var checkedType = "";

        $('.prem-summary').addClass('hidden');

        $('.table-condensed').css("background-color","#d9edf7");

        reloadPage();

        premType();

        premSelect();

        retrieveAddr();

        removePremises();

        doEdit();

        otherLic();

        addPubHolDay();

        removePH();

        //Binding method
        $('#Back').click(function(){
            submit(null,'back',null);
        });
        $('#Next').click(function(){
            $('input[type="radio"]').prop('disabled',false);
            submit('documents',null,null);
        });
        $('#SaveDraft').click(function(){
                $('input[type="radio"]').prop('disabled',false);
                submit('premises','saveDraft',$('#selectDraftNo').val());
        });
<%--<c:if test="${AppSubmissionDto.needEditController || AppSubmissionDto.onlySpecifiedSvc}">--%>
        <c:if test="${AppSubmissionDto.needEditController }">
        readonlyPartPage($('div.premises-content'));
        </c:if>

        <%--<c:if test="${PageCanEdit}">--%>
        <%--var $PremEle = $('.premises-content');--%>
        <%--unreadonlyPartPage($PremEle);--%>
        <%--</c:if>--%>
        <!-- init end-->
        init = 1;
    });


    $("#onSiteSel").change(function(){
        $("#addPremBody").removeAttr("hidden");
    })
    var oval = $("#onSiteSel").val();
    var cval = $("#conveyanceSel").val();
    var offval =  $('#offSiteSel').val();
    console.log("oval"+cval);

    if (oval != -1 || cval != -1 || offval != -1) {
        $("#addPremBody").removeAttr("hidden");
    }
    $("#conveyanceSel").change(function(){
        $("#addPremBody").removeAttr("hidden");
    })

    $('#offSiteSel').change(function () {
        $("#addPremBody").removeAttr("hidden");
    });


    function saveDraft() {
        $('input[type="radio"]').prop('disabled',false);
        submit('premises','saveDraft',$('#selectDraftNo').val());
    }

  function cancelSaveDraft() {
      submit('premises','saveDraft','cancelSaveDraft');
  }

  function cancel() {
      $('#saveDraft').modal('hide');
  }

  function jumpPage() {
      submit('premises','saveDraft','jumpPage');
  }

  function Continue() {
      $('#hciNameUsed').modal('hide');
      $("[name='crud_action_type_continue']").val("continue");
      submit('documents',null,'rfcSaveDraft');
  }

</script>



