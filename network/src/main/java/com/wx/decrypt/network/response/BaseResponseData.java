package com.wx.decrypt.network.response;

import com.google.gson.JsonElement;
import com.wx.decrypt.network.volley.IRequest;

public class BaseResponseData implements IRequest {

	private ResponseStatus status;

	/**
	 * 下面三个字段是后端返回
	 * 当前默认0是正常
	 */
	private Integer code;
	private String msg;
	public JsonElement result;

	/**
	 * brd.results解析之后塞入
	 */
	public Object data;

	public class ResponseStatus {
		private int code;
		private String message;

		public ResponseStatus(int code, String message) {
			this.code = code;
			this.message = message;
		}
	}

	private int mSquence = -1;
	private String mUrl = "";

	public int getCode() {
		if(status != null) {
			return status.code;
		}
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMessage() {
		if(status != null) {
			return status.message;
		}
		return msg;
	}

	@Override
	public int getSquence() {
		return mSquence;
	}

	@Override
	public String getUrl() {
		return mUrl;
	}

	@Override
	public void setSquence(int squence) {
		mSquence = squence;
	}

	@Override
	public void setUrl(String url) {
		mUrl = url;
	}

	public void setStatus(int code, String message) {
		status = new ResponseStatus(code, message);
	}

}
