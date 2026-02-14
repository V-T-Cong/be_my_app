package com.congvo.be_myapp.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendPasswordResetEmail(String to, String token) {
        // 1. Define the frontend URL (Link to your Reset Page)
        String resetUrl = "http://localhost:3000/reset-password?token=" + token;

        // 2. Create HTML Content with a Button
        // We use inline CSS because email clients often strip external stylesheets.
        String htmlContent = """
                 <html>
                 <body style="font-family: Arial, sans-serif;">
                     <div style="background-color: #f4f4f4; padding: 20px;">
                         <div style="max-width: 600px; margin: 0 auto; background-color: #ffffff; padding: 20px; border-radius: 8px; box-shadow: 0 2px 4px rgba(0,0,0,0.1);">
                             <h2 style="color: #333333;">Reset Your Password</h2>
                             <p style="color: #666666;">We received a request to reset the password for your account.</p>
                             <p style="color: #666666;">Please click the button below to create a new password:</p>
                            \s
                             <div style="text-align: center; margin: 30px 0;">
                                 <a href="%s" style="display: inline-block; padding: 12px 24px; font-size: 16px; color: #ffffff; background-color: #007bff; text-decoration: none; border-radius: 5px; font-weight: bold;">
                                     Reset Password
                                 </a>
                             </div>
                            \s
                             <p style="color: #999999; font-size: 12px;">If you did not request a password reset, please ignore this email. This link will expire in 15 minutes.</p>
                         </div>
                     </div>
                 </body>
                 </html>
                \s""".formatted(resetUrl);

        try {
            // 3. Create a MimeMessage (Required for HTML)
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(to);
            helper.setSubject("Reset Your Password");
            helper.setFrom("noreply@bemyapp.com"); // Ensure this does NOT have underscores

            // The 'true' flag here indicates that the text is HTML
            helper.setText(htmlContent, true);

            mailSender.send(message);
            System.out.println("HTML Email sent successfully with button to: " + to);

        } catch (MessagingException e) {
            System.err.println("Failed to send email: " + e.getMessage());
            throw new RuntimeException("Error sending email");
        }
    }
}