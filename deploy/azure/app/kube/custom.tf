# Example of further extensions to Stacks Core templates
# Potential user defined extensions
data "azurerm_application_insights" "example" {
  name                = var.core_resource_group
  resource_group_name = var.app_insights_name
}

output "app_insights_instrumentation_key" {
  description = "App Insights key for downstream deployment use"
  value       = data.azurerm_application_insights.example.instrumentation_key
  sensitive   = true
}

variable "app_insights_name" {
  type        = string
  description = "app insights name for key retrieval in memory"
}
