package com.maan.eway.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR, reason = "UnExpected Data")
public class DataStorageException extends Exception {

	private String messege;

	/**
	 * 
	 */
	private static final long serialVersionUID = -1341908625899279865L;

	public DataStorageException() {

	}

	public DataStorageException(String messege) {

		this.messege = messege;
	}

}
