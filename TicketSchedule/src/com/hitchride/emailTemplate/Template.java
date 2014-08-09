package com.hitchride.emailTemplate;

public class Template {
	protected String _subject;

	protected String _content;

	//protected String _head_name;

	//protected String _head_value;

	protected String _sendFrom;

	protected String _sendFromAlias;
	
	public Template(){
		_sendFrom = "no_reply@5udache.com";
		_sendFromAlias = "5udache";
	}
	
	public String getSubject(){
		return this._subject;
	}

	public String getContent(){
		return this._content;
	}

	public String getSendFrom(){
		return this._sendFrom;
	}

	public String getSendFromAlias(){
		return this._sendFromAlias;
	}
}
