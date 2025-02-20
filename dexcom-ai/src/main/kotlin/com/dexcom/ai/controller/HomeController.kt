package com.dexcom.ai.controller

import com.dexcom.ai.model.ContactForm
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.servlet.mvc.support.RedirectAttributes

@Controller
class HomeController {

    @GetMapping("/")
    fun home(model: Model): String {
        model.addAttribute("pageTitle", "Welcome to Dexcom AI")
        model.addAttribute("successMessage", "Your message was sent successfully!")
        return "index" // Refers to src/main/resources/templates/index.html
    }

    @PostMapping("/send-message")
    fun sendMessage(@ModelAttribute form: ContactForm, redirectAttributes: RedirectAttributes): String {
        println("Received message from: ${form.name} (${form.email}) - ${form.message}")

        redirectAttributes.addFlashAttribute("successMessage", "Message sent successfully!")

        return "redirect:/"
    }
}
