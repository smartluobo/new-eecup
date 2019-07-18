package com.ibay.tea.common.utils;

import java.io.Serializable;

public class PageUtil implements Serializable {

	/**
     * page number 页数从0开始
	 */
	private Integer page = 0;
	
	/**
	 * page size 每页显示的记录数
	 */
	private Integer size = 10;
	
	/**
	 * order Key 排序字段名称 对应数据库属性
	 */
	private String key;
	
	/**
	 * order value 排序规则 desc 降序, asc 升序
	 */
	private String value;
	
	/**
	 * 查询次数 直接返回 无需操作
	 */
	private String echo;
	
	/**
	 * 搜索框的值
	 */
	private String search;

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getEcho() {
		return echo;
	}

	public void setEcho(String echo) {
		this.echo = echo;
	}

	public String getSearch() {
		return search;
	}

	public void setSearch(String search) {
		this.search = search;
	}

	@Override
	public String toString() {
		return "PageUtil [page=" + page + ", size=" + size + ", key=" + key
				+ ", value=" + value + ", echo=" + echo + ", search=" + search
				+ "]";
	}
	
	
}
