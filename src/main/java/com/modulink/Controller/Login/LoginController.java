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

/**
 * Controller dedicato alla gestione delle procedure di autenticazione e recupero credenziali.
 * <p>
 * Oltre a servire la pagina di login, gestisce l'intero flusso di "Password Dimenticata" (Forgot Password),
 * che include:
 * <ol>
 *     <li>Richiesta di reset tramite email.</li>
 *     <li>Generazione e invio di codici OTP (One-Time Password) via email.</li>
 *     <li>Verifica dell'OTP e aggiornamento sicuro della password.</li>
 * </ol>
 * Utilizza {@link OTPManager} per la gestione temporanea dei codici e {@link EmailService} per le comunicazioni.
 *
 *
 * @author Modulink Team
 * @version 2.0.0
 * @since 1.0.0
 */
@Controller
public class LoginController {
    private final CustomUserDetailsService customUserDetailsService;
    private final OTPManager otpManager;
    private final String senderEmail;
    private final EmailService emailService;

    /**
     * Costruttore con iniezione delle dipendenze.
     *
     * @param customUserDetailsService Servizio gestione utenti.
     * @param senderEmail              Indirizzo email mittente configurato nelle properties.
     * @param emailService             Servizio per l'invio di email.
     * @since 1.0.0
     */
    public LoginController(CustomUserDetailsService customUserDetailsService, @Value("${spring.mail.properties.mail.smtp.from}") String senderEmail, EmailService emailService) {
        this.customUserDetailsService=customUserDetailsService;
        this.otpManager=new OTPManager();
        this.senderEmail=senderEmail;
        this.emailService=emailService;
    }

    /**
     * Visualizza la pagina di Login.
     * Se l'utente è già autenticato, esegue un redirect alla dashboard.
     *
     * @param model     Modello UI.
     * @param principal Utente autenticato.
     * @return Vista "login/login" o redirect.
     * @since 1.0.0
     */
    @GetMapping("/login")
    public String login(Model model, Principal principal) {
        if(principal != null) return "redirect:/dashboard";
        else return "login/login";
    }

    /**
     * Visualizza la pagina per la richiesta di recupero password.
     *
     * @param model     Modello UI.
     * @param principal Utente autenticato.
     * @return Vista "login/forgot-password" o redirect.
     * @since 1.0.0
     */
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

    /**
     * Metodo helper privato per generare e inviare un OTP via email.
     *
     * @param email        Indirizzo destinatario.
     * @param utenteToFind Entità utente associata.
     */
    private void sendEmailWithOTP(String email, UtenteEntity utenteToFind) {
        otpManager.addOTP(email,utenteToFind);
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(senderEmail);
        message.setTo(email);
        message.setSubject("Richiesta Password Dimenticata");
        message.setText("Salve "+utenteToFind.getNome()+" "+utenteToFind.getCognome()+", ecco il codice OTP per il recupero della password: "+otpManager.getOTPEmail(email));
        emailService.sendEmail(message);
    }

    /**
     * Elabora la richiesta di recupero password inviata dal form.
     * <p>
     * Verifica l'esistenza dell'email nel sistema. Se trovata, invia l'OTP e reindirizza alla pagina di verifica.
     * </p>
     *
     * @param model     Modello UI.
     * @param principal Utente autenticato.
     * @param email     Email fornita dall'utente.
     * @return Vista "login/otp-check" in caso di successo, o ricarica la pagina con errore.
     * @since 1.1.0
     */
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

    /**
     * Endpoint AJAX per il rinvio del codice OTP.
     *
     * @param model               Modello UI.
     * @param principal           Utente autenticato.
     * @param confirmPasswordForm DTO contenente l'email.
     * @return ResponseEntity OK o BadRequest.
     * @since 1.1.0
     */
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

    /**
     * Processa la conferma del cambio password tramite OTP.
     * <p>
     * Verifica la validità dell'OTP, la corrispondenza delle password e che la nuova password sia diversa dalla precedente.
     * In caso di successo, aggiorna la password criptata nel database.
     * </p>
     *
     * @param model               Modello UI.
     * @param principal           Utente autenticato.
     * @param confirmPasswordForm DTO con i dati di conferma.
     * @param bindingResult       Risultati validazione.
     * @return Vista di conferma successo o pagina di errore OTP.
     * @since 1.1.0
     */
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
