package com.wx.decrypt.network.volley;

public interface IRequest {

	//每一个请求对应一个mSequence，具体参考request中的mSequence，用于限制fifo
	public int getSquence();
	public void setSquence(int squence);

	//请求类型，不同的接口对应一个type
	public String getUrl();
	public void setUrl(String url);
}
