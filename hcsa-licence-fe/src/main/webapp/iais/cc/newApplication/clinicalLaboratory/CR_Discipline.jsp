<c:set value="${reloadLaboratoryDisciplines}" var="reloadData"/>
<c:forEach var="appGrpPremisesDto" items="${AppSubmissionDto.appGrpPremisesDtoList}" varStatus="status">
  <c:choose>
    <c:when test="${appGrpPremisesDto.hciName != null && appGrpPremisesDto.hciName != ''}">
      <c:set value="${appGrpPremisesDto.hciName}" var="premIndexNo"/>
    </c:when>
    <c:when test="${appGrpPremisesDto.conveyanceVehicleNo != null && appGrpPremisesDto.conveyanceVehicleNo != ''}">
      <c:set value="${appGrpPremisesDto.conveyanceVehicleNo}" var="premIndexNo"/>
    </c:when>
  </c:choose>
  <p>Please select the service disciplines you would like to apply at your premises${status.index+1}.</p>
  <span class="error-msg" name="iaisErrorMsg" id="error_checkError"></span>
<div class="wrapper">
  <div class="form-inner-content editableMode">
    <div id="overDiv" style="position:absolute; visibility:hidden; z-index:1000;"></div>
    <%--<form  name="formRender" method="post" target="_parent">--%>
      <div class="canvasContent">
        <!-- Place holder for tabbed interface. -->
        <input type="hidden" id="testCode" value="HST"/>
        <div id="formPanel" class="sopform ui-tabs ui-widget ui-widget-content ui-corner-all" style="display: block;">
          <div class="form-tab-panel ui-tabs-panel ui-widget-content ui-corner-bottom" id="tab_page_0">
            <div id="control--runtime--0" class="page control control-area  container-p-1">
              <div id="control--runtime--0--errorMsg_page_top" class="error_placements"></div>
              <table class="control-grid"><tbody><tr height="100%"><td class="first last" style="width: 100%;"><div id="control--runtime--1" class="control control-caption-horizontal">
                <div class="control-label-span control-set-alignment">
                  <label id="control--runtime--1--label" class="control-label control-set-font control-font-label"></label>
                  <span class="upload_controls"></span>
                </div>
                <div class="control-input-span control-set-alignment">
                  <div class="normal-indicator">
                    <table class="check-${premIndexNo}">
                      <tbody>
                      <c:forEach var="levelOneList" items="${HcsaSvcSubtypeOrSubsumedDto}" varStatus="levelOne">
                        <c:set var="checkIndexNo1" value="${premIndexNo};${levelOneList.name};${levelOneList.code};${levelOneList.parentId}"/>
                        <c:set var="reloadIndexNo1" value="${currentServiceId}${premIndexNo}${levelOneList.id}"/>
                        <!--one -->
                        <tr>
                          <td>
                            <div class="control-item-container parent-form-check" data-parent="<c:out value="${premIndexNo}${levelOneList.type}${levelOneList.name}" />" >
                              <input type="checkbox"
                             <c:if test="${reloadData[reloadIndexNo1] != null && reloadData[reloadIndexNo1] != ''}">
                              checked="checked"
                             </c:if>
                                     id="<c:out value="control--${levelOne.index}--${levelOne.index}" />"
                                     name="<c:out value="${premIndexNo}control--runtime--1" />" class="control-input"
                                     value="<c:out value="${checkIndexNo1}" />">
                              <label  for="<c:out value="control--${levelOne.index}--${levelOne.index}" />" data-code="<c:out value="${premIndexNo}${levelOneList.type}${levelOneList.name}" />" class="control-label control-set-font control-font-normal" />
                              <span class="check-square"></span><c:out value="${levelOneList.name}" />
                              </label>
                              <input class="checkValue" type="hidden" name="<c:out value="${checkIndexNo1}" />" value="<iais:mask name="${checkIndexNo1}" value="${levelOneList.id}"/>"/>
                            </div>
                          </td>
                        </tr>
                        <c:if test="${not empty levelOneList.list}">
                          <c:forEach var="levelTwoList" items="${levelOneList.list}" varStatus="levelTwo">
                            <c:set var="checkIndexNo2" value="${premIndexNo};${levelTwoList.name};${levelTwoList.code};${levelTwoList.parentId}"/>
                            <c:set var="reloadIndexNo2" value="${currentServiceId}${premIndexNo}${levelTwoList.id}"/>
                            <!--two -->
                            <tr>
                              <td>
                                <div class="control-item-container sub-form-check parent-form-check disabled" data-parent="<c:out value="${premIndexNo}${levelTwoList.type}${levelTwoList.name}"/>" data-child="<c:out value="${premIndexNo}${levelOneList.type}${levelOneList.name}"/>" >
                                  <input type="checkbox"
                                  <c:if test="${reloadData[reloadIndexNo2] != null && reloadData[reloadIndexNo2] != ''}">
                                         checked="checked"
                                  </c:if>
                                         id="<c:out value="control--${levelTwo.begin}--${levelTwo.index}"/>"
                                         name="<c:out value="${premIndexNo}control--runtime--1"/>" class="control-input"
                                         value="<c:out value="${checkIndexNo2}" />">
                                  <label  for="<c:out value="control--${levelTwo.index}--${levelTwo.index}"/>" class="control-label control-set-font control-font-normal" />
                                    <span class="check-square"></span><c:out value="${levelTwoList.name}"/>
                                  </label>
                                  <input class="checkValue" type="hidden" name="<c:out value="${checkIndexNo2}"/>" value="<iais:mask name="${checkIndexNo2}" value="${levelTwoList.id}"/>"/>
                                </div>
                              </td>
                            </tr>
                            <c:if test="${not empty levelTwoList.list}">
                              <!--three -->
                              <c:forEach var="levelThreeList" items="${levelTwoList.list}" varStatus="levelThree">
                                <c:set var="checkIndexNo3" value="${premIndexNo};${levelThreeList.name};${levelThreeList.code};${levelThreeList.parentId}"/>
                                <c:set var="reloadIndexNo3" value="${currentServiceId}${premIndexNo}${levelThreeList.id}"/>
                                <tr>
                                  <td>
                                    <div class="control-item-container sub-form-check double parent-form-check disabled" data-parent="<c:out value="${premIndexNo}${levelThreeList.type}${levelThreeList.name}"/>" data-child="<c:out value="${premIndexNo}${levelTwoList.type}${levelTwoList.name}"/>" >
                                      <input type="checkbox"
                                      <c:if test="${reloadData[reloadIndexNo3] != null && reloadData[reloadIndexNo3] != ''}">
                                             checked="checked"
                                      </c:if>
                                             id="<c:out value="control--${levelThree.index}--${levelThree.index}"/>"
                                             name="<c:out value="${premIndexNo}control--runtime--1"/>" class="control-input"
                                             value="<c:out value="${checkIndexNo3}"/>">
                                      <label  for="<c:out value="control--${levelThree.index}--${levelThree.index}"/>" class="control-label control-set-font control-font-normal">
                                        <span class="check-square"></span><c:out value="${levelThreeList.name}"/>
                                      </label>
                                      <input class="checkValue" type="hidden" name="<c:out value="${checkIndexNo3}"/>" value="<iais:mask name="${checkIndexNo3}" value="${levelThreeList.id}"/>"/>
                                    </div>
                                  </td>
                                </tr>
                              </c:forEach>
                            </c:if>
                          </c:forEach>
                        </c:if>
                      </c:forEach>
                      </tbody></table>
                  </div>
                  <div id="control--runtime--1--errorMsg_right" style="display: none;" class="error_placements"></div>
                </div>
              </div></td></tr></tbody></table>
              <div id="control--runtime--0--errorMsg_page_bottom" class="error_placements"></div>
            </div></div><div id="demo" style="display:none;" class="ui-tabs-panel ui-widget-content ui-corner-bottom ui-tabs-hide"></div>
        </div>
      </div>
    <%--</form>--%>
  </div>
