<input type="text" name="property::${id}::${name}" class="property-input-readonly"
	{if defined('value') && value} value="${value|escape}" {/if}
	{if defined('size') && size} size="${size}" {/if}
	{if defined('maxLength')&& maxLength} maxlength="${maxLength|escape}" {/if}
	readonly="readonly" />
