terraform {
  backend "s3" {
    bucket         = "dexcom-tfstate"
    key            = "dexcom-app-state/terraform.tfstate"  # This acts like a folder in S3
    region         = var.region
  }
}