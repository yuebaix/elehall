package com.geercode.elehall.core;

import com.geercode.elehall.core.mode.sm.StatusState;

/**
 * <p>Description : 示例状态</p>
 * <p>Created on  : 2019-11-15 11:20:41</p>
 *
 * @author jerryniu
 */
public enum SampleStates implements StatusState {
	START("1", "开始"),
	PROCESS("2", "处理中"),
	END("3", "结束");
	String code;
	String name;
	SampleStates(String code, String name) {
		this.code = code;
		this.name = name;
	}
	public String getCode() {
		return this.code;
	}
	public String toString() {
		return "" + this.name() + "(" + this.code + ", " + this.name + ")";
	}
}
