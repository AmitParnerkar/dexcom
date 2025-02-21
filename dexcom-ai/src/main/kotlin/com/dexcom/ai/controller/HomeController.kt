package com.dexcom.ai.controller

import com.dexcom.ai.model.ContactForm
import jakarta.mail.internet.MimeMessage
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.servlet.mvc.support.RedirectAttributes

@Controller
class HomeController(private val mailSender: JavaMailSender) {

    private val logger: Logger = LoggerFactory.getLogger(HomeController::class.java)


    @GetMapping("/")
    fun home(model: Model): String {
        model.addAttribute("pageTitle", "Welcome to Dexcom AI")
        return "index" // Refers to src/main/resources/templates/index.html
    }

    @PostMapping("/send-message")
    fun sendMessage(@ModelAttribute form: ContactForm, redirectAttributes: RedirectAttributes): String {
        logger.debug("Received message from: ${form.name} (${form.email}) - ${form.message}")
        try {
            sendEmail(form)
            redirectAttributes.addFlashAttribute("successMessage", "Message sent successfully!")
        } catch (ex: Exception) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to send message. Please try again later.")
            ex.printStackTrace()
        }

        return "redirect:/"
    }

    private fun sendEmail(form: ContactForm) {
        val message: MimeMessage = mailSender.createMimeMessage()
        val helper = MimeMessageHelper(message, true)

        helper.setFrom(form.email)
        helper.setTo("amit.parnerkar@gmail.com")
        helper.setSubject("Dexcom Feedback Submission from ${form.name}")
        helper.setText("""
            Name: ${form.name}
            Email: ${form.email}
            
            Message:
            ${form.message}
        """.trimIndent(), false)

        mailSender.send(message)
    }
}
