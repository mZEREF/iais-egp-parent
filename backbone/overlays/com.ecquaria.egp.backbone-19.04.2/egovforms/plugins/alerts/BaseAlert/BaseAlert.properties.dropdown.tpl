<select name="${name}" class="sla-alert-select">
<!-- options : [{value:'value1',label:'label1'}, {value:'value2',label:'label2'},...] -->
{for opt in options}
	<option value="${opt.value}"
		{if opt.value == value}selected{/if}
		>${opt.label}</option>	
{/for}
</select>