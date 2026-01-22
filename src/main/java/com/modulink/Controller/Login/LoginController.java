package com.modulink.Controller.Login;

import com.modulink.Model.Email.EmailService;
import com.modulink.Model.OTP.OTPManager;
import com.modulink.Model.Utente.CustomUserDetailsService;
import com.modulink.Model.Utente.PasswordUtility;
import com.modulink.Model.Utente.UtenteEntity;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Optional;
import java.util.regex.Pattern;

@Controller
public class LoginController {
    private final CustomUserDetailsService customUserDetailsService;
    private final OTPManager otpManager;
    private final String senderEmail;
    private final EmailService emailService;

    public LoginController(CustomUserDetailsService customUserDetailsService, @Value("${spring.mail.properties.mail.smtp.from}") String senderEmail, EmailService emailService) {
        this.customUserDetailsService=customUserDetailsService;
        this.otpManager=new OTPManager();
        this.senderEmail=senderEmail;
        this.emailService=emailService;
    }

    @GetMapping("/login")
    public String login(Model model, Principal principal) {
        if(principal != null) return "redirect:/dashboard";
        else return "login/login";
    }

    @GetMapping("/forgot-password")
    public String forgotPasswordGetter(Model model, Principal principal) {
        if(principal != null) return "redirect:/dashboard";
        else return "login/forgot-password";
    }

    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";

    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    private static boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }

    private void sendEmailWithOTP(String email, UtenteEntity utenteToFind) {
        otpManager.addOTP(email,utenteToFind);
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(senderEmail);
        message.setTo(email);
        message.setSubject("Richiesta Password Dimenticata");
        message.setText("Salve "+utenteToFind.getNome()+" "+utenteToFind.getCognome()+", ecco il codice OTP per il recupero della password: "+otpManager.getOTPEmail(email));
        emailService.sendEmail(message);
    }

    @PostMapping("/forgot-password")
    public String forgotPasswordController(Model model, Principal principal, @RequestParam String email) {
        if(principal!=null) return "redirect:/dashboard";
        else {
            if(!isValidEmail(email)) {
                model.addAttribute("error",true);
                model.addAttribute("message","L'email inserita non è valida.");
                return "login/forgot-password";
            }
            Optional<UtenteEntity> utenteOpt=customUserDetailsService.findByEmail(email);
            if(utenteOpt.isEmpty()) {
                model.addAttribute("error",true);
                model.addAttribute("message","L'email non è stata trovata.");
                return "login/forgot-password";
            }
            sendEmailWithOTP(email,utenteOpt.get());
            model.addAttribute("email", email);
            ConfirmPasswordForm form = new ConfirmPasswordForm();
            form.setEmail(email);
            model.addAttribute("confirmPasswordForm", form);
            return "login/otp-check";
        }
    }

    @PostMapping("/resend-otp")
    @ResponseBody
    public ResponseEntity<Void> reSendOTP(Model model, Principal principal, @ModelAttribute ConfirmPasswordForm confirmPasswordForm) {
        if(principal!=null) return ResponseEntity.badRequest().build();
        else {
            Optional<UtenteEntity> utenteOpt=customUserDetailsService.findByEmail(confirmPasswordForm.getEmail());
            if(utenteOpt.isEmpty()) return ResponseEntity.badRequest().build();
            sendEmailWithOTP(confirmPasswordForm.getEmail(),utenteOpt.get());
            return ResponseEntity.ok().build();
        }
    }

    @PostMapping("/confirm-new-password")
    public String confirmNewPassword(Model model, Principal principal, @Valid @ModelAttribute ConfirmPasswordForm confirmPasswordForm, BindingResult bindingResult) {
        if(principal!=null) return "redirect:/dashboard";
        else {
            if(bindingResult.hasErrors()) {
                model.addAttribute("confirmPasswordForm",confirmPasswordForm);
                return "login/otp-check";
            }
            if(!confirmPasswordForm.getNewPassword().equals(confirmPasswordForm.getConfirmNewPassword())) {
                bindingResult.rejectValue("confirmNewPassword", "error.confirmNewPassword","Le password non coincidono");
                return "login/otp-check";
            }
            String code=otpManager.getOTPEmail(confirmPasswordForm.getEmail());
            if(!code.equals(confirmPasswordForm.getOtp())) {
                bindingResult.rejectValue("otp","error.otp","L'OTP inserito è errato, controlla e riprova.");
                return "login/otp-check";
            }
            Optional<UtenteEntity> utenteOpt=customUserDetailsService.findByEmail(confirmPasswordForm.getEmail());
            if(utenteOpt.isEmpty()) {
                bindingResult.rejectValue("otp","error.otp","C'è stato qualche problema co la richiesta, ricomincia dall'inizio e riprova.");
                return "login/otp-check";
            }
            UtenteEntity utente=utenteOpt.get();
            if(PasswordUtility.checkPassword(confirmPasswordForm.getNewPassword(),utente.getHash_password())) {
                bindingResult.rejectValue("newPassword","error.newPassword","La nuova password deve essere diversa dalla vecchia.");
                return "login/otp-check";
            }
            else {
                otpManager.removeOTPEmail(utente.getEmail());
                utente.setHash_password(PasswordUtility.hashPassword(confirmPasswordForm.getNewPassword()));
                customUserDetailsService.aggiornaUtente(utente);
                return "login/confirm-new-password";
            }
        }
    }
}
