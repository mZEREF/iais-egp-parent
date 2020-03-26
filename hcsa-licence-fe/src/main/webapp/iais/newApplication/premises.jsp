<%@ page import="com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts" %>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
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
</style>
<form method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
  <%@ include file="/include/formHidden.jsp" %>

  <%--Validation fields Start--%>
  <input type="hidden" name="paramController" id="paramController" value="com.ecquaria.cloud.moh.iais.action.NewApplicationDelegator"/>
  <input type="hidden" name="valEntity" id="valEntity" value="com.ecquaria.cloud.moh.iais.dto.ApplicationValidateDto"/>
  <input type="hidden" name="valProfiles" id="valProfiles" value=""/>
  <%--Validation fields End--%>
  <div class="main-content">
    <div class="container">
      <div class="row">
        <div class="col-xs-12">
          <div class="tab-gp steps-tab">
            <%@ include file="./navTabs.jsp" %>

            <div class="tab-content  ">
              <div class="tab-pane active" id="premisesTab" role="tabpanel">
                <c:if test="${AppSubmissionDto.needEditController}">
                  <c:forEach var="clickEditPage" items="${AppSubmissionDto.clickEditPage}">
                    <c:if test="${'APPPN01' == clickEditPage}">
                      <c:set var="isClickEdit" value="true"/>
                    </c:if>
                  </c:forEach>

                  <c:choose>
                    <c:when test="${'true' != isClickEdit}">
                      <input id="isEditHiddenVal" type="hidden" name="isEdit" value="0"/>
                    </c:when>
                    <c:otherwise>
                      <input id="isEditHiddenVal" type="hidden" name="isEdit" value="1"/>
                    </c:otherwise>
                  </c:choose>
                  <c:if test="${'true' != isClickEdit}">
                    <c:set var="showPreview" value="true"/>
                    <c:set var="canEdit" value="${AppSubmissionDto.appEditSelectDto.premisesEdit}"/>
                    <div class="premises-summary-preview <c:if test="${'true' != showPreview}">hidden</c:if>">
                      <c:choose>
                        <c:when test="${'true' == canEdit}">
                          <p class="text-right"><a id="edit"><em class="fa fa-pencil-square-o"></em>Edit</a></p>
                        </c:when>
                        <c:otherwise>

                        </c:otherwise>
                      </c:choose>
                      <c:forEach var="appGrpPremDto" items="${AppSubmissionDto.appGrpPremisesDtoList}" varStatus="stat">
                        <h3 class="without-header-line">Premises ${stat.index+1}</h3>
                        <p class="premise-address-gp">
                          <span class="premise-type">
                            <strong>
                              <c:if test="${'ONSITE' == appGrpPremDto.premisesType}">
                                <c:out value="On-site"/>
                              </c:if>
                              <c:if test="${'CONVEYANCE' == appGrpPremDto.premisesType}">
                                <c:out value="Conveyance"/>
                              </c:if>
                              :
                            </strong>
                          </span>
                          <span class="premise-address">
                            <c:out value="${appGrpPremDto.address}"/>
                          </span>
                        </p>
                        <c:if test="${'CONVEYANCE' == appGrpPremDto.premisesType}">
                          <p class="vehicle-txt hidden"><strong>Vehicle No:</strong> <span class="vehicle-info">${appGrpPremDto.conveyanceVehicleNo}</span></p>
                        </c:if>
                      </c:forEach>
                    </div>
                  </c:if>
                </c:if>
                <div class="premises-content <c:if test="${'true' == showPreview}">hidden</c:if>" >
                  <div class="row ">
                    <div class="col-xs-12">
                      <div class="premises-txt">
                        <p>Premises are your service operation sites that can either be at a fixed address<strong> - &#34;on-site&#34;</strong>, or in a mobile clinic or ambulance<strong> - &#34;conveyance&#34;</strong>.</p>
                      </div>
                    </div>
                  </div>
                  <!--prem content -->
                  <%@include file="../common/premisesContent.jsp"%>
                </div>
                <div class="row">
                  <div class="col-xs-12">
                    <c:if test="${requestInformationConfig==null && 'APTY005' !=AppSubmissionDto.appType && !multiBase && 'APTY004' !=AppSubmissionDto.appType}">
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
                                      <a class="back" id="Back" href="/main-web/eservice/INTERNET/MohInternetInbox"><em class="fa fa-angle-left"></em> Back</a>
                                    </c:when>
                                    <c:otherwise>
                                      <a class="back" id="Back" href="#"><em class="fa fa-angle-left"></em> Back</a>
                                    </c:otherwise>
                                  </c:choose>
                                </div>
                                <div class="col-xs-12 col-sm-6">
                                    <div class="button-group">
                                        <c:if test="${requestInformationConfig==null}">
                                            <a class="btn btn-secondary premiseSaveDraft" id="SaveDraft" >Save as Draft</a>
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
  <%@ include file="/include/validation.jsp" %>
  <%@include file="../common/premFun.jsp"%>
  <input type="hidden" name="pageCon" value="valPremiseList" >
