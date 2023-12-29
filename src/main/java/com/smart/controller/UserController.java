package com.smart.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.smart.entity.Contact;
import com.smart.entity.User;
import com.smart.helper.Message;
import com.smart.repository.ContactRepository;
import com.smart.repository.UserRepository;

import jakarta.servlet.http.HttpSession;


@Controller
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ContactRepository contactRepository;
	
	
	//method to add common data
	@ModelAttribute
	public void addCommonData(Model model , Principal principal) 
	{
		String userName =principal.getName();
		System.out.println("User Name:"+userName);
		//getting the user email from database
		
		User user=  userRepository.getUserByUsername(userName);
		System.out.println("User" +user);
		//printing on the screen
		model.addAttribute("user", user);
		
	}
	
	//dashboard home
	@GetMapping("/index")
	public String dashboard(Model model, Principal principal)
	{
		
		return "normal/user_dashboard";
		
	}
	
	//open add contact form
	
	@GetMapping("/add-contact")
	public String openAddContactForm(Model model)
	{
		model.addAttribute("title", "Add Contact");
		
		model.addAttribute("contact", new Contact());
		
		return "normal/add_contact_form";
	}
	//processing contact
	@PostMapping("/process-contact")
	public String processcontact(
			@ModelAttribute Contact contact,
			/*@RequestParam("profileImage") MultipartFile file*/
			Principal principal, HttpSession session)
	{
			
		try {
			String name = principal.getName();
			User user= this.userRepository.getUserByUsername(name);
			
			/*
			 * //procesing file for the image. if(file.isEmpty()) {
			 * System.out.println("no file"); }else { //uploading file to folder and update
			 * contact table in database contact.setImage(file.getOriginalFilename());
			 * 
			 * File saveFile= new ClassPathResource("static/img").getFile();
			 * 
			 * Path path=
			 * Paths.get(saveFile.getAbsolutePath()+File.separator+file.getOriginalFilename(
			 * ));
			 * 
			 * Files. copy(file.getInputStream(), path,
			 * StandardCopyOption.REPLACE_EXISTING);
			 * System.out.println("Image is Uploaded"); }
			 */
			
			//to set the user id
			 contact.setUser(user); 
			
			user.getContacts().add(contact);
			
			//this line save the user into database.
			this.userRepository.save(user);
			
			
			System.out.println("Data"+contact);
			
			System.out.println("Added to database");
			
			//sucess message
			session.setAttribute("message", new Message("Contact is Added", "sucess"));
			
		} catch (Exception e) {
			System.out.println("ERROR"+e.getMessage());
			e.printStackTrace();
			//danger message
			session.setAttribute("message", new Message("Something went Wrong", "danger"));
		}
		return "normal/add_contact_form";
	}
	
	
	//show contact handler
	@GetMapping("/show-contacts")
	public String showContacts(Model m, Principal principal)
	
	{
		
		/*
		 * //list of the contacts 
		  String userName= principal.getName(); 
		 * User user = this.userRepository.getUserByUsername(userName);
		 * List<Contact> contacts = user.getContacts();
		 */
		String userName = principal.getName();
		User user= this.userRepository.getUserByUsername(userName);
		List<Contact> contacts=this.contactRepository.findContactsByUser(user.getId());
	 	
		m.addAttribute("contacts", contacts);
		
		
		m.addAttribute("title", "View Contact");
		return "normal/show_contacts"; 
	}
	//particular contact details
	@GetMapping("/{cid}/contact")
	public String showContactdetail(@PathVariable("cid") Integer cid,Model model,
			Principal principal)
	{
		System.out.println("Cid" +cid);
		Optional<Contact> contactOptional= this.contactRepository.findById(cid);
		Contact contact = contactOptional.get();
		
		String userName= principal.getName();
		User user = this.userRepository.getUserByUsername(userName);
		
		if(user.getId()==contact.getUser().getId()) 
			model.addAttribute("contact",contact);
		
	
		return "normal/contact_detail";
	}
	//delete contact handler
	@GetMapping("/delete/{cId}")
	public String deleteContact(@PathVariable("cId") Integer cid, Model model, HttpSession session, Principal principal)
	{
	Optional<Contact> contactOptional= this.contactRepository.findById(cid);
	Contact contact = contactOptional.get();
	
	
	//check
	
	User user = this.userRepository.getUserByUsername(principal.getName());
	
	user.getContacts().remove(contact);
	
	this.userRepository.save(user);
	/* contact.setUser(null); */
	
	/* this.contactRepository.delete(contact); */
	
	System.out.println("DELETED");
	/*
	 * session.setAttribute("message", new Message("Contact deleted sucessfully...",
	 * "success"));
	 */
		
	return "redirect:/user/show-contacts";
	}
	
	//open update form handler
	@PostMapping("/update-contact/{cid}")
	public String updateForm(@PathVariable("cid") Integer cid,Model m)
	{
		m.addAttribute("title", "update contact");
		
		Contact contact=this.contactRepository.findById(cid).get();
		
		m.addAttribute("contact",contact);	
		
		return "normal/update_form";
	}
	
	//update contacts handler	
	@PostMapping("/process-update")
	public String updateHandler(@ModelAttribute Contact contact
			, Model m, HttpSession session, Principal principal)
	{
		try {
			//image
		
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		User user = this.userRepository.getUserByUsername(principal.getName());
		
		contact.setUser(user);
		this.contactRepository.save(contact);
		session.setAttribute(" Your contact is updated...", "sucsess");
		
		
		
		System.out.println("Contact Name" +contact.getName());
		System.out.println("contact id" +contact.getCid());
		
		return "redirect:/user/"+contact.getCid()+"/contact";
	}
	
	//your profile handler
	@GetMapping("/profile")
	public String yourProfile( Model model) 
	{
		model.addAttribute("title", "Profile page");
		
		return "normal/profile";
	}
	
	
	
	
	
	
	
	
}
