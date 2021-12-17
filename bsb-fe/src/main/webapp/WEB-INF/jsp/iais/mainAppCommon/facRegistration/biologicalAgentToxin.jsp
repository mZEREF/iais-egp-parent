<div id="sectionGroup">
    <c:forEach var="info" items="${batInfo.batInfos}" varStatus="status">
        <section id="batInfoSection--v--${status.index}">
            <div class="form-group ">
                <div class="col-sm-5 control-label">
                    <label for="schedule--v--${status.index}">Schedule</label>
                    <span class="mandatory otherQualificationSpan">*</span>
                </div>
                <div class="col-sm-6" style="z-index: 30;">
                    <select name="schedule--v--${status.index}" id="schedule--v--${status.index}">
                        <c:forEach items="${ScheduleOps}" var="schedule">
                            <option value="${schedule.value}" <c:if test="${info.schedule eq schedule.value}">selected="selected"</c:if>>${schedule.text}</option>
                        </c:forEach>
                    </select>
                    <span data-err-ind="schedule--v--${status.index}" class="error-msg"></span>
                </div>
                <c:if test="${status.index gt 0}">
                    <div class="col-sm-1"><h4 class="text-danger"><em data-current-idx="${status.index}" class="fa fa-times-circle del-size-36 cursorPointer removeBtn"></em></h4></div>
                </c:if>
            </div>
            <div class="form-group ">
                <div class="col-sm-5 control-label">
                    <label for="batName--v--${status.index}">Name of Biological Agent/Toxin</label>
                    <span class="mandatory otherQualificationSpan">*</span>
                </div>
                <div class="col-sm-6" style="z-index: 20;">
                    <select name="batName--v--${status.index}" id="batName--v--${status.index}">
                        <c:forEach items="${batNameOps}" var="name">
                            <option value="${name.value}" <c:if test="${info.batName eq name.value}">selected="selected"</c:if>>${name.text}</option>
                        </c:forEach>
                    </select>
                    <span data-err-ind="batName--v--${status.index}" class="error-msg"></span>
                </div>
            </div>
            <div class="form-group ">
                <div class="col-sm-5 control-label">
                    <label>Types of samples that will be handled</label>
                    <span class="mandatory otherQualificationSpan">*</span>
                </div>
                <div class="col-sm-6" style="z-index: 10;">
                    <div class="self-assessment-checkbox-gp">
                        <div class="form-check">
                            <input type="checkbox" class="form-check-input" name="sampleType--v--${status.index}" id="sampleCultureIsolate--v--${status.index}" <c:if test="${info.sampleType.contains('BNOTS001')}">checked="checked"</c:if> value="BNOTS001"/>
                            <label for="sampleCultureIsolate--v--${status.index}" class="form-check-label"><span class="check-square"></span>Culture/isolate of biological agent(s)</label>
                        </div>
                        <div class="form-check">
                            <input type="checkbox" class="form-check-input" name="sampleType--v--${status.index}" id="samplePureToxin--v--${status.index}" <c:if test="${info.sampleType.contains('BNOTS002')}">checked="checked"</c:if> value="BNOTS002"/>
                            <label for="samplePureToxin--v--${status.index}" class="form-check-label"><span class="check-square"></span>Pure toxin(s)</label>
                        </div>
                        <div class="form-check">
                            <input type="checkbox" class="form-check-input" name="sampleType--v--${status.index}" id="sampleClinical--v--${status.index}" <c:if test="${info.sampleType.contains('BNOTS003')}">checked="checked"</c:if> value="BNOTS003"/>
                            <label for="sampleClinical--v--${status.index}" class="form-check-label"><span class="check-square"></span>Clinical samples e.g. blood, serum, respiratory swab, containing biological agent(s) or toxin(s)</label>
                        </div>
                        <div class="form-check">
                            <input type="checkbox" class="form-check-input" name="sampleType--v--${status.index}" id="sampleAnimal--v--${status.index}" <c:if test="${info.sampleType.contains('BNOTS004')}">checked="checked"</c:if> value="BNOTS004"/>
                            <label for="sampleAnimal--v--${status.index}" class="form-check-label"><span class="check-square"></span>Animal samples containing biological agent(s) or toxin(s)</label>
                        </div>
                        <div class="form-check">
                            <input type="checkbox" class="form-check-input" name="sampleType--v--${status.index}" id="sampleEnv--v--${status.index}" <c:if test="${info.sampleType.contains('BNOTS005')}">checked="checked"</c:if> value="BNOTS005"/>
                            <label for="sampleEnv--v--${status.index}" class="form-check-label"><span class="check-square"></span>Environmental samples containing biological agent(s) or toxin(s)</label>
                        </div>
                        <div class="form-check">
                            <input type="checkbox" class="form-check-input" name="sampleType--v--${status.index}" id="sampleFood--v--${status.index}" <c:if test="${info.sampleType.contains('BNOTS006')}">checked="checked"</c:if> value="BNOTS006"/>
                            <label for="sampleFood--v--${status.index}" class="form-check-label"><span class="check-square"></span>Food samples containing biological agent(s) or toxin(s)</label>
                        </div>
                        <div class="form-check">
                            <input type="checkbox" class="form-check-input" name="sampleType--v--${status.index}" id="sampleOthers--v--${status.index}" data-custom-ind="batOthersSampleType" <c:if test="${info.sampleType.contains('BNOTS007')}">checked="checked"</c:if> value="BNOTS007"/>
                            <label for="sampleOthers--v--${status.index}" class="form-check-label"><span class="check-square"></span>Others. Please specify under details.</label>
                        </div>
                        <div class="form-check" id="batOtherSampleTypeDiv--v--${status.index}" <c:if test="${not info.sampleType.contains('BNOTS007')}">style="display: none;"</c:if>>
                            <input type="text" autocomplete="off" name="otherSampleType--v--${status.index}" id="otherSampleType--v--${status.index}" value='<c:out value="${info.otherSampleType}"/>'/>
                        </div>
                    </div>
                    <span data-err-ind="sampleType--v--${status.index}" class="error-msg"></span>
                </div>
            </div>
        </section>
    </c:forEach>
</div>
<div class="form-group">
    <div class="col-12">
        <a id="addNewBatSection" style="text-decoration: none" href="javascript:void(0)">+ Add New Biological Agent/Toxins</a>
    </div>
</div>