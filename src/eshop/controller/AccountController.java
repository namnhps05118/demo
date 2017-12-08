package eshop.controller;

import java.io.File;

import javax.mail.internet.MimeMessage;
import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import eshop.entity.Customer;
import eshop.utils.XCaptcha;

@Controller
@RequestMapping("account")
@Transactional
public class AccountController extends EShopController{
	
	@Autowired
	ServletContext application;
	
	@Autowired
	HttpSession httpSession;
	
	@Autowired
	JavaMailSender mailSender;
	
	@RequestMapping(value="activate/{id}", method=RequestMethod.GET)
	public String activate(ModelMap model,
			@PathVariable("id") String id) {
		Session session = sessionFactory.getCurrentSession();
		Customer user = (Customer) session.load(Customer.class, id);
		user.setActivated(true);
		session.update(user);
		return "redirect:/account/login.htm";
	}
	
	@RequestMapping(value="edit", method=RequestMethod.GET)
	public String edit(ModelMap model) {
		Customer user = (Customer) httpSession.getAttribute("user");
		model.addAttribute("user", user);
		return "edit-profile";
	}
	
	@RequestMapping(value="edit", method=RequestMethod.POST)
	public String edit(ModelMap model,
			@ModelAttribute("user") Customer user,
			@RequestParam("file_photo") MultipartFile photo) {
			try {
				if(!photo.isEmpty()){
					String fileName = photo.getOriginalFilename();
					String path = application.getRealPath("images/customers/" + fileName);
					photo.transferTo(new File(path));
					user.setPhoto(fileName);
				}
				Session session = sessionFactory.getCurrentSession();
				session.update(user);
				
				httpSession.setAttribute("user", user);
				
				model.addAttribute("message", "Cập nhật thành công !");
			} 
			catch (Exception e) {
				model.addAttribute("message", "Cập nhật thất bại !");
			}
		
		return "edit-profile";
	}
	
	@RequestMapping(value="change", method=RequestMethod.GET)
	public String change(ModelMap model) {
		model.addAttribute("user", new Customer());
		return "change";
	}
	
	@RequestMapping(value="change", method=RequestMethod.POST)
	public String change(ModelMap model, 
			@ModelAttribute("user") Customer user,
			@RequestParam("password1") String password1, 
			@RequestParam("password2") String password2) {
		if(!password1.equals(password2)){
			model.addAttribute("message", "Xác nhận mật khẩu không đúng !");
			return "change";
		}
		
		Customer oldUser = (Customer) httpSession.getAttribute("user");
		if(oldUser.getId().equals(user.getId())){
			if(oldUser.getPassword().equals(user.getPassword())){
				oldUser.setPassword(password1);
				Session session = sessionFactory.getCurrentSession();
				session.update(oldUser);
				model.addAttribute("message", "Đổi mật khẩu thành công !");
			}
			else{
				model.addAttribute("message", "Sai mật khẩu!");
			}
		}
		else{
			model.addAttribute("message", "Sai tên đăng nhập !");
		}
		return "change";
	}
	
	@RequestMapping(value="change2")
	@ResponseBody
	public String changePwAjax(
			@RequestParam("id") String id,
			@RequestParam("pw") String password1, 
			@RequestParam("pw2") String password2) {
				
		Customer user = new Customer();
		user.setId(id);
		Session session = sessionFactory.getCurrentSession();
		try {
			session.refresh(user);
			
			if(password1.equals(user.getPassword())){
				user.setPassword(password2);
				session.update(user);
				return "true";
			}
			else{
				return "false";
			}
		} 
		catch (Exception e) {
			return "false";
		}
	}
	
	@RequestMapping(value="forgot", method=RequestMethod.GET)
	public String showForgotForm(ModelMap model) {
		model.addAttribute("user", new Customer());
		return "forgot";
	}
	
	@RequestMapping(value="forgot", method=RequestMethod.POST)
	public String submitForgotForm(ModelMap model, 
			@ModelAttribute Customer user) {
		String email = user.getEmail();
		
		Session session = sessionFactory.getCurrentSession();
		try {
			session.refresh(user);
			
			if(email.equals(user.getEmail())){
				// Gửi mật khẩu qua email
				try{
					MimeMessage message = mailSender.createMimeMessage();
					
					MimeMessageHelper helper = new MimeMessageHelper(message, true);
					helper.setFrom("Web Master <projectjava26012015@gmail.com>");
					helper.setTo(email);
					helper.setReplyTo("Web Master <projectjava26012015@gmail.com>");
					helper.setSubject("Mật khẩu của bạn");
					helper.setText("Mật khẩu: " + user.getPassword(), true);
					
					mailSender.send(message);
					model.addAttribute("message", "Mật khẩu đã được gửi đến email của bạn !");
				}
				catch(Exception ex){
					model.addAttribute("message", ex.getMessage());
				}
			}
			else{
				model.addAttribute("message", "Sai email !");
			}
		} 
		catch (Exception e) {
			model.addAttribute("message", "Sai tên đăng nhập !");
		}
		
		
		model.addAttribute("user", new Customer());
		return "forgot";
	}
	