</form>
<script type="text/javascript">
    var init;
    $(document).ready(function() {
        <!-- init start-->
        init = 0;

        var checkedType = "";

        $('.prem-summary').addClass('hidden');

        $('.table-condensed').css("background-color","#d9edf7");

        <!--for reload -->
        $('.premTypeValue').each(function (k,v) {
            checkedType = $(this).val();
            var $premCountEle = $(this).closest('div.premContent');
            if('ONSITE'==checkedType){
                $premCountEle.find('.onSiteSelect').removeClass('hidden');
                $premCountEle.find('.conveyanceSelect').addClass('hidden');
                $premCountEle.find('.new-premise-form-conv').addClass('hidden');
                var premSelValue =  $premCountEle.find('.onSiteSelect .premSelect').val();
                if(premSelValue == "newPremise"){
                    $premCountEle.find('.new-premise-form-on-site').removeClass('hidden');
                }else if(premSelValue == "-1"){

                }else{
                    $premCountEle.find('.new-premise-form-on-site').removeClass('hidden');
                    <!--disable this form -->
                    var $premFormOnsite = $premCountEle.find('div.new-premise-form-on-site');
                    readonlyPartPage($premFormOnsite);
                    <!--hidden btn -->
                    $premCountEle.find('a.retrieveAddr').addClass('hidden');
                    $premCountEle.find('button.addPubHolDay').addClass('hidden');
                }
            }else if('CONVEYANCE' == checkedType){
                $premCountEle.find('.conveyanceSelect').removeClass('hidden');
                $premCountEle.find('.onSiteSelect').addClass('hidden');
                $premCountEle.find('.new-premise-form-on-site').addClass('hidden');
                var premSelValue =  $premCountEle.find('.conveyanceSelect .premSelect').val();
                if(premSelValue =="newPremise"){
                    $premCountEle.find('.new-premise-form-conv').removeClass('hidden');
                }else if(premSelValue == "-1"){

                }else{
                    $premCountEle.find('.new-premise-form-conv').removeClass('hidden');
                    <!--disable this form -->
                    var $premFormConveyance = $premCountEle.find('div.new-premise-form-conv');
                    readonlyPartPage($premFormConveyance);
                    <!--hidden btn -->
                    $premCountEle.find('a.retrieveAddr').addClass('hidden');
                    $premCountEle.find('button.addPubHolDay').addClass('hidden');
                }
            }
        });

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
            submit('premises','saveDraft',null);
        });
        

        <c:if test="${AppSubmissionDto.appEditSelectDto!=null && !AppSubmissionDto.appEditSelectDto.premisesEdit}">
        disabledPage();
        </c:if>

        <c:if test="${PageCanEdit}">
        var $PremEle = $('.premises-content');
        unreadonlyPartPage($PremEle);
        </c:if>
        <!-- init end-->
        init = 1;
    });






</script>



