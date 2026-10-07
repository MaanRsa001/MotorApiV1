package com.maan.eway.bean;

import java.io.Serializable;

import jakarta.persistence.*;
import jakarta.persistence.*;
import jakarta.persistence.*;
import jakarta.persistence.*;
import jakarta.persistence.*;
import jakarta.persistence.*;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@DynamicInsert
@DynamicUpdate
@Builder
@Table(name="seq_archid")
public class SeqArchId implements Serializable {
	 
	private static final long serialVersionUID = 1L;
	 
	    //--- ENTITY PRIMARY KEY 
	    @Id
	    @GeneratedValue(strategy=GenerationType.IDENTITY)
	    @Column(name="ARCH_ID", nullable=false)
	    private Long       archId ;

	    //--- ENTITY DATA FIELDS 

	    //--- ENTITY LINKS ( RELATIONSHIP )


	}

