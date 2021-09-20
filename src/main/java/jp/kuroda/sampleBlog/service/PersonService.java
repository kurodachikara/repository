package jp.kuroda.sampleBlog.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jp.kuroda.sampleBlog.model.Blog;
import jp.kuroda.sampleBlog.model.Comment;
import jp.kuroda.sampleBlog.model.Person;
import jp.kuroda.sampleBlog.repository.CommentRepository;
import jp.kuroda.sampleBlog.repository.PersonRepository;

@Service
public class PersonService {
	@Autowired
	private PersonRepository personRepository;
	
	@Autowired
	private CommentRepository commentRepository;
	
	public Person createPerson() {
		Person person=new Person();
		person.setName("名前を設定してください");
		person.setBirthday(LocalDate.of(2021, 1, 1));
		person.setHobby("趣味・特技を設定してください");
		person.setWork("職業を設定してください");
		personRepository.save(person);
		return person;
	}
	public void updatePerson(Person person) {
	if(!person.getIcon_file().isEmpty()) {
		try {
			MultipartFile file=person.getIcon_file();
			StringBuffer data=new StringBuffer();
			String base64=new String(Base64.encodeBase64(file.getBytes()),"ASCII");
			data.append("data:image/jpeg;base64,");
			data.append(base64);
			person.setIcon_base64_str(data.toString());
		}catch(Exception e) {	
		}
	}
	personRepository.save(person);
	}
	
	public Comment getComment(Blog blog,Person person) {
		Comment comment=new Comment();
		comment.setBlog(blog);
		comment.setPerson(person);
		return comment;
	}
	public Comment createComment(Comment comment) {
		comment.setPostDateTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")));
		commentRepository.save(comment);
		return comment;
	}
	public void deleteComment(Comment comment) {
		commentRepository.deleteById(comment.getId());
	}
}
