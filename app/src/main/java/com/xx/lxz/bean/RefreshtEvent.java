package com.xx.lxz.bean;

/**
 * 刷新事件
 */
public class RefreshtEvent {
	  
    private RefreshModel mRefreshPosition;
    public RefreshtEvent(RefreshModel mRefresh) {
        mRefreshPosition = mRefresh;
    }  
    public RefreshModel getMrefreshPosition(){
        return mRefreshPosition;
    }  
}  