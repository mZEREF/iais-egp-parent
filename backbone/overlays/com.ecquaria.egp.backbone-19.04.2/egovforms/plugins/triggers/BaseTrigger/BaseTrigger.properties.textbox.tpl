<input type="text" name="${name}" class="sla-trigger-input"
	{if defined('value') && value} value="${value|escape}" {/if}
	{if defined('size') && size} size="${size}" {/if}
	{if defined('maxLength')&& maxLength} maxlength="${maxLength|escape}" {/if}/>