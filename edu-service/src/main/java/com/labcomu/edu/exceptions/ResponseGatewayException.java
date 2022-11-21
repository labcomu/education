package com.labcomu.edu.exceptions;

import org.springframework.http.HttpStatus;

public class ResponseGatewayException extends Exception {
	private static final long serialVersionUID = -2032008774752551936L;

	private HttpStatus status;

	public ResponseGatewayException(String msg, HttpStatus status) {
		super(msg);
		this.status = status;
	}

	public HttpStatus getStatus() {
		return status;
	}

	public void setStatus(HttpStatus status) {
		this.status = status;
	}
}
