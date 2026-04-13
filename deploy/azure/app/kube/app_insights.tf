# Example of further extensions to Stacks Core templates
# Potential user defined extensions
data "azurerm_application_insights" "example" {
  name                = var.app_insights_name
  resource_group_name = var.app_insights_resource_group_name != "" ? var.app_insights_resource_group_name : var.infra_resource_group
}
