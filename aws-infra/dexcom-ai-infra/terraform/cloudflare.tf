# Configure Cloudflare provider
provider "cloudflare" {
  api_key = var.cloudflare_api_key
  email    = "amit.parnerkar@gmail.com"
}

# Data source to retrieve Cloudflare zone ID
data "cloudflare_zone" "main_zone" {
    name = "spinachsoftware.com"
}

# Create a new A record for your ELB
resource "cloudflare_record" "elb_record" {
  zone_id  = data.cloudflare_zone.main_zone.id
  name     = "dexcom-ai-aws.spinachsoftware.com"  # or "www" if you want a subdomain
  type     = "CNAME"
  content    = aws_elb.app.dns_name
  proxied  = true  # Enable Cloudflare proxy if needed
}
