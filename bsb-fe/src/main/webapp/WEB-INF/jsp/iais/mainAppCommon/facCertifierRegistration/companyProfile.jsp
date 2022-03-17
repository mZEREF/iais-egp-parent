<h3 class="col-12 pl-0" style="border-bottom: 1px solid black">Company Profile</h3>
<div class="form-group ">
    <div class="col-sm-5 control-label">
        <label for="companyName">Company Name</label>
        <span class="mandatory otherQualificationSpan">*</span>
    </div>
    <div class="col-sm-6 col-md-7">
        <label id="companyName">SGH Laboratory</label>
    </div>
</div>

<div class="form-group ">
    <div class="col-sm-5 control-label">
        <label for="yearEstablished">Year of Establishment</label>
        <span class="mandatory otherQualificationSpan">*</span>
    </div>
    <div class="col-sm-6 col-md-7">
        <input type="number" autocomplete="off" name="yearEstablished" id="yearEstablished" oninput="if(value.length>4) value=value.slice(0,4)" value='${companyProfile.yearEstablished}'/>
        <span data-err-ind="yearEstablished" class="error-msg"></span>
    </div>
</div>

<div class="form-group ">
    <div class="col-sm-5 control-label">
        <label for="uen">UEN</label>
        <span class="mandatory otherQualificationSpan">*</span>
    </div>
    <div class="col-sm-6 col-md-7">
        <label id="uen">185412420D</label>
    </div>
</div>

<div class="form-group ">
    <div class="col-sm-5 control-label">
        <label>Is the mailing address the same as the company address?</label>
        <span class="mandatory otherQualificationSpan">*</span>
    </div>
    <div class="col-sm-6 col-md-7">
        <div class="col-sm-4" style="margin-top: 8px">
            <input type="radio" name="sameAddress" id="sameAddressY" value="Y" <c:if test="${companyProfile.sameAddress eq 'Y'}">checked</c:if> />
            <label for="sameAddressY">Yes</label>
        </div>
        <div class="col-sm-4" style="margin-top: 8px">
            <input type="radio" name="sameAddress" id="sameAddressN" value="N" <c:if test="${companyProfile.sameAddress eq 'N'}">checked</c:if> />
            <label for="sameAddressN">No</label>
        </div>
    </div>
</div>

<div class="form-group ">
    <div class="col-sm-5 control-label">
        <label>Is the company registered in Singapore or overseas?</label>
        <span class="mandatory otherQualificationSpan">*</span>
    </div>
    <div class="col-sm-6 col-md-7">
        <div class="col-sm-4" style="margin-top: 8px">
            <input type="radio" name="registered" id="registeredLocal" value="local" <c:if test="${companyProfile.registered eq 'local'}">checked</c:if> />
            <label for="registeredLocal">Local</label>
        </div>
        <div class="col-sm-4" style="margin-top: 8px">
            <input type="radio" name="registered" id="registeredOverseas" value="overseas" <c:if test="${companyProfile.registered eq 'overseas'}">checked</c:if> />
            <label for="registeredOverseas">Overseas</label>
        </div>
    </div>
</div>

<div id="overseasCon" <c:if test="${companyProfile.addressType ne 'overseas'}"> style="display:none" </c:if>>

    <div class="form-group ">
        <div class="col-sm-5 control-label">
            <label for="country">Country</label>
            <span class="mandatory otherQualificationSpan">*</span>
        </div>
        <div class="col-sm-6 col-md-7">
            <select name="country" id="country" >
                <c:forEach items="${countryOps}" var="co">
                    <option value="${co.value}" <c:if test="${co.value eq companyProfile.country}">selected="selected"</c:if>>${co.text}</option>
                </c:forEach>
            </select>
            <span data-err-ind="country" class="error-msg"></span>
        </div>
    </div>

    <div class="form-group ">
        <div class="col-sm-5 control-label">
            <label for="city">City</label>
            <span class="mandatory otherQualificationSpan">*</span>
        </div>
        <div class="col-sm-6 col-md-7">
            <input type="text" autocomplete="off" name="city" id="city" value='${companyProfile.city}' maxlength="30"/>
            <span data-err-ind="city" class="error-msg"></span>
        </div>
    </div>

    <div class="form-group ">
        <div class="col-sm-5 control-label">
            <label for="state">State</label>
            <span class="mandatory otherQualificationSpan">*</span>
        </div>
        <div class="col-sm-6 col-md-7">
            <input type="text" autocomplete="off" name="state" id="state" maxlength="66" value='${companyProfile.state}'/>
            <span data-err-ind="state" class="error-msg"></span>
        </div>
    </div>