	@RequestMapping(value="forgot2")
	@ResponseBody
	public String forgotAjax(
			@RequestParam String id,
			@RequestParam String email) {
		
		Customer user = new Customer();
		user.setId(id);
		
		Session session = sessionFactory.getCurrentSession();
		try {
			session.refresh(user);
			
			if(email.equals(user.getEmail())){
				// Gửi mật khẩu qua email
				try{
					MimeMessage message = mailSender.createMimeMessage();
					
					MimeMessageHelper helper = new MimeMessageHelper(message, true);
					helper.setFrom("Web Master <projectjava26012015@gmail.com>");
					helper.setTo(email);
					helper.setReplyTo("Web Master <projectjava26012015@gmail.com>");
					helper.setSubject("Mật khẩu của bạn");
					helper.setText("Mật khẩu: " + user.getPassword(), true);
					
					mailSender.send(message);
					return "true";
				}
				catch(Exception ex){
					return ex.getMessage();
				}
			}
			else{
				return "false";
			}
		} 
		catch (Exception e) {
			return "false";
		}
	}
	
	@RequestMapping("logoff")
	public String showLoginForm(ModelMap model) {
		httpSession.removeAttribute("user");
		return "redirect:/home/index.htm";
	}
	
	@RequestMapping(value="login", method=RequestMethod.GET)
	public String showLoginForm(ModelMap model,
			@CookieValue(defaultValue="") String id, 
			@CookieValue(defaultValue="") String pw) {
		
		Customer user = new Customer();
		user.setId(id);
		user.setPassword(pw);
		model.addAttribute("user", user);
		model.addAttribute("message", "login.validate.init");
		return "login";
	}
	
	@RequestMapping(value="login2")
	@ResponseBody
	public String ajaxLogin(
			@RequestParam String id,
			@RequestParam String password,
			@RequestParam Boolean remember,
			HttpServletResponse response) {
		
		Customer user = new Customer();
		user.setId(id);
		
		Session session = sessionFactory.getCurrentSession();
		try {
			session.refresh(user);
			
			if(password.equals(user.getPassword())){
				
				if(!user.getActivated()){
					return "activate";
				}
				else{
					httpSession.setAttribute("user", user);
					// Ghi nhớ tài khoản bằng cookie
					int expiry = 30*24*60*60;
					
					Cookie ckiId = new Cookie("id", user.getId());
					Cookie ckiPw = new Cookie("pw", user.getPassword());
					if(!remember){
						expiry = 0;
					}
					ckiId.setMaxAge(expiry);
					ckiPw.setMaxAge(expiry);
					
					response.addCookie(ckiId);
					response.addCookie(ckiPw);
					return "true";
				}
			}
			else{
				return "false";
			}
		} 
		catch (Exception e) {
			return "false";
		}
	}
	
	@RequestMapping(value="login", method=RequestMethod.POST)
	public String submitLoginForm(ModelMap model,
			@ModelAttribute("user") Customer user,
			@RequestParam(defaultValue="false") boolean remember,
			HttpServletResponse response) {
		
		Customer user1 = new Customer();
		user1.setId(user.getId());
		
		Session session = sessionFactory.getCurrentSession();
		try {
			session.refresh(user1);
			
			if(user.getPassword().equals(user1.getPassword())){
				
				if(!user1.getActivated()){
					model.addAttribute("message", "login.validate.activate");
					return "login";
				}
				else{
					httpSession.setAttribute("user", user1);
					// Ghi nhớ tài khoản bằng cookie
					Cookie ckiId = new Cookie("id", user.getId());
					Cookie ckiPw = new Cookie("pw", user.getPassword());
					int expiry = 30*24*60*60;
					if(!remember){
						expiry = 0;
					}
					ckiId.setMaxAge(expiry);
					ckiPw.setMaxAge(expiry);
					
					response.addCookie(ckiId);
					response.addCookie(ckiPw);
					return "home";
				}
			}
			else{
				model.addAttribute("message", "login.validate.errorPw");
			}
		} 
		catch (Exception e) {
			model.addAttribute("message", "login.validate.errorId");
		}
		
		return "login";
	}
	
	@RequestMapping(value="register", method=RequestMethod.GET)
	public String register(ModelMap model) {
		model.addAttribute("user", new Customer());
		model.addAttribute("message", "register.validate.init");
		return "register";
	}
	
	@RequestMapping(value="register", method=RequestMethod.POST)
	public String register(ModelMap model, 
			@ModelAttribute("user") Customer user,
			@RequestParam("file_photo") MultipartFile file,
			HttpServletRequest request) {
		
		//Check if captcha code is correct
		if(!XCaptcha.isValid(request)){
			model.addAttribute("message", "register.validate.captcha");
			return "register";
		}

		Session session = sessionFactory.getCurrentSession();
		try {
			if(!file.isEmpty()){
				String fileName = file.getOriginalFilename();
				String path = application.getRealPath("images/customers/" + fileName);
				file.transferTo(new File(path));
				user.setPhoto(fileName);
			}
			
			session.save(user);
			model.addAttribute("message", "register.success");
			
			// Gửi mật khẩu qua email
			try{
				MimeMessage message = mailSender.createMimeMessage();
				
				MimeMessageHelper helper = new MimeMessageHelper(message, true);
				helper.setFrom("Web Master <projectjava26012015@gmail.com>");
				helper.setTo(user.getEmail());
				helper.setReplyTo("Web Master <projectjava26012015@gmail.com>");
				helper.setSubject("Welcome");
				String url = request.getRequestURL().toString().replace("register", "activate/"+user.getId());
				String text = "Click to <a href='"+url+"'>Activate</a> your account !";
				helper.setText(text, true);
				
				mailSender.send(message);
			}
			catch(Exception ex){
				model.addAttribute("message", "register.mailSender.error");
			}
		} 
		catch (Exception e) {
			model.addAttribute("message", "register.error");
		}
		
		return "register";
	}
}
