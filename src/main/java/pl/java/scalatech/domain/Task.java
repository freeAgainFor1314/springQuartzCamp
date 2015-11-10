package pl.java.scalatech.domain;

import static javax.persistence.GenerationType.AUTO;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.Data;

@Entity
@Data
public class Task {
    @Id
    @GeneratedValue(strategy=AUTO)
    private Long id;

    @Column(name="taskName")
    private String name;
    
    @Column(name="launchTime")
    private Date date = new Date();
}
