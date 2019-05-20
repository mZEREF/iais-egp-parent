<select name="${name}">
{for alertType in alertTypes}
	<option value="${alertType.key}"
		{if alertType.key == selected}selected{/if}
		>${alertType.label}</option>	
{/for}
</select>