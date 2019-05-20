<div class="flowItemTooltip" id="${tooltipId}_tooltip">
	<table>
		<tr>
			<td class="tooltipLabel">Name</td>
			<td class="tooltipText">${name}</td>
		</tr>

		{if defined('description') && description}
		<tr>
			<td class="tooltipLabel">Description</td>
			<td class="tooltipText">${description}</td>
		</tr>
		{/if}

		{if stageProperties.length > 0}
			{var x = 0}
			{for i in stageProperties}
				{if i.type != 'button'}
					{eval}x++{/eval}
				{/if}
			{/for}
		{/if}

		{if x > 0}
		<tr>
			<td class="tooltipLabel">Properties</td>
			<td class="tooltipText">
				{for i in stageProperties}
					{if i.type != 'button'}
						${i.label}<br/>
					{/if}
				{/for}
			</td>
		</tr>
		{/if}

		{if stageResult.length > 0}
		<tr>
			<td class="tooltipLabel">Return values</td>
			<td class="tooltipText">
				{for i in stageResult}
					${i.result}<br/>
				{/for}
			</td>
		</tr>
		{/if}
	</table>
</div>
