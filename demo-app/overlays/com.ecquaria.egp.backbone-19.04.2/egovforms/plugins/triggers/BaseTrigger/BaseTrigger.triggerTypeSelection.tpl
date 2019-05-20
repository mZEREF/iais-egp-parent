<select name="${name}">
{for triggerType in triggerTypes}
	<option value="${triggerType.key}"
		{if triggerType.key == selected}selected{/if}
		>${triggerType.label}</option>	
{/for}
</select>