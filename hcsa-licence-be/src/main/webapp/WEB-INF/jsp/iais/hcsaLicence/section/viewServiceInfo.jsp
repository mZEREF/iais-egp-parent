<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<style>
    body {
        font-size: 14px;
        padding: 2%;
    }

    * {
        word-wrap: break-word
    }

    p {
        margin: 0 0 0px;
    }
    .check-square{
        border-color: #999999 !important;
    }
    .check-square:before{
        color: #999999 !important;
    }
    table {
        table-layout: fixed;
        /*word-break: break-all;*/
    }
</style>
<div class="panel-main-content service-pannel">
  <input style="display: none" value="${NOT_VIEW}" id="view">
  <input type="hidden" id="oldAppSubmissionDto" value="${appSubmissionDto.oldAppSubmissionDto==null}">
  <c:set var="appGrpPremisesDtoList" value="${appSubmissionDto.appGrpPremisesDtoList}"></c:set>
  <c:set var="oldAppGrpPremisesDtoList" value="${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList}"></c:set>

  <c:forEach items="${hcsaServiceStepSchemeDtoList}" var="hcsaServiceStepSchemeDto">
    <c:set var="currStepName" value="${hcsaServiceStepSchemeDto.stepName}" scope="request"/>
    <c:set var="currentStep" value="${hcsaServiceStepSchemeDto.stepCode}"/>
    <c:choose>
      <c:when test="${currentStep == 'SVST002'}">
        <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/keyPersonnel/viewSvcGovernanceOfficer.jsp"/>
      </c:when>
      <c:when test="${currentStep == 'SVST009'}">
        <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/keyPersonnel/viewSvcClinicalDirector.jsp"/>
      </c:when>
      <c:when test="${currentStep == 'SVST004'}">
        <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/keyPersonnel/viewSvcPrincipalOfficers.jsp"/>
      </c:when>
      <c:when test="${currentStep == 'SVST014'}">
        <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/keyPersonnel/viewSvcKeyAppointmentHolder.jsp"/>
      </c:when>
      <c:when test="${currentStep == 'SVST007'}">
        <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/keyPersonnel/viewSvcMedAlert.jsp"/>
      </c:when>
      <c:when test="${currentStep == 'SVST012'}">
        <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/businessInfo/viewSvcBusiness.jsp" />
      </c:when>
      <c:when test="${currentStep == 'SVST008'}">
        <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/viewSvcVehicle.jsp" />
      </c:when>
      <c:when test="${currentStep == 'SVST010'}">
        <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/viewSvcCharges.jsp" />
      </c:when>
      <c:when test="${currentStep == 'SVST013'}">
        <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/svcPersonnel/viewSvcSectionLeader.jsp" />
      </c:when>
      <c:when test="${currentStep == 'SVST015'}">
        <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/otherInfo/viewOtherInfo.jsp" />
      </c:when>
      <%-- <c:when test="${currentStep == 'SVST006'}">
         <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/svcPersonnel/viewSvcPersonnel.jsp" />
       </c:when>--%>
      <c:when test="${currentStep == 'SVST006'}">
        <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/svcPersonnel/viewSvcPersonnel.jsp"/>
      </c:when>
      <c:when test="${currentStep == 'SVST016'}">
        <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/supplementaryForm/viewSupplementaryForm.jsp"/>
      </c:when>
      <c:when test="${currentStep == 'SVST017'}">
        <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/specialServicesForm/viewSpecialServicesForm.jsp"/>
      </c:when>
      <c:when test="${currentStep == 'SVST005'}">
        <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/document/viewSvcDocument.jsp" />
      </c:when>
      <c:when test="${currentStep == 'SVST018'}">
        <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/outSourced/viewOutSourced.jsp" />
      </c:when>
    </c:choose>
  </c:forEach>
</div>

<script type="text/javascript">
    function showThisTableNewService(obj) {
        var $target = $(obj).closest('td');
        var w1 = $target.css('width');
        var w2 = $target.prev().css('width');
        if (w1 == w2) {
            $target.find("div.disciplinary-record").children("div").css("margin-left", "-50%");
        } else {
            $target.find("div.disciplinary-record").children("div").css("margin-left", "-29%");
        }
        $(obj).closest('div.img-show').closest('td').find("div.new-img-show").show();
    }

    function showThisTableOldService(obj) {
        var $target = $(obj).closest('td');
        var w1 = $target.css('width');
        var w2 = $target.prev().css('width');
        if (w1 == w2) {
            $target.find("div.disciplinary-record").children("div").css("margin-left", "-50%");
        } else {
            $target.find("div.disciplinary-record").children("div").css("margin-left", "-29%");
        }
        $(obj).closest('div.img-show').closest('td').find('div.old-img-show').show();
    }

    function showThisNameTableNewService(obj) {
        var $target = $(obj).closest('td');
        var h = $target.css('height');
        $target.find("div.disciplinary-record").children("div").css("margin-top", h);
        $(obj).closest('div.img-show').closest('td').find("div.new-img-show").show();
    }

    function showThisNameTableOldService(obj) {
        var $target = $(obj).closest('td');
        var h = $target.css('height');
        $target.find("div.disciplinary-record").children("div").css("margin-top", h);
        $(obj).closest('div.img-show').closest('td').find('div.old-img-show').show();
    }

</script>
