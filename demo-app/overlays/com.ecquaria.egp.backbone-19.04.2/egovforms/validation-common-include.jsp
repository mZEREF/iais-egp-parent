<script type="text/javascript">
  function getCommonValidationJSSupport() {
    return {
      // TODO add common validation methods here.
      //example
		validateEmail : function() {
			var value = this.getFieldValue();
			var regex = /^(\w)+(\.\w+)*@(\w)+((\.\w+)+)$/;
			if($.trim(value) == "") {
				this.setErrorMessage("Please enter Email address.");
			}else if(!regex.test(value)){
				this.setErrorMessage("Please enter a valid email address.");
			}
		},
		
		validatePassword : function() {
			var value = this.getFieldValue();
			if(value == "") {
				this.setErrorMessage("Password can not be empty.");
			}else if(value.length < 6) {
				this.setErrorMessage("Please enter at least 6 characters.");
			}else if(value.indexOf(" ") != -1) {
				this.setErrorMessage("The password can't contain space.");
			}
		},
		
		validateNumber : function() {
			var value = this.getFieldValue();
			if(!(/(^\d*$)/.test(value))) {
				this.setErrorMessage('Only accepts numbers.');
			}
		}
    }
  };
</script>

<%-- 

you can include your custom jsp of common validation js here,
and override function "getCommonValidationJSSupport".

--%>