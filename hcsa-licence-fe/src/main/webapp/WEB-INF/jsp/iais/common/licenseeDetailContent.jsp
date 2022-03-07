<div class="licensee-detail">
  <iais:row cssClass="company-no ${dto.licenseeType == companyType ? '' : 'hidden'}">
    <iais:field width="5" value="UEN No."/>
    <iais:value width="7" cssClass="col-md-7">
      <iais:code code="${dto.uenNo}" />
    </iais:value>
  </iais:row>

  <iais:row cssClass="ind-no ${dto.licenseeType == individualType ? '' : 'hidden'}">
    <iais:field width="5" mandatory="true" value="ID No."/>
    <iais:value width="3" cssClass="col-md-3">
      <iais:select name="idType" firstOption="Please Select" codeCategory="CATE_ID_ID_TYPE" value="${dto.idType}"
                   cssClass="idTypeSel"/>
    </iais:value>
    <iais:value width="4" cssClass="col-md-4">
      <iais:input maxLength="20" type="text" name="idNumber" value="${dto.idNumber}" />
    </iais:value>
  </iais:row>

  <iais:row cssClass="ind-no nationalityDiv ${dto.licenseeType == individualType ? '' : 'hidden'}">
    <iais:field width="5" mandatory="true" value="Nationality"/>
    <iais:value width="7" cssClass="col-md-7">
      <iais:select name="nationality" firstOption="Please Select" codeCategory="CATE_ID_NATIONALITY"
                   cssClass="nationality" value="${dto.nationality}" />
    </iais:value>
  </iais:row>

  <iais:row>
    <iais:field value="Licensee Name" mandatory="true" width="5"/>
    <iais:value width="7" cssClass="col-md-7">
      <iais:input maxLength="110" type="text" name="licenseeName" id="licenseeName" value="${dto.licenseeName}"/>
    </iais:value>
  </iais:row>

  <%-- Address start --%>
  <iais:row cssClass="postalCodeDiv">
    <iais:field value="Postal Code" mandatory="true" width="5"/>
    <iais:value width="4" cssClass="col-md-4">
      <iais:input cssClass="postalCode" maxLength="6" type="text" name="postalCode" value="${dto.postalCode}" />
    </iais:value>
    <iais:value width="3" cssClass="col-md-3">
      <p><a class="retrieveAddr">Retrieve your address</a></p>
    </iais:value>
  </iais:row>
  <iais:row>
    <iais:field value="Address Type" mandatory="true" width="5"/>
    <iais:value width="7" cssClass="col-md-7">
      <iais:select name="addrType" codeCategory="CATE_ID_ADDRESS_TYPE" firstOption="Please Select"
                   value="${dto.addrType}" cssClass="addrTypeSel"/>
    </iais:value>
  </iais:row>
  <iais:row cssClass="address">
    <iais:field value="Block / House No." width="5" cssClass="blkNoLabel"/>
    <iais:value width="7" cssClass="col-md-7">
      <iais:input maxLength="10" type="text" name="blkNo" id="blkNo" value="${dto.blkNo}"/>
    </iais:value>
  </iais:row>
  <iais:row>
    <iais:field value="Floor / Unit No." width="5" cssClass="floorUnitLabel"/>
    <iais:value width="3" cssClass="col-md-3">
      <iais:input maxLength="3" type="text" name="floorNo" id="floorNo" value="${dto.floorNo}"/>
    </iais:value>
    <div class="col-sm-4 col-xs-1 col-md-1 text-center"><p>-</p></div>
    <iais:value width="3" cssClass="col-md-3">
      <iais:input maxLength="5" type="text" name="unitNo" id="unitNo" value="${dto.unitNo}"/>
    </iais:value>
  </iais:row>
  <iais:row cssClass="address">
    <iais:field value="Street Name" mandatory="true" width="5"/>
    <iais:value width="7" cssClass="col-md-7">
      <iais:input maxLength="32" type="text" name="streetName" id="streetName" value="${dto.streetName}"/>
    </iais:value>
  </iais:row>

  <iais:row cssClass="address">
    <iais:field value="Building Name" width="5"/>
    <iais:value width="7" cssClass="col-md-7">
      <iais:input maxLength="66" type="text" name="buildingName" id="buildingName" value="${dto.buildingName}"/>
    </iais:value>
  </iais:row>
  <%-- Address end --%>

  <iais:row>
    <iais:field value="Mobile No." mandatory="true" width="5"/>
    <iais:value width="7" cssClass="col-md-7">
      <iais:input type="text" name="telephoneNo" maxLength="8" value="${dto.telephoneNo}"/>
    </iais:value>
  </iais:row>

  <iais:row>
    <iais:field value="Email Address" mandatory="true" width="5"/>
    <iais:value width="7" cssClass="col-md-7">
      <iais:input type="text" name="emailAddr" maxLength="320" value="${dto.emailAddr}"/>
    </iais:value>
  </iais:row>
</div>
<script type="text/javascript">
  $(document).ready(function() {
      checkAddressManatory();

      $('#addrType').on('change', checkAddressManatory);
      $('.retrieveAddr').click(function() {
          var $postalCodeEle = $(this).closest('div.postalCodeDiv');
          var postalCode = $postalCodeEle.find('.postalCode').val();
          retrieveAddr(postalCode, $(this).closest('div.licenseeContent').find('div.address'));
      });
      //$('.retrieveAddr').trigger('click');

    toggleIdType('#idType', '.nationalityDiv');
    $('#idType').on('change', function () {
      toggleIdType(this, '.nationalityDiv');
    });
  });

  function checkAddressManatory() {
    var addrType = $('#addrType').val();
    $('.blkNoLabel .mandatory').remove();
    $('.floorUnitLabel .mandatory').remove();
    if ('ADDTY001' == addrType) {
        $('.blkNoLabel').append('<span class="mandatory">*</span>');
        $('.floorUnitLabel').append('<span class="mandatory">*</span>');
    }
  }

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

  function toggleIdType(sel, elem) {
    if (isEmpty(sel) || isEmpty(elem)) {
      return;
    }
    var $sel = $(sel);
    var $elem = $(elem);
    if ($sel.length == 0 || $elem.length == 0) {
      return;
    }
    if ($sel.val() == 'IDTYPE003') {
      $elem.show();
    } else {
      $elem.hide();
      clearFields($elem);
    }
  }
</script>