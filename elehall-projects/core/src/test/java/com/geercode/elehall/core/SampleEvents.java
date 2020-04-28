package com.geercode.elehall.core;

import com.geercode.elehall.core.mode.sm.StatusEvent;

/**
 * <p>Description : 示例状态事件</p>
 * <p>Created on  : 2019-11-15 11:20:41</p>
 *
 * @author jerryniu
 */
public enum SampleEvents implements StatusEvent<LockDomain> {
	//打开
	OPEN,
	//关闭
	CLOSE,
	//取消
	CALLOFF;

	private LockDomain lockDomain;

	@Override
	public void setContext(LockDomain lockDomain) {
		this.lockDomain = lockDomain;
	}

	@Override
	public LockDomain getContext() {
		return lockDomain;
	}
}
