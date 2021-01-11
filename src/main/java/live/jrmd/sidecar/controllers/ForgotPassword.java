package live.jrmd.sidecar.controllers;

import live.jrmd.sidecar.repositories.UserRepository;
import live.jrmd.sidecar.services.Utility;
import live.jrmd.sidecar.models.User;
import live.jrmd.sidecar.services.EmailService;
import live.jrmd.sidecar.services.UserNotFoundException;
import live.jrmd.sidecar.services.UserService;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;

@Controller
public class ForgotPassword {
    @Autowired
    private final JavaMailSender mailSender;
    private UserRepository userDao;
    private final EmailService emailSender;

    @Autowired
    private final UserService userService;

    @Value("${spring.mail.from}")
    private String from;

    public ForgotPassword (JavaMailSender mailSender, EmailService emailSender, UserService userService){
        this.mailSender = mailSender;
        this.emailSender = emailSender;
        this.userService = userService;
    }

    @GetMapping("/forgot_password")
    String showForgotPasswordForm(){
        return "forgot_password_form";
    }

    @PostMapping("/forgot_password")
//    public <CustomerNotFoundException> String processForgotPassword(HttpServletRequest request, Model model) {
//        String email = request.getParameter("email");
//        String token = RandomString.make(30);
//
//        try {
//            userService.updateResetPasswordToken(token, email);
//            String resetPasswordLink = Utility.getSiteURL(request) + "/reset_password?token=" + token;
//            sendEmail(email, resetPasswordLink);
//            model.addAttribute("message", "We have sent a reset password link to your email. Please check.");
//
//        } catch (UserNotFoundException e) {
//            model.addAttribute("error", e.getMessage());
//        } catch (UnsupportedEncodingException | MessagingException e) {
//            model.addAttribute("error", "Error while sending email");
//        }
//
//        return "forgot_password_form";
//    }
//    All in one... Causes NullPointerException
    public String processForgotPassword(HttpServletRequest request, Model model) {
        String email = request.getParameter("email");
        String token = RandomString.make(30);

        try{
            userService.updateResetPasswordToken(token, email);
            String resetPasswordLink = Utility.getSiteURL(request) + "/reset_password?token=" + token;
            String subject = "Here is the link to reset your password";
            String body = "<p>Hello,</p>"
                    + "<p>You have requested to reset your password.</p>"
                    + "<p>Click the link below to change your password:</p>"
                    + "<p><a href=\"" + resetPasswordLink + "\">Change my password</a></p>"
                    + "<br>"
                    + "<p>Ignore this email if you do remember your password, "
                    + "or you have not made the request.</p>";
            emailSender.prepareAndSend(email, subject, body);

            model.addAttribute("message", "A password reset link has been sent to your email.");

        } catch (UserNotFoundException ex) {
            System.out.println("error:user not found");
            model.addAttribute("error", ex.getMessage());
        }
        System.out.println("should have worked");
        return "forgot_password_form";
    }

// Causes NullPointerException
//    public void sendEmail(String recipientEmail, String link)
//        throws MessagingException, UnsupportedEncodingException {
//            MimeMessage message = mailSender.createMimeMessage();
//            MimeMessageHelper helper = new MimeMessageHelper (message);
//
//            helper.setFrom("sidecar.live1@gmail.com", "Sidecar Live");
//            helper.setTo(recipientEmail);
//
//            String subject = "Here is the link to reset your password";
//            String content = "<p>Hello,</p>"
//                    + "<p>You have requested to reset your password.</p>"
//                    + "<p>Click the link below to change your password:</p>"
//                    + "<p><a href=\"" + link + "\">Change my password</a></p>"
//                    + "<br>"
//                    + "<p>Ignore this email if you do remember your password, "
//                    + "or you have not made the request.</p>";
//
//            helper.setSubject(subject);
//            helper.setText(content, true);
//
//            mailSender.send(message);
//        }

// Doesn't throw an exception/error but also doesn't send an email
//    public void sendEmail(String recipientEmail, String link)
//            throws MessagingException, UnsupportedEncodingException {
//        mailSender.send(message -> {
//            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
//            helper.setFrom(from);
//            helper.setTo(recipientEmail);
//            helper.setSubject("Here's the link to reset your password");
//
//            String content = "<p>Hello,</p>"
//                    + "<p>You have requested to reset your password.</p>"
//                    + "<p>Click the link below to change your password:</p>"
//                    + "<p><a href=\"" + link + "\">Change my password</a></p>"
//                    + "<br>"
//                    + "<p>Ignore this email if you remember your password, "
//                    + "or you have not made the request.</p>";
//
//            helper.setText(content, true);
//        });
//    }


    @GetMapping("/reset_password")
    public String showResetPasswordForm(@RequestParam(value = "token") String token, Model model) {
        User user = userService.getByResetPasswordToken(token);
        model.addAttribute("token", token);

        if(user == null) {
            model.addAttribute("message", "Invalid Token");
            return "message";
        }

        return "reset_password_form";
    }

    @PostMapping("/reset_password")
    public String processResetPassword(HttpServletRequest request, Model model) {
        String token = request.getParameter("token");
        String password = request.getParameter("password");

        User user = userService.getByResetPasswordToken(token);
        model.addAttribute("title", "Reset your password");

        if(user == null) {
            model.addAttribute("message", "Invalid Token");
            return "message";
        } else {
            userService.updatePassword(user, password);
            return "redirect:/login";
        }
    }
}
