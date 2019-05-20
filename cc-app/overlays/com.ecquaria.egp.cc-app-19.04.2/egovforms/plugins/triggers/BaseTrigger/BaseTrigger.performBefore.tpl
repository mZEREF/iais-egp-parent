{var units = ['day', 'week', 'month']}
{var isTypeProcessSLA = triggerEvent.indexOf('process-sla') == 0}
<div id="${id}">
	<input type="text" size="3" value="${value}"
		{if isTypeProcessSLA}disabled="disabled"{/if} />
	<select
		{if isTypeProcessSLA}disabled="disabled"{/if}>
		{for unit in units}
			<option value="${unit}"
				{if selected == unit}selected{/if}>${unit}</option>
		{/for}
	</select>
</div>