/* App servers */
resource "aws_instance" "app" {
  count             = 2
  ami               = var.amis[var.region]
  instance_type     = "t2.2xlarge"
  subnet_id         = aws_subnet.private.id
  security_groups   = [aws_security_group.common.id]
  key_name          = aws_key_pair.deployer.key_name
  source_dest_check = true
  root_block_device {
    volume_size = 50  # Increase the root volume size to 50 GB
    volume_type = "gp2"
  }
  user_data         = file("app-config/app.yaml")
  tags = {
    Name = "dexcom-app-${count.index}"
  }
}

/* Load balancer */
resource "aws_elb" "app" {
  name            = "dexcom-vpc-elb"
  subnets         = [aws_subnet.public.id]
  security_groups = [aws_security_group.common.id,
    aws_security_group.web.id]
  listener {
    instance_port     = 80
    instance_protocol = "http"
    lb_port           = 80
    lb_protocol       = "http"
  }

  health_check {
    healthy_threshold   = 2
    unhealthy_threshold = 2
    timeout             = 5
    target              = "HTTP:80/"
    interval            = 15
  }

  instances = aws_instance.app.*.id
}