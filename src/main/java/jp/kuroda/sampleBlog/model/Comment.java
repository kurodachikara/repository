package jp.kuroda.sampleBlog.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import lombok.Data;

@Entity
@Data
public class Comment {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer id;
	
	private String commenting;
	
	private String postDateTime;
	
	@ManyToOne
	private Blog blog;
	@ManyToOne
	private Person person;
}
