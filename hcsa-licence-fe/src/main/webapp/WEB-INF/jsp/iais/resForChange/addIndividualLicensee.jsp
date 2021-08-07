
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%
  sop.webflow.rt.api.BaseProcessClass process =
          (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>
<webui:setLayout name="iais-internet"/>
<br/>
<%@include file="../common/dashboard.jsp" %>
<form method="post"  id="mainForm" action=<%=process.runtime.continueURL()%>>
  <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
  <c:set var="companyType" value="LICTSUB001" />
  <c:set var="individualType" value="LICTSUB002" />
  <div class="main-content">
    <div class="container">
      <div class="row">
        <div class="col-xs-12">
          <div class="tab-gp">
            <div class="tab-content">
              <div class="tab-pane fade in active">
                <div class="row form-horizontal">
                  <iais:row>
                    <iais:value width="6">
                      <strong class="app-font-size-22 premHeader">Licensee Details:</strong>
                    </iais:value>
                  </iais:row>
                <%@ include file="/WEB-INF/jsp/iais/common/licenseeDetailContent.jsp" %>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
  <div class="row">
    <div class="container">
      <div class="col-xs-12 col-md-6 text-left">
        <a class="back" href="/hcsa-licence-web/eservice/INTERNET/MohRequestForChange/prepareTranfer"><em class="fa fa-angle-left"></em> Back</a>
      </div>
      <div class="col-xs-12 col-md-6 text-right">
        <a class="btn btn-primary next premiseId" id="Next" href="javascript:void(0);">Next</a>
      </div>
    </div>
  </div>
  <br/>
  <%@ include file="/WEB-INF/jsp/include/validation.jsp" %>
</form>



<iais:confirm msg="NEW_ACK016" needCancel="false" callBack="$('#postalCodePop').modal('hide');"
              popupOrder="postalCodePop" needEscapHtml="false" needFungDuoJi="false" />
<script>

    $("#Next").click(function () {
        showWaiting();
        document.getElementById("mainForm").submit();
    });
    function  showWaiting(){
        $.blockUI({message: '<div style="padding:3px;">We are processing your request now; please do not click the Back or Refresh button in the browser.</div>',
            css: {width: '25%', border: '1px solid #aaa'},
            overlayCSS: {opacity: 0.2}});
    }
    $('.retrieveAddr').click(function() {
        var $postalCodeEle = $(this).closest('div.postalCodeDiv');
        var postalCode = $postalCodeEle.find('.postalCode').val();
        retrieveAddr(postalCode, $(this).closest('div.licensee-detail').find('div.address'));
    });
    function retrieveAddr(postalCode, target) {
        var $addressSelectors = $(target);
        var re=new RegExp('^[0-9]*$');
        var data = {
            'postalCode':postalCode
        };
        showWaiting();
        $.ajax({
            'url':'${pageContext.request.contextPath}/retrieve-address',
            'dataType':'json',
            'data':data,
            'type':'GET',
            'success':function (data) {
                if(data == null){
                    // $postalCodeEle.find('.postalCodeMsg').html("the postal code information could not be found");
                    //show pop
                    $('#postalCodePop').modal('show');
                    handleVal($addressSelectors.find(':input'), '', false);
                } else {
                    handleVal($addressSelectors.find('input[name="blkNo"]'), data.blkHseNo, true);
                    handleVal($addressSelectors.find('input[name="streetName"]'), data.streetName, true);
                    handleVal($addressSelectors.find('input[name="buildingName"]'), data.buildingName, true);
                }
                dismissWaiting();
            },
            'error':function () {
                //show pop
                $('#postalCodePop').modal('show');
                handleVal($addressSelectors.find(':input'), '', false);
                dismissWaiting();
            }
        });
    }
    function handleVal(selector, val, readonly) {
        $(selector).val(val);
        $(selector).prop('readonly', readonly);
    }

</script>