</div>

<div class="form-group ">
    <div class="col-sm-5 control-label">
        <label for="postalCode">Postal Code </label>
        <span class="mandatory otherQualificationSpan">*</span>
    </div>
    <div class="col-sm-6 col-md-7">
        <div class="col-sm-8">
            <input type="number" autocomplete="off" name="postalCode" id="postalCode" maxlength="15" value='${companyProfile.postalCode}' oninput="value=value.replace(/[^\d]/g,'')"/>
            <span data-err-ind="postalCode" class="error-msg"></span>
        </div>
        <div class="col-sm-4">
            <a href="#">Retrieve your address</a>
        </div>
    </div>
</div>

<div class="form-group ">
    <div class="col-sm-5 control-label">
        <label for="addressType">Address Type</label>
        <span class="mandatory otherQualificationSpan">*</span>
    </div>
    <div class="col-sm-6 col-md-7">
        <select name="addressType" id="addressType" >
            <option value="">Please Select</option>
            <option value="Apt Blk">Apt Blk</option>
            <option value="Without Apt">Without Apt</option>
        </select>
        <span data-err-ind="addressType" class="error-msg"></span>
    </div>
</div>

<div class="form-group ">
    <div class="col-sm-5 control-label">
        <label for="block">Block / House No.</label>
        <span class="mandatory otherQualificationSpan">*</span>
    </div>
    <div class="col-sm-6 col-md-7">
        <input type="text" autocomplete="off" name="block" id="block" value='${companyProfile.block}' maxlength="10"/>
        <span data-err-ind="block" class="error-msg"></span>
    </div>
</div>

<div class="form-group ">
    <div class="col-sm-5 control-label">
        <label for="floor">Floor and Unit No.</label>
        <span class="mandatory otherQualificationSpan">*</span>
    </div>
    <div class="col-sm-2">
        <input type="text" autocomplete="off" name="floor" id="floor" value='${companyProfile.floor}' maxlength="4"/>
        <span data-err-ind="floor" class="error-msg"></span>
    </div>
    <div class="hidden-xs col-sm-1" style="text-align: center">
        <p>-</p>
    </div>
    <div class="col-sm-3 col-md-4">
        <input type="text" autocomplete="off" name="unitNo" id="unitNo" value='${companyProfile.unitNo}' maxlength="4"/>
        <span data-err-ind="unitNo" class="error-msg"></span>
    </div>
</div>

<div class="form-group ">
    <div class="col-sm-5 control-label">
        <label for="streetName">Street Name </label>
        <span class="mandatory otherQualificationSpan">*</span>
    </div>
    <div class="col-sm-6 col-md-7">
        <input type="text" autocomplete="off" name="streetName" id="streetName" value='${companyProfile.streetName}' maxlength="32"/>
        <span data-err-ind="streetName" class="error-msg"></span>
    </div>
</div>

<div class="form-group ">
    <div class="col-sm-5 control-label">
        <label for="building">Building Name </label>
        <span class="mandatory otherQualificationSpan">*</span>
    </div>
    <div class="col-sm-6 col-md-7">
        <input type="text" autocomplete="off" name="building" id="building" value='${companyProfile.building}' maxlength="10"/>
        <span data-err-ind="building" class="error-msg"></span>
    </div>
</div>