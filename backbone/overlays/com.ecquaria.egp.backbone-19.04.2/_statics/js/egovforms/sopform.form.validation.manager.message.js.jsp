<%@page contentType="text/javascript" %>
<%@ taglib uri="ecquaria/sop/sop-core" prefix="sop-core"%>
<%@ taglib uri="ecquaria/sop/egov-smc" prefix="egov-smc"%>
<egov-smc:messageTemplate var="requiredErr" key="egov.sdsform.required.value" 
     default="[{0}] is required."></egov-smc:messageTemplate>

<egov-smc:messageTemplate var="alphabetsSpacesLengthErr" key="egov.sdsform.value.alphabets.spaces.length" 
     default="[{0}] only accepts alphabets(case insensitive) and spaces of length min [{1}] and max [{2}]."></egov-smc:messageTemplate>

<egov-smc:messageTemplate var="alphaNumbericUnderscoreLengthErr" key="egov.sdsform.value.alpha.numberic.underscore.length" 
     default="[{0}] only accepts alpha numeric and underscore of length min [{1}] and max [{2}]."></egov-smc:messageTemplate>

<egov-smc:messageTemplate var="dateParseErr" key="egov.sdsform.value.date.parse.wrong.format" 
     default="[{0}] date fails to parse with format [{1}]"></egov-smc:messageTemplate>

<egov-smc:messageTemplate var="startDateParseErr" key="egov.sdsform.value.start.date.parse.wrong.format" 
     default="Start date fails to parse with format [{0}]"></egov-smc:messageTemplate>
     
<egov-smc:messageTemplate var="endDateParseErr" key="egov.sdsform.value.end.date.parse.wrong.format" 
     default="End date fails to parse with format [{0}]"></egov-smc:messageTemplate>
     
<egov-smc:messageTemplate var="shouldLessErr" key="egov.sdsform.value.should.be.less" 
     default="[{0}] should be &lt; [{1}]"></egov-smc:messageTemplate>
     
<egov-smc:messageTemplate var="shouldLessOrEqualErr" key="egov.sdsform.value.should.be.less.or.equal" 
     default="[{0}] should be &lt;= [{1}]"></egov-smc:messageTemplate>
     
<egov-smc:messageTemplate var="shouldLargerErr" key="egov.sdsform.value.should.be.larger" 
     default="[{0}] should be &gt; [{1}]"></egov-smc:messageTemplate>
     
<egov-smc:messageTemplate var="shouldLargerOrEqualErr" key="egov.sdsform.value.should.be.larger.or.equal" 
     default="[{0}] should be &gt;= [{1}]"></egov-smc:messageTemplate>
     
<egov-smc:messageTemplate var="shouldBetErr" key="egov.sdsform.value.should.be.between" 
     default="[{0}] should be between [{1}] and [{2}]"></egov-smc:messageTemplate>
     
<egov-smc:messageTemplate var="shouldBetInclusiveErr" key="egov.sdsform.value.should.be.between.inclusive" 
     default="[{0}] should be between [{1}] and [{2}](inclusive)"></egov-smc:messageTemplate>
     
<egov-smc:messageTemplate var="onlyDecimalErr" key="egov.sdsform.value.only.decimal" 
     default="[{0}] only accepts decimal numbers."></egov-smc:messageTemplate>
     
<egov-smc:messageTemplate var="onlyDecimalBtwInclusiveErr" key="egov.sdsform.value.only.decimal.between.Inclusive" 
     default="[{0}] must be a decimal number between [{1}] and [{2}] (inclusive)"></egov-smc:messageTemplate>

<egov-smc:messageTemplate var="onlyNumerErr" key="egov.sdsform.value.only.number" 
     default="[{0}] only accepts numbers(without decimal)."></egov-smc:messageTemplate>
     
<egov-smc:messageTemplate var="onlyNumberBtwInclusiveErr" key="egov.sdsform.value.only.number.between.Inclusive" 
     default="[{0}] must be a number between [{1}] and [{2}] (inclusive)"></egov-smc:messageTemplate>
     
<egov-smc:messageTemplate var="countBtwInclusiveErr" key="egov.sdsform.value.count.between.Inclusive" 
     default="[{0}] value count must be between [{1}] and [{2}] (inclusive)"></egov-smc:messageTemplate>

;
if (!window.EGOV) window.EGOV = {};
if (!EGOV.SDSFormMSG) EGOV.SDSFormMSG = {};

(function(ns) {    
    ns.requiredErr = '<sop-core:escapeJavaScript value="${requiredErr}"/>';
    ns.alphabetsSpacesLengthErr = '<sop-core:escapeJavaScript value="${alphabetsSpacesLengthErr}"/>';
    ns.alphaNumbericUnderscoreLengthErr = '<sop-core:escapeJavaScript value="${alphaNumbericUnderscoreLengthErr}"/>';
    ns.dateParseErr = '<sop-core:escapeJavaScript value="${dateParseErr}"/>';
    ns.startDateParseErr = '<sop-core:escapeJavaScript value="${startDateParseErr}"/>';
    ns.endDateParseErr = '<sop-core:escapeJavaScript value="${endDateParseErr}"/>';
    ns.shouldLessErr = '<sop-core:escapeJavaScript value="${shouldLessErr}"/>';
    ns.shouldLessOrEqualErr = '<sop-core:escapeJavaScript value="${shouldLessOrEqualErr}"/>';
    ns.shouldLargerErr = '<sop-core:escapeJavaScript value="${shouldLargerErr}"/>';
    ns.shouldLargerOrEqualErr = '<sop-core:escapeJavaScript value="${shouldLargerOrEqualErr}"/>';
    ns.shouldBetErr = '<sop-core:escapeJavaScript value="${shouldBetErr}"/>';
    ns.shouldBetInclusiveErr = '<sop-core:escapeJavaScript value="${shouldBetInclusiveErr}"/>';
    ns.onlyDecimalErr = '<sop-core:escapeJavaScript value="${onlyDecimalErr}"/>';
    ns.onlyDecimalBtwInclusiveErr = '<sop-core:escapeJavaScript value="${onlyDecimalBtwInclusiveErr}"/>';
    ns.onlyNumerErr = '<sop-core:escapeJavaScript value="${onlyNumerErr}"/>';
    ns.onlyNumberBtwInclusiveErr = '<sop-core:escapeJavaScript value="${onlyNumberBtwInclusiveErr}"/>';
    ns.countBtwInclusiveErr = '<sop-core:escapeJavaScript value="${countBtwInclusiveErr}"/>';
    
    ns.format = function(template, args){
        if(!args){
            return template;
        }
        
        var length = args.length;
        var result = template;
        var regex = null;
        for(var i = 0; i < length; i++){
            regex = new RegExp('\\{'+i+'\\}', 'g');
            result = result.replace(regex, args[i]);
        }
        
        return result;
    };
    
})(EGOV.SDSFormMSG);