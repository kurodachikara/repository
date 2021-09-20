package jp.kuroda.sampleBlog.controller;


import java.io.IOException;
import java.util.List;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jp.kuroda.sampleBlog.model.Blog;
import jp.kuroda.sampleBlog.model.Comment;
import jp.kuroda.sampleBlog.model.Person;
import jp.kuroda.sampleBlog.model.UserAccount;
import jp.kuroda.sampleBlog.service.BlogService;
import jp.kuroda.sampleBlog.service.PersonService;

@Controller
@RequestMapping("/person")
public class PersonController {
	@Autowired
	private PersonService personService;
	@Autowired
	private BlogService blogService;

	
	@ModelAttribute("person")
	public Person currentPerson(UserAccount userAccount) {
		return userAccount.getPerson();
	}
	
	//ログイントップ画面
	@GetMapping("/index")
	public String index(Model model) {
		List<Blog> blogs=blogService.getBlogList();
		model.addAttribute("blogs",blogs);
		return "person/index";
	}
	
	//プロフィール編集
	@GetMapping("/edit")
	public String edit(Person person,Model model) throws IOException{
		List<Blog> blogs=blogService.getMyBlogs(person);
		model.addAttribute("blogs",blogs);
		model.addAttribute("base64icon",person.getIcon_base64_str());
		return"person/form";
	}
	@PostMapping("/edit")
	public String edit(@Valid Person person,BindingResult bindingResult) throws IOException {
		if(bindingResult.hasErrors()) {
			return"person/form";
		}
		personService.updatePerson(person);
		return"redirect:/person/index";
	}
	
	//ブログ投稿
	@GetMapping("/create")
	public String createBlog(Blog blog,Person person) {
		blog.setPerson(person);
		return"person/create";
	}
	@PostMapping("/create")
	public String createBlog(@Valid Blog blog,BindingResult bindingResult) {
		if(bindingResult.hasErrors()) {
			return "person/create";
		}
		blogService.createBlog(blog);
		return"redirect:/person/blog/"+blog.getId();
	}
	//ブログ閲覧
	@GetMapping("/blog/{blogId}")
	public String showBlog(@PathVariable("blogId") Blog blog,Person person,Model model){
	    model.addAttribute("base64image",blog.getBase64_str());
		model.addAttribute("blog",blog);
		Comment comment=personService.getComment(blog,person);
		model.addAttribute("comment",comment);
		return"person/blog";
	}
	
	//ブログ編集
	@GetMapping("/blog/{blogId}/edit")
	public String editBlog(@PathVariable("blogId")Blog blog,Model model) {
		model.addAttribute("base64image",blog.getBase64_str());
		model.addAttribute("blog",blog);
		return"person/create";
	}
	@PostMapping("/blog/{blogId}/edit")
	public String editBlog(@Valid Blog blog,Comment comment,BindingResult bindingResult) {
		if(bindingResult.hasErrors()) {
			return"person/create";
		}
		blogService.editBlog(blog,comment);
		return"redirect:/person/edit";
	}
	@GetMapping("/blog/{id}/deleteimage")
	public String delete(Blog blog) {
		blogService.deleteImage(blog);
		return"redirect:/create";
	}
	
	//ブログ削除
	@Transactional
	@GetMapping("/blog/{id}/delete")
	public String deleteMyBlog(Blog blog) {
		blogService.deleteBlog(blog);
		return"redirect:/person/index";
	}
	//コメント投稿
	@PostMapping("/comment/blog/{blogId}")
	public String commentBlog(Comment comment) {
		personService.createComment(comment);
		return"redirect:/person/blog/"+comment.getBlog().getId();
	}
	//コメント削除
	@Transactional
	@GetMapping("/comment/{id}/delete")
	public String deleteMyComment(Comment comment) {
		personService.deleteComment(comment);
		return"redirect:/person/index";
	}
	
}