</div>
  <br/>
</c:forEach>
<script>
    $(document).ready(function () {

        $('input[type="checkbox"]:checked').each(function () {
            var parentID = $(this).closest('.parent-form-check').attr('data-parent');
            $('.sub-form-check[data-child="'+parentID+'"]').removeClass('disabled');
        });
    });

    /*var checkboxControl = function () {
        $('.parent-form-check input[type="checkbox"]').on('change', function() {
            alert(0);
            var parentID = $(this).closest('.parent-form-check').attr('data-parent');
            if ($(this).prop('checked') == true) {
                $('.sub-form-check[data-child="'+parentID+'"]').removeClass('disabled');
            } else {

                $('.sub-form-check[data-child="'+parentID+'"] input[type="checkbox"]').prop('checked', false);
                if($('.sub-form-check[data-child="'+parentID+'"]').hasClass('parent-form-check')) {
                    var secondChild = $('.sub-form-check[data-child="'+parentID+'"]').attr("data-parent");
                    $('.sub-form-check[data-child="'+secondChild+'"] input[type="checkbox"]').prop('checked', false);
                    $('.sub-form-check[data-child="'+secondChild+'"]').addClass('disabled');
                }
                $('.sub-form-check[data-child="'+parentID+'"]').addClass('disabled');

            }
        });
    };*/

</script>