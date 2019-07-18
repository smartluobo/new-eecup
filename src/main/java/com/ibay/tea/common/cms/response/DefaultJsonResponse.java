package com.ibay.tea.common.cms.response;

import java.io.Serializable;

public class DefaultJsonResponse<T> extends BaseResponse<T> implements Serializable {

	private static final long serialVersionUID = 5862483697124950176L;

	public static final String []API_REQUEST_SUCCESS = {"0", "成功执行"};

	public DefaultJsonResponse() {
        this.setErrcode(API_REQUEST_SUCCESS[0]);
		this.setDescription(API_REQUEST_SUCCESS[1]);
	}
}
